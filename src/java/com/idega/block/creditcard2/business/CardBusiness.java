package com.idega.block.creditcard2.business;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.util.IWTimestamp;

public interface CardBusiness {

	CreditCardClient getCardClient(Supplier supplier, IWTimestamp timestamp) throws Exception;

}