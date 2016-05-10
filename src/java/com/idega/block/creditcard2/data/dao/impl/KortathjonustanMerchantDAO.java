package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;

@Repository(KortathjonustanMerchantDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class KortathjonustanMerchantDAO extends GenericDaoImpl implements MerchantDAO<KortathjonustanMerchant> {
	public static final String BEAN_NAME = "KortathjonustanMerchantDAO";

	@Override
	public void store(KortathjonustanMerchant merchant) {
		if (merchant.getId() != null) {
			merchant.setStartDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			persist(merchant);
		} else {
			merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
			merge(merchant);
		}
	}

	@Override
	public void removeMerchant(KortathjonustanMerchant merchant) {
		merchant.setModificationDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setEndDate(new Date(IWTimestamp.getTimestampRightNow().getTime()));
		merchant.setDeleted(Boolean.TRUE);
		merge(merchant);
	}

	@Override
	public KortathjonustanMerchant findByName(String name) {
		return getSingleResult(KortathjonustanMerchant.findByName, KortathjonustanMerchant.class,
				new Param(KortathjonustanMerchant.nameProp, name));
	}

	@Override
	public CreditCardMerchant findById(Integer id) {
		return getSingleResult(KortathjonustanMerchant.findById, KortathjonustanMerchant.class,
				new Param(KortathjonustanMerchant.idProp, id));
	}

}
