
package com.gam.nocr.ems.biz.service.external.client.gaas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gam.nocr.ems.biz.service.external.client.gaas package. 
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

    private final static QName _GetLoginTokenResponse_QNAME = new QName("http://gaas.gam.com/webservice", "getLoginTokenResponse");
    private final static QName _Login_QNAME = new QName("http://gaas.gam.com/webservice", "login");
    private final static QName _CASWebServiceFault_QNAME = new QName("http://gaas.gam.com/webservice", "CASWebServiceFault");
    private final static QName _ValidateTicket_QNAME = new QName("http://gaas.gam.com/webservice", "validateTicket");
    private final static QName _GetLoginToken_QNAME = new QName("http://gaas.gam.com/webservice", "getLoginToken");
    private final static QName _ValidateTicketResponse_QNAME = new QName("http://gaas.gam.com/webservice", "validateTicketResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://gaas.gam.com/webservice", "loginResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gam.nocr.ems.biz.service.external.client.gaas
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CasWebServiceFault }
     * 
     */
    public CasWebServiceFault createCasWebServiceFault() {
        return new CasWebServiceFault();
    }

    /**
     * Create an instance of {@link GetLoginTokenResponse }
     * 
     */
    public GetLoginTokenResponse createGetLoginTokenResponse() {
        return new GetLoginTokenResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link WebServiceFault }
     * 
     */
    public WebServiceFault createWebServiceFault() {
        return new WebServiceFault();
    }

    /**
     * Create an instance of {@link ValidateTicket }
     * 
     */
    public ValidateTicket createValidateTicket() {
        return new ValidateTicket();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link GetLoginToken }
     * 
     */
    public GetLoginToken createGetLoginToken() {
        return new GetLoginToken();
    }

    /**
     * Create an instance of {@link ValidateTicketResponse }
     * 
     */
    public ValidateTicketResponse createValidateTicketResponse() {
        return new ValidateTicketResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoginTokenResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "getLoginTokenResponse")
    public JAXBElement<GetLoginTokenResponse> createGetLoginTokenResponse(GetLoginTokenResponse value) {
        return new JAXBElement<GetLoginTokenResponse>(_GetLoginTokenResponse_QNAME, GetLoginTokenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CasWebServiceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "CASWebServiceFault")
    public JAXBElement<CasWebServiceFault> createCASWebServiceFault(CasWebServiceFault value) {
        return new JAXBElement<CasWebServiceFault>(_CASWebServiceFault_QNAME, CasWebServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateTicket }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "validateTicket")
    public JAXBElement<ValidateTicket> createValidateTicket(ValidateTicket value) {
        return new JAXBElement<ValidateTicket>(_ValidateTicket_QNAME, ValidateTicket.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoginToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "getLoginToken")
    public JAXBElement<GetLoginToken> createGetLoginToken(GetLoginToken value) {
        return new JAXBElement<GetLoginToken>(_GetLoginToken_QNAME, GetLoginToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateTicketResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "validateTicketResponse")
    public JAXBElement<ValidateTicketResponse> createValidateTicketResponse(ValidateTicketResponse value) {
        return new JAXBElement<ValidateTicketResponse>(_ValidateTicketResponse_QNAME, ValidateTicketResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gaas.gam.com/webservice", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

}
