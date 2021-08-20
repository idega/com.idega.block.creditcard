/*
 * $Id: TPosException.java,v 1.2 2006/06/20 11:01:19 gimmi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.business;

import java.util.logging.Logger;

import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.StringUtil;

public class ValitorPayException extends CreditCardAuthorizationException {
	private static final long serialVersionUID = -1727056429379653439L;

	public ValitorPayException() {
	    super();
	}

	public ValitorPayException(String message) {
	    super(message);
	}

    public ValitorPayException(String message, Throwable cause) {
	  	super(message, cause);
	}

	public ValitorPayException(Throwable cause) {
	  	super(cause);
	}

	public ValitorPayException(String message, String errorNumber) {
		super(message, errorNumber);
	}

	@Override
	public String getLocalizedMessage(IWResourceBundle iwrb) {
			String message = "ValitorPay errormessage = " + this.getErrorMessage() + ", error number = " + this.getErrorNumber() + ", display error = " + this.getDisplayError();
			Logger.getLogger(getClass().getName()).warning(message);
			if (IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("credit_card.report_exceptions", false)) {
				CoreUtil.sendExceptionNotification(message, this);
			}

			String errorMessageTmp = CoreConstants.EMPTY;
			if (!StringUtil.isEmpty(this.getErrorMessage())) {
				errorMessageTmp = this.getErrorMessage();
			} else if (!StringUtil.isEmpty(this.getDisplayError())) {
				errorMessageTmp = this.getDisplayError();
			}

			if (!StringUtil.isEmpty(this.getErrorNumber())) {
				//Take into account only 2 first error number letter/numbers
				String errorNumberFinal = this.getErrorNumber().substring(0, 2);
				Logger.getLogger(getClass().getName()).info("Checking the ValitorPay errors by 2 first error number letters: " + errorNumberFinal);

				switch (errorNumberFinal) {
					case "VERIFICATION_DATA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.VERIFICATION_DATA", "Problems occured while crrating verification data."));
					case "DATA_NOT_PROVIDED":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DATA_NOT_PROVIDED","Some of the mandatory data is not provided"));
					case "APP_SETTINGS":
						return (iwrb.getLocalizedString("valitor_pay.error_code.APP_SETTINGS","Can not get the application settings"));
					case "PAYMENT_DATA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.PAYMENT_DATA","ValitorPay payment failed"));
					case "RESPONSE_ERROR":
						return (iwrb.getLocalizedString("valitor_pay.error_code.RESPONSE_ERROR","ValitorPay response was empty"));
					case "UNKNOWN_RESPONSE_ERROR":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UNKNOWN_RESPONSE_ERROR","ValitorPay - unknown error"));
					case "BAD_REQUEST_RESPONSE_ERROR":
						return (iwrb.getLocalizedString("valitor_pay.error_code.BAD_REQUEST_RESPONSE_ERROR","ValitorPay response was - Bad request"));
					case "INTERNAL_SERVER_ERROR_RESPONSE_ERROR":
						return (iwrb.getLocalizedString("valitor_pay.error_code.INTERNAL_SERVER_ERROR_RESPONSE_ERROR","ValitorPay response was - Internal server error"));
					case "VIRTUAL_CARD_UPDATE_DATA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.VIRTUAL_CARD_UPDATE_DATA","Could not construct the virtual card data for update."));
					case "UPDATE_CARD_EXPIRATION_DATE_FAILED":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UPDATE_CARD_EXPIRATION_DATE_FAILED","Could not update the card expiration date."));
					case "CREATE_VIRTUAL_CARD_FAILED":
						return (iwrb.getLocalizedString("valitor_pay.error_code.CREATE_VIRTUAL_CARD_FAILED","Could not create a virtual card."));

					case "A0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A0", "User cannot be authenticated or action was not authorized."));
					case "A1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A1", "Invalid Card acceptor identification code (MerchantXID)."));
					case "A2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A2", "Invalid Category code."));
					case "A3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A3", "Invalid Terminal ID or Device ID."));
					case "A4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A4", "Invalid message ID."));
					case "A5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A5", "Invalid card number."));
					case "A6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A6", "Invalid card expiration date."));
					case "A7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A7", "Invalid message UTC."));
					case "A8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A8", "Invalid Currency (TransCurrency or OriginalTransCurrency value is invalid or field is not allowed for TransType)."));
					case "A9":
						return (iwrb.getLocalizedString("valitor_pay.error_code.A9", "Invalid Amount (TransAmount or OriginalTransAmount value is invalid or field is not allowed for TransType. TransFee invalid or not allowed for CategoryCode)."));
					case "AA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AA", "Invalid Message type."));
					case "AB":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AB", "Invalid Transaction type."));
					case "AC":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AC", "Invalid Card type."));
					case "AD":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AD", "Invalid 3DSecure data (e.g. CAVD missing or wrong length)."));
					case "AE":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AE", "Transaction rejected - wrong CardVD value (CVC2/CVV2)."));
					case "AF":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AF", "Invalid Card verification data."));
					case "AG":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AG", "Invalid address verification data (AVSPostalCode or AVSStreetAddress)."));
					case "AH":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AH", "Invalid Cashback amount."));
					case "AI":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AI", "Invalid Additional Data."));
					case "AJ":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AJ", "Invalid Card (wrong issuer BIN or inactive BIN range)."));
					case "AK":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AK", "Invalid Transaction date."));
					case "AL":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AL", "Invalid Card sequence number."));
					case "AM":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AM", "Invalid Terminal capability/type."));
					case "AN":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AN", "Invalid Transaction time."));
					case "AO":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AO", "Invalid Transaction xid."));
					case "AP":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AP", "Invalid Authorization code."));
					case "AQ":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AQ", "Invalid Track 2."));
					case "AR":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AR", "Invalid 3DSecure electronic commerce fields."));
					case "AS":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AS", "Invalid Icc data."));
					case "AV":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AV", "Connection to Visa EU/Mastercard is temporarily unavailable."));
					case "AW":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AW", "Time-out (authorization host received no response from Visa/Mastercard)."));
					case "AY":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AY", "Unexpected error."));
					case "AZ":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AZ", "Invalid Character(s) in Input Data (not covered by any of the codes above). Can apply to MessageCode, TransactionId, LifeCycleId, TransitIndicator, MerchantVerificationValue or Mastercard Wallet Identifier."));
					case "AX":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AX", "Blocked by firewall."));
					case "T0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T0", "Unable to locate previous message."));
					case "T1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T1", "Previous message located, but the reversal data are inconsistent with original message."));
					case "T2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T2", "No action taken (authorization already finalized)."));
					case "T3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T3", "Duplicate transaction."));
					case "T4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T4", "Unable to confirm online pin."));
					case "T5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T5", "Initiation reason missing or invalid."));
					case "T6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T6", "Initiator missing or invalid."));
					case "T7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T7", "Dynamic Currency Conversion (DCC) not allowed on card."));
					case "T8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T8", "SCA exemption failed validation or exemption not allowed on transtype."));
					case "T9":
						return (iwrb.getLocalizedString("valitor_pay.error_code.T9", "TransStatus not allowed for transaction."));
					case "TA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.TA", "Unsupported BIN."));
					case "TB":
						return (iwrb.getLocalizedString("valitor_pay.error_code.TB", "Acquirer declined SCA exemption."));
					case "TC":
						return (iwrb.getLocalizedString("valitor_pay.error_code.TC", "Transaction being reversed was declined by the acquirer. No need to reverse."));
					case "M0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M0", "Merchant ID not found."));
					case "M1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M1", "Merchant account closed."));
					case "M2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M2", "Terminal ID not found."));
					case "M3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M3", "Terminal closed."));
					case "M4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M4", "Invalid category code."));
					case "M5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M5", "Invalid currency."));
					case "M6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M6", "Missing CVV2/CVC2."));
					case "M7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M7", "CVV2 not allowed."));
					case "M8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M8", "Merchant not registered for Verified by Visa/Secure Code."));
					case "M9":
						return (iwrb.getLocalizedString("valitor_pay.error_code.M9", "Merchant not registered for Amex."));
					case "MA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.MA", "Transaction not permitted at terminal."));
					case "MB":
						return (iwrb.getLocalizedString("valitor_pay.error_code.MB", "Agreement and terminal are not related."));
					case "MC":
						return (iwrb.getLocalizedString("valitor_pay.error_code.MC", "Invalid processor ID."));
					case "MD":
						return (iwrb.getLocalizedString("valitor_pay.error_code.MD", "Invalid merchant data (Name, City or Postal Code)."));
					case "ME":
						return (iwrb.getLocalizedString("valitor_pay.error_code.ME", "Sub-merchant account closed."));
					case "00":
						return (iwrb.getLocalizedString("valitor_pay.error_code.00", "Approval."));
					case "01":
						return (iwrb.getLocalizedString("valitor_pay.error_code.01", "Refer to card issuer."));
					case "02":
						return (iwrb.getLocalizedString("valitor_pay.error_code.02", "Refer to card issuer, special condition."));
					case "03":
						return (iwrb.getLocalizedString("valitor_pay.error_code.03", "Invalid merchant or service provider."));
					case "04":
						return (iwrb.getLocalizedString("valitor_pay.error_code.04", "Pickup card."));
					case "05":
						return (iwrb.getLocalizedString("valitor_pay.error_code.05", "Do not honor."));
					case "06":
						return (iwrb.getLocalizedString("valitor_pay.error_code.06", "Error."));
					case "07":
						return (iwrb.getLocalizedString("valitor_pay.error_code.07", "Pick up card, special condition (other than lost/stolen card)."));
					case "10":
						return (iwrb.getLocalizedString("valitor_pay.error_code.10", "Partial Approval."));
					case "11":
						return (iwrb.getLocalizedString("valitor_pay.error_code.11", "V.I.P approval."));
					case "12":
						return (iwrb.getLocalizedString("valitor_pay.error_code.12", "Invalid transaction."));
					case "13":
						return (iwrb.getLocalizedString("valitor_pay.error_code.13", "Invalid amount (currency conversion field overflow) or amount exceeds maximum for card program."));
					case "14":
						return (iwrb.getLocalizedString("valitor_pay.error_code.14", "Invalid account number (no such number)."));
					case "15":
						return (iwrb.getLocalizedString("valitor_pay.error_code.15", "No such issuer."));
					case "19":
						return (iwrb.getLocalizedString("valitor_pay.error_code.19", "Re-enter transaction."));
					case "21":
						return (iwrb.getLocalizedString("valitor_pay.error_code.21", "No action taken (unable to back out prior transaction)."));
					case "25":
						return (iwrb.getLocalizedString("valitor_pay.error_code.25", "Unable to locate record in file, or account number is missing from the inquiry."));
					case "28":
						return (iwrb.getLocalizedString("valitor_pay.error_code.28", "File is temporarily unavailable."));
					case "39":
						return (iwrb.getLocalizedString("valitor_pay.error_code.39", "No credit account."));
					case "41":
						return (iwrb.getLocalizedString("valitor_pay.error_code.41", "Pickup card (lost card)."));
					case "43":
						return (iwrb.getLocalizedString("valitor_pay.error_code.43", "Pickup card (stolen card)."));
					case "46":
						return (iwrb.getLocalizedString("valitor_pay.error_code.46", "Closed account."));
					case "51":
						return (iwrb.getLocalizedString("valitor_pay.error_code.51", "Insufficient funds."));
					case "52":
						return (iwrb.getLocalizedString("valitor_pay.error_code.52", "No checking account."));
					case "53":
						return (iwrb.getLocalizedString("valitor_pay.error_code.53", "No savings account."));
					case "54":
						return (iwrb.getLocalizedString("valitor_pay.error_code.54", "Expired card."));
					case "55":
						return (iwrb.getLocalizedString("valitor_pay.error_code.55", "Incorrect PIN."));
					case "57":
						return (iwrb.getLocalizedString("valitor_pay.error_code.57", "Transaction not permitted to cardholder."));
					case "58":
						return (iwrb.getLocalizedString("valitor_pay.error_code.58", "Transaction not allowed at terminal."));
					case "59":
						return (iwrb.getLocalizedString("valitor_pay.error_code.59", "Suspected fraud."));
					case "61":
						return (iwrb.getLocalizedString("valitor_pay.error_code.61", "Activity amount limit exceeded."));
					case "62":
						return (iwrb.getLocalizedString("valitor_pay.error_code.62", "Restricted card (e.g. in Country Exclusion table)."));
					case "63":
						return (iwrb.getLocalizedString("valitor_pay.error_code.63", "Security violation."));
					case "64":
						return (iwrb.getLocalizedString("valitor_pay.error_code.64", "Transaction does not fulfill AML requirement."));
					case "65":
						return (iwrb.getLocalizedString("valitor_pay.error_code.65", "Activity count limit exceeded."));
					case "70":
						return (iwrb.getLocalizedString("valitor_pay.error_code.70", "PIN Data Required."));
					case "75":
						return (iwrb.getLocalizedString("valitor_pay.error_code.75", "Allowable number of PIN-entry tries exceeded."));
					case "76":
						return (iwrb.getLocalizedString("valitor_pay.error_code.76", "Unable to locate previous message (no match on transaction ID)."));
					case "77":
						return (iwrb.getLocalizedString("valitor_pay.error_code.77", "Previous message located, but the reversal data are inconsistent with original message."));
					case "78":
						return (iwrb.getLocalizedString("valitor_pay.error_code.78", "Blocked, first used - Transaction from new cardholder, and card not properly unblocked."));
					case "79":
						return (iwrb.getLocalizedString("valitor_pay.error_code.79", "Transaction reversed."));
					case "80":
						return (iwrb.getLocalizedString("valitor_pay.error_code.80", "Visa transactions: credit issuer unavailable. Private label: invalid date."));
					case "81":
						return (iwrb.getLocalizedString("valitor_pay.error_code.81", "PIN cryptographic error found (error found by VIC security module during PIN decryption)."));
					case "82":
						return (iwrb.getLocalizedString("valitor_pay.error_code.82", "Negative Online CAM, dCVV, iCVV, or CVV results or offline PIN authentication interrupted."));
					case "85":
						return (iwrb.getLocalizedString("valitor_pay.error_code.85", "No reason to decline a request for Account Number verification, address verification or CVV2 verification."));
					case "86":
						return (iwrb.getLocalizedString("valitor_pay.error_code.86", "Cannot Verify PIN."));
					case "91":
						return (iwrb.getLocalizedString("valitor_pay.error_code.91", "Issuer unavailable or switch inoperative (STIP not applicable or available for this transaction)."));
					case "92":
						return (iwrb.getLocalizedString("valitor_pay.error_code.92", "Financial institution or intermediate network facility cannot be found for routing."));
					case "93":
						return (iwrb.getLocalizedString("valitor_pay.error_code.93", "Transaction cannot be completed; violation of law."));
					case "94":
						return (iwrb.getLocalizedString("valitor_pay.error_code.94", "Duplicate transaction."));
					case "96":
						return (iwrb.getLocalizedString("valitor_pay.error_code.96", "System malfunction."));
					case "N0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.N0", "Force STIP."));
					case "N3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.N3", "Cash service not available."));
					case "N4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.N4", "Cashback request exceeds Issuer limit."));
					case "N7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.N7", "Decline for CVV2 failure."));
					case "N8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.N8", "Transaction amount exceeds pre-authorized approval amount."));
					case "P2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.P2", "Invalid biller information."));
					case "P5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.P5", "PIN change/unblock request declined."));
					case "P6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.P6", "Unsafe PIN."));
					case "R0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.R0", "Stop Payment Order."));
					case "R1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.R1", "Revocation of Authorization."));
					case "R3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.R3", "Revocation of All Authorizations."));
					case "Z3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Z3", "Unable to go online."));
					case "XA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.XA", "Forward to issuer."));
					case "XD":
						return (iwrb.getLocalizedString("valitor_pay.error_code.XD", "Forward to issuer."));
					case "Q1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Q1", "Card authentication failed. Or offline PIN authentication interrupted."));
					case "1A":
						return (iwrb.getLocalizedString("valitor_pay.error_code.1A", "Additional customer authentication required."));
					case "AT":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AT", "Acquirer timed-out."));
					case "AU":
						return (iwrb.getLocalizedString("valitor_pay.error_code.AU", "Acquirer unavailable."));
					case "B1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.B1", "Success fetching and processing virtual card data."));
					case "B2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.B2", "Unable to fetch virtual card data."));
					case "B3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.B3", "Unexpected error when fetching virtual card data."));
					case "B4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.B4", "Initiator or initiator reason do not match with the virtual card found."));
					case "P1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.P1", "Merchant initiated transactions or virtual cards, can not be authorized with 3dSecure data."));
					case "PN":
						return (iwrb.getLocalizedString("valitor_pay.error_code.PN", "Payout is not allowed to merchant."));
					case "PP":
						return (iwrb.getLocalizedString("valitor_pay.error_code.PP", "Processing communication error."));
					case "Q5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Q1", "Origin host name could not be retrieved or it's not allowed."));
					case "Q2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Q2", "Payment can not be completed because call to payment system failed."));
					case "Q3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Q3", "Sending of success payment notification failed for the provided webhook endpoint."));
					case "Q4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.Q4", "Cardholder was not successfully 3DSecure authenticated."));
					case "S2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.S2", "Merchant data was not found."));
					case "S3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.S3", "Unexpected error occured when fetching merchant data."));
					case "UB":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UB", "It is not possible to get updated account for this bin at this time."));
					case "UD":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UD", "Account was found but insufficient card data was provided."));
					case "UI":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UI", "Internal Service Error while updating account."));
					case "UM":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UM", "Transaction ID and Transaction Date must be supplied for Valitor issued cards."));
					case "UN":
						return (iwrb.getLocalizedString("valitor_pay.error_code.UN", "No updated account was found for the given card number."));
					case "V1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.V1", "Success creating merchant virtual card."));
					case "V2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.V2", "Unexpected error, please contact support."));
					case "V3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.V3", "Card was not fully authenticated and therefore virtual card can not be created."));
					case "X1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X1", "DCC approved."));
					case "X2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X2", "DCC Transaction not allowed to Cardholder."));
					case "X3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X3", "DCC Cardholder currency not supported."));
					case "X4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X4", "DCC Exceeds time limit for withdrawal (too late)."));
					case "X5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X5", "DCC Transaction not allowed to terminal equipment."));
					case "X6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X6", "DCC not allowed to merchant."));
					case "X7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.X7", "DCC Unknown error."));
					case "D1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D1", "Success response code."));
					case "D2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D2", "Batch report is not yet available for this batch ID. Please try again later."));
					case "D3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D3", "The batch with this batch ID has been deleted."));
					case "D4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D4", "The batch with this batch ID has been closed."));
					case "D5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D5", "The batch with this batch ID is open, the batch report has not been created yet."));
					case "D6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D6", "The batch with this batch ID is beeing processed, the batch report has not been created yet."));
					case "D7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D7", "The batch with this batch ID has expired."));
					case "D8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D8", "The batch file with this batch ID has been received."));
					case "D9":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D9", "The batch file with this batch ID has been parsed successfully."));
					case "D0":
						return (iwrb.getLocalizedString("valitor_pay.error_code.D0", "The batch file with this batch ID has been successfully validated."));
					case "DA":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DA", "The batch file with this batch ID has been successfully saved."));
					case "DB":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DB", "The batch file with this batch ID has been sent."));
					case "DC":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DC", "The batch file with this batch ID had parsing errors."));
					case "DD":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DD", "The batch file with this batch ID had validation errors."));
					case "DE":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DE", "There was an error sending the batch file with this batch."));
					case "DF":
						return (iwrb.getLocalizedString("valitor_pay.error_code.DF", "There was an error saving the batch file with this batch ID."));
					case "C1":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C1", "3DSecure verification was a success."));
					case "C2":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C2", "3DSecure verification failed, merchant is not enrolled in 3DSecure."));
					case "C3":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C3", "3DSecure verification failed, unable to validate card."));
					case "C4":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C4", "3DSecure verification failed, could not determine card type."));
					case "C5":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C5", "There was a problem receiving data from 3dSecure MPI, please try again later."));
					case "C6":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C6", "Validation error from MPI."));
					case "C7":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C7", "There was an error in response from MPI."));
					case "C8":
						return (iwrb.getLocalizedString("valitor_pay.error_code.C8", "The card does not support 3DSecure."));

					default:
						Logger.getLogger(getClass().getName()).info("Error number is not defined, showing the error message: " + errorMessageTmp);
						return errorMessageTmp;
				}

			} else {
				Logger.getLogger(getClass().getName()).info("Error number is not defined, showing the error message: " + errorMessageTmp);
				return errorMessageTmp;
			}
	  }
}
