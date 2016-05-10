package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.DummyAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(DummyAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class DummyAuthorisationEntryDAO extends GenericDaoImpl
		implements AuthorisationEntriesDAO<DummyAuthorisationEntry> {
	public static final String BEAN_NAME = "DummyAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(DummyAuthorisationEntry entry) {
		return getSingleResult(DummyAuthorisationEntry.GET_BY_PARENT_ID, DummyAuthorisationEntry.class,
				new Param(DummyAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(DummyAuthorisationEntry.GET_BY_AUTH_CODE, DummyAuthorisationEntry.class,
				new Param(DummyAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(DummyAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(DummyAuthorisationEntry.dateFromProp, from),
				new Param(DummyAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(DummyAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(DummyAuthorisationEntry.dateFromProp, from),
				new Param(DummyAuthorisationEntry.dateToProp, to));
	}

	@Override
	public void store(DummyAuthorisationEntry entry) {
		if (entry.getId() != null) {
			persist(entry);
		} else {
			merge(entry);
		}
	}

	public DummyAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from DummyAuthorisationEntry kae where kae.id =:id",
				DummyAuthorisationEntry.class, new Param("id", parentDataPK));
	}

}
