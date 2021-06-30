/*
 * $Id: KortathjonustanAuthorisationEntriesHomeImpl.java,v 1.4 2005/06/15 16:37:14 gimmi Exp $
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

import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 *
 *  Last modified: $Date: 2005/06/15 16:37:14 $ by $Author: gimmi $
 *
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public class DummyAuthorisationEntriesHomeImpl extends IDOFactory implements
		DummyAuthorisationEntriesHome {

	/**
	 *
	 */
	private static final long serialVersionUID = -6276047455390776882L;

	protected Class getEntityInterfaceClass() {
		return DummyAuthorisationEntries.class;
	}

	public DummyAuthorisationEntries create() throws javax.ejb.CreateException {
		return (DummyAuthorisationEntries) super.createIDO();
	}

	public DummyAuthorisationEntries findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (DummyAuthorisationEntries) super.findByPrimaryKeyIDO(pk);
	}

	public DummyAuthorisationEntries findByAuthorizationCode(String code, IWTimestamp stamp)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((DummyAuthorisationEntriesBMPBean) entity).ejbFindByAuthorizationCode(code, stamp);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByDates(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((DummyAuthorisationEntriesBMPBean) entity).ejbFindByDates(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((DummyAuthorisationEntriesBMPBean) entity).ejbFindRefunds(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CreditCardAuthorizationEntry getAuthorizationEntryByUniqueId(String uniqueId) {
		try {
			com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
			Object pk = ((DummyAuthorisationEntriesBMPBean) entity).ejbFindByUniqueId(uniqueId);
			this.idoCheckInPooledEntity(entity);
			return this.findByPrimaryKey(pk);
		} catch (FinderException fe) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CreditCardAuthorizationEntry getAuthorizationEntryByMetaData(String key, String value) throws FinderException {
		try {
			com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
			Integer pk = ((DummyAuthorisationEntriesBMPBean) entity).ejbFindByMetaData(key, value);
			this.idoCheckInPooledEntity(entity);
			return this.findByPrimaryKey(pk);
		} catch (FinderException fe) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}