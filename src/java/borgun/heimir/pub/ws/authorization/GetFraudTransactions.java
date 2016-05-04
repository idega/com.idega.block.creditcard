
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getFraudTransactions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFraudTransactions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FraudRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFraudTransactions", propOrder = {
    "fraudRequestXML"
})
public class GetFraudTransactions {

    @XmlElement(name = "FraudRequestXML", required = true, nillable = true)
    protected String fraudRequestXML;

    /**
     * Gets the value of the fraudRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFraudRequestXML() {
        return fraudRequestXML;
    }

    /**
     * Sets the value of the fraudRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFraudRequestXML(String value) {
        this.fraudRequestXML = value;
    }

}
