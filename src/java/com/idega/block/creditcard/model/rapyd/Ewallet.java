package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class Ewallet implements Serializable {

    private static final long serialVersionUID = 4385878685525146298L;

	private String ewallet_id;

    private int amount;

    private int percent;

    private int refunded_amount;

	public String getEwallet_id() {
		return ewallet_id;
	}

	public void setEwallet_id(String ewallet_id) {
		this.ewallet_id = ewallet_id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getRefunded_amount() {
		return refunded_amount;
	}

	public void setRefunded_amount(int refunded_amount) {
		this.refunded_amount = refunded_amount;
	}

}