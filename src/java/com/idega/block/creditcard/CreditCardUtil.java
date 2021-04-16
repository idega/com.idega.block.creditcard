package com.idega.block.creditcard;

import com.idega.block.trade.business.CurrencyHolder;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class CreditCardUtil {

	public static final String getMaskedCreditCardNumber(String creditCardNumber) {
		if (StringUtil.isEmpty(creditCardNumber)) {
			return null;
		}

		return creditCardNumber.length() <= 4 ? creditCardNumber : "****-****-****-".concat(creditCardNumber.substring(creditCardNumber.length() - 4));
	}

	public static final String getMaskedSecurityCode(String securityCode) {
		if (StringUtil.isEmpty(securityCode)) {
			return null;
		}

		String masked = CoreConstants.EMPTY;
		for (int i = 0; i < securityCode.length(); i++) {
			masked = masked.concat(CoreConstants.STAR);
		}

		return masked;
	}

	public static final boolean isTestEnvironment() {
		return IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("test_env_credit_card", false);
	}

	public static final String getCurrencyAbbreviation(String currencyCode) {
		switch (currencyCode) {
		case "352":
			return CurrencyHolder.ICELANDIC_KRONA;

		case "840":
			return CurrencyHolder.USA_DOLLAR;

		case "826":
			return CurrencyHolder.BRITISH_POUND;

		case "208":
			return CurrencyHolder.DANISH_KRONA;

		case "978":
			return CurrencyHolder.EURO;

		case "752":
			return CurrencyHolder.SWEDISH_KRONA;

		case "578":
			return CurrencyHolder.NORWEGIAN_KRONA;

		default:
			return currencyCode;
		}
	}
	}

}