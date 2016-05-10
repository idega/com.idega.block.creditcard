package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.DummyMerchant;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(DummyMerchantDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class DummyMerchantDAO extends GenericDaoImpl implements MerchantDAO<DummyMerchant> {
	public static final String BEAN_NAME = "DummyMerchantDAO";

	@Override
	public void store(DummyMerchant merchant) {
		if (merchant.getId() != null) {
			merchant.setStartDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			persist(merchant);
		} else {
			merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			merge(merchant);
		}
	}

	@Override
	public void removeMerchant(DummyMerchant merchant) {
		merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setEndDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setDeleted(Boolean.TRUE);
		merge(merchant);
	}

	@Override
	public DummyMerchant findByName(String name) {
		return getSingleResult(DummyMerchant.findByName, DummyMerchant.class, new Param(DummyMerchant.nameProp, name));
	}

	@Override
	public CreditCardMerchant findById(Integer id) {
		return getSingleResult(DummyMerchant.findById, DummyMerchant.class, new Param(DummyMerchant.idProp, id));
	}

}
