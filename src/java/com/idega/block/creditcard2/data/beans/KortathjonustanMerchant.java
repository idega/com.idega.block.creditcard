package com.idega.block.creditcard2.data.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;

@Entity
@Table(name = KortathjonustanMerchant.ENTITY_NAME)
@NamedQueries({
		@NamedQuery(name = KortathjonustanMerchant.findByName, query = "from KortathjonustanMerchant mer where mer."
				+ KortathjonustanMerchant.nameProp + " =:" + KortathjonustanMerchant.nameProp),
		@NamedQuery(name = KortathjonustanMerchant.findById, query = "from KortathjonustanMerchant mer where mer."
				+ KortathjonustanMerchant.idProp + " =:" + KortathjonustanMerchant.idProp) })
public class KortathjonustanMerchant implements CreditCardMerchant {

	public static final String ENTITY_NAME = "CC_KTH_MERCHANT";
	public static final String findByName = "KortathjonustanMerchant.findByName";
	public static final String findById = "KortathjonustanMerchant.findById";
	public static final String nameProp = "merchantName";
	public static final String idProp = "id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getType() {
		return MERCHANT_TYPE_KORTHATHJONUSTAN;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CC_KTH_MERCHANT_ID")
	Integer id;

	@Column(name = "MERCHANT_NAME")
	private String merchantName;

	@Column(name = "SITE")
	private String site;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_PASSWORD")
	private String userPassword;

	@Column(name = "ACCEPTOR_TERM_ID")
	private String accTermId;

	@Column(name = "ACCEPTOR_IDENTIFICATION")
	private String accId;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "IS_DELETED")
	private Boolean deleted;

	@Column(name = COLUMN_SHARED_SECRET)
	private String secret;

	@Column(name = COLUMN_SUCCESS_REDIRECT_URL)
	private String successRedirectURL;

	@Column(name = COLUMN_AUTHORIZATION_TERMINAL)
	private String authorizationTerminal;

	@Override
	public String getAuthorizationTerminal() {
		return authorizationTerminal;
	}

	@Override
	public void setAuthorizationTerminal(String authorizationTerminal) {
		this.authorizationTerminal = authorizationTerminal;
	}

	@Override
	public String getSuccessRedirectURL() {
		return successRedirectURL;
	}

	@Override
	public void setSuccessRedirectURL(String successRedirectURL) {
		this.successRedirectURL = successRedirectURL;
	}

	/**
	 * Not implemented
	 */
	@Override
	public String getExtraInfo() {
		return null;
	}

	/**
	 * Not Implemented
	 */
	@Override
	public void setExtraInfo(String extra) {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getAccTermId() {
		return accTermId;
	}

	public void setAccTermId(String accTermId) {
		this.accTermId = accTermId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public Date getStartDateSql() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getModificationDateSql() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Date getEndDateSql() {
		return endDate;
	}

	@Override
	public Timestamp getStartDate() {
		return startDate == null ? null : new Timestamp(startDate.getTime());
	}

	@Override
	public Timestamp getModificationDate() {
		return modificationDate == null ? null : new Timestamp(modificationDate.getTime());
	}

	@Override
	public Timestamp getEndDate() {
		return endDate == null ? null : new Timestamp(endDate.getTime());
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getDeleted() {
		return deleted == null ? false : deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean getIsDeleted() {
		return getDeleted();
	}

	@Override
	public Integer getPrimaryKey() {
		return getId();
	}

	@Override
	public String getName() {
		return getMerchantName();
	}

	@Override
	public String getUser() {
		return getUserId();
	}

	@Override
	public String getPassword() {
		return getUserPassword();
	}

	@Override
	public String getMerchantID() {
		return getAccId();
	}

	@Override
	public String getLocation() {
		return getSite();
	}

	@Override
	public String getTerminalID() {
		return getAccTermId();
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
	public int compareTo(IDOEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setName(String name) {
		setMerchantName(name);
	}

	@Override
	public void setLocation(String location) {
		setSite(location);
	}

	@Override
	public void setUser(String user) {
		setUserId(user);
	}

	@Override
	public void setPassword(String password) {
		setUserPassword(password);
	}

	@Override
	public void setTerminalID(String terminalID) {
		setAccTermId(terminalID);
	}

	@Override
	public void setMerchantID(String id) {
		setAccId(id);
	}

	@Override
	public void store() {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove() throws RemoveException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSharedSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String getSharedSecret() {
		return secret;
	}

}