package com.idega.block.creditcard2.data.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard2.data.beans.Subscription;
import com.idega.block.creditcard2.data.beans.SubscriptionPayment;
import com.idega.block.creditcard2.data.dao.SubscriptionDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

@Repository(SubscriptionDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class SubscriptionDAOImpl extends GenericDaoImpl implements SubscriptionDAO {

	@Override
	public List<Subscription> getSubscriptionsForUser(Integer userId) {
		if (userId == null) {
			return null;
		}

		try {
			return getResultList(
					Subscription.GET_ALL_BY_USER_ID,
					Subscription.class,
					new Param(Subscription.PARAM_USER_ID, userId)
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting subscriptions by user id: " + userId, e);
		}
		return null;
	}

	@Override
	public List<Subscription> getSubscriptionsForUserByStatus(Integer userId, Boolean status) {
		if (userId == null || status == null) {
			return null;
		}

		try {
			List<Param> params = new ArrayList<>();
			params.add(new Param(Subscription.PARAM_USER_ID, userId));
			params.add(new Param(Subscription.PARAM_STATUS, status));
			return getResultList(
					Subscription.GET_ALL_BY_USER_ID_AND_STATUS,
					Subscription.class,
					params.toArray(new Param[params.size()])
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting subscriptions by user id: " + userId + " and status (is enabled): " + status, e);
		}
		return null;
	}

	@Override
	public Subscription getOneSubscriptionForUserByStatus(Integer userId, Boolean status) {
		if (userId == null || status == null) {
			return null;
		}

		try {
			List<Param> params = new ArrayList<>();
			params.add(new Param(Subscription.PARAM_USER_ID, userId));
			params.add(new Param(Subscription.PARAM_STATUS, status));
			return getSingleResult(
					Subscription.GET_ALL_BY_USER_ID_AND_STATUS,
					Subscription.class,
					params.toArray(new Param[params.size()])
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting subscriptions by user id: " + userId + " and status (is enabled): " + status, e);
		}
		return null;
	}

	@Override
	public List<Subscription> getAllActive() {
		try {
			return getResultList(
					Subscription.GET_ALL_ACTIVE,
					Subscription.class
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting active subscriptions.", e);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public Subscription createUpdateSubscription(Subscription subscription) {
		if (subscription == null) {
			getLogger().warning("Subscription is not provided");
			return null;
		}

		try {
			//Update/create the subscription
			if (subscription.getId() == null) {
				persist(subscription);
			} else {
				merge(subscription);
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING,"Could not create/update the subscription. Error message was: " + e.getLocalizedMessage(), e);
			return null;
		}

		return subscription;
	}

	@Override
	public List<Subscription> cancelSubscriptionsForUser(Integer userId) {
		if (userId == null) {
			getLogger().warning("User id is not provided");
			return null;
		}

		List<Subscription> cancelledSubscriptions = new ArrayList<>();

		try {

			List<Subscription> subscriptions = getSubscriptionsForUserByStatus(userId, Boolean.TRUE);
			if (!ListUtil.isEmpty(subscriptions)) {
				for (Subscription subscription : subscriptions) {
					if (subscription != null && subscription.getId() != null) {
						subscription.setEnabled(Boolean.FALSE);
						subscription.setDisabled(IWTimestamp.getTimestampRightNow());
						Subscription savedSubscription = createUpdateSubscription(subscription);
						if (savedSubscription != null) {
							cancelledSubscriptions.add(savedSubscription);
						}
					}
				}
			}

		} catch (Exception e) {
			getLogger().log(Level.WARNING,"Could not cancel subscriptions for user with id: " + userId + " Error message was: " + e.getLocalizedMessage(), e);
			return null;
		}

		return cancelledSubscriptions;
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deleteSubscriptionsForUser(Integer userId) {
		if (userId == null) {
			getLogger().warning("User id is not provided");
			return null;
		}

		try {
			Query query = getEntityManager().createNamedQuery(Subscription.DELETE_ALL_BY_USER_ID);
			query.setParameter(Subscription.PARAM_USER_ID, userId);
			query.executeUpdate();

			return true;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Could not remove the subscriptions for the user with id: " + userId + ". Error message was: " + e.getLocalizedMessage(), e);
		}

		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public SubscriptionPayment doCreateSubscriptionPayment(Integer userId, Long subscriptionId, String authCode) {
		if (userId == null || subscriptionId == null) {
			return null;
		}

		try {
			SubscriptionPayment sp = new SubscriptionPayment();
			sp.setUserId(userId);
			sp.setSubscriptionId(subscriptionId);
			sp.setAuthCode(authCode);

			persist(sp);

			return sp.getId() == null ? null : sp;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating record for subscription payment. User ID: " + userId + ", subscription ID: " + subscriptionId + ", auth. code: " + authCode, e);
		}

		return null;
	}

	@Override
	public List<SubscriptionPayment> getAllSubscriptionPaymentsForUser(Integer userId) {
		if (userId == null) {
			return null;
		}

		try {
			return getResultList(SubscriptionPayment.QUERY_GET_ALL_BY_USER_ID, SubscriptionPayment.class, new Param(SubscriptionPayment.PARAM_USER_ID, userId));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting records for subscription payment for user ID " + userId, e);
		}

		return null;
	}

}