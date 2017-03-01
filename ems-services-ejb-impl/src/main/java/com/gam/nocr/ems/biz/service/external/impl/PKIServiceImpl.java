package com.gam.nocr.ems.biz.service.external.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.xmldsig.certificatebase.XmlDSigCertUtil;
import com.gam.nocr.ems.biz.service.external.client.pki.BatchService;
import com.gam.nocr.ems.biz.service.external.client.pki.RAWS;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CertificateTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.TokenTO;
import com.gam.nocr.ems.data.enums.TokenReIssuanceReason;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.data.mapper.xmlmapper.XMLMapperProvider;
import com.gam.nocr.ems.util.EmsUtil;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "PKIService")
@Local(PKIServiceLocal.class)
@Remote(PKIServiceRemote.class)
public class PKIServiceImpl extends AbstractService implements PKIServiceLocal, PKIServiceRemote {
    private static final Logger logger = BaseLog.getLogger(PKIServiceImpl.class);
    private static final Logger pkiLogger = BaseLog.getLogger("PkiLogger");

    private static final String SEARCH_ID_PREFIX = "searchReq-";
    private static final String ENROLLMENT_ID_PREFIX = "enrollReq-";
    private static final String REVOCATION_ID_PREFIX = "revocationReq-";
    private static final String RENEWAL_ID_PREFIX = "renewalReq-";
    private static final String ACTIVATION_ID_PREFIX = "activationReq-";
    
    private static final String OFFER_ID = "offerID";
    private static final String MESSAGE_ID = "messageID";
    private static final String PROTOCOL_VERSION = "protocolVersion";
    private static final String COMMENT = "comment";

    private static final String PKI_ERROR_PREFIX = "EMS_S_PKI_";

    /**
     * Default values for web services
     */
    private static final String DEFAULT_WSDL_URL = "https://ra-ws.keynectis.net/WsOperator/batchRegistration?wsdl";
    private static final String DEFAULT_NAMESPACE = "http://www.keynectis.com/sequoia/ra/ws/batch";

    /**
     * Token offer ids
     */
    private static final String DEFAULT_OFFER_ID_AUTHENTICATION = "/IRANEID/ICM/USERAUTH:E";
    private static final String DEFAULT_OFFER_ID_SIGNATURE = "/IRANEID/ICM/USERSIGN:E";
    private static final String DEFAULT_OFFER_ID_ENCRYPTION = "/IRANEID/ICM/USERENC:E";
    private static final String DEFAULT_OFFER_ID_NETWORK_AUTHENTICATION = "/IRANEID/ICM/SERVERMSDC:E";

    /**
     * PKI Protocol Version
     */
    private static final String DEFAULT_PROTOCOL_VERSION = "1.0";

    /**
     * PKI ErrorCodes
     */
    private static final String WS_00400010 = "WS-00400010";
    private static final String WS_00400080 = "WS_00400080";
    
    private static final String WS_00000020 = "WS-00000020";
    private static final String WS_00010060 = "WS-00000060";
    private static final String WS_00000040 = "WS-00000040";
    
    private static final String WS_00400091 = "WS-00400091";

    RAWS service = null;

