
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

    private final static QName _BpiWebServiceFault_QNAME = new QName("http://bpi.farafan.ir/", "BpiWebServiceFault");
    private final static QName _GetResultResponse_QNAME = new QName("http://bpi.farafan.ir/", "getResultResponse");
    private final static QName _GetResult_QNAME = new QName("http://bpi.farafan.ir/", "getResult");

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
     * Create an instance of {@link GetResultResponse }
     * 
     */
    public GetResultResponse createGetResultResponse() {
        return new GetResultResponse();
    }

    /**
     * Create an instance of {@link GetResult }
     * 
     */
    public GetResult createGetResult() {
        return new GetResult();
    }

    /**
     * Create an instance of {@link SadadResponseAsJson }
     * 
     */
    public SadadResponseAsJson createSadadResponseAsJson() {
        return new SadadResponseAsJson();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "getResultResponse")
    public JAXBElement<GetResultResponse> createGetResultResponse(GetResultResponse value) {
        return new JAXBElement<GetResultResponse>(_GetResultResponse_QNAME, GetResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bpi.farafan.ir/", name = "getResult")
    public JAXBElement<GetResult> createGetResult(GetResult value) {
        return new JAXBElement<GetResult>(_GetResult_QNAME, GetResult.class, null, value);
    }

}
