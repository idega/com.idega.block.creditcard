package com.idega.block.creditcard2.data.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.idega.block.creditcard2.business.CreditCardAuthorizationEntry;

@Entity
@Table(name  = TPosAuthorisationEntry.ENTITY_NAME)
@NamedQueries(
		{
			@NamedQuery(
					name = TPosAuthorisationEntry.GET_BY_ID,
					query = "from TPosAuthorisationEntry bae where bae."+TPosAuthorisationEntry.idProp+" = :"+TPosAuthorisationEntry.idProp
					),
			@NamedQuery(
					name = TPosAuthorisationEntry.GET_BY_PARENT_ID,
					query = "from TPosAuthorisationEntry bae where bae."+TPosAuthorisationEntry.parentProp+" = :"+TPosAuthorisationEntry.parentProp
					),
			@NamedQuery(
					name = TPosAuthorisationEntry.GET_BY_AUTH_CODE,
					query = "from TPosAuthorisationEntry bae where bae."+TPosAuthorisationEntry.authCodeProp+" = :"+TPosAuthorisationEntry.authCodeProp
					),
			@NamedQuery(
					name = TPosAuthorisationEntry.GET_BY_DATES,
					query = "from TPosAuthorisationEntry bae where bae."+TPosAuthorisationEntry.dateProp+" >= :"+TPosAuthorisationEntry.dateFromProp + " and " +TPosAuthorisationEntry.dateProp+ " <=:" +TPosAuthorisationEntry.dateToProp 
					),
			@NamedQuery(
					name = TPosAuthorisationEntry.GET_REFUNDS_BY_DATES,
					query = "from TPosAuthorisationEntry bae where bae.transactionType = 3 and bae."+TPosAuthorisationEntry.dateProp+" >= :"+TPosAuthorisationEntry.dateFromProp + " and " +TPosAuthorisationEntry.dateProp+ " <=:" +TPosAuthorisationEntry.dateToProp 
					)
		}
	)
public class TPosAuthorisationEntry implements CreditCardAuthorizationEntry{

	public static final String GET_BY_ID = "TPosAuthorisationEntry.GET_BY_ID";
	public static final String GET_BY_PARENT_ID = "TPosAuthorisationEntry.GET_BY_PARENT_ID";
	public static final String GET_BY_AUTH_CODE = "TPosAuthorisationEntry.GET_BY_AUTH_CODE";
	public static final String GET_BY_DATES = "TPosAuthorisationEntry.GET_BY_DATES";
	public static final String GET_REFUNDS_BY_DATES = "TPosAuthorisationEntry.GET_REFUNDS_BY_DATES";
	
	public static final String idProp = "id";
	public static final String parentProp = "parentId";
	public static final String authCodeProp = "authCode";
	public static final String dateProp = "entryDate";
	public static final String dateFromProp = "entryDateFrom";
	public static final String dateToProp = "entryDateTo";
	
	@Id
	@Column(name = "tpos_auth_entries_id")
	private Integer id;
	
	@Column(name = "auth_amount")
	private String amount;
	
	@Column(name ="auth_currency")
	private String currency;
	
	@Column(name = "auth_code")
	private String authCode;
	
	@Column(name = "auth_id_rsp")
	private String authId;
	
	@Column(name = "auth_path_res_code")
	private String authPathResCode;
	
	@Column(name ="batch_nr")
	private String batchNr;
	
	@Column (name = "card_brand_id")
	private String cardBrandId;
	
	@Column (name = "card_brand_name")
	private String cardBrandName;
	
	@Column (name ="card_caracter")
	private String cardCaracter;
	
	@Column (name = "card_type_id")
	private String cardTypeId;
	
	@Column (name = "card_type_name")
	private String cardTypeName;
	
	@Column (name ="entry_date")
	private String entryDate;
	
	@Column (name = "detail")
	private String detail;
	
	@Column (name = "error_nr")
	private String errorNr;
	
	@Column (name ="error_text")
	private String errorText;
	
	@Column(name ="card_expires")
	private String cardExpires;
	
	@Column(name ="location_nr")
	private String locationNr;
	
	@Column(name = "merc_auth")
	private String mercAuth;
	
	@Column(name = "merc_other")
	private String mercOther;
	
	@Column(name = "merc_sub")
	private String mercSub;
	
	@Column (name = "att_count")
	private String attCount;
	
	@Column (name="pan")
	private String pan;
	
	@Column (name="pos_nr")
	private String posNr;
	
	@Column(name="pos_ser_nr")
	private String posSerNr;
	
