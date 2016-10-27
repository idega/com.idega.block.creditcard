
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DebitSvar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DebitSvar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Villunumer" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Villuskilabod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Kvittun" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}Kvittun" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebitSvar", propOrder = {
    "villunumer",
    "villuskilabod",
    "kvittun"
})
public class DebitSvar {

    @XmlElement(name = "Villunumer")
    protected int villunumer;
    @XmlElement(name = "Villuskilabod")
    protected String villuskilabod;
    @XmlElement(name = "Kvittun")
    protected Kvittun kvittun;

    /**
     * Gets the value of the villunumer property.
     * 
     */
    public int getVillunumer() {
        return villunumer;
    }

    /**
     * Sets the value of the villunumer property.
     * 
     */
    public void setVillunumer(int value) {
        this.villunumer = value;
    }

    /**
     * Gets the value of the villuskilabod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVilluskilabod() {
        return villuskilabod;
    }

    /**
     * Sets the value of the villuskilabod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVilluskilabod(String value) {
        this.villuskilabod = value;
    }

    /**
     * Gets the value of the kvittun property.
     * 
     * @return
     *     possible object is
     *     {@link Kvittun }
     *     
     */
    public Kvittun getKvittun() {
        return kvittun;
    }

    /**
     * Sets the value of the kvittun property.
     * 
     * @param value
     *     allowed object is
     *     {@link Kvittun }
     *     
     */
    public void setKvittun(Kvittun value) {
        this.kvittun = value;
    }

}
