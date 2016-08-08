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
@Table(name = "BORGUN_MERCHANT")
@NamedQueries({
		@NamedQuery(name = BorgunMerchant.GET_BY_ID, query = "from BorgunMerchant bm where bm." + BorgunMerchant.idProp
				+ " = :" + BorgunMerchant.idProp),
		@NamedQuery(name = BorgunMerchant.GET_BY_NAME, query = "from BorgunMerchant bm where bm."
				+ BorgunMerchant.nameProp + " = :" + BorgunMerchant.nameProp) })
public class BorgunMerchant implements CreditCardMerchant {

	public static final String GET_BY_ID = "BorgunMerchant.getByID";
	public static final String GET_BY_NAME = "BorgunMerchant.getByName";
	public static final String idProp = "id";
	public static final String nameProp = "merchantName";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "MERCHANT_ID")
	private String merchantId;

	@Column(name = "MERCHANT_LOGIN")
	private String merchantLogin;

	@Column(name = "MERCHANT_PASSWORD")
	private String merchantPassword;

	@Column(name = "MERCHANT_DOMAIN")
	private String merchantDomain;

	@Column(name = "MERCHANT_URL")
	private String merchantUrl;

	@Column(name = "MERCHANT_PROCESSOR")
	private String merchantProcessor;

	@Column(name = "MERCHANT_CONTRACT_NUMBER")
	private String merchantContractNumber;

	@Column(name = "MERCHANT_TERMINAL")
	private String merchantTerminal;

	@Column(name = "MERCHANT_RRN_SUFFIX")
	private String merchantRrnSuffix;

	@Column(name = "MERCHANT_NAME")
	private String merchantName;

	@Column(name = "location")
	private String location;

	@Column(name = "extra_info")
	private String extraInfo;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "modification_date")
	private Date modificationDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "deleted")
	private Boolean deleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantLogin() {
		return merchantLogin;
	}

	public void setMerchantLogin(String merchantLogin) {
		this.merchantLogin = merchantLogin;
	}

	public String getMerchantPassword() {
		return merchantPassword;
	}

	public void setMerchantPassword(String merchantPassword) {
		this.merchantPassword = merchantPassword;
	}

	public String getMerchantDomain() {
		return merchantDomain;
	}

	public void setMerchantDomain(String merchantDomain) {
		this.merchantDomain = merchantDomain;
	}

	public String getMerchantUrl() {
		return merchantUrl;
	}

	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}

	public String getMerchantProcessor() {
		return merchantProcessor;
	}

	public void setMerchantProcessor(String merchantProcessor) {
		this.merchantProcessor = merchantProcessor;
	}

	public String getMerchantTerminal() {
		return merchantTerminal;
	}

	public void setMerchantTerminal(String merchantTerminal) {
		this.merchantTerminal = merchantTerminal;
	}

	public String getMerchantRrnSuffix() {
		return merchantRrnSuffix;
	}

	public void setMerchantRrnSuffix(String merchantRrnSuffix) {
		this.merchantRrnSuffix = merchantRrnSuffix;
	}

	@Override
	public String getType() {
		return CreditCardMerchant.MERCHANT_TYPE_BORGUN;
	}

	@Override
	public String getName() {
		return this.merchantName;
	}

	@Override
	public String getLocation() {
		return this.location;
	}

	@Override
	public String getUser() {
		return getMerchantLogin();
	}

	@Override
	public String getPassword() {
		return getMerchantPassword();
	}

	@Override
	public String getTerminalID() {
		return getMerchantTerminal();
	}

	@Override
	public String getMerchantID() {
		return getMerchantId();
	}

	@Override
	public String getExtraInfo() {
		return this.extraInfo;
	}

	public Date getStartDateSql() {
		return this.startDate;
	}

	public Date getModificationDateSql() {
		return this.modificationDate;
	}

	public Date getEndDateSql() {
		return this.endDate;
	}

	@Override
	public boolean getIsDeleted() {
		if (this.deleted == null)
			return false;
		return this.deleted.booleanValue();
	}

	public void setIsDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
	}

	@Override
	public void setName(String name) {
		this.merchantName = name;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void setUser(String user) {
		setMerchantLogin(user);
	}

	@Override
	public void setPassword(String password) {
		setMerchantPassword(password);
	}

	@Override
	public void setTerminalID(String terminalID) {
		setMerchantTerminal(terminalID);
	}

	@Override
	public void setMerchantID(String id) {
		setMerchantId(merchantId);
	}

	@Override
	public void setExtraInfo(String extra) {
		this.extraInfo = extra;
	}

	@Override
	public Integer getPrimaryKey() {
		return getId();
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public void setModificationDate(Date date) {
		this.modificationDate = date;
	}

	public void setEndDate(Date date) {
		this.endDate = date;
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
		if (this.startDate == null)
			return null;
		return new Timestamp(this.startDate.getTime());
	}

	@Override
	public Timestamp getModificationDate() {
		if (this.modificationDate == null)
			return null;
		return new Timestamp(this.modificationDate.getTime());
	}

	@Override
	public Timestamp getEndDate() {
		if (this.endDate == null)
			return null;
		return new Timestamp(this.endDate.getTime());
	}

	@Override
	public void store() {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove() throws RemoveException {
		// TODO Auto-generated method stub

	}

	public String getMerchantContractNumber() {
		return merchantContractNumber;
	}

	public void setMerchantContractNumber(String merchantContractNumber) {
		this.merchantContractNumber = merchantContractNumber;
	}

}
