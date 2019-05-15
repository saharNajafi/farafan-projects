
package com.gam.nocr.ems.biz.service.external.client.bpi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gam.nocr.ems.biz.service.external.client.bpi package. 
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

    private final static QName _BpiInquiryWTO_QNAME = new QName("http://bpi.farafan.ir/", "bpiInquiryWTO");
    private final static QName _BpiWebServiceFault_QNAME = new QName("http://bpi.farafan.ir/", "BpiWebServiceFault");
    private final static QName _BpiInquiryResponse_QNAME = new QName("http://bpi.farafan.ir/", "bpiInquiryResponse");
    private final static QName _BpiInquiry_QNAME = new QName("http://bpi.farafan.ir/", "bpiInquiry");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gam.nocr.ems.biz.service.external.client.bpi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BpiException }
     * 
     */
    public BpiException createBpiException() {
        return new BpiException();
    }

    /**
     * Create an instance of {@link BpiInquiryWTO }
     * 
     */
    public BpiInquiryWTO createBpiInquiryWTO() {
        return new BpiInquiryWTO();
    }

    /**
     * Create an instance of {@link BpiInquiryResponse }
     * 
     */
    public BpiInquiryResponse createBpiInquiryResponse() {
        return new BpiInquiryResponse();
    }

    /**
     * Create an instance of {@link BpiInquiry }
     * 
     */
    public BpiInquiry createBpiInquiry() {
        return new BpiInquiry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BpiInquiryWTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "bpiInquiryWTO")
    public JAXBElement<BpiInquiryWTO> createBpiInquiryWTO(BpiInquiryWTO value) {
        return new JAXBElement<BpiInquiryWTO>(_BpiInquiryWTO_QNAME, BpiInquiryWTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BpiException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "BpiWebServiceFault")
    public JAXBElement<BpiException> createBpiWebServiceFault(BpiException value) {
        return new JAXBElement<BpiException>(_BpiWebServiceFault_QNAME, BpiException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BpiInquiryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "bpiInquiryResponse")
    public JAXBElement<BpiInquiryResponse> createBpiInquiryResponse(BpiInquiryResponse value) {
        return new JAXBElement<BpiInquiryResponse>(_BpiInquiryResponse_QNAME, BpiInquiryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BpiInquiry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "bpiInquiry")
    public JAXBElement<BpiInquiry> createBpiInquiry(BpiInquiry value) {
        return new JAXBElement<BpiInquiry>(_BpiInquiry_QNAME, BpiInquiry.class, null, value);
    }

}
