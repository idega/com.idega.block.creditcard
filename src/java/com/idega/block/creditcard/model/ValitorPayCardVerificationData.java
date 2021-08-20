package com.idega.block.creditcard.model;

import java.io.Serializable;
import java.util.Base64;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class ValitorPayCardVerificationData extends ValitorPayMainRequestData implements Serializable {
	private static final long serialVersionUID = -546529386521469704L;

	private String authenticationUrl; 	//A HTTPS URL endpoint that is capable of handling the results of a card holder authentication POST request.
	private String virtualCard; 		//Virtual card number used for 3dSecure card verification. If supplied, then CardNumber and expiration date can be omitted.
										//Only virtual cards created as Cardholder initiated can be supplied for this action.
	private String agreementNumber;
	private String terminalId;
	private String md;					//MANDATORY: Unique UUID number to identify the transaction - payment uuid or virtual card uuid.
	private String exponent; //The minor units of currency specified in ISO 4217. For example, US Dollars has a value of 2; Japanese Yen has a value of 0. This can be skipped but then a default exponent will be used.
							 //When using ISK as the currency the exponent can be either 0 or 2. This is due to inconsistencies in the exponent expected by the issuer.
							 //If the exponent was incorrect, the error is not visible until the callback response is received in AuthenticationUrl.

	private String authenticationSuccessUrl;
	private String authenticationFailedUrl;

	private String cavv;
	private String mdStatus;
	private String xid;


	public ValitorPayCardVerificationData() {
		super();
	}

	public ValitorPayCardVerificationData(
			String cardNumber,
			String virtualCard,
			Integer amount,
			String expirationMonth,
			String expirationYear,
			String currency,
			String cardholderDeviceType,
			String authenticationUrl,
			String agreementNumber,
			String terminalId,
			String md,
			String exponent,
			String authenticationSuccessUrl,
			String authenticationFailedUrl
	) {
		this();

		this.virtualCard = virtualCard;
		this.authenticationUrl = authenticationUrl;
		this.agreementNumber = agreementNumber;
		this.terminalId = terminalId;
		this.md = md;
		this.exponent = exponent;
		this.authenticationSuccessUrl = authenticationSuccessUrl;
		this.authenticationFailedUrl = authenticationFailedUrl;

		setCardNumber(cardNumber);
		setAmount(amount);
		setCurrency(currency);
		setExpirationMonth(expirationMonth);
		setExpirationYear(expirationYear);
		setCardholderDeviceType(cardholderDeviceType);
	}



	public ValitorPayCardVerificationData(String cavv, String mdStatus, String xid) {
		super();
		this.cavv = cavv;
		this.mdStatus = mdStatus;
		this.xid = xid;

		setCardholderDeviceType(null);
		setCurrency(null);
	}

	public String getAuthenticationUrl() {
		return authenticationUrl;
	}

	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}

	public String getVirtualCard() {
		return virtualCard;
	}

	public void setVirtualCard(String virtualCard) {
		this.virtualCard = virtualCard;
	}

	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}



	public String getAuthenticationSuccessUrl() {
		return authenticationSuccessUrl;
	}

	public void setAuthenticationSuccessUrl(String authenticationSuccessUrl) {
		this.authenticationSuccessUrl = authenticationSuccessUrl;
	}

	public String getAuthenticationFailedUrl() {
		return authenticationFailedUrl;
	}

	public void setAuthenticationFailedUrl(String authenticationFailedUrl) {
		this.authenticationFailedUrl = authenticationFailedUrl;
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

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getDecodedMd() {
		if (!StringUtil.isEmpty(getMd())) {
			byte[] decodedMd = Base64.getDecoder().decode(getMd());
			if (decodedMd != null) {
				return new String(decodedMd);
			}
		}
		return md;
	}


	@Override
	public String toString() {
		return	"Name on card: " + getDisplayName() +
				", number: " + (StringUtil.isEmpty(getCardNumber()) ? getVirtualCard() : CreditCardUtil.getMaskedCreditCardNumber(getCardNumber())) +
				", expires (MM/YY): " + getExpirationMonth() + CoreConstants.SLASH + getExpirationYear();
	}

}