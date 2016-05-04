
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for newBatchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="newBatchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newBatchResXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newBatchResponse", propOrder = {
    "newBatchResXml"
})
public class NewBatchResponse {

    @XmlElement(required = true, nillable = true)
    protected String newBatchResXml;

    /**
     * Gets the value of the newBatchResXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewBatchResXml() {
        return newBatchResXml;
    }

    /**
     * Sets the value of the newBatchResXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewBatchResXml(String value) {
        this.newBatchResXml = value;
    }

}
