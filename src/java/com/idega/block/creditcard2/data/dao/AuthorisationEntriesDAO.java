package com.idega.block.creditcard2.data.dao;

import java.sql.Date;
import java.util.List;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;

public interface AuthorisationEntriesDAO<T> {
	public CreditCardAuthorizationEntry getChild(T entry);
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date);
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to);
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to);
	public void store(T entry);
}
