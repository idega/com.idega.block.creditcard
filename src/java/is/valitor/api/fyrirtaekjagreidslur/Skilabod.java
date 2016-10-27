
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Skilabod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Skilabod">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Villunumer" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Villuskilabod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VilluLogID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Skilabod", propOrder = {
    "villunumer",
    "villuskilabod",
    "villuLogID"
})
@XmlSeeAlso({
    HeimildSkilabod.class,
    SyndarkortnumerSkilabod.class,
    KortnumerUtFraSyndarkortnumeriSkilabod.class
})
public class Skilabod {

    @XmlElement(name = "Villunumer")
    protected int villunumer;
    @XmlElement(name = "Villuskilabod")
    protected String villuskilabod;
    @XmlElement(name = "VilluLogID")
    protected long villuLogID;

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
     * Gets the value of the villuLogID property.
     * 
     */
    public long getVilluLogID() {
        return villuLogID;
    }

    /**
     * Sets the value of the villuLogID property.
     * 
     */
    public void setVilluLogID(long value) {
        this.villuLogID = value;
    }

}
