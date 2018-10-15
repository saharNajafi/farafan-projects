package com.gam.nocr.ems.biz.service.external.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.ValidationException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.xmldsig.certificatebase.XmlDSigCertUtil;
import com.gam.nocr.ems.biz.service.CardManagementService;
import com.gam.nocr.ems.biz.service.external.client.cms.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.BiometricInfoDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.CardApplicationInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.mapper.xmlmapper.XMLMapperProvider;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import servicePortUtil.ServicePorts;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_BIOMETRIC_INFO;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "CMSService")
@Local(CMSServiceLocal.class)
@Remote(CMSServiceRemote.class)
public class CMSServiceImpl extends AbstractService implements CMSServiceLocal, CMSServiceRemote {

    /**
     * CMS Exception Codes
     */
    private static final String ISS_000001 = "ISS-000001";
    private static final String ISS_000002 = "ISS-000002";
    private static final String ISS_000003 = "ISS-000003";
    private static final String ISS_000004 = "ISS-000004";
    private static final String ISS_000005 = "ISS-000005";
    private static final String ISS_000006 = "ISS-000006";
    private static final String ISS_000007 = "ISS-000007";
    private static final String ISS_000008 = "ISS-000008";
    private static final String ISS_000009 = "ISS-000009";
    private static final String ISS_000010 = "ISS-000010";
    private static final String ISS_000011 = "ISS-000011";
    private static final String ISS_000012 = "ISS-000012";
    private static final String ISS_000013 = "ISS-000013";
    private static final String ISS_000014 = "ISS-000014";
    private static final String ISS_NID_000001 = "ISS-NID-000001";

    private static final String SHPT_000001 = "SHPT-000001";
    private static final String SHPT_000004 = "SHPT-000004";
    private static final String SHPT_000005 = "SHPT-000005";
    private static final String SHPT_000006 = "SHPT-000006";
    private static final String SHPT_000007 = "SHPT-000007";
    private static final String SHPT_000008 = "SHPT-000008";
    private static final String SHPT_000009 = "SHPT-000009";
    private static final String SHPT_000010 = "SHPT-000010";
    private static final String SHPT_000011 = "SHPT-000011";

    private static final String CARD_000001 = "CARD-000001";
    private static final String CARD_000002 = "CARD-000002";
    private static final String CARD_000003 = "CARD-000003";
    private static final String CARD_000004 = "CARD-000004";
    private static final String CARD_000005 = "CARD-000005";
    private static final String CARD_000009 = "CARD-000009";
    private static final String CARD_000010 = "CARD-000010";
    private static final String CARD_000011 = "CARD-000011";

    private static final String SITE_000001 = "SITE-000001";
    private static final String SITE_000002 = "SITE-000002";
    private static final String SITE_000003 = "SITE-000003";
    private static final String SITE_000004 = "SITE-000004";
    private static final String SITE_000005 = "SITE-000005";
    private static final String SITE_000006 = "SITE-000006";
    private static final String SITE_000007 = "SITE-000007";
    private static final String SITE_000008 = "SITE-000008";
    private static final String SITE_000009 = "SITE-000009";
    private static final String SITE_000010 = "SITE-000010";

    private static final String CA_NET_000001 = "CA-NET-000001";
    private static final String CA_NET_000002 = "CA-NET-000002";
    private static final String CA_000003 = "CA-000003";
    private static final String CA_000004 = "CA-000004";
    private static final String CA_000005 = "CA-000005";
    private static final String CA_000006 = "CA-000006";

    /**
     * Card Issuance Types
     */
    private static final Integer ISSUE_TYPE_FIRST = 1;
    private static final Integer ISSUE_TYPE_EXTEND = 2;
    private static final Integer ISSUE_TYPE_REPLICA = 3;
    private static final Integer ISSUE_TYPE_REPLACE = 4;
    private static final Integer ISSUE_TYPE_UNSUCCESSFUL_DELIVERY = 5;

    /**
     * Default values for web services
     */
    private static final String DEFAULT_WSDL_URL = "http://localhost:8080/gto-iran-cms-ems-stub-war-1.0.6/cpm-ws-input/DocumentRequestWS?wsdl";
    private static final String DEFAULT_NAMESPACE = "http://document.ws.cms.iran.gemalto.com/";

    /**
     * Default values for two parameters of CMS issueCard method
     */
    private static final String DEFAULT_PRODUCT_ID = "200";
    private static final String DEFAULT_PRODUCT_VERSION = "1.0";
    private static final Integer DEFAULT_REQUEST_PRIORITY = 1;

    HashMap issueTypeHashMap = new HashMap();

    private static final Logger logger = BaseLog.getLogger(CMSServiceImpl.class);
    private static final Logger cmsLogger = BaseLog.getLogger("CmsLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");


    DocumentRequestWSLocal service = null;

    //	TODO : Do not forget completing this method on future
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    /**
     * The method getService is used to get WebServices from CMS sub system
     *
     * @return an instance of type {@link DocumentRequestWSLocal}
     * @throws BaseException if cannot get the service
     */
    private DocumentRequestWSLocal getService() throws BaseException {
        String wsdlUrl = "";
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_CMS_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "DocumentRequestWSService";
            logger.debug("Cms wsdl url: " + wsdlUrl);
            cmsLogger.debug("Cms wsdl url: " + wsdlUrl);
//            logger.info("javax.net.ssl.keyStore is : " + System.getProperty("javax.net.ssl.keyStore"));
//            logger.info("javax.net.ssl.trustStore is : " + System.getProperty("javax.net.ssl.trustStore"));

//            System.setProperty("javax.net.ssl.keyStore","/Oracle/Middleware/wlserver_12.1/certificates/CSS.jks");
//            System.setProperty("javax.net.ssl.keyStorePassword", "p@ssw0rd");
//            System.setProperty("javax.net.ssl.keyStoreType", "jks");
//            System.setProperty("javax.net.ssl.trustStore","/Oracle/Middleware/wlserver_12.1/certificates/servertrust.jks");
//            System.setProperty("javax.net.ssl.trustStorePassword", "p@ssw0rd");

            //Commented for ThreadLocal
            //DocumentRequestWSLocal port = new DocumentRequestWSService(new URL(wsdlUrl), new QName(namespace, serviceName)).getDocumentRequestWSPort();
            DocumentRequestWSLocal port = ServicePorts.getDocumentCMSPort();
            if (port == null) {
                threadLocalLogger.debug("**************************** new DocumentRequestWSLocal in CMS getService()");
                port = new DocumentRequestWSService(new URL(wsdlUrl),
                        new QName(namespace, serviceName))
                        .getDocumentRequestWSPort();
                ServicePorts.setDocumentCMSPort(port);
            } else {
                threadLocalLogger.debug("***************************** using DocumentRequestWSLocal(CMS) from ThradLocal");
            }
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (NullPointerException e) {
            logger.error(BizExceptionCode.CSI_150, BizExceptionCode.CSI_150_MSG, e);
            cmsLogger.error(BizExceptionCode.CSI_150 + " : " + BizExceptionCode.CSI_150_MSG, e);
            throw new ServiceException(BizExceptionCode.CSI_150, BizExceptionCode.CSI_150_MSG, new String[]{wsdlUrl});
        } catch (Exception e) {
            logger.error(BizExceptionCode.CSI_114, e.getMessage(), e);
            cmsLogger.error(BizExceptionCode.CSI_114 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CSI_114, e.getMessage(), e);
        }
    }

