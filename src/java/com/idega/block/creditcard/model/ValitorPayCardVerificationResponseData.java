package com.idega.block.creditcard.model;

import com.idega.block.creditcard.business.VerificationData;

public class ValitorPayCardVerificationResponseData implements VerificationData {
	private static final long serialVersionUID = -4722490535548205562L;

	private String cavv;
	private String mdStatus;
	private String xid;
	private String dsTransId;


	public ValitorPayCardVerificationResponseData() {
		super();
	}

	public ValitorPayCardVerificationResponseData(
			String cavv,
			String mdStatus,
			String xid,
			String dsTransId
	) {
		this();

		this.cavv = cavv;
		this.mdStatus = mdStatus;
		this.xid = xid;
		this.dsTransId = dsTransId;
	}


	@Override
	public String getCavv() {
		return cavv;
	}

	@Override
	public void setCavv(String cavv) {
		this.cavv = cavv;
	}

	@Override
	public String getMdStatus() {
		return mdStatus;
	}

	@Override
	public void setMdStatus(String mdStatus) {
		this.mdStatus = mdStatus;
	}

	@Override
	public String getXid() {
		return xid;
	}

	@Override
	public void setXid(String xid) {
		this.xid = xid;
	}



	public String getDsTransId() {
		return dsTransId;
	}

	public void setDsTransId(String dsTransId) {
		this.dsTransId = dsTransId;
	}

	@Override
	public String toString() {
		return	"cavv: " + getCavv() +
				", mdStatus: " + getMdStatus() +
				". xid: " + getXid() +
				". dsTransId: " + getDsTransId();
	}

}