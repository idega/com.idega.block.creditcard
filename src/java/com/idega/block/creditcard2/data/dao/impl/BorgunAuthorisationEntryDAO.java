package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.BorgunAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.DummyAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.KortathjonustanAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BorgunAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
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
		return getResultList(BorgunAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class, new Param(BorgunAuthorisationEntry.dateFromProp, from), new Param(BorgunAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(BorgunAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class, new Param(BorgunAuthorisationEntry.dateFromProp, from), new Param(BorgunAuthorisationEntry.dateToProp, to));
	}
	
	@Override
	public void store(BorgunAuthorisationEntry entry) {
		if (entry.getId()!=null) {
			persist(entry);
		}
		else {
			merge(entry);
		}
	}

	public BorgunAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from BorgunAuthorisationEntry kae where kae.id =:id", BorgunAuthorisationEntry.class, new Param("id", parentDataPK));
	}

}
