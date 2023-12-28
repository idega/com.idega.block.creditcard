package com.idega.block.creditcard2.business;

import java.sql.Timestamp;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.user.data.bean.User;
import com.idega.util.IWTimestamp;

public interface CardBusiness {

	CreditCardClient getCardClient(Supplier supplier, IWTimestamp timestamp) throws Exception;

	/**
	 * Creates virtual card by provided identifier
	 * @param identifier
	 * @return
	 */
	public VirtualCard getNewVirtualCard(String identifier);

	/**
	 * Finds virtual card by provided token
	 * @param token
	 * @return
	 */
	public VirtualCard getVirtualCard(String token);

	public boolean doUpdateVirtualCard(
			String token,
			String transactionId,
			String card4,
			String brand,
			Integer expireYear,
			Integer expireMonth,
			Boolean enabled,
			User owner
	);

	public VirtualCard createVirtualCard(String identifier, User owner, String cardUniqueId);

	public VirtualCard createVirtualCard(
			String cardUniqueId,
			String token,
			User owner,
			Integer groupId,
			String transactionId,
			String card4,
			String brand,
			Integer expireYear,
			Integer expireMonth,
			Boolean enabled
	);

	public VirtualCard getVirtualCardByOwner(Integer userId);

	public void doMakeSubscriptionPayments();

	public boolean isValidForSubscriptionPayment(Timestamp lastPaymentDate);

	public CreditCardAuthorizationEntry getByReference(String reference);

}