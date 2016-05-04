package com.idega.block.creditcard2.data.beans;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;

@Entity
@Table(name = "CC_KORTTHJ_AUTH_ENTRIES")
@NamedQueries({
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_ID, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.idProp + " = :" + KortathjonustanAuthorisationEntry.idProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.parentProp + " = :" + KortathjonustanAuthorisationEntry.parentProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.authCodeProp + " = :"
				+ KortathjonustanAuthorisationEntry.authCodeProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_DATES, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.dateProp + " >= :" + KortathjonustanAuthorisationEntry.dateFromProp
				+ " and " + KortathjonustanAuthorisationEntry.dateProp + " <=:"
				+ KortathjonustanAuthorisationEntry.dateToProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from KortathjonustanAuthorisationEntry bae where bae.transactionType = "
				+ KortathjonustanAuthorisationEntry.AUTHORIZATION_TYPE_REFUND + " and bae."
				+ KortathjonustanAuthorisationEntry.dateProp + " >= :" + KortathjonustanAuthorisationEntry.dateFromProp
				+ " and " + KortathjonustanAuthorisationEntry.dateProp + " <=:"
				+ KortathjonustanAuthorisationEntry.dateToProp) })
public class KortathjonustanAuthorisationEntry implements CreditCardAuthorizationEntry {

	public static final String TABLE_NAME = "CC_KORTTHJ_AUTH_ENTRIES";
	public static final String GET_BY_ID = "KortathjonustanAuthorisationEntry.GET_BY_ID";
	public static final String GET_BY_PARENT_ID = "KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID";
	public static final String GET_BY_AUTH_CODE = "KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE";
	public static final String GET_BY_DATES = "KortathjonustanAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES";

	public static final String idProp = "id";
	public static final String parentProp = "parent";
	public static final String authCodeProp = "authCode";
	public static final String dateProp = "entryDate";
	public static final String dateFromProp = "entryDateFrom";
	public static final String dateToProp = "entryDateTo";

	public static final String AUTHORIZATION_TYPE_SALE = "0";
	public static final String AUTHORIZATION_TYPE_REFUND = "1";
	public static final String AUTHORIZATION_TYPE_DELAYED_TRANSACTION = "2";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CC_KORTTHJ_AUTH_ENTRIES_ID")
	Integer id;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "AUTH_CODE")
	private String authCode;

	@Column(name = "BRAND_NAME")
	private String brandName;

	@Column(name = "CARD_EXPIRES")
	private String cardExpires;

	@Column(name = "CARD_NUMBER")
	private String cardNumber;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "ERROR_NUMBER")
	private String errorNumber;

	@Column(name = "ERROR_TEXT")
	private String errorText;

	@Column(name = "SERVER_RESPONSE")
	private String serverResponse;

	@Column(name = "PARENT_ID")
	private Integer parent;

	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;

	public String getEntityName() {
		return TABLE_NAME;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCardExpires() {
		return cardExpires;
	}

	public void setCardExpires(String cardExpires) {
		this.cardExpires = cardExpires;
	}

	@Override
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getServerResponse() {
		return serverResponse;
	}

	public void setServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