    /**
     * The method getService is used to get WebServices from PKI sub system
     *
     * @return an instance of type {@link RAWS}
     * @throws BaseException if cannot get the service
     */
    private RAWS getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_PKI_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_PKI_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "BatchService";
            logger.debug("Pki wsdl url: " + wsdlUrl);
            pkiLogger.debug("Pki wsdl url: " + wsdlUrl);
//            RAWS port = new BatchService().getRAWSPort();
            RAWS port = new BatchService(new URL(wsdlUrl), new QName(namespace, serviceName)).getRAWSPort();
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PKI_009, e.getMessage(), e);
        }
    }

    public PKIServiceImpl() {

//		headerMap.put(AUTHENTICATION, DEFAULT_OFFER_ID_AUTHENTICATION);
//		headerMap.put(TokenType.SIGNATURE, DEFAULT_OFFER_ID_SIGNATURE);
//		headerMap.put(TokenType.ENCRYPTION, DEFAULT_OFFER_ID_ENCRYPTION);
//		headerMap.put(TokenType.NETWORK, DEFAULT_OFFER_ID_NETWORK_AUTHENTICATION);

    }

    /**
     * The method prepareOfferId is used to prepare the offerId in spite of the type of the requested token
     *
     * @param tokenType is an enumeration instance of type {@link TokenType}
     * @return an instance of type {@link String}
     * @throws BaseException
     */
    private String prepareOfferId(TokenType tokenType) throws BaseException {
        String offerId = null;
        boolean defaultValueFlag = false;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            switch (tokenType) {
                case AUTHENTICATION:
                    offerId = (String) pm.getProfile(ProfileKeyName.KEY_PERSON_TOKEN_OFFER_ID_AUTHENTICATION, true, null, null);
                    break;

                case SIGNATURE:
                    offerId = (String) pm.getProfile(ProfileKeyName.KEY_PERSON_TOKEN_OFFER_ID_SIGNATURE, true, null, null);
                    break;

                case ENCRYPTION:
                    offerId = (String) pm.getProfile(ProfileKeyName.KEY_PERSON_TOKEN_OFFER_ID_ENCRYPTION, true, null, null);
                    break;

                case NETWORK:
                    offerId = (String) pm.getProfile(ProfileKeyName.KEY_NETWORK_TOKEN_OFFER_ID_AUTHENTICATION, true, null, null);
                    break;
            }
        } catch (Exception e) {
            defaultValueFlag = true;
            logger.warn(BizExceptionCode.PKI_013, BizExceptionCode.PKI_013_MSG, new String[]{tokenType.name()});
            pkiLogger.warn(BizExceptionCode.PKI_013, BizExceptionCode.PKI_013_MSG, new String[]{tokenType.name()});
        }

        if (offerId == null || defaultValueFlag) {
            switch (tokenType) {
                case AUTHENTICATION:
                    offerId = DEFAULT_OFFER_ID_AUTHENTICATION;
                    break;

                case SIGNATURE:
                    offerId = DEFAULT_OFFER_ID_SIGNATURE;
                    break;

                case ENCRYPTION:
                    offerId = DEFAULT_OFFER_ID_ENCRYPTION;
                    break;

                case NETWORK:
                    offerId = DEFAULT_OFFER_ID_NETWORK_AUTHENTICATION;
                    break;
            }
        }
        return offerId;
    }

    private String prepareProtocolVersion() throws BaseException {
        String protocolVersion = null;
        boolean defaultValueFlag = false;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            protocolVersion = (String) pm.getProfile(ProfileKeyName.KEY_TOKEN_PROTOCOL_VERSION, true, null, null);
        } catch (Exception e) {
            defaultValueFlag = true;
            logger.warn(BizExceptionCode.PKI_017, BizExceptionCode.PKI_017_MSG);
            pkiLogger.warn(BizExceptionCode.PKI_017, BizExceptionCode.PKI_017_MSG);
        }
        if (protocolVersion == null || defaultValueFlag) {
            protocolVersion = DEFAULT_PROTOCOL_VERSION;
        }
        return protocolVersion;
    }

    /**
     * The method sendPKIRequest is used to send the token requests to the sub system 'PKI'.
     *
     * @param stringRequest is the parameter which represents the xml request string
     * @return an object of type String which carries the PKI response
     */
    private String sendPKIRequest(String stringRequest) throws BaseException {

        String result;
//		TODO : Correct the process importing wsdl
        logger.info("The process 'sendPKIRequest' is started...");
        pkiLogger.info("The process 'sendPKIRequest' is started...");
        try {
            result = getService().request(stringRequest);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unable to call PKI 'request' service", e);
            pkiLogger.error("Unable to call PKI 'request' service", e);
            throw new ServiceException(
                    BizExceptionCode.PKI_001,
                    BizExceptionCode.PKI_001_MSG,
                    e);
        }
        logger.info("The process 'sendPKIRequest' is finished.");
        pkiLogger.info("The process 'sendPKIRequest' is finished.");
        return result;
    }

    /**
     * The signRequest method is used to sign the request context before sending it to PKI sub system
     *
     * @param byteRequest   represents an instance of a byte[] as input parameter
     * @param certificateTO is an object of type {@link CertificateTO} which is used for signing
     * @return an String value
     */
    private String signRequest(byte[] byteRequest,
                               CertificateTO certificateTO) throws BaseException {
        HashMap xmlDSigMap = new HashMap();
        xmlDSigMap.put(XmlDSigCertUtil.CERTIFICATE, certificateTO.getCertificate());
        xmlDSigMap.put(XmlDSigCertUtil.PASSWORD, certificateTO.getCode());
        InputStream is = new ByteArrayInputStream(byteRequest);
        XmlDSigCertUtil xmlDSigCertUtil = new XmlDSigCertUtil(xmlDSigMap);
        Document signedDoc = xmlDSigCertUtil.signXML(is);
        return convertDocumentToString(signedDoc);
    }

    /**
     * The verifyPKIResponse method is used to verify the response context which is received by PKI sub system
     *
     * @param response      represents the String response
     * @param certificateTO is an object of type {@link CertificateTO} which is used for verifying
     */
    private void verifyPKIResponse(String response, CertificateTO certificateTO) throws BaseException {
        HashMap xmlDSigMap = new HashMap();
        xmlDSigMap.put(XmlDSigCertUtil.CERTIFICATE, certificateTO.getCertificate());
        xmlDSigMap.put(XmlDSigCertUtil.PASSWORD, certificateTO.getCode());
        XmlDSigCertUtil xmlDSigCertUtil = new XmlDSigCertUtil(xmlDSigMap);
        Document doc = convertStringToDocument(response);
        boolean verified = false;
        try {
            verified = xmlDSigCertUtil.verifySignedXML(doc);
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                logger.info(response);
                pkiLogger.info(response);
                throw new ServiceException(BizExceptionCode.PKI_012, BizExceptionCode.PKI_012_MSG);
            }
        }
        if (!verified) {
            throw new ServiceException(BizExceptionCode.PKI_003, BizExceptionCode.PKI_003_MSG);
        }
    }

    /**
     * Convert one Document Object to String Object
     *
     * @param doc
     * @return
     * @throws javax.xml.transform.TransformerException
     */
    private String convertDocumentToString(Document doc) throws BaseException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            throw new ServiceException(BizExceptionCode.PKI_004, e.getMessage());
        } catch (TransformerException e) {
            throw new ServiceException(BizExceptionCode.PKI_005, e.getMessage());
        }
        return writer.toString();
    }

    private Document convertStringToDocument(String str) {

		/*InputStream is = new ByteArrayInputStream(str.getBytes());
        javax.xml.parsers.DocumentBuilderFactory factory =
				javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		javax.xml.parsers.DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (javax.xml.parsers.ParserConfigurationException ex) {
		}
		Document document = null;
		try {
			document = builder.parse(is);
			is.close();
		} catch (SAXException e) {
			//			logger.error(BizExceptionCode.GLB_ERR_MSG, e);
		} catch (IOException e) {
			//			logger.error(BizExceptionCode.GLB_ERR_MSG, e);
		}
		return document;*/

        InputSource source = new InputSource(new StringReader(str));
        DOMParser parser = new DOMParser();
        try {
            parser.parse(source);
        } catch (SAXException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        } catch (IOException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        }
        Document document = parser.getDocument();
        logger.info(document.toString());
        return document;

    }

    /**
     * The method issuePersonTokenRequest is used to send a request to the sub system 'PKI' for issuing a token for
     * person.
     *
     * @param personTokenTO is an object of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO}, which carries the
     *                      required field for issuePersonTokenRequest
     * @param certificateTO is an object of type {@link com.gam.nocr.ems.data.domain.CertificateTO}, which carries the
     *                      required field for signing request and verifying the response
     * @return an object of type {@link PersonTokenTO}
     */
    @Override
    public PersonTokenTO issuePersonTokenRequest(PersonTokenTO personTokenTO,
                                                 CertificateTO certificateTO) throws BaseException {
        logger.info("The process 'issuePersonTokenRequest' is started...");
        pkiLogger.info("The process 'issuePersonTokenRequest' is started...");

        if (personTokenTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_014, BizExceptionCode.PKI_014_MSG);
        }

        if (certificateTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_015, BizExceptionCode.PKI_015_MSG);
        }

        /**
         * OfferId Preparation
         */
        String offerId = prepareOfferId(personTokenTO.getType());

        /**
         * Protocol Version Preparation
         */
        String protocolVersion = prepareProtocolVersion();

        String result = "";
        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("id", ENROLLMENT_ID_PREFIX + personTokenTO.getId());
        map.put(OFFER_ID, offerId);
        map.put(MESSAGE_ID, "0000");
        map.put(PROTOCOL_VERSION, protocolVersion);
        byte[] byteRequest = xmlMapperProvider.writeXML(personTokenTO, map);

        /**
         * Signing the request
         */
        String signedRequest = signRequest(byteRequest, certificateTO);

        /**
         * Sending the request
         */
        result = sendPKIRequest(signedRequest);

        /**
         * Verifying the response
         */
        verifyPKIResponse(result, certificateTO);

        /**
         * Mapping verified request to an instance of PersonTokenTO
         */
