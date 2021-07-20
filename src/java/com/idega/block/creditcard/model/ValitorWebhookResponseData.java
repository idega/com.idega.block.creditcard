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

	private String Type; 		//"CardPayment"
	private String Signature; 	//As "SHA512-fingerprint"
	private Boolean IsSuccess;
	private Boolean IsLife;
	private String Timestamp; 	//As "2021-04-12T07:21:31.3465196Z"

	private ValitorWebhookResponseDataData Data;

	public String getType() {
		return Type;
	}
	public void setType(String type) {
		this.Type = type;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		this.Signature = signature;
	}
	public Boolean getIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.IsSuccess = isSuccess;
	}
	public Boolean getIsLife() {
		return IsLife;
	}
	public void setIsLife(Boolean isLife) {
		this.IsLife = isLife;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.Timestamp = timestamp;
	}
	public ValitorWebhookResponseDataData getData() {
		return Data;
	}
	public void setData(ValitorWebhookResponseDataData data) {
		this.Data = data;
	}

	@Override
	public String toString() {
		String returnStr = "ValitorWebhookResponseData. "
				+ "type: " + Type
				+ ", signature: " + Signature
				+ ", isSuccess: " + IsSuccess
				+ ", isLife: " + IsLife
				+ ", timestamp: " + Timestamp;
		if (getData() != null) {
			returnStr = returnStr + ". " + getData();
		}

		return returnStr;
	}

}