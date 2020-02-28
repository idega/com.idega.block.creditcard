package com.idega.block.creditcard;

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

}