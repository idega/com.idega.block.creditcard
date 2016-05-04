
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getVirtualCardResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getVirtualCardResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="virtualCardResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getVirtualCardResponse", propOrder = {
    "virtualCardResponseXML"
})
public class GetVirtualCardResponse {

    @XmlElement(required = true, nillable = true)
    protected String virtualCardResponseXML;

    /**
     * Gets the value of the virtualCardResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualCardResponseXML() {
        return virtualCardResponseXML;
    }

    /**
     * Sets the value of the virtualCardResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualCardResponseXML(String value) {
        this.virtualCardResponseXML = value;
    }

}
