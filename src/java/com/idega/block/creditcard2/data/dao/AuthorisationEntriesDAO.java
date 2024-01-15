package com.idega.block.creditcard2.data.dao;

import java.sql.Date;
import java.util.List;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.core.persistence.GenericDao;

public interface AuthorisationEntriesDAO<T> extends GenericDao {

	public CreditCardAuthorizationEntry getChild(T entry);

	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date);

	public CreditCardAuthorizationEntry getByMetadata(String key, String value);

	public CreditCardAuthorizationEntry findByReference(String reference);

	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to);

	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to);

	public T store(T entry);

}