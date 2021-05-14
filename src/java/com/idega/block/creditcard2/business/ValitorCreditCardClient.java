package com.idega.block.creditcard2.business;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
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
import com.idega.util.CoreConstants;
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

	public static final String CURRENT_VERSION = "1000";

	@Autowired
	private ValitorAuthorisationEntryDAO authDAO = null;

	private CreditCardMerchant merchant;
	private ValitorAuthorisationEntry auth = null;

	private String login;
	private String password;
	private String url;

	public static final String VERSION = "Version";
	public static final String PROCESSOR = "Processor";
	public static final String MERCHANT_ID = "MerchantID";
	public static final String TERMINAL_ID = "TerminalID";
	public static final String TRANSACTOIN_TYPE = "TransType";
	public static final String TRANSACTOIN_AMOUNT = "TrAmount";
	public static final String NEW_AMOUNT = "NewAmount";
	public static final String TRANSACTOIN_CURRENCY = "TrCurrency";
	public static final String DATE_AND_TIME = "DateAndTime";
	public static final String CARD_NUMBER = "PAN";
	public static final String EXPIRATION_DATE = "ExpDate";
	public static final String RETRIEVAL_REFERENCE_NUMBER = "RRN";
	public static final String CVC2_CODE = "CVC2";
	public static final String SECURITY_LEVEL_ID = "SecurityLevelInd";
	public static final String UNIVERSAL_CARDHOLDER_AUTHRNTICATION = "UCAF";
	public static final String VERIFIED_BY_VISA_CARDHOLDER_AUTHRNTICATION = "CAVV";
	public static final String VERIFIED_BY_VISA_TRANSACTION_ACTION_ID = "XID";
	public static final String MERCHANT_NAME = "MerchantName";
	public static final String MERCHANT_HOME = "MerchantHome";
	public static final String MERCHANT_CITY = "MerchantCity";
	public static final String MERCHANT_ZIP_CODE = "MerchantZipCode";
	public static final String MERCHANT_COUNTRY = "MerchantCountry";
	public static final String TRANSACTION_NUMBER_IN_BATCH = "Transaction";
	public static final String BATCH = "Batch";
	public static final String CVC_RESULT = "CVCResult";
	public static final String CARD_ACCEPTOR_ID = "CardAccId";
	public static final String CARD_ACCEPTOR_TERMINAL_ID = "CardAccTerminal";
	public static final String CARD_ACCEPTOR_NAME_AND_LOCATION = "CardAccName";
	public static final String AUTHORISATION_CODE = "AuthCode";
	public static final String ACTION_CODE = "ActionCode";
	public static final String STORE_TERMINAL = "StoreTerminal";
	public static final String CARD_TYPE = "CardType";
	public static final String ERROR_MESSAGE = "Message";
	public static final String MERCHANT_CONTRACT_NUMBER = "MerchantContractNumber";
	public static final String VIRTUAL_CARD_NUMBER = "VirtualCard";
	public static final String NUMBER_OF_NEW_BATCH = "NewBatch";
	public static final String NUMBER_OF_OLD_BATCH = "OldBatch";
	public static final String BATCH_NUMBER = "BatchNumber"; // for listing
																// transactions
	public static final String FROM_DATE = "FromDate";
	public static final String TO_DATE = "ToDate";
	public static final String VOIDED = "Voided";
	public static final String STATUS = "Status";
	public static final String CREDIT = "Credit"; // Contains 1 if OCT/CFT transaction

	public static final String ACTION_CODE_ACCEPTED = "000";

	public static final String ACTION_CODE_DO_NOT_HONOR = "100";
	public static final String ACTION_CODE_CARD_EXPIRED = "101";
	public static final String ACTION_CODE_SUSPECTED_FORGERY = "102";
	public static final String ACTION_CODE_MERCHANT_CALL_ACQUIRER = "103";
	public static final String ACTION_CODE_RESTRICTED_CARD = "104";
	public static final String DEFAULT_URL = "https://api-acquiring.valitor.is/fyrirtaekjagreidslur/1_1/fyrirtaekjagreidslur.asmx";

	public ValitorCreditCardClient(CreditCardMerchant merchant) {
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(merchant.getType())) {
			this.login = ((ValitorMerchant) merchant).getUser();
			this.password = ((ValitorMerchant) merchant).getPassword();
			this.url = ((ValitorMerchant) merchant).getMerchantUrl() == null ? ValitorCreditCardClient.DEFAULT_URL : ((ValitorMerchant) merchant).getMerchantUrl();
			this.merchant = merchant;
		}
	}

	@Override
	public String getExpireDateString(String month, String year) {
		return year + month;
	}

	// set parameter Valitor_supported_credit_cards before use
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

