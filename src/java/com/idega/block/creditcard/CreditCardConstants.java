package com.idega.block.creditcard;

public interface CreditCardConstants {

	public static final String CREDIT_CARD_MERCHANT_ID = "CC_MERCHANT_ID";

	public static final String REDIRECTION_URL = "REDIRECTION_URL";

	public static final String METADATA_SUCCESS = "SUCCESS";

	public static final String CARD_HOLDER_DEVICE_TYPE_WWW = "WWW";

	public static final String OPERATION_SALE = "Sale";
	public static final String TRANSACTION_TYPE_ECOMMERCE = "ECommerce";

	public static final String FULL_DATE_TIME_NO_DELIMITERS_STRING = "yyyyMMddHHmmssSSSS";

	public static final String METADATA_CARD_VERIFICATION_MDSTATUS = "CARD_VERIFICATION_MDSTATUS";
	public static final String METADATA_CARD_VERIFICATION_CAVV = "METADATA_CARD_VERIFICATION_CAVV";
	public static final String METADATA_CARD_XID = "METADATA_CARD_XID";

	public static final String SUBSEQUENT_TRANSACTION_TYPE_RECURRING = "MerchantInitiatedRecurring";
	public static final String INITIATION_REASON_RECURRING = "Recurring";

	public static final String APP_PROPERTY_CC_PAYMENT_INFO_ID = "CC_PAYMENT_INFO_ID";
	public static final String DEFAULT_CC_PAYMENT_INFO_ID = "1";
	public static final String APP_PROPERTY_CC_SUPPLIER_ID = "CC_SUPPLIER_ID";
	public static final String DEFAULT_CC_SUPPLIER_ID = "1";

	public static final String PATH_TO_CC_DOTENV_FILE = "../dotenv_cc_merchant";

	public static final String APP_PROP_PATH_TO_CC_DOTENV_FILE = "PATH_TO_CC_DOTENV_FILE";

}