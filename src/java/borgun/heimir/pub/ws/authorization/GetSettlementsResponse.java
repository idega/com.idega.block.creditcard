
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSettlementsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSettlementsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SettlementsXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettlementsResponse", propOrder = {
    "settlementsXML"
})
public class GetSettlementsResponse {

    @XmlElement(name = "SettlementsXML", required = true, nillable = true)
    protected String settlementsXML;

    /**
     * Gets the value of the settlementsXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementsXML() {
        return settlementsXML;
    }

    /**
     * Sets the value of the settlementsXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementsXML(String value) {
        this.settlementsXML = value;
    }

}
