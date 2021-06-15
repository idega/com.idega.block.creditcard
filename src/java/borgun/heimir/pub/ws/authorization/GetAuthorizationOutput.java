
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAuthorizationOutput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAuthorizationOutput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getAuthResXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAuthorizationOutput", propOrder = {
    "getAuthResXml"
})
public class GetAuthorizationOutput {

    @XmlElement(required = true, nillable = true)
    protected String getAuthResXml;

    /**
     * Gets the value of the getAuthResXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAuthResXml() {
        return getAuthResXml;
    }

    /**
     * Sets the value of the getAuthResXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAuthResXml(String value) {
        this.getAuthResXml = value;
    }

}
