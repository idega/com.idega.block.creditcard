package com.idega.block.creditcard.model;

public class AuthEntryData {

	private String authCode, uniqueId;

	public AuthEntryData(String authCode, String uniqueId) {
		super();

		this.authCode = authCode;
		this.uniqueId = uniqueId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public String toString() {
		return "Auth. code: " + getAuthCode() + ", unique ID: " + getUniqueId();
	}

}