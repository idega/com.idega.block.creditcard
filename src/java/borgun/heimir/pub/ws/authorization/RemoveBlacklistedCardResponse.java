
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for removeBlacklistedCardResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="removeBlacklistedCardResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BlacklistCardRemovalResponseXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeBlacklistedCardResponse", propOrder = {
    "blacklistCardRemovalResponseXML"
})
public class RemoveBlacklistedCardResponse {

    @XmlElement(name = "BlacklistCardRemovalResponseXML", required = true, nillable = true)
    protected String blacklistCardRemovalResponseXML;

    /**
     * Gets the value of the blacklistCardRemovalResponseXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlacklistCardRemovalResponseXML() {
        return blacklistCardRemovalResponseXML;
    }

    /**
     * Sets the value of the blacklistCardRemovalResponseXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlacklistCardRemovalResponseXML(String value) {
        this.blacklistCardRemovalResponseXML = value;
    }

}
