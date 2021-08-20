package com.idega.block.creditcard2.business;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.business.VerificationData;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard.model.CaptureResult;
import com.idega.block.creditcard.model.SaleOption;
import com.idega.block.creditcard2.data.beans.BorgunAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.creditcard2.data.dao.impl.BorgunAuthorisationEntryDAO;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;
import com.idega.util.expression.ELUtil;

import borgun.heimir.pub.ws.authorization.Authorization;
import borgun.heimir.pub.ws.authorization.AuthorizationPortType;
import borgun.heimir.pub.ws.authorization.CancelAuthorizationInput;
import borgun.heimir.pub.ws.authorization.GetAuthorizationInput;
import borgun.heimir.pub.ws.authorization.GetVirtualCard;

public class BorgunCreditCardClient implements CreditCardClient {

	private static final Logger LOGGER = Logger.getLogger(BorgunCreditCardClient.class.getName());

	private String login;
	private String password;
	private String url;
	private CreditCardMerchant merchant;
	private BorgunAuthorisationEntry auth = null;

	@Autowired
	private BorgunAuthorisationEntryDAO authDAO = null;

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
	public static final String DEFAULT_URL ="https://gateway01.borgun.is/ws/Heimir.pub.ws:Authorization",
								BORGUN_ACTION_NAME = "BorgunActionName";

	private static Long lastAuth = null;
	private static final Object LOCK = new Object() {
	};

	private static HashMap<Integer, RRN> authRefs = new HashMap<>();

	public BorgunCreditCardClient(CreditCardMerchant merchant) {
		if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(merchant.getType())) {
			this.login = ((BorgunMerchant) merchant).getUser();
			this.password = ((BorgunMerchant) merchant).getPassword();
			this.url = ((BorgunMerchant) merchant).getMerchantUrl() == null? BorgunCreditCardClient.DEFAULT_URL :((BorgunMerchant) merchant).getMerchantUrl();
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
		String suffix = ((BorgunMerchant) merchant).getMerchantRrnSuffix();
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

	// set parameter borgun_supported_credit_cards before use
	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("borgun");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}

