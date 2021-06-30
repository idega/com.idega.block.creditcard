package com.idega.block.creditcard.business;

import java.sql.Timestamp;
import java.util.Collection;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard.model.CaptureResult;

/**
 * @author gimmi
 */
/**
 * @author gimmi
 */
public interface CreditCardClient {

	/**
	 * Returns the datestring as it is used by the client.
	 * @param month Expire month, formatted like this 08
	 * @param year Expire year, formatted like this 09
	 * @return
	 */
	public String getExpireDateString(String month, String year);

	/**
	 * Returns a collection of valid card types.
	 * The collection contains String declared in CreditCardBusiness.
	 * @return Collection of Card Types that client supports
	 */
	public Collection getValidCardTypes();

	/**
	 * This method returns the CreditCardMerchant used by this client.
	 * The Merchant contains the data that the clients uses to connect with.
	 * @return CreditCardMerchant
	 */
	public CreditCardMerchant getCreditCardMerchant();

	/**
	 * Tries to get refund the amount from the card.
	 *
	 * @param nameOnCard Name on card
	 * @param cardnumber Card number, should only contain numbers. Example 1234123412341234
	 * @param monthExpires Expire date MONTH, example 06
	 * @param yearExpires	 Expire date YEAR, example 05
	 * @param ccVerifyNumber	Creditcard verification code, example 123
	 * @param amount	Amount
	 * @param currency	Currency
	 * @param referenceNumber	Reference number
	 * @return Creditcard Authorization Number
	 * @throws CreditCardAuthorizationException
	 */
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, Object parentDataPK, String extraField) throws CreditCardAuthorizationException;

	/**
	 * Tries to get deduct the amount from the card.
	 *
	 * @param nameOnCard Name on card
	 * @param cardnumber Card number, should only contain numbers. Example 1234123412341234
	 * @param monthExpires Expire date MONTH, example 06
	 * @param yearExpires	 Expire date YEAR, example 05
	 * @param ccVerifyNumber	Creditcard verification code, example 123
	 * @param amount	Amount
	 * @param currency	Currency
	 * @param referenceNumber	Reference number
	 * @return Creditcard Authorization Number
	 * @throws CreditCardAuthorizationException
	 */
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException;


	/**
	 * Checks for authorization without actually doing the transaction
	 * @param nameOnCard Name on card
	 * @param cardnumber Card number, should only contain numbers. Example 1234123412341234
	 * @param monthExpires Expire date MONTH, example 06
	 * @param yearExpires	 Expire date YEAR, example 05
	 * @param ccVerifyNumber	Creditcard verification code, example 123
	 * @param amount	Amount
	 * @param currency	Currency
	 * @param referenceNumber	Reference number
	 * @return Returns properties needed to finishTransaction later
	 * @throws CreditCardAuthorizationException
	 */
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException;


	/**
	 * Finishes a previously authorized transaciton
	 * @param properties Properties gotten from creditcardAuthorization
	 * @throws CreditCardAuthorizationException
	 * @return Authorization number
	 */
	public String finishTransaction(String properties) throws CreditCardAuthorizationException;

	/**
	 * Finishes a previously authorized transaciton
	 * @param properties Properties gotten from creditcardAuthorization
	 * @throws CreditCardAuthorizationException
	 * @return Authorization number
	 */
	public String voidTransaction(String properties) throws CreditCardAuthorizationException;


	/**
	 * Checks if the client supports delayed transaction. (creditcardAuthorization() and finishTransaction())
	 * @return
	 */
	public boolean supportsDelayedTransactions();

	public CreditCardAuthorizationEntry getAuthorizationEntry();

	public void setAuthorizationEntry(CreditCardAuthorizationEntry entry);

	public String getAuthorizationNumber(String properties) throws CreditCardAuthorizationException;

	public String getPropertiesToCaptureWebPayment(String currency, double amount, Timestamp timestamp, String reference, String approvalCode) throws CreditCardAuthorizationException;

	public CaptureResult getAuthorizationNumberForWebPayment(String properties) throws CreditCardAuthorizationException;

	public AuthEntryData doSaleWithCardToken(
			String cardToken,
			String transactionId,
			double amount,
			String currency,
			String referenceNumber,
			Object parentPaymentPK
	) throws CreditCardAuthorizationException;

}