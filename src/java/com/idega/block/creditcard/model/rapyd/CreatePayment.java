package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class CreatePayment implements Serializable {

	private static final long serialVersionUID = 248339525320518302L;

	private Integer amount;

    private String currency;

    private PaymentMethod payment_method;

    private Boolean capture = Boolean.TRUE;

    private String description;

    private String customer;

    private PaymentMethodOptions payment_method_options;

    private boolean save_payment_method = Boolean.TRUE;

    private String complete_payment_url;

    private String error_payment_url;

    private String merchant_reference_id;

    private String receipt_email;

    private String statement_descriptor;

    public CreatePayment() {
    	super();
    }

    public CreatePayment(Integer amount, String currency, String type, String name, String number, String expMonth, String expYear, String cvv, String reference) {
    	this();

    	this.amount = amount;
    	this.currency = currency;
    	this.payment_method = new PaymentMethod(type, name, number, expMonth, expYear, cvv);
    	this.merchant_reference_id = reference;
    	this.payment_method_options = new PaymentMethodOptions(true);
    }

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public PaymentMethod getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(PaymentMethod payment_method) {
		this.payment_method = payment_method;
	}

	public Boolean getCapture() {
		return capture;
	}

	public void setCapture(Boolean capture) {
		this.capture = capture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public PaymentMethodOptions getPayment_method_options() {
		return payment_method_options;
	}

	public void setPayment_method_options(PaymentMethodOptions payment_method_options) {
		this.payment_method_options = payment_method_options;
	}

	public boolean isSave_payment_method() {
		return save_payment_method;
	}

	public void setSave_payment_method(boolean save_payment_method) {
		this.save_payment_method = save_payment_method;
	}

	public String getComplete_payment_url() {
		return complete_payment_url;
	}

	public void setComplete_payment_url(String complete_payment_url) {
		this.complete_payment_url = complete_payment_url;
	}

	public String getError_payment_url() {
		return error_payment_url;
	}

	public void setError_payment_url(String error_payment_url) {
		this.error_payment_url = error_payment_url;
	}

	public String getMerchant_reference_id() {
		return merchant_reference_id;
	}

	public void setMerchant_reference_id(String merchant_reference_id) {
		this.merchant_reference_id = merchant_reference_id;
	}

	public String getReceipt_email() {
		return receipt_email;
	}

	public void setReceipt_email(String receipt_email) {
		this.receipt_email = receipt_email;
	}

	public String getStatement_descriptor() {
		return statement_descriptor;
	}

	public void setStatement_descriptor(String statement_descriptor) {
		this.statement_descriptor = statement_descriptor;
	}

}