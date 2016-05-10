package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.TPosAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(TPosAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class TPosAuthorisationEntryDAO extends GenericDaoImpl
		implements AuthorisationEntriesDAO<TPosAuthorisationEntry> {
	public static final String BEAN_NAME = "TPosAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(TPosAuthorisationEntry entry) {
		return getSingleResult(TPosAuthorisationEntry.GET_BY_PARENT_ID, CreditCardAuthorizationEntry.class,
				new Param(TPosAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(TPosAuthorisationEntry.GET_BY_AUTH_CODE, CreditCardAuthorizationEntry.class,
				new Param(TPosAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(TPosAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(TPosAuthorisationEntry.dateFromProp, from), new Param(TPosAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(TPosAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(TPosAuthorisationEntry.dateFromProp, from), new Param(TPosAuthorisationEntry.dateToProp, to));
	}

	@Override
	public void store(TPosAuthorisationEntry entry) {
		if (entry.getId() != null) {
			persist(entry);
		} else {
			merge(entry);
		}
	}

}
