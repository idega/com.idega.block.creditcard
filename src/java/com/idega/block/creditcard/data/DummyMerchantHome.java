package com.idega.block.creditcard.data;


public interface DummyMerchantHome extends com.idega.data.IDOHome
{
 public DummyMerchant create() throws javax.ejb.CreateException;
 public DummyMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}