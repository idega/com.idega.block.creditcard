package com.idega.block.creditcard.model.rapyd;

import com.idega.block.creditcard.model.PaymentIntegrationResult;
import com.idega.block.creditcard.model.Status;

public class PaymentResult implements PaymentIntegrationResult {

	private static final long serialVersionUID = 1321034848309498065L;

	private Status status;

	private Data data;

	@Override
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