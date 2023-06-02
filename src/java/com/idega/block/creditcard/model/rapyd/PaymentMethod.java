package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

	private static final long serialVersionUID = -537026199136632072L;

	private String type;

	private PaymentMethodFields fields;

	public PaymentMethod() {
		super();
	}

	public PaymentMethod(String type, String name, String number, String expMonth, String expYear, String cvv) {
		this();

		this.type = type;
		this.fields = new PaymentMethodFields(name, number, expMonth, expYear, cvv);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PaymentMethodFields getFields() {
		return fields;
	}

	public void setFields(PaymentMethodFields fields) {
		this.fields = fields;
	}

}