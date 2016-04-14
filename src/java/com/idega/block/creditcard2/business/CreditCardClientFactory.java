package com.idega.block.creditcard2.business;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(CreditCardClientFactory.BEAN_NAME)
@Scope("singleton")
public class CreditCardClientFactory {
	public static final String BEAN_NAME = "CreditCardClientFactory";
	
	public CreditCardClient getCrediCardClient(){
		return null;
	}
	
}
