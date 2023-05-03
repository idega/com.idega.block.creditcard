package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.RapydMerchant;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(RapydMerchantDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class RapydMerchantDAO extends GenericDaoImpl implements MerchantDAO<RapydMerchant> {

	public static final String BEAN_NAME = "RapydMerchantDAO";

	@Override
	@Transactional(readOnly = false)
	public void store(RapydMerchant merchant) {
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
	public void removeMerchant(RapydMerchant merchant) {
		merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setEndDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setIsDeleted(Boolean.TRUE);
		merge(merchant);
	}

	@Override
	public RapydMerchant findByName(String name) {
		return getSingleResult(RapydMerchant.GET_BY_NAME, RapydMerchant.class,
				new Param(RapydMerchant.nameProp, name));
	}

	@Override
	public CreditCardMerchant findById(Integer id) {
		return getSingleResult(RapydMerchant.GET_BY_ID, RapydMerchant.class, new Param(RapydMerchant.idProp, id));
	}

}