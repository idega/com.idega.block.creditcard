package com.idega.block.creditcard.data;

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

import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;

@Entity
@Table(name = "BORGUN_AUTHORISATION_ENTRIES")
//@NamedQueries(
//	{
//		@NamedQuery(
//				name = KvartaSavedFormEntity.GET_BY_ID,
//				query = "from KvartaSavedFormEntity sf where sf."+KvartaSavedFormEntity.idProp+" = :"+KvartaSavedFormEntity.idProp
//				),
//		@NamedQuery(
//				name = KvartaSavedFormEntity.GET_BY_UUID,
//				query = "from KvartaSavedFormEntity sf where sf."+KvartaSavedFormEntity.submissionUUIDProp+" = :"+KvartaSavedFormEntity.submissionUUIDProp
//				)
//	}
//)
public class BorgunAuthorisationEntry implements CreditCardAuthorizationEntry{

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
	
	@Column(name = "server_response")
	private String serverResponse;

	@Column(name = "rrn")
	private String rrn;
	
	@Column(name = "parent")
	private BorgunAuthorisationEntry parent;
	
	@Column(name= "merchant")
	private BorgunMerchant merchant;
	
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

	@Override
	public void store() throws IDOStoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() throws RemoveException, EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPrimaryKey() {
		return this.id;
	}

	@Override
	public String getCardExpires() {
		return this.cardExpireDate;
	}

	@Override
	public String getAuthorizationCode() {
		return this.authCode;
	}

	@Override
	public String getExtraField() {
		return this.serverResponse;
	}

	@Override
	public int getParentID() {
		return parent.getId().intValue();
	}

	@Override
	public CreditCardAuthorizationEntry getParent() {
		return this.parent;
	}

	@Override
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		//TODO make get child  
		return null;
	}
	
	@Override
	public int compareTo(IDOEntity o) {
		return 0;
	}

	@Override
	public IDOEntityDefinition getEntityDefinition() {
		return null;
	}

	@Override
	public Integer decode(String pkString) {
		return null;
	}

	@Override
	public Collection<Integer> decode(String[] pkString) {
		return null;
	}

	@Override
	public String getDatasource() {
		return null;
	}

	@Override
	public void setDatasource(String datasource) {
		
	}

	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		return null;
	}

	@Override
	public boolean isIdentical(EJBLocalObject arg0) throws EJBException {
		return false;
	}
	
}
