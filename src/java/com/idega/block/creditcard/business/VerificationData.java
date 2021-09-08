package com.idega.block.creditcard.business;

import java.io.Serializable;

public interface VerificationData extends Serializable {

	public String getCavv();

	public void setCavv(String cavv);

	public String getMdStatus();

	public void setMdStatus(String mdStatus);

	public String getXid();

	public void setXid(String xid);

	public String getDsTransId();

	public void setDsTransId(String dsTransId);

}
