package com.idega.block.creditcard.event;

import org.springframework.context.ApplicationEvent;

import com.idega.block.creditcard2.data.beans.Subscription;

public class PaymentBySubscriptionEvent extends ApplicationEvent {

	private static final long serialVersionUID = 3311184230008008774L;

	private Subscription subscription;

	private Boolean success = Boolean.TRUE;

	private String errorMessage;


	public PaymentBySubscriptionEvent(Object source, Subscription subscription, Boolean success, String errorMessage) {
		super(source);

		this.subscription = subscription;
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



}
