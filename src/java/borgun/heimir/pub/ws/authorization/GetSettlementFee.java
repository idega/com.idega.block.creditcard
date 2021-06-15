
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementFee complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementFee">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementIsoFeeRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementFee", propOrder = {
    "settlementIsoFeeRequestXML"
})
public class GetSettlementFee {

    @XmlElement(name = "SettlementIsoFeeRequestXML", required = true, nillable = true)
    protected String settlementIsoFeeRequestXML;

    /**
     * Gets the value of the settlementIsoFeeRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementIsoFeeRequestXML() {
        return settlementIsoFeeRequestXML;
    }

    /**
     * Sets the value of the settlementIsoFeeRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementIsoFeeRequestXML(String value) {
        this.settlementIsoFeeRequestXML = value;
    }

}
