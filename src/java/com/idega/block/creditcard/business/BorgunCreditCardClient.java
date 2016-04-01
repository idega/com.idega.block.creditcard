package com.idega.block.creditcard.business;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;

import Borgun.Heimir.pub.ws.Authorization.Authorization_PortTypeProxy;

import com.idega.block.creditcard.data.BorgunAuthorisationEntry;
import com.idega.block.creditcard.data.BorgunDocument;
import com.idega.block.creditcard.data.BorgunMerchant;

public class BorgunCreditCardClient implements CreditCardClient{

	private String login;
	private String password;
	private String url;
	private CreditCardMerchant merchant;
	private BorgunAuthorisationEntry auth = null;
	
	public BorgunCreditCardClient(CreditCardMerchant merchant){
		if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(merchant.getType())){
			this.login = ((BorgunMerchant)merchant).getUser();
			this.password = ((BorgunMerchant)merchant).getPassword();
			this.url = ((BorgunMerchant)merchant).getMerchantUrl();
		}
	}
	
	@Override
	public String getExpireDateString(String month, String year) {
		return year+month;
	}

	
	//set parameter borgun_supported_credit_cards before use
	@Override
	public Collection<String> getValidCardTypes() {
		return TPosClient.getValidCardTypes("borgun");
	}

	@Override
	public CreditCardMerchant getCreditCardMerchant() {
		return merchant;
	}

	@Override
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, Object parentDataPK, String extraField)
					throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		Authorization_PortTypeProxy port = new Authorization_PortTypeProxy(this.url,this.login,this.password);
		HashMap<String, String> params = new HashMap<String, String>();
		return null;
	}

	@Override
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
					throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishTransaction(String properties) throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String voidTransaction(String properties) throws CreditCardAuthorizationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsDelayedTransactions() {
		return true;
	}

	@Override
	public CreditCardAuthorizationEntry getAuthorizationEntry() {
		return auth;
	}

	@Override
	public String getAuthorizationNumber(String properties) {
		try {
			BorgunDocument data = new BorgunDocument(properties);
			if (!data.getData().containsKey("AuthCode")) return null;
			return data.getData().get("AuthCode");
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return null;
		}
		
	}

}
