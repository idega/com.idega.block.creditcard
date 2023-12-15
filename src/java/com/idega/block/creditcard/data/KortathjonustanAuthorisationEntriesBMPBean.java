package com.idega.block.creditcard.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * @author gimmi
 */
public class KortathjonustanAuthorisationEntriesBMPBean extends GenericEntity implements KortathjonustanAuthorisationEntries, CreditCardAuthorizationEntry {

	private static final long serialVersionUID = 5094910898141424839L;

	private static final String TABLE_NAME = "CC_KORTTHJ_AUTH_ENTRIES";

	private static final String COLUMN_AMOUNT = "AMOUNT";
	private static final String COLUMN_AUTHORIZATION_CODE = "AUTH_CODE";
	private static final String COLUMN_BRAND_NAME = "BRAND_NAME";
	private static final String COLUMN_CARD_EXPIRES = "CARD_EXPIRES";
	private static final String COLUMN_CARD_NUMBER = "CARD_NUMBER";
	private static final String COLUMN_CURRENCY = "CURRENCY";
	private static final String COLUMN_DATE = "ENTRY_DATE";
	private static final String COLUMN_ERROR_NUMBER = "ERROR_NUMBER";
	private static final String COLUMN_ERROR_TEXT = "ERROR_TEXT";
	private static final String COLUMN_SERVER_RESPONSE = "SERVER_RESPONSE";
	private static final String COLUMN_PARENT_ID = "PARENT_ID";
	private static final String COLUMN_TRANSACTION_TYPE = "TRANSACTION_TYPE"; //sale or refund ?

	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_AMOUNT, "amount", true, true, Double.class);
		addAttribute(COLUMN_AUTHORIZATION_CODE, "auth_code", true, true, String.class);
		addAttribute(COLUMN_BRAND_NAME, "brand_name", true, true, String.class);
		addAttribute(COLUMN_CARD_EXPIRES, "card expire date", true, true, String.class);
		addAttribute(COLUMN_CARD_NUMBER, "card number", true, true, String.class);
		addAttribute(COLUMN_CURRENCY, "currency", true, true, String.class);
		addAttribute(COLUMN_DATE, "date", true, true, Date.class);
		addAttribute(COLUMN_ERROR_NUMBER, "error number", true, true , String.class);
		addAttribute(COLUMN_ERROR_TEXT, "error text", true, true , String.class);
		addAttribute(COLUMN_TRANSACTION_TYPE, "transaction_type", true, true, String.class);
		addOneToOneRelationship(COLUMN_PARENT_ID, KortathjonustanAuthorisationEntries.class);

		addAttribute(COLUMN_SERVER_RESPONSE, "server response", true, true, String.class, 1000);
		addAttribute(COLUMN_REFERENCE, "Reference", true, true, String.class);
		addAttribute(COLUMN_TIMESTAMP, "timestamp", true, true, Timestamp.class);
		addUniqueIDColumn();
		addMetaDataRelationship();
		addAttribute(COLUMN_PAYMENT_ID, "Payment ID", true, true, String.class);
		addAttribute(COLUMN_CARD_TOKEN, "Card token", true, true, String.class);
		addAttribute(COLUMN_TRANSACTION_ID, "Transaction ID", true, true, String.class);
		addAttribute(COLUMN_REFUND, "Refund", true, true, Boolean.class);
	}

	@Override
	public void setTransactionId(String transactionId) {
		setColumn(COLUMN_TRANSACTION_ID, transactionId);
	}

	@Override
	public String getTransactionId() {
		return getStringColumnValue(COLUMN_TRANSACTION_ID);
	}

	@Override
	public String getPaymentId() {
		return getStringColumnValue(COLUMN_PAYMENT_ID);
	}

	@Override
	public void setPaymentId(String paymentId) {
		setColumn(COLUMN_PAYMENT_ID, paymentId);
	}

	@Override
	public Timestamp getTimestamp() {
		return getTimestampColumnValue(COLUMN_TIMESTAMP);
	}

	@Override
	public void setTimestamp(Timestamp timestamp) {
		setColumn(COLUMN_TIMESTAMP, timestamp);
	}

	@Override
	public double getAmount() {
		return getDoubleColumnValue(COLUMN_AMOUNT);
	}

	@Override
	public void setAmount(double amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	@Override
	public String getCurrency() {
		return getStringColumnValue(COLUMN_CURRENCY);
	}

	@Override
	public void setCurrency(String currency) {
		setColumn(COLUMN_CURRENCY, currency);
	}

	@Override
	public Date getDate() {
		return getDateColumnValue(COLUMN_DATE);
	}

	@Override
	public void setDate(Date date) {
		setColumn(COLUMN_DATE, date);
	}

	@Override
	public String getCardExpires() {
		return getStringColumnValue(COLUMN_CARD_EXPIRES);
	}

	@Override
	public void setCardExpires(String expires) {
		setColumn(COLUMN_CARD_EXPIRES, expires);
	}

	@Override
	public String getCardNumber() {
		return getStringColumnValue(COLUMN_CARD_NUMBER);
	}

	@Override
	public void setCardNumber(String number) {
		setColumn(COLUMN_CARD_NUMBER, number);
	}

	@Override
	public String getBrandName() {
		return getStringColumnValue(COLUMN_BRAND_NAME);
	}

	@Override
	public void setBrandName(String name) {
		setColumn(COLUMN_BRAND_NAME, name);
	}

	@Override
	public String getAuthorizationCode() {
		return getStringColumnValue(COLUMN_AUTHORIZATION_CODE);
	}

	@Override
	public void setAuthorizationCode(String code) {
		setColumn(COLUMN_AUTHORIZATION_CODE, code);
	}

	@Override
	public String getTransactionType() {
		return getStringColumnValue(COLUMN_TRANSACTION_TYPE);
	}

	@Override
	public void setTransactionType(String type) {
		setColumn(COLUMN_TRANSACTION_TYPE, type);
	}

	@Override
	public int getParentID() {
		return getIntColumnValue(COLUMN_PARENT_ID);
	}

	@Override
	public CreditCardAuthorizationEntry getParent() {
		return (KortathjonustanAuthorisationEntries) getColumnValue(COLUMN_PARENT_ID);
	}

	@Override
	public void setParentID(int id) {
		setColumn(COLUMN_PARENT_ID, id);
	}

	public Object ejbFindByAuthorizationCode(String code, IWTimestamp stamp) throws FinderException {
		Table table = new Table(this);
		Column auth = new Column(table, COLUMN_AUTHORIZATION_CODE);
		Column date = new Column(table, COLUMN_DATE);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(auth, MatchCriteria.EQUALS, code));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.EQUALS, stamp.getDate().toString()));
		return this.idoFindOnePKBySQL(query.toString());

		//return this.idoFindOnePKByColumnBySQL(COLUMN_AUTHORIZATION_CODE, code);
	}

	public Object ejbFindByUniqueId(String uniqueId) throws FinderException {
		Table table = new Table(this);
		Column unique = new Column(table, UNIQUE_ID_COLUMN_NAME);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));

		String shortenedUniqueId = uniqueId;
		if (uniqueId.length() > 30) {
			shortenedUniqueId = uniqueId.substring(0, 30);
		}
		query.addCriteria(new OR(new MatchCriteria(unique, MatchCriteria.EQUALS, uniqueId), new MatchCriteria(unique, MatchCriteria.LIKE, true, shortenedUniqueId)));

		return this.idoFindOnePKBySQL(query.toString());
	}

	public Integer ejbFindByMetaData(String key, String value) throws FinderException {
		Collection<?> entries = super.idoFindPKsByMetaData(key, value);
		if (ListUtil.isEmpty(entries)) {
			return null;
		}

		Object entryId = entries.iterator().next();
		return entryId instanceof Integer ? (Integer) entryId : null;
	}

	public Collection<?> ejbFindByDate(IWTimestamp stamp) throws FinderException {
		Table table = new Table(this);
		Column date = new Column(table, COLUMN_DATE);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));

		query.addCriteria(new MatchCriteria(date, MatchCriteria.EQUALS, stamp.getDate().toString()));
		return this.idoFindPKsByQuery(query);
	}

	public Collection<?> ejbFindByDates(IWTimestamp from, IWTimestamp to) throws FinderException {
		to.addDays(1);

		Table table = new Table(this);
		Column date = new Column(table, COLUMN_DATE);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));

		query.addCriteria(new MatchCriteria(date, MatchCriteria.GREATEREQUAL, from.getDate().toString()));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.LESSEQUAL, to.getDate().toString()));
		return this.idoFindPKsByQuery(query);
	}


	@Override
	public void setErrorNumber(String errorNumber) {
		setColumn(COLUMN_ERROR_NUMBER, errorNumber);
	}

	@Override
	public String getErrorNumber() {
		return getStringColumnValue(COLUMN_ERROR_NUMBER);
	}

	@Override
	public void setErrorText(String errorText) {
		setColumn(COLUMN_ERROR_TEXT, errorText);
	}

	@Override
	public String getErrorText() {
		return getStringColumnValue(COLUMN_ERROR_TEXT);
	}

	@Override
	public void setServerResponse(String response) {
		setColumn(COLUMN_SERVER_RESPONSE, response);
	}

	@Override
	public String getServerResponse() {
		return getStringColumnValue(COLUMN_SERVER_RESPONSE);
	}

	@Override
	public String getExtraField() {
		return getServerResponse();
	}

	@Override
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		Object obj = this.idoFindOnePKByColumnBySQL(COLUMN_PARENT_ID, this.getPrimaryKey().toString());
		if (obj != null) {
			KortathjonustanAuthorisationEntriesHome home;
			try {
				home = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntries.class);
				return home.findByPrimaryKey(obj);
			}
			catch (IDOLookupException e) {
				throw new FinderException(e.getMessage());
			}
		}
		return null;
	}

	public Collection<?> ejbFindRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
		to.addDays(1);

		Table table = new Table(this);
		Column date = new Column(table, COLUMN_DATE);
		Column code = new Column(table, COLUMN_TRANSACTION_TYPE);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(code, MatchCriteria.EQUALS, KortathjonustanAuthorisationEntries.AUTHORIZATION_TYPE_REFUND));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.GREATEREQUAL, from.getDate().toString()));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.LESSEQUAL, to.getDate().toString()));

		return this.idoFindPKsByQuery(query);
	}

	@Override
	public void setReference(String reference) {
		setColumn(COLUMN_REFERENCE, reference);
	}

	@Override
	public String getReference() {
		return getStringColumnValue(COLUMN_REFERENCE);
	}

	@Override
	public void setDate(Timestamp date) {
		if (date != null) {
			setDate(new Date(date.getTime()));
		}
	}

	@Override
	public void setCardToken(String cardToken) {
		setColumn(COLUMN_CARD_TOKEN, cardToken);
	}

	@Override
	public String getCardToken() {
		return getStringColumnValue(COLUMN_CARD_TOKEN);
	}

	@Override
	public void setAmount(Double amount) {
		setAmount(amount == null ? 0 : amount.doubleValue());
	}

	@Override
	public void setMerchant(CreditCardMerchant merchant) {
	}

	@Override
	public CreditCardMerchant getMerchant() {
		return null;
	}

	@Override
	public boolean isRefund() {
		return getBooleanColumnValue(COLUMN_REFUND, false);
	}

	@Override
	public void setRefund(boolean refund) {
		setColumn(COLUMN_REFUND, refund);
	}

	@Override
	public boolean isSuccess() {
		return !StringUtil.isEmpty(getAuthorizationCode());
	}

}