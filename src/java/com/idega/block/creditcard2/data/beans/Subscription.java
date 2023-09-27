package com.idega.block.creditcard2.data.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.idega.core.idgenerator.business.UUIDGenerator;
import com.idega.data.MetaDataCapable;
import com.idega.data.UniqueIDCapable;
import com.idega.data.bean.Metadata;
import com.idega.util.DBUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;

@Entity
@Table(name = Subscription.TABLE_NAME, indexes = {
		@Index(name = Subscription.TABLE_NAME + "_user_index", columnList = Subscription.COLUMN_USER_ID),
		@Index(name = Subscription.TABLE_NAME + "_uuid_index", columnList = Subscription.COLUMN_UNIQUE_ID)
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = Subscription.GET_ALL_BY_USER_ID,
			query = "from Subscription s where s.userId = :" + Subscription.PARAM_USER_ID + " order by s.enabled, s.created"
	),
	@NamedQuery(
			name = Subscription.GET_ALL_BY_USER_ID_AND_STATUS,
			query = "from Subscription s where s.userId = :" + Subscription.PARAM_USER_ID
				+ " and s.enabled = :" + Subscription.PARAM_STATUS
				+ " order by s.created"
	),
	@NamedQuery(
			name = Subscription.GET_ALL_ACTIVE,
			query = "from Subscription s where s.enabled = true order by s.created"
	),
	@NamedQuery(
			name = Subscription.DELETE_ALL_BY_USER_ID,
			query = "delete from Subscription s where s.userId = :" + Subscription.PARAM_USER_ID
	)
})
public class Subscription implements Serializable, UniqueIDCapable, MetaDataCapable {

	private static final long serialVersionUID = -8493448476538128516L;

	public static final String TABLE_NAME = "cc_subscription";

	static final String COLUMN_ID = "id",
						COLUMN_UNIQUE_ID = "unique_id",
						COLUMN_USER_ID = "user_id",
						COLUMN_LAST_UNSUCCESSFUL_PAYMENT_DATE = "unsuccess_payment_date",
						COLUMN_FAILED_PAYMENTS = "failed_payments",
						COLUMN_FAILED_PAYMENTS_PER_MONTH = "failed_per_month";

	public static final String GET_ALL_BY_USER_ID = "Subscription.getAllByUserId";
	public static final String GET_ALL_BY_USER_ID_AND_STATUS = "Subscription.getAllByUserIdAndStatus";
	public static final String GET_ALL_ACTIVE = "Subscription.getAllActive";
	public static final String DELETE_ALL_BY_USER_ID = "Subscription.deleteAllByUserId";

	public static final String PARAM_USER_ID = "userId";
	public static final String PARAM_STATUS = "status";

	public static final String SQL_RELATION_METADATA = "ic_metadata_cc_subscription";


	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "merchant_id", nullable = false)
	private Integer merchantId;

	@Column(name = "created")
	private Timestamp created;

	@Column(name = "disabled")
	private Timestamp disabled;

	@Column(name = "enabled")
	private Boolean enabled = Boolean.TRUE;

	@Column(name = COLUMN_USER_ID, nullable = false)
	private Integer userId;

	@Column(name = COLUMN_UNIQUE_ID, unique = true)
	private String uniqueId;

	@Column(name = "last_payment_date")
	private Timestamp lastPaymentDate;

	@Column(name = COLUMN_LAST_UNSUCCESSFUL_PAYMENT_DATE)
	private Timestamp lastUnsuccessfulPaymentDate;

	@Column(name = COLUMN_FAILED_PAYMENTS)
	private Integer failedPayments;

	@Column(name = COLUMN_FAILED_PAYMENTS_PER_MONTH)
	private Integer failedPaymentsPerMonth;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Metadata.class)
	@JoinTable(name = SQL_RELATION_METADATA, joinColumns = { @JoinColumn(name = COLUMN_ID) }, inverseJoinColumns = { @JoinColumn(name = Metadata.COLUMN_METADATA_ID) })
	private Set<Metadata> metadata;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getDisabled() {
		return disabled;
	}

	public void setDisabled(Timestamp disabled) {
		this.disabled = disabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Timestamp getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(Timestamp lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public Timestamp getLastUnsuccessfulPaymentDate() {
		return lastUnsuccessfulPaymentDate;
	}

	public void setLastUnsuccessfulPaymentDate(Timestamp lastUnsuccessfulPaymentDate) {
		this.lastUnsuccessfulPaymentDate = lastUnsuccessfulPaymentDate;
	}

	public Integer getFailedPayments() {
		return failedPayments;
	}

	public void setFailedPayments(Integer failedPayments) {
		this.failedPayments = failedPayments;
	}

	public Integer getFailedPaymentsPerMonth() {
		return failedPaymentsPerMonth;
	}

	public void setFailedPaymentsPerMonth(Integer failedPaymentsPerMonth) {
		this.failedPaymentsPerMonth = failedPaymentsPerMonth;
	}



	private Metadata getMetadata(String key) {
		Set<Metadata> list = getMetadata();
		if (!ListUtil.isEmpty(list)) {
			for (Metadata metaData : list) {
				if (metaData.getKey().equals(key)) {
					return metaData;
				}
			}
		}

		return null;
	}

	@Override
	public String getMetaData(String metaDataKey) {
		Set<Metadata> list = getMetadata();
		if (!ListUtil.isEmpty(list)) {
			for (Metadata metaData : list) {
				if (metaData.getKey().equals(metaDataKey)) {
					return metaData.getValue();
				}
			}
		}

		return null;
	}

	@Override
	public Map<String, String> getMetaDataAttributes() {
		Map<String, String> map = new HashMap<>();

		Set<Metadata> list = getMetadata();
		if (!ListUtil.isEmpty(list)) {
			for (Metadata metaData : list) {
				map.put(metaData.getKey(), metaData.getValue());
			}
		}

		return map;
	}

	@Override
	public Map<String, String> getMetaDataTypes() {
		Map<String, String> map = new HashMap<>();

		Set<Metadata> list = getMetadata();
		if (!ListUtil.isEmpty(list)) {
			for (Metadata metaData : list) {
				map.put(metaData.getKey(), metaData.getType());
			}
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
		if (!MapUtil.isEmpty(map)) {
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
	}

	@Override
	public void updateMetaData() throws SQLException {
	}

	public Set<Metadata> getMetadata() {
		metadata = DBUtil.getInstance().lazyLoad(metadata);
		if (metadata == null) {
			metadata = new HashSet<>();
		}
		return metadata;
	}

	public void setMetadata(Set<Metadata> metadata) {
		this.metadata = metadata;
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (StringUtil.isEmpty(uniqueId)) {
			uniqueId = UUIDGenerator.getInstance().generateUUID();
		}
		if (created == null) {
			created = new Timestamp(System.currentTimeMillis());
		}
	}

}