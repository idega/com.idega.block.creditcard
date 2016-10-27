
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
 *         &lt;element name="UppfaeraDebetkortaGildistimaResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}Skilabod" minOccurs="0"/>
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
    "uppfaeraDebetkortaGildistimaResult"
})
@XmlRootElement(name = "UppfaeraDebetkortaGildistimaResponse")
public class UppfaeraDebetkortaGildistimaResponse {

    @XmlElement(name = "UppfaeraDebetkortaGildistimaResult")
    protected Skilabod uppfaeraDebetkortaGildistimaResult;

    /**
     * Gets the value of the uppfaeraDebetkortaGildistimaResult property.
     * 
     * @return
     *     possible object is
     *     {@link Skilabod }
     *     
     */
    public Skilabod getUppfaeraDebetkortaGildistimaResult() {
        return uppfaeraDebetkortaGildistimaResult;
    }

    /**
     * Sets the value of the uppfaeraDebetkortaGildistimaResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Skilabod }
     *     
     */
    public void setUppfaeraDebetkortaGildistimaResult(Skilabod value) {
        this.uppfaeraDebetkortaGildistimaResult = value;
    }

}
