package com.idega.block.creditcard.helper.impl;

import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.helper.RapydFinanceHelper;
import com.idega.block.creditcard.model.rapyd.Data;
import com.idega.block.creditcard.model.rapyd.WebHook;
import com.idega.core.business.DefaultSpringBean;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RapydFinanceHelperImpl extends DefaultSpringBean implements RapydFinanceHelper {

	@Override
	public boolean isSuccess(WebHook hook) {
		if (hook == null) {
			return false;
		}

		try {
			Data data = hook.getData();
			String type = hook.getType();
			String status = data == null ? null : data.getStatus();

			boolean wrongStatus = isWrongStatus(status);
			return !wrongStatus && !StringUtil.isEmpty(type) && CreditCardConstants.PAYMENT_COMPLETED.equals(type);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error checking if success hook:\n" + CreditCardConstants.GSON.toJson(hook), e);
		}
		return false;
	}

	@Override
	public boolean isWrongStatus(String status) {
		if (StringUtil.isEmpty(status)) {
			return true;
		}

		String correctStatusesProp = getSettings().getProperty("rapyd.correct_statuses", CreditCardConstants.CLOSED + CoreConstants.COMMA + CreditCardConstants.ACTIVE);
		return StringUtil.isEmpty(correctStatusesProp) || correctStatusesProp.indexOf(status) == -1;
	}

}