package com.idega.block.creditcard.model.rapyd;

import java.util.ArrayList;

import com.idega.block.creditcard.model.PaymentIntegrationResult;
import com.idega.block.creditcard.model.Status;

public class PaymentMethodsResponse implements PaymentIntegrationResult {

	private static final long serialVersionUID = 941594752171528231L;

	private Status status;

	private ArrayList<Datum> data;

	@Override
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