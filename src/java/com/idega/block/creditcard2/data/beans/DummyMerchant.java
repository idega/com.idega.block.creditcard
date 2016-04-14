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
@Table(name = DummyMerchant.ENTITY_NAME)
@NamedQueries ({
		@NamedQuery(name = DummyMerchant.findByName, query="from DummyMerchant mer where mer." + DummyMerchant.nameProp + " =:"  + DummyMerchant.nameProp),
		@NamedQuery(name = DummyMerchant.findById, query="from DummyMerchant mer where mer." + DummyMerchant.idProp + " =:"  + DummyMerchant.idProp)
})
public class DummyMerchant implements CreditCardMerchant{

	public static final String ENTITY_NAME = "CC_DUMMY_MERCHANT";
	
	public static final String findByName = "DummyMerchant.findByName";
	public static final String findById = "DummyMerchant.findById";

	public static final String idProp = "id";
	@Id
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
	
	public String getType() {
		return MERCHANT_TYPE_DUMMY;
	}

	public String getLocation() {
		return "DUMMY_LOCATION";
	}

	
	public String getUser() {
		return "DUMMY_USER";
	}

	public String getPassword() {
		return "DUMMY_PASSWORD";
	}

	
	public String getExtraInfo() {
		return null;
	}

	
	public void setLocation(String location) {

	}

	public void setUser(String user) {

	}

	public void setPassword(String password) {

	}

	public void setExtraInfo(String extra) {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

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
	public String getMerchantID() {
		return getAccId();
	}

	@Override
	public String getTerminalID() {
		return getAccId();
	}
}
