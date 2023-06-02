package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class Metadata implements Serializable {

	private static final long serialVersionUID = -8327476471198446637L;

	private Boolean merchant_defined;

	public Boolean getMerchant_defined() {
		return merchant_defined;
	}

	public void setMerchant_defined(Boolean merchant_defined) {
		this.merchant_defined = merchant_defined;
	}

}