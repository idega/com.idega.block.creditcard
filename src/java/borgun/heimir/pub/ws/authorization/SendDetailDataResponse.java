
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendDetailDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendDetailDataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="detailDataResXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendDetailDataResponse", propOrder = {
    "detailDataResXML"
})
public class SendDetailDataResponse {

    @XmlElement(required = true, nillable = true)
    protected String detailDataResXML;

    /**
     * Gets the value of the detailDataResXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailDataResXML() {
        return detailDataResXML;
    }

    /**
     * Sets the value of the detailDataResXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailDataResXML(String value) {
        this.detailDataResXML = value;
    }

}
