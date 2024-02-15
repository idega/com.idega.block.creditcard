package com.idega.block.creditcard.gateway;

import com.idega.block.creditcard.service.FinanceService;

public interface CreditCardGateway extends FinanceService {

	public static final String	PATH = "/card/finance",

								HOOK = "/hook",
								SUCCESS = HOOK + "/success",
								FAILURE = HOOK + "/failure";

}