	public String getVirtualCardNumber(String cardnumber) throws CreditCardAuthorizationException {
		try {
			if (StringUtil.isEmpty(cardnumber)) {
				LOGGER.warning("Credit card number not provided");
				return null;
			}

			Authorization service = new Authorization();
			AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
			HashMap<String, String> params = new HashMap<>();
			params.put(BORGUN_ACTION_NAME, "getVirtualCard");
			params.put(BorgunCreditCardClient.MERCHANT_CONTRACT_NUMBER,
					((BorgunMerchant) this.merchant).getMerchantContractNumber());
			params.put(BorgunCreditCardClient.CARD_NUMBER, cardnumber);
			String result = null;
			BorgunDocument doc = new BorgunDocument(params);
			GetVirtualCard parameters = new GetVirtualCard();
			parameters.setVirtualCardRequestXML(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.getVirtualCard(parameters.getVirtualCardRequestXML());
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			if (resultData.containsKey(BorgunCreditCardClient.VIRTUAL_CARD_NUMBER)) {
				return resultData.get(BorgunCreditCardClient.VIRTUAL_CARD_NUMBER);
			} else {
				throw new CreditCardAuthorizationException("ERROR: no card number returned", "UNKNOWN");
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error while making a virtual number for card number " + CreditCardUtil.getMaskedCreditCardNumber(cardnumber), e);
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
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
		try {
			Long id = parentDataPK instanceof Long ?
					(Long) parentDataPK :
					parentDataPK instanceof Integer ?
							((Integer) parentDataPK).longValue() :
							Long.valueOf(parentDataPK.toString());

			BorgunAuthorisationEntry auth = getAuthDAO().findById(id);
			BorgunDocument prevAuth = new BorgunDocument(auth.getServerResponse());
			Authorization service = new Authorization();
			AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
			HashMap<String, String> params = new HashMap<>();
			params.put(BORGUN_ACTION_NAME, "getAuthorization");
			params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
			params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant) this.merchant).getMerchantProcessor());
			params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
			params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
			params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "3");
			if ("JPY".equals(currency)) {
				params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT,
						new String(((Double) (amount * 1)).intValue() + ""));
			} else {
				params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT,
						new String(((Double) (amount * 100)).intValue() + ""));
			}
			params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY, getCurrencyCode(currency));
			params.put(BorgunCreditCardClient.DATE_AND_TIME,
					prevAuth.getData().get(BorgunCreditCardClient.DATE_AND_TIME));
			params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER,
					prevAuth.getData().get(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER));
			String result = null;
			BorgunDocument doc = new BorgunDocument(params);
			GetAuthorizationInput parameters = new GetAuthorizationInput();
			parameters.setGetAuthReqXml(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.getAuthorization(parameters).getGetAuthResXml();
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result, auth);

			if (!isResultValid(resultData)) {
				throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
			}

			if (resultData.containsKey(BorgunCreditCardClient.AUTHORISATION_CODE)) {
				return resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE);
			}

		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Throwable e) {
			LOGGER.log(Level.WARNING, "Failed to refund credit card " + CreditCardUtil.getMaskedCreditCardNumber(cardnumber), e);
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
		CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: no authorization code");
		ex.setErrorNumber("UNKNOWN");
		throw ex;
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
		Authorization service = new Authorization();
		AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
		HashMap<String, String> params = new HashMap<>();
		params.put(BORGUN_ACTION_NAME, "getAuthorization");
		params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
		params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant) this.merchant).getMerchantProcessor());
		params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
		params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
		params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "1");
		if ("JPY".equals(currency)) {
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT, new String(((Double) (amount * 1)).intValue() + ""));
		} else {
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT,
					new String(((Double) (amount * 100)).intValue() + ""));
		}
		params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY, getCurrencyCode(currency));
		params.put(BorgunCreditCardClient.DATE_AND_TIME, getYYMMDDHHMMSSDate());
		params.put(BorgunCreditCardClient.CARD_NUMBER, cardnumber);
		params.put(BorgunCreditCardClient.EXPIRATION_DATE, yearExpires + monthExpires);
		params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER, getRRN());
		params.put(BorgunCreditCardClient.CVC2_CODE, ccVerifyNumber);
		String result = null;
		try {
			BorgunDocument doc = new BorgunDocument(params);
			GetAuthorizationInput parameters = new GetAuthorizationInput();
			parameters.setGetAuthReqXml(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.getAuthorization(parameters).getGetAuthResXml();
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result, null);

			if (!isResultValid(resultData)) {
				throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
			}

			if (resultData.containsKey(BorgunCreditCardClient.AUTHORISATION_CODE)) {
				return resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE);
			}
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Throwable e) {
			LOGGER.log(Level.WARNING, "doSale method failed for credit card: " + CreditCardUtil.getMaskedCreditCardNumber(cardnumber), e);
			throw new CreditCardAuthorizationException(e, "ERROR: no authorization code", "UNKNOWN");
		}

		throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
	}

	private boolean isResultValid(Map<String, String> resultData) throws CreditCardAuthorizationException {
		String actionCode = null;
		try {
			if (!MapUtil.isEmpty(resultData) && resultData.containsKey(BorgunCreditCardClient.ACTION_CODE)) {
				actionCode = resultData.get(BorgunCreditCardClient.ACTION_CODE);
				if (StringUtil.isEmpty(actionCode)) {
					throw new CreditCardAuthorizationException("ERROR: no action code", "NOACC");
				}

				if (!BorgunCreditCardClient.ACTION_CODE_ACCEPTED.equals(actionCode)) {
					String localizedError = getResourceBundle().getLocalizedString("CCERROR_" + actionCode, "ERROR: authorization code not 000");
					throw new CreditCardAuthorizationException(localizedError, actionCode);
				}
			} else {
				throw new CreditCardAuthorizationException("ERROR: no action code", "NOACC");
			}

			return true;
		} catch (Throwable e) {
			LOGGER.log(Level.WARNING, "Error checking result: " + resultData, e);
			throw new CreditCardAuthorizationException(e, actionCode == null ? "ERROR: no authorization code" : "CCERROR_" + actionCode, actionCode == null ? "UNKNOWN" : actionCode);
		}
	}

	private void storeAuthorizationEntry(Map<String, String> resultData, String result, BorgunAuthorisationEntry authEnt) {
		auth = this.auth == null ? new BorgunAuthorisationEntry() : this.auth;
		if (authEnt != null) {
			auth.setParent(authEnt);
		}
		auth.setMerchant((BorgunMerchant) merchant);
		auth.setAmount(Double.valueOf(resultData.get(BorgunCreditCardClient.TRANSACTOIN_AMOUNT)));
		auth.setAuthCode(resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE));
		auth.setBrandName(resultData.get(BorgunCreditCardClient.CARD_TYPE));
		// auth.setCardExpireDate(resultData.get(BorgunCreditCardClient.EXPIRATION_DATE));
		auth.setCardNumber(
				CreditCardBusiness.encodeCreditCardNumber(resultData.get(BorgunCreditCardClient.CARD_NUMBER)));
		auth.setCurrency(resultData.get(BorgunCreditCardClient.TRANSACTOIN_CURRENCY));
		auth.setDate(new java.sql.Date(
				getDateFromYYMMDDHHMMSS(resultData.get(BorgunCreditCardClient.DATE_AND_TIME)).getTime()));
		auth.setErrorNumber(resultData.get(BorgunCreditCardClient.ACTION_CODE));
		auth.setErrorText(resultData.get(BorgunCreditCardClient.ERROR_MESSAGE));
		auth.setTransactionType(resultData.get(BorgunCreditCardClient.TRANSACTOIN_TYPE));
		auth.setRrn(resultData.get(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER));
		try {
			BorgunDocument doc = new BorgunDocument(result);
			Map<String, String> data = doc.getData();
			data.put(BorgunCreditCardClient.CARD_NUMBER,
					CreditCardBusiness.encodeCreditCardNumber(doc.getData().get(BorgunCreditCardClient.CARD_NUMBER)));
			doc.setData(data);
			auth.setServerResponse(doc.toString());
		} catch (Exception e) {
		}
		auth = getAuthDAO().store(auth);
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
		Authorization service = new Authorization();
		AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
		HashMap<String, String> params = new HashMap<>();
		params.put(BORGUN_ACTION_NAME, "getAuthorization");
		params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
		params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant) this.merchant).getMerchantProcessor());
		params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
		params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
		params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "5");
		if ("JPY".equals(currency)) {
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT, new String(((Double) (amount * 1)).intValue() + ""));
		} else {
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT, new String(((Double) (amount * 100)).intValue() + ""));
		}
		params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY, getCurrencyCode(currency));
		params.put(BorgunCreditCardClient.DATE_AND_TIME, getYYMMDDHHMMSSDate());
		params.put(BorgunCreditCardClient.CARD_NUMBER, cardnumber);
		params.put(BorgunCreditCardClient.EXPIRATION_DATE, yearExpires + monthExpires);
		params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER, getRRN());
		params.put(BorgunCreditCardClient.CVC2_CODE, ccVerifyNumber);
		String result = null;
		try {
			BorgunDocument doc = new BorgunDocument(params);
			GetAuthorizationInput parameters = new GetAuthorizationInput();
			parameters.setGetAuthReqXml(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.getAuthorization(parameters).getGetAuthResXml();
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result, null);

			if (!isResultValid(resultData)) {
				throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
			}

			return result;
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Throwable e) {
			LOGGER.log(Level.WARNING, "creditcard authorization method failed for card number " + CreditCardUtil.getMaskedCreditCardNumber(cardnumber), e);
			throw new CreditCardAuthorizationException(e, "ERROR: no authorization code", "UNKNOWN");
		}
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		Authorization service = new Authorization();
		AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
		HashMap<String, String> params = new HashMap<>();
		try {
			BorgunDocument prevAuth = new BorgunDocument(properties);
			params.put(BORGUN_ACTION_NAME, "getAuthorization");
			params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
			params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant) this.merchant).getMerchantProcessor());
			params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
			params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
			params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "1");
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT,
					prevAuth.getData().get(BorgunCreditCardClient.TRANSACTOIN_AMOUNT));
			params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY,
					prevAuth.getData().get(BorgunCreditCardClient.TRANSACTOIN_CURRENCY));
			params.put(BorgunCreditCardClient.DATE_AND_TIME,
					prevAuth.getData().get(BorgunCreditCardClient.DATE_AND_TIME));
			params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER,
					prevAuth.getData().get(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER));
			params.put(BorgunCreditCardClient.AUTHORISATION_CODE,
					prevAuth.getData().get(BorgunCreditCardClient.AUTHORISATION_CODE));
			String result = null;
			BorgunDocument doc = new BorgunDocument(params);
			GetAuthorizationInput parameters = new GetAuthorizationInput();
			parameters.setGetAuthReqXml(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.getAuthorization(parameters).getGetAuthResXml();
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result, null); // TODO find auth

			if (!isResultValid(resultData)) {
				throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
			}

			if (resultData.containsKey(BorgunCreditCardClient.AUTHORISATION_CODE)) {
				return resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE);
			}

		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Throwable e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}

		CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: no authorization code");
		ex.setErrorNumber("UNKNOWN");
		throw ex;
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		Authorization service = new Authorization();
		AuthorizationPortType port = service.getHeimirPubWsAuthorizationPort();
		HashMap<String, String> params = new HashMap<>();
		try {
			BorgunDocument prevAuth = new BorgunDocument(properties);
			params.put(BORGUN_ACTION_NAME, "cancelAuthorization");
			params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
			params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant) this.merchant).getMerchantProcessor());
			params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
			params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
			params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE,
					prevAuth.getData().get(BorgunCreditCardClient.TRANSACTOIN_TYPE));
			params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT,
					prevAuth.getData().get(BorgunCreditCardClient.TRANSACTOIN_AMOUNT));
			params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY,
					prevAuth.getData().get(BorgunCreditCardClient.TRANSACTOIN_CURRENCY));
			params.put(BorgunCreditCardClient.DATE_AND_TIME,
					prevAuth.getData().get(BorgunCreditCardClient.DATE_AND_TIME));
			params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER,
					prevAuth.getData().get(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER));
			params.put(BorgunCreditCardClient.AUTHORISATION_CODE,
					prevAuth.getData().get(BorgunCreditCardClient.AUTHORISATION_CODE));
			String result = null;
			BorgunDocument doc = new BorgunDocument(params);
			CancelAuthorizationInput parameters = new CancelAuthorizationInput();
			parameters.setCancelAuthReqXml(doc.toString());
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			req_ctx.put(BindingProvider.USERNAME_PROPERTY, this.login);
			req_ctx.put(BindingProvider.PASSWORD_PROPERTY, this.password);
			result = port.cancelAuthorization(parameters).getCancelAuthResXml();
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result, null); // TODO find auth

			if (!isResultValid(resultData)) {
				throw new CreditCardAuthorizationException("ERROR: no authorization code", "UNKNOWN");
			}

			if (resultData.containsKey(BorgunCreditCardClient.AUTHORISATION_CODE)) {
				return resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE);
			}

		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Throwable e) {
			CreditCardAuthorizationException ex = new CreditCardAuthorizationException(e);
			ex.setErrorNumber("UNKNOWN");
			throw ex;
		}
		CreditCardAuthorizationException ex = new CreditCardAuthorizationException("ERROR: no authorization code");
		ex.setErrorNumber("UNKNOWN");
		throw ex;
	}

	@Override
	public boolean supportsDelayedTransactions() {
		return true;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		return auth;
	}

	@Override
	public void setAuthorizationEntry(CreditCardAuthorizationEntry entry) {
		if (entry instanceof BorgunAuthorisationEntry) {
			this.auth = (BorgunAuthorisationEntry) entry;
		}
	}

	@Override
	public String getAuthorizationNumber(String properties) {
		try {
			BorgunDocument data = new BorgunDocument(properties);
			if (!data.getData().containsKey("AuthCode")) {
				return null;
			}
			return data.getData().get("AuthCode");
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return null;
		}

	}

	public BorgunAuthorisationEntryDAO getAuthDAO() {
		if (authDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return authDAO;
	}

	public void setAuthDAO(BorgunAuthorisationEntryDAO authDAO) {
		this.authDAO = authDAO;
	}

	private String getCurrencyCode(String currency) {
		if (currency != null) {
			if (currency.equalsIgnoreCase(CurrencyHolder.ICELANDIC_KRONA)) {
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
	public AuthEntryData doSaleWithCardToken(String cardToken, String transactionId, double amount, String currency, String referenceNumber, Object parentPaymentPK) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public VirtualCard doCreateVirtualCard(String cardNumber, Integer monthExpires, Integer yearExpires,
			String ccVerifyNumber, double amount, String currency, VerificationData verificationData) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public VirtualCard doUpdateCard(VirtualCard card, Integer monthExpires, Integer yearExpires,
			String firstTransactionLifecycleId) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

	@Override
	public String doVerifyCard(String cardNumber, Integer monthExpires, Integer yearExpires, double amount,
			String currency, String verificationType, String referenceNumber) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

}