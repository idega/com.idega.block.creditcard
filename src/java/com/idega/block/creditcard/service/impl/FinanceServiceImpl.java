package com.idega.block.creditcard.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.event.PaymentFailedEvent;
import com.idega.block.creditcard.event.PaymentSucceededEvent;
import com.idega.block.creditcard.helper.RapydFinanceHelper;
import com.idega.block.creditcard.model.rapyd.BinDetails;
import com.idega.block.creditcard.model.rapyd.Data;
import com.idega.block.creditcard.model.rapyd.PaymentMethodData;
import com.idega.block.creditcard.model.rapyd.WebHook;
import com.idega.block.creditcard.service.FinanceService;
import com.idega.block.creditcard2.data.beans.RapydAuthorisationEntry;
import com.idega.block.creditcard2.data.dao.impl.RapydAuthorisationEntryDAO;
import com.idega.core.business.DefaultSpringBean;
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

	@Autowired
	private RapydAuthorisationEntryDAO rapydAuthEntryDAO;

	@Autowired
	private RapydFinanceHelper financeHelper;

	private Map<String, Boolean> HOOKS = new ConcurrentHashMap<>();

	@Override
	public void doHandleSuccessWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		WebHook hook = null;
		try {
			hook = getHook(request);
			doPrintInfo(hook, request, response, context);
			if (hook == null) {
				getLogger().warning("Hook not provided or do not know how to handle hook " + (hook == null ? CoreConstants.EMPTY : hook.getClass().getName()) +
						"! Can not handle success hook");
				return;
			}

			String hookId = hook.getId();
			if (!StringUtil.isEmpty(hookId)) {
				if (HOOKS.containsKey(hookId)) {
					return;
				}

				HOOKS.put(hookId, Boolean.TRUE);
			}

			Data data = hook.getData();
			String type = hook.getType();
			String status = data == null ? null : data.getStatus();

			boolean success = financeHelper.isSuccess(hook);
			if (!success) {
				getLogger().warning("Not success hook with type " + type + " and status " + status + ":\n" + hook);

				String successTypesProp = getSettings().getProperty(
						"rapyd.success_events",
						CreditCardConstants.PAYMENT_COMPLETED.concat(CoreConstants.COMMA).concat(CreditCardConstants.PAYMENT_SUCCEEDED)
				);
				List<String> successTypes = StringUtil.getValuesFromString(successTypesProp, CoreConstants.COMMA);
				if (ListUtil.isEmpty(successTypes) || !successTypes.contains(type) || financeHelper.isWrongStatus(status)) {
					PaymentFailedEvent failure = new PaymentFailedEvent(
							hook,
							data == null ? null : data.getId(),
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

			String payment = data == null ? null : data.getId();
			payment = StringUtil.isEmpty(payment) ?
					data == null ?
							null :
							data.getPayment() :
					payment;
			String reference = data == null ? null : data.getMerchant_reference_id();
			String authCode = data == null ? null : data.getAuth_code();
			String last4 = paymentData == null ? null : paymentData.getLast4();
			String brand = binDetails == null ? null : binDetails.getBrand();
			int paidAt = data == null ? null : data.getPaid_at();

			getCreatedAuthEntry(hook, payment, reference, authCode, last4, brand, paidAt);

			ELUtil.getInstance().publishEvent(
					new PaymentSucceededEvent(
							hook,
							payment,
							reference,
							authCode,
							last4,
							brand,
							paidAt,
							request,
							response,
							context
					)
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error handling success hook:\n" + hook, e);
		}
	}

	private RapydAuthorisationEntry getCreatedAuthEntry(WebHook hook, String payment, String reference, String authCode, String last4, String brand, int paidAt) {
		try {
			Data data = hook == null ? null : hook.getData();
			int amount = data == null ? -1 : data.getAmount();
			if (amount <= 0 && data != null) {
				amount = data.getOriginal_amount();
			}
			Timestamp timestamp = null;
			if (paidAt > 0) {
				Instant instant = Instant.ofEpochSecond(paidAt);
				timestamp = Timestamp.from(instant);
			}

			return rapydAuthEntryDAO.store(
					hook,
					null,
					amount >= 0 ?
							Integer.valueOf(amount).doubleValue() :
							null,
					payment,
					reference,
					authCode,
					last4,
					brand,
					timestamp,
					null
			);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating auth. entry for success hook:\n" + (hook == null ? null : CreditCardConstants.GSON.toJson(hook)), e);
		}
		return null;
	}

	@Override
	public void doHandleFailureWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		WebHook hook = null;
		try {
			hook = getHook(request);
			doPrintInfo(hook, request, response, context);
			if (hook == null) {
				getLogger().warning("Hook not provided or do not know how to handle hook " + (hook == null ? CoreConstants.EMPTY : hook.getClass().getName()) +
						"! Can not handle failure hook");
				return;
			}

			String hookId = hook.getId();
			if (!StringUtil.isEmpty(hookId)) {
				if (HOOKS.containsKey(hookId)) {
					return;
				}

				HOOKS.put(hookId, Boolean.TRUE);
			}

			Data data = hook.getData();
			PaymentFailedEvent failure = new PaymentFailedEvent(
					hook,
					data == null ? null : data.getId(),
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

	private void doPrintInfo(WebHook hook, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		if (request == null) {
			getLogger().warning("Request is unknown!" + (hook == null ? CoreConstants.EMPTY : " Hook:\n" + CreditCardConstants.GSON.toJson(hook)));
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

		getLogger().info("Header params:\n" + headerParams + "\n Content:\n" + hook + "\n Query parameters:\n" + params);
	}

}