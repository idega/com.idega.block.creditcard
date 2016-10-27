
package is.valitor.api.fyrirtaekjagreidslur;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import is.valitor.api.fyrirtaekjagreidslur.serverkvittun.Kvittun;


/**
 * <p>Java class for HeimildSkilabod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeimildSkilabod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.valitor.is/Fyrirtaekjagreidslur/}Skilabod">
 *       &lt;sequence>
 *         &lt;element name="Kvittun" type="{http://api.valitor.is/Fyrirtaekjagreidslur/ServerKvittun}Kvittun" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeimildSkilabod", propOrder = {
    "kvittun"
})
public class HeimildSkilabod
    extends Skilabod
{

    @XmlElement(name = "Kvittun")
    protected Kvittun kvittun;

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
