
package com.gam.nocr.ems.biz.service.external.client.bpi;

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
@WebServiceClient(name = "BpiService", targetNamespace = "http://bpi.farafan.ir/", wsdlLocation = "http://10.7.17.109:7006/bpi-1.0/BpiService?WSDL")
public class BpiService_Service
    extends Service
{

    private final static URL BPISERVICE_WSDL_LOCATION;
    private final static WebServiceException BPISERVICE_EXCEPTION;
    private final static QName BPISERVICE_QNAME = new QName("http://bpi.farafan.ir/", "BpiService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.7.17.109:7006/bpi-1.0/BpiService?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BPISERVICE_WSDL_LOCATION = url;
        BPISERVICE_EXCEPTION = e;
    }

    public BpiService_Service() {
        super(__getWsdlLocation(), BPISERVICE_QNAME);
    }

    public BpiService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), BPISERVICE_QNAME, features);
    }

    public BpiService_Service(URL wsdlLocation) {
        super(wsdlLocation, BPISERVICE_QNAME);
    }

    public BpiService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BPISERVICE_QNAME, features);
    }

    public BpiService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BpiService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BpiService
     */
    @WebEndpoint(name = "BpiServicePort")
    public BpiService getBpiServicePort() {
        return super.getPort(new QName("http://bpi.farafan.ir/", "BpiServicePort"), BpiService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BpiService
     */
    @WebEndpoint(name = "BpiServicePort")
    public BpiService getBpiServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://bpi.farafan.ir/", "BpiServicePort"), BpiService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BPISERVICE_EXCEPTION!= null) {
            throw BPISERVICE_EXCEPTION;
        }
        return BPISERVICE_WSDL_LOCATION;
    }

}
