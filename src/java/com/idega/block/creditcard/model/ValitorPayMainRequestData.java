package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class ValitorPayMainRequestData implements Serializable {
	private static final long serialVersionUID = 2008812189009666491L;

	private String cardNumber;
	private String expirationMonth;
	private String expirationYear;
	private String cardholderDeviceType = CreditCardConstants.CARD_HOLDER_DEVICE_TYPE_WWW;
	private Integer amount;
	private String currency = CurrencyHolder.ICELANDIC_KRONA;
	private String displayName;

	public ValitorPayMainRequestData() {
		super();
	}


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}


	public String getCardholderDeviceType() {
		return cardholderDeviceType;
	}

	public void setCardholderDeviceType(String cardholderDeviceType) {
		this.cardholderDeviceType = cardholderDeviceType;
	}


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	@Override
	public String toString() {
		return	"Name on card: " + getDisplayName() +
				", number: " + (StringUtil.isEmpty(getCardNumber()) ? CoreConstants.EMPTY : CreditCardUtil.getMaskedCreditCardNumber(getCardNumber())) +
				", expires (MM/YY): " + getExpirationMonth() + CoreConstants.SLASH + getExpirationYear();
	}

}