/*
 * Created on 1.4.2004
 */
package com.idega.block.creditcard.data;

import java.sql.Date;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.MetaDataCapable;
import com.idega.data.UniqueIDCapable;

/**
 * @author gimmi
 */
public interface CreditCardAuthorizationEntry extends IDOEntity, UniqueIDCapable, MetaDataCapable {

	public static final String	COLUMN_TIMESTAMP = "timestamp",
								COLUMN_PAYMENT_ID = "payment_id";

	public static float amountMultiplier = 100;

	@Override
	public Object getPrimaryKey();

	public double getAmount();

	public String getCurrency();

	public Date getDate();

	/**
	 * Get the card expire date
	 *
	 * @return Exiredate for card MMYY
	 */
	public String getCardExpires();

	public String getCardNumber();

	public String getBrandName();

	public String getAuthorizationCode();

	public String getErrorNumber();

	public String getErrorText();

	public String getExtraField();

	public int getParentID();

	public CreditCardAuthorizationEntry getParent();

	public CreditCardAuthorizationEntry getChild() throws FinderException;

	public void setReference(String reference);

	public String getReference();

	public void setAuthorizationCode(String authCode);

	public void setDate(Timestamp date);

	public Timestamp getTimestamp();

	public void setTimestamp(Timestamp timestamp);

	public String getPaymentId();

	public void setPaymentId(String paymentId);

}