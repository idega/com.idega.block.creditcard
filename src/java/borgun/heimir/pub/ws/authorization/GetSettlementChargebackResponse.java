
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementChargebackResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementChargebackResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementChargebackResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementChargebackResponse", propOrder = {
    "settlementChargebackResponseXML"
})
public class GetSettlementChargebackResponse {

    @XmlElement(name = "SettlementChargebackResponseXML", required = true, nillable = true)
    protected String settlementChargebackResponseXML;

    /**
     * Gets the value of the settlementChargebackResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementChargebackResponseXML() {
        return settlementChargebackResponseXML;
    }

    /**
     * Sets the value of the settlementChargebackResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementChargebackResponseXML(String value) {
        this.settlementChargebackResponseXML = value;
    }

}