    /**
     * getCardManagementService
     *
     * @return an instance of type {@link CardManagementService}
     * @throws {@link BaseException}
     */
    private CardManagementService getCardManagementService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        CardManagementService cardManagementService = null;
        try {
            cardManagementService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_CARD_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            cmsLogger.error(BizExceptionCode.CSI_137 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CSI_137,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_CARD_MANAGEMENT.split(","));
        }
        return cardManagementService;
    }

    /**
     * The private method convertToCardInfoVTO is used to convert an object of type CardInfo to an object of
     * type CardInfoVTO
     *
     * @param cardInfo is an object of type {@link CardInfo}
     * @return an object of type {@link CardInfoVTO}
     */

    private CardInfoVTO convertToCardInfoVTO(CardInfo cardInfo) {
        return new CardInfoVTO(
                cardInfo.getCitizenID(),
                cardInfo.getCrn(),
                cardInfo.getIssuanceDate(),
                cardInfo.getProductID(),
                cardInfo.getProductVersion(),
                cardInfo.getReason(),
                cardInfo.getRequestID(),
                cardInfo.getStatus()
        );
    }

    /**
     * The private method convertToCardApplicationInfoVTO is used to convert an object of type CardApplicationInfo to an
     * object of
     * type CardApplicationInfoVTO
     *
     * @param cardApplicationInfo is an object of type {@link CardApplicationInfo}
     * @return an object of type {@link CardApplicationInfoVTO}
     */
    private CardApplicationInfoVTO convertToCardApplicationInfoVTO(CardApplicationInfo cardApplicationInfo) {
        return new CardApplicationInfoVTO(
                cardApplicationInfo.getDescription(),
                cardApplicationInfo.getId(),
                cardApplicationInfo.getName(),
                cardApplicationInfo.getReason(),
                cardApplicationInfo.getStatus()
        );
    }

    /**
     * The private method convertToUserSiteInfo is used to convert an object of type EnrollmentOfficeTO to an object of
     * type
     * UserSiteInfo
     *
     * @param enrollmentOfficeTO is an object of type {@link EnrollmentOfficeTO}
     * @param enableStatus       1 for enabled and 2 for disabled
     * @return an object of type {@link UserSiteInfo}
     */
    private UserSiteInfo convertToUserSiteInfo(EnrollmentOfficeTO dbEnrollmentOffice, int enableStatus) throws BaseException {
        try {

            //Anbari
            UserSiteInfo userSiteInfo = null;
            if (dbEnrollmentOffice != null) {
                userSiteInfo = new UserSiteInfo();
                userSiteInfo.setUserSiteID(String.valueOf(dbEnrollmentOffice.getId()));
                userSiteInfo.setUserSiteCode(dbEnrollmentOffice.getCode());
                userSiteInfo.setUserSiteName(dbEnrollmentOffice.getName());
                userSiteInfo.setIsPostNeeded(dbEnrollmentOffice.getPostNeeded() ? 1 : 2);
                userSiteInfo.setPostDestinationCode(dbEnrollmentOffice.getPostDestinationCode() != null ? dbEnrollmentOffice.getPostDestinationCode() : "");
                String userSiteContact = dbEnrollmentOffice.getAddress() + ", " +
                        dbEnrollmentOffice.getPhone() + ", " +
                        dbEnrollmentOffice.getFax();
                if (userSiteContact.length() > 250) {
                    userSiteContact = userSiteContact.substring(0, 250);
                }
                userSiteInfo.setUserSiteContact(userSiteContact);
                userSiteInfo.setUserSitePostalCode(dbEnrollmentOffice.getPostalCode());

                // Anbari (if office has got enable delivery feature act like NOCR office)
                if (dbEnrollmentOffice.getSuperiorOffice() == null || EnrollmentOfficeDeliverStatus.ENABLED.equals(dbEnrollmentOffice.getDeliver())) {

                    userSiteInfo.setNocrOfficeID(String.valueOf(dbEnrollmentOffice.getId()));
                    userSiteInfo.setNocrOfficeCode(dbEnrollmentOffice.getCode());
                    userSiteInfo.setNocrOfficeName(dbEnrollmentOffice.getName());
                    String nocrOfficeContact = dbEnrollmentOffice.getAddress() + ", " +
                            dbEnrollmentOffice.getPhone() + ", " +
                            dbEnrollmentOffice.getFax();
                    if (nocrOfficeContact.length() > 250) {
                        nocrOfficeContact = nocrOfficeContact.substring(0, 250);
                    }
                    userSiteInfo.setNocrOfficeContact(nocrOfficeContact);
                    userSiteInfo.setNocrOfficePostalCode(dbEnrollmentOffice.getPostalCode());
                } else {

                    userSiteInfo.setNocrOfficeID(String.valueOf(dbEnrollmentOffice.getSuperiorOffice().getId()));
                    userSiteInfo.setNocrOfficeCode(dbEnrollmentOffice.getSuperiorOffice().getCode());
                    userSiteInfo.setNocrOfficeName(dbEnrollmentOffice.getSuperiorOffice().getName());
                    String nocrOfficeContact = dbEnrollmentOffice.getSuperiorOffice().getAddress() + ", " +
                            dbEnrollmentOffice.getSuperiorOffice().getPhone() + ", " +
                            dbEnrollmentOffice.getSuperiorOffice().getFax();
                    if (nocrOfficeContact.length() > 250) {
                        nocrOfficeContact = nocrOfficeContact.substring(0, 250);
                    }
                    userSiteInfo.setNocrOfficeContact(nocrOfficeContact);
                    userSiteInfo.setNocrOfficePostalCode(dbEnrollmentOffice.getSuperiorOffice().getPostalCode());

                }
//                if (dbEnrollmentOffice.getParentDepartment().getLocation().getType().equals("1")) {
//                    if (dbEnrollmentOffice.getParentDepartment().getLocation().getCounty() != null)
//                        userSiteInfo.setNocrOfficeStateName(dbEnrollmentOffice.getParentDepartment().
//                                getLocation().getCounty().getName());
//                } else if (dbEnrollmentOffice.getParentDepartment().getLocation().getType().equals("2")) {
//                    if (dbEnrollmentOffice.getParentDepartment().getLocation().getDistrict() != null)
//                        userSiteInfo.setNocrOfficeStateName(dbEnrollmentOffice.getParentDepartment().
//                                getLocation().getDistrict().getName());
//                }

                if (dbEnrollmentOffice.getParentDepartment().getLocation().getType().equals("1") ||
                        dbEnrollmentOffice.getParentDepartment().getLocation().getType().equals("2")) {
                    userSiteInfo.setNocrOfficeStateName(dbEnrollmentOffice.getParentDepartment().getLocation().getProvince().getName());
                }

                userSiteInfo.setStatus(enableStatus);
            }
            return userSiteInfo;
        } catch (Exception e) {
            cmsLogger.error(BizExceptionCode.CSI_147 + " : " + BizExceptionCode.GLB_008_MSG, e);
            throw new ServiceException(BizExceptionCode.CSI_147, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.EOS_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
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
        try {
            HashMap xmlDSigMap = new HashMap();
            xmlDSigMap.put(XmlDSigCertUtil.CERTIFICATE, certificateTO.getCertificate());
            xmlDSigMap.put(XmlDSigCertUtil.PASSWORD, certificateTO.getCode());
            InputStream is = new ByteArrayInputStream(byteRequest);
            XmlDSigCertUtil xmlDSigCertUtil = new XmlDSigCertUtil(xmlDSigMap);
            Document signedDoc = xmlDSigCertUtil.signXML(is);

//		================================================================================================================
//		=========================JUST FOR TEST(Creating a file and copy the signed request to it)=======================
//		OutputStream outputStream = null;
//		try {
//			outputStream = new FileOutputStream("C:\\Users\\jalilian\\Desktop\\issue_001.xml");
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.transform(new DOMSource(signedDoc), new StreamResult(outputStream));
//		} catch (FileNotFoundException e) {
//			logger.error(BizExceptionCode.GLB_ERR_MSG, e);
//		} catch (TransformerConfigurationException e) {
//			logger.error(BizExceptionCode.GLB_ERR_MSG, e);
//		} catch (TransformerException e) {
//			logger.error(BizExceptionCode.GLB_ERR_MSG, e);
//		}
//		================================================================================================================
//		================================================================================================================

            return convertDocumentToString(signedDoc);
        } catch (BaseException e) {
            cmsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            cmsLogger.error(BizExceptionCode.CSI_146 + " : " + BizExceptionCode.CSI_146_MSG, e);
            throw new ServiceException(BizExceptionCode.CSI_146, BizExceptionCode.CSI_146_MSG, e);
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
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            cmsLogger.error(BizExceptionCode.CSI_113 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CSI_113, e.getMessage());
        } catch (TransformerException e) {
            cmsLogger.error(BizExceptionCode.CSI_114 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CSI_014, e.getMessage());
        }
        return writer.toString();
    }

    public CMSServiceImpl() {
        issueTypeHashMap.put(CardRequestType.FIRST_CARD, ISSUE_TYPE_FIRST);
        issueTypeHashMap.put(CardRequestType.EXTEND, ISSUE_TYPE_EXTEND);
        issueTypeHashMap.put(CardRequestType.REPLICA, ISSUE_TYPE_REPLICA);
        issueTypeHashMap.put(CardRequestType.REPLACE, ISSUE_TYPE_REPLACE);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
//		issueTypeHashMap.put(CardRequestType.DESTROYED, ISSUE_TYPE_DESTROYED);//todo: should this be commented??
    }

    /**
     * The issueCard method is used for sending the card issuance request to CMS subsystem.
     *
     * @param cardRequestTO is the object which encapsulates the attributes which are needed for the process 'issue card'
     * @param requestId     is an object of type String which shows the request id of a specified card issuance request
     * @param certificateTO is an object of type {@link com.gam.nocr.ems.data.domain.CertificateTO}, which carries the
     *                      required field for signing request
     */
    @Override
    public void issueCard(CardRequestTO cardRequestTO,
                          String requestId, CertificateTO certificateTO) throws BaseException {
        logger.info("The process 'issueCard' is started...");
        cmsLogger.info("The process 'issueCard' is started...");

        List<SpouseTO> spouseTOs = cardRequestTO.getCitizen().getCitizenInfo().getSpouses();
        int currentSpousesCount = 0;
        int recordedSpousesCount = 0;
        int MARITAL_STATUS_STABLE = 1;
        for (SpouseTO spouseTO : spouseTOs) {
            if (spouseTO.getMaritalStatus().getId() == MARITAL_STATUS_STABLE) {
                currentSpousesCount++;
            }
        }

//		TODO : Dummy assignment
        recordedSpousesCount = spouseTOs.size();

        List<ChildTO> children = cardRequestTO.getCitizen().getCitizenInfo().getChildren();
        int currentChildrenCount = 0;
        int recordedChildrenCount = 0;
        for (ChildTO childTO : children) {
            if (childTO.getChildDeathDateSolar() == null) {
                currentChildrenCount++;
            }
        }
        BiometricInfoTO biometricInfoTO = getBiometricInfoDAO().findByNid(cardRequestTO.getCitizen().getNationalID());

//		TODO : Dummy assignment
        recordedChildrenCount = children.size();

        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();

        map.put("id", requestId);
        map.put("currentSpousesCount", currentSpousesCount);
        map.put("recordedSpousesCount", recordedSpousesCount);
        map.put("currentChildrenCount", currentChildrenCount);
        map.put("recordedChildrenCount", recordedChildrenCount);
        map.put("featureExtractorID", biometricInfoTO.getFeatureExtractorID());
        byte[] byteRequest = xmlMapperProvider.writeXML(cardRequestTO, map);

        String validateXml = new String(byteRequest);
        try {
            xmlMapperProvider.validateAgainstXSD(new ByteArrayInputStream(validateXml.getBytes("UTF-8")),
                    getClass().getClassLoader().getResourceAsStream("com/gam/nocr/cms/CMSIssuanceCardRequest.xsd"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Invalid XML Encoding for sending to CMS (CardRequest Id: " + cardRequestTO.getId() + ")");
            throw new ServiceException(BizExceptionCode.CSI_152, "Invalid XML Encoding for sending to EQC (CardRequest Id: " + cardRequestTO.getId() + ")", e);
        } catch (ValidationException ve) {
            logger.error("Error in XSD Validation for sending to CMS (CardRequest Id: " + cardRequestTO.getId() + ")", ve);
            throw new ServiceException(BizExceptionCode.CSI_151, ve.getCause().getMessage(), ve);
        }

        if (byteRequest != null && byteRequest.length != 0) {

            /**
             * Signing the request
             */
            String signedRequest = signRequest(byteRequest, certificateTO);

            if (signedRequest != null && !signedRequest.isEmpty()) {

                logger.debug("==========================================================================================");
                cmsLogger.debug("==========================================================================================");
                logger.debug("================================== SIGNED XML REQUEST ====================================");
                cmsLogger.debug("================================== SIGNED XML REQUEST ====================================");
                logger.debug(signedRequest);
                cmsLogger.debug(signedRequest);
                logger.debug("==========================================================================================");
                cmsLogger.debug("==========================================================================================");
                logger.debug("==========================================================================================");
                cmsLogger.debug("==========================================================================================");

                String productId = null;
                String productVersion = null;
                try {
                    ProfileManager pm = ProfileHelper.getProfileManager();
                    productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
                    productVersion = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_VERSION, true, null, null);
                } catch (Exception e) {
                    logger.warn(BizExceptionCode.CSI_138, BizExceptionCode.CSI_138_MSG, e);
                    cmsLogger.warn(BizExceptionCode.CSI_138, BizExceptionCode.CSI_138_MSG, e);
                }

                if (productId == null || productId.isEmpty()) {
                    productId = DEFAULT_PRODUCT_ID;
                }

                if (productVersion == null || productVersion.isEmpty()) {
                    productVersion = DEFAULT_PRODUCT_VERSION;
                }

                Integer priority = cardRequestTO.getPriority();
                if (priority == null) {
                    priority = DEFAULT_REQUEST_PRIORITY;
                }

                /**
                 * Sending the request
                 */
                try {
                    getService().issueCard(
                            requestId,
                            priority,
                            productId,
                            productVersion,
                            (Integer) issueTypeHashMap.get(cardRequestTO.getType()),
                            cardRequestTO.getReason(),
                            signedRequest.getBytes()
                    );
                } catch (ExternalInterfaceException_Exception e) {
                    String errorMessage = e.getFaultInfo().getMessage();
                    String errorCode = e.getFaultInfo().getErrorCode();
                    if (ISS_000001.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_001,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000002.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_002,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000003.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_003,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000004.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_004,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000005.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_005,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000006.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_006,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000007.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_007,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000008.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_008,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000009.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_009,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000010.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_010,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000011.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_011,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000012.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_012,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000013.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_013,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_000014.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_136,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    } else if (ISS_NID_000001.equals(errorCode)) {
                        ServiceException serviceException = new ServiceException(
                                BizExceptionCode.CSI_145,
                                errorMessage,
                                e,
                                EMSLogicalNames.SRV_CMS.split(","));
                        logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                        throw serviceException;

                    }
                    ServiceException serviceException = new ServiceException(
                            BizExceptionCode.CSI_116,
                            errorMessage,
                            e,
                            EMSLogicalNames.SRV_CMS.split(","));
                    logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                    cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                    throw serviceException;

                }
            } else {
                logger.warn(BizExceptionCode.CSI_149_MSG);
                cmsLogger.warn(BizExceptionCode.CSI_149_MSG);
            }

        } else {
            logger.warn(BizExceptionCode.CSI_148_MSG);
            cmsLogger.warn(BizExceptionCode.CSI_148_MSG);
        }
        logger.info("The process 'issueCard' is finished.");
        cmsLogger.info("The process 'issueCard' is finished.");
    }

    /**
     * The batchReceipt method is used to assure the sub system 'CMS' which the batch has completely received
     * by sub system 'EMS'
     *
     * @param batchId represents the batchId of the card
     */
    @Override
    public void batchReceipt(String batchId) throws BaseException {
        logger.info("The process 'batchReceipt' is started...");
        cmsLogger.info("The process 'batchReceipt' is started...");
        try {
            logger.info("Calling 'batchReceipt' service for 'batchId' : " + batchId);
            cmsLogger.info("Calling 'batchReceipt' service for 'batchId' : " + batchId);
            getService().batchReceipt(batchId);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'batchReceipt' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'batchReceipt' service failed because of : " + errorCode + " - " + errorMessage);
            if (SHPT_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_014,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_015,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_016,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_017,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_117,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;

        }
        logger.info("The process 'batchReceipt' is finished.");
        cmsLogger.info("The process 'batchReceipt' is finished.");
    }

    /**
     * The batchMissed method is used to inform the sub system 'CMS' from missing the batch by sub system 'EMS'
     *
     * @param batchId represents the batchId of the card
     * @param reason  is a description about why the batch has missed
     */
    public void batchMissed(String batchId,
                            String reason) throws BaseException {
        logger.info("The process 'batchMissed' is started...");
        cmsLogger.info("The process 'batchMissed' is started...");
        try {
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'batchMissed' service for 'batchId' : " + batchId + " and 'reason' : " + reason +
                    " and reason replaced with batchMissed");
            cmsLogger.info("Calling 'batchMissed' service for 'batchId' : " + batchId + " and 'reason' : " + reason +
                    " and reason replaced with batchMissed");
            reason = "batchMissed";
            getService().batchMissed(batchId, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'batchMissed' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'batchMissed' service failed because of : " + errorCode + " - " + errorMessage);
            if (SHPT_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_018,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_019,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_020,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_021,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_022,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_023,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_024,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_025,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_118,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'batchMissed' is finished.");
        cmsLogger.info("The process 'batchMissed' is finished.");
    }

    /**
     * The boxMissed method is used to inform the sub system 'CMS' from missing the box by sub system 'EMS'
     *
     * @param boxId  represents the boxId of the card
     * @param reason is a description about why the box has missed
     */
    @Override
    public void boxMissed(String boxId,
                          String reason) throws BaseException {
        logger.info("The process 'boxMissed' is started...");
        cmsLogger.info("The process 'boxMissed' is started...");
        try {
            reason = "boxMissed";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'boxMissed' service for 'boxId' : " + boxId + " and reason : " + reason +
                    " and the reason replaced with boxMissed");
            cmsLogger.info("Calling 'boxMissed' service for 'boxId' : " + boxId + " and reason : " + reason +
                    " and the reason replaced with boxMissed");
            getService().boxMissed(boxId, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'boxMissed' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'boxMissed' service failed because of : " + errorCode + " - " + errorMessage);
            if (SHPT_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_026,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_027,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_028,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_029,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_030,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_031,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_032,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_119,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'boxMissed' is finished.");
    }

    /**
     * The cardMissed method is used to inform the sub system 'CMS' from missing the card by sub system 'EMS'
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about why the card has missed
     */
    @Override
    public void cardMissed(String crn,
                           String reason) throws BaseException {
        logger.info("The process 'cardMissed' is started...");
        cmsLogger.info("The process 'cardMissed' is started...");
        try {
            reason = "cardMissed";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'cardMissed' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with cardMissed");
            cmsLogger.info("Calling 'cardMissed' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with cardMissed");
            getService().cardMissed(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'cardMissed' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'cardMissed' service failed because of : " + errorCode + " - " + errorMessage);
            if (SHPT_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_033,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000007.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_034,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000008.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_035,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000011.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_036,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_037,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_038,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_039,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_120,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'cardMissed' is finished.");
        cmsLogger.info("The process 'cardMissed' is finished.");
    }

    /**
     * The cardHandedOut method is used to inform the sub system 'CMS' from handing out the card by sub system 'EMS'
     *
     * @param crn represents the serial number of the card
     */
    @Override
    public void cardHandedOut(String crn) throws BaseException {
        logger.info("The process 'cardHandedOut' is started...");
        cmsLogger.info("The process 'cardHandedOut' is started...");
        try {
            logger.info("Calling 'cardHandedOut' service for 'crn' : " + crn);
            cmsLogger.info("Calling 'cardHandedOut' service for 'crn' : " + crn);
            getService().cardHandedOut(crn);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'cardHandedOut' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'cardHandedOut' service failed because of : " + errorCode + " - " + errorMessage);
            if (SHPT_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_040,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000007.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_041,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000008.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_042,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_112,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SHPT_000011.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_043,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                //it is a correct state.no change is need---ganjyar
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_044,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_045,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_046,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_121,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'cardHandedOut' is finished.");
        cmsLogger.info("The process 'cardHandedOut' is finished.");
    }

    /**
     * The expireCard method is used to send the request to the sub system 'CMS' to expire the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card expiration time
     */
    @Override
    public void expireCard(String crn, String reason) throws BaseException {
        logger.info("The process 'expireCard' is started...");
        cmsLogger.info("The process 'expireCard' is started...");
        try {
            reason = "expireCard";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'expireCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with expireCard");
            cmsLogger.info("Calling 'expireCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with expireCard");
            getService().expireCard(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'expireCard' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'expireCard' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_047,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_048,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_049,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_050,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_051,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_052,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_053,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_122,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'expireCard' is finished.");
        cmsLogger.info("The process 'expireCard' is finished.");
    }

    /**
     * The suspendCard method is used to send the request to the sub system 'CMS' to suspend the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card suspension
     */
    @Override
    public void suspendCard(String crn,
                            String reason) throws BaseException {
        logger.info("The process 'suspendCard' is started...");
        cmsLogger.info("The process 'suspendCard' is started...");
        try {
            reason = "suspendCard";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'suspendCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with suspendCard");
            cmsLogger.info("Calling 'suspendCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with suspendCard");
            getService().suspendCard(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'suspendCard' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'suspendCard' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_054,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_055,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_056,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_057,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_058,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_059,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_060,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_123,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'suspendCard' is finished.");
        cmsLogger.info("The process 'suspendCard' is finished.");
    }

    /**
     * The resumeCard method is used to send the request to the sub system 'CMS' to resume the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card resumption
     */
    @Override
    public void resumeCard(String crn,
                           String reason) throws BaseException {
        logger.info("The process 'resumeCard' is started...");
        cmsLogger.info("The process 'resumeCard' is started...");
        try {
            reason = "resumeCard";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'resumeCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with resumeCard");
            cmsLogger.info("Calling 'resumeCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with resumeCard");
            getService().resumeCard(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'resumeCard' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'resumeCard' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_061,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_062,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_063,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_064,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_065,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_066,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_067,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_124,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'resumeCard' is finished.");
        cmsLogger.info("The process 'resumeCard' is finished.");
    }

    /**
     * The revokeCard method is used to send the request to the sub system 'CMS' to revoke the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card revocation
     */
    @Override
    public void revokeCard(String crn,
                           String reason) throws BaseException {
        logger.info("The process 'revokeCard' is started...");
        cmsLogger.info("The process 'revokeCard' is started...");
        try {
            reason = "revokeCard";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'revokeCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with revokeCard");
            cmsLogger.info("Calling 'revokeCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with revokeCard");
            getService().revokeCard(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'revokeCard' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'revokeCard' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_068,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_069,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_070,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_071,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                //it is a correct state.no change is need---ganjyar
                throw serviceException;

            } else if (CA_NET_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_072,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_NET_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_073,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CA_000006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_074,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;
            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_115,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'revokeCard' is finished.");
        cmsLogger.info("The process 'revokeCard' is finished.");
    }

    /**
     * The destroyCard method is used to send the request to the sub system 'CMS' to destroy the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card destroying
     */
    @Override
    public void destroyCard(String crn,
                            String reason) throws BaseException {
        logger.info("The process 'destroyCard' is started...");
        cmsLogger.info("The process 'destroyCard' is started...");
        try {
            reason = "destroyCard";
            logger.info("---------------------------");
            cmsLogger.info("---------------------------");
            logger.info("Calling 'destroyCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with destroyCard");
            cmsLogger.info("Calling 'destroyCard' service for 'crn' : " + crn + " and reason : " + reason +
                    " and the reason replaced with destroyCard");
            getService().cardDestroyed(crn, reason);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'destroyCard' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'destroyCard' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_075,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_076,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_077,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_078,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_126,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'destroyCard' is finished.");
        cmsLogger.info("The process 'destroyCard' is finished.");
    }

    /**
     * The method getCitizenCards is used to be gained the information about all cards of the citizen.
     *
     * @param nationalId represents the citizen nationalId
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    @Override
    public List<CardInfoVTO> getCitizenCards(String nationalId) throws BaseException {
        List<CardInfoVTO> cardInfoVTOList = new ArrayList<CardInfoVTO>();
        logger.info("The process 'getCitizenCards' is started...");
        cmsLogger.info("The process 'getCitizenCards' is started...");
        try {
            logger.info("Calling 'getCitizenCards' service for 'nationalId' : " + nationalId);
            cmsLogger.info("Calling 'getCitizenCards' service for 'nationalId' : " + nationalId);
            List<CardInfo> cardInfoList = getService().getCitizenCards(nationalId);
            if (cardInfoList == null || cardInfoList.isEmpty()) {
                throw new ServiceException(BizExceptionCode.CSI_139, BizExceptionCode.CSI_139_MSG, new String[]{nationalId});
            }

            logger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            cmsLogger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            for (CardInfo cardInfo : cardInfoList) {
                if (cardInfo != null) {
                    logger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cmsLogger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cardInfoVTOList.add(convertToCardInfoVTO(cardInfo));
                }
            }
            if (!cardInfoVTOList.isEmpty()) {
                logger.info("CMSServiceImpl getCitizenCards return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
                cmsLogger.info("CMSServiceImpl getCitizenCards return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
            }

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCitizenCards' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCitizenCards' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_079,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_080,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;
            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_127,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCitizenCards' is finished.");
        cmsLogger.info("The process 'getCitizenCards' is finished.");
        return cardInfoVTOList;
    }


    /**
     * The method getCitizenCardsByProduct is used to be gained the information about all of the citizen cards for a
     * specified product.
     *
     * @param nationalId represents the citizen nationalId
     * @param productId  represents the productId of the card
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    @Override
    public List<CardInfoVTO> getCitizenCardsByProduct(String nationalId,
                                                      String productId) throws BaseException {

        List<CardInfoVTO> cardInfoVTOList = new ArrayList<CardInfoVTO>();
        logger.info("The process 'getCitizenCardsByProduct' is started...");
        cmsLogger.info("The process 'getCitizenCardsByProduct' is started...");
        try {
            logger.info("Calling 'getCitizenCardsByProduct' service for 'nationalId' : " + nationalId + " and 'productId' : " + productId);
            cmsLogger.info("Calling 'getCitizenCardsByProduct' service for 'nationalId' : " + nationalId + " and 'productId' : " + productId);

            List<CardInfo> cardInfoList = getService().getCitizenCardsByProduct(nationalId, productId);
            if (cardInfoList == null || cardInfoList.isEmpty()) {
                cmsLogger.error(BizExceptionCode.CSI_140, BizExceptionCode.CSI_140_MSG);
                throw new ServiceException(BizExceptionCode.CSI_140, BizExceptionCode.CSI_140_MSG, new String[]{nationalId, productId});
            }

            logger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            cmsLogger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            for (CardInfo cardInfo : cardInfoList) {
                if (cardInfo != null) {
                    logger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cmsLogger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cardInfoVTOList.add(convertToCardInfoVTO(cardInfo));
                }
            }

            if (!cardInfoVTOList.isEmpty()) {
                logger.info("CMSServiceImpl getCitizenCardsByProduct return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
                cmsLogger.info("CMSServiceImpl getCitizenCardsByProduct return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
            }

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCitizenCardsByProduct' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCitizenCardsByProduct' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_081,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_082,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000011.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_083,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;
            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_128,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCitizenCardsByProduct' is finished.");
        cmsLogger.info("The process 'getCitizenCardsByProduct' is finished.");
        return cardInfoVTOList;
    }

    /**
     * The method getCurrentCitizenCards is used to be gained the information about all cards of the citizen which are
     * active.
     *
     * @param nationalId represents the citizen nationalId
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    @Override
    public List<CardInfoVTO> getCurrentCitizenCards(String nationalId) throws BaseException {
        List<CardInfoVTO> cardInfoVTOList = new ArrayList<CardInfoVTO>();
        logger.info("The process 'getCurrentCitizenCards' is started...");
        cmsLogger.info("The process 'getCurrentCitizenCards' is started...");
        try {
            logger.info("Calling 'getCurrentCitizenCards' service for 'nationalId' : " + nationalId);
            cmsLogger.info("Calling 'getCurrentCitizenCards' service for 'nationalId' : " + nationalId);
            List<CardInfo> cardInfoList = getService().getCurrentCitizenCard(nationalId);
            if (cardInfoList == null || cardInfoList.isEmpty()) {
                cmsLogger.error(BizExceptionCode.CSI_141, BizExceptionCode.CSI_139_MSG);
                throw new ServiceException(BizExceptionCode.CSI_141, BizExceptionCode.CSI_139_MSG, new String[]{nationalId});
            }

            logger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            cmsLogger.info("List of 'CardInfo' objects received containing " + cardInfoList.size() + " cards");
            for (CardInfo cardInfo : cardInfoList) {
                if (cardInfo != null) {
                    logger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cmsLogger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
                    cardInfoVTOList.add(convertToCardInfoVTO(cardInfo));
                }
            }

            if (!cardInfoVTOList.isEmpty()) {
                logger.info("CMSServiceImpl getCurrentCitizenCards return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
                cmsLogger.info("CMSServiceImpl getCurrentCitizenCards return value : '" + cardInfoVTOList + "' with the count : '" + cardInfoVTOList.size() + "'");
            }
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCurrentCitizenCards' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCurrentCitizenCards' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_084,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_085,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_129,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCurrentCitizenCards' is finished.");
        cmsLogger.info("The process 'getCurrentCitizenCards' is finished.");
        return cardInfoVTOList;
    }

    /**
     * The method getCurrentCitizenCardsByProduct is used to be gained the information about the current card of the
     * citizen for a specified product.
     *
     * @param nationalId represents the citizen nationalId
     * @param productId  represents the productId of the card
     * @return an instance of volatile object of type {@link CardInfoVTO}
     */
    @Override
    public CardInfoVTO getCurrentCitizenCardByProduct(String nationalId,
                                                      String productId) throws BaseException {
        logger.info("The process 'getCurrentCitizenCardByProduct' is started...");
        cmsLogger.info("The process 'getCurrentCitizenCardByProduct' is started...");
        CardInfoVTO cardInfoVTO;
        try {
            logger.info("Calling 'getCurrentCitizenCardByProduct' service for 'nationalId' : " + nationalId + " and 'productId' : " + productId);
            cmsLogger.info("Calling 'getCurrentCitizenCardByProduct' service for 'nationalId' : " + nationalId + " and 'productId' : " + productId);
            CardInfo cardInfo = getService().getCurrentCitizenCardByProduct(nationalId, productId);
            if (cardInfo == null) {
                cmsLogger.error(BizExceptionCode.CSI_142, BizExceptionCode.CSI_140_MSG);
                throw new ServiceException(BizExceptionCode.CSI_142, BizExceptionCode.CSI_140_MSG, new String[]{nationalId, productId});
            }

            cardInfoVTO = convertToCardInfoVTO(cardInfo);

            if (cardInfoVTO != null) {
                logger.info("CMSServiceImpl getCurrentCitizenCardByProduct return value : '" + cardInfoVTO.toString());
                cmsLogger.info("CMSServiceImpl getCurrentCitizenCardByProduct return value : '" + cardInfoVTO.toString());
            }

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCurrentCitizenCardsByProduct' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCurrentCitizenCardsByProduct' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_086,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_087,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000011.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_088,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_130,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCurrentCitizenCardByProduct' is finished.");
        cmsLogger.info("The process 'getCurrentCitizenCardByProduct' is finished.");
        return cardInfoVTO;
    }

    /**
     * The method getCardInfo is used to be gained the information about the current active card of the
     * citizen.
     *
     * @param crn represents the serial number of the card
     * @return an object of type {@link CardInfoVTO}
     */
    @Override
    public CardInfoVTO getCardInfo(String crn) throws BaseException {
        CardInfoVTO cardInfoVTO = null;
        logger.info("The process 'getCardInfo' is started...");
        cmsLogger.info("The process 'getCardInfo' is started...");
        try {
            logger.info("Calling 'getCardInfo' service for 'crn' : " + crn);
            cmsLogger.info("Calling 'getCardInfo' service for 'crn' : " + crn);
            CardInfo cardInfo = getService().getCardInfo(crn);
            if (cardInfo == null) {
                cmsLogger.error(BizExceptionCode.CSI_143, BizExceptionCode.CSI_143_MSG);
                throw new ServiceException(BizExceptionCode.CSI_143, BizExceptionCode.CSI_143_MSG, new String[]{crn});
            }

            logger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
            cmsLogger.info("The CardInfo is {CitizenID: " + cardInfo.getCitizenID() + ", CRN: " + cardInfo.getCrn() + ", RequestID: " + cardInfo.getRequestID());
            cardInfoVTO = convertToCardInfoVTO(cardInfo);

            if (cardInfoVTO != null) {
                logger.info("CMSServiceImpl getCardInfo return value : '" + cardInfoVTO + "'");
                cmsLogger.info("CMSServiceImpl getCardInfo return value : '" + cardInfoVTO + "'");
            }

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCardInfo' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCardInfo' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_089,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_090,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_131,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCardInfo' is finished.");
        cmsLogger.info("The process 'getCardInfo' is finished.");
        return cardInfoVTO;
    }

    /**
     * The method getCardApplications is used to be gained the applications of the specified card.
     *
     * @param crn represents the serial number of the card
     * @return an object of type {@link com.gam.nocr.ems.data.domain.vol.CardApplicationInfoVTO}
     */
    @Override
    public List<CardApplicationInfoVTO> getCardApplications(String crn) throws BaseException {
        List<CardApplicationInfoVTO> caiVTOList = new ArrayList<CardApplicationInfoVTO>();
        logger.info("The process 'getCardApplications' is started...");
        cmsLogger.info("The process 'getCardApplications' is started...");
        try {
            logger.info("Calling 'getCardApplications' service for 'crn' : " + crn);
            cmsLogger.info("Calling 'getCardApplications' service for 'crn' : " + crn);
            List<CardApplicationInfo> cardApplicationInfoList = getService().getCardApplications(crn);
            if (cardApplicationInfoList == null || cardApplicationInfoList.isEmpty()) {
                cmsLogger.error(BizExceptionCode.CSI_144, BizExceptionCode.CSI_143_MSG);
                throw new ServiceException(BizExceptionCode.CSI_144, BizExceptionCode.CSI_143_MSG, new String[]{crn});
            }

            logger.info("List of 'CardApplicationInfo' objects received containing " + cardApplicationInfoList.size() + " applications");
            cmsLogger.info("List of 'CardApplicationInfo' objects received containing " + cardApplicationInfoList.size() + " applications");
            for (CardApplicationInfo cardApplicationInfo : cardApplicationInfoList) {
                logger.info("The CardApplicationInfo is {ApplicationID: " + cardApplicationInfo.getId() +
                        ", Name: " + cardApplicationInfo.getName() + ", Status: " + cardApplicationInfo.getStatus());
                cmsLogger.info("The CardApplicationInfo is {ApplicationID: " + cardApplicationInfo.getId() +
                        ", Name: " + cardApplicationInfo.getName() + ", Status: " + cardApplicationInfo.getStatus());

                CardApplicationInfoVTO caiVTO = convertToCardApplicationInfoVTO(cardApplicationInfo);
                if (caiVTO != null) {
                    caiVTOList.add(caiVTO);
                }
            }
            if (!caiVTOList.isEmpty()) {
                logger.info("CMSServiceImpl getCardApplications return value : '" + caiVTOList + "' with the count : '" + caiVTOList.size() + "'");
                cmsLogger.info("CMSServiceImpl getCardApplications return value : '" + caiVTOList + "' with the count : '" + caiVTOList.size() + "'");
            }

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'getCardApplications' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'getCardApplications' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_091,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_092,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_132,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'getCardApplications' is finished.");
        cmsLogger.info("The process 'getCardApplications' is finished.");
        return caiVTOList;
    }


    /**
     * The method updateCardApplicationStatus is used to send the update request on an specified
     * embedded application of the card.
     *
     * @param crn                    represents the serial number of the card
     * @param cardApplicationInfoVTO is a volatile object that encapsulates the necessary attributes which are needed in
     *                               sending the application update request to the sub system 'CMS'
     */
    @Override
    public void updateCardApplicationStatus(String crn,
                                            CardApplicationInfoVTO cardApplicationInfoVTO) throws BaseException {
        logger.info("The process 'updateCardApplicationStatus' is started...");
        cmsLogger.info("The process 'updateCardApplicationStatus' is started...");
        try {
            logger.info("Calling 'updateCardApplicationStatus' service for 'crn' : " + crn +
                    " and CardApplication : {ApplicationID: " + cardApplicationInfoVTO.getId() +
                    ", Name: " + cardApplicationInfoVTO.getName() +
                    ", Status: " + cardApplicationInfoVTO.getStatus() +
                    ", Reason: " + cardApplicationInfoVTO.getReason() + "}");
            cmsLogger.info("Calling 'updateCardApplicationStatus' service for 'crn' : " + crn +
                    " and CardApplication : {ApplicationID: " + cardApplicationInfoVTO.getId() +
                    ", Name: " + cardApplicationInfoVTO.getName() +
                    ", Status: " + cardApplicationInfoVTO.getStatus() +
                    ", Reason: " + cardApplicationInfoVTO.getReason() + "}");
            logger.info("Changing reason to updateCardApplicationStatus");
            cmsLogger.info("Changing reason to updateCardApplicationStatus");
            getService().updateCardApplicationStatus(
                    crn,
                    cardApplicationInfoVTO.getId(),
                    cardApplicationInfoVTO.getStatus(),
                    cardApplicationInfoVTO.getReason()
            );

        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            logger.error("Calling 'updateCardApplicationStatus' service failed because of : " + errorCode + " - " + errorMessage);
            cmsLogger.error("Calling 'updateCardApplicationStatus' service failed because of : " + errorCode + " - " + errorMessage);
            if (CARD_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_093,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_094,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_095,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_096,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (CARD_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_097,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_133,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }
        logger.info("The process 'updateCardApplicationStatus' is finished.");
        cmsLogger.info("The process 'updateCardApplicationStatus' is finished.");
    }

    /**
     * The method addEnrollmentOffice is used to send a request to add new enrollment office.
     *
     * @param enrollmentOfficeTO is used to encapsulate the necessary attributes which are needed in sending the
     *                           request of adding new enrollment office to the sub system 'CMS'
     * @param enableStatus       1 for enabled and 2 for disabled
     */
    @Override
    public void addEnrollmentOffice(EnrollmentOfficeTO enrollmentOfficeTO,
                                    int enableStatus) throws BaseException {

        logger.info("The process 'addEnrollmentOffice' is started... EnrollmentOfficeId : " + enrollmentOfficeTO.getId()
                + " - enableStatus : " + enableStatus);
        cmsLogger.info("The process 'addEnrollmentOffice' is started... EnrollmentOfficeId : " + enrollmentOfficeTO.getId()
                + " - enableStatus : " + enableStatus);

        UserSiteInfo userSiteInfo = null;
        if (enrollmentOfficeTO != null) {
            EnrollmentOfficeTO dbEnrollmentOffice = getEnrollmentOfficeDAO().loadLazyChildren(enrollmentOfficeTO);
            userSiteInfo = convertToUserSiteInfo(dbEnrollmentOffice, enableStatus);
        }
        try {
            getService().addUserSite(userSiteInfo);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            if (SITE_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_098,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_099,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_100,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_101,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000007.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_102,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000008.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_103,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_104,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_134,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }

        logger.info("The process 'addEnrollmentOffice' is finished.");
        cmsLogger.info("The process 'addEnrollmentOffice' is finished.");

    }

    /**
     * The method updateEnrollmentOffices is used to send a request to update a list of type {@link
     * com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}.
     *
     * @param enrollmentOfficeTOList is used to encapsulate the necessary attributes which are needed in sending the
     *                               request of updating new enrollment office to the sub system 'CMS'
     * @param enableStatus           1 for enabled and 2 for disabled
     */
    @Override
    public void updateEnrollmentOffices(List<EnrollmentOfficeTO> enrollmentOfficeTOList,
                                        int enableStatus) throws BaseException {

        logger.info("The process 'updateEnrollmentOffices' is started...");
        cmsLogger.info("The process 'updateEnrollmentOffices' is started...");

        List<Long> enrollmentOfficeIds = new ArrayList<Long>();
        List<UserSiteInfo> userSiteInfoList = new ArrayList<UserSiteInfo>();
        if (enrollmentOfficeTOList != null) {
            for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
                UserSiteInfo userSiteInfo;
                EnrollmentOfficeTO dbEnrollmentOffice = getEnrollmentOfficeDAO().loadLazyChildren(enrollmentOfficeTO);
                userSiteInfo = convertToUserSiteInfo(dbEnrollmentOffice, enableStatus);
                userSiteInfoList.add(userSiteInfo);
                enrollmentOfficeIds.add(dbEnrollmentOffice.getId());
            }
        }

        logger.info("\nCMSServiceImpl().updateEnrollmentOffices() called with enrollmentOfficeIds : " + EmsUtil.toStringList(enrollmentOfficeIds));
        cmsLogger.info("\nCMSServiceImpl().updateEnrollmentOffices() called with enrollmentOfficeIds : " + EmsUtil.toStringList(enrollmentOfficeIds));

        try {
            getService().updateUserSite(userSiteInfoList);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();
            if (SITE_000001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_105,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_106,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_107,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000007.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_108,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000008.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_109,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000009.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_110,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            } else if (SITE_000010.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.CSI_111,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_CMS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
                throw serviceException;

            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.CSI_135,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            cmsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CMS.split(","));
            throw serviceException;
        }

        logger.info("The process 'updateEnrollmentOffices' is finished.");
        cmsLogger.info("The process 'updateEnrollmentOffices' is finished.");
    }

    /**
     * getBiometricInfoDAO
     */
    private BiometricInfoDAO getBiometricInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_BIOMETRIC_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CMS_084,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_BIOMETRIC_INFO.split(","));
        }
    }
}


