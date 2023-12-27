package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class PaymentMethodData implements Serializable {

    private static final long serialVersionUID = -4802522732424413803L;

	private String id;

    private String type;

    private String category;

    private Metadata metadata;

    private String image;

    private String webhook_url;

    private String supporting_documentation;

    private String next_action;

    private String name;

    private String last4;

    private String acs_check;

    private String cvv_check;

    private BinDetails bin_details;

    private String expiration_year;

    private String expiration_month;

    private String fingerprint_token;

    private String network_reference_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getWebhook_url() {
		return webhook_url;
	}

	public void setWebhook_url(String webhook_url) {
		this.webhook_url = webhook_url;
	}

	public String getSupporting_documentation() {
		return supporting_documentation;
	}

	public void setSupporting_documentation(String supporting_documentation) {
		this.supporting_documentation = supporting_documentation;
	}

	public String getNext_action() {
		return next_action;
	}

	public void setNext_action(String next_action) {
		this.next_action = next_action;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getAcs_check() {
		return acs_check;
	}

	public void setAcs_check(String acs_check) {
		this.acs_check = acs_check;
	}

	public String getCvv_check() {
		return cvv_check;
	}

	public void setCvv_check(String cvv_check) {
		this.cvv_check = cvv_check;
	}

	public BinDetails getBin_details() {
		return bin_details;
	}

	public void setBin_details(BinDetails bin_details) {
		this.bin_details = bin_details;
	}

	public String getExpiration_year() {
		return expiration_year;
	}

	public void setExpiration_year(String expiration_year) {
		this.expiration_year = expiration_year;
	}

	public String getExpiration_month() {
		return expiration_month;
	}

	public void setExpiration_month(String expiration_month) {
		this.expiration_month = expiration_month;
	}

	public String getFingerprint_token() {
		return fingerprint_token;
	}

	public void setFingerprint_token(String fingerprint_token) {
		this.fingerprint_token = fingerprint_token;
	}

	public String getNetwork_reference_id() {
		return network_reference_id;
	}

	public void setNetwork_reference_id(String network_reference_id) {
		this.network_reference_id = network_reference_id;
	}

}