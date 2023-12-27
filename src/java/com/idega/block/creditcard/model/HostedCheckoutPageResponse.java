package com.idega.block.creditcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HostedCheckoutPageResponse implements PaymentIntegrationResult {

	private static final long serialVersionUID = 4173544311737879852L;

	@SerializedName("status")
    @Expose
    private Status status;

    @SerializedName("data")
    @Expose
    private Data data;

    @Override
	public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
	public String toString() {
    	return "Status: " + getStatus() + ", data: " + getData();
    }

}