
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SyndarkortnumerSkilabod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyndarkortnumerSkilabod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.valitor.is/Fyrirtaekjagreidslur/}Skilabod">
 *       &lt;sequence>
 *         &lt;element name="Syndarkortnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyndarkortnumerSkilabod", propOrder = {
    "syndarkortnumer"
})
public class SyndarkortnumerSkilabod
    extends Skilabod
{

    @XmlElement(name = "Syndarkortnumer")
    protected String syndarkortnumer;

    /**
     * Gets the value of the syndarkortnumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyndarkortnumer() {
        return syndarkortnumer;
    }

    /**
     * Sets the value of the syndarkortnumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyndarkortnumer(String value) {
        this.syndarkortnumer = value;
    }

}
