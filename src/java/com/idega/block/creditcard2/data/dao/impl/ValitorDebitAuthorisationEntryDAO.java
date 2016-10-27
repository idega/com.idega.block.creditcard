package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.ValitorDebitAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(ValitorDebitAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class ValitorDebitAuthorisationEntryDAO extends GenericDaoImpl
		implements AuthorisationEntriesDAO<ValitorDebitAuthorisationEntry> {

	public static final String BEAN_NAME = "ValitorDebitAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(ValitorDebitAuthorisationEntry entry) {
		return getSingleResult(ValitorDebitAuthorisationEntry.GET_BY_PARENT_ID, ValitorDebitAuthorisationEntry.class,
				new Param(ValitorDebitAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(ValitorDebitAuthorisationEntry.GET_BY_AUTH_CODE, ValitorDebitAuthorisationEntry.class,
				new Param(ValitorDebitAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(ValitorDebitAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(ValitorDebitAuthorisationEntry.dateFromProp, from),
				new Param(ValitorDebitAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(ValitorDebitAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(ValitorDebitAuthorisationEntry.dateFromProp, from),
				new Param(ValitorDebitAuthorisationEntry.dateToProp, to));
	}

	@Override
	@Transactional(readOnly = false)
	public ValitorDebitAuthorisationEntry store(ValitorDebitAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		if (entry.getPrimaryKey() == null) {
			throw new RuntimeException(ValitorDebitAuthorisationEntry.class.getName() + " could not be saved");
		}

		return entry;
	}

	public ValitorDebitAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from ValitorDebitAuthorisationEntry bae where bae.id =:id",
				ValitorDebitAuthorisationEntry.class, new Param("id", parentDataPK));
	}

	public String getLastAuthorizationForMerchant(String merchantRrnSuffix, Integer merchantId) {
		return getSingleResultByInlineQuery(
				"select max(bae.rrn) from ValitorDebitAuthorisationEntry bae where bae.rrn Like :rrn and bae.merchant.id = :id",
				String.class, new Param("rrn", merchantRrnSuffix + "%"), new Param("id", merchantId));
	}

}