package com.idega.block.creditcard.model;

public class ValitorWebHook implements WebHookable {

	private static final long serialVersionUID = 2548313037998969450L;

	private Payable payment;

	public ValitorWebHook(Payable payment) {
		this.payment = payment;
	}

	@Override
	public Payable getPayment() {
		return payment;
	}

	public void setPayment(Payable payment) {
		this.payment = payment;
	}

}