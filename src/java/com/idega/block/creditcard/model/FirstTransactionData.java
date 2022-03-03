package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.util.StringUtil;

public class FirstTransactionData implements Serializable {

	private static final long serialVersionUID = -4722490535548220222L;

	private String initiationReason;

	public FirstTransactionData() {
		super();

		this.initiationReason = CreditCardConstants.INITIATION_REASON_RECURRING;
	}

	public FirstTransactionData(String initiationReason) {
		this();

		if (!StringUtil.isEmpty(initiationReason)) {
			this.initiationReason = initiationReason;
		}
	}

	public String getInitiationReason() {
		return initiationReason;
	}

	public void setInitiationReason(String initiationReason) {
		this.initiationReason = initiationReason;
	}

	@Override
	public String toString() {
		return "Initiation reason: " + getInitiationReason();
	}

}