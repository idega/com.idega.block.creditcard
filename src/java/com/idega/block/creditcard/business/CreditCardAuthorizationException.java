/*
 * Created on 15.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.creditcard.business;

import java.util.logging.Logger;

import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.CoreUtil;

/**
 * @author gimmi
 *
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class CreditCardAuthorizationException extends Exception {

	private static final long serialVersionUID = -5439026878445387334L;

	protected String _errorMessage = null;
	protected String _errorNumber = null;
	protected String _displayError = null;

	/**
	 *
	 */
	public CreditCardAuthorizationException() {
		super();

		sendExceptionNotification();
	}

	/**
	 * @param message
	 */
	public CreditCardAuthorizationException(String message) {
		super(message);

		sendExceptionNotification();
	}

	public CreditCardAuthorizationException(String message, String errorNumber) {
		super(message);
		this._errorNumber = errorNumber;

		sendExceptionNotification();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CreditCardAuthorizationException(String message, Throwable cause) {
		super(message, cause);

		sendExceptionNotification();
	}

	/**
	 * @param cause
	 */
	public CreditCardAuthorizationException(Throwable cause) {
		super(cause);

		sendExceptionNotification();
	}

	public CreditCardAuthorizationException(Throwable cause, String errorMessage, String errorNumber) {
		super(cause);

		this._errorMessage = errorMessage;
		this._errorNumber = errorNumber;

		sendExceptionNotification();
	}

	private void sendExceptionNotification() {
		String message = "Message: " + _errorMessage + ", error number: " + _errorNumber + ", display error: " + _displayError;
		Logger.getLogger(getClass().getName()).warning(message);
		if (IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("credit_card.report_exceptions", false)) {
			CoreUtil.sendExceptionNotification(message, this);
		}
	}

	/**
	 *
	 */
	public void setErrorMessage(String message) {
		this._errorMessage = message;
	}

	/**
	 *
	 */
	public String getErrorMessage() {
		return (this._errorMessage);
	}

	/**
	 *
	 */
	public void setErrorNumber(String number) {
		this._errorNumber = number;
	}

	/**
	 *
	 */
	public String getErrorNumber() {
		return (this._errorNumber);
	}

	/**
	 *
	 */
	public void setDisplayError(String message) {
		this._displayError = message;
	}

	/**
	 *
	 */
	public String getDisplayError() {
		return (this._displayError);
	}

	public void setParentException(Exception e) {
		this.setStackTrace(e.getStackTrace());
	}

	public String getLocalizedMessage(IWResourceBundle iwrb) {
		if (iwrb != null && this._errorNumber != null && this._errorMessage != null) {
			return iwrb.getLocalizedString("CCERROR_" + this._errorNumber, this._errorMessage);
		}
		return this._errorMessage;
	}
}
