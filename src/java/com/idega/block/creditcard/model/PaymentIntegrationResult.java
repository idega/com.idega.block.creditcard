package com.idega.block.creditcard.model;

import java.io.Serializable;

public interface PaymentIntegrationResult extends Serializable {

	public Status getStatus();

}