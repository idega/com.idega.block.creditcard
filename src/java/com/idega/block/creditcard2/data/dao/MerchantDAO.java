package com.idega.block.creditcard2.data.dao;

import com.idega.block.creditcard.data.CreditCardMerchant;

public interface MerchantDAO<T> {

	public void store(T merchant);

	public void removeMerchant(T merchant);

	public CreditCardMerchant findByName(String name);

	public CreditCardMerchant findById(Integer id);

}
