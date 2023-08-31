package com.idega.block.creditcard2.data.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.idega.core.idgenerator.business.UUIDGenerator;
import com.idega.util.StringUtil;

@Entity
@Table(name = SubscriptionPayment.TABLE_NAME, indexes = {
		@Index(name = SubscriptionPayment.TABLE_NAME + "_user_index", columnList = SubscriptionPayment.COLUMN_USER_ID),
		@Index(name = SubscriptionPayment.TABLE_NAME + "_uuid_index", columnList = SubscriptionPayment.COLUMN_UNIQUE_ID),
		@Index(name = SubscriptionPayment.TABLE_NAME + "_sbsc_index", columnList = SubscriptionPayment.COLUMN_SUBSCRIPTION_ID),
		@Index(name = SubscriptionPayment.TABLE_NAME + "_auth_index", columnList = SubscriptionPayment.COLUMN_AUTH_CODE)
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = SubscriptionPayment.QUERY_GET_ALL_BY_USER_ID,
			query = "from SubscriptionPayment sp where sp.userId = :" + SubscriptionPayment.PARAM_USER_ID + " and sp.authCode is not null order by sp.created desc"
	)
})
public class SubscriptionPayment implements Serializable {

	private static final long serialVersionUID = -5270542237603606050L;

	public static final String TABLE_NAME = "cc_subscription_payment";

	static final String COLUMN_ID = "id",
						COLUMN_UNIQUE_ID = "unique_id",
						COLUMN_USER_ID = "user_id",
						COLUMN_SUBSCRIPTION_ID = "subscription_id",
						COLUMN_AUTH_CODE = "auth_code";

	public static final String	QUERY_GET_ALL_BY_USER_ID = "SubscriptionPayment.getAllByUserId",
								PARAM_USER_ID = "userId";

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "created")
	private Timestamp created;

	@Column(name = COLUMN_USER_ID, nullable = false)
	private Integer userId;

	@Column(name = COLUMN_SUBSCRIPTION_ID, nullable = false)
	private Long subscriptionId;

	@Column(name = COLUMN_AUTH_CODE)
	private String authCode;

	@Column(name = COLUMN_UNIQUE_ID, unique = true)
	private String uniqueId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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

	@Override
	public String toString() {
		return "Subscription payment ID " + getId() + ", user: " + getUserId() + ", subscription: " + getSubscriptionId() + ", auth. code: " + getAuthCode();
	}

}