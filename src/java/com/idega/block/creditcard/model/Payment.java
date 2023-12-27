
package com.idega.block.creditcard.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idega.block.creditcard.model.rapyd.PaymentMethodData;
import com.idega.block.creditcard.model.rapyd.PaymentMethodOptions;
import com.idega.block.creditcard.model.rapyd.VisualCodes;

public class Payment implements Serializable {

    private static final long serialVersionUID = 6753271128526546282L;

    @SerializedName("id")
    @Expose
    private Object id;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("original_amount")
    @Expose
    private Integer originalAmount;

    @SerializedName("is_partial")
    @Expose
    private Boolean isPartial;

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("country_code")
    @Expose
    private String countryCode;

    @SerializedName("status")
    @Expose
    private Object status;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("merchant_reference_id")
    @Expose
    private String merchantReferenceId;

    @SerializedName("customer_token")
    @Expose
    private Object customerToken;

    @SerializedName("payment_method")
    @Expose
    private Object paymentMethod;

    @SerializedName("payment_method_data")
    @Expose
    private PaymentMethodData paymentMethodData;

    @SerializedName("expiration")
    @Expose
    private Integer expiration;

    @SerializedName("captured")
    @Expose
    private Boolean captured;

    @SerializedName("refunded")
    @Expose
    private Boolean refunded;

    @SerializedName("refunded_amount")
    @Expose
    private Integer refundedAmount;

    @SerializedName("receipt_email")
    @Expose
    private Object receiptEmail;

    @SerializedName("redirect_url")
    @Expose
    private Object redirectUrl;

    @SerializedName("complete_payment_url")
    @Expose
    private Object completePaymentUrl;

    @SerializedName("error_payment_url")
    @Expose
    private Object errorPaymentUrl;

    @SerializedName("receipt_number")
    @Expose
    private Object receiptNumber;

    @SerializedName("flow_type")
    @Expose
    private Object flowType;

    @SerializedName("address")
    @Expose
    private Object address;

    @SerializedName("statement_descriptor")
    @Expose
    private String statementDescriptor;

    @SerializedName("transaction_id")
    @Expose
    private Object transactionId;

    @SerializedName("created_at")
    @Expose
    private Integer createdAt;

    @SerializedName("updated_at")
    @Expose
    private Integer updatedAt;

    @SerializedName("metadata")
    @Expose
    private Object metadata;

    @SerializedName("failure_code")
    @Expose
    private Object failureCode;

    @SerializedName("failure_message")
    @Expose
    private Object failureMessage;

    @SerializedName("paid")
    @Expose
    private Boolean paid;

    @SerializedName("paid_at")
    @Expose
    private Integer paidAt;

    @SerializedName("dispute")
    @Expose
    private Object dispute;

    @SerializedName("refunds")
    @Expose
    private Object refunds;

    @SerializedName("order")
    @Expose
    private Object order;

    @SerializedName("outcome")
    @Expose
    private Object outcome;

    @SerializedName("visual_codes")
    @Expose
    private VisualCodes visualCodes;

    @SerializedName("textual_codes")
    @Expose
    private TextualCodes textualCodes;

    @SerializedName("instructions")
    @Expose
    private Instructions instructions;

    @SerializedName("ewallet_id")
    @Expose
    private Object ewalletId;

    @SerializedName("ewallets")
    @Expose
    private List<Object> ewallets;

    @SerializedName("payment_method_options")
    @Expose
    private PaymentMethodOptions paymentMethodOptions;

    @SerializedName("payment_method_type")
    @Expose
    private Object paymentMethodType;

    @SerializedName("payment_method_type_category")
    @Expose
    private Object paymentMethodTypeCategory;

    @SerializedName("fx_rate")
    @Expose
    private Object fxRate;

    @SerializedName("merchant_requested_currency")
    @Expose
    private Object merchantRequestedCurrency;

    @SerializedName("merchant_requested_amount")
    @Expose
    private Object merchantRequestedAmount;

    @SerializedName("fixed_side")
    @Expose
    private Object fixedSide;

    @SerializedName("payment_fees")
    @Expose
    private Object paymentFees;

    @SerializedName("invoice")
    @Expose
    private Object invoice;

    @SerializedName("escrow")
    @Expose
    private Object escrow;

    @SerializedName("group_payment")
    @Expose
    private Object groupPayment;

    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;

    @SerializedName("initiation_type")
    @Expose
    private String initiationType;

    @SerializedName("mid")
    @Expose
    private Object mid;

    @SerializedName("next_action")
    @Expose
    private String nextAction;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Integer originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Boolean getIsPartial() {
        return isPartial;
    }

