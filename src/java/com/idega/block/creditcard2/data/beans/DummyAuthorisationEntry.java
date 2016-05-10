package com.idega.block.creditcard2.data.beans;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;

@Entity
@Table(name = DummyAuthorisationEntry.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = DummyAuthorisationEntry.GET_BY_ID, query = "from DummyAuthorisationEntry bae where bae."
				+ DummyAuthorisationEntry.idProp + " = :" + DummyAuthorisationEntry.idProp),
		@NamedQuery(name = DummyAuthorisationEntry.GET_BY_PARENT_ID, query = "from DummyAuthorisationEntry bae where bae."
				+ DummyAuthorisationEntry.parentProp + " = :" + DummyAuthorisationEntry.parentProp),
		@NamedQuery(name = DummyAuthorisationEntry.GET_BY_AUTH_CODE, query = "from DummyAuthorisationEntry bae where bae."
				+ DummyAuthorisationEntry.authCodeProp + " = :" + DummyAuthorisationEntry.authCodeProp),
		@NamedQuery(name = DummyAuthorisationEntry.GET_BY_DATES, query = "from DummyAuthorisationEntry bae where bae."
				+ DummyAuthorisationEntry.dateProp + " >= :" + DummyAuthorisationEntry.dateFromProp + " and "
				+ DummyAuthorisationEntry.dateProp + " <=:" + DummyAuthorisationEntry.dateToProp),
		@NamedQuery(name = DummyAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from DummyAuthorisationEntry bae where bae.transactionType = 1 and bae."
				+ DummyAuthorisationEntry.dateProp + " >= :" + DummyAuthorisationEntry.dateFromProp + " and "
				+ DummyAuthorisationEntry.dateProp + " <=:" + DummyAuthorisationEntry.dateToProp) })
public class DummyAuthorisationEntry implements com.idega.block.creditcard.data.CreditCardAuthorizationEntry {

	public static final String AUTHORIZATION_TYPE_SALE = "0";
	public static final String AUTHORIZATION_TYPE_REFUND = "1";
	public static final String AUTHORIZATION_TYPE_DELAYED_TRANSACTION = "2";

	public static final String TABLE_NAME = "CC_DUMMY_AUTH_ENTRIES";
	public static final String GET_BY_ID = "DummyAuthorisationEntry.GET_BY_ID";
	public static final String GET_BY_PARENT_ID = "DummyAuthorisationEntry.GET_BY_PARENT_ID";
	public static final String GET_BY_AUTH_CODE = "DummyAuthorisationEntry.GET_BY_AUTH_CODE";
	public static final String GET_BY_DATES = "DummyAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "DummyAuthorisationEntry.GET_REFUNDS_BY_DATES";

	public static final String idProp = "id";
	public static final String parentProp = "parentId";
	public static final String authCodeProp = "authCode";
	public static final String dateProp = "entryDate";
	public static final String dateFromProp = "entryDateFrom";
	public static final String dateToProp = "entryDateTo";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CC_DUMMY_AUTH_ENTRIES_ID")
	private Integer id;

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
	private String serverResponce;

	@Column(name = "PARENT_ID")
	private String parentId;

	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public double getAmount() {
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

	@Override
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
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

	@Override
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

	@Override
	public String getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}

	@Override
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getServerResponce() {
		return serverResponce;
	}

	public void setServerResponce(String serverResponce) {
		this.serverResponce = serverResponce;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public void store() throws IDOStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IDOEntityDefinition getEntityDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer decode(String pkString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> decode(String[] pkString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDatasource(String datasource) {
		// TODO Auto-generated method stub

	}

	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isIdentical(EJBLocalObject arg0) throws EJBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove() throws RemoveException, EJBException {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(IDOEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthorizationCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExtraField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getParentID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditCardAuthorizationEntry getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
}
