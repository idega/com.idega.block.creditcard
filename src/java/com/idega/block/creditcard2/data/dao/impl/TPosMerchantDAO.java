package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.TPosMerchant;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(TPosMerchantDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class TPosMerchantDAO extends GenericDaoImpl implements MerchantDAO<TPosMerchant> {
	public static final String BEAN_NAME = "TPosMerchantDAO";

	@Override
	@Transactional(readOnly = false)
	public void store(TPosMerchant merchant) {
		if (merchant.getId() == null) {
			merchant.setStartDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			persist(merchant);
		} else {
			merchant.setModifiedDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			merge(merchant);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removeMerchant(TPosMerchant merchant) {
		merchant.setModifiedDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setEndDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setDeleted(Boolean.TRUE);
		merge(merchant);
	}

	@Override
	public TPosMerchant findByName(String name) {
		return getSingleResult(TPosMerchant.findByName, TPosMerchant.class, new Param(TPosMerchant.nameProp, name));
	}

	@Override
	public CreditCardMerchant findById(Integer id) {
		return getSingleResult(TPosMerchant.findById, TPosMerchant.class, new Param(TPosMerchant.idProp, id));
	}

}