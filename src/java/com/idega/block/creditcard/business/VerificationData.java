package com.idega.block.creditcard.business;

import java.io.Serializable;

import com.idega.block.creditcard.model.FirstTransactionData;

public interface VerificationData extends Serializable {

	public String getCavv();

	public void setCavv(String cavv);

	public String getMdStatus();

	public void setMdStatus(String mdStatus);

	public String getXid();

	public void setXid(String xid);

	public String getDsTransId();

	public void setDsTransId(String dsTransId);

	public FirstTransactionData getFirstTransactionData();

}