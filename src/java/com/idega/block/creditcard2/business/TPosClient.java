package com.idega.block.creditcard2.business;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.TPosMerchant;
import com.idega.block.creditcard.data.TPosMerchantHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;;

public class TPosClient extends com.idega.block.creditcard.business.TPosClient implements CreditCardClient {

	public TPosClient(IWApplicationContext iwc) throws Exception {
		super(iwc);
	}

	public TPosClient(IWApplicationContext iwc, CreditCardMerchant merchant) throws Exception {
		super();
		if (merchant.getDatasource() == null) {
			try {
				TPosMerchantHome merHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
				this._merchant = merHome.findByPrimaryKey(merchant.getPrimaryKey());
			} catch (FinderException | IDOLookupException e) {
				e.printStackTrace();
			}
			init(iwc);
		} else {
			this._merchant = merchant;
			init(iwc);
		}
	}

}
