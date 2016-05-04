
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getActionCodeTextsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getActionCodeTextsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActionCodeTextsXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getActionCodeTextsResponse", propOrder = {
    "actionCodeTextsXML"
})
public class GetActionCodeTextsResponse {

    @XmlElement(name = "ActionCodeTextsXML", required = true, nillable = true)
    protected String actionCodeTextsXML;

    /**
     * Gets the value of the actionCodeTextsXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionCodeTextsXML() {
        return actionCodeTextsXML;
    }

    /**
     * Sets the value of the actionCodeTextsXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionCodeTextsXML(String value) {
        this.actionCodeTextsXML = value;
    }

}
