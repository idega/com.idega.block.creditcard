/*
 * $Id: TPosException.java,v 1.2 2006/06/20 11:01:19 gimmi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.business;

import java.util.logging.Logger;

import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.CoreUtil;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class TPosException extends CreditCardAuthorizationException {

	private static final long serialVersionUID = 4365805472197877086L;

  public TPosException() {
    super();
  }

  public TPosException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public TPosException(String message, Throwable cause) {
  	super(message, cause);
  }

  /**
   * @param cause
   */
  public TPosException(Throwable cause) {
  	super(cause);
  }

  @Override
public String getLocalizedMessage(IWResourceBundle iwrb) {
		String message = "TPOS errormessage = " + this.getErrorMessage() + ", number = " + this.getErrorNumber() + ", display = " + this.getDisplayError();
		Logger.getLogger(getClass().getName()).warning(message);
		if (IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("credit_card.report_exceptions", false)) {
			CoreUtil.sendExceptionNotification(message, this);
		}

		int number = Integer.parseInt(this.getErrorNumber());
		switch (number) {
			case 6:
			case 12:
			case 19:
				return (iwrb.getLocalizedString("travel.creditcard_number_incorrect","Creditcard number incorrect"));
			case 10:
			case 22:
			case 74:
				return (iwrb.getLocalizedString("travel.creditcard_type_not_accepted","Creditcard type not accepted"));
			case 17:
			case 18:
				return (iwrb.getLocalizedString("travel.creditcard_is_expired","Creditcard is expired"));
			case 48:
			case 49:
			case 50:
			case 51:
			case 56:
			case 57:
			case 76:
			case 79:
			case 2002:
			case 2010:
				return (iwrb.getLocalizedString("travel.cannot_connect_to_cps","Could not connect to Central Payment Server"));
			case 7:
			case 37:
			case 69:
			case 75:
				return (iwrb.getLocalizedString("travel.creditcard_autorization_failed","Authorization failed"));
			case 20:
			case 31:
				return (iwrb.getLocalizedString("travel.transaction_not_permitted","Transaction not permitted"));
			case 99999:
				return (iwrb.getLocalizedString("travel.booking_was_not_confirmed_try_again_later","Booking was not confirmed. Please try again later"));
			default:
				return (iwrb.getLocalizedString("travel.error_communicating","Error communicating with Central Payment Server"));
		}
  }
}
