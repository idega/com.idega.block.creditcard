package com.idega.block.creditcard2.business;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.idega.block.creditcard2.data.beans.BorgunAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.dao.impl.BorgunAuthorisationEntryDAO;
import com.idega.util.expression.ELUtil;

import Borgun.Heimir.pub.ws.Authorization.Authorization_PortTypeProxy;

public class BorgunCreditCardClient implements CreditCardClient{

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
	public static final String BATCH_NUMBER = "BatchNumber"; //for listing transactions
	public static final String FROM_DATE = "FromDate";
	public static final String TO_DATE = "ToDate";
	public static final String VOIDED = "Voided";
	public static final String STATUS = "Status";
	public static final String CREDIT = "Credit"; //Contains 1 if OCT/CFT transaction
	
	public static final String ACTION_CODE_ACCEPTED = "000";
	
	public static final String ACTION_CODE_DO_NOT_HONOR = "100";
	public static final String ACTION_CODE_CARD_EXPIRED = "101";
	public static final String ACTION_CODE_SUSPECTED_FORGERY = "102";
	public static final String ACTION_CODE_MERCHANT_CALL_ACQUIRER = "103";
	public static final String ACTION_CODE_RESTRICTED_CARD = "104";
	
	
	public BorgunCreditCardClient(CreditCardMerchant merchant){
		if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(merchant.getType())){
			this.login = ((BorgunMerchant)merchant).getUser();
			this.password = ((BorgunMerchant)merchant).getPassword();
			this.url = ((BorgunMerchant)merchant).getMerchantUrl();
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
	    } catch (ParseException e){
	    	
	    }
	    return result;
	}
	
	private Date getDateFromYYMMDDHHMMSS(String date) {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMddHHmmss");
	    Date result = null;
	    try {
	    	result = sdfDate.parse(date);
	    } catch (ParseException e){
	    	
	    }
	    return result;
	}
	
	@Override
	public String getExpireDateString(String month, String year) {
		return year+month;
	}

	
	//set parameter borgun_supported_credit_cards before use
	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("borgun");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}

	@Override
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, Object parentDataPK, String extraField)
					throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		Authorization_PortTypeProxy port = new Authorization_PortTypeProxy(this.url,this.login,this.password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
		params.put(BorgunCreditCardClient.PROCESSOR, ((BorgunMerchant)this.merchant).getMerchantProcessor());
		params.put(BorgunCreditCardClient.MERCHANT_ID, this.merchant.getMerchantID());
		params.put(BorgunCreditCardClient.TERMINAL_ID, this.merchant.getTerminalID());
		params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "1");
		params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT, new String((amount*100)+""));
		params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY, currency);
		params.put(BorgunCreditCardClient.DATE_AND_TIME, getYYMMDDHHMMSSDate());
		params.put(BorgunCreditCardClient.CARD_NUMBER, cardnumber);
		params.put(BorgunCreditCardClient.EXPIRATION_DATE, monthExpires+yearExpires);
		params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER, referenceNumber);
		params.put(BorgunCreditCardClient.CVC2_CODE, ccVerifyNumber);
		String result = null;
		try {
			BorgunDocument doc = new BorgunDocument(params);
			result = port.getAuthorization(doc.toString());
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result);
			if (resultData.containsKey("AuthCode")) return resultData.get("AuthCode");
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}

	private void storeAuthorizationEntry(Map<String, String> resultData, String result){
		auth = new BorgunAuthorisationEntry();
		auth.setAmount(Double.valueOf(resultData.get(BorgunCreditCardClient.TRANSACTOIN_AMOUNT)));
		auth.setAuthCode(resultData.get(BorgunCreditCardClient.AUTHORISATION_CODE));
		auth.setBrandName(resultData.get(BorgunCreditCardClient.CARD_TYPE));
		//auth.setCardExpireDate(resultData.get(BorgunCreditCardClient.EXPIRATION_DATE));
		auth.setCardNumber(resultData.get(BorgunCreditCardClient.CARD_NUMBER)); //TODO hide
		auth.setCurrency(resultData.get(BorgunCreditCardClient.TRANSACTOIN_CURRENCY));
		auth.setDate(new java.sql.Date(getDateFromYYMMDDHHMMSS(resultData.get(BorgunCreditCardClient.DATE_AND_TIME)).getTime()));
		auth.setErrorNumber(resultData.get(BorgunCreditCardClient.ACTION_CODE));
		auth.setErrorText(resultData.get(BorgunCreditCardClient.ERROR_MESSAGE));
		auth.setTransactionType(BorgunCreditCardClient.TRANSACTOIN_TYPE);
		auth.setServerResponse(result); //TODO unsafe: hide PAN
		getAuthDAO().store(auth);
	}
	
	@Override
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		Authorization_PortTypeProxy port = new Authorization_PortTypeProxy(this.url,this.login,this.password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(BorgunCreditCardClient.VERSION, BorgunCreditCardClient.CURRENT_VERSION);
		params.put(BorgunCreditCardClient.PROCESSOR, "21");
		params.put(BorgunCreditCardClient.MERCHANT_ID, "21");
		params.put(BorgunCreditCardClient.TERMINAL_ID, "1");
		params.put(BorgunCreditCardClient.TRANSACTOIN_TYPE, "5");
		params.put(BorgunCreditCardClient.TRANSACTOIN_AMOUNT, new String((amount*100)+""));
		params.put(BorgunCreditCardClient.TRANSACTOIN_CURRENCY, currency);
		params.put(BorgunCreditCardClient.DATE_AND_TIME, getYYMMDDHHMMSSDate());
		params.put(BorgunCreditCardClient.CARD_NUMBER, cardnumber);
		params.put(BorgunCreditCardClient.EXPIRATION_DATE, monthExpires+yearExpires);
		params.put(BorgunCreditCardClient.RETRIEVAL_REFERENCE_NUMBER, referenceNumber);
		params.put(BorgunCreditCardClient.CVC2_CODE, ccVerifyNumber);
		String result = null;
		try {
			BorgunDocument doc = new BorgunDocument(params);
			result = port.getAuthorization(doc.toString());
			doc = new BorgunDocument(result);
			Map<String, String> resultData = doc.getData();
			storeAuthorizationEntry(resultData, result);
			if (resultData.containsKey("AuthCode")) return resultData.get("AuthCode");
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
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
	public String getAuthorizationNumber(String properties) {
		try {
			BorgunDocument data = new BorgunDocument(properties);
			if (!data.getData().containsKey("AuthCode")) return null;
			return data.getData().get("AuthCode");
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return null;
		}
		
	}

	public BorgunAuthorisationEntryDAO getAuthDAO() {
		if (authDAO==null) ELUtil.getInstance().autowire(this);
		return authDAO;
	}

	public void setAuthDAO(BorgunAuthorisationEntryDAO authDAO) {
		this.authDAO = authDAO;
	}

}
