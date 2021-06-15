package com.idega.block.creditcard.data;

import java.sql.Timestamp;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.hsqldb.lib.StringUtil;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */

public class DummyMerchantBMPBean extends GenericEntity implements DummyMerchant {

	private static final long serialVersionUID = -8593110250335041804L;

	private static final String ENTITY_NAME = "CC_DUMMY_MERCHANT";
	private static final String COLUMN_NAME = "MERCHANT_NAME";
	private static final String COLUMN_ACCEPTOR_TERMINAL_ID = "ACCEPTOR_TERM_ID";
	private static final String COLUMN_ACCEPTOR_IDENTIFICATION = "ACCEPTOR_IDENTIFICATION";
	private static final String COLUMN_START_DATE = "START_DATE";
	private static final String COLUMN_MODIFICATION_DATE = "MODIFICATION_DATE";
	private static final String COLUMN_END_DATE = "END_DATE";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getType() {
		return MERCHANT_TYPE_DUMMY;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", true, true, String.class);
		addAttribute(COLUMN_ACCEPTOR_TERMINAL_ID, "accTermID", true, true, String.class);
		addAttribute(COLUMN_ACCEPTOR_IDENTIFICATION, "accId", true, true, String.class);
	    addAttribute(COLUMN_START_DATE, "Start date", true, true, Timestamp.class);
	    addAttribute(COLUMN_MODIFICATION_DATE, "Modification date", true, true, Timestamp.class);
	    addAttribute(COLUMN_END_DATE, "End date", true, true, Timestamp.class);
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
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getLocation()
	 */
	@Override
	public String getLocation() {
		return "DUMMY_LOCATION";
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getUser()
	 */
	@Override
	public String getUser() {
		return "DUMMY_USER";
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getPassword()
	 */
	@Override
	public String getPassword() {
		return "DUMMY_PASSWORD";
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getTerminalID()
	 */
	@Override
	public String getTerminalID() {
		return getStringColumnValue(COLUMN_ACCEPTOR_TERMINAL_ID);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getMerchantID()
	 */
	@Override
	public String getMerchantID() {
		return getStringColumnValue(COLUMN_ACCEPTOR_IDENTIFICATION);
	}

	/**
	 * Not implemented
	 */
	@Override
	public String getExtraInfo() {
		return null;
	}

	/** (non-Javadoc)
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setLocation(java.lang.String)
	 */
	@Override
	public void setLocation(String location) {

	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setUser(java.lang.String)
	 */
	@Override
	public void setUser(String user) {

	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {

	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setTerminalID(java.lang.String)
	 */
	@Override
	public void setTerminalID(String terminalID) {
		setColumn(COLUMN_ACCEPTOR_TERMINAL_ID, terminalID);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setMerchantID(java.lang.String)
	 */
	@Override
	public void setMerchantID(String id) {
		setColumn(COLUMN_ACCEPTOR_IDENTIFICATION, id);
	}

	/**
	 * Not Implemented
	 */
	@Override
	public void setExtraInfo(String extra) {
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getStartDate()
	 */
	@Override
	public Timestamp getStartDate() {
		return getTimestampColumnValue(COLUMN_START_DATE);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getEndDate()
	 */
	@Override
	public Timestamp getEndDate() {
		return getTimestampColumnValue(COLUMN_END_DATE);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#IsDeleted()
	 */
	@Override
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_IS_DELETED);
	}

	private void setStartDate(Timestamp startDate) {
		setColumn(COLUMN_START_DATE, startDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setEndDate(java.sql.Date)
	 */
	private void setEndDate(Timestamp endDate) {
		setColumn(COLUMN_END_DATE, endDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getModificationDate()
	 */
	@Override
	public Timestamp getModificationDate() {
		return getTimestampColumnValue(COLUMN_MODIFICATION_DATE);
	}

	private void setModificationDate(Timestamp modificationDate) {
		setColumn(COLUMN_MODIFICATION_DATE, modificationDate);
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


	public Object ejbFindByName(String merchantName) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		if (!StringUtil.isEmpty(merchantName)) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_NAME), MatchCriteria.EQUALS, merchantName));
			return idoFindOnePKByQuery(query);
		}
		return null;
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