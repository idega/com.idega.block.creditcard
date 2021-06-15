
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getFraudTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFraudTransactionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FraudTransactionResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFraudTransactionsResponse", propOrder = {
    "fraudTransactionResponseXML"
})
public class GetFraudTransactionsResponse {

    @XmlElement(name = "FraudTransactionResponseXML", required = true, nillable = true)
    protected String fraudTransactionResponseXML;

    /**
     * Gets the value of the fraudTransactionResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFraudTransactionResponseXML() {
        return fraudTransactionResponseXML;
    }

    /**
     * Sets the value of the fraudTransactionResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFraudTransactionResponseXML(String value) {
        this.fraudTransactionResponseXML = value;
    }

}
