package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class CreateRefund implements Serializable {

	private static final long serialVersionUID = -5785858589731479937L;

	private String payment;		//Unique RAPYD payment id received while making the payment as: "payment_0dd0ddfe830c8fa0ba35e3d7370b2348"

	private String amount;

    private String currency;

    private String merchant_reference_id;

    private String reason;

    public CreateRefund() {
    	super();
    }

    public CreateRefund(String payment, String amount, String currency, String merchantReferenceId, String reason) {
    	this();

    	this.payment = payment;
    	this.amount = amount;
    	this.currency = currency;
    	this.merchant_reference_id = merchantReferenceId;
    	this.reason = reason;
    }

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMerchant_reference_id() {
		return merchant_reference_id;
	}

	public void setMerchant_reference_id(String merchant_reference_id) {
		this.merchant_reference_id = merchant_reference_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}