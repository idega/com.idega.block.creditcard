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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

@Entity
@Table(name = VirtualCard.TABLE_NAME, indexes = {
		@Index(name = VirtualCard.TABLE_NAME + "_token_index", columnList = VirtualCard.COLUMN_TOKEN)
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = VirtualCard.QUERY_FIND_BY_TOKEN,
			query = "select vc from VirtualCard vc where vc.token = :" + VirtualCard.COLUMN_TOKEN
	)
})
public class VirtualCard implements Serializable {

	private static final long serialVersionUID = 3698316604836438730L;

	public static final String	TABLE_NAME = "cc_virtual_card",

								COLUMN_TOKEN = "token",

								QUERY_FIND_BY_TOKEN = "VirtualCard.findByToken";

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

	@Override
	public String toString() {
		return "Virtual card with token" + getToken();
	}

}