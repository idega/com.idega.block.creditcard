package com.idega.block.creditcard.event;

import java.sql.Timestamp;
import java.time.Instant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import com.idega.block.creditcard.model.WebHookable;

public abstract class PaymentEvent extends ApplicationEvent {

	private static final long serialVersionUID = -4542295523430969137L;

	private WebHookable hook;

	private String reference;

	private String authCode;

	private String last4;

	private String cardBrand;

	private Timestamp paidAt;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private ServletContext context;

	public PaymentEvent(
			WebHookable hook,
			String reference,
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext context
	) {
		super(hook);

		this.hook = hook;
		this.reference = reference;
		this.request = request;
		this.response = response;
		this.context = context;
	}

	public PaymentEvent(
			WebHookable hook,
			String reference,
			String authCode,
			String last4,
			String cardBrand,
			int paidAt,
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext context
	) {
		this(hook, reference, request, response, context);

		this.authCode = authCode;
		this.last4 = last4;
		this.cardBrand = cardBrand;
		if (paidAt > 0) {
			Instant instant = Instant.ofEpochSecond(paidAt);
			this.paidAt = Timestamp.from(instant);
		}
	}

	public WebHookable getHook() {
		return hook;
	}

	public void setHook(WebHookable hook) {
		this.hook = hook;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public Timestamp getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Timestamp paidAt) {
		this.paidAt = paidAt;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

}