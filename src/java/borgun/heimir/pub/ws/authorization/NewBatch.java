
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for newBatch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="newBatch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newBatchReqXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newBatch", propOrder = {
    "newBatchReqXml"
})
public class NewBatch {

    @XmlElement(required = true, nillable = true)
    protected String newBatchReqXml;

    /**
     * Gets the value of the newBatchReqXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewBatchReqXml() {
        return newBatchReqXml;
    }

    /**
     * Sets the value of the newBatchReqXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewBatchReqXml(String value) {
        this.newBatchReqXml = value;
    }

}
