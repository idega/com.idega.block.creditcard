package com.idega.block.creditcard2.business;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;

public class KortathjonustanCreditCardClient extends com.idega.block.creditcard.business.KortathjonustanCreditCardClient
		implements CreditCardClient {

	public KortathjonustanCreditCardClient(IWApplicationContext iwc, String host, int port, String keystoreLocation,
			String keystorePass, CreditCardMerchant merchant) {

		super();

		this.HOST_NAME = host;
		this.HOST_PORT = port;
		this.strKeystore = keystoreLocation;
		this.strKeystorePass = keystorePass;

		try {
			KortathjonustanMerchantHome merHome = (KortathjonustanMerchantHome) IDOLookup
					.getHome(KortathjonustanMerchant.class);
			this.ccMerchant = merHome.findByPrimaryKey(merchant.getPrimaryKey());
		} catch (FinderException | IDOLookupException e) {
			e.printStackTrace();
		}
		this.SITE = merchant.getLocation();
		this.USER = merchant.getUser();
		this.PASSWORD = merchant.getPassword();
		this.ACCEPTOR_TERM_ID = merchant.getTerminalID();
		this.ACCEPTOR_IDENTIFICATION = merchant.getMerchantID();
		init(iwc);

	}

}