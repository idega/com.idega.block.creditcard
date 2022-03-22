package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.util.CoreConstants;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;

public class ValitorPayPaymentAdditionalData implements Serializable {
	private static final long serialVersionUID = 2295908197936916470L;

	private String merchantReferenceData; //EXAMPLE:  "00000000-0000-0000-0000-000000000000",

	public ValitorPayPaymentAdditionalData() {
		super();
	}

	public ValitorPayPaymentAdditionalData(
			String merchantReferenceData
	) {
		this();

		if (!StringUtil.isEmpty(merchantReferenceData)) {
			merchantReferenceData = StringHandler.replace(merchantReferenceData, CoreConstants.SPACE, CoreConstants.EMPTY);
			merchantReferenceData = StringHandler.stripNonRomanCharacters(merchantReferenceData, new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
			if (merchantReferenceData.length() > 50) {
				merchantReferenceData = merchantReferenceData.substring(0, 50);
			}
		}

		this.merchantReferenceData = merchantReferenceData;
	}

	public String getMerchantReferenceData() {
		return merchantReferenceData;
	}

	public void setMerchantReferenceData(String merchantReferenceData) {
		this.merchantReferenceData = merchantReferenceData;
	}

}