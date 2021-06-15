/*
 * $Id: KortathjonustanAuthorisationEntriesHome.java,v 1.4 2005/06/15 16:37:14 gimmi Exp $
 * Created on 8.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.creditcard.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;


/**
 *
 *  Last modified: $Date: 2005/06/15 16:37:14 $ by $Author: gimmi $
 *
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface DummyAuthorisationEntriesHome extends IDOHome {

	public DummyAuthorisationEntries create() throws javax.ejb.CreateException;

	public DummyAuthorisationEntries findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.creditcard.data.DummyAuthorisationEntriesBMPBean#ejbFindByAuthorizationCode
	 */
	public DummyAuthorisationEntries findByAuthorizationCode(String code, IWTimestamp stamp)
			throws FinderException;

	/**
	 * @see com.idega.block.creditcard.data.DummyAuthorisationEntriesBMPBean#ejbFindByDates
	 */
	public Collection findByDates(IWTimestamp from, IWTimestamp to) throws FinderException;

	/**
	 * @see com.idega.block.creditcard.data.DummyAuthorisationEntriesBMPBean#ejbFindRefunds
	 */
	public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException;

	public CreditCardAuthorizationEntry getAuthorizationEntryByUniqueId(String uniqueId);

	public CreditCardAuthorizationEntry getAuthorizationEntryByMetaData(String key, String value) throws FinderException;

}