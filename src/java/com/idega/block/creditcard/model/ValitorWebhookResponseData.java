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

	private String md;
	private String xid;
	private String cavv;
	private String mdStatus;
	private String version;
	private String parEsVerified;
	private String parEsSyntaxOK;
	private String veresEnrolledStatus;
	private String paresTxStatus;
	private String protocol;
	private String cardType;
	private String merchantId;
	private String mdErrorMsg;
	private String iReqCode;
	private String iReqDetail;
	private String vendorCode;
	private String eci;
	private String cavvAlgorithm;
	private String txstatus;
	private String digest;
	private String signature;


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



	public String getMd() {
		return md;
	}
	public void setMd(String md) {
		this.md = md;
	}
	public String getXid() {
		return xid;
	}
	public void setXid(String xid) {
		this.xid = xid;
	}
	public String getCavv() {
		return cavv;
	}
	public void setCavv(String cavv) {
		this.cavv = cavv;
	}
	public String getMdStatus() {
		return mdStatus;
	}
	public void setMdStatus(String mdStatus) {
		this.mdStatus = mdStatus;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getParEsVerified() {
		return parEsVerified;
	}
	public void setParEsVerified(String parEsVerified) {
		this.parEsVerified = parEsVerified;
	}
	public String getParEsSyntaxOK() {
		return parEsSyntaxOK;
	}
	public void setParEsSyntaxOK(String parEsSyntaxOK) {
		this.parEsSyntaxOK = parEsSyntaxOK;
	}
	public String getVeresEnrolledStatus() {
		return veresEnrolledStatus;
	}
	public void setVeresEnrolledStatus(String veresEnrolledStatus) {
		this.veresEnrolledStatus = veresEnrolledStatus;
	}
	public String getParesTxStatus() {
		return paresTxStatus;
	}
	public void setParesTxStatus(String paresTxStatus) {
		this.paresTxStatus = paresTxStatus;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMdErrorMsg() {
		return mdErrorMsg;
	}
	public void setMdErrorMsg(String mdErrorMsg) {
		this.mdErrorMsg = mdErrorMsg;
	}
	public String getiReqCode() {
		return iReqCode;
	}
	public void setiReqCode(String iReqCode) {
		this.iReqCode = iReqCode;
	}
	public String getiReqDetail() {
		return iReqDetail;
	}
	public void setiReqDetail(String iReqDetail) {
		this.iReqDetail = iReqDetail;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getEci() {
		return eci;
	}
	public void setEci(String eci) {
		this.eci = eci;
	}
	public String getCavvAlgorithm() {
		return cavvAlgorithm;
	}
	public void setCavvAlgorithm(String cavvAlgorithm) {
		this.cavvAlgorithm = cavvAlgorithm;
	}
	public String getTxstatus() {
		return txstatus;
	}
	public void setTxstatus(String txstatus) {
		this.txstatus = txstatus;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
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