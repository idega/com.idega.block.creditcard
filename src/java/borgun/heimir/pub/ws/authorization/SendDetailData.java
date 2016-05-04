
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendDetailData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendDetailData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="detailDataXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendDetailData", propOrder = {
    "detailDataXML"
})
public class SendDetailData {

    @XmlElement(required = true, nillable = true)
    protected String detailDataXML;

    /**
     * Gets the value of the detailDataXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailDataXML() {
        return detailDataXML;
    }

    /**
     * Sets the value of the detailDataXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailDataXML(String value) {
        this.detailDataXML = value;
    }

}
