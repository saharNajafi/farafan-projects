
package est;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the est package. 
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

    private final static QName _GetImageSmart_QNAME = new QName("http://est", "getImageSmart");
    private final static QName _GetVersion_QNAME = new QName("http://est", "getVersion");
    private final static QName _GetImage3Response_QNAME = new QName("http://est", "getImage3Response");
    private final static QName _GetEstelam2SCResponse_QNAME = new QName("http://est", "getEstelam2SCResponse");
    private final static QName _GetEstelam2_QNAME = new QName("http://est", "getEstelam2");
    private final static QName _GetEstelam_QNAME = new QName("http://est", "getEstelam");
    private final static QName _GetEstelam2Response_QNAME = new QName("http://est", "getEstelam2Response");
    private final static QName _GetImage_QNAME = new QName("http://est", "getImage");
    private final static QName _GetEstelam2SC_QNAME = new QName("http://est", "getEstelam2SC");
    private final static QName _GetImageSmartResponse_QNAME = new QName("http://est", "getImageSmartResponse");
    private final static QName _GetImage2_QNAME = new QName("http://est", "getImage2");
    private final static QName _GetImage3_QNAME = new QName("http://est", "getImage3");
    private final static QName _GetEstelamResponse_QNAME = new QName("http://est", "getEstelamResponse");
    private final static QName _GetImageResponse_QNAME = new QName("http://est", "getImageResponse");
    private final static QName _GetVersionResponse_QNAME = new QName("http://est", "getVersionResponse");
    private final static QName _GetImage2Response_QNAME = new QName("http://est", "getImage2Response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: est
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetEstelam2SCResponse }
     * 
     */
    public GetEstelam2SCResponse createGetEstelam2SCResponse() {
        return new GetEstelam2SCResponse();
    }

    /**
     * Create an instance of {@link GetImage3 }
     * 
     */
    public GetImage3 createGetImage3() {
        return new GetImage3();
    }

    /**
     * Create an instance of {@link GetEstelamResponse }
     * 
     */
    public GetEstelamResponse createGetEstelamResponse() {
        return new GetEstelamResponse();
    }

    /**
     * Create an instance of {@link GetImage2 }
     * 
     */
    public GetImage2 createGetImage2() {
        return new GetImage2();
    }

    /**
     * Create an instance of {@link GetImage }
     * 
     */
    public GetImage createGetImage() {
        return new GetImage();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link GetImage2Response }
     * 
     */
    public GetImage2Response createGetImage2Response() {
        return new GetImage2Response();
    }

    /**
     * Create an instance of {@link GetImageResponse }
     * 
     */
    public GetImageResponse createGetImageResponse() {
        return new GetImageResponse();
    }

    /**
     * Create an instance of {@link GetImage3Response }
     * 
     */
    public GetImage3Response createGetImage3Response() {
        return new GetImage3Response();
    }

    /**
     * Create an instance of {@link ImageResult }
     * 
     */
    public ImageResult createImageResult() {
        return new ImageResult();
    }

    /**
     * Create an instance of {@link GetEstelam2Response }
     * 
     */
    public GetEstelam2Response createGetEstelam2Response() {
        return new GetEstelam2Response();
    }

    /**
     * Create an instance of {@link GetImageSmart }
     * 
     */
    public GetImageSmart createGetImageSmart() {
        return new GetImageSmart();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link GetEstelam2 }
     * 
     */
    public GetEstelam2 createGetEstelam2() {
        return new GetEstelam2();
    }

    /**
     * Create an instance of {@link GetEstelam }
     * 
     */
    public GetEstelam createGetEstelam() {
        return new GetEstelam();
    }

    /**
     * Create an instance of {@link EstelamResult }
     * 
     */
    public EstelamResult createEstelamResult() {
        return new EstelamResult();
    }

    /**
     * Create an instance of {@link GetEstelam2SC }
     * 
     */
    public GetEstelam2SC createGetEstelam2SC() {
        return new GetEstelam2SC();
    }

    /**
     * Create an instance of {@link PersonInfo }
     * 
     */
    public PersonInfo createPersonInfo() {
        return new PersonInfo();
    }

    /**
     * Create an instance of {@link GetImageSmartResponse }
     * 
     */
    public GetImageSmartResponse createGetImageSmartResponse() {
        return new GetImageSmartResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImageSmart }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImageSmart")
    public JAXBElement<GetImageSmart> createGetImageSmart(GetImageSmart value) {
        return new JAXBElement<GetImageSmart>(_GetImageSmart_QNAME, GetImageSmart.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getVersion")
    public JAXBElement<GetVersion> createGetVersion(GetVersion value) {
        return new JAXBElement<GetVersion>(_GetVersion_QNAME, GetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImage3Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImage3Response")
    public JAXBElement<GetImage3Response> createGetImage3Response(GetImage3Response value) {
        return new JAXBElement<GetImage3Response>(_GetImage3Response_QNAME, GetImage3Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelam2SCResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelam2SCResponse")
    public JAXBElement<GetEstelam2SCResponse> createGetEstelam2SCResponse(GetEstelam2SCResponse value) {
        return new JAXBElement<GetEstelam2SCResponse>(_GetEstelam2SCResponse_QNAME, GetEstelam2SCResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelam2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelam2")
    public JAXBElement<GetEstelam2> createGetEstelam2(GetEstelam2 value) {
        return new JAXBElement<GetEstelam2>(_GetEstelam2_QNAME, GetEstelam2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelam")
    public JAXBElement<GetEstelam> createGetEstelam(GetEstelam value) {
        return new JAXBElement<GetEstelam>(_GetEstelam_QNAME, GetEstelam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelam2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelam2Response")
    public JAXBElement<GetEstelam2Response> createGetEstelam2Response(GetEstelam2Response value) {
        return new JAXBElement<GetEstelam2Response>(_GetEstelam2Response_QNAME, GetEstelam2Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImage")
    public JAXBElement<GetImage> createGetImage(GetImage value) {
        return new JAXBElement<GetImage>(_GetImage_QNAME, GetImage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelam2SC }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelam2SC")
    public JAXBElement<GetEstelam2SC> createGetEstelam2SC(GetEstelam2SC value) {
        return new JAXBElement<GetEstelam2SC>(_GetEstelam2SC_QNAME, GetEstelam2SC.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImageSmartResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImageSmartResponse")
    public JAXBElement<GetImageSmartResponse> createGetImageSmartResponse(GetImageSmartResponse value) {
        return new JAXBElement<GetImageSmartResponse>(_GetImageSmartResponse_QNAME, GetImageSmartResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImage2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImage2")
    public JAXBElement<GetImage2> createGetImage2(GetImage2 value) {
        return new JAXBElement<GetImage2>(_GetImage2_QNAME, GetImage2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImage3 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImage3")
    public JAXBElement<GetImage3> createGetImage3(GetImage3 value) {
        return new JAXBElement<GetImage3>(_GetImage3_QNAME, GetImage3 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstelamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getEstelamResponse")
    public JAXBElement<GetEstelamResponse> createGetEstelamResponse(GetEstelamResponse value) {
        return new JAXBElement<GetEstelamResponse>(_GetEstelamResponse_QNAME, GetEstelamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImageResponse")
    public JAXBElement<GetImageResponse> createGetImageResponse(GetImageResponse value) {
        return new JAXBElement<GetImageResponse>(_GetImageResponse_QNAME, GetImageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetImage2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://est", name = "getImage2Response")
    public JAXBElement<GetImage2Response> createGetImage2Response(GetImage2Response value) {
        return new JAXBElement<GetImage2Response>(_GetImage2Response_QNAME, GetImage2Response.class, null, value);
    }

}
