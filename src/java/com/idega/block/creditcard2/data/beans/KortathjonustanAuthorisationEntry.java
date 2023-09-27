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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;
import com.idega.data.bean.Metadata;
import com.idega.util.DBUtil;

@Entity
@Table(name = "CC_KORTTHJ_AUTH_ENTRIES")
@NamedQueries({
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_ID, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.idProp + " = :" + KortathjonustanAuthorisationEntry.idProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.parentProp + " = :" + KortathjonustanAuthorisationEntry.parentProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.authCodeProp + " = :"
				+ KortathjonustanAuthorisationEntry.authCodeProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_BY_DATES, query = "from KortathjonustanAuthorisationEntry bae where bae."
				+ KortathjonustanAuthorisationEntry.dateProp + " >= :" + KortathjonustanAuthorisationEntry.dateFromProp
				+ " and " + KortathjonustanAuthorisationEntry.dateProp + " <=:"
				+ KortathjonustanAuthorisationEntry.dateToProp),
		@NamedQuery(name = KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from KortathjonustanAuthorisationEntry bae where bae.transactionType = "
				+ KortathjonustanAuthorisationEntry.AUTHORIZATION_TYPE_REFUND + " and bae."
				+ KortathjonustanAuthorisationEntry.dateProp + " >= :" + KortathjonustanAuthorisationEntry.dateFromProp
				+ " and " + KortathjonustanAuthorisationEntry.dateProp + " <=:"
				+ KortathjonustanAuthorisationEntry.dateToProp) })
public class KortathjonustanAuthorisationEntry implements CreditCardAuthorizationEntry {

	public static final String TABLE_NAME = "CC_KORTTHJ_AUTH_ENTRIES";
	public static final String GET_BY_ID = "KortathjonustanAuthorisationEntry.GET_BY_ID";
	public static final String GET_BY_PARENT_ID = "KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID";
	public static final String GET_BY_AUTH_CODE = "KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE";
	public static final String GET_BY_DATES = "KortathjonustanAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES";

	public static final String idProp = "id";
	public static final String parentProp = "parent";
	public static final String authCodeProp = "authCode";
	public static final String dateProp = "entryDate";
	public static final String dateFromProp = "entryDateFrom";
	public static final String dateToProp = "entryDateTo";

	public static final String AUTHORIZATION_TYPE_SALE = "0";
	public static final String AUTHORIZATION_TYPE_REFUND = "1";
	public static final String AUTHORIZATION_TYPE_DELAYED_TRANSACTION = "2";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CC_KORTTHJ_AUTH_ENTRIES_ID")
	Integer id;

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

	@Column(name = "SERVER_RESPONSE", length = 1000)
	private String serverResponse;

	@OneToOne
	@JoinColumn(name = "PARENT_ID")
	private KortathjonustanAuthorisationEntry parent;

	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;

	@Column(name = "UNIQUE_ID")
	private String uniqueId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Metadata.class)
	@JoinTable(name = TABLE_NAME + "_" + Metadata.ENTITY_NAME, joinColumns = { @JoinColumn(name = TABLE_NAME + "_ID") }, inverseJoinColumns = { @JoinColumn(name = Metadata.COLUMN_METADATA_ID) })
	private Set<Metadata> metadata;

	@Column(name = "reference")
	private String reference;

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

	public String getEntityName() {
		return TABLE_NAME;
	}

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

	@Override
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

	@Override
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

	public String getServerResponse() {
		return serverResponse;
	}

	public void setServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}

	@Override
	public CreditCardAuthorizationEntry getParent() {
		return parent;
	}

	public void setParent(KortathjonustanAuthorisationEntry parent) {
		this.parent = parent;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		// TODO Auto-generated method stub
		return null;
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
			setEntryDate(new Date(date.getTime()));
		}
	}

	@Override
	public void setMerchant(CreditCardMerchant merchant) {
	}

	@Override
	public CreditCardMerchant getMerchant() {
		return null;
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