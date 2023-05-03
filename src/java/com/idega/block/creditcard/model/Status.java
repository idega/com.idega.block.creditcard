
package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Serializable {

    private static final long serialVersionUID = 1337966474049212465L;

    @SerializedName("error_code")
    @Expose
    private String errorCode;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("response_code")
    @Expose
    private String responseCode;

    @SerializedName("operation_id")
    @Expose
    private String operationId;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
	public String toString() {
    	return "Status: " + getStatus() + ", message: " + getMessage() + ", error code: " + getErrorCode();
    }

}