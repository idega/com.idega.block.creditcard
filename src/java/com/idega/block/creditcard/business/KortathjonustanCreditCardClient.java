package com.idega.block.creditcard.business;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ws.rs.core.Response.Status;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntries;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesHome;
import com.idega.block.creditcard.model.CaptureResult;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.restful.util.ConnectionUtil;
import com.idega.servlet.filter.IWBundleResourceFilter;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;
import com.sun.jersey.api.client.ClientResponse;

public class KortathjonustanCreditCardClient implements CreditCardClient {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	private static final Logger LOGGER = Logger.getLogger(KortathjonustanCreditCardClient.class.getName());

	protected String HOST_NAME;// = "test.kortathjonustan.is";
	protected int HOST_PORT;// = 8443;
	protected String strKeystore;// = "/demoFolder/testkeys.jks";
	protected String strKeystorePass;// = "changeit";

	private String PROPERTY_USER = "user";
	private String PROPERTY_PASSWORD = "pwd";
	private String PROPERTY_SITE = "site";

	// private String PROPERTY_MERCHANT_LANGUAGE = "mlang"; // valid = en, is
	// (default = en)
	// private String PROPERTY_CLIENT_LANGUAGE = "clang"; // valid = en, is
	// (default = en)
	//
	// private String PROPERTY_CLIENT_IP = "cip";

	private String PROPERTY_CARDHOLDER_NAME = "d2name";
	private String PROPERTY_CC_NUMBER = "d2";
	private String PROPERTY_AMOUNT = "d4";
	private String PROPERTY_CURRENCY_EXPONENT = "de4";
	private String PROPERTY_CURRENT_DATE = "d12";
	private String PROPERTY_CC_EXPIRE = "d14";
	private String PROPERTY_REFERENCE_ID = "d31";
	private String PROPERTY_APPROVAL_CODE = "d38"; // gotten from response
	private String PROPERTY_ACTION_CODE = "d39"; // gotten from response

	private String PROPERTY_ACCEPTOR_TERM_ID = "d41";
	private String PROPERTY_ACCEPTOR_IDENT = "d42";
	private String PROPERTY_CC_VERIFY_CODE = "d47";
	private String PROPERTY_CURRENCY_CODE = "d49";
	private String PROPERTY_ORIGINAL_DATA_ELEMENT = "d56"; // gotten from response
	private String PROPERTY_TRANSACTION_ID = "transactionId";

	private String PROPERTY_AMOUNT_ECHO = "o4"; // Echo from d4
	private String PROPERTY_CURRENT_DATE_ECHO = "o12"; // Echo from d12
	private String PROPERTY_APPROVAL_CODE_ECHO = "o38"; // Echo from d38
	private String PROPERTY_ACTION_CODE_ECHO = "o39"; // Echo from d39

	// private String PROPERTY_SHIPPING_ADDRESS = "d2saddr";
	// private String PROPERTY_SHIPPING_CITY = "d2scity";
	// private String PROPERTY_SHIPPING_ZIP = "d2szip";
	// private String PROPERTY_SHIPPING_COUNTRY = "d2sctr";
	private String PROPERTY_CARD_BRAND_NAME = "d2brand";
	private String PROPERTY_TOTAL_RESPONSE = "totalResponse";

	// private String PROPERTY_SETTLEMENT_REFERENCE_NUMBER = "d37";
	private String PROPERTY_ACTION_CODE_TEXT = "d39text";
	private String PROPERTY_ERROR_CODE = "error";
	private String PROPERTY_ERROR_TEXT = "errortext";

	private static String REQUEST_TYPE_AUTHORIZATION = "/rpc/RequestAuthorisation";
	private static String REQUEST_TYPE_CAPTURE = "/rpc/RequestCapture";
	private static String REQUEST_TYPE_REVERSAL = "/rpc/RequestReversal";

	private static String CODE_AUTHORIZATOIN_APPROVED = "000";
	// private static String CODE_AUTHORIZATOIN_DECLINED = "100";
	// private static String CODE_SYSTEM_FAILURE_RETRY = "946";
	// private static String CODE_SYSTEM_FAILURE_ERROR = "909";

	protected String SITE = null;// "22";
	protected String USER = null;// "idega";
	protected String PASSWORD = null;// "zde83af";
	protected String ACCEPTOR_TERM_ID = null;// "90000022";
	protected String ACCEPTOR_IDENTIFICATION = null;// "8180001";
	// tmp values

	private String strCCNumber = null;// "5413033024823099";
	private String strCCExpire = null;// "0504";
	private String strCCVerify = null;// "150";

	private String strAmount = null;// "2"; // 1 aur
	// private String strAmount = "3000";
	private String strName = null; // "Grimur";
	private String strCurrentDate = null;// "031216113900";
	private String strCurrencyCode = null; // "352"; // ISK, check Appendix A,
											// page 20

	private String strCurrencyExponent = null;
	private String strReferenceNumber = null;// Integer.toString((int)
												// (Math.random() *
												// 43200));
	private KortathjonustanAuthorisationEntries auth = null;
	// private Hashtable returnedProperties = null;
	// // Test indicator
	// private boolean bTestServer = false;
	// private CreditCardTransaction cct = null;
	protected CreditCardMerchant ccMerchant = null;
	protected IWBundle bundle = null;

	public KortathjonustanCreditCardClient(IWApplicationContext iwc, String host, int port, String keystoreLocation,
			String keystorePass, CreditCardMerchant merchant) {
		// this(iwc, host, port, keystoreLocation, keystorePass,
		// merchant.getLocation(), merchant.getUser(), merchant.getPassword(),
		// merchant.getTerminalID(), merchant.getMerchantID());
		// }
		// private KortathjonustanCreditCardClient(IWApplicationContext iwc,
		// String
		// host, int port, String keystoreLocation, String keystorePass, String
		// site, String user, String password, String acceptorTerminalID, String
		// acceptorIdentification) {
		this.HOST_NAME = host;
		this.HOST_PORT = port;
		this.strKeystore = keystoreLocation;
		this.strKeystorePass = keystorePass;

		this.ccMerchant = merchant;
		this.SITE = merchant.getLocation();
		this.USER = merchant.getUser();
		this.PASSWORD = merchant.getPassword();
		this.ACCEPTOR_TERM_ID = merchant.getTerminalID();
		this.ACCEPTOR_IDENTIFICATION = merchant.getMerchantID();
		init(iwc);
	}

