package com.idega.block.creditcard.data;


public class DummyMerchantHomeImpl extends com.idega.data.IDOFactory implements DummyMerchantHome
{
 protected Class getEntityInterfaceClass(){
  return DummyMerchant.class;
 }


 public DummyMerchant create() throws javax.ejb.CreateException{
  return (DummyMerchant) super.createIDO();
 }


 public DummyMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DummyMerchant) super.findByPrimaryKeyIDO(pk);
 }



}