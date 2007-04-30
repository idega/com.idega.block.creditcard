package com.idega.block.creditcard.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CreditCardBusinessHomeImpl extends IBOHomeImpl implements CreditCardBusinessHome {

	public Class getBeanInterfaceClass() {
		return CreditCardBusiness.class;
	}

	public CreditCardBusiness create() throws CreateException {
		return (CreditCardBusiness) super.createIBO();
	}
}