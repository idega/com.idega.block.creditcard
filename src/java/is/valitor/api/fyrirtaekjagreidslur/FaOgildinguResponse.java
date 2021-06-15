
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
 *         &lt;element name="FaOgildinguResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}HeimildSkilabod" minOccurs="0"/>
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
    "faOgildinguResult"
})
@XmlRootElement(name = "FaOgildinguResponse")
public class FaOgildinguResponse {

    @XmlElement(name = "FaOgildinguResult")
    protected HeimildSkilabod faOgildinguResult;

    /**
     * Gets the value of the faOgildinguResult property.
     * 
     * @return
     *     possible object is
     *     {@link HeimildSkilabod }
     *     
     */
    public HeimildSkilabod getFaOgildinguResult() {
        return faOgildinguResult;
    }

    /**
     * Sets the value of the faOgildinguResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeimildSkilabod }
     *     
     */
    public void setFaOgildinguResult(HeimildSkilabod value) {
        this.faOgildinguResult = value;
    }

}
