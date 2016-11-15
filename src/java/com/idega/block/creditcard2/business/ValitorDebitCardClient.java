package com.idega.block.creditcard2.business;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.ValitorDebitAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.ValitorDebitMerchant;
import com.idega.block.creditcard2.data.dao.impl.ValitorDebitAuthorisationEntryDAO;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.expression.ELUtil;

import is.valitor.api.fyrirtaekjagreidslur.DebitSvar;
import is.valitor.api.fyrirtaekjagreidslur.Fyrirtaekjagreidslur;
import is.valitor.api.fyrirtaekjagreidslur.FyrirtaekjagreidslurSoap;
import is.valitor.api.fyrirtaekjagreidslur.HeimildSkilabod;
import is.valitor.api.fyrirtaekjagreidslur.SyndarkortnumerSkilabod;
import is.valitor.greidslugatt.TegundKorts;

public class ValitorDebitCardClient implements CreditCardClient {

	private String login;
	private String password;
	private String url;
	private CreditCardMerchant merchant;
	private ValitorDebitAuthorisationEntry auth = null;
	@Autowired
	private ValitorDebitAuthorisationEntryDAO authDAO = null;

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


	private static Long lastAuth = null;
	private static final Object LOCK = new Object() {
	};

	private static HashMap<Integer, RRN> authRefs = new HashMap<Integer, RRN>();

	public ValitorDebitCardClient(CreditCardMerchant merchant) {
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(merchant.getType())) {
			this.login = ((ValitorDebitMerchant) merchant).getUser();
			this.password = ((ValitorDebitMerchant) merchant).getPassword();
			this.url = ((ValitorDebitMerchant) merchant).getMerchantUrl() == null ? ValitorDebitCardClient.DEFAULT_URL : ((ValitorDebitMerchant) merchant).getMerchantUrl();
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
		return TPosClient.getValidCardTypes("valitor_debit");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}

	public String getVirtualCardNumber(String cardTypeStr, String cardnumber, String kennitala, String expirationDate) throws CreditCardAuthorizationException {
		TegundKorts cardType = TegundKorts.fromValue(cardTypeStr);
		try {

			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			SyndarkortnumerSkilabod result = port.faSyndarDebetkortnumer(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), merchant.getTerminalID(), cardType, kennitala, cardnumber, expirationDate);

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
		throw new CreditCardAuthorizationException("ERROR: unimplemented", "UNKNOWN");
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
		if (bundle == null)
			bundle = iwma.getBundle("com.idega.block.creditcard", true);
		return bundle;
	}


	//when using a virtual card number monthExpires yearExpires ccVerifyNumber should be null
	//nameOnCard = kennitala, ccVerifyNumber = type of card
	@Override
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		TegundKorts cardType = TegundKorts.fromValue(ccVerifyNumber);
		try {

			if (monthExpires!=null && ccVerifyNumber!=null && yearExpires!=null){
				cardnumber = getVirtualCardNumber(ccVerifyNumber, cardnumber, nameOnCard, yearExpires+monthExpires);
			}

			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			DebitSvar result = port.greidaMedDebetkorti(merchant.getUser(), merchant.getPassword(), merchant.getExtraInfo(), merchant.getMerchantID(), Integer.parseInt(merchant.getTerminalID()), cardType, cardnumber, nameOnCard, amount);


			ValitorDebitAuthorisationEntry auth = new ValitorDebitAuthorisationEntry();
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
			auth.setMerchant((ValitorDebitMerchant) merchant);
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
						throw new CreditCardAuthorizationException("ERROR: unimplemented", "UNKNOWN");
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("ERROR: unimplemented", "UNKNOWN");
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		try {

			ValitorDebitAuthorisationEntry authEnt = (ValitorDebitAuthorisationEntry) getAuthDAO().findByAuthorizationCode(properties, null);
			String resp = authEnt.getServerResponse();
			Fyrirtaekjagreidslur service = new Fyrirtaekjagreidslur();
			FyrirtaekjagreidslurSoap port = service.getFyrirtaekjagreidslurSoap();
			Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.url);
			HeimildSkilabod result = port.ogildaDebetFaerslu(merchant.getUser(), merchant.getPassword(), merchant.getTerminalID(), authEnt.getCardNumber().substring(authEnt.getCardNumber().length()-4, authEnt.getCardNumber().length()), resp, null);

			ValitorDebitAuthorisationEntry auth = new ValitorDebitAuthorisationEntry();
			auth.setAmount(authEnt.getAmount());
			auth.setCardNumber(authEnt.getCardNumber());
			auth.setCurrency(authEnt.getCurrency());
			auth.setServerResponse(result.getKvittun().getFaerslunumer());
			auth.setCardNumber(result.getKvittun().getKortnumer());
			auth.setAuthCode(result.getKvittun().getHeimildarnumer());
			if (result.getVillunumer()!=0){
				auth.setErrorNumber(result.getVillunumer()+"");
				auth.setErrorText(result.getVilluskilabod());
			}
			auth.setMerchant((ValitorDebitMerchant) merchant);
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
	public String getAuthorizationNumber(String properties) {
		try {
			BorgunDocument data = new BorgunDocument(properties);
			if (!data.getData().containsKey("AuthCode"))
				return null;
			return data.getData().get("AuthCode");
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return null;
		}

	}

	public ValitorDebitAuthorisationEntryDAO getAuthDAO() {
		if (authDAO == null)
			ELUtil.getInstance().autowire(this);
		return authDAO;
	}

	public void setAuthDAO(ValitorDebitAuthorisationEntryDAO authDAO) {
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
}
