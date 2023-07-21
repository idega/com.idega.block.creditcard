package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.helper.RapydFinanceHelper;
import com.idega.block.creditcard.model.rapyd.Data;
import com.idega.block.creditcard.model.rapyd.WebHook;
import com.idega.block.creditcard2.data.beans.RapydAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.RapydMerchant;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.business.SpringBeanName;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

@Repository(RapydAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
@SpringBeanName(RapydAuthorisationEntryDAO.BEAN_NAME)
public class RapydAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<RapydAuthorisationEntry> {

	public static final String BEAN_NAME = "RapydAuthorisationEntryDAO";

	@Autowired
	private RapydFinanceHelper financeHelper;

	@Override
	public CreditCardAuthorizationEntry getChild(RapydAuthorisationEntry entry) {
		return getSingleResult(RapydAuthorisationEntry.GET_BY_PARENT_ID, RapydAuthorisationEntry.class, new Param(RapydAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		if (StringUtil.isEmpty(code)) {
			return null;
		}

		CreditCardAuthorizationEntry entry = null;
		try {
			entry = getSingleResult(
					RapydAuthorisationEntry.GET_BY_UNIQUE_ID,
					RapydAuthorisationEntry.class,
					new Param(RapydAuthorisationEntry.uniqueIdProp, code)
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by unique ID " + code, e);
		}
		if (entry != null) {
			return entry;
		}

		try {
			entry = getSingleResult(
					RapydAuthorisationEntry.GET_BY_AUTH_CODE,
					RapydAuthorisationEntry.class,
					new Param(RapydAuthorisationEntry.authCodeProp, code)
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by auth. code " + code, e);
		}
		if (entry != null) {
			return entry;
		}

		try {
			List<RapydAuthorisationEntry> entries = getResultList(
					RapydAuthorisationEntry.GET_BY_PAYMENT,
					RapydAuthorisationEntry.class,
					new Param(RapydAuthorisationEntry.paymentProp, code)
			);
			if (!ListUtil.isEmpty(entries)) {
				return entries.iterator().next();
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by payment " + code, e);
		}

		return null;
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(
				RapydAuthorisationEntry.GET_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(RapydAuthorisationEntry.dateFromProp, from),
				new Param(RapydAuthorisationEntry.dateToProp, to)
		);
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(
				RapydAuthorisationEntry.GET_REFUNDS_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(RapydAuthorisationEntry.dateFromProp, from),
				new Param(RapydAuthorisationEntry.dateToProp, to)
		);
	}

	@Transactional(readOnly = false)
	public RapydAuthorisationEntry store(
			WebHook hook,
			Data data,
			Double amount,
			String payment,
			String reference,
			String authCode,
			String last4,
			String brand,
			Timestamp timestamp,
			CreditCardMerchant merchant,
			String currency
	) throws CreditCardAuthorizationException {
		if (merchant != null) {
			if (merchant.getId() == null) {
				persist(merchant);
			} else {
				merchant = getSingleResult(RapydMerchant.GET_BY_ID, RapydMerchant.class, new Param(RapydMerchant.idProp, merchant.getId()));
			}
		}

		CreditCardAuthorizationEntry entry = findByAuthorizationCode(payment, null);
		entry = entry == null ? findByAuthorizationCode(reference, null) : entry;
		entry = entry == null ? findByAuthorizationCode(authCode, null) : entry;
		entry = entry == null ? new RapydAuthorisationEntry() : entry;

		RapydAuthorisationEntry rapydEntry = (RapydAuthorisationEntry) entry;

		rapydEntry.setAmount(amount);
		rapydEntry.setPaymentId(payment);
		rapydEntry.setReference(reference);
		rapydEntry.setAuthorizationCode(authCode);
		rapydEntry.setCardNumber(last4);
		rapydEntry.setBrandName(brand);
		rapydEntry.setDate(timestamp);
		rapydEntry.setTimestamp(timestamp);
		if (merchant != null) {
			rapydEntry.setMerchant(merchant);
		}
		entry.setCurrency(currency);
		if (hook != null) {
			rapydEntry.setSuccess(financeHelper.isSuccess(hook));
			rapydEntry.setServerResponse(CreditCardConstants.GSON.toJson(hook));

		} else if (data != null) {
			rapydEntry.setServerResponse(CreditCardConstants.GSON.toJson(data));
		}

		data = data == null && hook != null ? hook.getData() : data;
		if (data != null) {
			rapydEntry.setErrorNumber(data.getError_code());
			rapydEntry.setRefund(data.isRefunded());
		}

		return store(rapydEntry);
	}

	@Override
	@Transactional(readOnly = false)
	public RapydAuthorisationEntry store(RapydAuthorisationEntry entry) {
		if (entry == null) {
			throw new RuntimeException(RapydAuthorisationEntry.class.getName() + " not provided");
		}

		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		if (entry.getPrimaryKey() == null) {
			throw new RuntimeException(RapydAuthorisationEntry.class.getName() + " could not be saved");
		}

		return entry;
	}

	public RapydAuthorisationEntry findById(Integer parentDataPK) {
		return getSingleResultByInlineQuery(
				"from RapydAuthorisationEntry rae where rae.id =:id",
				RapydAuthorisationEntry.class,
				new Param("id", parentDataPK)
		);
	}

	public String getLastAuthorizationForMerchant(String merchantRrnSuffix, Integer merchantId) {
		return getSingleResultByInlineQuery(
				"select max(bae.rrn) from RapydAuthorisationEntry bae where bae.rrn like :rrn and bae.merchant.id = :id",
				String.class,
				new Param("rrn", merchantRrnSuffix + "%"),
				new Param("id", merchantId)
		);
	}

	@Override
	public RapydAuthorisationEntry getByMetadata(String key, String value) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(value)) {
			return null;
		}

		try {
			List<RapydAuthorisationEntry> entries = getResultList(
					RapydAuthorisationEntry.QUERY_FIND_BY_METADATA,
					RapydAuthorisationEntry.class,
					new Param(RapydAuthorisationEntry.METADATA_KEY_PROP, key),
					new Param(RapydAuthorisationEntry.METADATA_VALUE_PROP, value)
			);
			if (ListUtil.isEmpty(entries)) {
				return null;
			}

			RapydAuthorisationEntry entry = null;
			for (Iterator<RapydAuthorisationEntry> iter = entries.iterator(); (entry == null && iter.hasNext());) {
				entry = iter.next();
			}
			return entry;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting RapydAuthorisationEntry by metadada: " + key + "=" + value, e);
		}

		return null;
	}

}