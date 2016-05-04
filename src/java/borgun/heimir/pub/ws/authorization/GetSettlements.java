
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlements complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlements">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="settlementRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlements", propOrder = {
    "settlementRequestXML"
})
public class GetSettlements {

    @XmlElement(required = true, nillable = true)
    protected String settlementRequestXML;

    /**
     * Gets the value of the settlementRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementRequestXML() {
        return settlementRequestXML;
    }

    /**
     * Sets the value of the settlementRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementRequestXML(String value) {
        this.settlementRequestXML = value;
    }

}
