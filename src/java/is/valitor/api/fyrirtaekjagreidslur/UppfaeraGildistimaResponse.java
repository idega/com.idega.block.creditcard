
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
 *         &lt;element name="UppfaeraGildistimaResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}Skilabod" minOccurs="0"/>
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
    "uppfaeraGildistimaResult"
})
@XmlRootElement(name = "UppfaeraGildistimaResponse")
public class UppfaeraGildistimaResponse {

    @XmlElement(name = "UppfaeraGildistimaResult")
    protected Skilabod uppfaeraGildistimaResult;

    /**
     * Gets the value of the uppfaeraGildistimaResult property.
     * 
     * @return
     *     possible object is
     *     {@link Skilabod }
     *     
     */
    public Skilabod getUppfaeraGildistimaResult() {
        return uppfaeraGildistimaResult;
    }

    /**
     * Sets the value of the uppfaeraGildistimaResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Skilabod }
     *     
     */
    public void setUppfaeraGildistimaResult(Skilabod value) {
        this.uppfaeraGildistimaResult = value;
    }

}
