package com.idega.block.creditcard2.data.dao;

import java.sql.Date;
import java.util.List;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;

public interface AuthorisationEntriesDAO<T> {

	public CreditCardAuthorizationEntry getChild(T entry);

	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date);

	public CreditCardAuthorizationEntry getByMetadata(String key, String value);

	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to);

	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to);

	public T store(T entry);

}