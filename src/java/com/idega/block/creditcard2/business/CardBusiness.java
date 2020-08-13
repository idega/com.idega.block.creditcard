package com.idega.block.creditcard2.business;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.trade.stockroom.data.Supplier;
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

}