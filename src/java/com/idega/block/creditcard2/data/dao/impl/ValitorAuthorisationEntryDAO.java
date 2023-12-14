package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.ValitorAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.business.SpringBeanName;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

@Repository(ValitorAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
@SpringBeanName(ValitorAuthorisationEntryDAO.BEAN_NAME)
public class ValitorAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<ValitorAuthorisationEntry> {

	public static final String BEAN_NAME = "ValitorAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(ValitorAuthorisationEntry entry) {
		return getSingleResult(ValitorAuthorisationEntry.GET_BY_PARENT_ID, ValitorAuthorisationEntry.class, new Param(ValitorAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
		CreditCardAuthorizationEntry entry = null;
		try {
			//	Searching by unique id, because unique id property contains the merchant reference id, which is needed.
			List<ValitorAuthorisationEntry> entries = getResultList(
					ValitorAuthorisationEntry.GET_BY_UNIQUE_ID,
					ValitorAuthorisationEntry.class,
					new Param(ValitorAuthorisationEntry.uniqueIdProp, code)
			);
			entry = ListUtil.isEmpty(entries) ? null : entries.iterator().next();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by unique ID " + code, e);
		}
		if (entry != null) {
			return entry;
		}

		try {
			List<ValitorAuthorisationEntry> entries = getResultList(
					ValitorAuthorisationEntry.GET_BY_AUTH_CODE,
					ValitorAuthorisationEntry.class,
					new Param(ValitorAuthorisationEntry.authCodeProp, code)
			);
			entry = ListUtil.isEmpty(entries) ? null : entries.iterator().next();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by auth. code " + code, e);
		}
		return entry;
	}

	@Override
	public List<CreditCardAuthorizationEntry> findByDates(Date from, Date to) {
		return getResultList(
				ValitorAuthorisationEntry.GET_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(ValitorAuthorisationEntry.dateFromProp, from),
				new Param(ValitorAuthorisationEntry.dateToProp, to)
		);
	}

	@Override
	public List<CreditCardAuthorizationEntry> findRefunds(Date from, Date to) {
		return getResultList(
				ValitorAuthorisationEntry.GET_REFUNDS_BY_DATES,
				CreditCardAuthorizationEntry.class,
				new Param(ValitorAuthorisationEntry.dateFromProp, from),
				new Param(ValitorAuthorisationEntry.dateToProp, to)
		);
	}

	@Override
	@Transactional(readOnly = false)
	public ValitorAuthorisationEntry store(ValitorAuthorisationEntry entry) {
		if (entry.getId() == null) {
			persist(entry);
		} else {
			merge(entry);
		}

		if (entry.getPrimaryKey() == null) {
			throw new RuntimeException(ValitorAuthorisationEntry.class.getName() + " could not be saved");
		}

		return entry;
	}

	public ValitorAuthorisationEntry findById(Integer parentDataPK) {
		if (parentDataPK == null || parentDataPK.intValue() < 0) {
			return null;
		}

		return getSingleResultByInlineQuery(
				"from ValitorAuthorisationEntry bae where bae.id =:id",
				ValitorAuthorisationEntry.class,
				new Param("id", Long.valueOf(parentDataPK.longValue()))
		);
	}

	public String getLastAuthorizationForMerchant(String merchantRrnSuffix, Integer merchantId) {
		return getSingleResultByInlineQuery(
				"select max(bae.rrn) from ValitorAuthorisationEntry bae where bae.rrn Like :rrn and bae.merchant.id = :id",
				String.class,
				new Param("rrn", merchantRrnSuffix + "%"),
				new Param("id", merchantId)
		);
	}

	public ValitorAuthorisationEntry getByMetadata(String key, String value) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(value)) {
			return null;
		}

		try {
			List<Object> objects = getResultList(
					ValitorAuthorisationEntry.QUERY_FIND_BY_METADATA,
					Object.class,
					new Param(ValitorAuthorisationEntry.METADATA_KEY_PROP, key),
					new Param(ValitorAuthorisationEntry.METADATA_VALUE_PROP, value)
			);
			Object obj = ListUtil.isEmpty(objects) ? null : objects.iterator().next();
			if (obj != null && obj instanceof ValitorAuthorisationEntry) {
				return (ValitorAuthorisationEntry) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting ValitorAuthorisationEntry by metadada: " + key + "=" + value, e);
		}

		return null;
	}

	public ValitorAuthorisationEntry getById(Long id) {
		if (id == null) {
			return null;
		}

		try {
			return getSingleResult(ValitorAuthorisationEntry.GET_BY_ID, ValitorAuthorisationEntry.class, new Param(ValitorAuthorisationEntry.idProp, id));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting auth. entry by ID " + id, e);
		}

		return null;
	}

	public List<Long> getIdsOfSuccfullPaymentsToMarchant(Integer merchantId, Date date) {
		if (merchantId == null || date == null) {
			return null;
		}

		try {
			return getResultList(
					ValitorAuthorisationEntry.QUERY_GET_SUCCESSFULL_PAYMENTS_TO_MERCHANT,
					Long.class,
					new Param(ValitorAuthorisationEntry.PARAM_MERCHANT, merchantId),
					new Param(ValitorAuthorisationEntry.dateFromProp, date)
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting IDs of successfull payments to merchant " + merchantId + " starting from " + date, e);
		}

		return null;
	}

	@Override
	public CreditCardAuthorizationEntry findByReference(String reference) {
		if (StringUtil.isEmpty(reference)) {
			return null;
		}

		try {
			return getSingleResult(ValitorAuthorisationEntry.QUERY_GET_BY_REFRENCE, ValitorAuthorisationEntry.class, new Param(CreditCardAuthorizationEntry.COLUMN_REFERENCE, reference));
 		} catch (Exception e) {
 			getLogger().log(Level.WARNING, "Error getting auth. entry by reference " + reference, e);
 		}

		return null;
	}

}