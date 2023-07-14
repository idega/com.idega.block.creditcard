package com.idega.block.creditcard.model.rapyd;

import java.util.logging.Logger;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.model.PaymentIntegrationResult;
import com.idega.block.creditcard.model.Status;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.CoreUtil;
import com.idega.util.StringUtil;

public class RapydException extends CreditCardAuthorizationException {

	private static final long serialVersionUID = 1629999009633570876L;

	private static final Logger LOGGER = Logger.getLogger(RapydException.class.getName());

	private String status;

	private String responseCode;

	private String errorCode;

	private String message;

	public RapydException(String message) {
		super(message);
	}

	public RapydException(Throwable cause, String message) {
		this(cause, message, null);
	}

	public RapydException(Throwable cause, String message, PaymentIntegrationResult result) {
		super(cause, message, result == null || result.getStatus() == null ? null : result.getStatus().getErrorCode());

		setValues(result == null ? null : result.getStatus());
	}

	public RapydException(String message, PaymentIntegrationResult result) {
		super(message);

		setValues(result == null ? null : result.getStatus());
	}

	public RapydException(String message, Data data) {
		super(message);

		if (data == null) {
			return;
		}

		String errorCode = data.getError_code();
		errorCode = StringUtil.isEmpty(errorCode) ? data.getFailure_code() : errorCode;
		this.errorCode = errorCode;

		String errorMessage = data.getFailure_message();
		if (!StringUtil.isEmpty(errorMessage)) {
			setErrorMessage(errorMessage);
		}
	}

	private void setValues(Status status) {
		if (status == null) {
			return;
		}

		this.status = status.getStatus();
		responseCode = status.getResponseCode();
		errorCode = status.getErrorCode();
		message = status.getMessage();
	}

	@Override
	public String getLocalizedMessage(IWResourceBundle iwrb) {
		String message = "Rapyd error. Message: " + getMessage() + ", error message: " + getErrorMessage() + ", error code: " + getErrorCode() + ", status: " + getStatus() +
				", response code: " + getResponseCode();
		LOGGER.warning(message);
		if (IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("credit_card.report_exceptions", false)) {
			CoreUtil.sendExceptionNotification(message, this);
		}

		String errorCode = getErrorCode();
		String errorMessage = getErrorMessage();
		if (StringUtil.isEmpty(errorCode)) {
			return StringUtil.isEmpty(errorMessage) ? message : errorMessage;
		}

		String localizedMessage = null;
		if (iwrb == null) {
			IWMainApplication iwma = IWMainApplication.getDefaultIWMainApplication();
			IWBundle bundle = iwma.getBundle(CreditCardConstants.IW_BUNDLE_IDENTIFIER);
			iwrb = bundle.getResourceBundle(CoreUtil.getCurrentLocale());
		}
		if (iwrb != null) {
			localizedMessage = iwrb.getLocalizedString(errorCode, errorMessage);
		}
		if (!StringUtil.isEmpty(localizedMessage)) {
			return localizedMessage;
		}

		return StringUtil.isEmpty(errorMessage) ? message : errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return StringUtil.isEmpty(message) ? super.getErrorMessage() : message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}