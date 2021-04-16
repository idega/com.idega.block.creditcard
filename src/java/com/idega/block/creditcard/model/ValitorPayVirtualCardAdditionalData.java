package com.idega.block.creditcard.model;

import java.io.Serializable;

public class ValitorPayVirtualCardAdditionalData implements Serializable {

	private static final long serialVersionUID = -581075863315686568L;

	private String merchantReferenceData;

	public ValitorPayVirtualCardAdditionalData() {
		super();
	}

	public ValitorPayVirtualCardAdditionalData(
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