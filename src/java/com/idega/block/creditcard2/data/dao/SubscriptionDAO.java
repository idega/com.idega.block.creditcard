package com.idega.block.creditcard2.data.dao;

import java.util.List;

import com.idega.block.creditcard2.data.beans.Subscription;
import com.idega.business.SpringBeanName;
import com.idega.core.persistence.GenericDao;

@SpringBeanName(SubscriptionDAO.BEAN_NAME)
public interface SubscriptionDAO extends GenericDao {

	public static final String BEAN_NAME = "subscriptionDAO";

	public List<Subscription> getSubscriptionsForUser(Integer userId);

	public List<Subscription> getSubscriptionsForUserByStatus(Integer userId, Boolean status);

	public Subscription createUpdateSubscription(Subscription subscription);

	public List<Subscription> cancelSubscriptionsForUser(Integer userId);

	public Boolean deleteSubscriptionsForUser(Integer userId);

	public Subscription getOneSubscriptionForUserByStatus(Integer userId, Boolean status);

	public List<Subscription> getAllActive();

}
