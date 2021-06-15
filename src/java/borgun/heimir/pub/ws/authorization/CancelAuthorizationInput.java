
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cancelAuthorizationInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelAuthorizationInput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cancelAuthReqXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelAuthorizationInput", propOrder = {
    "cancelAuthReqXml"
})
public class CancelAuthorizationInput {

    @XmlElement(required = true, nillable = true)
    protected String cancelAuthReqXml;

    /**
     * Gets the value of the cancelAuthReqXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelAuthReqXml() {
        return cancelAuthReqXml;
    }

    /**
     * Sets the value of the cancelAuthReqXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelAuthReqXml(String value) {
        this.cancelAuthReqXml = value;
    }

}
