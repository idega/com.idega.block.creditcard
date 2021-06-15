package com.idega.block.creditcard2.data.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BORGUN_SAVED_CARDS")
public class BorgunSavedCard implements Serializable{

	private static final long serialVersionUID = 8685686026790184225L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PERSONAL_ID")
	private String personalId;
	
	@Column(name = "VIRTUAL_CARD_NUMBER")
	private String virtualCardNumber;

	@Column(name = "CARD_DISPLAYED_NUMBER")
	private String displayedNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String userId) {
		this.personalId = userId;
	}

	public String getVirtualCardNumber() {
		return virtualCardNumber;
	}

	public void setVirtualCardNumber(String virtualCardNumber) {
		this.virtualCardNumber = virtualCardNumber;
	}

	public String getDisplayedNumber() {
		return displayedNumber;
	}

	public void setDisplayedNumber(String displayedNumber) {
		this.displayedNumber = displayedNumber;
	}
	
}
