package com.idega.block.creditcard2.business;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.business.VerificationData;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard.model.CaptureResult;
import com.idega.block.creditcard.model.HostedCheckoutPageRequest;
import com.idega.block.creditcard.model.HostedCheckoutPageResponse;
import com.idega.block.creditcard.model.PaymentIntegrationResult;
import com.idega.block.creditcard.model.SaleOption;
import com.idega.block.creditcard.model.rapyd.CreatePayment;
import com.idega.block.creditcard.model.rapyd.CreateRefund;
import com.idega.block.creditcard.model.rapyd.Data;
import com.idega.block.creditcard.model.rapyd.Datum;
import com.idega.block.creditcard.model.rapyd.PaymentMethodsResponse;
import com.idega.block.creditcard.model.rapyd.PaymentResult;
import com.idega.block.creditcard.model.rapyd.RapydException;
import com.idega.block.creditcard2.data.beans.RapydAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.RapydMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.idegaweb.IWMainApplication;
import com.idega.restful.util.ConnectionUtil;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.RequestUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.URIUtil;
import com.sun.jersey.api.client.ClientResponse;

public class RapydCreditCardClient implements CreditCardClient {

	private static final Logger LOGGER = Logger.getLogger(RapydCreditCardClient.class.getName());

	private CreditCardMerchant merchant;

	private RapydAuthorisationEntry auth;

	private String url;

	public RapydCreditCardClient(CreditCardMerchant merchant) {
		this.merchant = merchant;

		if (merchant instanceof RapydMerchant && CreditCardMerchant.MERCHANT_TYPE_RAPYD.equals(merchant.getType())) {
			String url = ((RapydMerchant) merchant).getMerchantUrl();
			this.url = StringUtil.isEmpty(url) ? getDefaultURL() : url;
		}
	}

	private String getDefaultURL() {
		String url = merchant == null ? null : merchant.getLocation();
		if (!StringUtil.isEmpty(url)) {
			return url;
		}

		return CreditCardUtil.isTestEnvironment() ?
				"https://sandboxapi.rapyd.net" :
				"https://api.rapyd.net";
	}

