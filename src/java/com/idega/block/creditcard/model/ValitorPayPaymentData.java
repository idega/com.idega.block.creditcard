package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class ValitorPayPaymentData implements Serializable {

	private static final long serialVersionUID = 6388603347171361654L;

	public static final String CARD_HOLDER_DEVICE_TYPE_WWW = "WWW";
	public static final String OPERATION_SALE = "Sale";

	private String merchantReentryUrl; //EXAMPLE: "https://example.com/orderstatus?orderid=00000000-0000-0000-0000-000000000000",
	private String merchantWebhookUrl; //EXAMPLE: "https://example.com/webhook",
	private String merchantReferenceId; //EXAMPLE:  "00000000-0000-0000-0000-000000000000",
	private Integer amount;
	private String currency = CurrencyHolder.ICELANDIC_KRONA;
	private String cardNumber;
	private String expirationMonth;
	private String expirationYear;
	private String cvc;
	private String cardholderDeviceType = CARD_HOLDER_DEVICE_TYPE_WWW;
	private String displayName;

	private ValitorPayVirtualCardData virtualCardData;

	//Payment with virtual card
	private String operation;
	private String virtualCardNumber;

	private ValitorPayVirtualCardAdditionalData virtualCardAdditionalData;

	public ValitorPayPaymentData() {
		super();
	}

	public ValitorPayPaymentData(
			String merchantReentryUrl,
			String merchantWebhookUrl,
			String merchantReferenceId,
			Integer amount,
			String currency,
			String cardNumber,
			String expirationMonth,
			String expirationYear,
			String cvc,
			String cardholderDeviceType,
			ValitorPayVirtualCardData virtualCardData,
			String displayName
	) {
		this();

		this.merchantReentryUrl = merchantReentryUrl;
		this.merchantWebhookUrl = merchantWebhookUrl;
		this.merchantReferenceId = merchantReferenceId;
		this.amount = amount;
		this.currency = currency;
		this.cardNumber = cardNumber;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.cvc = cvc;
		this.cardholderDeviceType = cardholderDeviceType;
		this.virtualCardData = virtualCardData;
		//this.displayName = displayName;
	}

	public ValitorPayPaymentData(
			String operation,
			String currency,
			Integer amount,
			String merchantReferenceId,
			ValitorPayVirtualCardAdditionalData virtualCardAdditionalData
	) {
		this();

		this.operation = operation;
		this.currency = currency;
		this.amount = amount;
		this.merchantReferenceId = merchantReferenceId;
		this.virtualCardAdditionalData = virtualCardAdditionalData;
	}

	public String getMerchantReentryUrl() {
		return merchantReentryUrl;
	}

	public void setMerchantReentryUrl(String merchantReentryUrl) {
		this.merchantReentryUrl = merchantReentryUrl;
	}

	public String getMerchantWebhookUrl() {
		return merchantWebhookUrl;
	}

	public void setMerchantWebhookUrl(String merchantWebhookUrl) {
		this.merchantWebhookUrl = merchantWebhookUrl;
	}

	public String getMerchantReferenceId() {
		return merchantReferenceId;
	}

	public void setMerchantReferenceId(String merchantReferenceId) {
		this.merchantReferenceId = merchantReferenceId;
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

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public String getCardholderDeviceType() {
		return cardholderDeviceType;
	}

	public void setCardholderDeviceType(String cardholderDeviceType) {
		this.cardholderDeviceType = cardholderDeviceType;
	}

	public ValitorPayVirtualCardData getVirtualCardData() {
		return virtualCardData;
	}

	public void setVirtualCardData(ValitorPayVirtualCardData virtualCardData) {
		this.virtualCardData = virtualCardData;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getVirtualCardNumber() {
		return virtualCardNumber;
	}

	public void setVirtualCardNumber(String virtualCardNumber) {
		this.virtualCardNumber = virtualCardNumber;
	}

	public ValitorPayVirtualCardAdditionalData getVirtualCardAdditionalData() {
		return virtualCardAdditionalData;
	}

	public void setVirtualCardAdditionalData(ValitorPayVirtualCardAdditionalData virtualCardAdditionalData) {
		this.virtualCardAdditionalData = virtualCardAdditionalData;
	}

	@Override
	public String toString() {
		return	"Name on card: " + getDisplayName() +
				", number: " + (StringUtil.isEmpty(getCardNumber()) ? getVirtualCardNumber() : CreditCardUtil.getMaskedCreditCardNumber(getCardNumber())) +
				", expires (MM/YY): " + getExpirationMonth() + CoreConstants.SLASH + getExpirationYear() +
				". Virtual card data: " + getVirtualCardData();
	}

}