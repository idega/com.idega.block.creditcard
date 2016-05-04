
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setBlacklistedCard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setBlacklistedCard">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BlacklistCardRequestXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setBlacklistedCard", propOrder = {
    "blacklistCardRequestXML"
})
public class SetBlacklistedCard {

    @XmlElement(name = "BlacklistCardRequestXML", required = true, nillable = true)
    protected String blacklistCardRequestXML;

    /**
     * Gets the value of the blacklistCardRequestXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlacklistCardRequestXML() {
        return blacklistCardRequestXML;
    }

    /**
     * Sets the value of the blacklistCardRequestXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlacklistCardRequestXML(String value) {
        this.blacklistCardRequestXML = value;
    }

}