//		TODO : Refactor exceptionHandling on future
        PersonTokenTO tempTokenTo = null;
        try {
            tempTokenTo = (PersonTokenTO) xmlMapperProvider.readXML(result, personTokenTO);
        } catch (BaseException e) {
            String errorMessage = e.getMessage();
            String errorCode = e.getExceptionCode();
            if (WS_00400010.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_011, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));

            } else if (WS_00400080.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_016, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            }

            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
            throw new ServiceException(reformedExceptionCode, e.getMessage());
        }
        logger.info("The process 'issuePersonTokenRequest' is finished.");
        pkiLogger.info("The process 'issuePersonTokenRequest' is finished.");
        return tempTokenTo;
    }
    
    @Override
    public PersonTokenTO activatePersonToken(PersonTokenTO personTokenTO, CertificateTO certificateTO) throws BaseException 
    {
    	logger.info("The process 'activatePersonToken' is started...");
        pkiLogger.info("The process 'activatePersonToken' is started...");

        if (personTokenTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_030, BizExceptionCode.PKI_014_MSG);
        }

        if (certificateTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_031, BizExceptionCode.PKI_015_MSG);
        }

        /**
         * OfferId Preparation
         */
        String offerId = prepareOfferId(personTokenTO.getType());

        /**
         * Protocol Version Preparation
         */
        String protocolVersion = prepareProtocolVersion();

        String result = "";
        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("id", ACTIVATION_ID_PREFIX + personTokenTO.getId());
        map.put(OFFER_ID, offerId);
        map.put(MESSAGE_ID, "0000");
        map.put(PROTOCOL_VERSION, protocolVersion);
        byte[] byteRequest = xmlMapperProvider.writeXML(personTokenTO, map);

        /**
         * Signing the request
         */
        String signedRequest = signRequest(byteRequest, certificateTO);

        /**
         * Sending the request
         */
        result = sendPKIRequest(signedRequest);

        /**
         * Verifying the response
         */
        verifyPKIResponse(result, certificateTO);

        /**
         * Mapping verified request to an instance of PersonTokenTO
         */
