package com.idega.block.creditcard.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ValitorWebhookResponseDataData implements Serializable {

	private static final long serialVersionUID = -6985835352807317377L;

	private String transactionId;			//As "914217598109"
	private String authorizationCode;		//As "HOSTOK"
	private String transactionLifecycleId;	//As "ABC123"
	private String acquirerReferenceNumber;	//As "123456"
	private String virtualCardNumber;		//As "99999999-9999-9999-9999-999999999999"
	private String merchantReferenceId;		//As "00000000-0000-0000-0000-000000000000"
	private String responseCode;			//As "00-I"
	private String responseDescription;		//As "Authorized"

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getTransactionLifecycleId() {
		return transactionLifecycleId;
	}
	public void setTransactionLifecycleId(String transactionLifecycleId) {
		this.transactionLifecycleId = transactionLifecycleId;
	}
	public String getAcquirerReferenceNumber() {
		return acquirerReferenceNumber;
	}
	public void setAcquirerReferenceNumber(String acquirerReferenceNumber) {
		this.acquirerReferenceNumber = acquirerReferenceNumber;
	}
	public String getVirtualCardNumber() {
		return virtualCardNumber;
	}
	public void setVirtualCardNumber(String virtualCardNumber) {
		this.virtualCardNumber = virtualCardNumber;
	}
	public String getMerchantReferenceId() {
		return merchantReferenceId;
	}
	public void setMerchantReferenceId(String merchantReferenceId) {
		this.merchantReferenceId = merchantReferenceId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

}