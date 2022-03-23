package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class ValitorPayPaymentData extends ValitorPayMainRequestData implements Serializable {

	private static final long serialVersionUID = 6388603347171361654L;

	private String merchantReentryUrl; //EXAMPLE: "https://example.com/orderstatus?orderid=00000000-0000-0000-0000-000000000000",
	private String merchantWebhookUrl; //EXAMPLE: "https://example.com/webhook",
	private String merchantReferenceId; //EXAMPLE:  "00000000-0000-0000-0000-000000000000",
	private String cvc;

	private ValitorPayVirtualCardData virtualCardData;

	//Payment with virtual card / real card
	private String operation;
	private String transactionType;
	private String virtualCardNumber;

	private String systemCalling;

	private ValitorPayVirtualCardAdditionalData virtualCardPaymentAdditionalData;

	private ValitorPayCardVerificationResponseData cardVerificationData;

	private ValitorPayPaymentAdditionalData additionalData;

	private FirstTransactionData firstTransactionData;

	public ValitorPayPaymentData() {
		super();

		systemCalling = IWMainApplication.getDefaultIWMainApplication().getSettings().getProperty("valitor_pay.caller");
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
		this.cvc = cvc;
		this.virtualCardData = virtualCardData;

		setAmount(amount);
		setCurrency(currency);
		setCardNumber(cardNumber);
		setExpirationMonth(expirationMonth);
		setExpirationYear(expirationYear);
		setCardholderDeviceType(cardholderDeviceType);
	}

	public ValitorPayPaymentData(
			String operation,
			String currency,
			Integer amount,
			String virtualCardNumber,
			ValitorPayVirtualCardAdditionalData virtualCardAdditionalData
	) {
		this();

		this.operation = operation;
		this.virtualCardNumber = virtualCardNumber;
		this.virtualCardPaymentAdditionalData = virtualCardAdditionalData;

		setAmount(amount);
		setCurrency(currency);

		setCardholderDeviceType(null);
	}

	public ValitorPayPaymentData(
			String merchantReferenceId,
			Integer amount,
			String currency,
			String cardNumber,
			String expirationMonth,
			String expirationYear,
			String cvc,
			String cardholderDeviceType,
			ValitorPayCardVerificationResponseData valitorPayCardVerificationResponseData,
			String displayName
	) {
		this();

		this.cvc = cvc;
		this.cardVerificationData = valitorPayCardVerificationResponseData;

		if (!StringUtil.isEmpty(merchantReferenceId)) {
			ValitorPayPaymentAdditionalData additionalData = new ValitorPayPaymentAdditionalData(merchantReferenceId);
			this.additionalData = StringUtil.isEmpty(additionalData.getMerchantReferenceData()) ? null : additionalData;
		}

		setAmount(amount);
		setCurrency(currency);
		setCardNumber(cardNumber);
		setExpirationMonth(expirationMonth);
		setExpirationYear(expirationYear);
		setCardholderDeviceType(cardholderDeviceType);
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

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public ValitorPayVirtualCardData getVirtualCardData() {
		return virtualCardData;
	}

	public void setVirtualCardData(ValitorPayVirtualCardData virtualCardData) {
		this.virtualCardData = virtualCardData;
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

	public ValitorPayVirtualCardAdditionalData getVirtualCardPaymentAdditionalData() {
		return virtualCardPaymentAdditionalData;
	}

	public void setVirtualCardPaymentAdditionalData(ValitorPayVirtualCardAdditionalData virtualCardPaymentAdditionalData) {
		this.virtualCardPaymentAdditionalData = virtualCardPaymentAdditionalData;
	}

	public ValitorPayCardVerificationResponseData getCardVerificationData() {
		return cardVerificationData;
	}

	public void setCardVerificationData(ValitorPayCardVerificationResponseData cardVerificationData) {
		this.cardVerificationData = cardVerificationData;
	}

	public ValitorPayPaymentAdditionalData getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(ValitorPayPaymentAdditionalData additionalData) {
		this.additionalData = additionalData;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public FirstTransactionData getFirstTransactionData() {
		return firstTransactionData;
	}

	public void setFirstTransactionData(FirstTransactionData firstTransactionData) {
		this.firstTransactionData = firstTransactionData;
	}

	public String getSystemCalling() {
		return systemCalling;
	}

	public void setSystemCalling(String systemCalling) {
		this.systemCalling = systemCalling;
	}

	@Override
	public String toString() {
		return	"Name on card: " + getDisplayName() +
				", number: " + (StringUtil.isEmpty(getCardNumber()) ? getVirtualCardNumber() : CreditCardUtil.getMaskedCreditCardNumber(getCardNumber())) +
				", expires (MM/YY): " + getExpirationMonth() + CoreConstants.SLASH + getExpirationYear() +
				". Virtual card data: " + getVirtualCardData() + ". First transaction data: " + getFirstTransactionData();
	}

}