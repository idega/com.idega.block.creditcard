package com.idega.block.creditcard.event;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.block.creditcard.model.WebHookable;

public class PaymentSucceededEvent extends PaymentEvent {

	private static final long serialVersionUID = 8550122485607845036L;

	public PaymentSucceededEvent(
			WebHookable hook,
			String payment,
			String reference,
			String authCode,
			String last4,
			String cardBrand,
			int paidAt,
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext context
	) {
		super(hook, payment, reference, authCode, last4, cardBrand, paidAt, request, response, context);
	}

}