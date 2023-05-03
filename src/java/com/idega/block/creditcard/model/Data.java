
package com.idega.block.creditcard.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

    private static final long serialVersionUID = 4550968338668096557L;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("language")
    @Expose
    private Object language;

    @SerializedName("org_id")
    @Expose
    private String orgId;

    @SerializedName("merchant_color")
    @Expose
    private Object merchantColor;

    @SerializedName("merchant_logo")
    @Expose
    private Object merchantLogo;

    @SerializedName("merchant_website")
    @Expose
    private Object merchantWebsite;

    @SerializedName("merchant_customer_support")
    @Expose
    private MerchantCustomerSupport merchantCustomerSupport;

    @SerializedName("merchant_alias")
    @Expose
    private String merchantAlias;

    @SerializedName("merchant_terms")
    @Expose
    private Object merchantTerms;

    @SerializedName("merchant_privacy_policy")
    @Expose
    private Object merchantPrivacyPolicy;

    @SerializedName("page_expiration")
    @Expose
    private Integer pageExpiration;

    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;

    @SerializedName("merchant_main_button")
    @Expose
    private String merchantMainButton;

    @SerializedName("cancel_checkout_url")
    @Expose
    private Object cancelCheckoutUrl;

    @SerializedName("complete_checkout_url")
    @Expose
    private Object completeCheckoutUrl;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("payment")
    @Expose
    private Payment payment;

    @SerializedName("payment_method_type")
    @Expose
    private Object paymentMethodType;

    @SerializedName("payment_method_type_categories")
    @Expose
    private Object paymentMethodTypeCategories;

    @SerializedName("payment_method_types_include")
    @Expose
    private Object paymentMethodTypesInclude;

    @SerializedName("payment_method_types_exclude")
    @Expose
    private Object paymentMethodTypesExclude;

    @SerializedName("account_funding_transaction")
    @Expose
    private Object accountFundingTransaction;

    @SerializedName("customer")
    @Expose
    private Object customer;

    @SerializedName("custom_elements")
    @Expose
    private CustomElements customElements;

    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;

    @SerializedName("payment_expiration")
    @Expose
    private Object paymentExpiration;

    @SerializedName("cart_items")
    @Expose
    private List<Object> cartItems;

    @SerializedName("escrow")
    @Expose
    private Object escrow;

    @SerializedName("escrow_release_days")
    @Expose
    private Object escrowReleaseDays;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Object getMerchantColor() {
        return merchantColor;
    }

    public void setMerchantColor(Object merchantColor) {
        this.merchantColor = merchantColor;
    }

    public Object getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(Object merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public Object getMerchantWebsite() {
        return merchantWebsite;
    }

    public void setMerchantWebsite(Object merchantWebsite) {
        this.merchantWebsite = merchantWebsite;
    }

    public MerchantCustomerSupport getMerchantCustomerSupport() {
        return merchantCustomerSupport;
    }

    public void setMerchantCustomerSupport(MerchantCustomerSupport merchantCustomerSupport) {
        this.merchantCustomerSupport = merchantCustomerSupport;
    }

    public String getMerchantAlias() {
        return merchantAlias;
    }

    public void setMerchantAlias(String merchantAlias) {
        this.merchantAlias = merchantAlias;
    }

    public Object getMerchantTerms() {
        return merchantTerms;
    }

    public void setMerchantTerms(Object merchantTerms) {
        this.merchantTerms = merchantTerms;
    }

    public Object getMerchantPrivacyPolicy() {
        return merchantPrivacyPolicy;
    }

    public void setMerchantPrivacyPolicy(Object merchantPrivacyPolicy) {
        this.merchantPrivacyPolicy = merchantPrivacyPolicy;
    }

    public Integer getPageExpiration() {
        return pageExpiration;
    }

    public void setPageExpiration(Integer pageExpiration) {
        this.pageExpiration = pageExpiration;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMerchantMainButton() {
        return merchantMainButton;
    }

    public void setMerchantMainButton(String merchantMainButton) {
        this.merchantMainButton = merchantMainButton;
    }

    public Object getCancelCheckoutUrl() {
        return cancelCheckoutUrl;
    }

    public void setCancelCheckoutUrl(Object cancelCheckoutUrl) {
        this.cancelCheckoutUrl = cancelCheckoutUrl;
    }

    public Object getCompleteCheckoutUrl() {
        return completeCheckoutUrl;
    }

    public void setCompleteCheckoutUrl(Object completeCheckoutUrl) {
        this.completeCheckoutUrl = completeCheckoutUrl;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Object getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(Object paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public Object getPaymentMethodTypeCategories() {
        return paymentMethodTypeCategories;
    }

    public void setPaymentMethodTypeCategories(Object paymentMethodTypeCategories) {
        this.paymentMethodTypeCategories = paymentMethodTypeCategories;
    }

    public Object getPaymentMethodTypesInclude() {
        return paymentMethodTypesInclude;
    }

    public void setPaymentMethodTypesInclude(Object paymentMethodTypesInclude) {
        this.paymentMethodTypesInclude = paymentMethodTypesInclude;
    }

    public Object getPaymentMethodTypesExclude() {
        return paymentMethodTypesExclude;
    }

    public void setPaymentMethodTypesExclude(Object paymentMethodTypesExclude) {
        this.paymentMethodTypesExclude = paymentMethodTypesExclude;
    }

    public Object getAccountFundingTransaction() {
        return accountFundingTransaction;
    }

    public void setAccountFundingTransaction(Object accountFundingTransaction) {
        this.accountFundingTransaction = accountFundingTransaction;
    }

    public Object getCustomer() {
        return customer;
    }

    public void setCustomer(Object customer) {
        this.customer = customer;
    }

    public CustomElements getCustomElements() {
        return customElements;
    }

    public void setCustomElements(CustomElements customElements) {
        this.customElements = customElements;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Object getPaymentExpiration() {
        return paymentExpiration;
    }

    public void setPaymentExpiration(Object paymentExpiration) {
        this.paymentExpiration = paymentExpiration;
    }

    public List<Object> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Object> cartItems) {
        this.cartItems = cartItems;
    }

    public Object getEscrow() {
        return escrow;
    }

    public void setEscrow(Object escrow) {
        this.escrow = escrow;
    }

    public Object getEscrowReleaseDays() {
        return escrowReleaseDays;
    }

    public void setEscrowReleaseDays(Object escrowReleaseDays) {
        this.escrowReleaseDays = escrowReleaseDays;
    }

    @Override
	public String toString() {
    	return "Data ID: " + getId() + ", status: " + getStatus() + ", redirect URL: " + getRedirectUrl() + ", payment: " + getPayment();
    }

}