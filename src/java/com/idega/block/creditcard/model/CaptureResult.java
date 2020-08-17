package com.idega.block.creditcard.model;

import java.util.Map;

public class CaptureResult {

	private String authCode;

	private String transactionId;

	private Map<String, String> properties;

	public CaptureResult(String authCode, String transactionId, Map<String, String> properties) {
		super();

		this.authCode = authCode;
		this.transactionId = transactionId;
		this.properties = properties;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}