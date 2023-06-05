package com.idega.block.creditcard.model.rapyd;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.model.PaymentIntegrationResult;
import com.idega.block.creditcard.model.Status;

public class RapydException extends CreditCardAuthorizationException {

	private static final long serialVersionUID = 1629999009633570876L;

	private Status status;

	public RapydException(Throwable cause, String message) {
		this(cause, message, null);
	}

	public RapydException(Throwable cause, String message, PaymentIntegrationResult result) {
		super(cause, message, result == null || result.getStatus() == null ? null : result.getStatus().getErrorCode());

		this.status = result == null ? null : result.getStatus();
	}

	public RapydException(String message, PaymentIntegrationResult result) {
		super(message);

		this.status = result == null ? null : result.getStatus();
	}

	public RapydException(String message) {
		super(message);
	}

}