    public void setIsPartial(Boolean isPartial) {
        this.isPartial = isPartial;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchantReferenceId() {
        return merchantReferenceId;
    }

    public void setMerchantReferenceId(String merchantReferenceId) {
        this.merchantReferenceId = merchantReferenceId;
    }

    public Object getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(Object customerToken) {
        this.customerToken = customerToken;
    }

    public Object getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Object paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodData getPaymentMethodData() {
        return paymentMethodData;
    }

    public void setPaymentMethodData(PaymentMethodData paymentMethodData) {
        this.paymentMethodData = paymentMethodData;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Boolean getCaptured() {
        return captured;
    }

    public void setCaptured(Boolean captured) {
        this.captured = captured;
    }

    public Boolean getRefunded() {
        return refunded;
    }

    public void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    public Integer getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(Integer refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Object getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(Object receiptEmail) {
        this.receiptEmail = receiptEmail;
    }

    public Object getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(Object redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Object getCompletePaymentUrl() {
        return completePaymentUrl;
    }

    public void setCompletePaymentUrl(Object completePaymentUrl) {
        this.completePaymentUrl = completePaymentUrl;
    }

    public Object getErrorPaymentUrl() {
        return errorPaymentUrl;
    }

    public void setErrorPaymentUrl(Object errorPaymentUrl) {
        this.errorPaymentUrl = errorPaymentUrl;
    }

    public Object getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(Object receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Object getFlowType() {
        return flowType;
    }

    public void setFlowType(Object flowType) {
        this.flowType = flowType;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getStatementDescriptor() {
        return statementDescriptor;
    }

    public void setStatementDescriptor(String statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

    public Object getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Object transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Object getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(Object failureCode) {
        this.failureCode = failureCode;
    }

    public Object getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(Object failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Integer getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Integer paidAt) {
        this.paidAt = paidAt;
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

    public VisualCodes getVisualCodes() {
        return visualCodes;
    }

    public void setVisualCodes(VisualCodes visualCodes) {
        this.visualCodes = visualCodes;
    }

    public TextualCodes getTextualCodes() {
        return textualCodes;
    }

    public void setTextualCodes(TextualCodes textualCodes) {
        this.textualCodes = textualCodes;
    }

    public Instructions getInstructions() {
        return instructions;
    }

    public void setInstructions(Instructions instructions) {
        this.instructions = instructions;
    }

    public Object getEwalletId() {
        return ewalletId;
    }

    public void setEwalletId(Object ewalletId) {
        this.ewalletId = ewalletId;
    }

    public List<Object> getEwallets() {
        return ewallets;
    }

    public void setEwallets(List<Object> ewallets) {
        this.ewallets = ewallets;
    }

    public PaymentMethodOptions getPaymentMethodOptions() {
        return paymentMethodOptions;
    }

    public void setPaymentMethodOptions(PaymentMethodOptions paymentMethodOptions) {
        this.paymentMethodOptions = paymentMethodOptions;
    }

    public Object getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(Object paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public Object getPaymentMethodTypeCategory() {
        return paymentMethodTypeCategory;
    }

    public void setPaymentMethodTypeCategory(Object paymentMethodTypeCategory) {
        this.paymentMethodTypeCategory = paymentMethodTypeCategory;
    }

    public Object getFxRate() {
        return fxRate;
    }

    public void setFxRate(Object fxRate) {
        this.fxRate = fxRate;
    }

    public Object getMerchantRequestedCurrency() {
        return merchantRequestedCurrency;
    }

    public void setMerchantRequestedCurrency(Object merchantRequestedCurrency) {
        this.merchantRequestedCurrency = merchantRequestedCurrency;
    }

    public Object getMerchantRequestedAmount() {
        return merchantRequestedAmount;
    }

    public void setMerchantRequestedAmount(Object merchantRequestedAmount) {
        this.merchantRequestedAmount = merchantRequestedAmount;
    }

    public Object getFixedSide() {
        return fixedSide;
    }

    public void setFixedSide(Object fixedSide) {
        this.fixedSide = fixedSide;
    }

    public Object getPaymentFees() {
        return paymentFees;
    }

    public void setPaymentFees(Object paymentFees) {
        this.paymentFees = paymentFees;
    }

    public Object getInvoice() {
        return invoice;
    }

    public void setInvoice(Object invoice) {
        this.invoice = invoice;
    }

    public Object getEscrow() {
        return escrow;
    }

    public void setEscrow(Object escrow) {
        this.escrow = escrow;
    }

    public Object getGroupPayment() {
        return groupPayment;
    }

    public void setGroupPayment(Object groupPayment) {
        this.groupPayment = groupPayment;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getInitiationType() {
        return initiationType;
    }

    public void setInitiationType(String initiationType) {
        this.initiationType = initiationType;
    }

    public Object getMid() {
        return mid;
    }

    public void setMid(Object mid) {
        this.mid = mid;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    @Override
	public String toString() {
    	return "Payment ID: " + getId() + ", amount: " + getAmount() + ", status: " + getStatus() + ", merchant reference ID: " + getMerchantReferenceId();
    }

}