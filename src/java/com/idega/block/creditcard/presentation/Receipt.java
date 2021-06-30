package com.idega.block.creditcard.presentation;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.idega.block.creditcard.CreditCardUtil;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.contact.data.Phone;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.StringUtil;
import com.idega.util.text.TextSoap;

/**
 * Title: idegaWeb Description: Copyright: Copyright (c) 2001 Company: idega
 * 
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Receipt extends PresentationObjectContainer {

	private IWResourceBundle iwrb;
	private IWBundle bundle;
	private CreditCardAuthorizationEntry _entries;
	private Supplier _supplier;

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	public Receipt(CreditCardAuthorizationEntry entry, Supplier supplier) {
		this._entries = entry;
		this._supplier = supplier;
	}

	public void main(IWContext iwc) {
		init(iwc);
		createReceipt(iwc);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private void init(IWContext iwc) {
		this.bundle = getBundle(iwc);
		this.iwrb = this.bundle.getResourceBundle(iwc);
	}

	private Text getText(String content) {
		Text text = new Text(content);
		return text;
	}

	private Text getTextBold(String content) {
		Text text = getText(content);
		text.setBold(true);
		return text;
	}

	private void createReceipt(IWContext iwc) {
		Table table = new Table();
		// table.setBorder(1);
		table.setColor("#FFFFFF");
		int row = 1;

		try {
			table.mergeCells(1, row, 2, row);
			table.setRowAlignment(row, "center");
			table.add(getTextBold(this._supplier.getName()), 1, row);
			++row;
			table.mergeCells(1, row, 2, row);
			table.setRowAlignment(row, "center");
			table.add(getText(this._supplier.getAddress().getStreetName()), 1, row);
			List phones = this._supplier.getHomePhone();
			if (phones != null && phones.size() > 0) {
				++row;
				table.mergeCells(1, row, 2, row);
				table.setRowAlignment(row, "center");
				table.add(getText(this.iwrb.getLocalizedString("travel.phone", "Phone") + ": " + ((Phone) phones.get(phones.size() - 1)).getNumber()), 1, row);
			}

			if (this._supplier.getOrganizationID() != null) {
				++row;
				table.mergeCells(1, row, 2, row);
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
				table.add(getText(this.iwrb.getLocalizedString("travel.organization_id", "Organization id") + ": " + this._supplier.getOrganizationID()), 1, row);
			}

			HorizontalRule hr = new HorizontalRule("100%");
			hr.setColor("#000000");
			++row;
			table.mergeCells(1, row, 2, row);
			table.add(hr, 1, row);
			++row;
			IWTimestamp stamp = null;
			/*
			 * if (_client != null) { stamp = _client.getIdegaTimestamp(); } else
			 */

			if (this._entries != null) {
				try {
					Date date = this._entries.getDate();
					// String time = this._entries.getEntryTime();
					// String date = this._entries.getEntryDate();
					// stamp = new IWTimestamp(date.substring(0,4)+"-"+date.substring(4,
					// 6)+"-"+date.substring(6)+" "+time.substring(0,
					// 2)+":"+time.substring(2, 4)+":"+time.substring(4, 6));
					stamp = new IWTimestamp(date);
				}
				catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			if (stamp != null) {
				table.add(getText(this.iwrb.getLocalizedString("date", "Date") + " : " + stamp.getLocaleDate(iwc)), 1, row);
				++row;
				table.add(getText(this.iwrb.getLocalizedString("time", "Time") + " : " + TextSoap.addZero(stamp.getHour()) + ":" + TextSoap.addZero(stamp.getMinute())), 1, row);
			}
			else {
				table.add(getText(this.iwrb.getLocalizedString("date", "Date") + " : <error>"), 1, row);
				++row;
				table.add(getText(this.iwrb.getLocalizedString("time", "Time") + " : <error>"), 1, row);

			}

			table.mergeCells(1, row, 2, row);
			table.add(hr, 1, row);
			++row;

			++row;

			/*
			 * if (_client != null) {
			 * table.add(getTextBold(_client.getCardTypeName()),1,row); } else
			 */
			if (this._entries != null) {
				table.add(getTextBold(this._entries.getBrandName()), 1, row);
			}
			++row;
			/*
			 * if (_client != null) { String ccNumber = _client.getCCNumber(); if
			 * (ccNumber.length() <5) { table.add(getText(ccNumber),1,row); }else {
			 * for (int i = 0; i < ccNumber.length() -4; i++) {
			 * table.add(getText("*"),1,row); } } } else {
			 */

			String cardNumber = this._entries.getCardNumber();
			String maskedNumber = CreditCardUtil.getMaskedCreditCardNumber(cardNumber);
			table.add(getText(StringUtil.isEmpty(maskedNumber) ? CoreConstants.MINUS : maskedNumber), 1, row);
			// }

			String expire = "";
			/*
			 * if (_client != null) { expire =_client.getExpire(); } else if (this._entries !=
			 * null) {
			 */
			expire = this._entries.getCardExpires();
			// }
			table.add(getText(StringUtil.isEmpty(expire) ? CoreConstants.EMPTY : (this.iwrb.getLocalizedString("valid", "Valid") + Text.NON_BREAKING_SPACE)), 2, row);
			table.add(getText(StringUtil.isEmpty(expire) ? CoreConstants.EMPTY : (expire.substring(2, 4) + CoreConstants.SLASH + expire.substring(0, 2))), 2, row);

			++row;
			table.add(getText(Text.NON_BREAKING_SPACE), 1, row);
			++row;
			table.add(getTextBold(this.iwrb.getLocalizedString("amount", "Amount")), 1, row);
			/*
			 * if (_client != null) {
			 * table.add(getTextBold(TextSoap.decimalFormat(_client.getAmount(), 2)),
			 * 2, row);
			 * table.add(getTextBold(Text.NON_BREAKING_SPACE+_client.getCurrency()),
			 * 2, row); } else if (this._entries != null) {
			 * table.add(getTextBold(TextSoap.decimalFormat(this._entries.getAuthorisationAmount(),
			 * 2)), 2, row);
			 * table.add(getTextBold(Text.NON_BREAKING_SPACE+this._entries.getAuthorisationCurrency()),
			 * 2, row); }
			 */
			table.add(getTextBold(TextSoap.decimalFormat(this._entries.getAmount() / CreditCardAuthorizationEntry.amountMultiplier, 2)), 2, row);
			table.add(getTextBold(Text.NON_BREAKING_SPACE + this._entries.getCurrency()), 2, row);

			++row;
			table.add(getText(Text.NON_BREAKING_SPACE), 1, row);
			++row;
			table.add(getText(this.iwrb.getLocalizedString("autorization_number", "Autorization number")), 1, row);
			/*
			 * if(_client != null) { table.add(getText(_client.getAutorIdentifyRSP()),
			 * 2, row); } else if (this._entries != null) {
			 * table.add(getText(this._entries.getAuthorisationIdRsp()), 2, row); }
			 */
			table.add(getText(this._entries.getAuthorizationCode()), 2, row);
		}
		catch (SQLException sql) {
			sql.printStackTrace(System.err);
		}

		table.setColumnAlignment(2, "right");

		add(table);
	}

	public synchronized Object clone() {
		Receipt obj = null;
		try {
			obj = (Receipt) super.clone();
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
}
