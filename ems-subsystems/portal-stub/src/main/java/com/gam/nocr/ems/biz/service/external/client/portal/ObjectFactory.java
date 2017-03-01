
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gam.nocr.ems.biz.service.external.client.portal package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RetrieveRequestedSms_QNAME = new QName("http://portalws.ws.web.portal.nocr.gam.com/", "retrieveRequestedSms");
    private final static QName _ExternalInterfaceException_QNAME = new QName("http://portalws.ws.web.portal.nocr.gam.com/", "ExternalInterfaceException");
    private final static QName _RetrieveRequestedSmsResponse_QNAME = new QName("http://portalws.ws.web.portal.nocr.gam.com/", "retrieveRequestedSmsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gam.nocr.ems.biz.service.external.client.portal
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetrieveRequestedSmsResponse }
     * 
     */
    public RetrieveRequestedSmsResponse createRetrieveRequestedSmsResponse() {
        return new RetrieveRequestedSmsResponse();
    }

    /**
     * Create an instance of {@link ExternalInterfaceException }
     * 
     */
    public ExternalInterfaceException createExternalInterfaceException() {
        return new ExternalInterfaceException();
    }

    /**
     * Create an instance of {@link RetrieveRequestedSms }
     * 
     */
    public RetrieveRequestedSms createRetrieveRequestedSms() {
        return new RetrieveRequestedSms();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveRequestedSms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portalws.ws.web.portal.nocr.gam.com/", name = "retrieveRequestedSms")
    public JAXBElement<RetrieveRequestedSms> createRetrieveRequestedSms(RetrieveRequestedSms value) {
        return new JAXBElement<RetrieveRequestedSms>(_RetrieveRequestedSms_QNAME, RetrieveRequestedSms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExternalInterfaceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portalws.ws.web.portal.nocr.gam.com/", name = "ExternalInterfaceException")
    public JAXBElement<ExternalInterfaceException> createExternalInterfaceException(ExternalInterfaceException value) {
        return new JAXBElement<ExternalInterfaceException>(_ExternalInterfaceException_QNAME, ExternalInterfaceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveRequestedSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portalws.ws.web.portal.nocr.gam.com/", name = "retrieveRequestedSmsResponse")
    public JAXBElement<RetrieveRequestedSmsResponse> createRetrieveRequestedSmsResponse(RetrieveRequestedSmsResponse value) {
        return new JAXBElement<RetrieveRequestedSmsResponse>(_RetrieveRequestedSmsResponse_QNAME, RetrieveRequestedSmsResponse.class, null, value);
    }

}
