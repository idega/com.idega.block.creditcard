
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OgildaDebetFaersluResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}HeimildSkilabod" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ogildaDebetFaersluResult"
})
@XmlRootElement(name = "OgildaDebetFaersluResponse")
public class OgildaDebetFaersluResponse {

    @XmlElement(name = "OgildaDebetFaersluResult")
    protected HeimildSkilabod ogildaDebetFaersluResult;

    /**
     * Gets the value of the ogildaDebetFaersluResult property.
     * 
     * @return
     *     possible object is
     *     {@link HeimildSkilabod }
     *     
     */
    public HeimildSkilabod getOgildaDebetFaersluResult() {
        return ogildaDebetFaersluResult;
    }

    /**
     * Sets the value of the ogildaDebetFaersluResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeimildSkilabod }
     *     
     */
    public void setOgildaDebetFaersluResult(HeimildSkilabod value) {
        this.ogildaDebetFaersluResult = value;
    }

}
