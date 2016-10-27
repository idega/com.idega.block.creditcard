
package is.valitor.greidslugatt;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TegundKorts.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TegundKorts">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Electron"/>
 *     &lt;enumeration value="Maestro"/>
 *     &lt;enumeration value="MasterCardDebit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TegundKorts", namespace = "http://greidslugatt.valitor.is")
@XmlEnum
public enum TegundKorts {

    @XmlEnumValue("Electron")
    ELECTRON("Electron"),
    @XmlEnumValue("Maestro")
    MAESTRO("Maestro"),
    @XmlEnumValue("MasterCardDebit")
    MASTER_CARD_DEBIT("MasterCardDebit");
    private final String value;

    TegundKorts(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TegundKorts fromValue(String v) {
        for (TegundKorts c: TegundKorts.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
