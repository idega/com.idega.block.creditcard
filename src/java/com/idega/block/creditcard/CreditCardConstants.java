package com.idega.block.creditcard;

import com.google.gson.Gson;

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

	public static final Gson GSON = new Gson();

}