//		TODO : Refactor exceptionHandling on future
        PersonTokenTO tempTokenTo = null;
        try {
            tempTokenTo = (PersonTokenTO) xmlMapperProvider.readXML(result, personTokenTO);
        } catch (BaseException e) {
            String errorMessage = e.getMessage();
            String errorCode = e.getExceptionCode();
            if (WS_00400091.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_032, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));

            } else if (WS_00000020.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_033, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            } else if (WS_00000040.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_034, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            }

            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
            throw new ServiceException(reformedExceptionCode, e.getMessage());
        }
        logger.info("The process 'activatePersonToken' is finished.");
        pkiLogger.info("The process 'activatePersonToken' is finished.");
        return tempTokenTo;
    }
    
    
    @Override
    public PersonTokenTO renewalPersonTokenRequest(PersonTokenTO personTokenTO,
                                                 CertificateTO certificateTO) throws BaseException {
        logger.info("The process 'renewalPersonTokenRequest' is started...");
        pkiLogger.info("The process 'renewalPersonTokenRequest' is started...");

        if (personTokenTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_025, BizExceptionCode.PKI_025_MSG);
        }

        if (certificateTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_026, BizExceptionCode.PKI_026_MSG);
        }

        /**
         * OfferId Preparation
         */
        String offerId = prepareOfferId(personTokenTO.getType());

        /**
         * Protocol Version Preparation
         */
        String protocolVersion = prepareProtocolVersion();

        String result = "";
        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("id", RENEWAL_ID_PREFIX + personTokenTO.getId());
        map.put(OFFER_ID, offerId);
        map.put(MESSAGE_ID, "0000");
        map.put(PROTOCOL_VERSION, protocolVersion);
        byte[] byteRequest = xmlMapperProvider.writeXML(personTokenTO, map);

        /**
         * Signing the request
         */
        String signedRequest = signRequest(byteRequest, certificateTO);

        /**
         * Sending the request
         */
        result = sendPKIRequest(signedRequest);

        /**
         * Verifying the response
         */
        verifyPKIResponse(result, certificateTO);

        /**
         * Mapping verified request to an instance of PersonTokenTO
         */
