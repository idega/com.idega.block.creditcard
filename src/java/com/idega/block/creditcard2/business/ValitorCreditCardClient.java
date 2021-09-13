package com.idega.block.creditcard2.business;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.business.ValitorPayException;
import com.idega.block.creditcard.business.VerificationData;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard.model.CaptureResult;
import com.idega.block.creditcard.model.SaleOption;
import com.idega.block.creditcard.model.ValitorPayCardVerificationData;
import com.idega.block.creditcard.model.ValitorPayCardVerificationResponseData;
import com.idega.block.creditcard.model.ValitorPayPaymentData;
import com.idega.block.creditcard.model.ValitorPayResponseData;
import com.idega.block.creditcard.model.ValitorPayVirtualCardAdditionalData;
import com.idega.block.creditcard.model.ValitorPayVirtualCardData;
import com.idega.block.creditcard2.data.beans.ValitorAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.creditcard2.data.dao.impl.ValitorAuthorisationEntryDAO;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.restful.util.ConnectionUtil;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.IOUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.RequestUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;
import com.idega.util.expression.ELUtil;
import com.sun.jersey.api.client.ClientResponse;

import is.valitor.api.fyrirtaekjagreidslur.Fyrirtaekjagreidslur;
import is.valitor.api.fyrirtaekjagreidslur.FyrirtaekjagreidslurSoap;
import is.valitor.api.fyrirtaekjagreidslur.HeimildSkilabod;
import is.valitor.api.fyrirtaekjagreidslur.SyndarkortnumerSkilabod;

public class ValitorCreditCardClient implements CreditCardClient {

	private static final Logger LOGGER = Logger.getLogger(ValitorCreditCardClient.class.getName());

	@Autowired
	private ValitorAuthorisationEntryDAO authDAO = null;

	private CreditCardMerchant merchant;
	private ValitorAuthorisationEntry auth = null;

	private String url;

	public static final String DEFAULT_URL = "https://valitorpay.com/Payment";

