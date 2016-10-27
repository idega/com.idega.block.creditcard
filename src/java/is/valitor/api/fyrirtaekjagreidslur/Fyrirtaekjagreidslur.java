
package is.valitor.api.fyrirtaekjagreidslur;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "Fyrirtaekjagreidslur", targetNamespace = "http://api.valitor.is/Fyrirtaekjagreidslur/", wsdlLocation = "https://api-acquiring.valitor.is/fyrirtaekjagreidslur/1_1/fyrirtaekjagreidslur.asmx?wsdl")
public class Fyrirtaekjagreidslur
    extends Service
{

    private final static URL FYRIRTAEKJAGREIDSLUR_WSDL_LOCATION;
    private final static WebServiceException FYRIRTAEKJAGREIDSLUR_EXCEPTION;
    private final static QName FYRIRTAEKJAGREIDSLUR_QNAME = new QName("http://api.valitor.is/Fyrirtaekjagreidslur/", "Fyrirtaekjagreidslur");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://api-acquiring.valitor.is/fyrirtaekjagreidslur/1_1/fyrirtaekjagreidslur.asmx?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        FYRIRTAEKJAGREIDSLUR_WSDL_LOCATION = url;
        FYRIRTAEKJAGREIDSLUR_EXCEPTION = e;
    }

    public Fyrirtaekjagreidslur() {
        super(__getWsdlLocation(), FYRIRTAEKJAGREIDSLUR_QNAME);
    }

    public Fyrirtaekjagreidslur(WebServiceFeature... features) {
        super(__getWsdlLocation(), FYRIRTAEKJAGREIDSLUR_QNAME, features);
    }

    public Fyrirtaekjagreidslur(URL wsdlLocation) {
        super(wsdlLocation, FYRIRTAEKJAGREIDSLUR_QNAME);
    }

    public Fyrirtaekjagreidslur(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, FYRIRTAEKJAGREIDSLUR_QNAME, features);
    }

    public Fyrirtaekjagreidslur(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Fyrirtaekjagreidslur(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns FyrirtaekjagreidslurSoap
     */
    @WebEndpoint(name = "FyrirtaekjagreidslurSoap")
    public FyrirtaekjagreidslurSoap getFyrirtaekjagreidslurSoap() {
        return super.getPort(new QName("http://api.valitor.is/Fyrirtaekjagreidslur/", "FyrirtaekjagreidslurSoap"), FyrirtaekjagreidslurSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FyrirtaekjagreidslurSoap
     */
    @WebEndpoint(name = "FyrirtaekjagreidslurSoap")
    public FyrirtaekjagreidslurSoap getFyrirtaekjagreidslurSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://api.valitor.is/Fyrirtaekjagreidslur/", "FyrirtaekjagreidslurSoap"), FyrirtaekjagreidslurSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (FYRIRTAEKJAGREIDSLUR_EXCEPTION!= null) {
            throw FYRIRTAEKJAGREIDSLUR_EXCEPTION;
        }
        return FYRIRTAEKJAGREIDSLUR_WSDL_LOCATION;
    }

}