//		TODO : Refactor exceptionHandling on future
        PersonTokenTO tempTokenTo = null;
        try {
            tempTokenTo = (PersonTokenTO) xmlMapperProvider.readXML(result, personTokenTO);
        } catch (BaseException e) {
            String errorMessage = e.getMessage();
            String errorCode = e.getExceptionCode();
            if (WS_00000020.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PKI_027, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            }
            else if(WS_00010060.equals(errorCode)){
            	throw new ServiceException(BizExceptionCode.PKI_028, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            }
            else if(WS_00000040.equals(errorCode)){
            	throw new ServiceException(BizExceptionCode.PKI_029, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
            }
            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
            throw new ServiceException(reformedExceptionCode, e.getMessage());
        }
        logger.info("The process 'renewalPersonTokenRequest' is finished.");
        pkiLogger.info("The process 'renewalPersonTokenRequest' is finished.");
        return tempTokenTo;
    }

    /**
     * The method issueNetworkTokenRequest is used to send a request to the sub system 'PKI' for issuing a token of type
     * network.
     *
     * @param networkTokenTO is an object of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO}, which carries the
     *                       required field for
     *                       issueNetworkTokenRequest
     * @param certificateTO  is an object of type {@link CertificateTO}, which carries the required field for
     *                       signing request and verifying the response
     * @return an object of type {@link NetworkTokenTO}
     */
//Commented By Adldoost
//    @Override
//    public NetworkTokenTO issueNetworkTokenRequest(NetworkTokenTO networkTokenTO,
//                                                   CertificateTO certificateTO) throws BaseException {
//        logger.info("The process 'issueNetworkTokenRequest' is started...");
//        pkiLogger.info("The process 'issueNetworkTokenRequest' is started...");
//
//        if (networkTokenTO == null) {
//            throw new ServiceException(BizExceptionCode.PKI_018, BizExceptionCode.PKI_018_MSG);
//        }
//
//        if (certificateTO == null) {
//            throw new ServiceException(BizExceptionCode.PKI_019, BizExceptionCode.PKI_015_MSG);
//        }
//
//        /**
//         * OfferId Preparation
//         */
//        String offerId = prepareOfferId(TokenType.NETWORK);
//
//        /**
//         * Protocol Version Preparation
//         */
//        String protocolVersion = prepareProtocolVersion();
//
//        String result = "";
//
//        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
//        HashMap map = new HashMap();
//        map.put("id", ENROLLMENT_ID_PREFIX + networkTokenTO.getId());
//        map.put(OFFER_ID, offerId);
//        map.put(MESSAGE_ID, "0000");
//        map.put(PROTOCOL_VERSION, protocolVersion);
//        byte[] byteRequest = xmlMapperProvider.writeXML(networkTokenTO, map);
//
//        /**
//         * Signing the request
//         */
//        String signedRequest = signRequest(byteRequest, certificateTO);
//
//        /**
//         * Sending the request
//         */
//        result = sendPKIRequest(signedRequest);
//
//        /**
//         * Verifying the response
//         */
//        verifyPKIResponse(result, certificateTO);
//
//        /**
//         * Mapping verified request to an instance of PersonTokenTO
//         */
//        //		TODO : Refactor the exceptionHandling on future
//        NetworkTokenTO tempTokenTo = null;
//        try {
//            tempTokenTo = (NetworkTokenTO) xmlMapperProvider.readXML(result, networkTokenTO);
//        } catch (BaseException e) {
//            String errorMessage = e.getMessage();
//            String errorCode = e.getExceptionCode();
//
//            if (WS_00400010.equals(errorCode)) {
//                throw new ServiceException(BizExceptionCode.PKI_010, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
//
//            } else if (WS_00400080.equals(errorCode)) {
//                throw new ServiceException(BizExceptionCode.PKI_024, errorMessage, e, EMSLogicalNames.SRV_PKI.split(","));
//            }
//
//            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
//            throw new ServiceException(reformedExceptionCode, e.getMessage());
//
//        }
//        logger.info("The process 'issueNetworkTokenRequest' is finished.");
//        pkiLogger.info("The process 'issueNetworkTokenRequest' is finished.");
//        return tempTokenTo;
//    }

    /**
     * The method revokeTokenRequest is used to send a request to the sub system 'PKI' for revoking any type of token.
     *
     * @param tokenTO       is an object of type {@link com.gam.nocr.ems.data.domain.TokenTO}, which carries the required
     *                      fields for revocation request
     * @param tokenType     represents the type of token in spite of the enumeration {@link TokenType}
     * @param certificateTO is an object of type {@link CertificateTO}, which carries the required field for
     *                      signing request and verifying the response
     * @param comment
     */
    @Override
    public void revokeTokenRequest(TokenTO tokenTO,
                                   TokenType tokenType,
                                   CertificateTO certificateTO,
                                   String comment) throws BaseException {
        logger.info("The process 'revokeTokenRequest' is started...");
        pkiLogger.info("The process 'revokeTokenRequest' is started...");

        if (tokenTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_020, BizExceptionCode.PKI_020_MSG);
        }

        if (certificateTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_021, BizExceptionCode.PKI_015_MSG);
        }

        /**
         * OfferId Preparation
         */
        String offerId = prepareOfferId(tokenType);

        /**
         * Protocol Version Preparation
         */
        String protocolVersion = prepareProtocolVersion();

        String result = "";

        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("id", REVOCATION_ID_PREFIX + tokenTO.getId());
        map.put(OFFER_ID, offerId);
        map.put(MESSAGE_ID, "0000");
        map.put(PROTOCOL_VERSION, protocolVersion);
        map.put(COMMENT, comment);

