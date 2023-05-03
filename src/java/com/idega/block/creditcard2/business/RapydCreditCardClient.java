package com.idega.block.creditcard2.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
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
import com.idega.block.creditcard.model.SaleOption;
import com.idega.block.creditcard2.data.beans.RapydAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.RapydMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.restful.util.ConnectionUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.RequestUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.sun.jersey.api.client.ClientResponse;

public class RapydCreditCardClient implements CreditCardClient {

	private static final Logger LOGGER = Logger.getLogger(RapydCreditCardClient.class.getName());

	private static final Gson GSON = new Gson();

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
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String doSale(
			String nameOnCard,
			String cardnumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber,
			SaleOption... options
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
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

		//	Unique access key provided by Rapyd for each authorized user
		String accessKey = merchant.getPassword();
		params.add(new AdvancedProperty("access_key", accessKey, "access_key"));

		//	Indicates that the data appears in JSON format. Always set to application/json
		params.add(new AdvancedProperty(RequestUtil.HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON, RequestUtil.HEADER_CONTENT_TYPE));

		//	Salt for the request signature. A random string for each request. Length: 8-16 digits, letters and special characters
		String salt = StringUtil.getAlphaNumericString(16);
		params.add(new AdvancedProperty("salt", salt, "salt"));

		//	The time of the request, in Unix time (seconds)
		long timestamp = System.currentTimeMillis() / 1000L;
		String timestampValue = Long.toString(timestamp);
		params.add(new AdvancedProperty("timestamp", timestampValue, "timestamp"));

		//	Signature calculated for each message individually.
		//	For REST requests, see https://docs.rapyd.net/build-with-rapyd/reference/authentication#request-signatures
		//	signature = BASE64 ( HASH ( http_method + url_path + salt + timestamp + access_key + secret_key + body_string ) )

		//	For webhooks, see https://docs.rapyd.net/build-with-rapyd/reference/authentication#webhook-authentication
		//	signature = BASE64 ( HASH ( url_path + salt + timestamp + access_key + secret_key + body_string ) )
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

		String url = null, json = null, errorMessage = null, responseData = null;
		try {
			String urlPath = "/v1/checkout";
			String httpMethod = HttpMethod.POST;

			url = this.url.concat(urlPath);
			json = GSON.toJson(request);
			ClientResponse response = ConnectionUtil.getInstance().getResponseFromREST(
					url,
					Long.valueOf(json.length()),
					MediaType.APPLICATION_JSON,
					httpMethod,
					json,
					getHeaderParams(httpMethod, urlPath, json),
					null
			);

			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				Integer status = response == null ? null : response.getStatus();
				errorMessage = response == null ? null : StringHandler.getContentFromInputStream(response.getEntityInputStream());
				LOGGER.warning("Error sending " + json + " to Rapyd (" + url + "). Response (" + response +
						") or response status is not OK: " + (status == null ? "unknown" : status) + ". Response message:\n" +
						(errorMessage == null ? "not provided" : errorMessage));
				return null;
			}

			responseData = StringHandler.getContentFromInputStream(response.getEntityInputStream());
			LOGGER.info("Response data from Rapyd (" + url + ") for " + json + ":\n" + responseData);
			HostedCheckoutPageResponse result = GSON.fromJson(responseData, HostedCheckoutPageResponse.class);
			return result;
		} catch (Exception e) {
			String error = "Error getting hosted checkout page for " + json + ". " + errorMessage;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		}

		return null;
	}

}