package com.idega.block.creditcard.model;

import java.io.Serializable;

public class ValitorPayVirtualCardData implements Serializable {
	private static final long serialVersionUID = 2832777737979590086L;

	public static final String SUBSEQUENT_TRANSACTION_TYPE = "CardholderInitiatedCredentialOnFile";

	private String subsequentTransactionType = SUBSEQUENT_TRANSACTION_TYPE;


	public ValitorPayVirtualCardData() {
	}

	public ValitorPayVirtualCardData(String subsequentTransactionType) {
		this.subsequentTransactionType = subsequentTransactionType;
	}



	public String getSubsequentTransactionType() {
		return subsequentTransactionType;
	}

	public void setSubsequentTransactionType(String subsequentTransactionType) {
		this.subsequentTransactionType = subsequentTransactionType;
	}



}
