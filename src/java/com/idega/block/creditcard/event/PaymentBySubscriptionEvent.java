package com.idega.block.creditcard.event;

import org.springframework.context.ApplicationEvent;

import com.idega.block.creditcard2.data.beans.Subscription;

public class PaymentBySubscriptionEvent extends ApplicationEvent {

	private static final long serialVersionUID = 3311184230008008774L;

	private Subscription subscription;

	public PaymentBySubscriptionEvent(Object source, Subscription subscription) {
		super(source);

		this.subscription = subscription;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}


}