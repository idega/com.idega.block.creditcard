
package is.valitor.api.fyrirtaekjagreidslur.serverkvittun;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Kvittun complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Kvittun">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VerslunNafn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VerslunHeimilisfang" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VerslunStadur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TegundKorts" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TegundKortsKodi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Dagsetning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Timi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Kortnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Upphaed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Faerslunumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Faersluhirdir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Heimildarnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StadsetningNumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UtstodNumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuidAdOgilda" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Bunkanumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Soluadilinumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Hugbunadarnumer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PosiID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PinSkilabod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Vidskiptaskilabod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="F22_1til4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaD1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinaD2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TegundAdgerd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaerslunumerUpphafleguFaerslu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TerminalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kvittun", propOrder = {
    "verslunNafn",
    "verslunHeimilisfang",
    "verslunStadur",
    "tegundKorts",
    "tegundKortsKodi",
    "dagsetning",
    "timi",
    "kortnumer",
    "upphaed",
    "faerslunumer",
    "faersluhirdir",
    "heimildarnumer",
    "stadsetningNumer",
    "utstodNumer",
    "buidAdOgilda",
    "bunkanumer",
    "soluadilinumer",
    "hugbunadarnumer",
    "posiID",
    "pinSkilabod",
    "vidskiptaskilabod",
    "f221Til4",
    "linaC1",
    "linaC2",
    "linaC3",
    "linaC4",
    "linaD1",
    "linaD2",
    "tegundAdgerd",
    "faerslunumerUpphafleguFaerslu",
    "terminalID"
})
public class Kvittun implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = -7891544058921277010L;
	@XmlElement(name = "VerslunNafn")
    protected String verslunNafn;
    @XmlElement(name = "VerslunHeimilisfang")
    protected String verslunHeimilisfang;
    @XmlElement(name = "VerslunStadur")
    protected String verslunStadur;
    @XmlElement(name = "TegundKorts")
    protected String tegundKorts;
    @XmlElement(name = "TegundKortsKodi")
    protected String tegundKortsKodi;
    @XmlElement(name = "Dagsetning")
    protected String dagsetning;
    @XmlElement(name = "Timi")
    protected String timi;
    @XmlElement(name = "Kortnumer")
    protected String kortnumer;
    @XmlElement(name = "Upphaed")
    protected String upphaed;
    @XmlElement(name = "Faerslunumer")
    protected String faerslunumer;
    @XmlElement(name = "Faersluhirdir")
    protected String faersluhirdir;
    @XmlElement(name = "Heimildarnumer")
    protected String heimildarnumer;
    @XmlElement(name = "StadsetningNumer")
    protected String stadsetningNumer;
    @XmlElement(name = "UtstodNumer")
    protected String utstodNumer;
    @XmlElement(name = "BuidAdOgilda")
    protected boolean buidAdOgilda;
    @XmlElement(name = "Bunkanumer")
    protected String bunkanumer;
    @XmlElement(name = "Soluadilinumer")
    protected String soluadilinumer;
    @XmlElement(name = "Hugbunadarnumer")
    protected String hugbunadarnumer;
    @XmlElement(name = "PosiID")
    protected String posiID;
    @XmlElement(name = "PinSkilabod")
    protected String pinSkilabod;
    @XmlElement(name = "Vidskiptaskilabod")
    protected String vidskiptaskilabod;
    @XmlElement(name = "F22_1til4")
    protected String f221Til4;
    @XmlElement(name = "LinaC1")
    protected String linaC1;
    @XmlElement(name = "LinaC2")
    protected String linaC2;
    @XmlElement(name = "LinaC3")
    protected String linaC3;
    @XmlElement(name = "LinaC4")
    protected String linaC4;
    @XmlElement(name = "LinaD1")
    protected String linaD1;
    @XmlElement(name = "LinaD2")
    protected String linaD2;
    @XmlElement(name = "TegundAdgerd")
    protected String tegundAdgerd;
    @XmlElement(name = "FaerslunumerUpphafleguFaerslu")
    protected String faerslunumerUpphafleguFaerslu;
    @XmlElement(name = "TerminalID")
    protected String terminalID;

    /**
     * Gets the value of the verslunNafn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVerslunNafn() {
        return verslunNafn;
    }

    /**
     * Sets the value of the verslunNafn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVerslunNafn(String value) {
        this.verslunNafn = value;
    }

    /**
     * Gets the value of the verslunHeimilisfang property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVerslunHeimilisfang() {
        return verslunHeimilisfang;
    }

    /**
     * Sets the value of the verslunHeimilisfang property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVerslunHeimilisfang(String value) {
        this.verslunHeimilisfang = value;
    }

    /**
     * Gets the value of the verslunStadur property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVerslunStadur() {
        return verslunStadur;
    }

    /**
     * Sets the value of the verslunStadur property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVerslunStadur(String value) {
        this.verslunStadur = value;
    }

    /**
     * Gets the value of the tegundKorts property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTegundKorts() {
        return tegundKorts;
    }

    /**
     * Sets the value of the tegundKorts property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTegundKorts(String value) {
        this.tegundKorts = value;
    }

    /**
     * Gets the value of the tegundKortsKodi property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTegundKortsKodi() {
        return tegundKortsKodi;
    }

    /**
     * Sets the value of the tegundKortsKodi property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTegundKortsKodi(String value) {
        this.tegundKortsKodi = value;
    }

    /**
     * Gets the value of the dagsetning property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDagsetning() {
        return dagsetning;
    }

    /**
     * Sets the value of the dagsetning property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDagsetning(String value) {
        this.dagsetning = value;
    }

    /**
     * Gets the value of the timi property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTimi() {
        return timi;
    }

    /**
     * Sets the value of the timi property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTimi(String value) {
        this.timi = value;
    }

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

    /**
     * Gets the value of the upphaed property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUpphaed() {
        return upphaed;
    }

    /**
     * Sets the value of the upphaed property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUpphaed(String value) {
        this.upphaed = value;
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
     * Gets the value of the faersluhirdir property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFaersluhirdir() {
        return faersluhirdir;
    }

    /**
     * Sets the value of the faersluhirdir property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFaersluhirdir(String value) {
        this.faersluhirdir = value;
    }

    /**
     * Gets the value of the heimildarnumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHeimildarnumer() {
        return heimildarnumer;
    }

    /**
     * Sets the value of the heimildarnumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHeimildarnumer(String value) {
        this.heimildarnumer = value;
    }

    /**
     * Gets the value of the stadsetningNumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStadsetningNumer() {
        return stadsetningNumer;
    }

    /**
     * Sets the value of the stadsetningNumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStadsetningNumer(String value) {
        this.stadsetningNumer = value;
    }

    /**
     * Gets the value of the utstodNumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUtstodNumer() {
        return utstodNumer;
    }

    /**
     * Sets the value of the utstodNumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUtstodNumer(String value) {
        this.utstodNumer = value;
    }

    /**
     * Gets the value of the buidAdOgilda property.
     *
     */
    public boolean isBuidAdOgilda() {
        return buidAdOgilda;
    }

    /**
     * Sets the value of the buidAdOgilda property.
     *
     */
    public void setBuidAdOgilda(boolean value) {
        this.buidAdOgilda = value;
    }

    /**
     * Gets the value of the bunkanumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBunkanumer() {
        return bunkanumer;
    }

    /**
     * Sets the value of the bunkanumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBunkanumer(String value) {
        this.bunkanumer = value;
    }

    /**
     * Gets the value of the soluadilinumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSoluadilinumer() {
        return soluadilinumer;
    }

    /**
     * Sets the value of the soluadilinumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSoluadilinumer(String value) {
        this.soluadilinumer = value;
    }

    /**
     * Gets the value of the hugbunadarnumer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHugbunadarnumer() {
        return hugbunadarnumer;
    }

    /**
     * Sets the value of the hugbunadarnumer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHugbunadarnumer(String value) {
        this.hugbunadarnumer = value;
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
     * Gets the value of the pinSkilabod property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPinSkilabod() {
        return pinSkilabod;
    }

    /**
     * Sets the value of the pinSkilabod property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPinSkilabod(String value) {
        this.pinSkilabod = value;
    }

    /**
     * Gets the value of the vidskiptaskilabod property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVidskiptaskilabod() {
        return vidskiptaskilabod;
    }

    /**
     * Sets the value of the vidskiptaskilabod property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVidskiptaskilabod(String value) {
        this.vidskiptaskilabod = value;
    }

    /**
     * Gets the value of the f221Til4 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getF221Til4() {
        return f221Til4;
    }

    /**
     * Sets the value of the f221Til4 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setF221Til4(String value) {
        this.f221Til4 = value;
    }

    /**
     * Gets the value of the linaC1 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaC1() {
        return linaC1;
    }

    /**
     * Sets the value of the linaC1 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaC1(String value) {
        this.linaC1 = value;
    }

    /**
     * Gets the value of the linaC2 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaC2() {
        return linaC2;
    }

    /**
     * Sets the value of the linaC2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaC2(String value) {
        this.linaC2 = value;
    }

    /**
     * Gets the value of the linaC3 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaC3() {
        return linaC3;
    }

    /**
     * Sets the value of the linaC3 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaC3(String value) {
        this.linaC3 = value;
    }

    /**
     * Gets the value of the linaC4 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaC4() {
        return linaC4;
    }

    /**
     * Sets the value of the linaC4 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaC4(String value) {
        this.linaC4 = value;
    }

    /**
     * Gets the value of the linaD1 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaD1() {
        return linaD1;
    }

    /**
     * Sets the value of the linaD1 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaD1(String value) {
        this.linaD1 = value;
    }

    /**
     * Gets the value of the linaD2 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinaD2() {
        return linaD2;
    }

    /**
     * Sets the value of the linaD2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinaD2(String value) {
        this.linaD2 = value;
    }

    /**
     * Gets the value of the tegundAdgerd property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTegundAdgerd() {
        return tegundAdgerd;
    }

    /**
     * Sets the value of the tegundAdgerd property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTegundAdgerd(String value) {
        this.tegundAdgerd = value;
    }

    /**
     * Gets the value of the faerslunumerUpphafleguFaerslu property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFaerslunumerUpphafleguFaerslu() {
        return faerslunumerUpphafleguFaerslu;
    }

    /**
     * Sets the value of the faerslunumerUpphafleguFaerslu property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFaerslunumerUpphafleguFaerslu(String value) {
        this.faerslunumerUpphafleguFaerslu = value;
    }

    /**
     * Gets the value of the terminalID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTerminalID() {
        return terminalID;
    }

    /**
     * Sets the value of the terminalID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTerminalID(String value) {
        this.terminalID = value;
    }

}
