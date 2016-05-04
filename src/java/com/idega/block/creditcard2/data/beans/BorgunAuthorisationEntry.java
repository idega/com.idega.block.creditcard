package com.idega.block.creditcard2.data.beans;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;

@Entity
@Table(name = "BORGUN_AUTHORISATION_ENTRIES")
@NamedQueries({
		@NamedQuery(name = BorgunAuthorisationEntry.GET_BY_ID, query = "from BorgunAuthorisationEntry bae where bae."
				+ BorgunAuthorisationEntry.idProp + " = :" + BorgunAuthorisationEntry.idProp),
		@NamedQuery(name = BorgunAuthorisationEntry.GET_BY_PARENT_ID, query = "from BorgunAuthorisationEntry bae where bae."
				+ BorgunAuthorisationEntry.parentProp + " = :" + BorgunAuthorisationEntry.parentProp),
		@NamedQuery(name = BorgunAuthorisationEntry.GET_BY_AUTH_CODE, query = "from BorgunAuthorisationEntry bae where bae."
				+ BorgunAuthorisationEntry.authCodeProp + " = :" + BorgunAuthorisationEntry.authCodeProp),
		@NamedQuery(name = BorgunAuthorisationEntry.GET_BY_DATES, query = "from BorgunAuthorisationEntry bae where bae."
				+ BorgunAuthorisationEntry.dateProp + " >= :" + BorgunAuthorisationEntry.dateFromProp + " and "
				+ BorgunAuthorisationEntry.dateProp + " <=:" + BorgunAuthorisationEntry.dateToProp),
		@NamedQuery(name = BorgunAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from BorgunAuthorisationEntry bae where bae.transactionType = "
				+ BorgunAuthorisationEntry.AUTHORIZATION_TYPE_REFUND + " and bae." + BorgunAuthorisationEntry.dateProp
				+ " >= :" + BorgunAuthorisationEntry.dateFromProp + " and " + BorgunAuthorisationEntry.dateProp + " <=:"
				+ BorgunAuthorisationEntry.dateToProp) })
public class BorgunAuthorisationEntry implements CreditCardAuthorizationEntry {

	public static final String GET_BY_ID = "BorgunAuthorisationEntry.getByID";
	public static final String GET_BY_PARENT_ID = "BorgunAuthorisationEntry.getByParentID";
	public static final String GET_BY_AUTH_CODE = "BorgunAuthorisationEntry.getByAuthCode";
	public static final String idProp = "id";
	public static final String parentProp = "parent";
	public static final String authCodeProp = "authCode";
	public static final String GET_BY_DATES = "BorgunAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "BorgunAuthorisationEntry.GET_REFUNDS_BY_DATES";
	public static final String dateProp = "date";
	public static final String dateFromProp = "dateFrom";
	public static final String dateToProp = "dateTo";

	public static final String AUTHORIZATION_TYPE_SALE = "1";
	public static final String AUTHORIZATION_TYPE_REFUND = "3";
	public static final String AUTHORIZATION_TYPE_PARTIAL_REVERSAL = "4";
	public static final String AUTHORIZATION_TYPE_PRE_AUTHORIZE = "5";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "auth_code")
	private String authCode;

	@Column(name = "brand_name")
	private String brandName;

	@Column(name = "card_expire_date")
	private String cardExpireDate;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "currency")
	private String currency;

	@Column(name = "date")
	private Date date;

	@Column(name = "error_number")
	private String errorNumber;

	@Column(name = "error_text")
	private String errorText;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "server_response", length = 65000)
	private String serverResponse;

	@Column(name = "rrn")
	private String rrn;

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	@OneToOne
	@JoinColumn(name = "parent")
	private BorgunAuthorisationEntry parent;

	public void setParent(BorgunAuthorisationEntry parent) {
		this.parent = parent;
	}

	@OneToOne
	@JoinColumn(name = "merchant", nullable = false)
	private BorgunMerchant merchant;

	public BorgunMerchant getMerchant() {
		return merchant;
	}

	public void setMerchant(BorgunMerchant merchant) {
		this.merchant = merchant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount.doubleValue();
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

	public String getCardExpireDate() {
		return cardExpireDate;
	}

	public void setCardExpireDate(String cardExpireDate) {
		this.cardExpireDate = cardExpireDate;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getServerResponse() {
		return serverResponse;
	}

	public void setServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}

	public Object getPrimaryKey() {
		return this.id;
	}

	public String getCardExpires() {
		return this.cardExpireDate;
	}

	public String getAuthorizationCode() {
		return this.authCode;
	}

	public String getExtraField() {
		return this.serverResponse;
	}

	public int getParentID() {
		return parent.getId().intValue();
	}

	public CreditCardAuthorizationEntry getParent() {
		return this.parent;
	}

}