//	//when using a virtual card number monthExpires yearExpires ccVerifyNumber should be null
//	@Override
//	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
//			String ccVerifyNumber, double amount, String currency, String referenceNumber)
//					throws CreditCardAuthorizationException {
//
//		try {
//
//			if (monthExpires!=null && ccVerifyNumber!=null && yearExpires!=null && !StringUtil.isEmpty(monthExpires) && !StringUtil.isEmpty(ccVerifyNumber) && !StringUtil.isEmpty(yearExpires) && !"null".equals(monthExpires) && !"null".equals(ccVerifyNumber) && !"null".equals(yearExpires) ){
//				cardnumber = getVirtualCardNumber(cardnumber, monthExpires+yearExpires, ccVerifyNumber);
//			}
//
//			Double amt = amount;
//			String amt2 =  amt.toString();
//			String amountToPay = String.valueOf(amt.intValue());
//
//			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
//			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
//			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
//			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
//			HeimildSkilabod result = port.faHeimild(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), merchant.getTerminalID(), cardnumber, amountToPay, currency, null);
//
//			ValitorAuthorisationEntry auth = this.auth == null ? new ValitorAuthorisationEntry() : this.auth;
//			auth.setAmount(amount);
//			auth.setCardNumber(cardnumber);
//			auth.setCurrency(currency);
//			auth.setServerResponse(result.getKvittun().getFaerslunumer());
//			auth.setCardNumber(result.getKvittun().getKortnumer());
//			auth.setAuthCode(result.getKvittun().getHeimildarnumer());
//			if (result.getVillunumer()!=0){
//				auth.setErrorNumber(result.getVillunumer()+"");
//				auth.setErrorText(result.getVilluskilabod());
//			}
//			auth.setMerchant((ValitorMerchant) merchant);
//			getAuthDAO().store(auth);
//
//			if (result.getVillunumer()==0){
//				return result.getKvittun().getHeimildarnumer();
//			} else {
//				throw new CreditCardAuthorizationException("ERROR: " + result.getVilluskilabod(), result.getVillunumer()+"");
//			}
//		} catch (Exception e) {
//			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
//			ex.setErrorNumber("UNKNOWN");
//			throw ex;
//		}
//	}

	@Override
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
						throw new CreditCardAuthorizationException("ERROR: unimplemented", "UNKNOWN");
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
			String referenceNumber
	) throws CreditCardAuthorizationException {
		String details = null;
		try {
			details = "Name on card: " + nameOnCard + ", card number: " + CreditCardUtil.getMaskedCreditCardNumber(cardNumber) +
			", expires (MM/YY): " + monthExpires + CoreConstants.SLASH + yearExpires + ", CVC: " + ccVerifyNumber + ", amount: " + amount +
			", currency: " + currency + ", reference number: " + referenceNumber;
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
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "APP_SETTINGS");
				throw ex;
			}

			String valitorPayCardPaymentWithVerificationWebServiceURL = getValitorPayCardPaymentWithVerificationWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = getValitorPayApiKey(settings);

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
					referenceNumber
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment data. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
				throw ex;
			}

			//FIXME: Should we include once more "MerchantReferenceId" as query parameter???
			//FROM API: The MerchantReferenceId should be included as a query parameter to ensure that the user is redirected to the correct page on the merchant site.

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
					null
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
			throw ex;
		} catch (Throwable e) {
			String error = "Message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);

			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
	}

	@Override
	public AuthEntryData doSaleWithCardToken(String cardToken, String transactionId, double amount, String currency, String referenceNumber, Object parentPaymentPK) throws CreditCardAuthorizationException {
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
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				String error = "ERROR: Can not get the application settings. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "APP_SETTINGS");
				throw ex;
			}

			String valitorPayWithVirtualCardWebServiceURL = getValitorPayPayWithVirtualCardWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = getValitorPayApiKey(settings);

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayWithVirtualCardPaymentData(
					settings,
					cardToken,
					transactionId,
					amount,
					currency,
					referenceNumber,
					parentPaymentPK //FIXME: Where should we use this???
			);
			if (valitorPayPaymentData == null) {
				String error = "ERROR: Can not construct ValitorPay payment with virtual card data. " + details;
				LOGGER.warning(error);
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException(error, "PAYMENT_DATA");
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
			throw ex;
		} catch (Throwable e) {
			String error = "Message: " + e.getMessage() + ", " + details;
			LOGGER.log(Level.WARNING, error, e);

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
			String referenceNumber
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
		ValitorPayVirtualCardData virtualCardData = new ValitorPayVirtualCardData(ValitorPayVirtualCardData.SUBSEQUENT_TRANSACTION_TYPE);
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
			auth.setAuthCode(payment.getMerchantReferenceId()); //response.getAuthorizationCode() //TODO: We need to store merchant reference id as authorisation code, to get the entry later
			auth.setDate(new IWTimestamp().getDate());
			auth.setUniqueId(payment.getMerchantReferenceId());		//	TODO: is this correct?
			if (response.getIsSuccess() != null && response.getIsSuccess().booleanValue() == false){
				auth.setErrorNumber(response.getResponseCode());
				auth.setErrorText(response.getResponseDescription());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			getAuthDAO().store(auth);
			return auth.getId() == null ? null : auth;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Could not store the ValitorAuthorisationEntry after the ValidtoPay transaction. "
					+ "valitorPayResponseData: " + response.toString()
					+ ". valitorPayPaymentData: " + payment, e);
		}
		return null;
	}

	private String getValitorPayCardPaymentWithVerificationWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.card_payment_with_verification",
				"https://uat.valitorpay.com/Payment/CardPaymentWithVerification"
		);
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

	private String getValitorPayApiVersion(IWMainApplicationSettings settings) {
		String valitorPayApiVersion = settings.getProperty("valitorpay.api_version", "1.0");
		return valitorPayApiVersion;
	}

	private String getValitorPayApiKey(IWMainApplicationSettings settings) {
		String valitorPayApiKey = settings.getProperty("valitorpay.api_key", "VPUAT.avI9NMNHxj+X2JJn16ckUwZ+wOUXo8btfSBYvQpzogg=");
		return valitorPayApiKey;
	}

	private String getValitorPayPayWithVirtualCardWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty(
				"valitorpay.url.virtual_card_payment",
				"https://uat.valitorpay.com/Payment/VirtualCardPayment"
		);
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

}