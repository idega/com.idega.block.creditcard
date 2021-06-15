
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KortnumerUtFraSyndarkortnumeriSkilabod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KortnumerUtFraSyndarkortnumeriSkilabod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.valitor.is/Fyrirtaekjagreidslur/}Skilabod">
 *       &lt;sequence>
 *         &lt;element name="Kortnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KortnumerUtFraSyndarkortnumeriSkilabod", propOrder = {
    "kortnumer"
})
public class KortnumerUtFraSyndarkortnumeriSkilabod
    extends Skilabod
{

    @XmlElement(name = "Kortnumer")
    protected String kortnumer;

    /**
     * Gets the value of the kortnumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKortnumer() {
        return kortnumer;
    }

    /**
     * Sets the value of the kortnumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKortnumer(String value) {
        this.kortnumer = value;
    }

}
