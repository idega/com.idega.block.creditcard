package com.idega.block.creditcard2.data.beans;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;
import com.idega.data.bean.Metadata;
import com.idega.util.DBUtil;
import com.idega.util.IWTimestamp;

@Entity
@Table(name = TPosAuthorisationEntry.ENTITY_NAME)
@NamedQueries({
		@NamedQuery(name = TPosAuthorisationEntry.GET_BY_ID, query = "from TPosAuthorisationEntry bae where bae."
				+ TPosAuthorisationEntry.idProp + " = :" + TPosAuthorisationEntry.idProp),
		@NamedQuery(name = TPosAuthorisationEntry.GET_BY_PARENT_ID, query = "from TPosAuthorisationEntry bae where bae."
				+ TPosAuthorisationEntry.parentProp + " = :" + TPosAuthorisationEntry.parentProp),
		@NamedQuery(name = TPosAuthorisationEntry.GET_BY_AUTH_CODE, query = "from TPosAuthorisationEntry bae where bae."
				+ TPosAuthorisationEntry.authCodeProp + " = :" + TPosAuthorisationEntry.authCodeProp),
		@NamedQuery(name = TPosAuthorisationEntry.GET_BY_DATES, query = "from TPosAuthorisationEntry bae where bae."
				+ TPosAuthorisationEntry.dateProp + " >= :" + TPosAuthorisationEntry.dateFromProp + " and "
				+ TPosAuthorisationEntry.dateProp + " <=:" + TPosAuthorisationEntry.dateToProp),
		@NamedQuery(name = TPosAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from TPosAuthorisationEntry bae where bae.authCode = 'T5' and bae."
				+ TPosAuthorisationEntry.dateProp + " >= :" + TPosAuthorisationEntry.dateFromProp + " and "
				+ TPosAuthorisationEntry.dateProp + " <=:" + TPosAuthorisationEntry.dateToProp) })
public class TPosAuthorisationEntry implements CreditCardAuthorizationEntry {

	public static final String GET_BY_ID = "TPosAuthorisationEntry.GET_BY_ID";
	public static final String GET_BY_PARENT_ID = "TPosAuthorisationEntry.GET_BY_PARENT_ID";
	public static final String GET_BY_AUTH_CODE = "TPosAuthorisationEntry.GET_BY_AUTH_CODE";
	public static final String GET_BY_DATES = "TPosAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "TPosAuthorisationEntry.GET_REFUNDS_BY_DATES";

