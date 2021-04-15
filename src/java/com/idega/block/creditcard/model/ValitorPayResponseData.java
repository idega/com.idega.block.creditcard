package com.idega.block.creditcard.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ValitorPayResponseData implements Serializable {
	private static final long serialVersionUID = -4453041109576575679L;

	//RESPONSE - 200
	private String verificationHtml;
	private String postUrl;
	private String verificationStatus;
	private Boolean isSuccess;
	private String responseCode;
	private String responseDescription;
	private String responseTime;
	private String correlationID;
	//For virtual card payment
	private String acquirerReferenceNumber;
	private String transactionID;
	private String authorizationCode;
	private String transactionLifecycleId;

	//RESPONSE - 400
	private String title;


	//RESPONSE - 500
	private String message;


	public String getVerificationHtml() {
		return verificationHtml;
	}
	public void setVerificationHtml(String verificationHtml) {
		this.verificationHtml = verificationHtml;
	}
	public String getPostUrl() {
		return postUrl;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	public String getVerificationStatus() {
		return verificationStatus;
	}
	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
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
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getCorrelationID() {
		return correlationID;
	}
	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAcquirerReferenceNumber() {
		return acquirerReferenceNumber;
	}
	public void setAcquirerReferenceNumber(String acquirerReferenceNumber) {
		this.acquirerReferenceNumber = acquirerReferenceNumber;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
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


}
