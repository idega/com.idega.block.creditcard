package com.idega.block.creditcard2.business;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.ws.BindingProvider;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard.model.CaptureResult;
import com.idega.block.creditcard.model.SaleOption;
import com.idega.block.creditcard.model.ValitorPayPaymentData;
import com.idega.block.creditcard.model.ValitorPayResponseData;
import com.idega.block.creditcard.model.ValitorPayVirtualCardAdditionalData;
import com.idega.block.creditcard.model.ValitorPayVirtualCardData;
import com.idega.block.creditcard2.data.beans.ValitorAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
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
			SyndarkortnumerSkilabod result = port.faSyndarkortnumer(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), merchant.getTerminalID(), cardnumber, expirationDate, ccv, null);

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
			HeimildSkilabod result = port.faEndurgreitt(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), merchant.getTerminalID(), cardnumber, amount+"", currency, null);


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
			HeimildSkilabod result = port.faOgildingu(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), merchant.getTerminalID(), authEnt.getCardNumber(), authEnt.getCurrency(), String.valueOf(authEnt.getAmount()), resp);

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
	public String getPropertiesToCaptureWebPayment(String currency, double amount, Timestamp timestamp, String reference, String approvalCode) throws CreditCardAuthorizationException {
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
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayCardPaymentWithVerificationWebServiceURL = getValitorPayCardPaymentWithVerificationWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayPaymentData(
					settings,
					nameOnCard,
					cardNumber,
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					amount,
					currency,
					referenceNumber,
					options
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment data. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//Call the ValitorPay web service
			String postJSON = new Gson().toJson(valitorPayPaymentData);
			LOGGER.info("Calling ValitorPay (" + valitorPayCardPaymentWithVerificationWebServiceURL + ") with data: " + postJSON);
			ClientResponse response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayCardPaymentWithVerificationWebServiceURL,
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
			ValitorPayResponseData valitorPayResponseData = null;
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayCardPaymentWithVerificationWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				CreditCardAuthorizationException ex = handleValitorPayErrorResponse(response, valitorPayResponseData);
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
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
				//Save the transaction
				storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, false);

				//Return the verification HTML
				return valitorPayResponseData.getVerificationHtml();
			}

			String error = "ERROR: ValitorPay payment failed. " + details;
			LOGGER.warning(error);
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;
		} catch (Throwable e) {
			String error = "Message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, e);
			ex.setErrorNumber("UNKNOWN");
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
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "DATA_NOT_PROVIDED");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "APP_SETTINGS");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			CreditCardMerchant ccMerchant = getCreditCardMerchant();

			String valitorPayWithVirtualCardWebServiceURL = getValitorPayPayWithVirtualCardWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = ccMerchant.getSharedSecret();

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayWithVirtualCardPaymentData(
					settings,
					cardToken,
					transactionId,
					amount,
					currency,
					referenceNumber,
					parentPaymentPK
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment with virtual card data. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
				CoreUtil.sendExceptionNotification(error, ex);
				throw ex;
			}

			//Call the ValitorPay web service
			String postJSON = new Gson().toJson(valitorPayPaymentData);
			LOGGER.info("Calling ValitorPay (" + valitorPayWithVirtualCardWebServiceURL + ") with data: " + postJSON);
			ClientResponse response = ConnectionUtil.getInstance().getResponseFromREST(
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
			ValitorPayResponseData valitorPayResponseData = null;
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			LOGGER.info("After calling ValitorPay (" + valitorPayWithVirtualCardWebServiceURL + "). Response data: " + valitorPayResponseData.toString());

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				CreditCardAuthorizationException ex = handleValitorPayErrorResponse(response, valitorPayResponseData);
				String error = "ERROR: no response (" + response + ") or response status is not OK: " + (response == null ? "unknown" : response.getStatus()) + ". " + details;
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
				if (StringUtil.isEmpty(valitorPayPaymentData.getMerchantReferenceId()) && valitorPayPaymentData.getVirtualCardAdditionalData() != null) {
					valitorPayPaymentData.setMerchantReferenceId(valitorPayPaymentData.getVirtualCardAdditionalData().getMerchantReferenceData());
				}
				storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, true);

				//Return the data
				return new AuthEntryData(valitorPayResponseData.getAuthorizationCode(), valitorPayPaymentData.getMerchantReferenceId());
			}

			String error = "ERROR: ValitorPay virtual card payment failed. " + details;
			LOGGER.warning(error);
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
			CoreUtil.sendExceptionNotification(error, ex);
			throw ex;
		} catch (Throwable e) {
			String error = "Message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);

			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, e);
			ex.setErrorNumber("UNKNOWN");
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

	private ValitorPayPaymentData getValitorPayPaymentData(
			IWMainApplicationSettings settings,
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
				ValitorPayPaymentData.CARD_HOLDER_DEVICE_TYPE_WWW,
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
				ValitorPayPaymentData.OPERATION_SALE,
				currency,
				amountInt,
				cardToken,
				virtualCardAdditionalData
		);

		return valitorPayPaymentData;
	}

	private String getServerUrl(IWMainApplicationSettings settings) {
		String serverUrl = settings.getProperty(
				IWMainApplication.PROPERTY_DEFAULT_SERVICE_URL,
				 "https://cloud4club.com"
		);
		return serverUrl;
	}

	private CreditCardAuthorizationException handleValitorPayErrorResponse(ClientResponse response, ValitorPayResponseData valitorPayResponseData) {
		CreditCardAuthorizationException ex = null;
		if (response == null) {
			ex = new CreditCardAuthorizationException("ERROR: ValitorPay response was empty.", "RESPONSE_ERROR");

		} else if (response.getStatus() == Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			String errorMsg = "ERROR: ValitorPay response was - Internal server error. ";
			if (valitorPayResponseData != null) {
				if (!StringUtil.isEmpty(valitorPayResponseData.getMessage())) {
					errorMsg += " MESSAGE: ";
					errorMsg += valitorPayResponseData.getMessage();
				}
				if (!StringUtil.isEmpty(valitorPayResponseData.getCorrelationID())) {
					errorMsg += " Correlation id: ";
					errorMsg += valitorPayResponseData.getCorrelationID();
				}
			}
			ex = new CreditCardAuthorizationException(errorMsg, "RESPONSE_ERROR");

		} else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
			String errorMsg = "ERROR: ValitorPay response was - Bad request. ";
			if (valitorPayResponseData != null) {
				if (!StringUtil.isEmpty(valitorPayResponseData.getTitle())) {
					errorMsg += " TITLE: ";
					errorMsg += valitorPayResponseData.getTitle();
				}
				if (!MapUtil.isEmpty(valitorPayResponseData.getErrors())) {
					errorMsg += ". Errors: ";
					errorMsg += valitorPayResponseData.getErrors();
				}
			}
			ex = new CreditCardAuthorizationException(errorMsg, "RESPONSE_ERROR");

		} else {
			ex = new CreditCardAuthorizationException("ERROR: ValitorPay - unknown error. Response code: " + response.getStatus(), "RESPONSE_ERROR");
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
				String responseFromTheServer = IOUtils.toString(stream);
				LOGGER.info("Response from ValitorPay: " + responseFromTheServer);
			}
		} catch (Throwable e) {
			String error = "Error reading from response " + response + ". Message: " + e.getMessage();
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
			throw new CreditCardAuthorizationException(error, e);
		} finally {
			IOUtil.close(stream);
			IOUtil.close(reader);
		}

		return valitorPayResponseData;
	}

	private ValitorAuthorisationEntry storeValitorAuthorizationEntry(ValitorPayResponseData response, ValitorPayPaymentData payment, boolean virtualCardPayment) {
		try {
			ValitorAuthorisationEntry auth = this.auth == null ? new ValitorAuthorisationEntry() : this.auth;
			auth.setAmount(Double.valueOf(payment.getAmount()));
			if (virtualCardPayment) {
				auth.setCardNumber(payment.getVirtualCardNumber()); //FIXME: Using virtual card number instead of real card number
			} else {
				auth.setCardNumber(CreditCardUtil.getMaskedCreditCardNumber(payment.getCardNumber()));
			}
			auth.setCurrency(payment.getCurrency());
			String serverResponse = new Gson().toJson(response);
			serverResponse = serverResponse.length() > 255 ? serverResponse.substring(0, 255) : serverResponse;
			auth.setServerResponse(serverResponse);
			auth.setAuthCode(response.getAuthorizationCode());
			IWTimestamp timestamp = new IWTimestamp();
			auth.setDate(timestamp.getDate());
			auth.setTimestamp(timestamp.getTimestamp());
			auth.setUniqueId(payment.getMerchantReferenceId());		//	TODO: We should get the authorization entry by unique id later, not by auth code, because merchant reference id is saved as unique id only
			if (response.getIsSuccess() != null && response.getIsSuccess().booleanValue() == false){
				auth.setErrorNumber(response.getResponseCode());
				auth.setErrorText(response.getResponseDescription());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			getAuthDAO().store(auth);
			return auth.getId() == null ? null : auth;
		} catch (Exception e) {
			String error = "Could not store the ValitorAuthorisationEntry after the ValidtoPay transaction. "
					+ "valitorPayResponseData: " + response.toString()
					+ ". valitorPayPaymentData: " + payment;
			LOGGER.log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		}
		return null;
	}

	private String getValitorPayCardPaymentWithVerificationWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.card_payment_with_verification",
				DEFAULT_URL + "/CardPaymentWithVerification"
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
				DEFAULT_URL + "/VirtualCardPayment"
		);
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

}