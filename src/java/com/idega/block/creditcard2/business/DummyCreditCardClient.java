package com.idega.block.creditcard2.business;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.idegaweb.IWApplicationContext;

public class DummyCreditCardClient extends com.idega.block.creditcard.business.DummyCreditCardClient
		implements CreditCardClient {

	public DummyCreditCardClient(IWApplicationContext iwc) {
		super(iwc);
	}
}