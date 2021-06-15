
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getTransactionList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionListReqXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTransactionList", propOrder = {
    "transactionListReqXML"
})
public class GetTransactionList {

    @XmlElement(required = true, nillable = true)
    protected String transactionListReqXML;

    /**
     * Gets the value of the transactionListReqXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionListReqXML() {
        return transactionListReqXML;
    }

    /**
     * Sets the value of the transactionListReqXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionListReqXML(String value) {
        this.transactionListReqXML = value;
    }

}
