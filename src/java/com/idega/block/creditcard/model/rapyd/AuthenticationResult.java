package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class AuthenticationResult implements Serializable {

	private static final long serialVersionUID = -3927248714869233821L;

	private String eci;

    private String result;

    private String version;

	public String getEci() {
		return eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}