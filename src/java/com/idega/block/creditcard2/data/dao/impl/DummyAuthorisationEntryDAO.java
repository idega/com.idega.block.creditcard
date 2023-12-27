package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.DummyAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.StringUtil;

@Repository(DummyAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class DummyAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<DummyAuthorisationEntry> {

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
	@Transactional(readOnly = false)
	public DummyAuthorisationEntry store(DummyAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		return entry.getPrimaryKey() == null ? null : entry;
	}

	public DummyAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from DummyAuthorisationEntry kae where kae.id =:id",
				DummyAuthorisationEntry.class, new Param("id", parentDataPK));
	}

	@Override
	public CreditCardAuthorizationEntry getByMetadata(String key, String value) {
		return null;
	}

	public CreditCardAuthorizationEntry findByReference(String reference) {
		if (StringUtil.isEmpty(reference)) {
			return null;
		}

		try {
			return getSingleResult(DummyAuthorisationEntry.GET_BY_REFRENCE, DummyAuthorisationEntry.class, new Param(CreditCardAuthorizationEntry.COLUMN_REFERENCE, reference));
 		} catch (Exception e) {
 			getLogger().log(Level.WARNING, "Error getting auth. entry by reference " + reference, e);
 		}

		return null;
	}

}
