
package ws.web.ems.nocr.gam.com;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws.web.ems.nocr.gam.com package. 
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

    private final static QName _EMSWebServiceFault_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "EMSWebServiceFault");
    private final static QName _BaseException_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "BaseException");
    private final static QName _CheckBirthCertificateSerial_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkBirthCertificateSerial");
    private final static QName _CheckTrackingId_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkTrackingId");
    private final static QName _CheckCardRequestStateResponse_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkCardRequestStateResponse");
    private final static QName _CheckTrackingIdResponse_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkTrackingIdResponse");
    private final static QName _CheckBirthCertificateSerialResponse_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkBirthCertificateSerialResponse");
    private final static QName _CheckCardRequestState_QNAME = new QName("http://ws.web.ems.nocr.gam.com/", "checkCardRequestState");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws.web.ems.nocr.gam.com
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InternalInterfaceException }
     * 
     */
    public InternalInterfaceException createInternalInterfaceException() {
        return new InternalInterfaceException();
    }

    /**
     * Create an instance of {@link BaseException }
     * 
     */
    public BaseException createBaseException() {
        return new BaseException();
    }

    /**
     * Create an instance of {@link CheckBirthCertificateSerial }
     * 
     */
    public CheckBirthCertificateSerial createCheckBirthCertificateSerial() {
        return new CheckBirthCertificateSerial();
    }

    /**
     * Create an instance of {@link CheckCardRequestStateResponse }
     * 
     */
    public CheckCardRequestStateResponse createCheckCardRequestStateResponse() {
        return new CheckCardRequestStateResponse();
    }

    /**
     * Create an instance of {@link CheckTrackingId }
     * 
     */
    public CheckTrackingId createCheckTrackingId() {
        return new CheckTrackingId();
    }

    /**
     * Create an instance of {@link CheckTrackingIdResponse }
     * 
     */
    public CheckTrackingIdResponse createCheckTrackingIdResponse() {
        return new CheckTrackingIdResponse();
    }

    /**
     * Create an instance of {@link CheckBirthCertificateSerialResponse }
     * 
     */
    public CheckBirthCertificateSerialResponse createCheckBirthCertificateSerialResponse() {
        return new CheckBirthCertificateSerialResponse();
    }

    /**
     * Create an instance of {@link CheckCardRequestState }
     * 
     */
    public CheckCardRequestState createCheckCardRequestState() {
        return new CheckCardRequestState();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternalInterfaceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "EMSWebServiceFault")
    public JAXBElement<InternalInterfaceException> createEMSWebServiceFault(InternalInterfaceException value) {
        return new JAXBElement<InternalInterfaceException>(_EMSWebServiceFault_QNAME, InternalInterfaceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "BaseException")
    public JAXBElement<BaseException> createBaseException(BaseException value) {
        return new JAXBElement<BaseException>(_BaseException_QNAME, BaseException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckBirthCertificateSerial }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkBirthCertificateSerial")
    public JAXBElement<CheckBirthCertificateSerial> createCheckBirthCertificateSerial(CheckBirthCertificateSerial value) {
        return new JAXBElement<CheckBirthCertificateSerial>(_CheckBirthCertificateSerial_QNAME, CheckBirthCertificateSerial.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckTrackingId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkTrackingId")
    public JAXBElement<CheckTrackingId> createCheckTrackingId(CheckTrackingId value) {
        return new JAXBElement<CheckTrackingId>(_CheckTrackingId_QNAME, CheckTrackingId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckCardRequestStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkCardRequestStateResponse")
    public JAXBElement<CheckCardRequestStateResponse> createCheckCardRequestStateResponse(CheckCardRequestStateResponse value) {
        return new JAXBElement<CheckCardRequestStateResponse>(_CheckCardRequestStateResponse_QNAME, CheckCardRequestStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckTrackingIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkTrackingIdResponse")
    public JAXBElement<CheckTrackingIdResponse> createCheckTrackingIdResponse(CheckTrackingIdResponse value) {
        return new JAXBElement<CheckTrackingIdResponse>(_CheckTrackingIdResponse_QNAME, CheckTrackingIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckBirthCertificateSerialResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkBirthCertificateSerialResponse")
    public JAXBElement<CheckBirthCertificateSerialResponse> createCheckBirthCertificateSerialResponse(CheckBirthCertificateSerialResponse value) {
        return new JAXBElement<CheckBirthCertificateSerialResponse>(_CheckBirthCertificateSerialResponse_QNAME, CheckBirthCertificateSerialResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckCardRequestState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.web.ems.nocr.gam.com/", name = "checkCardRequestState")
    public JAXBElement<CheckCardRequestState> createCheckCardRequestState(CheckCardRequestState value) {
        return new JAXBElement<CheckCardRequestState>(_CheckCardRequestState_QNAME, CheckCardRequestState.class, null, value);
    }

}
