
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementChargeback complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementChargeback">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementChargebackRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementChargeback", propOrder = {
    "settlementChargebackRequestXML"
})
public class GetSettlementChargeback {

    @XmlElement(name = "SettlementChargebackRequestXML", required = true, nillable = true)
    protected String settlementChargebackRequestXML;

    /**
     * Gets the value of the settlementChargebackRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementChargebackRequestXML() {
        return settlementChargebackRequestXML;
    }

    /**
     * Sets the value of the settlementChargebackRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementChargebackRequestXML(String value) {
        this.settlementChargebackRequestXML = value;
    }

}
