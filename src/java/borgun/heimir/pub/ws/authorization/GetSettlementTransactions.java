
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementTransactions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementTransactions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementTransactionRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementTransactions", propOrder = {
    "settlementTransactionRequestXML"
})
public class GetSettlementTransactions {

    @XmlElement(name = "SettlementTransactionRequestXML", required = true, nillable = true)
    protected String settlementTransactionRequestXML;

    /**
     * Gets the value of the settlementTransactionRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementTransactionRequestXML() {
        return settlementTransactionRequestXML;
    }

    /**
     * Sets the value of the settlementTransactionRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementTransactionRequestXML(String value) {
        this.settlementTransactionRequestXML = value;
    }

}
