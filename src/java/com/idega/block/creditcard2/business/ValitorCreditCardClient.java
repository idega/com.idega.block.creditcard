package com.idega.block.creditcard2.business;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
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
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.restful.util.ConnectionUtil;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.RequestUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.sun.jersey.api.client.ClientResponse;

import is.valitor.api.fyrirtaekjagreidslur.Fyrirtaekjagreidslur;
import is.valitor.api.fyrirtaekjagreidslur.FyrirtaekjagreidslurSoap;
import is.valitor.api.fyrirtaekjagreidslur.HeimildSkilabod;
import is.valitor.api.fyrirtaekjagreidslur.SyndarkortnumerSkilabod;

public class ValitorCreditCardClient implements CreditCardClient {

	private String login;
	private String password;
	private String url;
	private CreditCardMerchant merchant;
	private ValitorAuthorisationEntry auth = null;
	@Autowired
	private ValitorAuthorisationEntryDAO authDAO = null;

	public static final String CURRENT_VERSION = "1000";

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
	public static final String CREDIT = "Credit"; // Contains 1 if OCT/CFT
													// transaction

	public static final String ACTION_CODE_ACCEPTED = "000";

	public static final String ACTION_CODE_DO_NOT_HONOR = "100";
	public static final String ACTION_CODE_CARD_EXPIRED = "101";
	public static final String ACTION_CODE_SUSPECTED_FORGERY = "102";
	public static final String ACTION_CODE_MERCHANT_CALL_ACQUIRER = "103";
	public static final String ACTION_CODE_RESTRICTED_CARD = "104";
	public static final String DEFAULT_URL = "https://api-acquiring.valitor.is/fyrirtaekjagreidslur/1_1/fyrirtaekjagreidslur.asmx";

	private final Logger log = Logger.getLogger(this.getClass().getName());

	private static Long lastAuth = null;
	private static final Object LOCK = new Object() {
	};

	private static HashMap<Integer, RRN> authRefs = new HashMap<>();

