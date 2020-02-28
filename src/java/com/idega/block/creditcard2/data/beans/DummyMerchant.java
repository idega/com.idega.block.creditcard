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
@Table(name = DummyMerchant.ENTITY_NAME)
@NamedQueries({
		@NamedQuery(name = DummyMerchant.findByName, query = "from DummyMerchant mer where mer."
				+ DummyMerchant.nameProp + " =:" + DummyMerchant.nameProp),
		@NamedQuery(name = DummyMerchant.findById, query = "from DummyMerchant mer where mer." + DummyMerchant.idProp
				+ " =:" + DummyMerchant.idProp) })
public class DummyMerchant implements CreditCardMerchant {

	public static final String ENTITY_NAME = "CC_DUMMY_MERCHANT";

	public static final String findByName = "DummyMerchant.findByName";
	public static final String findById = "DummyMerchant.findById";

	public static final String idProp = "id";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CC_DUMMY_MERCHANT_ID")
	private Integer id;

	public static final String nameProp = "name";
	@Column(name = "MERCHANT_NAME")
	private String name;

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

	@Override
	public String getType() {
		return MERCHANT_TYPE_DUMMY;
	}

	@Override
	public String getLocation() {
		return "DUMMY_LOCATION";
	}

	@Override
	public String getUser() {
		return "DUMMY_USER";
	}

	@Override
	public String getPassword() {
		return "DUMMY_PASSWORD";
	}

	@Override
	public String getExtraInfo() {
		return null;
	}

	@Override
	public void setLocation(String location) {

	}

	@Override
	public void setUser(String user) {

	}

	@Override
	public void setPassword(String password) {

	}

	@Override
	public void setExtraInfo(String extra) {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getDeleted() {
		return deleted;
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
	public String getMerchantID() {
		return getAccId();
	}

	@Override
	public String getTerminalID() {
		return getAccId();
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
	public Timestamp getStartDate() {
		return new Timestamp(this.startDate.getTime());
	}

	@Override
	public Timestamp getModificationDate() {
		return new Timestamp(this.modificationDate.getTime());
	}

	@Override
	public Timestamp getEndDate() {
		return new Timestamp(this.endDate.getTime());
	}

	@Override
	public void setTerminalID(String terminalID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMerchantID(String id) {
		// TODO Auto-generated method stub

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