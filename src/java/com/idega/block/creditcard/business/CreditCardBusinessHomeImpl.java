/*
 * $Id: CreditCardBusinessHomeImpl.java,v 1.4 2006/02/13 13:21:51 gimmi Exp $
 * Created on Feb 13, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.creditcard.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO gimmi Describe Type CreditCardBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/02/13 13:21:51 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public class CreditCardBusinessHomeImpl extends IBOHomeImpl implements CreditCardBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CreditCardBusiness.class;
	}

	public CreditCardBusiness create() throws javax.ejb.CreateException {
		return (CreditCardBusiness) super.createIBO();
	}
}
