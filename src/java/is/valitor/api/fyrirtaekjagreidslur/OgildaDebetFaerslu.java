
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="notandanafn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lykilord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="posiID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sidustuFjorirITekkaabyrgdarnumeri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="faerslunumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stillingar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "posiID",
    "sidustuFjorirITekkaabyrgdarnumeri",
    "faerslunumer",
    "stillingar"
})
@XmlRootElement(name = "OgildaDebetFaerslu")
public class OgildaDebetFaerslu {

    protected String notandanafn;
    protected String lykilord;
    protected String posiID;
    protected String sidustuFjorirITekkaabyrgdarnumeri;
    protected String faerslunumer;
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
     * Gets the value of the posiID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosiID() {
        return posiID;
    }

    /**
     * Sets the value of the posiID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosiID(String value) {
        this.posiID = value;
    }

    /**
     * Gets the value of the sidustuFjorirITekkaabyrgdarnumeri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSidustuFjorirITekkaabyrgdarnumeri() {
        return sidustuFjorirITekkaabyrgdarnumeri;
    }

    /**
     * Sets the value of the sidustuFjorirITekkaabyrgdarnumeri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSidustuFjorirITekkaabyrgdarnumeri(String value) {
        this.sidustuFjorirITekkaabyrgdarnumeri = value;
    }

    /**
     * Gets the value of the faerslunumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaerslunumer() {
        return faerslunumer;
    }

    /**
     * Sets the value of the faerslunumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaerslunumer(String value) {
        this.faerslunumer = value;
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
