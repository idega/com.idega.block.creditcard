
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setBlacklistedCardResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setBlacklistedCardResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BlacklistCardResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setBlacklistedCardResponse", propOrder = {
    "blacklistCardResponseXML"
})
public class SetBlacklistedCardResponse {

    @XmlElement(name = "BlacklistCardResponseXML", required = true, nillable = true)
    protected String blacklistCardResponseXML;

    /**
     * Gets the value of the blacklistCardResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlacklistCardResponseXML() {
        return blacklistCardResponseXML;
    }

    /**
     * Sets the value of the blacklistCardResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlacklistCardResponseXML(String value) {
        this.blacklistCardResponseXML = value;
    }

}
