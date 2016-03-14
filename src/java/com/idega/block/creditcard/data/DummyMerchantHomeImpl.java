package com.idega.block.creditcard.data;

import com.idega.data.IDOEntity;

public class DummyMerchantHomeImpl extends com.idega.data.IDOFactory implements DummyMerchantHome
{
 @Override
protected Class getEntityInterfaceClass(){
  return DummyMerchant.class;
 }


 @Override
public DummyMerchant create() throws javax.ejb.CreateException{
  return (DummyMerchant) super.createIDO();
 }


 @Override
public DummyMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DummyMerchant) super.findByPrimaryKeyIDO(pk);
 }

 	@Override
	public DummyMerchant findByName(String name) throws javax.ejb.FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((DummyMerchantBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}


}