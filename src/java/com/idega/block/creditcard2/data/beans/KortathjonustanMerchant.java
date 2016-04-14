package com.idega.block.creditcard2.data.beans;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard2.business.CreditCardMerchant;

@Entity
@Table(name =KortathjonustanMerchant.ENTITY_NAME)
@NamedQueries ({
	@NamedQuery(name = KortathjonustanMerchant.findByName, query="from KortathjonustanMerchant mer where mer." + KortathjonustanMerchant.nameProp + " =:"  + KortathjonustanMerchant.nameProp),
	@NamedQuery(name = KortathjonustanMerchant.findById, query="from KortathjonustanMerchant mer where mer." + KortathjonustanMerchant.idProp + " =:"  + KortathjonustanMerchant.idProp)
})
public class KortathjonustanMerchant implements CreditCardMerchant {

	public static final String ENTITY_NAME = "CC_KTH_MERCHANT";
	public static final String findByName = "KortathjonustanMerchant.findByName";
	public static final String findById = "KortathjonustanMerchant.findById";
	public static final String nameProp = "merchantName";
	public static final String idProp = "id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getType() {
		return MERCHANT_TYPE_KORTHATHJONUSTAN;
	}
	
	@Id
	@Column(name = "CC_KTH_MERCHANT_ID")
	Integer id;
	
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	
	@Column (name ="SITE")
	private String site;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name ="USER_PASSWORD")
	private String userPassword;
	
	@Column(name = "ACCEPTOR_TERM_ID")
	private String accTermId;
	
	@Column(name = "ACCEPTOR_IDENTIFICATION")
	private String accId;
	
	@Column (name = "START_DATE")
	private Date startDate;
	
	@Column(name ="MODIFICATION_DATE")
	private Date modificationDate;
	
	@Column (name = "END_DATE")
	private Date endDate;
	
	@Column (name ="IS_DELETED")
	private Boolean deleted;
	
	/**
	 * Not implemented
	 */
	public String getExtraInfo() {
		return null;
	}

	/**
	 * Not Implemented
	 */
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

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
	public Integer getPrimaryKey(){
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
}
