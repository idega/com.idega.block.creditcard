package com.idega.block.creditcard.model;

import java.io.Serializable;

import com.idega.util.CoreConstants;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;

public class ValitorPayVirtualCardAdditionalData implements Serializable {

	private static final long serialVersionUID = -581075863315686568L;

	private String merchantReferenceData;

	public ValitorPayVirtualCardAdditionalData() {
		super();
	}

	public ValitorPayVirtualCardAdditionalData(
			String merchantReferenceData
	) {
		this();

		if (!StringUtil.isEmpty(merchantReferenceData)) {
			merchantReferenceData = StringHandler.replace(merchantReferenceData, CoreConstants.SPACE, CoreConstants.EMPTY);
			merchantReferenceData = StringHandler.stripNonRomanCharacters(merchantReferenceData, new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
			merchantReferenceData = StringHandler.replace(merchantReferenceData, CoreConstants.MINUS, CoreConstants.EMPTY);
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