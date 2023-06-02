package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class TextualCodes implements Serializable {

	private static final long serialVersionUID = 6322265275787977229L;

	private Code code;

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

}