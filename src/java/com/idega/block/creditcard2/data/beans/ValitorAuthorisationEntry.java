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
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;
import com.idega.data.bean.Metadata;
import com.idega.util.DBUtil;
import com.idega.util.StringUtil;

@Entity
@Table(name = ValitorAuthorisationEntry.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = ValitorAuthorisationEntry.GET_BY_ID, query = "from ValitorAuthorisationEntry bae where bae."
				+ ValitorAuthorisationEntry.idProp + " = :" + ValitorAuthorisationEntry.idProp),
		@NamedQuery(name = ValitorAuthorisationEntry.GET_BY_PARENT_ID, query = "from ValitorAuthorisationEntry bae where bae."
				+ ValitorAuthorisationEntry.parentProp + " = :" + ValitorAuthorisationEntry.parentProp),
		@NamedQuery(name = ValitorAuthorisationEntry.GET_BY_AUTH_CODE, query = "from ValitorAuthorisationEntry bae where bae."
				+ ValitorAuthorisationEntry.authCodeProp + " = :" + ValitorAuthorisationEntry.authCodeProp),
		@NamedQuery(name = ValitorAuthorisationEntry.GET_BY_UNIQUE_ID, query = "from ValitorAuthorisationEntry bae where bae."
				+ ValitorAuthorisationEntry.uniqueIdProp + " = :" + ValitorAuthorisationEntry.uniqueIdProp),
		@NamedQuery(name = ValitorAuthorisationEntry.GET_BY_DATES, query = "from ValitorAuthorisationEntry bae where bae."
				+ ValitorAuthorisationEntry.dateProp + " >= :" + ValitorAuthorisationEntry.dateFromProp + " and "
				+ ValitorAuthorisationEntry.dateProp + " <=:" + ValitorAuthorisationEntry.dateToProp),
		@NamedQuery(name = ValitorAuthorisationEntry.GET_REFUNDS_BY_DATES, query = "from ValitorAuthorisationEntry bae where bae.transactionType = "
				+ ValitorAuthorisationEntry.AUTHORIZATION_TYPE_REFUND + " and bae." + ValitorAuthorisationEntry.dateProp
				+ " >= :" + ValitorAuthorisationEntry.dateFromProp + " and " + ValitorAuthorisationEntry.dateProp + " <=:"
				+ ValitorAuthorisationEntry.dateToProp),
		@NamedQuery(
				name = ValitorAuthorisationEntry.QUERY_FIND_BY_METADATA,
				query = "select vae from ValitorAuthorisationEntry vae "
						+ "join vae.metadata meta "
						+ " where meta.key = :" + ValitorAuthorisationEntry.METADATA_KEY_PROP
						+ " and meta.value = :" + ValitorAuthorisationEntry.METADATA_VALUE_PROP

		)
})
public class ValitorAuthorisationEntry implements CreditCardAuthorizationEntry {

	public static final String TABLE_NAME = "VALITOR_AUTHORISATION_ENTRIES";

	public static final String GET_BY_ID = "ValitorAuthorisationEntry.getByID";
	public static final String GET_BY_PARENT_ID = "ValitorAuthorisationEntry.getByParentID";
	public static final String GET_BY_AUTH_CODE = "ValitorAuthorisationEntry.getByAuthCode";
	public static final String GET_BY_UNIQUE_ID = "ValitorAuthorisationEntry.getByUniqueId";
	public static final String idProp = "id";
	public static final String parentProp = "parent";
	public static final String authCodeProp = "authCode";
	public static final String uniqueIdProp = "uniqueId";
	public static final String GET_BY_DATES = "ValitorAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "ValitorAuthorisationEntry.GET_REFUNDS_BY_DATES";
	public static final String dateProp = "date";
	public static final String dateFromProp = "dateFrom";
	public static final String dateToProp = "dateTo";
	public static final String QUERY_FIND_BY_METADATA = "ValitorAuthorisationEntry.findByMetadata";
	public static final String METADATA_KEY_PROP = "metadataKey";
	public static final String METADATA_VALUE_PROP = "metadataValue";


	//for Valitor these are Idega internal
	public static final String AUTHORIZATION_TYPE_SALE = "1";
	public static final String AUTHORIZATION_TYPE_REFUND = "3";
	public static final String AUTHORIZATION_TYPE_PARTIAL_REVERSAL = "4";
	public static final String AUTHORIZATION_TYPE_PRE_AUTHORIZE = "5";

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

	@Column(name = "server_response", length = 1000)
	private String serverResponse;

	@Column(name = "rrn")
	private String rrn;

	@Column(name = "UNIQUE_ID")
	private String uniqueId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Metadata.class)
	@JoinTable(name = TABLE_NAME + "_" + Metadata.ENTITY_NAME, joinColumns = { @JoinColumn(name = "ID") }, inverseJoinColumns = { @JoinColumn(name = Metadata.COLUMN_METADATA_ID) })
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

	@Column(name = "redirection_url")
	private String redirectionUrl;

	@Column(name = "success")
	private Boolean success;

	@Column(name = "transaction_lifecycle_id")
	private String transactionLifecycleId;

	@Column(name = "md_status")
	private String mdStatus;

	@Column(name = "cavv")
	private String cavv;

	@Column(name = "xid")
	private String xid;

	@Column(name = "dsTransID")
	private String dsTransID;

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

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	@OneToOne
	@JoinColumn(name = "parent")
	private ValitorAuthorisationEntry parent;

	public void setParent(ValitorAuthorisationEntry parent) {
		this.parent = parent;
	}

	@OneToOne
	@JoinColumn(name = "merchant", nullable = false)
	private ValitorMerchant merchant;

	public ValitorMerchant getMerchant() {
		return merchant;
	}

	@Override
	public void setMerchant(CreditCardMerchant merchant) {
		if (merchant instanceof ValitorMerchant) {
			this.merchant = (ValitorMerchant) merchant;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public double getAmount() {
		return amount.doubleValue();
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

	public String getCardExpireDate() {
		return cardExpireDate;
	}

	public void setCardExpireDate(String cardExpireDate) {
		this.cardExpireDate = cardExpireDate;
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

	@Override
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
		if (!StringUtil.isEmpty(errorText) && errorText.length() >= 255) {
			errorText = errorText.substring(0, 253);
		}
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
		return this.serverResponse.toString();
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
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@PrePersist
	protected void onCreate() {
		this.date = new Date(System.currentTimeMillis());
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

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getTransactionLifecycleId() {
		return transactionLifecycleId;
	}

	public void setTransactionLifecycleId(String transactionLifecycleId) {
		this.transactionLifecycleId = transactionLifecycleId;
	}

	public String getMdStatus() {
		return mdStatus;
	}

	public void setMdStatus(String mdStatus) {
		this.mdStatus = mdStatus;
	}

	public String getCavv() {
		return cavv;
	}

	public void setCavv(String cavv) {
		this.cavv = cavv;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getDsTransID() {
		return dsTransID;
	}

	public void setDsTransID(String dsTransID) {
		this.dsTransID = dsTransID;
	}

	@Override
	public void setDate(Timestamp date) {
		if (date != null) {
			setDate(new Date(date.getTime()));
		}
	}

	@Override
	public boolean isRefund() {
		return refund == null ? false : refund;
	}

	@Override
	public void setRefund(boolean refund) {
		this.refund = refund;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " ID: " + getId() + ". Amount: " + getAmount() + ", auth. code: " + getAuthCode() + ", timestamp: " + getTimestamp();
	}

}