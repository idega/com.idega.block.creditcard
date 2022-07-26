package com.idega.block.creditcard;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.CoreConstants;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;

public class CreditCardUtil {

	public static final String getCardBrand(String cardNumber) {
		cardNumber = StringHandler.getNumbersOnly(cardNumber);
		if (StringUtil.isEmpty(cardNumber)) {
			return null;
		}

		List<AdvancedProperty> data = Arrays.asList(
				new AdvancedProperty("Visa", "^4[0-9]{0,}$"),
				new AdvancedProperty("MasterCard", "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]{0,}$"),
				new AdvancedProperty("JCB", "^(?:2131|1800|35)[0-9]{0,}$"),
				new AdvancedProperty("Amex", "^3[47][0-9]{0,}$"),
				new AdvancedProperty("Diners", "^3(?:0[0-59]{1}|[689])[0-9]{0,}$"),
				new AdvancedProperty("Maestro", "^(5[06789]|6)[0-9]{0,}$"),
				new AdvancedProperty("Discover", "^(6011|65|64[4-9]|62212[6-9]|6221[3-9]|622[2-8]|6229[01]|62292[0-5])[0-9]{0,}$")
		);
		for (AdvancedProperty patternData: data) {
			Pattern pattern = Pattern.compile(patternData.getValue());
			Matcher matcher = pattern.matcher(cardNumber);
			if (matcher.matches()) {
				return patternData.getId();
			}
		}

		return null;
	}

	public static final String getMaskedCreditCardNumber(String creditCardNumber) {
		if (StringUtil.isEmpty(creditCardNumber)) {
			return null;
		}

		int length = creditCardNumber.length();

		//	CVC?
		if (length <= 3) {
			int to = length - 1;
			StringBuilder masked = new StringBuilder();
			for (int i = 0; i < to; i++) {
				masked.append(CoreConstants.STAR);
			}
			masked.append(creditCardNumber.substring(to));
			return masked.toString();
		}

		//	Card number
		return length <= 4 ? creditCardNumber : "****-****-****-".concat(creditCardNumber.substring(length - 4));
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

	public static final String getCurrencySymbol(Locale localeForCurrency) throws NullPointerException, IllegalArgumentException {
		if (localeForCurrency == null) {
			return null;
		}

		return getCurrencySymbol(Currency.getInstance(localeForCurrency));
	}

	public static final String getCurrencySymbol(String currencyCode) throws NullPointerException, IllegalArgumentException {
		if (StringUtil.isEmpty(currencyCode)) {
			return null;
		}

		return getCurrencySymbol(Currency.getInstance(currencyCode));
	}

	private static final String getCurrencySymbol(Currency currency) {
		String defaultSymbol = "kr.";
		if (currency == null) {
			return defaultSymbol;
		}

		String symbol = currency.getSymbol();
		if (StringUtil.isEmpty(symbol)) {
			return defaultSymbol;
		}

		switch (symbol) {
		case CurrencyHolder.ICELANDIC_KRONA:
			return defaultSymbol;

		default:
			return symbol;
		}
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

	public static final int getAmountWithExponents(double amount, String currencyExponent) throws CreditCardAuthorizationException {
		if (amount < 0) {
			throw new CreditCardAuthorizationException("Invalid amount: " + amount);
		}
		if (StringUtil.isEmpty(currencyExponent)) {
			throw new CreditCardAuthorizationException("Invalid currency exponent: " + currencyExponent);
		}

		if (amount == Double.valueOf(0).doubleValue()) {
			return 0;
		}

		try {
			int amountMultiplier = (int) Math.pow(10, Double.parseDouble(currencyExponent));
			return (int) amount * amountMultiplier;
		} catch (Throwable t) {
			String error = "Error getting amount (" + amount + ") with exponent (" + currencyExponent + "). " + t.getMessage();
			throw new CreditCardAuthorizationException(error, t);
		}
	}

}