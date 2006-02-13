/*
 * $Id: CreditCardBusinessHome.java,v 1.4 2006/02/13 13:21:51 gimmi Exp $
 * Created on Feb 13, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.creditcard.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO gimmi Describe Type CreditCardBusinessHome
 * </p>
 *  Last modified: $Date: 2006/02/13 13:21:51 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface CreditCardBusinessHome extends IBOHome {

	public CreditCardBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
