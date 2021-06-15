package com.idega.block.creditcard.data;
import java.sql.Timestamp;

import javax.ejb.RemoveException;

import com.idega.data.GenericEntity;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TPosMerchantBMPBean extends GenericEntity implements TPosMerchant {

	private static final long serialVersionUID = 103141253215413457L;

private static final String _EntityName = "TPOS_MERCHANT";
  private static final String _ColumnNameName = "MERCHANT_NAME";
  private static final String _ColumnNameMerchantID = "MERCHANT_ID";
  private static final String _ColumnNameLocationID = "LOCATION_ID";
  private static final String _ColumnNameUser = "USER_ID";
  private static final String _ColumnNamePassword = "PASSW";
  private static final String _ColumnNamePosID = "POS_ID";
  private static final String _ColumnNameKeyReceivedPassword = "KEY_RCV_PASSW";
  private static final String _ColumnStartDate = "START_DATE";
  private static final String _ColumnModifiedDate = "MODIFIED_DATE";
  private static final String _ColumnEndDate = "END_DATE";


  public TPosMerchantBMPBean() {
  }

  @Override
public String getType() {
  	return MERCHANT_TYPE_TPOS;
  }

  @Override
public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(_ColumnNameName,"Merchant Name", true, true, String.class);
    addAttribute(_ColumnNameMerchantID,"Merchant ID", true, true, String.class);
    addAttribute(_ColumnNameLocationID,"Location ID", true, true, String.class);
    addAttribute(_ColumnNameUser,"User", true, true, String.class);
    addAttribute(_ColumnNamePassword,"Password", true, true, String.class);
    addAttribute(_ColumnNamePosID,"Pos ID", true, true, String.class);
    addAttribute(_ColumnNameKeyReceivedPassword,"KeyRcvPassw", true, true, String.class);
    addAttribute(_ColumnStartDate, "Start date", true, true, Timestamp.class);
    addAttribute(_ColumnModifiedDate, "Modification date", true, true, Timestamp.class);
    addAttribute(_ColumnEndDate, "End date", true, true, Timestamp.class);
    addAttribute(COLUMN_IS_DELETED, "Is delted", true, true, Boolean.class);
    addAttribute(COLUMN_SHARED_SECRET, "Shared secret", true, true, String.class);
    addAttribute(COLUMN_SUCCESS_REDIRECT_URL, "Success redirect URL", true, true, String.class);
    addAttribute(COLUMN_AUTHORIZATION_TERMINAL, "Authorization terminal", true, true, String.class);
}

@Override
public String getAuthorizationTerminal() {
	return getStringColumnValue(COLUMN_AUTHORIZATION_TERMINAL);
}

@Override
public void setAuthorizationTerminal(String authorizationTerminal) {
	setColumn(COLUMN_AUTHORIZATION_TERMINAL, authorizationTerminal);
}

@Override
public String getSuccessRedirectURL() {
	return getStringColumnValue(COLUMN_SUCCESS_REDIRECT_URL);
}

@Override
public void setSuccessRedirectURL(String successRedirectURL) {
	setColumn(COLUMN_SUCCESS_REDIRECT_URL, successRedirectURL);
}


  @Override
public String getEntityName() {
    return _EntityName;
  }

  /** SETTERS */
  @Override
public void setName(String name) {
    setMerchantName(name);
  }

  @Override
public void setMerchantName(String name) {
    setColumn(_ColumnNameName, name);
  }

  @Override
public void setMerchantID(String id) {
    setColumn(_ColumnNameMerchantID, id);
  }

  @Override
public void setLocationID(String id) {
    setColumn(_ColumnNameLocationID, id);
  }

  @Override
public void setUserID(String id) {
    setColumn(_ColumnNameUser, id);
  }

  @Override
public void setPassword(String password) {
    setColumn(_ColumnNamePassword, password);
  }

  @Override
public void setPosID(String id) {
    setColumn(_ColumnNamePosID, id);
  }

  @Override
