
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDisputedTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDisputedTransactionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DisputedTransactionResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDisputedTransactionsResponse", propOrder = {
    "disputedTransactionResponseXML"
})
public class GetDisputedTransactionsResponse {

    @XmlElement(name = "DisputedTransactionResponseXML", required = true, nillable = true)
    protected String disputedTransactionResponseXML;

    /**
     * Gets the value of the disputedTransactionResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisputedTransactionResponseXML() {
        return disputedTransactionResponseXML;
    }

    /**
     * Sets the value of the disputedTransactionResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisputedTransactionResponseXML(String value) {
        this.disputedTransactionResponseXML = value;
    }

}
