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

import com.idega.block.creditcard2.business.CreditCardMerchant;

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

	@Override
	public Date getStartDate() {
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

	@Override
	public Date getEndDate() {
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
	public Boolean getIsDeleted() {
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
		return null;
	}

	@Override
	public String getExtraInfo() {
		return getKeyRcvPassw();
	}
}