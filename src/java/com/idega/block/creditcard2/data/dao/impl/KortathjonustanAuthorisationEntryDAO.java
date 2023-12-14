package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.KortathjonustanAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.StringUtil;

@Repository(KortathjonustanAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class KortathjonustanAuthorisationEntryDAO extends GenericDaoImpl
		implements AuthorisationEntriesDAO<KortathjonustanAuthorisationEntry> {
	public static final String BEAN_NAME = "KortathjonustanAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(KortathjonustanAuthorisationEntry entry) {
		return getSingleResult(KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID, CreditCardAuthorizationEntry.class,
				new Param(KortathjonustanAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE, CreditCardAuthorizationEntry.class,
				new Param(KortathjonustanAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(KortathjonustanAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(KortathjonustanAuthorisationEntry.dateFromProp, from),
				new Param(KortathjonustanAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class,
				new Param(KortathjonustanAuthorisationEntry.dateFromProp, from),
				new Param(KortathjonustanAuthorisationEntry.dateToProp, to));
	}

	@Override
	@Transactional(readOnly = false)
	public KortathjonustanAuthorisationEntry store(KortathjonustanAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		return entry.getPrimaryKey() == null ? null : entry;
	}

	public KortathjonustanAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from KortathjonustanAuthorisationEntry kae where kae.id =:id",
				KortathjonustanAuthorisationEntry.class, new Param("id", parentDataPK));
	}

	@Override
	public CreditCardAuthorizationEntry findByReference(String reference) {
		if (StringUtil.isEmpty(reference)) {
			return null;
		}

		try {
			return getSingleResult(KortathjonustanAuthorisationEntry.GET_BY_REFRENCE, KortathjonustanAuthorisationEntry.class, new Param(CreditCardAuthorizationEntry.COLUMN_REFERENCE, reference));
 		} catch (Exception e) {
 			getLogger().log(Level.WARNING, "Error getting auth. entry by reference " + reference, e);
 		}

		return null;
	}

}