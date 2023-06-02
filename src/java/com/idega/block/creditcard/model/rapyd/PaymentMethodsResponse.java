package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;
import java.util.ArrayList;

import com.idega.block.creditcard.model.Status;

public class PaymentMethodsResponse implements Serializable {

	private static final long serialVersionUID = 941594752171528231L;

	private Status status;

	private ArrayList<Datum> data;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ArrayList<Datum> getData() {
		return data;
	}

	public void setData(ArrayList<Datum> data) {
		this.data = data;
	}

}