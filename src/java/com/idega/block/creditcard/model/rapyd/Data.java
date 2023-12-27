package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

	private static final long serialVersionUID = 1731718877442207407L;

	private String id;

    private int amount;

    private int original_amount;

    private boolean is_partial;

    private String currency_code;

    private String country_code;

    private String status;

    private String description;

    private String merchant_reference_id;

    private String customer_token;

    private String payment_method;

    private PaymentMethodData payment_method_data;

    private String auth_code;

    private int expiration;

    private boolean captured;

    private boolean refunded;

    private int refunded_amount;

    private String receipt_email;

    private String redirect_url;

    private String complete_payment_url;

    private String error_payment_url;

    private String receipt_number;

    private String flow_type;

    private Object address;

    private String statement_descriptor;

    private String transaction_id;

    private int created_at;

    private Metadata metadata;

    private String failure_code;

    private String failure_message;

    private boolean paid;

    private int paid_at;

    private Object dispute;

    private Object refunds;

    private Object order;

    private Object outcome;

    private VisualCodes visual_codes;

    private TextualCodes textual_codes;

    private Object instructions;

    private String ewallet_id;

    private ArrayList<Ewallet> ewallets;

    private PaymentMethodOptions payment_method_options;

    private String payment_method_type;

    private String payment_method_type_category;

    private int fx_rate;

    private Object merchant_requested_currency;

    private Object merchant_requested_amount;

    private String fixed_side;

    private Object payment_fees;

    private String invoice;

    private Object escrow;

    private String group_payment;

    private Object cancel_reason;

    private String initiation_type;

    private String mid;

    private String next_action;

    private String error_code;

    private RemitterInformation remitter_information;

    private boolean save_payment_method;

    private AuthenticationResult authentication_result;

    private String payment;

    private String currency;

    private String failure_reason;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getOriginal_amount() {
		return original_amount;
	}

	public void setOriginal_amount(int original_amount) {
		this.original_amount = original_amount;
	}

	public boolean isIs_partial() {
		return is_partial;
	}

	public void setIs_partial(boolean is_partial) {
		this.is_partial = is_partial;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchant_reference_id() {
		return merchant_reference_id;
	}

	public void setMerchant_reference_id(String merchant_reference_id) {
		this.merchant_reference_id = merchant_reference_id;
	}

	public String getCustomer_token() {
		return customer_token;
	}

	public void setCustomer_token(String customer_token) {
		this.customer_token = customer_token;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public PaymentMethodData getPayment_method_data() {
		return payment_method_data;
	}

	public void setPayment_method_data(PaymentMethodData payment_method_data) {
		this.payment_method_data = payment_method_data;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public int getExpiration() {
		return expiration;
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	public boolean isCaptured() {
		return captured;
	}

	public void setCaptured(boolean captured) {
		this.captured = captured;
	}

	public boolean isRefunded() {
		return refunded;
	}

	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}

	public int getRefunded_amount() {
		return refunded_amount;
	}

	public void setRefunded_amount(int refunded_amount) {
		this.refunded_amount = refunded_amount;
	}

	public String getReceipt_email() {
		return receipt_email;
	}

	public void setReceipt_email(String receipt_email) {
		this.receipt_email = receipt_email;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getComplete_payment_url() {
		return complete_payment_url;
	}

	public void setComplete_payment_url(String complete_payment_url) {
		this.complete_payment_url = complete_payment_url;
	}

	public String getError_payment_url() {
		return error_payment_url;
	}

	public void setError_payment_url(String error_payment_url) {
		this.error_payment_url = error_payment_url;
	}

	public String getReceipt_number() {
		return receipt_number;
	}

	public void setReceipt_number(String receipt_number) {
		this.receipt_number = receipt_number;
	}

	public String getFlow_type() {
		return flow_type;
	}

	public void setFlow_type(String flow_type) {
		this.flow_type = flow_type;
	}

	public Object getAddress() {
		return address;
	}

	public void setAddress(Object address) {
		this.address = address;
	}

	public String getStatement_descriptor() {
		return statement_descriptor;
	}

	public void setStatement_descriptor(String statement_descriptor) {
		this.statement_descriptor = statement_descriptor;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getFailure_code() {
		return failure_code;
	}

	public void setFailure_code(String failure_code) {
		this.failure_code = failure_code;
	}

	public String getFailure_message() {
		return failure_message;
	}

	public void setFailure_message(String failure_message) {
		this.failure_message = failure_message;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public int getPaid_at() {
		return paid_at;
	}

	public void setPaid_at(int paid_at) {
		this.paid_at = paid_at;
	}

	public Object getDispute() {
		return dispute;
	}

	public void setDispute(Object dispute) {
		this.dispute = dispute;
	}

	public Object getRefunds() {
		return refunds;
	}

	public void setRefunds(Object refunds) {
		this.refunds = refunds;
	}

	public Object getOrder() {
		return order;
	}

	public void setOrder(Object order) {
		this.order = order;
	}

	public Object getOutcome() {
		return outcome;
	}

	public void setOutcome(Object outcome) {
		this.outcome = outcome;
	}

	public VisualCodes getVisual_codes() {
		return visual_codes;
	}

	public void setVisual_codes(VisualCodes visual_codes) {
		this.visual_codes = visual_codes;
	}

	public TextualCodes getTextual_codes() {
		return textual_codes;
	}

	public void setTextual_codes(TextualCodes textual_codes) {
		this.textual_codes = textual_codes;
	}

	public Object getInstructions() {
		return instructions;
	}

	public void setInstructions(Object instructions) {
		this.instructions = instructions;
	}

	public String getEwallet_id() {
		return ewallet_id;
	}

	public void setEwallet_id(String ewallet_id) {
		this.ewallet_id = ewallet_id;
	}

	public ArrayList<Ewallet> getEwallets() {
		return ewallets;
	}

	public void setEwallets(ArrayList<Ewallet> ewallets) {
		this.ewallets = ewallets;
	}

	public PaymentMethodOptions getPayment_method_options() {
		return payment_method_options;
	}

	public void setPayment_method_options(PaymentMethodOptions payment_method_options) {
		this.payment_method_options = payment_method_options;
	}

	public String getPayment_method_type() {
		return payment_method_type;
	}

	public void setPayment_method_type(String payment_method_type) {
		this.payment_method_type = payment_method_type;
	}

	public String getPayment_method_type_category() {
		return payment_method_type_category;
	}

	public void setPayment_method_type_category(String payment_method_type_category) {
		this.payment_method_type_category = payment_method_type_category;
	}

	public int getFx_rate() {
		return fx_rate;
	}

	public void setFx_rate(int fx_rate) {
		this.fx_rate = fx_rate;
	}

	public Object getMerchant_requested_currency() {
		return merchant_requested_currency;
	}

	public void setMerchant_requested_currency(Object merchant_requested_currency) {
		this.merchant_requested_currency = merchant_requested_currency;
	}

	public Object getMerchant_requested_amount() {
		return merchant_requested_amount;
	}

	public void setMerchant_requested_amount(Object merchant_requested_amount) {
		this.merchant_requested_amount = merchant_requested_amount;
	}

	public String getFixed_side() {
		return fixed_side;
	}

	public void setFixed_side(String fixed_side) {
		this.fixed_side = fixed_side;
	}

	public Object getPayment_fees() {
		return payment_fees;
	}

	public void setPayment_fees(Object payment_fees) {
		this.payment_fees = payment_fees;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Object getEscrow() {
		return escrow;
	}

	public void setEscrow(Object escrow) {
		this.escrow = escrow;
	}

	public String getGroup_payment() {
		return group_payment;
	}

	public void setGroup_payment(String group_payment) {
		this.group_payment = group_payment;
	}

	public Object getCancel_reason() {
		return cancel_reason;
	}

	public void setCancel_reason(Object cancel_reason) {
		this.cancel_reason = cancel_reason;
	}

	public String getInitiation_type() {
		return initiation_type;
	}

	public void setInitiation_type(String initiation_type) {
		this.initiation_type = initiation_type;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getNext_action() {
		return next_action;
	}

	public void setNext_action(String next_action) {
		this.next_action = next_action;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public RemitterInformation getRemitter_information() {
		return remitter_information;
	}

	public void setRemitter_information(RemitterInformation remitter_information) {
		this.remitter_information = remitter_information;
	}

	public boolean isSave_payment_method() {
		return save_payment_method;
	}

	public void setSave_payment_method(boolean save_payment_method) {
		this.save_payment_method = save_payment_method;
	}

	public AuthenticationResult getAuthentication_result() {
		return authentication_result;
	}

	public void setAuthentication_result(AuthenticationResult authentication_result) {
		this.authentication_result = authentication_result;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFailure_reason() {
		return failure_reason;
	}

	public void setFailure_reason(String failure_reason) {
		this.failure_reason = failure_reason;
	}

}