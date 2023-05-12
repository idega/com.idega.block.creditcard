package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.util.StringUtil;

public class HostedCheckoutPageRequest implements Serializable {

	private static final long serialVersionUID = -8127517372991772982L;

	//	The amount of the payment, in units of the currency defined in currency. Required if cart_items is not used. Decimal.
	private Integer amount;

	//	URL where the customer is redirected after pressing Back to Website to exit the hosted page.
	private String cancel_checkout_url;

	//	URL where the customer is redirected after pressing Finish to exit the hosted page.
	private String complete_checkout_url;

	//	URL where the customer is redirected when payment is successful. Relevant to bank redirect payment methods.
	private String complete_payment_url;

	//	Determines when the payment is processed for capture. When true, the payment is captured immediately.
	//	When false, the payment is captured at a later time. Relevant to cards. Default is true.
	private Boolean capture = Boolean.TRUE;

	//	The two-letter ISO 3166-1 ALPHA-2 code for the country.
	private String country;

	//	Defines the currency for the amount. Three-letter ISO 4217 code
	private String currency;

	private String requested_currency;

	//	ID of the 'customer' object. String starting with cus_. When used, the customer has the option to save card details for future purchases.
	//	This field is required for some payment methods and for all 'company' type wallets.
	private String customer;

	//	Description of the payment. To display the description, set display_description to true in custom_elements
	private String description;

	//	URL where the customer is redirected when payment is not successful. Relevant to bank redirect payment methods.
	private String error_payment_url;

	//	Determines the default language of the hosted page. The values are documented in https://docs.rapyd.net/build-with-rapyd/reference/hosted-page-language-support
	private String language;

	//	Identifier for the transaction. Defined by the merchant.
	private String merchant_reference_id;

	//	A text description that appears in the customer's bank statement.
	private String statement_descriptor;

	public HostedCheckoutPageRequest() {
		super();
	}

	public HostedCheckoutPageRequest(
			Integer amount,
			String country,
			String currency,
			String statementDescriptor,
			String merchantReferenceId,
			String completeCheckoutUrl,
			String cancelCheckoutUrl
	) {
		this();

		this.amount = amount;
		this.country = country;
		this.currency = currency;
		this.requested_currency = currency;
		if (!StringUtil.isEmpty(statementDescriptor)) {
			if (statementDescriptor.length() > 22) {
				statementDescriptor = statementDescriptor.substring(0, 22);
			}
			this.statement_descriptor = statementDescriptor;
		}
		this.merchant_reference_id = merchantReferenceId;
		this.complete_checkout_url = completeCheckoutUrl;
		this.cancel_checkout_url = cancelCheckoutUrl;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCancel_checkout_url() {
		return cancel_checkout_url;
	}

	public void setCancel_checkout_url(String cancel_checkout_url) {
		this.cancel_checkout_url = cancel_checkout_url;
	}

	public String getComplete_checkout_url() {
		return complete_checkout_url;
	}

	public void setComplete_checkout_url(String complete_checkout_url) {
		this.complete_checkout_url = complete_checkout_url;
	}

	public String getComplete_payment_url() {
		return complete_payment_url;
	}

	public void setComplete_payment_url(String complete_payment_url) {
		this.complete_payment_url = complete_payment_url;
	}

	public Boolean getCapture() {
		return capture;
	}

	public void setCapture(Boolean capture) {
		this.capture = capture;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getError_payment_url() {
		return error_payment_url;
	}

	public void setError_payment_url(String error_payment_url) {
		this.error_payment_url = error_payment_url;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getStatement_descriptor() {
		return statement_descriptor;
	}

	public void setStatement_descriptor(String statement_descriptor) {
		this.statement_descriptor = statement_descriptor;
	}

	public String getMerchant_reference_id() {
		return merchant_reference_id;
	}

	public void setMerchant_reference_id(String merchant_reference_id) {
		this.merchant_reference_id = merchant_reference_id;
	}

	public String getRequested_currency() {
		return requested_currency;
	}

	public void setRequested_currency(String requested_currency) {
		this.requested_currency = requested_currency;
	}

	@Override
	public String toString() {
		return getStatement_descriptor() + ": " + getAmount() + " " + getCurrency() + ". ID: " + getMerchant_reference_id();
	}

}