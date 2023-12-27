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
@Table(name = TPosMerchant.EntityName)
@NamedQueries({
		@NamedQuery(name = TPosMerchant.findByName, query = "from TPosMerchant mer where mer." + TPosMerchant.nameProp
				+ " =:" + TPosMerchant.nameProp),
		@NamedQuery(name = TPosMerchant.findById, query = "from TPosMerchant mer where mer." + TPosMerchant.idProp
				+ " =:" + TPosMerchant.idProp) })
public class TPosMerchant implements CreditCardMerchant {

	public static final String EntityName = "TPOS_MERCHANT";
	public static final String findByName = "TPosMerchant.findByName";
	public static final String findById = "TPosMerchant.findById";
	public static final String nameProp = "merchantName";
	public static final String idProp = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TPOS_MERCHANT_ID")
	private Integer id;

	@Column(name = "MERCHANT_NAME")
	private String merchantName;

	@Column(name = "MERCHANT_ID")
	private String merchantId;

	@Column(name = "LOCATION_ID")
	private String locationId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "PASSW")
	private String passw;

	@Column(name = "POS_ID")
	private String posId;

	@Column(name = "KEY_RCV_PASSW")
	private String keyRcvPassw;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

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

	@Column(name = COLUMN_ERROR_REDIRECT_URL)
	private String errorRedirectURL;

	@Column(name = COLUMN_COUNTRY)
	private String country;

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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassw() {
		return passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getKeyRcvPassw() {
		return keyRcvPassw;
	}

	public void setKeyRcvPassw(String keyRcvPassw) {
		this.keyRcvPassw = keyRcvPassw;
	}

	public Date getStartDateSql() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
	public String getType() {
		return MERCHANT_TYPE_TPOS;
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
		return getPassw();
	}

	@Override
	public String getMerchantID() {
		return getMerchantId();
	}

	@Override
	public String getLocation() {
		return getLocationId();
	}

	@Override
	public String getTerminalID() {
		return getPosId();
	}

	@Override
	public String getExtraInfo() {
		return getKeyRcvPassw();
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
		return new Timestamp(this.modifiedDate.getTime());
	}

	@Override
	public Timestamp getEndDate() {
		return new Timestamp(this.endDate.getTime());
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocation(String location) {
		setLocationId(location);
	}

	@Override
	public void setUser(String user) {
		setUserId(user);
	}

	@Override
	public void setPassword(String password) {
		setPassw(password);
	}

	@Override
	public void setTerminalID(String terminalID) {
		setPosId(terminalID);
	}

	@Override
	public void setMerchantID(String id) {
		setMerchantId(id);
	}

	@Override
	public void setExtraInfo(String extra) {
		setKeyRcvPassw(extra);
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

	@Override
	public String getErrorRedirectURL() {
		return errorRedirectURL;
	}

	@Override
	public void setErrorRedirectURL(String errorRedirectURL) {
		this.errorRedirectURL = errorRedirectURL;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

}