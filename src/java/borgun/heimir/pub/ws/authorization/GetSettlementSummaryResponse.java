
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementSummaryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementSummaryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementSummaryResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementSummaryResponse", propOrder = {
    "settlementSummaryResponseXML"
})
public class GetSettlementSummaryResponse {

    @XmlElement(name = "SettlementSummaryResponseXML", required = true, nillable = true)
    protected String settlementSummaryResponseXML;

    /**
     * Gets the value of the settlementSummaryResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementSummaryResponseXML() {
        return settlementSummaryResponseXML;
    }

    /**
     * Sets the value of the settlementSummaryResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementSummaryResponseXML(String value) {
        this.settlementSummaryResponseXML = value;
    }

}