	protected KortathjonustanCreditCardClient() {
		// TODO Auto-generated constructor stub
	}

	protected void init(IWApplicationContext iwc) {

		this.bundle = iwc.getIWMainApplication().getBundle(getBundleIdentifier());

	}

	private void log(String msg) {

		Handler fh = null;

		try {
			Logger logger = Logger.getLogger(this.getClass().getName());
			fh = new FileHandler(
					this.bundle.getPropertiesRealPath() + FileUtil.getFileSeparator() + "kortathjonustan.log");
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			logger.info(msg);
			fh.close();
		} catch (Exception e) {
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
			} else if (currency.equalsIgnoreCase("USD")) {
				this.strCurrencyCode = "840";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else if (currency.equalsIgnoreCase("SEK")) {
				this.strCurrencyCode = "752";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else if (currency.equalsIgnoreCase("NOK")) {
				this.strCurrencyCode = "578";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else if (currency.equalsIgnoreCase("GBP")) {
				this.strCurrencyCode = "826";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else if (currency.equalsIgnoreCase("DKK")) {
				this.strCurrencyCode = "208";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else if (currency.equalsIgnoreCase("EUR")) {
				this.strCurrencyCode = "978";
				this.strCurrencyExponent = "2";
				amountMultiplier = (int) Math.pow(10, Double.parseDouble(this.strCurrencyExponent));
			} else {
				throw new CreditCardAuthorizationException("Unsupported currency (" + currency + ")");
			}
			/* Setting amount with correct */
			this.strAmount = Integer.toString((int) amount * amountMultiplier);
		} else {
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
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		IWTimestamp stamp = IWTimestamp.RightNow();
		this.strName = nameOnCard;
		this.strCCNumber = cardnumber;
		this.strCCExpire = yearExpires + monthExpires;
		this.strCCVerify = ccVerifyNumber;
		setCurrencyAndAmount(currency, amount);
		this.strCurrentDate = getDateString(stamp);
		this.strReferenceNumber = convertStringToNumbers(referenceNumber);

		Hashtable<String, String> returnedProperties = getFirstResponse();
		if (returnedProperties != null) {
			return propertiesToString(returnedProperties);
		} else {
			return null;
		}
	}

	@Override
	public String getAuthorizationNumber(String properties) {

		return null;
	}

	@Override
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
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
			// System.out.println("referenceNumber => " + strReferenceNumber);

			Hashtable<String, String> returnedProperties = getFirstResponse();
			String authCode = null;
			if (returnedProperties != null) {
				logText.append("Authorization successful");
				Hashtable<String, String> returnedCaptureProperties = finishTransaction(returnedProperties);
				if (returnedCaptureProperties != null
						&& returnedCaptureProperties.get(this.PROPERTY_APPROVAL_CODE).toString() != null) {
					// System.out.println("Approval Code =
					// "+returnedCaptureProperties.get(PROPERTY_APPROVAL_CODE).toString());
					authCode = returnedCaptureProperties.get(this.PROPERTY_APPROVAL_CODE).toString();// returnedCaptureProperties;

					logText.append("\nCapture successful").append("\nAuthorization Code = " + authCode);
					logText.append(
							"\nAction Code = " + returnedCaptureProperties.get(this.PROPERTY_ACTION_CODE).toString());

					try {
						String tmpCardNum = CreditCardBusinessBean.encodeCreditCardNumber(cardnumber);
						this.storeAuthorizationEntry(tmpCardNum, null, returnedCaptureProperties,
								KortathjonustanAuthorisationEntries.AUTHORIZATION_TYPE_SALE);

						log(logText.toString());

					} catch (Exception e) {
						System.err.println("Unable to save entry to database");
						throw new CreditCardAuthorizationException(e);
					}
				}
			}

			return authCode;
		} catch (CreditCardAuthorizationException e) {
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
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, Object parentDataPK, String captureProperties)
					throws CreditCardAuthorizationException {
		IWTimestamp stamp = IWTimestamp.RightNow();
		this.strCCNumber = cardnumber;
		this.strCCExpire = yearExpires + monthExpires;
		this.strCCVerify = ccVerifyNumber;
		setCurrencyAndAmount(currency, amount);
		this.strCurrentDate = getDateString(stamp);

		try {
			StringBuffer logText = new StringBuffer();
			Hashtable<String, String> capturePropertiesHash = parseResponse(captureProperties);
			Hashtable<String, String> properties = doRefund(getAmountWithExponents(amount), capturePropertiesHash, parentDataPK);

			String authCode = properties.get(this.PROPERTY_APPROVAL_CODE).toString();
			logText.append("\nRefund successful").append("\nAuthorization Code = " + authCode);
			logText.append("\nAction Code = " + properties.get(this.PROPERTY_ACTION_CODE).toString());
			try {
				String tmpCardNum = CreditCardBusinessBean.encodeCreditCardNumber(cardnumber);
				storeAuthorizationEntry(tmpCardNum, parentDataPK, properties,
						KortathjonustanAuthorisationEntries.AUTHORIZATION_TYPE_REFUND);
				log(logText.toString());

			} catch (Exception e) {
				System.err.println("Unable to save entry to database");
				e.printStackTrace();
				if (authCode != null) {
					return authCode;
				} else {
					throw new CreditCardAuthorizationException(e);
				}
			}

			return authCode;
		} catch (CreditCardAuthorizationException e) {
			StringBuffer logText = new StringBuffer();
			logText.append("Authorization FAILED");
			logText.append("\nError           = " + e.getErrorMessage());
			logText.append("\nNumber        = " + e.getErrorNumber());
			logText.append("\nDisplay error = " + e.getDisplayError());
			log(logText.toString());
			throw e;
		} catch (NullPointerException n) {
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
	private void storeAuthorizationEntry(String encodedCardnumber, Object parentDataPK, Hashtable<String, String> properties, String authorizationType) throws IDOLookupException, CreateException {
		if (auth == null) {
			KortathjonustanAuthorisationEntriesHome authHome = (KortathjonustanAuthorisationEntriesHome) IDOLookup
					.getHome(KortathjonustanAuthorisationEntries.class);
			auth = authHome.create();
		}

		LOGGER.info("Save entry " + auth + " with data " + properties);

		if (properties.containsKey(this.PROPERTY_AMOUNT))
		 {
			auth.setAmount(Double.parseDouble(properties.get(this.PROPERTY_AMOUNT).toString()));// Double.parseDouble(strAmount));
		}
		if (properties.containsKey(this.PROPERTY_APPROVAL_CODE))
		 {
			auth.setAuthorizationCode(properties.get(this.PROPERTY_APPROVAL_CODE).toString());// authCode);
		}
		if (properties.containsKey(this.PROPERTY_CARD_BRAND_NAME)) {
			auth.setBrandName(properties.get(this.PROPERTY_CARD_BRAND_NAME).toString());
		}
		if (properties.containsKey(this.PROPERTY_CC_EXPIRE))
		 {
			auth.setCardExpires(properties.get(this.PROPERTY_CC_EXPIRE).toString());// monthExpires+yearExpires);
		}
		if (properties.containsKey(this.PROPERTY_CURRENCY_CODE))
		 {
			auth.setCurrency(getCurrencyAbbreviation(properties.get(this.PROPERTY_CURRENCY_CODE).toString()));// currency);
		}
		if (properties.containsKey(this.PROPERTY_ERROR_CODE)) {
			auth.setErrorNumber(properties.get(this.PROPERTY_ERROR_CODE).toString());
		}
		if (properties.containsKey(this.PROPERTY_ERROR_TEXT)) {
			auth.setErrorText(properties.get(this.PROPERTY_ERROR_TEXT).toString());
		}
		if (properties.containsKey(this.PROPERTY_TOTAL_RESPONSE)) {
			auth.setServerResponse(properties.get(this.PROPERTY_TOTAL_RESPONSE).toString());
		}
		if (properties.containsKey(this.PROPERTY_ORIGINAL_DATA_ELEMENT)) {
			auth.setPaymentId(properties.get(this.PROPERTY_ORIGINAL_DATA_ELEMENT));
		}
		if (properties.containsKey(PROPERTY_TRANSACTION_ID)) {
			auth.setTransactionId(properties.get(PROPERTY_TRANSACTION_ID));
		}

		auth.setTransactionType(authorizationType);
		auth.setCardNumber(encodedCardnumber);
		IWTimestamp now = IWTimestamp.RightNow();
		auth.setDate(now.getDate());
		if (auth.getTimestamp() == null) {
			auth.setTimestamp(new Timestamp(now.getTime().getTime()));
		}

		if (parentDataPK != null && StringHandler.isNumeric(parentDataPK.toString())) {
			try {
				auth.setParentID(((Integer) parentDataPK).intValue());
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "KortathjonustanCCCleint : could not set parentID : " + parentDataPK, e);
			}
		}
		auth.store();
	}

	private String getDateString(IWTimestamp stamp) {
		return stamp.getDateString("yyMMddHHmmss");
	}

	/*
	 * public static void main(String[] args) throws Exception { String host =
	 * "test.kortathjonustan.is"; int port = 8443; String SITE = "22"; String
	 * USER = "idega"; String PASSWORD = "zde83af"; String ACCEPTOR_TERM_ID =
	 * "90000022"; String ACCEPTOR_IDENTIFICATION = "8180001";
	 *
	 * String strCCNumber = "5413033024823099"; String strCCExpire = "0504";
	 * String strCCVerify = "150"; String strReferenceNumber =
	 * Integer.toString((int) (Math.random() * 43200)); String keystore =
	 * "/Applications/idega/webs/nat/idegaweb/bundles/com.idega.block.creditcard.bundle/resources/demoFolder/testkeys.jks";
	 * String keystorePass = "changeit";
	 *
	 * KortathjonustanCreditCardClient client = new
	 * KortathjonustanCreditCardClient(IWContext.getInstance(), host, port,
	 * keystore, keystorePass, SITE, USER, PASSWORD, ACCEPTOR_TERM_ID,
	 * ACCEPTOR_IDENTIFICATION); try { String tmp = client.doSale("Gr�mur Steri"
	 * , strCCNumber, strCCExpire.substring(2, 4), strCCExpire.substring(0, 2),
	 * strCCVerify, 1, "ISK", strReferenceNumber );
	 *
	 * //CreditCardBusiness cBus = (CreditCardBusiness)
	 * IBOLookup.getServiceInstance(IWContext.getInstance(),
	 * CreditCardBusiness.class); //KortathjonustanAuthorisationEntries entry =
	 * (KortathjonustanAuthorisationEntries) cBus.getAuthorizationEntry(supp,
	 * tmp);
	 *
	 *
	 * //String tmp2 = client.doRefund(strCCNumber, strCCExpire.substring(2, 4),
	 * strCCExpire.substring(0, 2), strCCVerify, 1, "ISK",
	 * entry.getResponseString()); System.out.println("AuthorizationNumber =
	 * "+tmp); //System.out.println("RefundAuthNumber = "+tmp2); } catch
	 * (CreditCardAuthorizationException e) { System.out.println(" ----
	 * Exception ----"); System.out.println("DisplayText =
	 * "+e.getDisplayError()); System.out.println("ErrorText = "
	 * +e.getErrorMessage()); System.out.println("ErrorNum = "
	 * +e.getErrorNumber()); System.out.println(" -----------------------");
	 * e.printStackTrace(System.err); } }
	 *
	 */
	private Hashtable<String, String> doRefund(int iAmountToRefund, Hashtable<String, String> captureProperties, Object parentDataPK) throws CreditCardAuthorizationException {
		Hashtable<String, String> refundProperties = new Hashtable<>();
		try {

			int iAmount = 0;
			try {
				iAmount = Integer.parseInt(captureProperties.get(this.PROPERTY_AMOUNT).toString());
				if (iAmountToRefund > iAmount) {
					CreditCardAuthorizationException e = new CreditCardAuthorizationException(
							"Amount to refund can not be higher that the original amount");
					e.setDisplayError("Amount to refund can not be higher that the original amount");
					throw e;
				}
			} catch (NumberFormatException e1) {
				throw new CreditCardAuthorizationException("Amount must be a number");
			}

			StringBuffer strPostData = new StringBuffer();
			// "DEFAULT" PROPERTIES
			appendProperty(strPostData, this.PROPERTY_USER, this.USER);
			appendProperty(strPostData, this.PROPERTY_PASSWORD, this.PASSWORD);
			appendProperty(strPostData, this.PROPERTY_SITE, this.SITE);
			appendProperty(strPostData, this.PROPERTY_CURRENT_DATE, getDateString(IWTimestamp.RightNow()));
			// TODO IMPLEMENT
			// appendProperty(strPostData, PROPERTY_MERCHANT_LANGUAGE)
			// appendProperty(strPostData, PROPERTY_CLIENT_LANGUAGE)
			appendProperty(strPostData, this.PROPERTY_AMOUNT_ECHO, this.strAmount);

			appendProperty(strPostData, this.PROPERTY_AMOUNT, Integer.toString(iAmountToRefund));
			if (iAmount > iAmountToRefund) {
				appendProperty(strPostData, this.PROPERTY_AMOUNT_ECHO,
						captureProperties.get(this.PROPERTY_AMOUNT).toString());
			}
			appendProperty(strPostData, this.PROPERTY_CURRENCY_EXPONENT,
					captureProperties.get(this.PROPERTY_CURRENCY_EXPONENT).toString());
			appendProperty(strPostData, this.PROPERTY_REFERENCE_ID,
					captureProperties.get(this.PROPERTY_REFERENCE_ID).toString());
			appendProperty(strPostData, this.PROPERTY_ACCEPTOR_TERM_ID,
					captureProperties.get(this.PROPERTY_ACCEPTOR_TERM_ID).toString());
			appendProperty(strPostData, this.PROPERTY_ACCEPTOR_IDENT,
					captureProperties.get(this.PROPERTY_ACCEPTOR_IDENT).toString());
			appendProperty(strPostData, this.PROPERTY_CURRENCY_CODE,
					captureProperties.get(this.PROPERTY_CURRENCY_CODE).toString());
			appendProperty(strPostData, this.PROPERTY_ORIGINAL_DATA_ELEMENT,
					captureProperties.get(this.PROPERTY_ORIGINAL_DATA_ELEMENT).toString());
			appendProperty(strPostData, this.PROPERTY_CURRENT_DATE_ECHO,
					captureProperties.get(this.PROPERTY_CURRENT_DATE).toString());
			appendProperty(strPostData, this.PROPERTY_ACTION_CODE_ECHO,
					captureProperties.get(this.PROPERTY_ACTION_CODE).toString());
			appendProperty(strPostData, this.PROPERTY_APPROVAL_CODE_ECHO,
					captureProperties.get(this.PROPERTY_APPROVAL_CODE).toString());

			String strResponse = null;

			SSLClient client = getSSLClient();
			// System.out.println("Request [" + strPostData.toString() + "]");
			try {
				strResponse = client.sendRequest(REQUEST_TYPE_REVERSAL, strPostData.toString());
			} catch (Exception e) {
				CreditCardAuthorizationException cce = new CreditCardAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest failed");
				cce.setErrorNumber("-");
				cce.setParentException(e);
				throw cce;
			}
			// System.out.println("Response [" + strResponse + "]");
			if (strResponse == null) {
				CreditCardAuthorizationException cce = new CreditCardAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest returned null");
				cce.setErrorNumber("-");
				throw cce;
			} else if (!strResponse.startsWith(this.PROPERTY_ACTION_CODE)) {
				CreditCardAuthorizationException cce = new CreditCardAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("Invalid response from host, should start with d39 [" + strResponse + "]");
				cce.setErrorNumber("-");
				throw cce;
			} else {
				refundProperties = parseResponse(strResponse);
				if (CODE_AUTHORIZATOIN_APPROVED.equals(refundProperties.get(this.PROPERTY_ACTION_CODE))) {
					return refundProperties;
				} else {
					CreditCardAuthorizationException cce = new CreditCardAuthorizationException();
					cce.setDisplayError(refundProperties.get(this.PROPERTY_ACTION_CODE_TEXT).toString());
					cce.setErrorMessage(refundProperties.get(this.PROPERTY_ERROR_TEXT).toString());
					cce.setErrorNumber(refundProperties.get(this.PROPERTY_ACTION_CODE).toString());
					throw cce;
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return refundProperties;
	}

	private String propertiesToString(Hashtable<String, String> properties) {
		StringBuffer strPostData = new StringBuffer();
		try {
			addProperties(strPostData, properties, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strPostData.toString();
	}

	private String getPostData(Hashtable<String, String> properties) {
		StringBuffer strPostData = new StringBuffer();
		try {
			appendProperty(strPostData, this.PROPERTY_PASSWORD, this.PASSWORD);
			addProperties(strPostData, properties, true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strPostData.toString();
	}

	@Override
	public String finishTransaction(String properties) throws KortathjonustanAuthorizationException {
		Hashtable<String, String> propertiesInHash = parseResponse(properties);
		LOGGER.info("Properties: " + properties + ", in hash: " + propertiesInHash);
		Hashtable<String, String> returnedCaptureProperties = finishTransaction(propertiesInHash);
		return finishWithCapturedResponse(returnedCaptureProperties, null);
	}

	private String finishWithCapturedResponse(Hashtable<String, String> returnedCaptureProperties, Object mainPaymentPK) throws KortathjonustanAuthorizationException {
		LOGGER.info("Captured properties: " + returnedCaptureProperties);
		try {
			this.storeAuthorizationEntry(null, mainPaymentPK, returnedCaptureProperties, KortathjonustanAuthorisationEntries.AUTHORIZATION_TYPE_DELAYED_TRANSACTION);
			return MapUtil.isEmpty(returnedCaptureProperties) || !returnedCaptureProperties.containsKey(this.PROPERTY_APPROVAL_CODE) ?
					null :
					returnedCaptureProperties.get(this.PROPERTY_APPROVAL_CODE).toString();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to save entry to database", e);
			throw new KortathjonustanAuthorizationException(e);
		}
	}

	@Override
	public CaptureResult getAuthorizationNumberForWebPayment(String properties) throws CreditCardAuthorizationException {
		if (auth != null && !StringUtil.isEmpty(auth.getPaymentId()) && !StringUtil.isEmpty(auth.getAuthorizationCode())) {
			LOGGER.info("Payment for " + auth + " (auth. code: " + auth.getAuthorizationCode() + ", payment ID: " + auth.getPaymentId() + ") was already captured");
			return new CaptureResult(auth.getAuthorizationCode(), null, null);
		}

		String protocol = "https://";
		boolean test = CreditCardUtil.isTestEnvironment();
		if (test) {
			this.HOST_NAME = protocol.concat("test.kortathjonustan.is");
			this.HOST_PORT = 8443;
		}
		String url = this.HOST_NAME + CoreConstants.COLON + this.HOST_PORT + REQUEST_TYPE_CAPTURE;
		if (!url.startsWith(protocol)) {
			url = protocol.concat(url);
		}
		Hashtable<String, String> propertiesInHash = parseResponse(properties);
		if (!MapUtil.isEmpty(propertiesInHash) && !StringUtil.isEmpty(this.USER)) {
			propertiesInHash.put(PROPERTY_USER, this.USER);
		}
		if (!MapUtil.isEmpty(propertiesInHash) && !StringUtil.isEmpty(this.PASSWORD)) {
			propertiesInHash.put(PROPERTY_PASSWORD, this.PASSWORD);
		}
		if (test) {
			propertiesInHash.put(PROPERTY_ACCEPTOR_IDENT, "8180001");
			propertiesInHash.put(PROPERTY_ACCEPTOR_TERM_ID, "90000001");
		}
		String data = getPostData(propertiesInHash);
		LOGGER.info("Properties: " + properties + ", in hash: " + propertiesInHash + ", data to send: " + data + " to URL: " + url);
		Long length = Integer.valueOf(data.length()).longValue();
		ClientResponse response = null;
		try {
			response = ConnectionUtil.getInstance().getResponseFromREST(url, length, "application/x-www-form-urlencoded", "POST", data, null, null);
		} catch (Exception e) {
			throw new KortathjonustanAuthorizationException("Error getting response from " + url, e);
		}
		if (response == null) {
			throw new KortathjonustanAuthorizationException("Unknown response from " + url);
		}
		if (response.getStatus() != Status.OK.getStatusCode()) {
			throw new KortathjonustanAuthorizationException("Response is not OK: " + response.getStatus() + ". " + response);
		}

		String strResponse = null;
		try {
			strResponse = StringHandler.getContentFromInputStream(response.getEntityInputStream());
		} catch (Exception e) {
			String error = "Error parsing response " + response;
			LOGGER.log(Level.WARNING, error, e);
			throw new KortathjonustanAuthorizationException(error);
		}
		if (strResponse == null) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("SendRequest returned null");
			cce.setErrorNumber("-");
			throw cce;
		} else if (!strResponse.startsWith(this.PROPERTY_ACTION_CODE)) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("Invalid response from host, should start with d39 [" + strResponse + "]");
			cce.setErrorNumber("-");
			throw cce;
		} else {
			Hashtable<String, String> captureProperties = parseResponse(strResponse);
			if (CODE_AUTHORIZATOIN_APPROVED.equals(captureProperties.get(this.PROPERTY_ACTION_CODE))) {
				String authCode = finishWithCapturedResponse(captureProperties, null);
				String transactionId = captureProperties.get(PROPERTY_ORIGINAL_DATA_ELEMENT);
				transactionId = StringUtil.isEmpty(transactionId) ? captureProperties.get(PROPERTY_TRANSACTION_ID) : transactionId;
				return new CaptureResult(authCode, transactionId, captureProperties);
			} else {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError(captureProperties.get(this.PROPERTY_ACTION_CODE_TEXT).toString());
				cce.setErrorMessage(captureProperties.get(this.PROPERTY_ERROR_TEXT).toString());
				cce.setErrorNumber(captureProperties.get(this.PROPERTY_ACTION_CODE).toString());
				throw cce;
			}
		}
	}

	private Hashtable<String, String> finishTransaction(Hashtable<String, String> properties) throws KortathjonustanAuthorizationException {
		// System.out.println(" ------ CAPTURE ------");
		Hashtable<String, String> captureProperties = new Hashtable<>();
		try {
			SSLClient client = getSSLClient();
			String strResponse = null;

			// System.out.println("strPostData [ "+strPostData.toString()+" ]");
			try {
				strResponse = client.sendRequest(REQUEST_TYPE_CAPTURE, getPostData(properties));
			} catch (Exception e) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest failed");
				cce.setErrorNumber("-");
				cce.setParentException(e);
				throw cce;
			}
			// System.out.println("Response [ "+strResponse+" ]");
			if (strResponse == null) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest returned null");
				cce.setErrorNumber("-");
				throw cce;
			} else if (!strResponse.startsWith(this.PROPERTY_ACTION_CODE)) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("Invalid response from host, should start with d39 [" + strResponse + "]");
				cce.setErrorNumber("-");
				throw cce;
			} else {
				captureProperties = parseResponse(strResponse);
				captureProperties.put(this.PROPERTY_CARD_BRAND_NAME, properties.get(this.PROPERTY_CARD_BRAND_NAME));
				if (CODE_AUTHORIZATOIN_APPROVED.equals(captureProperties.get(this.PROPERTY_ACTION_CODE))) {
					return captureProperties;
				} else {
					KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
					cce.setDisplayError(captureProperties.get(this.PROPERTY_ACTION_CODE_TEXT).toString());
					cce.setErrorMessage(captureProperties.get(this.PROPERTY_ERROR_TEXT).toString());
					cce.setErrorNumber(captureProperties.get(this.PROPERTY_ACTION_CODE).toString());
					throw cce;
				}
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error finishing transaction with properties " + properties, e);
		}

		return captureProperties;
	}

	private String getProperty(String name, String value) {
		return getProperty(name, value, false);
	}

	private String getProperty(String name, String value, boolean firstTime) {
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(value)) {
			return CoreConstants.EMPTY;
		}

		return (firstTime ? CoreConstants.EMPTY : CoreConstants.AMP).concat(name).concat(CoreConstants.EQ).concat(value);
	}

	/**
	 * Sets d4, de4, d41, d42, d49, d12, d31
	 *
	 * @param currency
	 * @param amount
	 * @param timestamp
	 * @param reference
	 * @return
	 * @throws CreditCardAuthorizationException
	 */
	private String getProperties(String currency, double amount, Timestamp timestamp, String reference, String approvalCode) throws CreditCardAuthorizationException {
		StringBuffer properties = new StringBuffer();

		setCurrencyAndAmount(currency, amount);
		properties.append(getProperty(this.PROPERTY_AMOUNT, strAmount, true));						//	d4
		properties.append(getProperty(this.PROPERTY_CURRENCY_EXPONENT, strCurrencyExponent));		//	de4

		String authTerminal = ccMerchant.getAuthorizationTerminal();
		authTerminal = StringUtil.isEmpty(authTerminal) ? this.ACCEPTOR_TERM_ID : authTerminal;
		properties.append(getProperty(this.PROPERTY_ACCEPTOR_TERM_ID, authTerminal));				//	d41
		properties.append(getProperty(this.PROPERTY_ACCEPTOR_IDENT, this.ACCEPTOR_IDENTIFICATION));	//	d42
		properties.append(getProperty(this.PROPERTY_CURRENCY_CODE, strCurrencyCode));				//	d49

		//	d12
		IWTimestamp stamp = new IWTimestamp(timestamp.getTime());
		if (StringUtil.isEmpty(approvalCode)) {
			strCurrentDate = getDateString(stamp);
		} else {
			strCurrentDate = stamp.getDateString("yyMMdd");
			strCurrentDate = strCurrentDate.concat(approvalCode);
		}
		properties.append(getProperty(this.PROPERTY_CURRENT_DATE, strCurrentDate));

		//	d31
		strReferenceNumber = reference;
		properties.append(getProperty(this.PROPERTY_REFERENCE_ID, strReferenceNumber));

		String result = properties.toString();
		LOGGER.info("Properties for " + amount + " at " + timestamp + ": " + result);
		return result;
	}

	@Override
	public String getPropertiesToCaptureWebPayment(String currency, double amount, Timestamp timestamp, String reference, String approvalCode) throws CreditCardAuthorizationException {
		StringBuffer properties = new StringBuffer(getProperties(currency, amount, timestamp, reference, approvalCode));

		//	d38
		properties.append(getProperty(this.PROPERTY_APPROVAL_CODE, approvalCode));

		String result = properties.toString();
		LOGGER.info("Properties to capture web payment " + approvalCode + " for " + amount + " at " + timestamp + ": " + result);
		return result;
	}

	private Hashtable<String, String> getFirstResponse() throws KortathjonustanAuthorizationException {
		Hashtable<String, String> properties = null;
		// System.out.println(" ------ REQUEST ------");

		// long lStartTime = System.currentTimeMillis();
		try {
			SSLClient client = getSSLClient();

			StringBuffer strPostData = new StringBuffer();
			appendProperty(strPostData, this.PROPERTY_SITE, this.SITE);// "site=22"
			appendProperty(strPostData, this.PROPERTY_USER, this.USER);
			appendProperty(strPostData, this.PROPERTY_PASSWORD, this.PASSWORD);
			appendProperty(strPostData, this.PROPERTY_ACCEPTOR_TERM_ID, this.ACCEPTOR_TERM_ID);
			appendProperty(strPostData, this.PROPERTY_ACCEPTOR_IDENT, this.ACCEPTOR_IDENTIFICATION);
			appendProperty(strPostData, this.PROPERTY_CC_NUMBER, this.strCCNumber);
			appendProperty(strPostData, this.PROPERTY_CC_EXPIRE, this.strCCExpire);
			appendProperty(strPostData, this.PROPERTY_AMOUNT, this.strAmount);
			appendProperty(strPostData, this.PROPERTY_CURRENCY_CODE, this.strCurrencyCode);
			appendProperty(strPostData, this.PROPERTY_CURRENCY_EXPONENT, this.strCurrencyExponent);
			appendProperty(strPostData, this.PROPERTY_CARDHOLDER_NAME, this.strName);
			appendProperty(strPostData, this.PROPERTY_REFERENCE_ID, this.strReferenceNumber);
			appendProperty(strPostData, this.PROPERTY_CURRENT_DATE, this.strCurrentDate);
			appendProperty(strPostData, this.PROPERTY_CC_VERIFY_CODE, this.strCCVerify);
			addDefautProperties(strPostData);

			String strResponse = null;

			// System.out.println("Request [" + strPostData.toString() + "]");
			try {
				strResponse = client.sendRequest(REQUEST_TYPE_AUTHORIZATION, strPostData.toString());
				// System.out.println("[Korta] strResponse = "+strResponse);
			} catch (Exception e) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest failed");
				cce.setErrorNumber("-");
				cce.setParentException(e);
				e.printStackTrace();
				throw cce;
			}
			// System.out.println("Response [" + strResponse + "]");

			if (strResponse == null) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("SendRequest returned null");
				cce.setErrorNumber("-");
				throw cce;
			} else if (!strResponse.startsWith(this.PROPERTY_ACTION_CODE)) {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError("Cannot connect to Central Payment Server");
				cce.setErrorMessage("Invalid response from host, should start with d39 [" + strResponse + "]");
				cce.setErrorNumber("-");
				throw cce;
			} else {
				properties = parseResponse(strResponse);
				if (CODE_AUTHORIZATOIN_APPROVED.equals(properties.get(this.PROPERTY_ACTION_CODE))) {
					return properties;
				} else {
					KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
					try {
						cce.setDisplayError(properties.get(this.PROPERTY_ACTION_CODE_TEXT).toString());
					} catch (NullPointerException n) {
					}
					try {
						cce.setErrorMessage(properties.get(this.PROPERTY_ERROR_TEXT).toString());
					} catch (NullPointerException n) {
					}
					try {
						cce.setErrorNumber(properties.get(this.PROPERTY_ACTION_CODE).toString());
					} catch (NullPointerException n) {
					}
					throw cce;
				}
			}

		} catch (UnsupportedEncodingException e) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("UnsupportedEncodingException");
			cce.setErrorNumber("-");
			cce.setParentException(e);
			throw cce;
		}

	}

	private static final File getKeyStoreFile(String path, IWBundle bundle) {
		String virtualFile = null;
		try {
			if (path.startsWith("resources/")) {
				path = StringHandler.replace(path, "resources/", CoreConstants.EMPTY);
			} else if (path.startsWith("properties/")) {
				path = StringHandler.replace(path, "properties/", CoreConstants.EMPTY);
			}
			virtualFile = bundle.getVirtualPathWithFileNameString(path);
			return IWBundleResourceFilter.copyResourceFromJarToWebapp(IWMainApplication.getDefaultIWMainApplication(), virtualFile);
		} catch (Exception e) {
			String message = "Error copying file '" + virtualFile + "' from JAR to web app";
			Logger.getLogger(SSLClient.class.getName()).log(Level.WARNING, virtualFile, e);
			CoreUtil.sendExceptionNotification(message, e);
		}
		return null;
	}

	/**
	 * @return @throws IOException
	 */
	private SSLClient getSSLClient() throws KortathjonustanAuthorizationException {
		File keystore = getKeyStoreFile(this.strKeystore, this.bundle);
		if (keystore == null) {
			Logger.getLogger(SSLClient.class.getName()).warning("Unable to copy file " + strKeystore + " from " + bundle.getBundleIdentifier());
		}

		SSLClient client;
		try {
			String tmp = this.strKeystore;
			if (!tmp.startsWith("/")) { // Backwards compatability when this
										// looked like this /home/idegaweb/...
										// (marathon.is)
				tmp = this.bundle.getBundleBaseRealPath() + "/" + this.strKeystore;
			}
			client = new SSLClient(this.HOST_NAME, this.HOST_PORT, tmp, this.strKeystorePass, this.USER, this.PASSWORD);
		} catch (Exception e) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("Cannot get SSLClient instance");
			cce.setErrorNumber("-");
			cce.setParentException(e);
			throw cce;
		}
		return client;
	}

	private void addDefautProperties(StringBuffer strPostData) throws UnsupportedEncodingException {
		// appendProperty(strPostData, , );
		// appendProperty(strPostData, PROPERTY_MERCHANT_LANGUAGE, "is");
		// appendProperty(strPostData, PROPERTY_CLIENT_LANGUAGE, "is");
		// appendProperty(strPostData, PROPERTY_CLIENT_IP, "80.62.56.56");
	}

	private Hashtable<String, String> parseResponse(String response) {
		return parseResponse(response, false);
	}

	private Hashtable<String, String> parseResponse(String response, boolean listOnly) {
		Hashtable<String, String> responseElements = new Hashtable<>();
		int index = 0;
		int tmpIndex = 0;
		String tmpString;
		String key, value;
		responseElements.put(this.PROPERTY_TOTAL_RESPONSE, response);
		while (index >= 0) {
			tmpIndex = response.indexOf("&");
			tmpString = response.substring(0, tmpIndex);
			response = response.substring(tmpIndex + 1, response.length());
			index = response.indexOf("&");
			if (tmpString.indexOf("=") > -1) {
				key = tmpString.substring(0, tmpString.indexOf("="));
				value = tmpString.substring(tmpString.indexOf("=") + 1, tmpString.length());
				if (listOnly) {
					System.out.println(tmpString + " (" + key + "," + value + ")");
				} else {
					// System.out.println(tmpString+" ("+key+","+value+")");
					responseElements.put(key, value);
				}
			}
		}
		if (response.indexOf("=") > -1) {
			key = response.substring(0, response.indexOf("="));
			value = response.substring(response.indexOf("=") + 1, response.length());
			if (listOnly) {
				System.out.println(response + " (" + key + "," + value + ")");
			} else {
				responseElements.put(key, value);
			}
		}
		LOGGER.info("Response: " + responseElements);
		return responseElements;
	}

	private void addProperties(StringBuffer buffer, Hashtable<String, String> properties, boolean urlEncode)
			throws UnsupportedEncodingException {
		Set<String> keys = properties.keySet();
		Iterator<String> iter = keys.iterator();
		if (iter != null) {
			String key;
			while (iter.hasNext()) {
				key = iter.next().toString();
				appendProperty(buffer, key, properties.get(key).toString(), urlEncode);
			}
		}
	}

	private void appendProperty(StringBuffer buffer, String propertyName, String propertyValue)
			throws UnsupportedEncodingException {
		appendProperty(buffer, propertyName, propertyValue, true);
	}

	private void appendProperty(StringBuffer buffer, String propertyName, String propertyValue, boolean urlEncode)
			throws UnsupportedEncodingException {
		if (propertyValue != null) {
			if (urlEncode) {
				buffer.append("&").append(propertyName).append("=")
						.append(URLEncoder.encode(propertyValue, "ISO-8859-1"));
			} else {
				buffer.append("&").append(propertyName).append("=").append(propertyValue);
			}
			// buffer.append("&").append(propertyName).append("=").append(URLEncoder.encode(propertyValue,
			// "UTF-8"));
		}
	}

	// private String encodeBase64(String _strData) {
	// Base64 oB64 = new Base64();
	// return oB64.encode(_strData.getBytes());
	// }

	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("kortathjo");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.idega.block.creditcard.business.CreditCardClient#
	 * getCreditCardMerchant()
	 */
	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return this.ccMerchant;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.idega.block.creditcard.business.CreditCardClient#
	 * supportsDelayedTransactions()
	 */
	@Override
	public boolean supportsDelayedTransactions() {
		return true;
	}

	@Override
	public String getExpireDateString(String month, String year) {
		return year + month;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		if (auth == null) {
			try {
				KortathjonustanAuthorisationEntriesHome authHome = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntries.class);
				auth = authHome.create();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error getting auth. entry for " + getClass().getSimpleName(), e);
			}
		}
		return auth;
	}

	@Override
	public void setAuthorizationEntry(CreditCardAuthorizationEntry entry) {
		if (entry instanceof KortathjonustanAuthorisationEntries) {
			this.auth = (KortathjonustanAuthorisationEntries) entry;
		}
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		throw new CreditCardAuthorizationException("Not implemented for korta cliens");
	}

	@Override
	public String doSaleWithCardToken(
			String cardToken,
			String transactionId,
			double amount,
			String currency,
			String referenceNumber,
			Object parentPaymentPK
	) throws CreditCardAuthorizationException {
		if (
				StringUtil.isEmpty(cardToken) ||
				StringUtil.isEmpty(transactionId) ||
				amount <= 0 ||
				StringUtil.isEmpty(currency) ||
				StringUtil.isEmpty(referenceNumber)
		) {
			String error = "Invalid parameters";
			LOGGER.warning(error);
			throw new CreditCardAuthorizationException(error);
		}

		//	Sets d4, de4, d41, d42, d49, d12, d31
		String properties = getProperties(currency, amount, new Timestamp(System.currentTimeMillis()), referenceNumber, null);
		if (StringUtil.isEmpty(properties)) {
			String error = "Invalid properties";
			LOGGER.warning(error);
			throw new CreditCardAuthorizationException(error);
		}

		String protocol = "https://";
		boolean test = CreditCardUtil.isTestEnvironment();
		if (test) {
			this.HOST_NAME = protocol.concat("test.kortathjonustan.is");
			this.HOST_PORT = 8443;
		}
		String url = this.HOST_NAME + CoreConstants.COLON + this.HOST_PORT + REQUEST_TYPE_AUTHORIZATION;
		if (!url.startsWith(protocol)) {
			url = protocol.concat(url);
		}
		Hashtable<String, String> propertiesInHash = parseResponse(properties);
		if (MapUtil.isEmpty(propertiesInHash)) {
			String error = "Invalid parsed properties from " + properties;
			LOGGER.warning(error);
			throw new CreditCardAuthorizationException(error);
		}

		if (!StringUtil.isEmpty(this.USER)) {
			propertiesInHash.put(PROPERTY_USER, this.USER);
		}
		if (!StringUtil.isEmpty(this.PASSWORD)) {
			propertiesInHash.put(PROPERTY_PASSWORD, this.PASSWORD);
		}
		propertiesInHash.put("capture", Boolean.TRUE.toString());
		propertiesInHash.put("daskm", cardToken);
		propertiesInHash.put("initiatedby", "M");
		propertiesInHash.put("cof", "U");
		propertiesInHash.put(PROPERTY_TRANSACTION_ID, transactionId);
		propertiesInHash.put("d22cp", "RPA");
		if (test) {
			propertiesInHash.put(PROPERTY_ACCEPTOR_IDENT, "8180001");
			propertiesInHash.put(PROPERTY_ACCEPTOR_TERM_ID, "90000001");
		}

		//	Resetting auth. entry not to mix up with parent payment
		if (this.auth != null && this.auth.getPrimaryKey() != null && parentPaymentPK != null && this.auth.getPrimaryKey().toString().equals(parentPaymentPK.toString())) {
			this.auth = null;
		}

		String data = getPostData(propertiesInHash);
		LOGGER.info("Properties: " + properties + ", in hash: " + propertiesInHash + ", data to send to sale with card token: " + data + " to URL: " + url);
		Long length = Integer.valueOf(data.length()).longValue();
		ClientResponse response = null;
		try {
			response = ConnectionUtil.getInstance().getResponseFromREST(url, length, "application/x-www-form-urlencoded", "POST", data, null, null);
		} catch (Exception e) {
			throw new KortathjonustanAuthorizationException("Error getting response from " + url, e);
		}
		if (response == null) {
			throw new KortathjonustanAuthorizationException("Unknown response from " + url);
		}
		if (response.getStatus() != Status.OK.getStatusCode()) {
			throw new KortathjonustanAuthorizationException("Response is not OK: " + response.getStatus() + ". " + response);
		}

		String strResponse = null;
		try {
			strResponse = StringHandler.getContentFromInputStream(response.getEntityInputStream());
		} catch (Exception e) {
			String error = "Error parsing response " + response;
			LOGGER.log(Level.WARNING, error, e);
			throw new KortathjonustanAuthorizationException(error);
		}
		if (strResponse == null) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("SendRequest returned null");
			cce.setErrorNumber("-");
			throw cce;
		} else if (!strResponse.startsWith(this.PROPERTY_ACTION_CODE)) {
			KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
			cce.setDisplayError("Cannot connect to Central Payment Server");
			cce.setErrorMessage("Invalid response from host, should start with d39 [" + strResponse + "]");
			cce.setErrorNumber("-");
			throw cce;
		} else {
			Hashtable<String, String> captureProperties = parseResponse(strResponse);
			if (CODE_AUTHORIZATOIN_APPROVED.equals(captureProperties.get(this.PROPERTY_ACTION_CODE))) {
				String authCode = finishWithCapturedResponse(captureProperties, parentPaymentPK);
				return authCode;
			} else {
				KortathjonustanAuthorizationException cce = new KortathjonustanAuthorizationException();
				cce.setDisplayError(captureProperties.get(this.PROPERTY_ACTION_CODE_TEXT).toString());
				cce.setErrorMessage(captureProperties.get(this.PROPERTY_ERROR_TEXT).toString());
				cce.setErrorNumber(captureProperties.get(this.PROPERTY_ACTION_CODE).toString());
				throw cce;
			}
		}
	}

}