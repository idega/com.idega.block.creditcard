package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

import com.idega.block.creditcard.model.Status;

public class PaymentResult implements Serializable {

	private static final long serialVersionUID = 1321034848309498065L;

	private Status status;

	private Data data;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}