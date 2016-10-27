
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
 *         &lt;element name="CreatePaymentResult" type="{http://api.valitor.is/Fyrirtaekjagreidslur/}PaymentResponse" minOccurs="0"/>
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
    "createPaymentResult"
})
@XmlRootElement(name = "CreatePaymentResponse")
public class CreatePaymentResponse {

    @XmlElement(name = "CreatePaymentResult")
    protected PaymentResponse createPaymentResult;

    /**
     * Gets the value of the createPaymentResult property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentResponse }
     *     
     */
    public PaymentResponse getCreatePaymentResult() {
        return createPaymentResult;
    }

    /**
     * Sets the value of the createPaymentResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentResponse }
     *     
     */
    public void setCreatePaymentResult(PaymentResponse value) {
        this.createPaymentResult = value;
    }

}
