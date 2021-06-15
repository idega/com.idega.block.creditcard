
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
 *         &lt;element name="GreidaMedDebetkortiResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}DebitSvar" minOccurs="0"/>
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
    "greidaMedDebetkortiResult"
})
@XmlRootElement(name = "GreidaMedDebetkortiResponse")
public class GreidaMedDebetkortiResponse {

    @XmlElement(name = "GreidaMedDebetkortiResult")
    protected DebitSvar greidaMedDebetkortiResult;

    /**
     * Gets the value of the greidaMedDebetkortiResult property.
     * 
     * @return
     *     possible object is
     *     {@link DebitSvar }
     *     
     */
    public DebitSvar getGreidaMedDebetkortiResult() {
        return greidaMedDebetkortiResult;
    }

    /**
     * Sets the value of the greidaMedDebetkortiResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DebitSvar }
     *     
     */
    public void setGreidaMedDebetkortiResult(DebitSvar value) {
        this.greidaMedDebetkortiResult = value;
    }

}
