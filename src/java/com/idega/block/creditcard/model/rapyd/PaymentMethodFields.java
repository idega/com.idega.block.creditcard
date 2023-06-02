package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class PaymentMethodFields implements Serializable {

	private static final long serialVersionUID = 3151114926567082520L;

	private String name;

	private String number;

	private String expiration_month;

	private String expiration_year;

	private String cvv;

	public PaymentMethodFields() {
		super();
	}

	public PaymentMethodFields(String name, String number, String expMonth, String expYear, String cvv) {
		this();

		this.name = name;
		this.number = number;
		if (expMonth != null && expMonth.length() == 1) {
			expMonth = String.valueOf(0).concat(expMonth);
		}
		this.expiration_month = expMonth;
		if (expYear != null && expYear.length() > 2) {
			expYear = expYear.substring(expYear.length() - 2);
		}
		this.expiration_year = expYear;
		this.cvv = cvv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiration_month() {
		return expiration_month;
	}

	public void setExpiration_month(String expiration_month) {
		this.expiration_month = expiration_month;
	}

	public String getExpiration_year() {
		return expiration_year;
	}

	public void setExpiration_year(String expiration_year) {
		this.expiration_year = expiration_year;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

}