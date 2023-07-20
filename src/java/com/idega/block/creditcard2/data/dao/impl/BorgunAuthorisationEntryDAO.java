package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.BorgunAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BorgunAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class BorgunAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<BorgunAuthorisationEntry> {

	public static final String BEAN_NAME = "BorgunAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(BorgunAuthorisationEntry entry) {
		return getSingleResult(BorgunAuthorisationEntry.GET_BY_PARENT_ID, BorgunAuthorisationEntry.class, new Param(BorgunAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(BorgunAuthorisationEntry.GET_BY_AUTH_CODE, BorgunAuthorisationEntry.class, new Param(BorgunAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(
				BorgunAuthorisationEntry.GET_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(BorgunAuthorisationEntry.dateFromProp, from),
				new Param(BorgunAuthorisationEntry.dateToProp, to)
		);
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(
				BorgunAuthorisationEntry.GET_REFUNDS_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(BorgunAuthorisationEntry.dateFromProp, from),
				new Param(BorgunAuthorisationEntry.dateToProp, to)
		);
	}

	@Override
	@Transactional(readOnly = false)
	public BorgunAuthorisationEntry store(BorgunAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		if (entry.getPrimaryKey() == null) {
			throw new RuntimeException(BorgunAuthorisationEntry.class.getName() + " could not be saved");
		}

		return entry;
	}

	public BorgunAuthorisationEntry findById(Long parentDataPK) {
		return getSingleResultByInlineQuery(
				"from BorgunAuthorisationEntry bae where bae.id = :id",
				BorgunAuthorisationEntry.class,
				new Param("id", parentDataPK)
		);
	}

	public String getLastAuthorizationForMerchant(String merchantRrnSuffix, Integer merchantId) {
		return getSingleResultByInlineQuery(
				"select max(bae.rrn) from BorgunAuthorisationEntry bae where bae.rrn Like :rrn and bae.merchant.id = :id",
				String.class,
				new Param("rrn", merchantRrnSuffix + "%"),
				new Param("id", merchantId)
		);
	}

	@Override
	public CreditCardAuthorizationEntry getByMetadata(String key, String value) {
		return null;
	}

}