	public ValitorCreditCardClient(CreditCardMerchant merchant) {
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(merchant.getType())) {
			this.login = ((ValitorMerchant) merchant).getUser();
			this.password = ((ValitorMerchant) merchant).getPassword();
			this.url = ((ValitorMerchant) merchant).getMerchantUrl() == null ? ValitorCreditCardClient.DEFAULT_URL : ((ValitorMerchant) merchant).getMerchantUrl();
			this.merchant = merchant;
		}
	}

	private String getYYYYMMDDHHMMSSDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	private String getYYMMDDHHMMSSDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMddHHmmss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	private Date getDateFromYYYYMMDDHHMMSS(String date) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date result = null;
		try {
			result = sdfDate.parse(date);
		} catch (ParseException e) {

		}
		return result;
	}

	private Date getDateFromYYMMDDHHMMSS(String date) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMddHHmmss");
		Date result = null;
		try {
			result = sdfDate.parse(date);
		} catch (ParseException e) {

		}
		return result;
	}

	private String getRRN() {

		RRN ref = authRefs.get(merchant.getPrimaryKey());
		String suffix = ((ValitorMerchant) merchant).getMerchantRrnSuffix();
		Long lastAuth;

		if (ref == null) {
			synchronized (LOCK) {
				ref = authRefs.get(merchant.getPrimaryKey());
				if (ref == null) {
					ref = new RRN();
					String last = getAuthDAO().getLastAuthorizationForMerchant(suffix,
							(Integer) merchant.getPrimaryKey());
					if (last == null) {
						lastAuth = (long) 1;
					} else {
						String lastNUmber = last.substring(suffix.length());
						lastAuth = Long.parseLong(lastNUmber, 10);
						lastAuth++;
					}
					ref.setLastAuth(lastAuth);
					authRefs.put((Integer) merchant.getPrimaryKey(), ref);
				} else {
					synchronized (ref.getLOCK()) {
						lastAuth = ref.getNextAuth();
					}
				}
			}
		} else {
			synchronized (ref.getLOCK()) {
				lastAuth = ref.getNextAuth();
			}
		}
		StringBuilder rrn = new StringBuilder();
		rrn.append(suffix);
		for (int i = 0; i < 12 - (suffix.length() + lastAuth.toString().length()); i++) {
			rrn.append('0');
		}
		rrn.append(lastAuth);
		return rrn.toString();

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

	private IWResourceBundle getResourceBundle() {
		IWContext iwc = IWContext.getCurrentInstance();
		if (iwc != null) {
			return getBundle().getResourceBundle(iwc);
		} else {
			return getBundle().getResourceBundle(IWMainApplication.getDefaultIWMainApplication().getDefaultLocale());
		}
	}

	private IWBundle getBundle() {
		IWMainApplication iwma = IWMainApplication.getDefaultIWMainApplication();
		IWBundle bundle = iwma.getBundle("com.idega.block.creditcard");
		if (bundle == null) {
			bundle = iwma.getBundle("com.idega.block.creditcard", true);
		}
		return bundle;
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

	private String getCurrencyCode(String currency) {
		if (currency != null) {
			if (currency.equalsIgnoreCase("ISK")) {
				return "352";
			} else if (currency.equalsIgnoreCase("USD")) {
				return "840";
			} else if (currency.equalsIgnoreCase("SEK")) {
				return "752";
			} else if (currency.equalsIgnoreCase("NOK")) {
				return "578";
			} else if (currency.equalsIgnoreCase("GBP")) {
				return "826";
			} else if (currency.equalsIgnoreCase("DKK")) {
				return "208";
			} else if (currency.equalsIgnoreCase("EUR")) {
				return "978";
			}
		}
		return null;
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
			String cardnumber,
			String monthExpires,
			String yearExpires,
			String ccVerifyNumber,
			double amount,
			String currency,
			String referenceNumber
	) throws CreditCardAuthorizationException {

		try {
			if (StringUtil.isEmpty(cardnumber) || StringUtil.isEmpty(monthExpires) || StringUtil.isEmpty(yearExpires)
					|| StringUtil.isEmpty(ccVerifyNumber) || StringUtil.isEmpty(currency) || StringUtil.isEmpty(referenceNumber) || amount == 0
			) {
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Some of the mandatory data is not provided.", "DATA_NOT_PROVIDED");
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Can not get the application settings.", "APP_SETTINGS");
				throw ex;
			}
			String valitorPayCardPaymentWithVerificationWebServiceURL = getValitorPayCardPaymentWithVerificationWebServiceURL(settings);
			String valitorPayApiVersion = getValitorPayApiVersion(settings);
			String valitorPayApiKey = getValitorPayApiKey(settings);

			//Get the ValitorPay payment data
			ValitorPayPaymentData valitorPayPaymentData = getValitorPayPaymentData(
					settings,
					nameOnCard,
					cardnumber,
					monthExpires,
					yearExpires,
					ccVerifyNumber,
					amount,
					currency,
					referenceNumber
			);
			if (valitorPayPaymentData == null) {
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Can not construct ValitorPay payment data.", "PAYMENT_DATA");
				throw ex;
			}

			//FIXME: Should we include once more "MerchantReferenceId" as query parameter???
			//FROM API: The MerchantReferenceId should be included as a query parameter to ensure that the user is redirected to the correct page on the merchant site.


			//Call the ValitorPay web service
			String postJSON = new Gson().toJson(valitorPayPaymentData);
			log.info("Calling ValitorPay doSale web service with data: " + postJSON);
			ClientResponse response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayCardPaymentWithVerificationWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, "".concat(valitorPayApiVersion), "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			ValitorPayResponseData valitorPayResponseData = null;
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			log.info("After calling ValitorPay doSale web service. Response data: " + valitorPayResponseData);

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				CreditCardAuthorizationException ex = handleValitorPayErrorResponse(response, valitorPayResponseData);
				throw ex;
			} else {
				//OK response
				if (valitorPayResponseData != null
						&& !StringUtil.isEmpty(valitorPayResponseData.getVerificationHtml())
						&& valitorPayResponseData.getIsSuccess() != null && valitorPayResponseData.getIsSuccess().booleanValue() == true
				) {
					//Save the transaction
					storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, false);

					//Return the verification HTML
					return valitorPayResponseData.getVerificationHtml();
				} else {
					CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: ValitorPay payment failed.", "PAYMENT_DATA");
					throw ex;
				}
			}

		} catch (Exception e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
	}


	@Override
	public AuthEntryData doSaleWithCardToken(String cardToken, String transactionId, double amount, String currency, String referenceNumber, Object parentPaymentPK) throws CreditCardAuthorizationException {
		try {
			if (StringUtil.isEmpty(cardToken) || StringUtil.isEmpty(transactionId) || parentPaymentPK == null
					|| StringUtil.isEmpty(currency) || StringUtil.isEmpty(referenceNumber) || amount == 0
			) {
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Some of the mandatory data is not provided.", "DATA_NOT_PROVIDED");
				throw ex;
			}

			IWMainApplicationSettings settings = getSettings();
			if (settings == null) {
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Can not get the application settings.", "APP_SETTINGS");
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
				CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: Can not construct ValitorPay payment with virtual card data.", "PAYMENT_DATA");
				throw ex;
			}

			//Call the ValitorPay web service
			String postJSON = new Gson().toJson(valitorPayPaymentData);
			log.info("Calling ValitorPay doSaleWithCardToken web service with data: " + postJSON);
			ClientResponse response = ConnectionUtil.getInstance().getResponseFromREST(
					valitorPayWithVirtualCardWebServiceURL,
					StringUtil.isEmpty(postJSON) ? null : Long.valueOf(postJSON.length()),
					MediaType.APPLICATION_JSON,
					HttpMethod.POST,
					postJSON,
					Arrays.asList(
							new AdvancedProperty(null, "".concat(valitorPayApiVersion), "valitorpay-api-version"),
							new AdvancedProperty(null, "APIKey ".concat(valitorPayApiKey), RequestUtil.HEADER_AUTHORIZATION)
					),
					null
			);

			//Get ValitorPay response data
			ValitorPayResponseData valitorPayResponseData = null;
			if (response != null) {
				valitorPayResponseData = getValitorPayResponseData(response);
			}
			log.info("After calling ValitorPay doSaleWithCardToken web service. Response data: " + valitorPayResponseData);

			//Handle ValitorPay response
			if (response == null || response.getStatus() != Status.OK.getStatusCode()) {
				//Error response
				CreditCardAuthorizationException ex = handleValitorPayErrorResponse(response, valitorPayResponseData);
				throw ex;
			} else {
				//OK response
				if (valitorPayResponseData != null
						&& valitorPayResponseData.getIsSuccess() != null && valitorPayResponseData.getIsSuccess().booleanValue() == true
				) {
					//Save the transaction
					if (StringUtil.isEmpty(valitorPayPaymentData.getMerchantReferenceId()) && valitorPayPaymentData.getVirtualCardAdditionalData() != null) {
						valitorPayPaymentData.setMerchantReferenceId(valitorPayPaymentData.getVirtualCardAdditionalData().getMerchantReferenceData());
					}
					storeValitorAuthorizationEntry(valitorPayResponseData, valitorPayPaymentData, true);

					//Return the data
					return new AuthEntryData(valitorPayResponseData.getAuthorizationCode(), valitorPayPaymentData.getMerchantReferenceId());
				} else {
					CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: ValitorPay virtual card payment failed.", "PAYMENT_DATA");
					throw ex;
				}
			}

		} catch (Exception e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
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
	) {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		amount = amount * 100;
		Integer amountInt = new Double(amount).intValue();

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
		String merchantWebhookWebServiceUrl = settings.getProperty("valitorpay.merchant_webhook_web_service_url", "payment/callback/hook");
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
	) {
		//According the ValitorPay, amount should be provided in minor currency unit:
		//EXPLANATION: The total amount of the payment specified in a minor currency unit. This means that GBP is quoted in pence, USD in cents, DKK in öre, ISK in aurar etc.
		amount = amount * 100;
		Integer amountInt = new Double(amount).intValue();

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
			}
			ex = new CreditCardAuthorizationException(errorMsg, "RESPONSE_ERROR");
		} else {
			ex = new CreditCardAuthorizationException("ERROR: ValitorPay - unknown error.", "RESPONSE_ERROR");

		}
		return ex;
	}

	private ValitorPayResponseData getValitorPayResponseData(ClientResponse response) {
		ValitorPayResponseData valitorPayResponseData = null;
		try {
			//String reader = StringHandler.getContentFromInputStream(response.getEntityInputStream());
			InputStreamReader reader = new InputStreamReader(response.getEntityInputStream());
			if (reader != null) {
				valitorPayResponseData = new Gson().fromJson(reader, ValitorPayResponseData.class);
			}
		} catch (Exception e) {
			return null;
		}

		return valitorPayResponseData;
	}

	private void storeValitorAuthorizationEntry(ValitorPayResponseData valitorPayResponseData, ValitorPayPaymentData valitorPayPaymentData, boolean virtualCardPayment) {
		try {
			ValitorAuthorisationEntry auth = this.auth == null ? new ValitorAuthorisationEntry() : this.auth;
			auth.setAmount(Double.valueOf(valitorPayPaymentData.getAmount()));
			if (virtualCardPayment) {
				auth.setCardNumber(valitorPayPaymentData.getVirtualCardNumber()); //FIXME: Using virtual card number instead of real card number
			} else {
				auth.setCardNumber(valitorPayPaymentData.getCardNumber());
			}
			auth.setCurrency(valitorPayPaymentData.getCurrency());
			auth.setServerResponse(valitorPayPaymentData.getMerchantReferenceId()); //FIXME: Is it OK to store here reference number uuid of the payment?
			auth.setAuthCode(valitorPayPaymentData.getMerchantReferenceId());		//FIXME: Is it OK to store here reference number uuid of the payment?
			auth.setDate(new IWTimestamp().getDate());
			auth.setUniqueId(valitorPayPaymentData.getMerchantReferenceId());
			if (valitorPayResponseData.getIsSuccess() != null && valitorPayResponseData.getIsSuccess().booleanValue() == false){
				auth.setErrorNumber(valitorPayResponseData.getResponseCode());
				auth.setErrorText(valitorPayResponseData.getResponseDescription());
			}
			auth.setMerchant((ValitorMerchant) merchant);
			getAuthDAO().store(auth);
		} catch (Exception e) {
			log.log(Level.WARNING, "Could not store the ValitorAuthorisationEntry after the ValidtoPay transaction. "
					+ "valitorPayResponseData: " + valitorPayResponseData
					+ ". valitorPayPaymentData: " + valitorPayPaymentData, e);
		}
	}

	private String getValitorPayCardPaymentWithVerificationWebServiceURL(IWMainApplicationSettings settings) {
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty("valitorpay.url.card_payment_with_verification", "https://uat.valitorpay.com/Payment/CardPaymentWithVerification");
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
		String valitorPayCardPaymentWithVerificationWebServiceURL = settings.getProperty("valitorpay.url.virtual_card_payment", "https://uat.valitorpay.com/Payment/VirtualCardPayment");
		return valitorPayCardPaymentWithVerificationWebServiceURL;
	}

}