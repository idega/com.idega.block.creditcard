package com.idega.block.creditcard.model;

import java.io.Serializable;

public class ValitorPayRefundWithCorrelationIdData implements Serializable {


	private static final long serialVersionUID = -6218514679910596374L;

	private String originalCorrelationId;		//The CorrelationId of the transaction to be refunded.
	private String originalTransactionDate;		//Date of when the original transaction was made.
	private String correlationId;				//Unique id for the request from the merchant system, for tracked logging and duplication check.
	private String systemCalling;				//Specify the name and version of the system calling RapydGateway. Important to have a space between the name and the version. Used for tracking purposes only.

	public ValitorPayRefundWithCorrelationIdData() {
		super();
	}

	public ValitorPayRefundWithCorrelationIdData(
			String originalCorrelationId,
			String originalTransactionDate,
			String correlationId,
			String systemCalling
	) {
		this.originalCorrelationId = originalCorrelationId;
		this.originalTransactionDate = originalTransactionDate;
		this.correlationId = correlationId;
		this.systemCalling = systemCalling;
	}



	public String getOriginalCorrelationId() {
		return originalCorrelationId;
	}

	public void setOriginalCorrelationId(String originalCorrelationId) {
		this.originalCorrelationId = originalCorrelationId;
	}

	public String getOriginalTransactionDate() {
		return originalTransactionDate;
	}

	public void setOriginalTransactionDate(String originalTransactionDate) {
		this.originalTransactionDate = originalTransactionDate;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getSystemCalling() {
		return systemCalling;
	}

	public void setSystemCalling(String systemCalling) {
		this.systemCalling = systemCalling;
	}

	@Override
	public String toString() {
		return	"originalCorrelationId: " + getOriginalCorrelationId() +
				", originalTransactionDate: " + getOriginalTransactionDate() +
				", correlationId: " + getCorrelationId() +
				", systemCalling: " + getSystemCalling();
	}

}