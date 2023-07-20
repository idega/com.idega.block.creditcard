package com.idega.block.creditcard.helper;

import com.idega.block.creditcard.model.rapyd.WebHook;

public interface RapydFinanceHelper {

	public boolean isSuccess(WebHook hook);

	public boolean isWrongStatus(String status);

}