public void setKeyReceivedPassword(String keyRcvPassw) {
    setColumn(_ColumnNameKeyReceivedPassword, keyRcvPassw);
  }

  /** GETTERS */
  @Override
public String getName() {
    return getMerchantName();
  }

  @Override
public String getMerchantName() {
    return getStringColumnValue(_ColumnNameName);
  }

  @Override
public String getMerchantID() {
    return getStringColumnValue(_ColumnNameMerchantID);
  }

  @Override
public String getLocationID() {
    return getStringColumnValue(_ColumnNameLocationID);
  }

  @Override
public String getUserID() {
    return getStringColumnValue(_ColumnNameUser);
  }

  @Override
public String getPassword() {
    return getStringColumnValue(_ColumnNamePassword);
  }

  @Override
public String getPosID() {
    return getStringColumnValue(_ColumnNamePosID);
  }

  @Override
public String getKeyReceivedPassword() {
    return getStringColumnValue(_ColumnNameKeyReceivedPassword);
  }


	/* (non-Javadoc)
	 * @see com.idega.block.tpos.business.CreditCardMerchant#getLocation()
	 */
	@Override
	public String getLocation() {
		return getLocationID();
	}


	/* (non-Javadoc)
	 * @see com.idega.block.tpos.business.CreditCardMerchant#getUser()
	 */
	@Override
	public String getUser() {
		return getUserID();
	}


	/* (non-Javadoc)
	 * @see com.idega.block.tpos.business.CreditCardMerchant#getTerminalID()
	 */
	@Override
	public String getTerminalID() {
		return getPosID();
	}


	/* (non-Javadoc)
	 * @see com.idega.block.tpos.business.CreditCardMerchant#getExtraInfo()
	 */
	@Override
	public String getExtraInfo() {
		return getKeyReceivedPassword();
	}


	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setLocation(java.lang.String)
	 */
	@Override
	public void setLocation(String location) {
		setLocationID(location);
	}


	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setUser(java.lang.String)
	 */
	@Override
	public void setUser(String user) {
		setUserID(user);
	}


	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setTerminalID(java.lang.String)
	 */
	@Override
	public void setTerminalID(String terminalID) {
		setPosID(terminalID);
	}


	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setExtraInfo(java.lang.String)
	 */
	@Override
	public void setExtraInfo(String extra) {
		setKeyReceivedPassword(extra);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getStartDate()
	 */
	@Override
	public Timestamp getStartDate() {
		return getTimestampColumnValue(_ColumnStartDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getEndDate()
	 */
	@Override
	public Timestamp getEndDate() {
		return getTimestampColumnValue(_ColumnEndDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#IsDeleted()
	 */
	@Override
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_IS_DELETED);
	}

	private void setStartDate(Timestamp startDate) {
		setColumn(_ColumnStartDate, startDate);
	}

	private void setEndDate(Timestamp endDate) {
		setColumn(_ColumnEndDate, endDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getModificationDate()
	 */
	@Override
	public Timestamp getModificationDate() {
		return getTimestampColumnValue(_ColumnModifiedDate);
	}

	private void setModificationDate(Timestamp modificationDate) {
		setColumn(_ColumnModifiedDate, modificationDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#store()
	 */
	@Override
	public void store() {
		setModificationDate(IWTimestamp.RightNow().getTimestamp());
		if (getStartDate() == null) {
			setStartDate(IWTimestamp.RightNow().getTimestamp());
		}
		super.store();
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#remove()
	 */
	@Override
	public void remove() throws RemoveException {
		setModificationDate(IWTimestamp.RightNow().getTimestamp());
		setEndDate(IWTimestamp.RightNow().getTimestamp());
		setColumn(COLUMN_IS_DELETED, true);
		store();
	}

	@Override
	public void setSharedSecret(String secret) {
		setColumn(COLUMN_SHARED_SECRET, secret);
	}

	@Override
	public String getSharedSecret() {
		return getStringColumnValue(COLUMN_SHARED_SECRET);
	}

	@Override
	public Integer getId() {
		Object pk = getPrimaryKey();
		return (Integer) pk;
	}

}