//		TODO : =================================================
//		TODO : Refactoring it on future
        tokenTO.setReason(TokenReIssuanceReason.unspecified);
//		TODO : =================================================

        byte[] byteRequest = xmlMapperProvider.writeXML(tokenTO, map);

        /**
         * Signing the request
         */
        String signedRequest = signRequest(byteRequest, certificateTO);

        /**
         * Sending the request
         */
        result = sendPKIRequest(signedRequest);

        /**
         * Verifying the response
         */
        verifyPKIResponse(result, certificateTO);

        /**
         * Mapping verified request to an instance of TokenTO
         */
//		TODO : Refactor the method of exceptionHandling on future
        try {
            xmlMapperProvider.readXML(result, tokenTO);
        } catch (BaseException e) {
            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
            throw new ServiceException(reformedExceptionCode, e.getMessage());
        }
        logger.info("The process 'revokeTokenRequest' is finished.");
        pkiLogger.info("The process 'revokeTokenRequest' is finished.");
    }

    /**
     * The method searchTokenRequest is used to send a request to the sub system 'PKI' for searching any type of token.
     *
     * @param tokenTO       is an object of type {@link com.gam.nocr.ems.data.domain.TokenTO}, which carries the required
     *                      fields for revocation request
     * @param tokenType     represents the type of token in spite of the enumeration {@link TokenType}
     * @param certificateTO is an object of type {@link CertificateTO}, which carries the required field for
     *                      signing request and verifying the response
     * @return an object of type {@link TokenTO}
     */
    @Override
    public TokenTO searchTokenRequest(TokenTO tokenTO,
                                      TokenType tokenType,
                                      CertificateTO certificateTO) throws BaseException {
        logger.info("The process 'searchTokenRequest' is started...");
        pkiLogger.info("The process 'searchTokenRequest' is started...");

        if (tokenTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_022, BizExceptionCode.PKI_020_MSG);
        }

        if (certificateTO == null) {
            throw new ServiceException(BizExceptionCode.PKI_023, BizExceptionCode.PKI_015_MSG);
        }

        /**
         * OfferId Preparation
         */
        String offerId = prepareOfferId(tokenType);

        /**
         * Protocol Version Preparation
         */
        String protocolVersion = prepareProtocolVersion();

        String result = "";

        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("id", SEARCH_ID_PREFIX + tokenTO.getId());
        map.put(OFFER_ID, offerId);
        map.put(MESSAGE_ID, "0000");
        map.put(PROTOCOL_VERSION, protocolVersion);
        byte[] byteRequest = xmlMapperProvider.writeXML(tokenTO, map);

        /**
         * Signing the request
         */
        String signedRequest = signRequest(byteRequest, certificateTO);

        /**
         * Sending the request
         */
        result = sendPKIRequest(signedRequest);

        /**
         * Verifying the response
         */
        verifyPKIResponse(result, certificateTO);

        /**
         * Mapping verified request to an instance of TokenTO
         */

//		TODO : Refactor the method of exceptionHandling on future
        try {
            xmlMapperProvider.readXML(result, tokenTO);
        } catch (BaseException e) {
            String reformedExceptionCode = PKI_ERROR_PREFIX + e.getExceptionCode().split("-")[1];
            throw new ServiceException(reformedExceptionCode, e.getMessage());
        }

//		TODO : If the certificate has issued, turn the state of tokenTO to PROCESSED
        logger.info("The process 'searchTokenRequest' is finished.");
        pkiLogger.info("The process 'searchTokenRequest' is finished.");
        return tokenTO;
    }
}
