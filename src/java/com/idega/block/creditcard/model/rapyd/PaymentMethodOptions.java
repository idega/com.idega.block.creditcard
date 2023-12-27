package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PaymentMethodOptions implements Serializable {

	private static final long serialVersionUID = 1122640063794032483L;

	@JsonProperty("3d_required")
	@SerializedName(value = "3d_required")
    private boolean _3d_required;

	public PaymentMethodOptions() {
		super();
	}

	public PaymentMethodOptions(boolean _3d_required) {
		this();

		this._3d_required = _3d_required;
	}

	public boolean is_3d_required() {
		return _3d_required;
	}

	public void set_3d_required(boolean _3d_required) {
		this._3d_required = _3d_required;
	}

}