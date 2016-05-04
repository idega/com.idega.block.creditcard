
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAuthorizationInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAuthorizationInput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getAuthReqXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAuthorizationInput", propOrder = {
    "getAuthReqXml"
})
public class GetAuthorizationInput {

    @XmlElement(required = true, nillable = true)
    protected String getAuthReqXml;

    /**
     * Gets the value of the getAuthReqXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAuthReqXml() {
        return getAuthReqXml;
    }

    /**
     * Sets the value of the getAuthReqXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAuthReqXml(String value) {
        this.getAuthReqXml = value;
    }

}
