package com.idega.block.creditcard.event;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.block.creditcard.model.WebHookable;

public class PaymentFailedEvent extends PaymentEvent {

	private static final long serialVersionUID = 8299350738013820991L;

	public PaymentFailedEvent(WebHookable hook, String payment, String reference, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		super(hook, payment, reference, request, response, context);
	}

}