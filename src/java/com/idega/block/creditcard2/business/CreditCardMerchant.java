package com.idega.block.creditcard2.business;

import java.sql.Date;

public interface CreditCardMerchant {
	public static final String MERCHANT_TYPE_TPOS = "TPOS";
	public static final String MERCHANT_TYPE_KORTHATHJONUSTAN = "KORTATHJONUSTAN";
	public static final String MERCHANT_TYPE_DUMMY = "DUMMY";
	public static final String MERCHANT_TYPE_BORGUN = "BORGUN";
	
	public String getType();
	
	public Boolean getIsDeleted();
	public Date getStartDate();
	public Date getEndDate();
	public Integer getPrimaryKey();

	public String getName();

	public String getUser();

	public String getPassword();

	public String getMerchantID();

	public String getLocation();

	public String getTerminalID();

	public String getExtraInfo();
	
}