	public static final String idProp = "id";
	public static final String parentProp = "parentId";
	public static final String authCodeProp = "authCode";
	public static final String dateProp = "entryDate";
	public static final String dateFromProp = "entryDateFrom";
	public static final String dateToProp = "entryDateTo";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = ENTITY_NAME + "_id")
	private Integer id;

	@Column(name = "auth_amount")
	private String amount;

	@Column(name = "auth_currency")
	private String currency;

	@Column(name = "auth_code")
	private String authCode;

	@Column(name = "auth_id_rsp")
	private String authId;

	@Column(name = "auth_path_res_code")
	private String authPathResCode;

	@Column(name = "batch_nr")
	private String batchNr;

	@Column(name = "card_brand_id")
	private String cardBrandId;

	@Column(name = "card_brand_name")
	private String cardBrandName;

	@Column(name = "card_caracter")
	private String cardCaracter;

	@Column(name = "card_type_id")
	private String cardTypeId;

	@Column(name = "card_type_name")
	private String cardTypeName;

	@Column(name = "entry_date")
	private String entryDate;

	@Column(name = "detail")
	private String detail;

	@Column(name = "error_nr")
	private String errorNr;

	@Column(name = "error_text")
	private String errorText;

	@Column(name = "card_expires")
	private String cardExpires;

	@Column(name = "location_nr")
	private String locationNr;

	@Column(name = "merc_auth")
	private String mercAuth;

	@Column(name = "merc_other")
	private String mercOther;

	@Column(name = "merc_sub")
	private String mercSub;

	@Column(name = "att_count")
	private String attCount;

	@Column(name = "pan")
	private String pan;

	@Column(name = "pos_nr")
	private String posNr;

	@Column(name = "pos_ser_nr")
	private String posSerNr;

	@Column(name = "print_data")
	private String printData;

	@Column(name = "sub_amount")
	private String subAmount;

	@Column(name = "sub_currency")
	private String subCurrency;

	@Column(name = "entry_time")
	private String entryTime;

	@Column(name = "rsp_code")
	private String rspCode;

	@Column(name = "transact_nr")
	private String transactNr;

	@Column(name = "void_rsp")
	private String voidRsp;

	@Column(name = "void_trans")
	private String voidTrans;

	@Column(name = "xml")
	private String xml;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "parent_id")
	private String parentId;

	@Column(name = "UNIQUE_ID")
	private String uniqueId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Metadata.class)
	@JoinTable(name = ENTITY_NAME + "_" + Metadata.ENTITY_NAME, joinColumns = { @JoinColumn(name = ENTITY_NAME + "_id") }, inverseJoinColumns = { @JoinColumn(name = Metadata.COLUMN_METADATA_ID) })
	private Set<Metadata> metadata;

	@Column(name = "reference")
	private String reference;

	public final static String ENTITY_NAME = "tpos_auth_entries";

	@Column(name = COLUMN_TIMESTAMP)
	private Timestamp timestamp;

	@Column(name = COLUMN_PAYMENT_ID)
	private String paymentId;

	@Column(name = COLUMN_CARD_TOKEN)
	private String cardToken;

	@Column(name = COLUMN_TRANSACTION_ID)
	private String transactionId;

	@Column(name = COLUMN_REFUND)
	private Boolean refund;

	@Override
	public String getTransactionId() {
		return transactionId;
	}

	@Override
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String getCardToken() {
		return cardToken;
	}

	@Override
	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}

	@Override
	public String getPaymentId() {
		return paymentId;
	}

	@Override
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	@Override
	public Timestamp getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public double getAmount() {
		return Double.parseDouble(amount);
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthPathResCode() {
		return authPathResCode;
	}

	public void setAuthPathResCode(String authPathResCode) {
		this.authPathResCode = authPathResCode;
	}

	public String getBatchNr() {
		return batchNr;
	}

	public void setBatchNr(String batchNr) {
		this.batchNr = batchNr;
	}

	public String getCardBrandId() {
		return cardBrandId;
	}

	public void setCardBrandId(String cardBrandId) {
		this.cardBrandId = cardBrandId;
	}

	public String getCardBrandName() {
		return cardBrandName;
	}

	public void setCardBrandName(String cardBrandName) {
		this.cardBrandName = cardBrandName;
	}

	public String getCardCaracter() {
		return cardCaracter;
	}

	public void setCardCaracter(String cardCaracter) {
		this.cardCaracter = cardCaracter;
	}

	public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getErrorNr() {
		return errorNr;
	}

	public void setErrorNr(String errorNr) {
		this.errorNr = errorNr;
	}

	@Override
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	@Override
	public String getCardExpires() {
		return cardExpires;
	}

	public void setCardExpires(String cardExpires) {
		this.cardExpires = cardExpires;
	}

	public String getLocationNr() {
		return locationNr;
	}

	public void setLocationNr(String locationNr) {
		this.locationNr = locationNr;
	}

	public String getMercAuth() {
		return mercAuth;
	}

	public void setMercAuth(String mercAuth) {
		this.mercAuth = mercAuth;
	}

	public String getMercOther() {
		return mercOther;
	}

	public void setMercOther(String mercOther) {
		this.mercOther = mercOther;
	}

	public String getMercSub() {
		return mercSub;
	}

	public void setMercSub(String mercSub) {
		this.mercSub = mercSub;
	}

	public String getAttCount() {
		return attCount;
	}

	public void setAttCount(String attCount) {
		this.attCount = attCount;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getPosNr() {
		return posNr;
	}

	public void setPosNr(String posNr) {
		this.posNr = posNr;
	}

	public String getPosSerNr() {
		return posSerNr;
	}

	public void setPosSerNr(String posSerNr) {
		this.posSerNr = posSerNr;
	}

	public String getPrintData() {
		return printData;
	}

	public void setPrintData(String printData) {
		this.printData = printData;
	}

	public String getSubAmount() {
		return subAmount;
	}

	public void setSubAmount(String subAmount) {
		this.subAmount = subAmount;
	}

	public String getSubCurrency() {
		return subCurrency;
	}

	public void setSubCurrency(String subCurrency) {
		this.subCurrency = subCurrency;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getTransactNr() {
		return transactNr;
	}

	public void setTransactNr(String transactNr) {
		this.transactNr = transactNr;
	}

	public String getVoidRsp() {
		return voidRsp;
	}

	public void setVoidRsp(String voidRsp) {
		this.voidRsp = voidRsp;
	}

	public String getVoidTrans() {
		return voidTrans;
	}

	public void setVoidTrans(String voidTrans) {
		this.voidTrans = voidTrans;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	@Override
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public String getBrandName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthorizationCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorNumber() {
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

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	private Metadata getMetadata(String key) {
		Set<Metadata> list = getMetadata();
		for (Metadata metaData : list) {
			if (metaData.getKey().equals(key)) {
				return metaData;
			}
		}

		return null;
	}

	@Override
	public String getMetaData(String metaDataKey) {
		Set<Metadata> list = getMetadata();
		for (Metadata metaData : list) {
			if (metaData.getKey().equals(metaDataKey)) {
				return metaData.getValue();
			}
		}

		return null;
	}

	@Override
	public Map<String, String> getMetaDataAttributes() {
		Map<String, String> map = new HashMap<>();

		Set<Metadata> list = getMetadata();
		for (Metadata metaData : list) {
			map.put(metaData.getKey(), metaData.getValue());
		}

		return map;
	}

	@Override
	public Map<String, String> getMetaDataTypes() {
		Map<String, String> map = new HashMap<>();

		Set<Metadata> list = getMetadata();
		for (Metadata metaData : list) {
			map.put(metaData.getKey(), metaData.getType());
		}

		return map;
	}

	@Override
	public boolean removeMetaData(String metaDataKey) {
		Metadata metadata = getMetadata(metaDataKey);
		if (metadata != null) {
			getMetadata().remove(metadata);
		}

		return false;
	}

	@Override
	public void renameMetaData(String oldKeyName, String newKeyName, String value) {
		Metadata metadata = getMetadata(oldKeyName);
		if (metadata != null) {
			metadata.setKey(newKeyName);
			if (value != null) {
				metadata.setValue(value);
			}
		}
	}

	@Override
	public void renameMetaData(String oldKeyName, String newKeyName) {
		renameMetaData(oldKeyName, newKeyName, null);
	}

	@Override
	public void setMetaData(String metaDataKey, String value, String type) {
		Metadata metadata = getMetadata(metaDataKey);
		if (metadata == null) {
			metadata = new Metadata();
			metadata.setKey(metaDataKey);
		}
		metadata.setValue(value);
		if (type != null) {
			metadata.setType(type);
		}

		getMetadata().add(metadata);

	}

	@Override
	public void setMetaData(String metaDataKey, String value) {
		setMetaData(metaDataKey, value, null);
	}

	@Override
	public void setMetaDataAttributes(Map<String, String> map) {
		for (String key : map.keySet()) {
			String value = map.get(key);

			Metadata metadata = getMetadata(key);
			if (metadata == null) {
				metadata = new Metadata();
				metadata.setKey(key);
			}
			metadata.setValue(value);

			getMetadata().add(metadata);
		}
	}

	@Override
	public void updateMetaData() throws SQLException {
		// Does nothing...
	}

	public Set<Metadata> getMetadata() {
		metadata = DBUtil.getInstance().lazyLoad(metadata);
		return this.metadata;
	}

	public void setMetadata(Set<Metadata> metadata) {
		this.metadata = metadata;
	}

	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String getReference() {
		return reference;
	}

	@Override
	public void setAuthorizationCode(String authCode) {
		setAuthCode(authCode);
	}

	@Override
	public void setDate(Timestamp date) {
		if (date != null) {
			setEntryDate(new IWTimestamp(date).getDateString("yyyyMMdd"));
		}
	}

	@Override
	public void setAmount(Double amount) {
		setAmount(amount == null ? null : String.valueOf(amount));
	}

	@Override
	public void setMerchant(CreditCardMerchant merchant) {
	}

	@Override
	public boolean isRefund() {
		return refund == null ? false : refund;
	}

	@Override
	public void setRefund(boolean refund) {
		this.refund = refund;
	}

}