	@Override
	public String getExpireDateString(String month, String year) {
		return year + month;
	}

	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("Rapyd");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}


	private boolean hasSaleOption(SaleOption[] options, SaleOption option) {
		if (ArrayUtil.isEmpty(options) || option == null) {
			return false;
		}

		return Arrays.asList(options).contains(option);
	}

	@Override
	public String doSale(
			String nameOnCard,
			String number,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber,
			SaleOption... options
	) throws CreditCardAuthorizationException {
		if (StringUtil.isEmpty(nameOnCard)) {
			throw new RapydException("Name must be provided");
		}
		if (StringUtil.isEmpty(number)) {
			throw new RapydException("Card number must be provided");
		}
		if (StringUtil.isEmpty(monthExpires)) {
			throw new RapydException("Expiration month must be provided");
		}
		if (StringUtil.isEmpty(yearExpires)) {
			throw new RapydException("Expiration year must be provided");
		}
		if (StringUtil.isEmpty(ccVerifyNumber)) {
			throw new RapydException("CVV must be provided");
		}
		if (amount < 0) {
			throw new RapydException("Invalid amount");
		}
		if (StringUtil.isEmpty(currency)) {
			throw new RapydException("Currency must be provided");
		}

		String error = "Error making sale with card " + CreditCardUtil.getMaskedCreditCardNumber(number) + " " + monthExpires + " (MM)/" + yearExpires + " (YY) issued for " +
				nameOnCard + ". Amount: " + amount + " " + currency + ". Reference number: " + referenceNumber;
		PaymentResult result = null;
		try {
			String country = merchant.getCountry();
			if (StringUtil.isEmpty(country)) {
				Locale locale = IWMainApplication.getDefaultIWMainApplication().getDefaultLocale();
				country = locale == null ? null : locale.getCountry();
			}
			PaymentMethodsResponse paymentMethods = getAvailablePaymentMethods(country, currency);
			if (paymentMethods == null || !isSuccess(paymentMethods.getStatus()) || ListUtil.isEmpty(paymentMethods.getData())) {
				String message = "No payment methods available for merchant " + merchant + ". " + error;
				LOGGER.warning(message);

				throw new RapydException(message, paymentMethods);
			}

			String type = null, cardBrand = CreditCardUtil.getCardBrand(number);
			List<Datum> data = paymentMethods.getData();
			for (Iterator<Datum> iter = data.iterator(); (StringUtil.isEmpty(type) && iter.hasNext());) {
				Datum datum = iter.next();
				String category = datum == null ? null : datum.getCategory();
				if (StringUtil.isEmpty(category) || !CreditCardConstants.CARD.equals(category)) {
					continue;
				}

				String name = datum.getName();
				if (StringUtil.isEmpty(name) || StringUtil.isEmpty(cardBrand)) {
					type = datum.getType();

				} else if (name.toLowerCase().equals(cardBrand.toLowerCase())) {
					type = datum.getType();
				}
			}
			if (StringUtil.isEmpty(type)) {
				String message = "No payment methods for cards available for merchant " + merchant + ". Received payment methods: " + paymentMethods + ". " + error;
				LOGGER.warning(message);

				throw new RapydException(message, paymentMethods);
			}

			boolean skip3DAuth = hasSaleOption(options, SaleOption.SKIP_3D_AUTH);
			CreatePayment payment = new CreatePayment(
					Double.valueOf(amount).intValue(),
					currency,
					type,
					nameOnCard,
					number,
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					referenceNumber,
					skip3DAuth
			);
			result = getResponseFromRapyd("/v1/payments", HttpMethod.POST, payment, PaymentResult.class);
			Data responseData = result == null ? null : result.getData();
			String redirect = responseData == null ? null : responseData.getRedirect_url();
			if (
					!isSuccess(result == null ? null : result.getStatus()) ||
					responseData == null ||
					StringUtil.isEmpty(responseData.getStatus()) ||
					!CreditCardConstants.CLOSED.equals(responseData.getStatus())
			) {
				if (!StringUtil.isEmpty(redirect)) {
					LOGGER.info("Must redirect to " + redirect + " to complete sail!");
					return redirect;
				}

				LOGGER.warning(error);
				throw new RapydException(error, result);
			}

			String authCode = responseData.getAuth_code();
			if (!StringUtil.isEmpty(authCode) && !authCode.equalsIgnoreCase("null")) {
				//FIXME: We need to have payment id in all the cases - to save for possible refund.
				String authCodeWithPaymentId = authCode + CoreConstants.HASH + responseData.getId();
				return authCodeWithPaymentId;
			}

			if (!StringUtil.isEmpty(redirect)) {
				LOGGER.info("Redirect to " + redirect + " to complete sail!");
				return redirect;
			}

			return responseData.getId();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			throw new RapydException(e, error, result);
		}
	}

	private PaymentMethodsResponse getAvailablePaymentMethods(String country, String currency) throws CreditCardAuthorizationException {
		if (StringUtil.isEmpty(country) || StringUtil.isEmpty(currency)) {
			return null;
		}

		try {
			return getResponseFromRapyd(
					"/v1/payment_methods/countries/",
					HttpMethod.GET,
					null,
					PaymentMethodsResponse.class,
					Arrays.asList(
							new AdvancedProperty(country, country, "country")
					),
					new AdvancedProperty(currency, currency, "currency")
			);
		} catch (Exception e) {
			String error = "Error getting payment methods for country " + country + " and currency " + currency;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			throw new RapydException(e, error);
		}
	}

	@Override
	public String creditcardAuthorization(
			String nameOnCard,
			String cardnumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public boolean supportsDelayedTransactions() {
		return false;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		if (auth == null) {
			auth = new RapydAuthorisationEntry();
		}
		return auth;
	}

	@Override
	public void setAuthorizationEntry(CreditCardAuthorizationEntry entry) {
		if (entry instanceof RapydAuthorisationEntry) {
			this.auth = (RapydAuthorisationEntry) entry;
		}
	}

	@Override
	public String getAuthorizationNumber(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String getPropertiesToCaptureWebPayment(String currency, double amount, Timestamp timestamp, String reference, String approvalCode) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public CaptureResult getAuthorizationNumberForWebPayment(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public AuthEntryData doSaleWithCardToken(
			String cardToken,
			String merchantReferenceData,
			double amount,
			String currency,
			String referenceNumber,
			Object parentPaymentPK
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public VirtualCard doCreateVirtualCard(
			String cardNumber,
			Integer monthExpires,
			Integer yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			VerificationData verificationData
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public VirtualCard doUpdateCard(
			VirtualCard card,
			Integer monthExpires,
			Integer yearExpires,
			String firstTransactionLifecycleId
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String doVerifyCard(
			String cardNumber,
			Integer monthExpires,
			Integer yearExpires,
			double amount,
			String currency,
			String verificationType,
			String referenceNumber
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	private List<AdvancedProperty> getHeaderParams(String httpMethod, String url, String json) {
		List<AdvancedProperty> params = new ArrayList<>();

		String accessKey = merchant.getPassword();
		params.add(new AdvancedProperty("access_key", accessKey, "access_key"));

		params.add(new AdvancedProperty(RequestUtil.HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON, RequestUtil.HEADER_CONTENT_TYPE));

		String salt = StringUtil.getAlphaNumericString(16);
		params.add(new AdvancedProperty("salt", salt, "salt"));

		long timestamp = System.currentTimeMillis() / 1000L;
		String timestampValue = Long.toString(timestamp);
		params.add(new AdvancedProperty("timestamp", timestampValue, "timestamp"));

		json = json == null ? CoreConstants.EMPTY : json;
		String secretKey = merchant.getSharedSecret();
		String toEnc =
				(StringUtil.isEmpty(httpMethod) ? CoreConstants.EMPTY : httpMethod.toLowerCase())
				.concat(url)
				.concat(salt)
				.concat(timestampValue)
				.concat(accessKey)
				.concat(secretKey)
				.concat(json);
        String hashCode = hmacDigest(toEnc, secretKey, "HmacSHA256");
        String signature = Base64.getEncoder().encodeToString(hashCode.getBytes());
        params.add(new AdvancedProperty("signature", signature, "signature"));

		return params;
	}

	private String hmacDigest(String msg, String keyString, String algo) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("ASCII"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[]bytes = mac.doFinal(msg.getBytes(CoreConstants.ENCODING_UTF8));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to hmacDigest message '" + msg + "' with key '" + keyString + "' and algo " + algo, e);
		}
		return digest;
	}

	@Override
	public HostedCheckoutPageResponse getHostedCheckoutPage(HostedCheckoutPageRequest request) throws CreditCardAuthorizationException {
		if (request == null) {
			LOGGER.warning("Request is not provided");
			return null;
		}

		HostedCheckoutPageResponse response = null;
		try {
			response = getResponseFromRapyd("/v1/checkout", HttpMethod.POST, request, HostedCheckoutPageResponse.class);
			return response;
		} catch (Exception e) {
			String error = "Error getting hosted checkout page for " + request;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			throw new RapydException(e, error, response);
		}
	}

	private boolean isSuccess(com.idega.block.creditcard.model.Status status) {
		if (
				status == null ||
				StringUtil.isEmpty(status.getStatus()) ||
				!CreditCardConstants.SUCCESS.equals(status.getStatus())
		) {
			return false;
		}

		return true;
	}

	private <T extends PaymentIntegrationResult> T getResponseFromRapyd(
			String uri,
			String method,
			Serializable request,
			Class<T> responseType
	) throws CreditCardAuthorizationException {
		return getResponseFromRapyd(uri, method, request, responseType, null);
	}

	private <T extends PaymentIntegrationResult> T getResponseFromRapyd(
			String uri,
			String method,
			Serializable request,
			Class<T> responseType,
			List<AdvancedProperty> pathParams,
			AdvancedProperty... queryParams
	) throws CreditCardAuthorizationException {
		T result = null;
		Integer status = null;
		ClientResponse response = null;
		String url = null, json = null, errorMessage = null, responseData = null;
		try {
			if (!ListUtil.isEmpty(pathParams)) {
				for (AdvancedProperty param: pathParams) {
					String value = param == null ? null : param.getValue();
					if (StringUtil.isEmpty(value)) {
						continue;
					}

					if (!uri.endsWith(CoreConstants.SLASH)) {
						uri = uri.concat(CoreConstants.SLASH);
					}
					uri = uri.concat(value);
				}
			}
			if (!ArrayUtil.isEmpty(queryParams)) {
				URIUtil uriUtil = new URIUtil(uri);
				for (AdvancedProperty param: queryParams) {
					String value = param == null ? null : param.getValue();
					if (StringUtil.isEmpty(value)) {
						continue;
					}

					uriUtil.setParameter(param.getName(), param.getValue());
				}
				uri = uriUtil.getUri();
			}
			url = this.url.concat(uri);

			json = request == null ? null : CreditCardConstants.GSON.toJson(request);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					url,
					StringUtil.isEmpty(json) ? null : Long.valueOf(json.length()),
					MediaType.APPLICATION_JSON,
					method,
					json,
					getHeaderParams(method, uri, json),
					null
			);

			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				status = response == null ? null : response.getStatus();
				errorMessage = response == null ? null : StringHandler.getContentFromInputStream(response.getEntityInputStream());
				LOGGER.warning("Error " + (HttpMethod.POST.equals(method) ? "sending " + json + " to" : "getting from") + " Rapyd (" + url + "). Response (" + response +
						") or response status is not OK: " + (status == null ? "unknown" : status) + ". Response message:\n" +
						(errorMessage == null ? "not provided" : errorMessage));
				return null;
			}

			responseData = StringHandler.getContentFromInputStream(response.getEntityInputStream());
			LOGGER.info("Response data from Rapyd (" + url + ", HTTP method " + method + ")" + (StringUtil.isEmpty(json) ? CoreConstants.EMPTY : " for " + json) + ":\n" + responseData);
			result = CreditCardConstants.GSON.fromJson(responseData, responseType);
			return result;
		} catch (Exception e) {
			status = status == null ?
					response == null ? null : response.getStatus() :
					status;
			String error = StringUtil.isEmpty(errorMessage) ?
					"Error " + (HttpMethod.POST.equals(method) ? "sending " + json + " to" : "getting from") + " Rapyd (" + url + "). Response (" + response +
					") or response status is not OK: " + (status == null ? "unknown" : status) + ". Response message:\n" +
					(errorMessage == null ? "not provided" : errorMessage) :
					errorMessage;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			throw new RapydException(e, error, result);
		}
	}


	/**
	 * Do refund
	 * @param cardnumber - NOT USED
	 * @param monthExpires - NOT USED
	 * @param yearExpires - NOT USED
	 * @param ccVerifyNumber - NOT USED
	 * @param amount - Amount for refund. If zero or null - all the original amount will be refunded.
	 * @param currency - Currency.
	 * @param parentDataPK - NOT USED
	 * @param extraField - MANDATORY unique RAPYD payment id received while making the payment as: "payment_0dd0ddfe830c8fa0ba35e3d7370b2348"
	 * @return ID of refund action
	 * @throws CreditCardAuthorizationException
	 */
	@Override
	public String doRefund(
			String cardnumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			Object parentDataPK,
			String extraField
	) throws CreditCardAuthorizationException {
		if (StringUtil.isEmpty(extraField)) {
			throw new RapydException("Extra field should be provided. It should hold the unique payment id, which was received while making the payment.");
		}

		String error = "Error making the refund for RAPYD payment id: " + extraField
			+ ". Amount: " + amount + " " + currency;

		PaymentResult result = null;
		try {
			String merchantReferenceId = UUID.randomUUID().toString();

			CreateRefund refundData = new CreateRefund(
					extraField,
					amount > 0 ? Double.valueOf(amount).toString() : null,
					currency,
					merchantReferenceId,
					null
			);
			result = getResponseFromRapyd("/v1/refunds", HttpMethod.POST, refundData, PaymentResult.class);
			Data responseData = result == null ? null : result.getData();
			if (
					!isSuccess(result == null ? null : result.getStatus()) ||
					responseData == null ||
					StringUtil.isEmpty(responseData.getStatus()) ||
					!CreditCardConstants.COMPLETED.equalsIgnoreCase(responseData.getStatus())
			) {
				if (responseData != null) {
					if (!StringUtil.isEmpty(responseData.getStatus())) {
						error = error + ". RAPYD response status: " + responseData.getStatus();
					}
					if (!StringUtil.isEmpty(responseData.getFailure_reason())) {
						error = error + ". Failure reason: " + responseData.getFailure_reason();
					}
				}

				LOGGER.warning(error);
				throw new RapydException(error, result);
			}

			return responseData.getId();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			throw new RapydException(e, error, result);
		}
	}



}