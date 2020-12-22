
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

    private final static QName _IMSException_QNAME = new QName("http://nims.manipardaz.com/", "iMSException");
    private final static QName _Exception_QNAME = new QName("http://nims.manipardaz.com/", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: est
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link IMSException }
     * 
     */
    public IMSException createIMSException() {
        return new IMSException();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link TransferInfo }
     * 
     */
    public TransferInfo createTransferInfo() {
        return new TransferInfo();
    }

    /**
     * Create an instance of {@link ErrorInfo }
     * 
     */
    public ErrorInfo createErrorInfo() {
        return new ErrorInfo();
    }

    /**
     * Create an instance of {@link CardRevokedInfo }
     * 
     */
    public CardRevokedInfo createCardRevokedInfo() {
        return new CardRevokedInfo();
    }

    /**
     * Create an instance of {@link CardDeliverInfo }
     * 
     */
    public CardDeliverInfo createCardDeliverInfo() {
        return new CardDeliverInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IMSException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nims.manipardaz.com/", name = "iMSException")
    public JAXBElement<IMSException> createIMSException(IMSException value) {
        return new JAXBElement<IMSException>(_IMSException_QNAME, IMSException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nims.manipardaz.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
