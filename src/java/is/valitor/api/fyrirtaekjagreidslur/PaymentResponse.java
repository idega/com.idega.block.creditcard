
package is.valitor.api.fyrirtaekjagreidslur;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageUTC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MerchantXID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TerminalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VirtualCardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CardExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaskedCardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CardVR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TransactionCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentResponse", propOrder = {
    "messageType",
    "messageUTC",
    "messageID",
    "responseCode",
    "responseSource",
    "responseText",
    "merchantXID",
    "terminalID",
    "virtualCardNumber",
    "cardExpirationDate",
    "maskedCardNumber",
    "cardVR",
    "transactionAmount",
    "transactionCurrency",
    "transactionID",
    "authenticationCode"
})
public class PaymentResponse {

    @XmlElement(name = "MessageType")
    protected String messageType;
    @XmlElement(name = "MessageUTC")
    protected String messageUTC;
    @XmlElement(name = "MessageID")
    protected String messageID;
    @XmlElement(name = "ResponseCode")
    protected String responseCode;
    @XmlElement(name = "ResponseSource")
    protected String responseSource;
    @XmlElement(name = "ResponseText")
    protected String responseText;
    @XmlElement(name = "MerchantXID")
    protected String merchantXID;
    @XmlElement(name = "TerminalID")
    protected String terminalID;
    @XmlElement(name = "VirtualCardNumber")
    protected String virtualCardNumber;
    @XmlElement(name = "CardExpirationDate")
    protected String cardExpirationDate;
    @XmlElement(name = "MaskedCardNumber")
    protected String maskedCardNumber;
    @XmlElement(name = "CardVR")
    protected String cardVR;
    @XmlElement(name = "TransactionAmount", required = true)
    protected BigDecimal transactionAmount;
    @XmlElement(name = "TransactionCurrency")
    protected String transactionCurrency;
    @XmlElement(name = "TransactionID")
    protected String transactionID;
    @XmlElement(name = "AuthenticationCode")
    protected String authenticationCode;

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the messageUTC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageUTC() {
        return messageUTC;
    }

    /**
     * Sets the value of the messageUTC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageUTC(String value) {
        this.messageUTC = value;
    }

    /**
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseSource() {
        return responseSource;
    }

    /**
     * Sets the value of the responseSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseSource(String value) {
        this.responseSource = value;
    }

    /**
     * Gets the value of the responseText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Sets the value of the responseText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseText(String value) {
        this.responseText = value;
    }

    /**
     * Gets the value of the merchantXID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchantXID() {
        return merchantXID;
    }

    /**
     * Sets the value of the merchantXID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchantXID(String value) {
        this.merchantXID = value;
    }

    /**
     * Gets the value of the terminalID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalID() {
        return terminalID;
    }

    /**
     * Sets the value of the terminalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalID(String value) {
        this.terminalID = value;
    }

    /**
     * Gets the value of the virtualCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualCardNumber() {
        return virtualCardNumber;
    }

    /**
     * Sets the value of the virtualCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualCardNumber(String value) {
        this.virtualCardNumber = value;
    }

    /**
     * Gets the value of the cardExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    /**
     * Sets the value of the cardExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardExpirationDate(String value) {
        this.cardExpirationDate = value;
    }

    /**
     * Gets the value of the maskedCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    /**
     * Sets the value of the maskedCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskedCardNumber(String value) {
        this.maskedCardNumber = value;
    }

    /**
     * Gets the value of the cardVR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardVR() {
        return cardVR;
    }

    /**
     * Sets the value of the cardVR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardVR(String value) {
        this.cardVR = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransactionAmount(BigDecimal value) {
        this.transactionAmount = value;
    }

    /**
     * Gets the value of the transactionCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    /**
     * Sets the value of the transactionCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionCurrency(String value) {
        this.transactionCurrency = value;
    }

    /**
     * Gets the value of the transactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the authenticationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    /**
     * Sets the value of the authenticationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationCode(String value) {
        this.authenticationCode = value;
    }

}
