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

	private String TransactionId;			//As "914217598109"
	private String AuthorizationCode;		//As "HOSTOK"
	private String TransactionLifecycleId;	//As "ABC123"
	private String AcquirerReferenceNumber;	//As "123456"
	private String VirtualCardNumber;		//As "99999999-9999-9999-9999-999999999999"
	private String MerchantReferenceId;		//As "00000000-0000-0000-0000-000000000000"
	private String ResponseCode;			//As "00-I"
	private String ResponseDescription;		//As "Authorized"

	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		this.TransactionId = transactionId;
	}
	public String getAuthorizationCode() {
		return AuthorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.AuthorizationCode = authorizationCode;
	}
	public String getTransactionLifecycleId() {
		return TransactionLifecycleId;
	}
	public void setTransactionLifecycleId(String transactionLifecycleId) {
		this.TransactionLifecycleId = transactionLifecycleId;
	}
	public String getAcquirerReferenceNumber() {
		return AcquirerReferenceNumber;
	}
	public void setAcquirerReferenceNumber(String acquirerReferenceNumber) {
		this.AcquirerReferenceNumber = acquirerReferenceNumber;
	}
	public String getVirtualCardNumber() {
		return VirtualCardNumber;
	}
	public void setVirtualCardNumber(String virtualCardNumber) {
		this.VirtualCardNumber = virtualCardNumber;
	}
	public String getMerchantReferenceId() {
		return MerchantReferenceId;
	}
	public void setMerchantReferenceId(String merchantReferenceId) {
		this.MerchantReferenceId = merchantReferenceId;
	}
	public String getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(String responseCode) {
		this.ResponseCode = responseCode;
	}
	public String getResponseDescription() {
		return ResponseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.ResponseDescription = responseDescription;
	}

}