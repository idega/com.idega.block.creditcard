package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;
import java.util.ArrayList;

public class Datum implements Serializable {

	private static final long serialVersionUID = 1074260040970796735L;

	private String type;

	private String name;

	private String category;

	private String image;

	private String country;

	private String payment_flow_type;

	private ArrayList<String> currencies;

	private int status;

	private boolean is_cancelable;

	private ArrayList<PaymentOption> payment_options;

	private boolean is_expirable;

	private boolean is_online;

	private Object minimum_expiration_seconds;

	private Object maximum_expiration_seconds;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPayment_flow_type() {
		return payment_flow_type;
	}

	public void setPayment_flow_type(String payment_flow_type) {
		this.payment_flow_type = payment_flow_type;
	}

	public ArrayList<String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(ArrayList<String> currencies) {
		this.currencies = currencies;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isIs_cancelable() {
		return is_cancelable;
	}

	public void setIs_cancelable(boolean is_cancelable) {
		this.is_cancelable = is_cancelable;
	}

	public ArrayList<PaymentOption> getPayment_options() {
		return payment_options;
	}

	public void setPayment_options(ArrayList<PaymentOption> payment_options) {
		this.payment_options = payment_options;
	}

	public boolean isIs_expirable() {
		return is_expirable;
	}

	public void setIs_expirable(boolean is_expirable) {
		this.is_expirable = is_expirable;
	}

	public boolean isIs_online() {
		return is_online;
	}

	public void setIs_online(boolean is_online) {
		this.is_online = is_online;
	}

	public Object getMinimum_expiration_seconds() {
		return minimum_expiration_seconds;
	}

	public void setMinimum_expiration_seconds(Object minimum_expiration_seconds) {
		this.minimum_expiration_seconds = minimum_expiration_seconds;
	}

	public Object getMaximum_expiration_seconds() {
		return maximum_expiration_seconds;
	}

	public void setMaximum_expiration_seconds(Object maximum_expiration_seconds) {
		this.maximum_expiration_seconds = maximum_expiration_seconds;
	}

}