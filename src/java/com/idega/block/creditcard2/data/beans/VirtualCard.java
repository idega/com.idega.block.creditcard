package com.idega.block.creditcard2.data.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.idega.user.data.bean.User;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

@Entity
@Table(name = VirtualCard.TABLE_NAME, indexes = {
		@Index(name = VirtualCard.TABLE_NAME + "_token_index", columnList = VirtualCard.COLUMN_TOKEN),
		@Index(name = VirtualCard.TABLE_NAME + "_transaction_index", columnList = VirtualCard.COLUMN_TRANSACTION),
		@Index(name = VirtualCard.TABLE_NAME + "_owner_index", columnList = VirtualCard.COLUMN_OWNER),
		@Index(name = VirtualCard.TABLE_NAME + "_uuid_index", columnList = VirtualCard.COLUMN_UNIQUE_ID)
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = VirtualCard.QUERY_FIND_BY_TOKEN,
			query = "select vc from VirtualCard vc where vc.token = :" + VirtualCard.COLUMN_TOKEN
	),
	@NamedQuery(
			name = VirtualCard.QUERY_FIND_BY_OWNER,
			query = "select vc from VirtualCard vc where vc.owner.userID = :" + VirtualCard.PARAM_OWNER_ID + " and vc.token is not null order by vc.created desc"
	),
	@NamedQuery(
			name = VirtualCard.QUERY_FIND_ACTIVE_BY_OWNER,
			query = "select vc from VirtualCard vc where vc.owner.userID = :" + VirtualCard.PARAM_OWNER_ID + " and (vc.deleted is null OR vc.deleted = 'N' OR vc.deleted = '0') and vc.token is not null order by vc.created desc"
	),
	@NamedQuery(
			name = VirtualCard.QUERY_FIND_BY_UUID_AND_OWNER,
			query = "select vc from VirtualCard vc where vc.uniqueId = :" + VirtualCard.PARAM_UNIQUE_ID + " and vc.owner.userID = :" + VirtualCard.PARAM_OWNER_ID +
				" and vc.transactionId is not null and vc.token is not null order by vc.created desc"
	)
})
public class VirtualCard implements Serializable {

	private static final long serialVersionUID = 3698316604836438730L;

	public static final String	TABLE_NAME = "cc_virtual_card",

								COLUMN_TOKEN = "token",
								COLUMN_TRANSACTION = "transaction_id",
								COLUMN_OWNER = "owner_id",
								COLUMN_UNIQUE_ID = "unique_id",

								PARAM_OWNER_ID = "ownerId",
								PARAM_UNIQUE_ID = "uniqueId",

								QUERY_FIND_BY_TOKEN = "VirtualCard.findByToken",
								QUERY_FIND_BY_OWNER = "VirtualCard.findByOwner",
								QUERY_FIND_BY_UUID_AND_OWNER = "VirtualCard.findByUniqueIdAndOwner",
								QUERY_FIND_ACTIVE_BY_OWNER = "VirtualCard.findActiveByOwner";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "exp_month")
	private Integer expMonth;

	@Column(name = "exp_year")
	private Integer expYear;

	@Column(name = "last_4")
	private String last4;

	@Column(name = COLUMN_TOKEN, nullable = false, unique = true)
	private String token;

	@Column(name = "created")
	private Timestamp created;

	@Column(name = COLUMN_TRANSACTION)
	private String transactionId;

	@Column(name = "brand")
	private String brand;

	@ManyToOne
	@JoinColumn(name = COLUMN_OWNER)
	private User owner;

	@Column(name = "deleted")
	private Boolean deleted;

	@ManyToOne
	@JoinColumn(name = "deleted_by")
	private User deletedBy;

	@Column(name = "deleted_when")
	private Timestamp deletedWhen;

	@Column(name = COLUMN_UNIQUE_ID, unique = true)
	private String uniqueId;

	@Column(name = "enabled")
	private Boolean enabled = Boolean.TRUE;

	public VirtualCard() {
		super();
	}

	public VirtualCard(String identifier) {
		this();

		setTokenFromIdentifier(identifier);
	}

	private void setTokenFromIdentifier(String identifier) {
		String token = CoreConstants.EMPTY;
		Random random = new Random();
		while (token.length() < 19) {
			Integer randomNumber = random.ints(0, 9).findFirst().getAsInt();
			token = token.concat(randomNumber.toString());
		}
		this.token = token;
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (StringUtil.isEmpty(token)) {
			setTokenFromIdentifier(UUID.randomUUID().toString());
		}

		if (created == null) {
			created = new Timestamp(System.currentTimeMillis());
		}

		if (uniqueId == null) {
			uniqueId = UUID.randomUUID().toString();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(Integer expMonth) {
		this.expMonth = expMonth;
	}

	public Integer getExpYear() {
		return expYear;
	}

	public void setExpYear(Integer expYear) {
		this.expYear = expYear;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public User getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(User deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Timestamp getDeletedWhen() {
		return deletedWhen;
	}

	public void setDeletedWhen(Timestamp deletedWhen) {
		this.deletedWhen = deletedWhen;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Virtual card token '" + getToken() + "'. Last 4: " + getLast4() + ", valid to (MM/YY): " + getExpMonth() + "/" + getExpYear() + ", brand: " + getBrand() +
				". Unique ID: " + getUniqueId();
	}

}