	public ValitorCreditCardClient(CreditCardMerchant merchant) {
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(merchant.getType())) {
			String url = ((ValitorMerchant) merchant).getMerchantUrl();
			this.url = StringUtil.isEmpty(url) ? ValitorCreditCardClient.DEFAULT_URL : ((ValitorMerchant) merchant).getMerchantUrl();
			this.merchant = merchant;
		}
	}

	@Override
	public String getExpireDateString(String month, String year) {
		return year + month;
	}

	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("Valitor");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}

	public String getVirtualCardNumber(String cardnumber, String expirationDate, String ccv) throws CreditCardAuthorizationException {
		try {
			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			SyndarkortnumerSkilabod result = port.faSyndarkortnumer(
					merchant.getUser(),
					merchant.getPassword(),
					merchant.getExtraInfo(),
					merchant.getMerchantID(),
					merchant.getTerminalID(),
					cardnumber,
					expirationDate,
					ccv,
					null
			);

			if (result.getVillunumer()==0) {
				return result.getSyndarkortnumer();
			} else {
				throw new CreditCardAuthorizationException("ERROR: " + result.getVilluskilabod(), result.getVillunumer()+"");
			}

		} catch (Exception e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
	}

	@Override
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, Object parentDataPK, String extraField)
					throws CreditCardAuthorizationException {

		try {
			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			HeimildSkilabod result = port.faEndurgreitt(
					merchant.getUser(),
					merchant.getPassword(),
					merchant.getExtraInfo(),
					merchant.getMerchantID(),
					merchant.getTerminalID(),
					cardnumber,
					amount+"",
					currency,
					null
			);

			ValitorAuthorisationEntry auth = new ValitorAuthorisationEntry();
			auth.setAuthCode(result.getKvittun().getHeimildarnumer());
			auth.setAmount(amount);
			auth.setCardNumber(cardnumber);
			auth.setCurrency(currency);
			auth.setServerResponse(result.getKvittun().getFaerslunumer());
			auth.setCardNumber(result.getKvittun().getKortnumer());
			auth.setAuthCode(result.getKvittun().getHeimildarnumer());
			if (result.getVillunumer()!=0){
				auth.setErrorNumber(result.getVillunumer()+"");
				auth.setErrorText(result.getVilluskilabod());
			}
			auth.setParent((ValitorAuthorisationEntry)parentDataPK);
			auth.setMerchant((ValitorMerchant) merchant);
			getAuthDAO().store(auth);

			if (result.getVillunumer()==0){
				return result.getKvittun().getHeimildarnumer();
			} else {
				throw new CreditCardAuthorizationException("ERROR: " + result.getVilluskilabod(), result.getVillunumer()+"");
			}
		} catch (Exception e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
	}

	@Override
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		return doSale(nameOnCard, cardnumber, monthExpires, yearExpires, ccVerifyNumber, amount, currency, referenceNumber);
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("ERROR: unimplemented", "UNKNOWN");
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		try {

			ValitorAuthorisationEntry authEnt = (ValitorAuthorisationEntry) getAuthDAO().findByAuthorizationCode(properties, null);
			String resp = authEnt.getServerResponse();
			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			HeimildSkilabod result = port.faOgildingu(
					merchant.getUser(),
					merchant.getPassword(),
					merchant.getExtraInfo(),
					merchant.getMerchantID(),
					merchant.getTerminalID(),
					authEnt.getCardNumber(),
					authEnt.getCurrency(),
					String.valueOf(authEnt.getAmount()),
					resp
			);

			ValitorAuthorisationEntry auth = new ValitorAuthorisationEntry();
			auth.setAmount(authEnt.getAmount());
			auth.setCardNumber(authEnt.getCardNumber());
			auth.setCurrency(authEnt.getCurrency());
			auth.setServerResponse(result.getKvittun().getFaerslunumer());
			auth.setAuthCode(result.getKvittun().getHeimildarnumer());
			if (result.getVillunumer()!=0){
				auth.setErrorNumber(result.getVillunumer()+"");
				auth.setErrorText(result.getVilluskilabod());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			getAuthDAO().store(auth);

			if (result.getVillunumer()==0){
				return result.getKvittun().getHeimildarnumer();
			} else {
				throw new CreditCardAuthorizationException("ERROR: " + result.getVilluskilabod(), result.getVillunumer()+"");
			}
		} catch (Exception e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
	}

	@Override
	public boolean supportsDelayedTransactions() {
		return false;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		return auth;
	}

	@Override
	public void setAuthorizationEntry(CreditCardAuthorizationEntry entry) {
		if (entry instanceof ValitorAuthorisationEntry) {
			this.auth = (ValitorAuthorisationEntry) entry;
		}
	}

	@Override
	public String getAuthorizationNumber(String properties) {
		return properties;
	}

	public ValitorAuthorisationEntryDAO getAuthDAO() {
		if (authDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return authDAO;
	}

	public void setAuthDAO(ValitorAuthorisationEntryDAO authDAO) {
		this.authDAO = authDAO;
	}

	@Override
	public String getPropertiesToCaptureWebPayment(
			String currency,
			double amount,
			Timestamp timestamp,
			String reference,
			String approvalCode
	) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public CaptureResult getAuthorizationNumberForWebPayment(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String doSale(
			String nameOnCard,
			String cardNumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber,
			SaleOption... options
	) throws CreditCardAuthorizationException {
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			details = "Name on card: " + nameOnCard + ", card number: " + CreditCardUtil.getMaskedCreditCardNumber(cardNumber) +
			", expires (MM/YY): " + monthExpires + CoreConstants.SLASH + yearExpires + ", CVC: " + ccVerifyNumber + ", amount: " + amount +
			", currency: " + currency + ", reference number: " + referenceNumber +
			(ArrayUtil.isEmpty(options) ? CoreConstants.EMPTY : ", sale options: " + Arrays.asList(options));
			if (
					StringUtil.isEmpty(cardNumber) ||
					StringUtil.isEmpty(monthExpires) ||
					StringUtil.isEmpty(yearExpires) ||
					StringUtil.isEmpty(ccVerifyNumber) ||
					StringUtil.isEmpty(currency) ||
					StringUtil.isEmpty(referenceNumber) ||
					amount < 0
			) {
				String error = "ERROR: Some of the mandatory data is not provided. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayCardPaymentWebServiceURL = getValitorPayCardPaymentWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Get the card verification data from credit card authorization entry metadata to use in payment
			CreditCardAuthorizationEntry creditCardAuthorizationEntry = getAuthDAO().findByAuthorizationCode(referenceNumber, null);
			ValitorAuthorisationEntry valitorAuthorisationEntry = null;
			if (creditCardAuthorizationEntry != null) {
				valitorAuthorisationEntry = (ValitorAuthorisationEntry) creditCardAuthorizationEntry;
			}
			ValitorPayCardVerificationResponseData valitorPayCardVerificationResponseData = null;
			if (valitorAuthorisationEntry != null) {
				valitorPayCardVerificationResponseData = new ValitorPayCardVerificationResponseData(
						valitorAuthorisationEntry.getCavv(),
						valitorAuthorisationEntry.getMdStatus(),
						valitorAuthorisationEntry.getXid(),
						valitorAuthorisationEntry.getDsTransID()
						//creditCardAuthorizationEntry.getMetaData(CreditCardConstants.METADATA_CARD_VERIFICATION_CAVV),
						//creditCardAuthorizationEntry.getMetaData(CreditCardConstants.METADATA_CARD_VERIFICATION_MDSTATUS),
						//creditCardAuthorizationEntry.getMetaData(CreditCardConstants.METADATA_CARD_XID)
				);
			}

			if (monthExpires.length() == 1) {
				monthExpires = String.valueOf(0).concat(monthExpires);
			}

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayPaymentDataForPaymentAfterVerification(
					settings,
					nameOnCard,
					cardNumber,
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					amount,
					currency,
					referenceNumber,
					valitorPayCardVerificationResponseData,
					options
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment data. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "PAYMENT_DATA");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayPaymentData);
			String postJSONForLogging = getJSON(getValitorPayPaymentDataForPaymentAfterVerification(
					settings,
					nameOnCard,
					CreditCardUtil.getMaskedCreditCardNumber(cardNumber),
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					amount,
					currency,
					referenceNumber,
					valitorPayCardVerificationResponseData,
					options
			));
			LOGGER.info("Calling ValitorPay (" + valitorPayCardPaymentWebServiceURL + ") with data: " + postJSONForLogging);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayCardPaymentWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null,
					new AdvancedProperty(null, referenceNumber, "merchantReferenceId")
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayCardPaymentWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "PAYMENT_DATA", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
			) {
				//Save the transaction
				storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, false);

				//Return the verification HTML
				return valitorPayResponseData.getAuthorizationCode();
			}

			String error = "ERROR: ValitorPay payment failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "PAYMENT_DATA", error);
			error = error + ". Error number: " + ex.getErrorNumber() + ". Error message: " + ex.getErrorMessage();
			LOGGER.warning(error);
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;

		} catch (ValitorPayException eV) {
			String error = "ValitorPay error message: " + eV.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, eV);
			throw eV;

		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, e, "PAYMENT_DATA", error);
			throw ex;
		}
	}

	@Override
	public AuthEntryData doSaleWithCardToken(
			String cardToken,
			String transactionId,
			double amount,
			String currency,
			String referenceNumber,
			Object parentPaymentPK
	) throws CreditCardAuthorizationException {
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			details = "Card token: " + cardToken + ", transaction ID: " + transactionId +
			", amount: " + amount + "currency: " + currency + ", reference number: " + referenceNumber + ", parent payment PK: " + parentPaymentPK;
			if (
					StringUtil.isEmpty(cardToken) ||
					StringUtil.isEmpty(transactionId) ||
					parentPaymentPK == null ||
					StringUtil.isEmpty(currency) ||
					StringUtil.isEmpty(referenceNumber) ||
					amount < 0
			) {
				String error = "ERROR: Some of the mandatory data is not provided. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayWithVirtualCardWebServiceURL = getValitorPayPayWithVirtualCardWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			String uniqueBase64EncodedUUID = !StringUtil.isEmpty(transactionId) ? Base64.getEncoder().encodeToString(transactionId.getBytes()) : transactionId;

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayWithVirtualCardPaymentData(
					settings,
					cardToken,
					uniqueBase64EncodedUUID,
					amount,
					currency,
					referenceNumber,
					parentPaymentPK
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment with virtual card data. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "PAYMENT_DATA");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayPaymentData);
			LOGGER.info("Calling ValitorPay (" + valitorPayWithVirtualCardWebServiceURL + ") with data: " + postJSON);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayWithVirtualCardWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayWithVirtualCardWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "PAYMENT_DATA", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
			) {
				//Save the transaction
				if (StringUtil.isEmpty(valitorPayPaymentData.getMerchantReferenceId()) && valitorPayPaymentData.getVirtualCardPaymentAdditionalData() != null) {
					valitorPayPaymentData.setMerchantReferenceId(valitorPayPaymentData.getVirtualCardPaymentAdditionalData().getMerchantReferenceData());
				}
				//Decode the merchant reference id
				if (!StringUtil.isEmpty(valitorPayPaymentData.getMerchantReferenceId())) {
					byte[] decodedMerchantReferenceId = Base64.getDecoder().decode(valitorPayPaymentData.getMerchantReferenceId());
					if (decodedMerchantReferenceId != null) {
						valitorPayPaymentData.setMerchantReferenceId(new String(decodedMerchantReferenceId));
					}
				}
				storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, true);

				//Return the data
				return new AuthEntryData(valitorPayResponseData.getAuthorizationCode(), valitorPayPaymentData.getMerchantReferenceId());
			}

			String error = "ERROR: ValitorPay virtual card payment failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "PAYMENT_DATA", error);
			error = error + ". Error number: " + ex.getErrorNumber() + ". Error message: " + ex.getErrorMessage();
			LOGGER.warning(error);
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;

		} catch (ValitorPayException eV) {
			String error = "ValitorPay error message: " + eV.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, eV);
			throw eV;

		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, e, "PAYMENT_DATA", error);
			throw ex;
		}
	}

	private IWMainApplicationSettings getSettings() {
		IWMainApplication application = IWMainApplication.getDefaultIWMainApplication();
		if (application != null) {
			return application.getSettings();
		}

		return null;
	}

	ValitorPayPaymentData getValitorPayPaymentData(
			IWMainApplicationSettings settings,
			String nameOnCard,
			String cardNumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber,
			ValitorPayCardVerificationResponseData valitorPayCardVerificationResponseData,
			SaleOption... options
	) throws CreditCardAuthorizationException {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		Integer amountInt = CreditCardUtil.getAmountWithExponents(amount, "2");

		//Creating MerchantReentryUrl
		//FIXME: We have different redirections for different payments, but we can not get them here ???
		String merchantReentryUrl = getServerUrl(settings);
		String merchantReentryPostUrl = settings.getProperty("valitorpay.merchant_reentry_post_url", "#/dashboard");
		if (merchantReentryUrl.endsWith(CoreConstants.SLASH)) {
			merchantReentryUrl += merchantReentryPostUrl;
		} else {
			merchantReentryUrl += CoreConstants.SLASH;
			merchantReentryUrl += merchantReentryPostUrl;
		}

		//Creating MerchantWebhookUrl
		String merchantWebhookUrl = getServerUrl(settings);
		String merchantWebhookWebServiceUrl = settings.getProperty("valitorpay.merchant_webhook_web_service_url", "portal/c4c/payment/callback/hook");
		if (merchantWebhookUrl.endsWith(CoreConstants.SLASH)) {
			merchantWebhookUrl += merchantWebhookWebServiceUrl;
		} else {
			merchantWebhookUrl += CoreConstants.SLASH;
			merchantWebhookUrl += merchantWebhookWebServiceUrl;
		}

		//Create the data bean to send to ValitorPay
		boolean createVirtualCard = false;
		if (!ArrayUtil.isEmpty(options)) {
			List<SaleOption> tmp = Arrays.asList(options);
			createVirtualCard = tmp.contains(SaleOption.CREATE_VIRTUAL_CARD);
		}
		ValitorPayVirtualCardData virtualCardData = createVirtualCard ? new ValitorPayVirtualCardData(ValitorPayVirtualCardData.SUBSEQUENT_TRANSACTION_TYPE) : null;
		ValitorPayPaymentData valitorPayPaymentData = new ValitorPayPaymentData(
				merchantReentryUrl,
				merchantWebhookUrl,
				referenceNumber,
				amountInt,
				currency,
				cardNumber,
				monthExpires,
				yearExpires,
				ccVerifyNumber,
				CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW,
				virtualCardData,
				nameOnCard
		);

		return valitorPayPaymentData;
	}

	private ValitorPayPaymentData getValitorPayWithVirtualCardPaymentData(
			IWMainApplicationSettings settings,
			String cardToken,
			String transactionId,
			double amount,
			String currency,
			String referenceNumber,
			Object parentPaymentPK
	) throws CreditCardAuthorizationException {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		Integer amountInt = CreditCardUtil.getAmountWithExponents(amount, "2");

		//Create the data bean to send to ValitorPay
		ValitorPayVirtualCardAdditionalData virtualCardAdditionalData = new ValitorPayVirtualCardAdditionalData(transactionId);
		ValitorPayPaymentData valitorPayPaymentData = new ValitorPayPaymentData(
				CreditCardConstants.OPERATION_SALE,
				currency,
				amountInt,
				cardToken,
				virtualCardAdditionalData
		);

		return valitorPayPaymentData;
	}

	private String getServerUrl(IWMainApplicationSettings settings) {
		return settings.getProperty(IWMainApplication.PROPERTY_DEFAULT_SERVICE_URL);
	}

	private ValitorPayException handleValitorPayErrorResponse(ClientResponse response, ValitorPayResponseData responseData, Throwable t, String errorKey, String errorDescription) {
		if (responseData != null) {
			LOGGER.info("Handling the ValitorPay error response for valitorPayResponseData: " + responseData.toString());
		}

		ValitorPayException ex = null;

		//No response from ValitorPay
		if (response == null && responseData == null) {
			ex = new ValitorPayException(t, "ValitorPay response was empty", "RESPONSE_ERROR");
			return ex;
		}

		//Checking for the error code/responseCode and error messages
		if (responseData != null) {
			String errorCode = CoreConstants.EMPTY;
			String errorMessage = CoreConstants.EMPTY;
			if (!StringUtil.isEmpty(responseData.getResponseCode())) {
				errorCode = responseData.getResponseCode();
			}
			Map<String, List<String>> errorMap = responseData.getErrors();
			if (!MapUtil.isEmpty(errorMap)) {
				for (Map.Entry<String, List<String>> errorMsgMapEntry : errorMap.entrySet()) {
					if (errorMsgMapEntry == null) {
						continue;
					}

					List<String> errorMessagesList = errorMsgMapEntry.getValue();
					if (!ListUtil.isEmpty(errorMessagesList)) {
						for (String errMsgIn : errorMessagesList) {
							if (!StringUtil.isEmpty(errMsgIn)) {
								if (!StringUtil.isEmpty(errorMessage)) {
									if (!errorMessage.endsWith(CoreConstants.DOT)) {
										errorMessage += CoreConstants.DOT;
									}
									errorMessage += CoreConstants.SPACE;
								}
								errorMessage += errMsgIn;
							}
						}
					}
				}
			} else if (!StringUtil.isEmpty(responseData.getResponseDescription())) {
				errorMessage = responseData.getResponseDescription();
			}

			//Create the exception, if needed data is available
			if (!StringUtil.isEmpty(errorCode) || !StringUtil.isEmpty(errorMessage)) {
				ex = new ValitorPayException(t, errorMessage, errorCode);
				return ex;
			}
		}

		//In case no response code nor the error messages
		if (response != null) {
			if (response.getStatus() == Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				String errorMsg = "ValitorPay response was - Internal server error.";
				if (responseData != null) {
					if (!StringUtil.isEmpty(responseData.getMessage())) {
						errorMsg += " MESSAGE: ";
						errorMsg += responseData.getMessage();
					}
				}
				ex = new ValitorPayException(t, errorMsg, "INTERNAL_SERVER_ERROR_RESPONSE_ERROR");

			} else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
				String errorMsg = "ValitorPay response was - Bad request.";
				if (responseData != null) {
					if (!StringUtil.isEmpty(responseData.getTitle())) {
						errorMsg += " TITLE: ";
						errorMsg += responseData.getTitle();
					}
					if (!MapUtil.isEmpty(responseData.getErrors())) {
						errorMsg += ". Errors: ";
						errorMsg += responseData.getErrors();
					}
				}
				ex = new ValitorPayException(t, errorMsg, "BAD_REQUEST_RESPONSE_ERROR");
			}
		}

		if (ex == null) {
			ex = new ValitorPayException(
					t,
					StringUtil.isEmpty(errorDescription) ? "ValitorPay - unknown error. Response code: " + (response == null ? "unknown" : response.getStatus()) : errorDescription,
					StringUtil.isEmpty(errorKey) ? "UNKNOWN_RESPONSE_ERROR" : errorKey
			);
		}

		return ex;
	}

	private ValitorPayResponseData getValitorPayResponseData(ClientResponse response) throws CreditCardAuthorizationException {
		ValitorPayResponseData valitorPayResponseData = null;
		Reader reader = null;
		InputStream stream = null;
		try {
			stream = response == null ? null : response.getEntityInputStream();
			reader = stream == null ? null : new InputStreamReader(stream);
			if (reader != null) {
				valitorPayResponseData = new Gson().fromJson(reader, ValitorPayResponseData.class);
			}
		} catch (Throwable e) {
			String error = "Error reading from response " + response + ". Message: " + e.getMessage();
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
			throw new ValitorPayException(e, error);
		} finally {
			IOUtil.close(stream);
			IOUtil.close(reader);
		}

		return valitorPayResponseData;
	}

	private ValitorAuthorisationEntry storeValitorAuthorizationEntry(ValitorPayResponseData response, ValitorPayPaymentData payment, boolean virtualCardPayment) {
		try {
			ValitorAuthorisationEntry auth = this.auth;
			String merchantReferenceId = null;
			if (payment != null) {
				if (!StringUtil.isEmpty(payment.getMerchantReferenceId())) {
					merchantReferenceId = payment.getMerchantReferenceId();
				} else if (payment.getAdditionalData() != null && !StringUtil.isEmpty(payment.getAdditionalData().getMerchantReferenceData())) {
					byte[] decodedRefId = Base64.getDecoder().decode(payment.getAdditionalData().getMerchantReferenceData());
					if (decodedRefId != null) {
						merchantReferenceId = new String(decodedRefId);
					}
				}
				if (!StringUtil.isEmpty(merchantReferenceId)) {
					CreditCardAuthorizationEntry creditCardAuthorizationEntry = getAuthDAO().findByAuthorizationCode(merchantReferenceId, null);
					if (creditCardAuthorizationEntry != null) {
						auth = (ValitorAuthorisationEntry) creditCardAuthorizationEntry;
					}
				}
			}
			if (auth == null) {
				auth = new ValitorAuthorisationEntry();
			}

			auth.setAmount(Double.valueOf(payment.getAmount()));
			if (virtualCardPayment) {
				auth.setCardNumber(payment.getVirtualCardNumber());
				auth.setSuccess(Boolean.TRUE);
			} else {
				auth.setCardNumber(CreditCardUtil.getMaskedCreditCardNumber(payment.getCardNumber()));
			}
			auth.setCurrency(payment.getCurrency());
			String serverResponse = getJSON(response);
			serverResponse = serverResponse.length() > 255 ? serverResponse.substring(0, 255) : serverResponse;
			auth.setServerResponse(serverResponse);
			auth.setAuthCode(response.getAuthorizationCode());
			IWTimestamp timestamp = new IWTimestamp();
			auth.setDate(timestamp.getDate());
			auth.setTimestamp(timestamp.getTimestamp());
			if (!StringUtil.isEmpty(merchantReferenceId)) {
				auth.setUniqueId(merchantReferenceId);		//	Authorization entry must be resolved by unique id. Merchant reference id is saved as unique id only
			}
			if (response.getIsSuccess() != null && response.getIsSuccess().booleanValue() == false){
				auth.setErrorNumber(response.getResponseCode());
				auth.setErrorText(response.getResponseDescription());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			if (!StringUtil.isEmpty(response.getTransactionID())) {
				auth.setTransactionId(response.getTransactionID());
			}
			if (!StringUtil.isEmpty(response.getTransactionLifecycleId())) {
				auth.setTransactionLifecycleId(response.getTransactionLifecycleId());
			}
			getAuthDAO().store(auth);
			return auth.getId() == null ? null : auth;
		} catch (Exception e) {
			String error = "Could not store the ValitorAuthorisationEntry after the ValidtoPay transaction. "
					+ "valitorPayResponseData: " + response.toString()
					+ ". valitorPayPaymentData: " + payment;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		} finally {
			CoreUtil.clearAllCaches();
		}
		return null;
	}

	private String getDefaultValitorPayURL(IWMainApplicationSettings settings, String type) {
		if (StringUtil.isEmpty(type)) {
			return DEFAULT_URL;
		} else {
			if (type.equalsIgnoreCase("PAYMENT")) {
				return settings.getProperty(
						"valitorpay.url.default.payment",
						"https://valitorpay.com/Payment"
				);
			} else if (type.equalsIgnoreCase("VIRTUAL_CARD")) {
				return settings.getProperty(
						"valitorpay.url.default.virtual_card",
						"https://valitorpay.com/VirtualCard"
				);
			} else {
				return settings.getProperty(
						"valitorpay.url.default.verification",
						"https://valitorpay.com"
				);
			}
		}
	}

	private String getValitorPayCardPaymentWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWebServiceURL = settings.getProperty(
				"valitorpay.url.card_payment",
				getDefaultValitorPayURL(settings, "PAYMENT") + "/CardPayment"
		);
		return valitorPayCardPaymentWebServiceURL;
	}

	String getValitorPayCardPaymentWithVerificationWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.card_payment_with_verification",
				getDefaultValitorPayURL(settings, "PAYMENT") + "/CardPaymentWithVerification"
		);
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

	private String getValitorPayApiVersion(IWMainApplicationSettings settings) {
		String valitorPayApiVersion = settings.getProperty("valitorpay.api_version", "2.0");
		return valitorPayApiVersion;
	}

	private String getValitorPayPayWithVirtualCardWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.virtual_card_payment",
				getDefaultValitorPayURL(settings, "PAYMENT") + "/VirtualCardPayment"
		);
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

	private String getValitorPayCardVerificationWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.card_verification",
				getDefaultValitorPayURL(settings, "VERIFICATION") + "/CardVerification"
		);
		return valitorPayCardVerificationWebServiceURL;
	}

	private String getValitorPayUpdateExpirationDateWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayUpdateExpirationDateWebServiceURL = settings.getProperty(
				"valitorpay.url.update_card_expiration_date",
				getDefaultValitorPayURL(settings, "VIRTUAL_CARD") + "/UpdateExpirationDate"
		);
		return valitorPayUpdateExpirationDateWebServiceURL;
	}

	private String getValitorPayUpdateTransactionLifeCycleIdWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayUpdateTransactionLifeCycleIdWebServiceURL = settings.getProperty(
				"valitorpay.url.update_transaction_lifecycle_id",
				getDefaultValitorPayURL(settings, "VIRTUAL_CARD") + "/UpdateTransactionLifecycleId"
		);
		return valitorPayUpdateTransactionLifeCycleIdWebServiceURL;
	}

	private String getValitorPayCreateVirtualCardWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCreateVirtualCardWebServiceURL = settings.getProperty(
				"valitorpay.url.create_virtual_card",
				getDefaultValitorPayURL(settings, "VIRTUAL_CARD") + "/CreateVirtualCard"
		);
		return valitorPayCreateVirtualCardWebServiceURL;
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
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			details = "Card number: " + CreditCardUtil.getMaskedCreditCardNumber(cardNumber) +
			", expires (MM/YY): " + monthExpires + CoreConstants.SLASH + yearExpires + ", amount: " + amount +
			", currency: " + currency;
			if (
					StringUtil.isEmpty(cardNumber) ||
					StringUtil.isEmpty(currency) ||
					monthExpires == null ||
					yearExpires == null ||
					amount < 0
			) {
				String error = "ERROR: Some of the mandatory data is not provided. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			if (StringUtil.isEmpty(verificationType)) {
				verificationType = com.idega.block.creditcard.business.CreditCardBusiness.CARD_VERIFICATION_TYPE_CARD;
			}

			String valitorPayCardVerificationWebServiceURL = getValitorPayCardVerificationWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			LOGGER.info("Key/Identifier/MD before encoding and calling ValitorPay verification web service: " + referenceNumber);

			String uniqueBase64EncodedUUID = !StringUtil.isEmpty(referenceNumber) ? Base64.getEncoder().encodeToString(referenceNumber.getBytes()) : referenceNumber;

			//Get the ValitorPay payment data
			ValitorPayCardVerificationData valitorPayCardVerificationData = getValitorPayCardVerificationData(
					settings,
					cardNumber,
					null, //Virtual card token
					amount,
					monthExpires,
					yearExpires,
					currency,
					CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW,
					null,
					null,
					uniqueBase64EncodedUUID,
					null,
					verificationType
			);
			if (valitorPayCardVerificationData == null) {
				String error = "ERROR: Can not construct ValitorPay verification data. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "VERIFICATION_DATA");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWTimestamp merchantReferenceIdStamp = new IWTimestamp();
			String merchantReferenceId = merchantReferenceIdStamp.getDateString(CreditCardConstants.FULL_DATE_TIME_NO_DELIMITERS_STRING);

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayCardVerificationData);
			String postJSONForLogging = getJSON(getValitorPayCardVerificationData(
					settings,
					CreditCardUtil.getMaskedCreditCardNumber(cardNumber),
					null, //Virtual card token
					amount,
					monthExpires,
					yearExpires,
					currency,
					CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW,
					null,
					null,
					uniqueBase64EncodedUUID,
					null,
					verificationType
			));
			LOGGER.info("Calling ValitorPay (" + valitorPayCardVerificationWebServiceURL + ") with data: " + postJSONForLogging);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayCardVerificationWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null,
					new AdvancedProperty(null, merchantReferenceId, "merchantReferenceId")
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayCardVerificationWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "VERIFICATION_DATA", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					(!StringUtil.isEmpty(valitorPayResponseData.getVerificationHtml()) || !StringUtil.isEmpty(valitorPayResponseData.getCardVerificationRawResponse())) &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
			) {
				//Save the transaction
				if (!StringUtil.isEmpty(verificationType) && verificationType.equalsIgnoreCase(com.idega.block.creditcard.business.CreditCardBusiness.CARD_VERIFICATION_TYPE_CARD)) {
					storeValitorAuthorizationEntryAfterCardVerification(valitorPayResponseData, valitorPayCardVerificationData, false);
				}

				//Return the verification HTML
				return !StringUtil.isEmpty(valitorPayResponseData.getCardVerificationRawResponse()) ?
						valitorPayResponseData.getCardVerificationRawResponse() :
						valitorPayResponseData.getVerificationHtml();
			}

			String error = "ERROR: ValitorPay payment failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "VERIFICATION_DATA", error);
			error = error + ". Error number: " + ex.getErrorNumber() + ". Error message: " + ex.getErrorMessage();
			LOGGER.warning(error);
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;

		} catch (ValitorPayException eV) {
			String error = "ValitorPay error message: " + eV.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, eV);
			throw eV;

		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, e, "VERIFICATION_DATA", error);
			throw ex;
		}
	}

	private ValitorPayCardVerificationData getValitorPayCardVerificationData(
			IWMainApplicationSettings settings,
			String cardNumber,
			String virtualCard,
			double amount,
			Integer expirationMonth,
			Integer expirationYear,
			String currency,
			String cardholderDeviceType,
			String agreementNumber,
			String terminalId,
			String md,
			String exponent,
			String verificationType
	) throws CreditCardAuthorizationException {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		Integer amountInt = CreditCardUtil.getAmountWithExponents(amount, "2");

		//Creating MerchantWebhookUrl
		String authenticationURL = getServerUrl(settings);
		String merchantWebhookWebServiceUrl = CoreConstants.EMPTY;
		if (!StringUtil.isEmpty(verificationType) && verificationType.equalsIgnoreCase(com.idega.block.creditcard.business.CreditCardBusiness.CARD_VERIFICATION_TYPE_VIRTUAL_CARD)) {
			merchantWebhookWebServiceUrl = settings.getProperty("valitorpay.mwu.virt_card", "portal/c4c/payment/callback/hook/virtual");
		} else {
			merchantWebhookWebServiceUrl = settings.getProperty("valitorpay.mwu.card", "portal/c4c/payment/callback/hook");
		}
		if (authenticationURL.endsWith(CoreConstants.SLASH)) {
			authenticationURL += merchantWebhookWebServiceUrl;
		} else {
			authenticationURL += CoreConstants.SLASH;
			authenticationURL += merchantWebhookWebServiceUrl;
		}

		String serverURL = getServerUrl(settings);
		String expMonth = expirationMonth != null ? String.valueOf(expirationMonth) : CoreConstants.EMPTY;
		if (expMonth.length() == 1) {
			expMonth = String.valueOf(0).concat(expMonth);
		}
		ValitorPayCardVerificationData valitorPayCardVerificationData = new ValitorPayCardVerificationData(
				cardNumber,
				virtualCard,
				amountInt > 0 ? new Double(amountInt).intValue() : 0,
				expMonth,
				expirationYear != null ? String.valueOf(expirationYear) : CoreConstants.EMPTY,
				currency,
				CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW,
				authenticationURL,
				agreementNumber,
				terminalId,
				md,
				exponent,
				settings.getProperty("valitorpay.card_verification.authentication_success_url", serverURL),
				settings.getProperty("valitorpay.card_verification.authentication_failed_url", serverURL)
		);

		return valitorPayCardVerificationData;
	}

	private ValitorAuthorisationEntry storeValitorAuthorizationEntryAfterCardVerification(
			ValitorPayResponseData response,
			ValitorPayCardVerificationData verificationData,
			boolean virtualCardPayment
	) {
		try {
			ValitorAuthorisationEntry auth = this.auth == null ? new ValitorAuthorisationEntry() : this.auth;

			auth.setAmount(Double.valueOf(verificationData.getAmount()));
			if (!StringUtil.isEmpty(verificationData.getVirtualCard())) {
				auth.setCardNumber(verificationData.getVirtualCard());
			} else {
				auth.setCardNumber(CreditCardUtil.getMaskedCreditCardNumber(verificationData.getCardNumber()));
			}
			auth.setCurrency(verificationData.getCurrency());
			String serverResponse = getJSON(response);
			serverResponse = serverResponse.length() > 255 ? serverResponse.substring(0, 255) : serverResponse;
			auth.setServerResponse(serverResponse);
			auth.setAuthCode(response.getAuthorizationCode());
			IWTimestamp timestamp = new IWTimestamp();
			auth.setDate(timestamp.getDate());
			auth.setTimestamp(timestamp.getTimestamp());
			auth.setUniqueId(verificationData.getDecodedMd());		//	Authorization entry must be resolved by unique id. Merchant reference id is saved as unique id only
			if (response.getIsSuccess() != null && response.getIsSuccess().booleanValue() == false){
				auth.setErrorNumber(response.getResponseCode());
				auth.setErrorText(response.getResponseDescription());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			if (!StringUtil.isEmpty(response.getTransactionID())) {
				auth.setTransactionId(response.getTransactionID());
			}
			if (!StringUtil.isEmpty(response.getTransactionLifecycleId())) {
				auth.setTransactionLifecycleId(response.getTransactionLifecycleId());
			}
			getAuthDAO().store(auth);
			return auth.getId() == null ? null : auth;
		} catch (Exception e) {
			String error = "Could not store the ValitorAuthorisationEntry after the ValidtoPay card verification transaction. "
					+ "valitorPayResponseData: " + response.toString()
					+ ". verificationData: " + verificationData;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		}
		return null;
	}

	private ValitorPayPaymentData getValitorPayPaymentDataForPaymentAfterVerification(
			IWMainApplicationSettings settings,
			String nameOnCard,
			String cardNumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber,
			ValitorPayCardVerificationResponseData valitorPayCardVerificationResponseData,
			SaleOption... options
	) throws CreditCardAuthorizationException {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		Integer amountInt = CreditCardUtil.getAmountWithExponents(amount, "2");

		String uniqueBase64EncodedUUID = !StringUtil.isEmpty(referenceNumber) ? Base64.getEncoder().encodeToString(referenceNumber.getBytes()) : referenceNumber;

		ValitorPayPaymentData valitorPayPaymentData = new ValitorPayPaymentData(
				uniqueBase64EncodedUUID,
				amountInt,
				currency,
				cardNumber,
				monthExpires,
				yearExpires,
				ccVerifyNumber,
				null, //CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW,
				valitorPayCardVerificationResponseData,
				nameOnCard
		);
		valitorPayPaymentData.setOperation(CreditCardConstants.OPERATION_SALE);
		valitorPayPaymentData.setTransactionType(CreditCardConstants.TRANSACTION_TYPE_ECOMMERCE);

		return valitorPayPaymentData;
	}

	@Override
	public VirtualCard doUpdateCard(
			VirtualCard card,
			Integer monthExpires,
			Integer yearExpires,
			String firstTransactionLifecycleId
	) throws CreditCardAuthorizationException {
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			if (
					card == null ||
					StringUtil.isEmpty(card.getUniqueId()) ||
					StringUtil.isEmpty(card.getToken()) ||
					monthExpires == null ||
					yearExpires == null
			) {
				String error = "ERROR: Some of the mandatory data is not provided. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			details = "Update virtual card. Virtual card UUID: " + card.getUniqueId()
				+ ", ValitorPay virtual card UUID/token: " + card.getToken()
				+ ", expires (MM/YY): " + monthExpires + CoreConstants.SLASH + yearExpires;

			LOGGER.info("Updating virtual card expiration date on ValitorPay. Data: " + details);

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayUpdateCardExpirationDateWebServiceURL = getValitorPayUpdateExpirationDateWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Get the ValitorPay update card data
			ValitorPayVirtualCardData valitorPayVirtualCardData = new ValitorPayVirtualCardData(
					card.getToken(),
					monthExpires,
					yearExpires,
					null,
					null,
					null
			);

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayVirtualCardData);
			LOGGER.info("Calling ValitorPay (" + valitorPayUpdateCardExpirationDateWebServiceURL + ") with data: " + postJSON);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayUpdateCardExpirationDateWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayUpdateCardExpirationDateWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "UPDATE_CARD_EXPIRATION_DATE_FAILED", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
			) {
				//*** Update the transaction life cycle id, if needed ***
				if (!StringUtil.isEmpty(firstTransactionLifecycleId)) {
					updateTransactionLifecycleId(card, firstTransactionLifecycleId);
				}

				//Return
				return card;
			}

			String error = "ERROR: ValitorPay update expiration date web service call failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "UPDATE_CARD_EXPIRATION_DATE_FAILED", error);
			error = error + ". Error number: " + ex.getErrorNumber() + ". Error message: " + ex.getErrorMessage();
			LOGGER.warning(error);
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;

		} catch (ValitorPayException eV) {
			String error = "ValitorPay error message: " + eV.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, eV);
			throw eV;

		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, e, "UPDATE_CARD_EXPIRATION_DATE_FAILED", error);
			throw ex;
		}
	}

	private String updateTransactionLifecycleId(VirtualCard card, String firstTransactionLifecycleId) throws CreditCardAuthorizationException {
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			if (
					card == null ||
					StringUtil.isEmpty(card.getUniqueId()) ||
					StringUtil.isEmpty(card.getToken())
			) {
				String error = "ERROR: Some of the mandatory data is not provided for updating transaction life cycle id.";
				LOGGER.warning(error);
				return null;
			}

			details = "Update transaction life cycle id. Virtual card UUID: " + card.getUniqueId()
				+ ", ValitorPay virtual card UUID/token: " + card.getToken()
				+ ", firstTransactionLifecycleId: " + firstTransactionLifecycleId;

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			LOGGER.info("Updating transaction life cycle id. Data: " + details);

			CreditCardMerchant ccMerchant = getCreditCardMerchant();
			String valitorPayUpdateTransactionLifeCycleIdWebServiceURL = getValitorPayUpdateTransactionLifeCycleIdWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Get the ValitorPay update card data
			ValitorPayVirtualCardData valitorPayVirtualCardData = new ValitorPayVirtualCardData(
					card.getToken(),
					firstTransactionLifecycleId,
					null,
					null,
					null
			);

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayVirtualCardData);
			LOGGER.info("Calling ValitorPay (" + valitorPayUpdateTransactionLifeCycleIdWebServiceURL + ") with data: " + postJSON);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayUpdateTransactionLifeCycleIdWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayUpdateTransactionLifeCycleIdWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "UPDATE_CARD_EXPIRATION_DATE_FAILED", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					!StringUtil.isEmpty(valitorPayResponseData.getVerificationHtml()) &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
			) {
				//Return
				return valitorPayResponseData.getTransactionLifecycleId();
			}

			String error = "ERROR: ValitorPay update transaction life cycle id web service call failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "UPDATE_CARD_EXPIRATION_DATE_FAILED", error);
			LOGGER.warning(error);
			throw ex;
		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		}
		return null;
	}

	private String getJSON(Serializable object) {
		if (object == null) {
			return null;
		}

		return new GsonBuilder()
				.disableHtmlEscaping()
				.create()
				.toJson(object);
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
		String details = null;
		ClientResponse response = null;
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			details = "Card number: " + CreditCardUtil.getMaskedCreditCardNumber(cardNumber) +
			", expires (MM/YY): " + monthExpires + CoreConstants.SLASH + yearExpires + ", CVC: " + ccVerifyNumber + ", amount: " + amount +
			", currency: " + currency;
			if (verificationData != null) {
				details = details + ". Verification data. cavv: " + verificationData.getCavv()
				 + ", mdstatus: " + verificationData.getMdStatus()
				 + ", xid: " + verificationData.getXid();
			}
			if (
					verificationData == null ||
					StringUtil.isEmpty(cardNumber) ||
					monthExpires == null ||
					yearExpires == null ||
					StringUtil.isEmpty(ccVerifyNumber)
			) {
				String error = "ERROR: Some of the mandatory data is not provided. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				ValitorPayException ex = new ValitorPayException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayCreateVirtualCardWebServiceURL = getValitorPayCreateVirtualCardWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Set the verification data
			ValitorPayCardVerificationData valitorPayCardVerificationData = null;
			if (verificationData != null) {
				valitorPayCardVerificationData = new ValitorPayCardVerificationData(
						verificationData.getCavv(),
						null, //verificationData.getMdStatus(),
						verificationData.getXid(),
						verificationData.getDsTransId()
				);
			}


			//Get the ValitorPay create virtual card data
			ValitorPayVirtualCardData valitorPayCreateVirtualCardData = new ValitorPayVirtualCardData(
					cardNumber,
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					valitorPayCardVerificationData
			);

			//Call the ValitorPay web service
			String postJSON = getJSON(valitorPayCreateVirtualCardData);
			String postJSONForLogging = getJSON(new ValitorPayVirtualCardData(
					CreditCardUtil.getMaskedCreditCardNumber(cardNumber),
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					valitorPayCardVerificationData
			));
			LOGGER.info("Calling ValitorPay (" + valitorPayCreateVirtualCardWebServiceURL + ") with data: " + postJSONForLogging);
			response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayCreateVirtualCardWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, valitorPayApiVersion, "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayCreateVirtualCardWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
				ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "CREATE_VIRTUAL_CARD_FAILED", error);
				LOGGER.warning(error);
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//OK response
			if (
					valitorPayResponseData != null &&
					valitorPayResponseData.getIsSuccess() != null &&
					valitorPayResponseData.getIsSuccess().booleanValue() == true
					&& !StringUtil.isEmpty(valitorPayResponseData.getVirtualCard())
			) {
				//Return the updated virtual card
				VirtualCard virtualCard = new VirtualCard();
				virtualCard.setToken(valitorPayResponseData.getVirtualCard());
				return virtualCard;
			}

			String error = "ERROR: ValitorPay creating of virtual card failed. " + details;
			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, null, "CREATE_VIRTUAL_CARD_FAILED", error);
			error = error + ". Error number: " + ex.getErrorNumber() + ". Error message: " + ex.getErrorMessage();
			LOGGER.warning(error);
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;

		} catch (ValitorPayException eV) {
			String error = "ValitorPay error message: " + eV.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, eV);
			throw eV;

		} catch (Throwable e) {
			String error = "Error message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			ValitorPayException ex = handleValitorPayErrorResponse(response, valitorPayResponseData, e, "CREATE_VIRTUAL_CARD_FAILED", error);
			throw ex;
		}
	}

}