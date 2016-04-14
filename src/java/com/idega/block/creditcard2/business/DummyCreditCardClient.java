package com.idega.block.creditcard2.business;

/*
 * Q&D java demo for communicating with kortathjonustan's RPCS
 *
 * Gunnar Mar Gunnarsson 9. Dec 2003
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.creditcard2.data.beans.DummyAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.impl.DummyAuthorisationEntryDAO;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;

public class DummyCreditCardClient implements CreditCardClient {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	private String strCCNumber = null;//"5413033024823099";
	private String strCCExpire = null;//"0504";
	private String strCCVerify = null;//"150";

	private String strAmount = null;//"2"; // 1 aur
	//private String strAmount = "3000";
	private String strName = null; //"Grimur";
	private String strCurrentDate = null;//"031216113900";
	private String strCurrencyCode = null; //"352"; // ISK, check Appendix A, page 20

	private String strCurrencyExponent = null;
	private String strReferenceNumber = null;//Integer.toString((int)
																												
	DummyAuthorisationEntry auth = null;
	@Autowired
	DummyAuthorisationEntryDAO authDAO = null;
	
	public DummyAuthorisationEntryDAO getAuthDAO() {
		if (authDAO == null) ELUtil.getInstance().autowire(this);
		return authDAO;
	}

	public void setAuthDAO(DummyAuthorisationEntryDAO authDAO) {
		this.authDAO = authDAO;
	}

	private CreditCardMerchant ccMerchant = null;
	private IWBundle bundle = null;

	
	
	public DummyCreditCardClient(IWApplicationContext iwc){
		init(iwc);
	}

	private void init(IWApplicationContext iwc) {

		this.bundle = iwc.getIWMainApplication().getBundle(getBundleIdentifier());

	}

	private void log(String msg) {

		Handler fh = null;

		try {
			Logger logger = Logger.getLogger(this.getClass().getName());
			fh = new FileHandler(System.getProperty("catalina.base") + FileUtil.getFileSeparator() + "logs" + FileUtil.getFileSeparator() + "dummyCC.log");
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			logger.info(msg);
			fh.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fh != null) {
				fh.close();

			}
		}
	}

	public String getBundleIdentifier() {
		return (IW_BUNDLE_IDENTIFIER);
	}

	private int getAmountWithExponents(double amount) {
		int amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));

		return (int) amount * amountMultiplier;

	}

	private String getCurrencyAbbreviation(String currencyCode) {
		if (currencyCode.equals("352")) {
			return "ISK";
		} else if (currencyCode.equals("840")) {
			return "USD";
		} else if (currencyCode.equals("826")) {
			return "GBP";
		} else if (currencyCode.equals("208")) {
			return "DDK";
		} else if (currencyCode.equals("978")) {
			return "EUR";
		} else if (currencyCode.equals("752")) {
			return "SEK";
		} else if (currencyCode.equals("578")) {
			return "NOK";
		}
		return currencyCode;
	}

	private void setCurrencyAndAmount(String currency, double amount) throws CreditCardAuthorizationException {
		if (currency != null) {
			int amountMultiplier = 100;

			if (currency.equalsIgnoreCase("ISK")) {
				this.strCurrencyCode = "352";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("USD")) {
				this.strCurrencyCode = "840";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("SEK")) {
				this.strCurrencyCode = "752";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("NOK")) {
				this.strCurrencyCode = "578";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("GBP")) {
				this.strCurrencyCode = "826";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("DKK")) {
				this.strCurrencyCode = "208";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else if (currency.equalsIgnoreCase("EUR")) {
				this.strCurrencyCode = "978";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			}
			else {
				throw new CreditCardAuthorizationException("Unsupported currency (" + currency + ")");
			}
			/* Setting amount with correct */
			this.strAmount = Integer.toString((int) amount * amountMultiplier);
		}
		else {
			throw new CreditCardAuthorizationException("Currency is missing");
		}
	}

	protected String convertStringToNumbers(String string) {
		if (string != null) {
			int length = string.length();
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < length; i++) {
				str.append(Character.getNumericValue(string.charAt(i)));
			}
			return str.toString();
		}
		return string;
	}

	@Override
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException{
		IWTimestamp stamp = IWTimestamp.RightNow();
		this.strName = nameOnCard;
		this.strCCNumber = cardnumber;
		this.strCCExpire = yearExpires + monthExpires;
		this.strCCVerify = ccVerifyNumber;
		setCurrencyAndAmount(currency, amount);
		this.strCurrentDate = getDateString(stamp);
		this.strReferenceNumber = convertStringToNumbers(referenceNumber);

		return null;

	}

	@Override
	public String getAuthorizationNumber(String properties) {

		return null;
	}


	@Override
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException {
		try {
			IWTimestamp stamp = IWTimestamp.RightNow();
			this.strName = nameOnCard;
			this.strCCNumber = cardnumber;
			this.strCCExpire = yearExpires + monthExpires;
			this.strCCVerify = ccVerifyNumber;
			setCurrencyAndAmount(currency, amount);
			this.strCurrentDate = getDateString(stamp);
			this.strReferenceNumber = convertStringToNumbers(referenceNumber);

			StringBuffer logText = new StringBuffer();
			//System.out.println("referenceNumber => " + strReferenceNumber);

			String authCode = null;

				logText.append("Authorization successful");
					authCode = UUID.randomUUID().toString();

					logText.append("\nCapture successful").append("\nAuthorization Code = " + authCode);
					logText.append("\nAction Code = doSale");

					try {
						String tmpCardNum = CreditCardBusiness.encodeCreditCardNumber(cardnumber);
						storeAuthorizationEntry(tmpCardNum, amount, authCode, "DUMMY_BRAND", strCCExpire, currency, "0", "AL_GOOD", DummyAuthorisationEntry.AUTHORIZATION_TYPE_SALE, null);
						log(logText.toString());

					}
					catch (Exception e) {
						System.err.println("Unable to save entry to database");
						throw new CreditCardAuthorizationException(e);
					}

			return authCode;
		}
		catch (CreditCardAuthorizationException e) {
			StringBuffer logText = new StringBuffer();
			logText.append("Authorization FAILED");
			logText.append("\nError           = " + e.getErrorMessage());
			logText.append("\nNumber        = " + e.getErrorNumber());
			logText.append("\nDisplay error = " + e.getDisplayError());
			log(logText.toString());
			throw e;
		}
	}

	@Override
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, Object parentDataPK, String captureProperties) throws CreditCardAuthorizationException {
		IWTimestamp stamp = IWTimestamp.RightNow();
		this.strCCNumber = cardnumber;
		this.strCCExpire = yearExpires + monthExpires;
		this.strCCVerify = ccVerifyNumber;
		setCurrencyAndAmount(currency, amount);
		this.strCurrentDate = getDateString(stamp);

		String authCode = null;
		try {
			StringBuffer logText = new StringBuffer();

			logText.append("\nRefund successful").append("\nAuthorization Code = " + authCode);
			logText.append("\nAction Code = Refund");
			try {
				String tmpCardNum = CreditCardBusiness.encodeCreditCardNumber(cardnumber);
				//storeAuthorizationEntry(tmpCardNum, parentDataPK, properties, KortathjonustanAuthorisationEntries.AUTHORIZATION_TYPE_REFUND);
				log(logText.toString());

			}
			catch (Exception e) {
				System.err.println("Unable to save entry to database");
				e.printStackTrace();
				if (authCode != null) {
					return authCode;
				} else {
					throw new CreditCardAuthorizationException(e);
				}
			}

			return authCode;
		}
		catch (CreditCardAuthorizationException e) {
			StringBuffer logText = new StringBuffer();
			logText.append("Authorization FAILED");
			logText.append("\nError           = " + e.getErrorMessage());
			logText.append("\nNumber        = " + e.getErrorNumber());
			logText.append("\nDisplay error = " + e.getDisplayError());
			log(logText.toString());
			throw e;
		}
		catch (NullPointerException n) {
			throw new CreditCardAuthorizationException(n);
		}

	}

	/**
	 * @param cardnumber
	 * @param parentDataPK
	 * @param properties
	 * @throws IDOLookupException
	 * @throws CreateException
	 */
	private void storeAuthorizationEntry(String encodedCardnumber, Double amount, String authorizationCode, String brandName, String cardExpires, String currency, String errorNumber, String errorText, String authorizationType, Object parentDataPK) throws IDOLookupException, CreateException {
			auth = new DummyAuthorisationEntry();

		
			auth.setAmount(amount);//Double.parseDouble(strAmount));
		
			auth.setAuthCode(authorizationCode);//authCode);
		
			auth.setBrandName(brandName);
	
			auth.setCardExpires(cardExpires);//monthExpires+yearExpires);
		
			auth.setCurrency(currency);//currency);
		
			auth.setErrorNumber(errorNumber);
		
			auth.setErrorText(errorText);
		
			auth.setServerResponce("dummy responce");

		auth.setTransactionType(authorizationType);
		auth.setCardNumber(encodedCardnumber);
		auth.setEntryDate(IWTimestamp.RightNow().getDate());

		if (parentDataPK != null) {
			try {
				auth.setParentId(((Integer) parentDataPK).toString());
			}
			catch (Exception e) {
				System.out.println("KortathjonustanCCCleint : could not set parentID : " + parentDataPK);
			}
		}
		getAuthDAO().store(auth);
	}

	private String getDateString(IWTimestamp stamp) {
		return stamp.getDateString("yyMMddHHmmss");
	}


	@Override
	public String finishTransaction(String properties) throws KortathjonustanAuthorizationException {
		return null;
	}


	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("kortathjo");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.idega.block.creditcard.business.CreditCardClient#getCreditCardMerchant()
	 */
	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return this.ccMerchant;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.creditcard.business.CreditCardClient#supportsDelayedTransactions()
	 */
	@Override
	public boolean supportsDelayedTransactions() {
		return false;
	}

	@Override
	public String getExpireDateString(String month, String year) {
		return year+month;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		return auth;
	}

	@Override
	public String voidTransaction(String properties)
			throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented");
	}

}