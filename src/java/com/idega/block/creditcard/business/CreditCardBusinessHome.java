package com.idega.block.creditcard.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CreditCardBusinessHome extends IBOHome {

	public CreditCardBusiness create() throws CreateException, RemoteException;
}