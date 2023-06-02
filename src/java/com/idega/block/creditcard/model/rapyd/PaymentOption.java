package com.idega.block.creditcard.model.rapyd;

import java.io.Serializable;

public class PaymentOption implements Serializable {

	private static final long serialVersionUID = -9013646958858849223L;

	private String name;

	private String type;

	private String regex;

	private String description;

	private boolean is_required;

	private boolean is_updatable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIs_required() {
		return is_required;
	}

	public void setIs_required(boolean is_required) {
		this.is_required = is_required;
	}

	public boolean isIs_updatable() {
		return is_updatable;
	}

	public void setIs_updatable(boolean is_updatable) {
		this.is_updatable = is_updatable;
	}

}