
package ws.web.ems.nocr.gam.com;

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
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CardRequestStateWS", targetNamespace = "http://ws.web.ems.nocr.gam.com/", wsdlLocation = "http://localhost:7001/ems-web/CardRequestStateWS?WSDL")
public class CardRequestStateWS_Service
    extends Service
{

    private final static URL CARDREQUESTSTATEWS_WSDL_LOCATION;
    private final static WebServiceException CARDREQUESTSTATEWS_EXCEPTION;
    private final static QName CARDREQUESTSTATEWS_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "CardRequestStateWS");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:7001/ems-web/CardRequestStateWS?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CARDREQUESTSTATEWS_WSDL_LOCATION = url;
        CARDREQUESTSTATEWS_EXCEPTION = e;
    }

    public CardRequestStateWS_Service() {
        super(__getWsdlLocation(), CARDREQUESTSTATEWS_QNAME);
    }

    public CardRequestStateWS_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), CARDREQUESTSTATEWS_QNAME, features);
    }

    public CardRequestStateWS_Service(URL wsdlLocation) {
        super(wsdlLocation, CARDREQUESTSTATEWS_QNAME);
    }

    public CardRequestStateWS_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CARDREQUESTSTATEWS_QNAME, features);
    }

    public CardRequestStateWS_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CardRequestStateWS_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CardRequestStateWS
     */
    @WebEndpoint(name = "CardRequestStatePort")
    public CardRequestStateWS getCardRequestStatePort() {
        return super.getPort(new QName("http://ws.web.ems.nocr.gam.com/", "CardRequestStatePort"), CardRequestStateWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CardRequestStateWS
     */
    @WebEndpoint(name = "CardRequestStatePort")
    public CardRequestStateWS getCardRequestStatePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.web.ems.nocr.gam.com/", "CardRequestStatePort"), CardRequestStateWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CARDREQUESTSTATEWS_EXCEPTION!= null) {
            throw CARDREQUESTSTATEWS_EXCEPTION;
        }
        return CARDREQUESTSTATEWS_WSDL_LOCATION;
    }

}
