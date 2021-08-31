package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.util.CoreConstants;

public class ValitorPayVirtualCardData implements Serializable {

	private static final long serialVersionUID = 2832777737979590086L;

	public static final String SUBSEQUENT_TRANSACTION_TYPE = "CardholderInitiatedCredentialOnFile";

	private String subsequentTransactionType = SUBSEQUENT_TRANSACTION_TYPE;

	private String virtualCardNumber; //Virtual card UUID - token from VirtualCard object
	private Integer expirationMonth;
	private Integer expirationYear;
	private String sponsoredMerchantId;
	private String agreementNumber;
	private String terminalId;

	private String virtualCard; //Virtual card UUID - token from VirtualCard object
	private String firstTransactionLifecycleId;

	private String cardNumber; //Card real number
	private String cvc;
	private ValitorPayCardVerificationData cardVerificationData;

	public ValitorPayVirtualCardData() {
		super();
	}

	public ValitorPayVirtualCardData(String subsequentTransactionType) {
		this();

		this.subsequentTransactionType = subsequentTransactionType;
	}

	public ValitorPayVirtualCardData(
			String virtualCardNumber,
			Integer expirationMonth,
			Integer expirationYear,
			String sponsoredMerchantId,
			String agreementNumber,
			String terminalId
	) {
		super();
		this.virtualCardNumber = virtualCardNumber;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.sponsoredMerchantId = sponsoredMerchantId;
		this.agreementNumber = agreementNumber;
		this.terminalId = terminalId;
		this.subsequentTransactionType = null;
	}

	public ValitorPayVirtualCardData(
			String virtualCard,
			String firstTransactionLifecycleId,
			String sponsoredMerchantId,
			String agreementNumber,
			String terminalId
	) {
		super();
		this.sponsoredMerchantId = sponsoredMerchantId;
		this.agreementNumber = agreementNumber;
		this.terminalId = terminalId;
		this.virtualCard = virtualCard;
		this.firstTransactionLifecycleId = firstTransactionLifecycleId;
	}

	public ValitorPayVirtualCardData(
			String cardNumber,
			Integer expirationMonth,
			Integer expirationYear,
			String cvc,
			ValitorPayCardVerificationData cardVerificationData
	) {
		super();
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.cardNumber = cardNumber;
		this.cvc = cvc;
		this.cardVerificationData = cardVerificationData;
	}

	public String getSubsequentTransactionType() {
		return subsequentTransactionType;
	}

	public void setSubsequentTransactionType(String subsequentTransactionType) {
		this.subsequentTransactionType = subsequentTransactionType;
	}

	public String getVirtualCardNumber() {
		return virtualCardNumber;
	}

	public void setVirtualCardNumber(String virtualCardNumber) {
		this.virtualCardNumber = virtualCardNumber;
	}

	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getSponsoredMerchantId() {
		return sponsoredMerchantId;
	}

	public void setSponsoredMerchantId(String sponsoredMerchantId) {
		this.sponsoredMerchantId = sponsoredMerchantId;
	}

	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getVirtualCard() {
		return virtualCard;
	}

	public void setVirtualCard(String virtualCard) {
		this.virtualCard = virtualCard;
	}

	public String getFirstTransactionLifecycleId() {
		return firstTransactionLifecycleId;
	}

	public void setFirstTransactionLifecycleId(String firstTransactionLifecycleId) {
		this.firstTransactionLifecycleId = firstTransactionLifecycleId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public ValitorPayCardVerificationData getCardVerificationData() {
		return cardVerificationData;
	}

	public void setCardVerificationData(ValitorPayCardVerificationData cardVerificationData) {
		this.cardVerificationData = cardVerificationData;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	@Override
	public String toString() {
		return 	"VirtualCardNumber: " + getVirtualCardNumber() + ", expirationMonth: " + getExpirationMonth()
			+ ", expirationYear: " + getExpirationYear() + ", sponsoredMerchantId: " + getSponsoredMerchantId()
			+ ", agreementNumber: " + getAgreementNumber() + ", terminalId: " + getTerminalId()
			+ ", Sub-sequent transaction type: " + getSubsequentTransactionType()
			+ ", virtualCard: " + getVirtualCard()
			+ ", firstTransactionLifecycleId: " + getFirstTransactionLifecycleId()
			+ ", cardNumber: " + getCardNumber() + CoreConstants.SPACE
			+ ", cvc: " + getCvc()
			+ (cardVerificationData != null ? cardVerificationData.toString() : CoreConstants.EMPTY);
	}

}