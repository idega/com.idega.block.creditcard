package com.idega.block.creditcard2.data.dao.impl;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.RapydAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.business.SpringBeanName;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.StringUtil;

@Repository(RapydAuthorisationEntryDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
@SpringBeanName(RapydAuthorisationEntryDAO.BEAN_NAME)
public class RapydAuthorisationEntryDAO extends GenericDaoImpl implements AuthorisationEntriesDAO<RapydAuthorisationEntry> {

	public static final String BEAN_NAME = "RapydAuthorisationEntryDAO";

	@Override
	public CreditCardAuthorizationEntry getChild(RapydAuthorisationEntry entry) {
		return getSingleResult(RapydAuthorisationEntry.GET_BY_PARENT_ID, RapydAuthorisationEntry.class, new Param(RapydAuthorisationEntry.parentProp, entry.getId()));
	}

	@Override
	public CreditCardAuthorizationEntry findByAuthorizationCode(String code, Date date) {
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
		return entry;
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

	@Override
	@Transactional(readOnly = false)
	public RapydAuthorisationEntry store(RapydAuthorisationEntry entry) {
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
				"select max(bae.rrn) from RapydAuthorisationEntry bae where bae.rrn Like :rrn and bae.merchant.id = :id",
				String.class,
				new Param("rrn", merchantRrnSuffix + "%"),
				new Param("id", merchantId)
		);
	}

	public RapydAuthorisationEntry getByMetadata(String key, String value) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(value)) {
			return null;
		}

		try {
			Object obj = getSingleResult(
					RapydAuthorisationEntry.QUERY_FIND_BY_METADATA,
					Object.class,
					new Param(RapydAuthorisationEntry.METADATA_KEY_PROP, key),
					new Param(RapydAuthorisationEntry.METADATA_VALUE_PROP, value)
			);
			if (obj != null && obj instanceof RapydAuthorisationEntry) {
				return (RapydAuthorisationEntry) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting RapydAuthorisationEntry by metadada: " + key + "=" + value, e);
		}

		return null;
	}

}