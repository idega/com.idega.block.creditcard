package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(ValitorMerchantDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class ValitorMerchantDAO extends GenericDaoImpl implements MerchantDAO<ValitorMerchant> {
	public static final String BEAN_NAME = "ValitorMerchantDAO";

	@Override
	@Transactional(readOnly = false)
	public void store(ValitorMerchant merchant) {
		if (merchant.getId() == null) {
			merchant.setStartDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			persist(merchant);
		} else {
			merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			merge(merchant);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removeMerchant(ValitorMerchant merchant) {
		merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setEndDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setIsDeleted(Boolean.TRUE);
		merge(merchant);
	}

	@Override
	public ValitorMerchant findByName(String name) {
		return getSingleResult(ValitorMerchant.GET_BY_NAME, ValitorMerchant.class,
				new Param(ValitorMerchant.nameProp, name));
	}

	@Override
	public CreditCardMerchant findById(Integer id) {
		return getSingleResult(ValitorMerchant.GET_BY_ID, ValitorMerchant.class, new Param(ValitorMerchant.idProp, id));
	}

}
