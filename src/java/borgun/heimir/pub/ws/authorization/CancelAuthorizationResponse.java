
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cancelAuthorizationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelAuthorizationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cancelAuthResXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelAuthorizationResponse", propOrder = {
    "cancelAuthResXml"
})
public class CancelAuthorizationResponse {

    @XmlElement(required = true, nillable = true)
    protected String cancelAuthResXml;

    /**
     * Gets the value of the cancelAuthResXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelAuthResXml() {
        return cancelAuthResXml;
    }

    /**
     * Sets the value of the cancelAuthResXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelAuthResXml(String value) {
        this.cancelAuthResXml = value;
    }

}
