
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for removeBlacklistedCard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="removeBlacklistedCard">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BlacklistCardRemovalRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeBlacklistedCard", propOrder = {
    "blacklistCardRemovalRequestXML"
})
public class RemoveBlacklistedCard {

    @XmlElement(name = "BlacklistCardRemovalRequestXML", required = true, nillable = true)
    protected String blacklistCardRemovalRequestXML;

    /**
     * Gets the value of the blacklistCardRemovalRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlacklistCardRemovalRequestXML() {
        return blacklistCardRemovalRequestXML;
    }

    /**
     * Sets the value of the blacklistCardRemovalRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlacklistCardRemovalRequestXML(String value) {
        this.blacklistCardRemovalRequestXML = value;
    }

}
