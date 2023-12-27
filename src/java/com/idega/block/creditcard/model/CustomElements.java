
package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomElements implements Serializable {

    private static final long serialVersionUID = -3975071126373808874L;

    @SerializedName("save_card_default")
    @Expose
    private Boolean saveCardDefault;

    @SerializedName("display_description")
    @Expose
    private Boolean displayDescription;

    @SerializedName("payment_fees_display")
    @Expose
    private Boolean paymentFeesDisplay;

    @SerializedName("merchant_currency_only")
    @Expose
    private Boolean merchantCurrencyOnly;

    @SerializedName("billing_address_collect")
    @Expose
    private Boolean billingAddressCollect;

    @SerializedName("dynamic_currency_conversion")
    @Expose
    private Boolean dynamicCurrencyConversion;

    public Boolean getSaveCardDefault() {
        return saveCardDefault;
    }

    public void setSaveCardDefault(Boolean saveCardDefault) {
        this.saveCardDefault = saveCardDefault;
    }

    public Boolean getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(Boolean displayDescription) {
        this.displayDescription = displayDescription;
    }

    public Boolean getPaymentFeesDisplay() {
        return paymentFeesDisplay;
    }

    public void setPaymentFeesDisplay(Boolean paymentFeesDisplay) {
        this.paymentFeesDisplay = paymentFeesDisplay;
    }

    public Boolean getMerchantCurrencyOnly() {
        return merchantCurrencyOnly;
    }

    public void setMerchantCurrencyOnly(Boolean merchantCurrencyOnly) {
        this.merchantCurrencyOnly = merchantCurrencyOnly;
    }

    public Boolean getBillingAddressCollect() {
        return billingAddressCollect;
    }

    public void setBillingAddressCollect(Boolean billingAddressCollect) {
        this.billingAddressCollect = billingAddressCollect;
    }

    public Boolean getDynamicCurrencyConversion() {
        return dynamicCurrencyConversion;
    }

    public void setDynamicCurrencyConversion(Boolean dynamicCurrencyConversion) {
        this.dynamicCurrencyConversion = dynamicCurrencyConversion;
    }

}