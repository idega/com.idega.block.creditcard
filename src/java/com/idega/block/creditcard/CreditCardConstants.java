package com.idega.block.creditcard;

import com.google.gson.Gson;
import com.idega.util.CoreConstants;

public interface CreditCardConstants {

	public static final String	CREDIT_CARD_MERCHANT_ID = "CC_MERCHANT_ID",

								REDIRECTION_URL = "REDIRECTION_URL",

								SUCCESS = "SUCCESS",
								COMPLETED = "COMPLETED",
								METADATA_SUCCESS = SUCCESS,
								PAYMENT_COMPLETED = "PAYMENT_COMPLETED",
								PAYMENT_SUCCEEDED = "PAYMENT_SUCCEEDED",

								CARD = "card",
								CLOSED = "CLO",
								ACTIVE = "ACT",

								CARD_HOLDER_DEVICE_TYPE_WWW = "WWW",

								OPERATION_SALE = "Sale",
								TRANSACTION_TYPE_ECOMMERCE = "ECommerce",

								FULL_DATE_TIME_NO_DELIMITERS_STRING = "yyyyMMddHHmmssSSSS",

								METADATA_CARD_VERIFICATION_MDSTATUS = "CARD_VERIFICATION_MDSTATUS",
								METADATA_CARD_VERIFICATION_CAVV = "METADATA_CARD_VERIFICATION_CAVV",
								METADATA_CARD_XID = "METADATA_CARD_XID",

								SUBSEQUENT_TRANSACTION_TYPE_RECURRING = "MerchantInitiatedRecurring",
								INITIATION_REASON_RECURRING = "Recurring",

								QUALIFIER_FINANCE_SERVICE = "ccFinanceService",

								IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	public static final Gson GSON = CoreConstants.GSON;

	public static final String APP_PROPERTY_CC_PAYMENT_INFO_ID = "CC_PAYMENT_INFO_ID";
	public static final String DEFAULT_CC_PAYMENT_INFO_ID = "1";
	public static final String APP_PROPERTY_CC_SUPPLIER_ID = "CC_SUPPLIER_ID";
	public static final String DEFAULT_CC_SUPPLIER_ID = "1";

	public static final String PATH_TO_CC_DOTENV_FILE = "../dotenv_cc_merchant";

	public static final String APP_PROP_PATH_TO_CC_DOTENV_FILE = "PATH_TO_CC_DOTENV_FILE";

}