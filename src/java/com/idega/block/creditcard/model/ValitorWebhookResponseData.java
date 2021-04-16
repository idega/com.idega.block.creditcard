package com.idega.block.creditcard.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ValitorWebhookResponseData implements Serializable {

	private static final long serialVersionUID = 3245706993422208476L;

	private String type; 		//"CardPayment"
	private String signature; 	//As "SHA512-fingerprint"
	private Boolean isSuccess;
	private Boolean isLife;
	private String timestamp; 	//As "2021-04-12T07:21:31.3465196Z"

	private ValitorWebhookResponseDataData data;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getIsLife() {
		return isLife;
	}
	public void setIsLife(Boolean isLife) {
		this.isLife = isLife;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public ValitorWebhookResponseDataData getData() {
		return data;
	}
	public void setData(ValitorWebhookResponseDataData data) {
		this.data = data;
	}

}