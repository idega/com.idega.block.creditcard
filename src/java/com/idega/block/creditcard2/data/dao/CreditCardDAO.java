package com.idega.block.creditcard2.data.dao;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.CreditCardMerchant2;
import com.idega.core.persistence.GenericDao;

public interface CreditCardDAO extends GenericDao{
	
	public CreditCardMerchant getMerchantByID(Long id);
	public CreditCardMerchant getMerchantByName(String name); 
	public CreditCardMerchant getMerchantByGatewayId(String id);
	
	public void storeMerchant(CreditCardMerchant2 merchant);
	public void removeMerchant(CreditCardMerchant2 merchant);
	
	public CreditCardAuthorizationEntry getAuthorizationEntryByAuthorizationCode(String authCode);
	public CreditCardAuthorizationEntry getAuthorizationEntryById(Long id);
	public CreditCardAuthorizationEntry getChildAuthorizationEntry(CreditCardAuthorizationEntry auth);
	public CreditCardAuthorizationEntry getParentAuthorizationEntry(CreditCardAuthorizationEntry auth);
	
	public void storeAuthorizationEntry(CreditCardAuthorizationEntry auth);
	
}
