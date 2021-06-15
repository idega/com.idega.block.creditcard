
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
 *         &lt;element name="Notandanafn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Lykilord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SamningsKennitala" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Syndarkortnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NyrGildistimi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Stillingar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "samningsKennitala",
    "syndarkortnumer",
    "nyrGildistimi",
    "stillingar"
})
@XmlRootElement(name = "UppfaeraGildistima")
public class UppfaeraGildistima {

    @XmlElement(name = "Notandanafn")
    protected String notandanafn;
    @XmlElement(name = "Lykilord")
    protected String lykilord;
    @XmlElement(name = "SamningsKennitala")
    protected String samningsKennitala;
    @XmlElement(name = "Syndarkortnumer")
    protected String syndarkortnumer;
    @XmlElement(name = "NyrGildistimi")
    protected String nyrGildistimi;
    @XmlElement(name = "Stillingar")
    protected String stillingar;

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

    /**
     * Gets the value of the nyrGildistimi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNyrGildistimi() {
        return nyrGildistimi;
    }

    /**
     * Sets the value of the nyrGildistimi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNyrGildistimi(String value) {
        this.nyrGildistimi = value;
    }

    /**
     * Gets the value of the stillingar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStillingar() {
        return stillingar;
    }

    /**
     * Sets the value of the stillingar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStillingar(String value) {
        this.stillingar = value;
    }

}
