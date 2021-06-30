/*
 * $Id: CreditCardBusiness.java,v 1.11 2007/06/11 18:11:43 gimmi Exp $ Created on Feb 13, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package com.idega.block.creditcard.business;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOService;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Last modified: $Date: 2007/06/11 18:11:43 $ by $Author: gimmi $
 *
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.11 $
 */
public interface CreditCardBusiness extends IBOService {

	public final static String CARD_TYPE_VISA = "VISA";
	public final static String CARD_TYPE_ELECTRON = "ELECTRON";
	public final static String CARD_TYPE_DINERS = "DINERS";
	public final static String CARD_TYPE_DANKORT = "DANKORT";
	public final static String CARD_TYPE_MASTERCARD = "MASTERCARD";
	public final static String CARD_TYPE_JCB = "JCB";
	public final static String CARD_TYPE_AMERICAN_EXPRESS = "AMERICAN_EXRESS";

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getAuthorizationEntries
	 */
	public Collection getAuthorizationEntries(int clientType, String merchantID, IWTimestamp from, IWTimestamp to) throws IDOLookupException, FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardTypeImages
	 */
	public Collection getCreditCardTypeImages(CreditCardClient client) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardClient
	 */
	public CreditCardClient getCreditCardClient(Supplier supplier, IWTimestamp stamp) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardClient
	 */
	public CreditCardClient getCreditCardClient(Group supplierManager, IWTimestamp stamp) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardClient
	 */
	public CreditCardClient getCreditCardClient(CreditCardMerchant merchant) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant(String merchantPK, String merchantType) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardInformation
	 */
	public CreditCardInformation getCreditCardInformation(String merchantPK, String merchantType) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardInformation
	 */
	public CreditCardInformation getCreditCardInformation(Supplier supplier, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardInformation
	 */
	public CreditCardInformation getCreditCardInformation(Group supplierManager, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, Object PK) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, Object PK) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#createCreditCardMerchant
	 */
	public CreditCardMerchant createCreditCardMerchant(String type) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#addCreditCardMerchant
	 */
	public void addCreditCardMerchant(Group supplierManager, CreditCardMerchant merchant) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#addCreditCardMerchant
	 */
	public void addCreditCardMerchant(Supplier supplier, CreditCardMerchant merchant) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardInformations
	 */
	public Collection getCreditCardInformations(Supplier supplier) throws IDORelationshipException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getCreditCardInformations
	 */
	public Collection getCreditCardInformations(Group supplierManager) throws FinderException, IDOLookupException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#verifyCreditCardNumber
	 */
	public boolean verifyCreditCardNumber(String numberToCheck, CreditCardAuthorizationEntry entry) throws IllegalArgumentException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getAuthorizationEntry
	 */
	public CreditCardAuthorizationEntry getAuthorizationEntry(CreditCardInformation info, String authorizationCode, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getAuthorizationEntry
	 */
	public CreditCardAuthorizationEntry getAuthorizationEntry(Group supplierManager, String authorizationCode, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getAuthorizationEntry
	 */
	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode, IWTimestamp stamp) throws java.rmi.RemoteException;

	public CreditCardAuthorizationEntry getAuthorizationEntry(String authorizationCode, IWTimestamp stamp) throws java.rmi.RemoteException;

	public CreditCardAuthorizationEntry getAuthorizationEntryByUniqueId(String uniqueId);

	public CreditCardAuthorizationEntry getAuthorizationEntryByMetaData(String key, String value);

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getUseCVC
	 */
	public boolean getUseCVC(CreditCardClient client) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getUseCVC
	 */
	public boolean getUseCVC(CreditCardMerchant merchant) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getUseCVC
	 */
	public boolean getUseCVC(Supplier supplier, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.creditcard.business.CreditCardBusinessBean#getAllRefunds
	 */
	public Collection getAllRefunds(IWTimestamp from, IWTimestamp to, int clientType) throws IDOLookupException, FinderException, java.rmi.RemoteException;

	public DropdownMenu getCreditCardTypes(CreditCardClient client, IWResourceBundle iwrb, String dropdownName);

	public CreditCardMerchant getCreditCardMerchant(CreditCardInformation ccInfo);

}
