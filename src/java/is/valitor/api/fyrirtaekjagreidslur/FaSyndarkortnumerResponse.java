
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
 *         &lt;element name="FaSyndarkortnumerResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}SyndarkortnumerSkilabod" minOccurs="0"/>
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
    "faSyndarkortnumerResult"
})
@XmlRootElement(name = "FaSyndarkortnumerResponse")
public class FaSyndarkortnumerResponse {

    @XmlElement(name = "FaSyndarkortnumerResult")
    protected SyndarkortnumerSkilabod faSyndarkortnumerResult;

    /**
     * Gets the value of the faSyndarkortnumerResult property.
     * 
     * @return
     *     possible object is
     *     {@link SyndarkortnumerSkilabod }
     *     
     */
    public SyndarkortnumerSkilabod getFaSyndarkortnumerResult() {
        return faSyndarkortnumerResult;
    }

    /**
     * Sets the value of the faSyndarkortnumerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SyndarkortnumerSkilabod }
     *     
     */
    public void setFaSyndarkortnumerResult(SyndarkortnumerSkilabod value) {
        this.faSyndarkortnumerResult = value;
    }

}