	@Column(name="print_data")
	private String printData;
	
	@Column(name="sub_amount")
	private String subAmount;
	
	@Column(name="sub_currency")
	private String subCurrency;
	
	@Column(name="entry_time")
	private String entryTime;
	
	@Column(name="rsp_code")
	private String rspCode;
	
	@Column(name="transact_nr")
	private String transactNr;
	
	@Column(name="void_rsp")
	private String voidRsp;
	
	@Column(name="void_trans")
	private String voidTrans;
	
	@Column(name="xml")
	private String xml;
	
	@Column(name="card_number")
	private String cardNumber;
	
	@Column(name="parent_id")
	private String parentId;
	
	
	public final static String ENTITY_NAME = "tpos_auth_entries";


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getAuthCode() {
		return authCode;
	}


	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}


	public String getAuthId() {
		return authId;
	}


	public void setAuthId(String authId) {
		this.authId = authId;
	}


	public String getAuthPathResCode() {
		return authPathResCode;
	}


	public void setAuthPathResCode(String authPathResCode) {
		this.authPathResCode = authPathResCode;
	}


	public String getBatchNr() {
		return batchNr;
	}


	public void setBatchNr(String batchNr) {
		this.batchNr = batchNr;
	}


	public String getCardBrandId() {
		return cardBrandId;
	}


	public void setCardBrandId(String cardBrandId) {
		this.cardBrandId = cardBrandId;
	}


	public String getCardBrandName() {
		return cardBrandName;
	}


	public void setCardBrandName(String cardBrandName) {
		this.cardBrandName = cardBrandName;
	}


	public String getCardCaracter() {
		return cardCaracter;
	}


	public void setCardCaracter(String cardCaracter) {
		this.cardCaracter = cardCaracter;
	}


	public String getCardTypeId() {
		return cardTypeId;
	}


	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}


	public String getCardTypeName() {
		return cardTypeName;
	}


	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}


	public String getEntryDate() {
		return entryDate;
	}


	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getErrorNr() {
		return errorNr;
	}


	public void setErrorNr(String errorNr) {
		this.errorNr = errorNr;
	}


	public String getErrorText() {
		return errorText;
	}


	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}


	public String getCardExpires() {
		return cardExpires;
	}


	public void setCardExpires(String cardExpires) {
		this.cardExpires = cardExpires;
	}


	public String getLocationNr() {
		return locationNr;
	}


	public void setLocationNr(String locationNr) {
		this.locationNr = locationNr;
	}


	public String getMercAuth() {
		return mercAuth;
	}


	public void setMercAuth(String mercAuth) {
		this.mercAuth = mercAuth;
	}


	public String getMercOther() {
		return mercOther;
	}


	public void setMercOther(String mercOther) {
		this.mercOther = mercOther;
	}


	public String getMercSub() {
		return mercSub;
	}


	public void setMercSub(String mercSub) {
		this.mercSub = mercSub;
	}


	public String getAttCount() {
		return attCount;
	}


	public void setAttCount(String attCount) {
		this.attCount = attCount;
	}


	public String getPan() {
		return pan;
	}


	public void setPan(String pan) {
		this.pan = pan;
	}


	public String getPosNr() {
		return posNr;
	}


	public void setPosNr(String posNr) {
		this.posNr = posNr;
	}


	public String getPosSerNr() {
		return posSerNr;
	}


	public void setPosSerNr(String posSerNr) {
		this.posSerNr = posSerNr;
	}


	public String getPrintData() {
		return printData;
	}


	public void setPrintData(String printData) {
		this.printData = printData;
	}


	public String getSubAmount() {
		return subAmount;
	}


	public void setSubAmount(String subAmount) {
		this.subAmount = subAmount;
	}


	public String getSubCurrency() {
		return subCurrency;
	}


	public void setSubCurrency(String subCurrency) {
		this.subCurrency = subCurrency;
	}


	public String getEntryTime() {
		return entryTime;
	}


	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}


	public String getRspCode() {
		return rspCode;
	}


	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}


	public String getTransactNr() {
		return transactNr;
	}


	public void setTransactNr(String transactNr) {
		this.transactNr = transactNr;
	}


	public String getVoidRsp() {
		return voidRsp;
	}


	public void setVoidRsp(String voidRsp) {
		this.voidRsp = voidRsp;
	}


	public String getVoidTrans() {
		return voidTrans;
	}


	public void setVoidTrans(String voidTrans) {
		this.voidTrans = voidTrans;
	}


	public String getXml() {
		return xml;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
