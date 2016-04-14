package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.DummyAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.KortathjonustanAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(KortathjonustanAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class KortathjonustanAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<KortathjonustanAuthorisationEntry> {
	public static final String BEAN_NAME = "KortathjonustanAuthorisationEntryDAO";
	
	@Override
	public CreditCardAuthorizationEntry getChild(KortathjonustanAuthorisationEntry entry) {
		return getSingleResult(KortathjonustanAuthorisationEntry.GET_BY_PARENT_ID, CreditCardAuthorizationEntry.class, new Param(KortathjonustanAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		return getSingleResult(KortathjonustanAuthorisationEntry.GET_BY_AUTH_CODE, CreditCardAuthorizationEntry.class, new Param(KortathjonustanAuthorisationEntry.authCodeProp, code));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(KortathjonustanAuthorisationEntry.GET_BY_DATES, CreditCardAuthorizationEntry.class, new Param(KortathjonustanAuthorisationEntry.dateFromProp, from), new Param(KortathjonustanAuthorisationEntry.dateToProp, to));
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(KortathjonustanAuthorisationEntry.GET_REFUNDS_BY_DATES, CreditCardAuthorizationEntry.class, new Param(KortathjonustanAuthorisationEntry.dateFromProp, from), new Param(KortathjonustanAuthorisationEntry.dateToProp, to));
	}

	@Override
	public void store(KortathjonustanAuthorisationEntry entry) {
		if (entry.getId()!=null) {
			persist(entry);
		}
		else {
			merge(entry);
		}
	}

	public KortathjonustanAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery("from KortathjonustanAuthorisationEntry kae where kae.id =:id", KortathjonustanAuthorisationEntry.class, new Param("id", parentDataPK));
	}

}
