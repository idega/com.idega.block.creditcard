
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import is.valitor.greidslugatt.TegundKorts;


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
 *         &lt;element name="notandanafn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lykilord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="samningsnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="samningsKennitala" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="posiID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tegundKorts" type="{http://greidslugatt.valitor.is}TegundKorts"/>
 *         &lt;element name="syndarDebetNumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kennitalaKorthafa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="upphaed" type="{http://www.w3.org/2001/XMLSchema}double"/>
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
    "notandanafn",
    "lykilord",
    "samningsnumer",
    "samningsKennitala",
    "posiID",
    "tegundKorts",
    "syndarDebetNumer",
    "kennitalaKorthafa",
    "upphaed"
})
@XmlRootElement(name = "GreidaMedDebetkorti")
public class GreidaMedDebetkorti {

    protected String notandanafn;
    protected String lykilord;
    protected String samningsnumer;
    protected String samningsKennitala;
    protected int posiID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TegundKorts tegundKorts;
    protected String syndarDebetNumer;
    protected String kennitalaKorthafa;
    protected double upphaed;

    /**
     * Gets the value of the notandanafn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotandanafn() {
        return notandanafn;
    }

    /**
     * Sets the value of the notandanafn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotandanafn(String value) {
        this.notandanafn = value;
    }

    /**
     * Gets the value of the lykilord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLykilord() {
        return lykilord;
    }

    /**
     * Sets the value of the lykilord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLykilord(String value) {
        this.lykilord = value;
    }

    /**
     * Gets the value of the samningsnumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamningsnumer() {
        return samningsnumer;
    }

    /**
     * Sets the value of the samningsnumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamningsnumer(String value) {
        this.samningsnumer = value;
    }

    /**
     * Gets the value of the samningsKennitala property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamningsKennitala() {
        return samningsKennitala;
    }

    /**
     * Sets the value of the samningsKennitala property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamningsKennitala(String value) {
        this.samningsKennitala = value;
    }

    /**
     * Gets the value of the posiID property.
     * 
     */
    public int getPosiID() {
        return posiID;
    }

    /**
     * Sets the value of the posiID property.
     * 
     */
    public void setPosiID(int value) {
        this.posiID = value;
    }

    /**
     * Gets the value of the tegundKorts property.
     * 
     * @return
     *     possible object is
     *     {@link TegundKorts }
     *     
     */
    public TegundKorts getTegundKorts() {
        return tegundKorts;
    }

    /**
     * Sets the value of the tegundKorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link TegundKorts }
     *     
     */
    public void setTegundKorts(TegundKorts value) {
        this.tegundKorts = value;
    }

    /**
     * Gets the value of the syndarDebetNumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyndarDebetNumer() {
        return syndarDebetNumer;
    }

    /**
     * Sets the value of the syndarDebetNumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyndarDebetNumer(String value) {
        this.syndarDebetNumer = value;
    }

    /**
     * Gets the value of the kennitalaKorthafa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKennitalaKorthafa() {
        return kennitalaKorthafa;
    }

    /**
     * Sets the value of the kennitalaKorthafa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKennitalaKorthafa(String value) {
        this.kennitalaKorthafa = value;
    }

    /**
     * Gets the value of the upphaed property.
     * 
     */
    public double getUpphaed() {
        return upphaed;
    }

    /**
     * Sets the value of the upphaed property.
     * 
     */
    public void setUpphaed(double value) {
        this.upphaed = value;
    }

}
