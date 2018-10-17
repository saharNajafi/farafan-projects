package com.gam.nocr.ems.config;

/**
 * Collection of logical names for services (EJBs) plus some utility methods to convert them to an appropriate format to
 * look up in JNDI tree
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class EMSLogicalNames {

    // Global Logical Names
    public static String JNDI_PREFIX = "java:app/";

    // Service Layer Logical Names
    public static String SRV_EJB_MODULE = "ems-services-ejb-impl/";
    public static String SRV_EJB_PACKAGE = "com.gam.nocr.ems.biz.service.internal.impl.";
    public static String SRV_EXTERNAL_EJB_PACKAGE = "com.gam.nocr.ems.biz.service.external.impl.";
    public static String SRV_EXTERNAL_IMS_PROXY_PACKAGE = SRV_EXTERNAL_EJB_PACKAGE + "ims.";
    public static String SRV_BUSINESS_LOG = "BusinessLogService";
    public static String SRV_AFTER_DELIVERY = "AfterDeliveryService";
    public static String SRV_CARD_ISSUANCE_REQUEST = "CardIssuanceRequestService";
    public static String SRV_DEPARTMENT = "DepartmentService";
    public static String SRV_DOC_TYPE = "DocTypeService";
    public static String SRV_DISPATCHING = "DispatchingService";
    public static String SRV_ENROLLMENT_OFFICE = "EnrollmentOfficeService";
    public static String SRV_BLACK_LIST = "BlackListService";
    public static String SRV_JOB = "JobService";
    public static String SRV_PERSON = "PersonManagementService";
    public static String SRV_USER = "UserManagementService";
    public static String SRV_REGISTRATION = "RegistrationService";
    public static String SRV_COMPLETE_REGISTRATION = "CompleteRegistrationService";
    public static String SRV_STATE_PROVIDER = "StateProviderService";
    public static String SRV_WORKSTATION = "WorkstationService";
    public static String SRV_WORKSTATIONINFO = "WorkstationInfoService";
    public static String SRV_WORKSTATIONPLUGINS = "WorkstationPluginsService";
    public static String SRV_RATING = "RatingService";
    public static String SRV_OFFICE_SETTING = "OfficeSettingService";
    public static String SRV_FEATURE_EXTRACT_IDS = "FeatureExtractIdsService";
    public static String SRV_FEATURE_EXTRACT_VERSIONS = "FeatureExtractVersionsService";
    public static String SRV_LOCATION = "LocationService";
    public static String SRV_HOLIDAY= "HolidayService";
    public static String SRV_TOKEN_MANAGEMENT = "TokenManagementService";
    public static String SRV_PKI = "PKIService";
    public static String SRV_CARD_MANAGEMENT = "CardManagementService";
    public static String SRV_IMS_MANAGEMENT = "IMSManagementService";
    public static String SRV_PORTAL_MANAGEMENT = "PortalManagementService";
    public static String SRV_SYSTEM_PROFILE = "SystemProfileService";
    public static String SRV_REPORT_MANAGEMENT = "ReportManagementService";
    public static String SRV_REPORT_REQUEST = "ReportRequestService";
    public static String SRV_CARD_REQUEST = "CardRequestService";
    public static String SRV_OFFICE_ACTIVE_SHIFT = "OfficeActiveShiftService";
    public static String SRV_CITIZEN = "CitizenService";
    public static String SRV_CITIZEN_INFO = "CitizenInfoService";
    public static String SRV_EMKS = "EmksService";

    public static String SRV_ABOUT = "AboutService";
    public static String SRV_HELP = "HelpService";
    public static String SRV_MESSAGE = "MessageService";
    public static String SRV_PREPARED_MESSAGE = "PreparedMessageService";
    public static String SRV_OUTGOING_SMS = "OutgoingSMSService";
    public static String SRV_OFFICE_CAPACITY = "OfficeCapacityService";
    public static String SRV_RESERVATION = "ReservationService";
    public static String SRV_INTERNAL_SERVICE_CHEKER= "InternalServiceChecker";
    public static String SRV_REGISTRATION_PAYMENT= "RegistrationPaymentService";
    public static String SRV_CARD_REQUEST_HISTORY= "CardRequestHistoryService";
    // Proxy Services
    public static String SRV_GAAS = "GAASService";
    public static String SRV_CAS = "CASService";
    public static String SRV_CMS = "CMSService";
    public static String SRV_PORTAL_REGISTRATION = "PortalRegistrationService";
    public static String SRV_PORTAL_RESERVATION = "PortalReservationService";
    public static String SRV_PORTAL_BASE_INFO = "PortalBaseInfoService";
    public static String SRV_PORTAL_SMS = "PortalSmsService";
    public static String SRV_IMS = "IMSService";
    public static String SRV_NIB = "NOCRIMSBatchService";
    public static String SRV_NIO = "NOCRIMSOnlineService";
    public static String SRV_NIF = "NOCRIMSFarafanService";
    public static String SRV_IMS_BATCH = "NOCRIMSBatchService";
    public static String SRV_IMS_FARAFAN = "NOCRIMSFarafanService";


    // Data Layer Logical Names
    public static String DAO_EJB_MODULE = "ems-dao-jpa-impl/";
    public static String DAO_EJB_PACKAGE = "com.gam.nocr.ems.data.dao.impl.";
    public static String DAO_PERSON = "PersonDAO";
    public static String DAO_PROVINCE = "ProvinceDAO";
    public static String DAO_LOCATION = "LocationDAO";
    public static String DAO_HOLIDAY = "HolidayDAO";
    public static String DAO_ExceptionCodeDAO = "ExceptionCodeDAO";
    public static String DAO_CARD = "CardDAO";
    public static String DAO_DISPATCHING = "DispatchDAO";
    public static String DAO_CITIZEN = "CitizenDAO";
    public static String DAO_CITIZEN_INFO = "CitizenInfoDAO";
    public static String DAO_BIOMETRIC = "BiometricDAO";
    public static String DAO_BIOMETRIC_INFO = "BiometricInfoDAO";
    public static String DAO_DOCUMENT = "DocumentDAO";
    public static String DAO_DOC_TYPE = "DocTypeDAO";
    public static String DAO_SERVICE_DOCTYPE = "ServiceDocumentTypeDAO";
    public static String DAO_BUSINESSLOG = "BusinessLogDAO";
    public static String DAO_CARD_REQUEST = "CardRequestDAO";
    public static String DAO_PURGE_STATUS = "PurgeStatusDAO";
    public static String DAO_CARD_REQUEST_HISTORY = "CardRequestHistoryDAO";
    public static String DAO_DEPARTMENT = "DepartmentDAO";
    public static String DAO_WORKSTATION = "WorkstationDAO";
    public static String DAO_WORKSTATIONINFO = "WorkstationInfoDAO";
    public static String DAO_WORKSTATIONPlugins = "WorkstationPluginsDAO";
    public static String DAO_ENROLLMENT_OFFICE = "EnrollmentOfficeDAO";
    public static String DAO_PERSON_TOKEN = "PersonTokenDAO";
    public static String DAO_NETWORK_TOKEN = "NetworkTokenDAO";
    public static String DAO_CERTIFICATE = "CertificateDAO";
    public static String DAO_BATCH = "BatchDAO";
    public static String DAO_BLACK_LIST = "BlackListDAO";
    public static String DAO_RATING_INFO = "RatingInfoDAO";
    public static String DAO_RESERVATION = "ReservationDAO";
    public static String DAO_REGISTRATION_PAYMENT = "RegistrationPaymentDAO";
    public static String DAO_ENCRYPTED_CARD_REQUEST = "EncryptedCardRequestDAO";
    public static String DAO_REPORT = "ReportDAO";
    public static String DAO_REPORT_REQUEST = "ReportRequestDAO";
    public static String DAO_REPORT_FILE = "ReportFileDAO";
    public static String DAO_SMS = "SmsDAO";
    public static String DAO_RELIGION = "ReligionDAO";
    public static String DAO_ESTELAM2_FAILURE_LOG = "Estelam2FailureLogDAO";
    public static String DAO_CHILD = "ChildDAO";
    public static String DAO_SPOUSE = "SpouseDAO";
    public static String DAO_ABOUT = "AboutDAO";
    public static String DAO_HELP = "HelpDAO";
    public static String DAO_MESSAGE = "MessageDAO";
    public static String DAO_PREPARED_MESSAGE = "PreparedMessageDAO";
    public static String DAO_MESSAGE_DESTINATION = "MessageDestinationDAO";
    public static String DAO_MESSAGE_PERSON = "MessagePersonDAO";
    public static String DAO_PHOTO_VIP = "PhotoVipDAO";
    public static String DAO_IMS_ESTELAM_IMAGE = "ImsEstelamImageDAO";
    public static String DAO_OFFICE_SETTING = "OfficeSettingDAO";
    public static String DAO_OFFICE_CAPACITY = "OfficeCapacityDAO";
    public static String DAO_FEATURE_EXTRACT_IDS = "FeatureExtractIdsDAO";
    public static String DAO_FEATURE_EXTRACT_VERSIONS = "FeatureExtractVersionsDAO";
    public static String DAO_OFFICE_ACTIVE_SHIFT= "OfficeActiveShiftDAO";
    public static String DAO_NIST_HEADER = "NistHeaderDAO";
    public static String DAO_XML_AFIS = "XmlAfisDAO";
    public static String DAO_CARDREQUEST_BLOBS = "CardRequestBlobsDAO";
    public static String DAO_PURGE_HISTORY = "PurgeHistoryDAO";
    public static String DAO_EXCEPTION_CODE = "ExceptionCodeDAO";

    /**
     * DAOs, which are required in GamIms implementation
     */
    public static String DAO_IMS_UPDATE = "IMSUpdateDAO";
    public static String DAO_IMS_CITIZEN_INFO = "IMSCitizenInfoDAO";
    public static String DAO_IMS_BATCH_ENQUIRY = "IMSBatchEnquiryDAO";

    /**
     * Given a logical name of an internal service and constructs its fully qualified JNDI name to lookup in JNDI tree.
     * The JNDI name of EJBs in WebLogic JNDI tree comprises of following parts:<br/>
     * <p/>
     * <ul>
     * <li>The JNDI Prefix : 'java:app'</li>
     * <li>The name of the module that services are implemented in: 'ems-services-ejb-impl'</li>
     * <li>The logical name of the service (e.g. ReportManagementService)</li>
     * <li>An exclamation mark : !</li>
     * <li>The packaging of the services : 'com.gam.nocr.ems.biz.service.internal.impl'</li>
     * <li>The logical name of the service (e.g. ReportManagementService)</li>
     * </ul>
     * <p/>
     * for example the JNDI name of ReportManagementService is :
     * <b>java:app/ems-services-ejb-impl/ReportManagementService!com.gam.nocr.ems.biz.service.internal.impl.ReportManagementService</b>
     *
     * @param logicalName Logical name of the service to look up in JNDI tree
     * @return Fully qualified JNDI name to lookup in JNDI tree
     */
    public static String getServiceJNDIName(String logicalName) {
        return JNDI_PREFIX + SRV_EJB_MODULE + logicalName + "!"
                + SRV_EJB_PACKAGE + logicalName;
    }

    /**
     * Same as {@link com.gam.nocr.ems.config.EMSLogicalNames#getServiceJNDIName(String)} but for external services.
     * The JNDI name of external services (in EJBs) in WebLogic JNDI tree comprises of following parts:<br/>
     * <p/>
     * <ul>
     * <li>The JNDI Prefix : 'java:app'</li>
     * <li>The name of the module that services are implemented in: 'ems-services-ejb-impl'</li>
     * <li>The logical name of the service (e.g. PKIService)</li>
     * <li>An exclamation mark : !</li>
     * <li>The packaging of the services : 'com.gam.nocr.ems.biz.service.external.impl'</li>
     * <li>The logical name of the service (e.g. PKIService)</li>
     * </ul>
     * <p/>
     * for example the JNDI name of ReportManagementService is :
     * <b>java:app/ems-services-ejb-impl/PKIService!com.gam.nocr.ems.biz.service.external.impl.PKIService</b>
     *
     * @param logicalName Logical name of the service to look up in JNDI tree
     * @return Fully qualified JNDI name to lookup in JNDI tree
     */
    public static String getExternalServiceJNDIName(String logicalName) {
        return JNDI_PREFIX + SRV_EJB_MODULE + logicalName + "!"
                + SRV_EXTERNAL_EJB_PACKAGE + logicalName;
    }

    /**
     * Same as {@link com.gam.nocr.ems.config.EMSLogicalNames#getServiceJNDIName(String)} but for dao EJBs.
     * The JNDI name of dao (as they're EJBs) in WebLogic JNDI tree comprises of following parts:<br/>
     * <p/>
     * <ul>
     * <li>The JNDI Prefix : 'java:app'</li>
     * <li>The name of the module that services are implemented in: 'ems-dao-jpa-impl'</li>
     * <li>The logical name of the dao (e.g. PersonDAO)</li>
     * <li>An exclamation mark : !</li>
     * <li>The packaging of the services : 'com.gam.nocr.ems.data.dao.impl'</li>
     * <li>The logical name of the dao (e.g. PersonDAO)</li>
     * </ul>
     * <p/>
     * for example the JNDI name of PersonDAO is :
     * <b>java:app/ems-dao-jpa-impl/PersonDAO!com.gam.nocr.ems.data.dao.impl.PersonDAO</b>
     *
     * @param logicalName Logical name of the dao to look up in JNDI tree
     * @return Fully qualified JNDI name to lookup in JNDI tree
     */
    public static String getDaoJNDIName(String logicalName) {
        return JNDI_PREFIX + DAO_EJB_MODULE + logicalName + "!"
                + DAO_EJB_PACKAGE + logicalName;
    }

    /**
     * Same as {@link com.gam.nocr.ems.config.EMSLogicalNames#getServiceJNDIName(String)} but dedicated for ims external
     * services. As these services are in different package, their JNDI name are different. The JNDI name of these
     * services in WebLogic JNDI tree comprises of following parts:<br/>
     * <p/>
     * <ul>
     * <li>The JNDI Prefix : 'java:app'</li>
     * <li>The name of the module that services are implemented in: 'ems-services-ejb-impl'</li>
     * <li>The logical name of the service (e.g. NOCRIMSOnlineService)</li>
     * <li>An exclamation mark : !</li>
     * <li>The packaging of the services : 'com.gam.nocr.ems.biz.service.external.impl.ims'</li>
     * <li>The logical name of the service (e.g. NOCRIMSOnlineService)</li>
     * </ul>
     * <p/>
     * for example the JNDI name of NOCRIMSOnlineService is :
     * <b>java:app/ems-services-ejb-impl/NOCRIMSOnlineService!com.gam.nocr.ems.biz.service.external.impl.ims.NOCRIMSOnlineService</b>
     *
     * @param logicalName Logical name of the service to look up in JNDI tree
     * @return Fully qualified JNDI name to lookup in JNDI tree
     */
    public static String getExternalIMSServiceJNDIName(String logicalName) {
        return JNDI_PREFIX + SRV_EJB_MODULE + logicalName + "!"
                + SRV_EXTERNAL_IMS_PROXY_PACKAGE + logicalName;
    }
}
