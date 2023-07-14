package com.idega.block.creditcard.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.event.PaymentFailedEvent;
import com.idega.block.creditcard.event.PaymentSucceededEvent;
import com.idega.block.creditcard.model.rapyd.BinDetails;
import com.idega.block.creditcard.model.rapyd.Data;
import com.idega.block.creditcard.model.rapyd.PaymentMethodData;
import com.idega.block.creditcard.model.rapyd.WebHook;
import com.idega.block.creditcard.service.FinanceService;
import com.idega.core.business.DefaultSpringBean;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;
import com.idega.util.expression.ELUtil;

@Scope("request")
@Service(FinanceService.BEAN_NAME)
@Qualifier(CreditCardConstants.QUALIFIER_FINANCE_SERVICE)
public class FinanceServiceImpl extends DefaultSpringBean implements FinanceService {

	@Override
	public void doHandleSuccessWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		WebHook hook = null;
		try {
			hook = getHook(request);

			doHandle("SUCCESS", hook, request, response, context);

			if (hook == null) {
				getLogger().warning("Hook not provided or do not know how to handle hook " + (hook == null ? CoreConstants.EMPTY : hook.getClass().getName()) +
						"! Can not handle success hook");
				return;
			}

			IWMainApplicationSettings settings = getSettings();

			Data data = hook.getData();
			String type = hook.getType();
			String status = data == null ? null : data.getStatus();

			String correctStatusesProp = settings.getProperty("rapyd.correct_statuses", CreditCardConstants.CLOSED);
			boolean wrongStatus = StringUtil.isEmpty(status) || StringUtil.isEmpty(correctStatusesProp) || correctStatusesProp.indexOf(status) == -1;
			boolean proceed = !wrongStatus && !StringUtil.isEmpty(type) && CreditCardConstants.PAYMENT_COMPLETED.equals(type);
			if (!proceed) {
				getLogger().warning("Not success hook with type " + type + " and status " + status + ":\n" + hook);

				String successTypesProp = settings.getProperty(
						"rapyd.success_events",
						CreditCardConstants.PAYMENT_COMPLETED.concat(CoreConstants.COMMA).concat(CreditCardConstants.PAYMENT_SUCCEEDED)
				);
				List<String> successTypes = StringUtil.getValuesFromString(successTypesProp, CoreConstants.COMMA);
				if (wrongStatus || ListUtil.isEmpty(successTypes) || !successTypes.contains(type)) {
					PaymentFailedEvent failure = new PaymentFailedEvent(
							hook,
							data == null ? null : data.getMerchant_reference_id(),
							request,
							response,
							context
					);
					ELUtil.getInstance().publishEvent(failure);
				}

				return;
			}

			PaymentMethodData paymentData = data == null ? null : data.getPayment_method_data();
			BinDetails binDetails = paymentData == null ? null : paymentData.getBin_details();

			//FIXME: We need to have payment id in all the cases - to save for possible refund.
			String authCodeWithPaymentId = null;
			if (data != null) {
				authCodeWithPaymentId = !StringUtil.isEmpty(data.getAuth_code()) && !data.getAuth_code().equalsIgnoreCase("null") ? data.getAuth_code() : CoreConstants.EMPTY;
				if (!StringUtil.isEmpty(data.getId())) {
					authCodeWithPaymentId = authCodeWithPaymentId + CoreConstants.HASH + data.getId();
				}
			}

			PaymentSucceededEvent success = new PaymentSucceededEvent(
					hook,
					data == null ? null : data.getMerchant_reference_id(),
					authCodeWithPaymentId,
					paymentData == null ? null : paymentData.getLast4(),
					binDetails == null ? null : binDetails.getBrand(),
					data == null ? null : data.getPaid_at(),
					request,
					response,
					context
			);
			ELUtil.getInstance().publishEvent(success);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error handling success hook:\n" + hook, e);
		}
	}

	@Override
	public void doHandleFailureWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		WebHook hook = null;
		try {
			hook = getHook(request);

			doHandle("FAILURE", hook, request, response, context);

			if (hook == null) {
				getLogger().warning("Hook not provided or do not know how to handle hook " + (hook == null ? CoreConstants.EMPTY : hook.getClass().getName()) +
						"! Can not handle failure hook");
				return;
			}

			Data data = hook.getData();
			PaymentFailedEvent failure = new PaymentFailedEvent(
					hook,
					data == null ? null : data.getMerchant_reference_id(),
					request,
					response,
					context
			);
			ELUtil.getInstance().publishEvent(failure);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error handling failure hook:\n" + hook, e);
		}
	}

	private WebHook getHook(HttpServletRequest request) {
		try {
			String content = StringHandler.getContentFromInputStream(request.getInputStream());
			return StringUtil.isEmpty(content) ? null : CreditCardConstants.GSON.fromJson(content, WebHook.class);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error reading hook from " + request, e);
		}
		return null;
	}

	private void doHandle(String type, WebHook hook, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		if (request == null) {
			getLogger().warning(type + ". Request is unknown");
			return;
		}

		Map<String, String> headerParams = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				if (StringUtil.isEmpty(header)) {
					continue;
				}

				String param = request.getHeader(header);
				if (!StringUtil.isEmpty(param)) {
					headerParams.put(header, param);
				}
			}
		}

		Map<String, List<String>> params = new HashMap<>();
		Map<String, String[]> requestParams = request.getParameterMap();
		if (!MapUtil.isEmpty(requestParams)) {
			for (String param: requestParams.keySet()) {
				String[] values = requestParams.get(param);
				params.put(param, ArrayUtil.isEmpty(values) ? Collections.emptyList() : Arrays.asList(values));
			}
		}

		getLogger().info(type + ". Header params:\n" + headerParams + "\n Content:\n" + hook + "\n Query parameters:\n" + params);
	}

}