package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.ValitorAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(ValitorAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class ValitorAuthorisationEntryDAO extends GenericDaoImpl
		implements AuthorisationEntriesDAO<ValitorAuthorisationEntry> {

	public static final String BEAN_NAME = "ValitorAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(ValitorAuthorisationEntry entry) {
		return getSingleResult(ValitorAuthorisationEntry.GET_BY_PARENT_ID, ValitorAuthorisationEntry.class,
				new Param(ValitorAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(ValitorAuthorisationEntry.GET_BY_AUTH_CODE, ValitorAuthorisationEntry.class,
				new Param(ValitorAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(ValitorAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(ValitorAuthorisationEntry.dateFromProp, from),
				new Param(ValitorAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(ValitorAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(ValitorAuthorisationEntry.dateFromProp, from),
				new Param(ValitorAuthorisationEntry.dateToProp, to));
	}

	@Override
	@Transactional(readOnly = false)
	public ValitorAuthorisationEntry store(ValitorAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		if (entry.getPrimaryKey() == null) {
			throw new RuntimeException(ValitorAuthorisationEntry.class.getName() + " could not be saved");
		}

		return entry;
	}

	public ValitorAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from ValitorAuthorisationEntry bae where bae.id =:id",
				ValitorAuthorisationEntry.class, new Param("id", parentDataPK));
	}

	public String getLastAuthorizationForMerchant(String merchantRrnSuffix, Integer merchantId) {
		return getSingleResultByInlineQuery(
				"select max(bae.rrn) from ValitorAuthorisationEntry bae where bae.rrn Like :rrn and bae.merchant.id = :id",
				String.class, new Param("rrn", merchantRrnSuffix + "%"), new Param("id", merchantId));
	}

}