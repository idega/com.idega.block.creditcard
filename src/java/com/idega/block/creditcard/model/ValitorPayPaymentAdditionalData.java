package com.idega.block.creditcard.model;

import java.io.Serializable;

public class ValitorPayPaymentAdditionalData implements Serializable {
	private static final long serialVersionUID = 2295908197936916470L;

	private String merchantReferenceData; //EXAMPLE:  "00000000-0000-0000-0000-000000000000",

	public ValitorPayPaymentAdditionalData() {
		super();
	}

	public ValitorPayPaymentAdditionalData(
			String merchantReferenceData
	) {
		this();

		this.merchantReferenceData = merchantReferenceData;
	}

	public String getMerchantReferenceData() {
		return merchantReferenceData;
	}

	public void setMerchantReferenceData(String merchantReferenceData) {
		this.merchantReferenceData = merchantReferenceData;
	}

}