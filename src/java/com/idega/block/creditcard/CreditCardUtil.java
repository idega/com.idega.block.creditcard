package com.idega.block.creditcard;

import com.idega.util.StringUtil;

public class CreditCardUtil {

	public static final String getMaskedCreditCardNumber(String creditCardNumber) {
		if (StringUtil.isEmpty(creditCardNumber)) {
			return null;
		}

		return creditCardNumber.length() <= 4 ? creditCardNumber : "****-****-****-".concat(creditCardNumber.substring(creditCardNumber.length() - 4));
	}

}