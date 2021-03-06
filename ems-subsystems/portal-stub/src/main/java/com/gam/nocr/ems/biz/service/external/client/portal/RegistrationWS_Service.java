
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "RegistrationWS", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", wsdlLocation = "http://trainerpc:7001/services/RegistrationWS?WSDL")
public class RegistrationWS_Service
    extends Service
{

    private final static URL REGISTRATIONWS_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS_Service.class.getResource(".");
            url = new URL(baseUrl, "http://trainerpc:7001/services/RegistrationWS?WSDL");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://trainerpc:7001/services/RegistrationWS?WSDL', retrying as a local file");
            logger.warning(e.getMessage());
        }
        REGISTRATIONWS_WSDL_LOCATION = url;
    }

    public RegistrationWS_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegistrationWS_Service() {
        super(REGISTRATIONWS_WSDL_LOCATION, new QName("http://portalws.ws.web.portal.nocr.gam.com/", "RegistrationWS"));
    }

    /**
     * 
     * @return
     *     returns RegistrationWS
     */
    @WebEndpoint(name = "RegistrationWSPort")
    public RegistrationWS getRegistrationWSPort() {
        return super.getPort(new QName("http://portalws.ws.web.portal.nocr.gam.com/", "RegistrationWSPort"), RegistrationWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegistrationWS
     */
    @WebEndpoint(name = "RegistrationWSPort")
    public RegistrationWS getRegistrationWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://portalws.ws.web.portal.nocr.gam.com/", "RegistrationWSPort"), RegistrationWS.class, features);
    }

}
