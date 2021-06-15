
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementTransactionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementTransactionsResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementTransactionsResponse", propOrder = {
    "settlementTransactionsResponseXML"
})
public class GetSettlementTransactionsResponse {

    @XmlElement(name = "SettlementTransactionsResponseXML", required = true, nillable = true)
    protected String settlementTransactionsResponseXML;

    /**
     * Gets the value of the settlementTransactionsResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementTransactionsResponseXML() {
        return settlementTransactionsResponseXML;
    }

    /**
     * Sets the value of the settlementTransactionsResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementTransactionsResponseXML(String value) {
        this.settlementTransactionsResponseXML = value;
    }

}
