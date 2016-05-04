
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTransactionListResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getTransactionListResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionListXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTransactionListResponse", propOrder = {
    "transactionListXML"
})
public class GetTransactionListResponse {

    @XmlElement(required = true, nillable = true)
    protected String transactionListXML;

    /**
     * Gets the value of the transactionListXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionListXML() {
        return transactionListXML;
    }

    /**
     * Sets the value of the transactionListXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionListXML(String value) {
        this.transactionListXML = value;
    }

}
