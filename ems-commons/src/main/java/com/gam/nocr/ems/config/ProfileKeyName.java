package com.gam.nocr.ems.config;

/**
 * Collection of profile key names that are defined in underlying database. All accesses to profile values in EMS should
 * be done through these constant values
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class ProfileKeyName {

    public static final String EMS_PROFILE_ROOT = "nocr.ems.profile";
    public static final String PORTAL_PROFILE_ROOT = "nocr.portal.profile";

    public static final String KEY_CMS_ENDPOINT = EMS_PROFILE_ROOT + ".cmsEndpoint";
    public static final String KEY_CMS_NAMESPACE = EMS_PROFILE_ROOT + ".cmsNamespace";
    public static final String KEY_GAAS_ENDPOINT = EMS_PROFILE_ROOT + ".gaasEndpoint";
    public static final String KEY_GAAS_NAMESPACE = EMS_PROFILE_ROOT + ".gaasNamespace";
    public static final String KEY_CAS_ENDPOINT = EMS_PROFILE_ROOT + ".casEndpoint";
    public static final String KEY_CAS_NAMESPACE = EMS_PROFILE_ROOT + ".casNamespace";
    public static final String KEY_PKI_ENDPOINT = EMS_PROFILE_ROOT + ".pkiEndpoint";
    public static final String KEY_PKI_NAMESPACE = EMS_PROFILE_ROOT + ".pkiNamespace";

    public static final String KEY_CMS_SUBJECT_DN = EMS_PROFILE_ROOT + ".cmsSubjectDN";
    public static final String KEY_CMS_CHECK_SUBJECT_DN = EMS_PROFILE_ROOT + ".cmsCheckSubjectDN";

    public static final String KEY_IMS_OFFLINE_ENDPOINT = EMS_PROFILE_ROOT + ".imsOfflineEndpoint";
    public static final String KEY_IMS_OFFLINE_NAMESPACE = EMS_PROFILE_ROOT + ".imsOfflineNamespace";
    public static final String KEY_IMS_OFFLINE_USERNAME = EMS_PROFILE_ROOT + ".imsOfflineUsername";
    public static final String KEY_IMS_OFFLINE_PASSWORD = EMS_PROFILE_ROOT + ".imsOfflinePassword";
    public static final String KEY_IMS_ONLINE_ENDPOINT = EMS_PROFILE_ROOT + ".imsOnlineEndpoint";
    public static final String KEY_IMS_ONLINE_NAMESPACE = EMS_PROFILE_ROOT + ".imsOnlineNamespace";
    public static final String KEY_IMS_ONLINE_USERNAME = EMS_PROFILE_ROOT + ".imsOnlineUsername";
    public static final String KEY_IMS_ONLINE_PASSWORD = EMS_PROFILE_ROOT + ".imsOnlinePassword";
    public static final String KEY_IMS_ONLINE_KEYHAN_USERNAME = EMS_PROFILE_ROOT + ".imsOnlineKeyhanUsername";
    public static final String KEY_IMS_ONLINE_KEYHAN_SERIAL = EMS_PROFILE_ROOT + ".imsOnlineKeyhanSerial";
    public static final String KEY_IMS_UPDATE_CITIZENS_INFO_USERNAME = EMS_PROFILE_ROOT + ".imsUpdateCitizensInfoUsername";
    public static final String KEY_IMS_UPDATE_CITIZENS_INFO_PASSWORD = EMS_PROFILE_ROOT + ".imsUpdateCitizensInfoPassword";
    public static final String KEY_IMS_ONLINE_ENQUIRY_FLAG = EMS_PROFILE_ROOT + ".imsOnlineEnquiryFlag";
    public static final String KEY_IMS_OFFLINE_ENQUIRY_FLAG = EMS_PROFILE_ROOT + ".imsOfflineEnquiryFlag";
    public static final String KEY_IMS_UPDATE_CITIZENS_INFO_FLAG = EMS_PROFILE_ROOT + ".imsUpdateCitizensInfoFlag";
    public static final String KEY_IMS_OFFLINE_ENQUIRY_SIZE = EMS_PROFILE_ROOT + ".imsOfflineEnquirySize";
    public static final String KEY_IMS_STUB_ENDPOINT = EMS_PROFILE_ROOT + ".imsStubEndpoint";
    public static final String KEY_IMS_STUB_NAMESPACE = EMS_PROFILE_ROOT + ".imsStubNamespace";
    public static final String KEY_IMS_NEW_SERVICES_ENDPOINT = EMS_PROFILE_ROOT + ".imsNewServicesEndPoint";
    public static final String KEY_IMS_NEW_SERVICES_NAMESPACE = EMS_PROFILE_ROOT + ".imsNewServicesNameSpace";
    public static final String KEY_IMS_NEW_SERVICES_USERNAME = EMS_PROFILE_ROOT + ".imsNewServicesUsername";
    public static final String KEY_IMS_NEW_SERVICES_PASSWORD = EMS_PROFILE_ROOT + ".imsNewServicesPassword";
    public static final String KEY_IMS_FETCH_CITIZEN_INFO_FLAG = EMS_PROFILE_ROOT + ".imsFetchCitizensInfoFlag";
    public static final String KEY_IMS_SET_CITIZEN_CARD_REQUESTED_FLAG = EMS_PROFILE_ROOT + ".imsSetCitizenCardRequestedFlag";
    public static final String KEY_IMS_SET_CITIZEN_CARD_DELIVERED_FLAG = EMS_PROFILE_ROOT + ".imsSetCitizenCardDeliveredFlag";
    public static final String KEY_IMS_UPDATE_CITIZEN_INFO_RESULT_TRY_COUNTER = EMS_PROFILE_ROOT + ".imsUpdateCitizenInfoResultTryCounter";
    public static final String KEY_BATCH_ENQUIRY_INTERVAL_FOR_IMS_PENDING_REQUESTS = EMS_PROFILE_ROOT + ".imsBatchEnquiryIntervalForIMSPending";
    public static final String KEY_BATCH_ENQUIRY_INTERVAL_TYPE_FOR_IMS_PENDING_REQUESTS = EMS_PROFILE_ROOT + ".imsBatchEnquiryIntervalTypeForIMSPending";
    public static final String KEY_NOCR_SMS_SERVICE_USERNAME = EMS_PROFILE_ROOT + ".nocrSmsServiceUserName";
    public static final String KEY_NOCR_SMS_SERVICE_PASSWORD = EMS_PROFILE_ROOT + ".nocrSmsServicePassword";

    public static final String KEY_MATIRAN_IMS_BATCH_SERVICES_ENDPOINT = EMS_PROFILE_ROOT + ".matiranIMSBatchServicesEndPoint";
    public static final String KEY_MATIRAN_IMS_ONLINE_SERVICES_ENDPOINT = EMS_PROFILE_ROOT + ".matiranIMSOnlineServicesEndPoint";
    public static final String KEY_MATIRAN_IMS_FARAFAN_SERVICES_ENDPOINT = EMS_PROFILE_ROOT + ".matiranIMSFarafanServicesEndPoint";

    public static final String KEY_GAM_IMS_SERVICES_ENDPOINT = EMS_PROFILE_ROOT + ".gamIMSServicesEndPoint";

    public static final String KEY_PORTAL_REGISTRATION_ENDPOINT = EMS_PROFILE_ROOT + ".portalRegistrationEndpoint";
    public static final String KEY_PORTAL_RESERVATION_ENDPOINT = EMS_PROFILE_ROOT + ".portalReservationEndpoint";
    public static final String KEY_PORTAL_BASIC_INFO_ENDPOINT = EMS_PROFILE_ROOT + ".portalBasicInfoEndpoint";
    public static final String KEY_PORTAL_SMS_ENDPOINT = EMS_PROFILE_ROOT + ".portalSmsEndpoint";
    public static final String KEY_PORTAL_NAMESPACE = EMS_PROFILE_ROOT + ".portalNamespace";
    public static final String KEY_NOCR_SMS_ENDPOINT = EMS_PROFILE_ROOT + ".NOCRSmsEndpoint";
    public static final String KEY_NOCR_SMS_NAMESPACE = EMS_PROFILE_ROOT + ".NOCRSmsNamespace";
    public static final String KEY_NUMBER_OF_REQUEST_TO_LOAD = EMS_PROFILE_ROOT + ".numberOfRequestToLoad";

    public static final String CMS_PROSHIP_ERROR_CODE_PREFIX = EMS_PROFILE_ROOT + ".cmsProShipPrefix";
    public static final String CMS_CARDERR_ERROR_CODE_PREFIX = EMS_PROFILE_ROOT + ".cmsCardErrPrefix";

    public static final String AUTO_COMPLETE_ROOT = EMS_PROFILE_ROOT + ".autocomplete";
    public static final String STATE_ROOT = EMS_PROFILE_ROOT + ".state";

    public static final String KEY_CMS_ISSUE_CARD_PRODUCT_ID = EMS_PROFILE_ROOT + ".cmsIssueCardProductId";
    public static final String KEY_CMS_ISSUE_CARD_PRODUCT_VERSION = EMS_PROFILE_ROOT + ".cmsIssueCardProductVersion";

    public static final String KEY_CMS_REQUEST_ID_DIGIT_COUNT = EMS_PROFILE_ROOT + ".cmsRequestIdDigitCount";

    public static final String KEY_CARD_APPLICATION_SIGNATURE_ID = EMS_PROFILE_ROOT + ".cardApplicationSignature";

    public static final String KEY_TIME_INTERVAL_FOR_RECEIVED_BATCHES = EMS_PROFILE_ROOT + ".receivedBatchesTimeInterval";
    public static final String KEY_TIME_INTERVAL_FOR_MISSED_BATCHES = EMS_PROFILE_ROOT + ".missedBatchesTimeInterval";
    public static final String KEY_TIME_INTERVAL_FOR_MISSED_BOXES = EMS_PROFILE_ROOT + ".missedBoxesTimeInterval";
    public static final String KEY_TIME_INTERVAL_FOR_MISSED_CARDS = EMS_PROFILE_ROOT + ".missedCardsTimeInterval";

    public static final String KEY_CARD_REQUESTS_COUNT_FOR_SENDING_TO_CMS = EMS_PROFILE_ROOT + ".cmsCardRequestsCountToSend";
    public static final String KEY_CARD_REQUESTS_COUNT_FOR_SENDING_TO_IMS = EMS_PROFILE_ROOT + ".imsCardRequestsCountToSend";
    public static final String KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_CMS = EMS_PROFILE_ROOT + ".cmsCardRequestsCountQueryTimeInterval";
    public static final String KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS = EMS_PROFILE_ROOT + ".imsCardRequestsCountQueryTimeInterval";

    public static final String KEY_CMS_SEND_CARD_REQUEST_TRY_COUNTER = EMS_PROFILE_ROOT + ".cmsCardRequestTryCounter";

    public static final String KEY_PERSON_TOKEN_OFFER_ID_AUTHENTICATION = EMS_PROFILE_ROOT + ".personTokenAuthentication";
    public static final String KEY_PERSON_TOKEN_OFFER_ID_SIGNATURE = EMS_PROFILE_ROOT + ".personTokenSignature";
    public static final String KEY_PERSON_TOKEN_OFFER_ID_ENCRYPTION = EMS_PROFILE_ROOT + ".personTokenEncryption";
    public static final String KEY_NETWORK_TOKEN_OFFER_ID_AUTHENTICATION = EMS_PROFILE_ROOT + ".networkTokenAuthentication";
    public static final String KEY_TOKEN_PROTOCOL_VERSION = EMS_PROFILE_ROOT + ".tokenProtocolVersion";

    public static final String KEY_CARD_REQUEST_IDLE_PERIOD = EMS_PROFILE_ROOT + ".cardRequestIdlePeriod";

    public static final String KEY_CARD_REQUEST_FIND_BY_STATE_AND_ORIGIN_MAX_RESULT = EMS_PROFILE_ROOT + ".cardRequestFindByStateAndOriginMaxResult";

    public static final String KEY_FING_CANDIDATE_SIZE_KB = EMS_PROFILE_ROOT + ".fingCandidateSizeKB";
    public static final String KEY_FING_ISO_19794_NORMAL_FORMAT_MAX_SIZE_BYTES = EMS_PROFILE_ROOT + STATE_ROOT + ".ccos.ISO19794NormalFormatMaxSizeBytes";
    public static final String KEY_SCANNED_DOCUMENT_SIZE_KB = EMS_PROFILE_ROOT + ".scannedDocumentSizeKB";
    public static final String KEY_SCANNED_DOCUMENT_MIN_SIZE_KB = EMS_PROFILE_ROOT + ".scannedDocumentMinSizeKB";
    public static final String KEY_SCANNED_DOCUMENT_FACE_IMAGE_COMPRESSION_MAX_SIZE_LIMIT_BYTES = EMS_PROFILE_ROOT + STATE_ROOT + ".ccos.document.FaceImageCompressionMaxSizeLimitBytes";
    public static final String KEY_SCANNED_DOCUMENT_SERIAL_NUMBER_IMAGE_COMPRESSION_MAX_SIZE_LIMIT_BYTES = EMS_PROFILE_ROOT + STATE_ROOT + ".ccos.document.SerialNumberImageCompressionMaxSizeLimitBytes";
    public static final String KEY_SMS_RETRY_DURATION = EMS_PROFILE_ROOT + ".smsRetryDuration";
    public static final String KEY_SMS_MAX_RETRY_COUNT = EMS_PROFILE_ROOT + ".smsMaxRetryCount";

    public static final String KEY_DELIVER_MESSAGE = EMS_PROFILE_ROOT + ".deliverMessage";

    public static final String KEY_CCOS_RESERVATION_RANGE_TO_SHOW = EMS_PROFILE_ROOT + ".ccos.reservationRange";

    public static final String KEY_REPORT_USER_DEFINED_ATTRIBUTES = EMS_PROFILE_ROOT + ".userDefinedReportAttributesPrefix";
    public static final String KEY_REPORT_REQUEST_TRY_COUNTER = EMS_PROFILE_ROOT + ".reportRequestTryCounter";

    public static final String KEY_INPROGRESSED_CARD_REQUEST_STATES = EMS_PROFILE_ROOT + ".inProgressedCardRequestStates";

    public static final String KEY_CRITICAL_CARD_REQUEST_STATES = EMS_PROFILE_ROOT + ".criticalCardRequestStates";

    public static final String ACTIVESHIFT_MAXIMUM_DURATION_TO_RESERVE = EMS_PROFILE_ROOT + ".activeShiftMaximumDurationToReserve";

    //****SMS****\\
    public static final String KEY_SMS_BODY = EMS_PROFILE_ROOT + ".smsBody";
    public static final String KEY_SMS_BODY_DOCUMENT_AUTHENTICATED = EMS_PROFILE_ROOT + ".smsBodyDocumentAuthenticate";
    public static final String KEY_SMS_BODY_REPEALED_FIRST_CARD = EMS_PROFILE_ROOT + ".smsBodyRepealedFirstCard";
    public static final String KEY_SMS_BODY_REPEALED_OTHERS = EMS_PROFILE_ROOT + ".smsBodyRepealedOthers";

    //***** SMS IMS get Result ***********
    public static final String KEY_SMS_BODY_DELETE_IMAGE = EMS_PROFILE_ROOT + ".smsBodyIMSDeleteImage";
    public static final String KEY_SMS_BODY_DELETE_FINGER = EMS_PROFILE_ROOT + ".smsBodyIMSDeleteFinger";
    public static final String KEY_SMS_BODY_DELETE_DOC = EMS_PROFILE_ROOT + ".smsBodyIMSDeleteDoc";
    public static final String KEY_IMS_SMS_ENABLE = EMS_PROFILE_ROOT + ".imsEnableSMS";


    //Estelam3
    public static final String KEY_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_UP = EMS_PROFILE_ROOT + ".durationOfReservationDateToFetchUp";
    public static final String KEY_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_DOWN = EMS_PROFILE_ROOT + ".durationOfReservationDateToFetchDown";

    public static final String KEY_SKIP_CMS_CHECK = EMS_PROFILE_ROOT + ".skipCmsCheck";
    public static final String KEY_SKIP_ESTELAM_CHECK = EMS_PROFILE_ROOT + ".skipEstelamCheck";

    public static final String KEY_WEBSERVICE_TIMEOUT = EMS_PROFILE_ROOT + ".webservice.timeout";
    public static final String KEY_SYNC_WEBSERVICE_TIMEOUT = EMS_PROFILE_ROOT + ".sync.webservice.timeout";
    public static final String KEY_FREETIME_RESERVATION_WEBSERVICE_TIMEOUT = EMS_PROFILE_ROOT + ".freetime.webservice.timeout";

    public static final String KEY_CARTABLE_QUERY_METHOD = EMS_PROFILE_ROOT + ".cartable.query.method";
    public static final String KEY_NUMBER_OF_Estelam2_IMS_TO_LOAD = EMS_PROFILE_ROOT + ".numberOfEstelam2ImsToLoad";

    public static final String KEY_NUMBER_OF_REQUEST_FOR_Estelam_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForEstelamFetchLimit";
    public static final String KEY_NUMBER_OF_REQUEST_FOR_AFIS_RESULT_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForAfisResultFetchLimit";
    public static final String KEY_NUMBER_OF_REQUEST_FOR_CARD_ISSUANCE_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForCardIssuanceFetchLimit";
    public static final String KEY_NUMBER_OF_REQUEST_FOR_UPDATE_STATE_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForUpdateStateFetchLimit";
    public static final String KEY_NUMBER_OF_REQUEST_FOR_UPDATE_AFIS_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForUpdateAFISFetchLimit";
    public static final String KEY_NUMBER_OF_REQUEST_FOR_NOTIFICATION_CARD_FOR_IMS_CMS = EMS_PROFILE_ROOT + ".numberOfRequestForDeliveredIMSandCMSFetchLimit";

    public static final String Signature_Token_Expire_Notification_Days = EMS_PROFILE_ROOT + ".signatureTokenExpireNotificationDays";

    public static final String Auto_Accept_Renewal_Request = EMS_PROFILE_ROOT + ".autoAcceptRenewalRequest";


    // *** portal ***
    public static final String KEY_NUMBER_OF_PORTAL_REQUEST_TO_LOAD = PORTAL_PROFILE_ROOT + ".numberOfPortalRequestToLoad";
    public static final String KEY_NUMBER_OF_REQUEST_TO_UPDATE_STATE = PORTAL_PROFILE_ROOT + ".numberOfRequestToUpdateState";
    public static final String KEY_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD = PORTAL_PROFILE_ROOT + ".numberOfPortalReservationToLoad";
    public static final String KEY_NUMBER_OF_LOCATION_TO_UPDATE = EMS_PROFILE_ROOT + ".numberOfLocationToUpdate";
    public static final String KEY_NUMBER_OF_PORTAL_RESERVATION_ASYNC_CALL_COUNT = EMS_PROFILE_ROOT + ".numberOfPortalReservationAsyncCallCount";
    public static final String KEY_NUMBER_OF_Estelam2_ASYNC_CALL_COUNT = EMS_PROFILE_ROOT + ".numberOfEstelam2AsyncCallCount";
    public static final String KEY_NUMBER_OF_EOF_TO_UPDATE = PORTAL_PROFILE_ROOT + ".numberOfEOFToUpdate";
    public static final String KEY_CARD_REQUEST_STATE_WS_ENDPOINT = PORTAL_PROFILE_ROOT + ".cardRequestStateWSEndPoint";
    public static final String KEY_CARD_REQUEST_STATE_WS_NAMESPACE = PORTAL_PROFILE_ROOT + ".cardRequestStateWSNameSpace";

    //Emks
    public static final String KEY_EMKS_ENDPOINT = EMS_PROFILE_ROOT + ".emksEndpoint";
    public static final String KEY_EMKS_NAMESPACE = EMS_PROFILE_ROOT + ".emksNamespace";
    // *** vip ***
    public static final String KEY_VIP_ENROLLMENT_OFFICE = EMS_PROFILE_ROOT + ".vipEnrollmentOffice";

    public static final String KEY_PURGE_ACTIVATION = EMS_PROFILE_ROOT + ".purgeActivation";
    public static final String KEY_PURGE_AFTER_DELIVERY_DAYS = EMS_PROFILE_ROOT + ".purgeAfterDeliveryDays";
    public static final String KEY_PURGE_COUNT_PER_CYCLE = EMS_PROFILE_ROOT + ".purgeCountPerCycle";
    //lost card confirm
    public static final String KEY_LOST_CARD_CONFIRM = EMS_PROFILE_ROOT + ".lostCardConfirm";

    //sms
    public static final String KEY_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfRequestForSendReservedDateRmindingSmsFetchLimit";
    public static final String KEY_SEND_RESERVE_SMS_TIME_INTERVAL = EMS_PROFILE_ROOT + ".sendReserveSmsTimeInterval";
    public static final String KEY_SMS_BODY_READY_TO_DELIVER = EMS_PROFILE_ROOT + ".smsBodyReadyToDeliverCard";
    public static final String KEY_SMS_BODY_RESERVED_REQUEST = EMS_PROFILE_ROOT + ".smsBodyReservedRequest";
    public static final String KEY_DELETE_FROM_MSGT_ENABLE = EMS_PROFILE_ROOT + ".deleteFromMsgtEnable";
    public static final String KEY_DELETE_FROM_MSGT_TIME_INTERVAL = EMS_PROFILE_ROOT + ".deleteFromMsgtTimeInterval";
    public static final String KEY_NUMBER_OF_PROCESS_RESERVED_SMS_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfProcessReservedSmsFetchLimit";
    public static final String KEY_NUMBER_OF_PROCESS_DELIVERY_SMS_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfProcessDeliverySmsFetchLimit";
    public static final String KEY_NUMBER_OF_PROCESS_REFER_TO_CCOS_SMS_FETCH_LIMIT = EMS_PROFILE_ROOT + ".numberOfProcessReferToCcosSmsFetchLimit";


    //Async
    public static final String KEY_NUMBER_OF_Issuace_ASYNC_CALL_COUNT = EMS_PROFILE_ROOT + ".issuanceAsyncCall";
    public static final String KEY_NUMBER_OF_HandOut_ASYNC_CALL_COUNT = EMS_PROFILE_ROOT + ".handoutAsyncCall";
    public static final String KEY_NUMBER_OF_IMSUPDATE_ASYNC_CALL_COUNT = EMS_PROFILE_ROOT + ".imsUpdateAsyncCall";
    public static final String KEY_ASYNC_ENABLE = EMS_PROFILE_ROOT + ".asyncEnable";
    public static final String KEY_ASYNC_ENABLE_HANDOUT = EMS_PROFILE_ROOT + ".asyncEnableHandOut";
    public static final String KEY_ASYNC_ENABLE_IMSUPDATE = EMS_PROFILE_ROOT + ".asyncEnableImsUpdate";
    public static final String KEY_DURATION_OF_VALID_YEAR_FOR_EXTEND = ".extendValidYear";
    public static final String KEY_DURATION_OF_VALID_YEAR_TOLERANCE_FOR_EXTEND = EMS_PROFILE_ROOT + ".extendValidYearTolerance";

    //Anbari:Estelam
    public static final String KEY_IMS_ESTELAM_IAMGE_ENABLE = EMS_PROFILE_ROOT + ".imsEstelamImageEnable";
    //change manager
    public static final String KEY_IS_DISABLE_USER_IN_CHANGE_MANAGER_ACTION = EMS_PROFILE_ROOT + ".disableUserInChangeManager";

    public static final String KEY_PARSE_NIST_ALLWAYS = EMS_PROFILE_ROOT + ".parseFingerAllAlways";
    public static final String KEY_SAVE_XMLAFIS = EMS_PROFILE_ROOT + ".saveXmlAfis";
    //emks-webservice-timeout
    public static final String KEY_EMKS_WEBSERVICE_TIMEOUT = EMS_PROFILE_ROOT + ".emks.webservice.timeout";
    public static final String KEY_DISPATCH_CARTABLE_QUERY_METHOD = EMS_PROFILE_ROOT + ".dispatch.cartable.query.method";

    //bizlog
    public static final String KEY_BIZLOG_INSERT_METHOD = EMS_PROFILE_ROOT + ".bizLogInsertMethod";

    //Cache Certificate
    public static final String KEY_IS_CERT_CACHE_ENABLE = EMS_PROFILE_ROOT + ".enableCertCache";

    //CCOS version
    public static final String KEY_CCOS_EXACT_VERSION = EMS_PROFILE_ROOT + ".ccosExactVersion";
    public static final String KEY_ENABLE_CCOS_VERSION_CHECK = EMS_PROFILE_ROOT + ".enableCcosVersionCheck";

    //reporting
    public static final String KEY_REPORTING_JNDI_NAME = EMS_PROFILE_ROOT + ".reportingJndiName";

    //purge
    public static final String KEY_NUMBER_OF_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT = EMS_PROFILE_ROOT + ".purgeBioFetchLimit";
    public static final String KEY_PURGE_BIO_TIME_INTERVAL = EMS_PROFILE_ROOT + ".purgeBioTimeInterval";
    public static final String KEY_SAVE_PURGE_HISTORY = EMS_PROFILE_ROOT + ".savePurgeHistory";

    //Gaas DS
    public static final String KEY_GAAS_JNDI_NAME = EMS_PROFILE_ROOT + ".gaasJndiName";

    public static final String KEY_INTERVAL_PURGE_UP = EMS_PROFILE_ROOT + ".intervalPurgeUp";
    public static final String KEY_INTERVAL_PURGE_DOWN = EMS_PROFILE_ROOT + ".intervalPurgeDown";

    //Payment Amount
    public static final String KEY_PAYMENT_AMOUNT_FIRST_CARD = EMS_PROFILE_ROOT + ".firstCardPaymentAmount";
    public static final String KEY_PAYMENT_AMOUNT_FIRST_REPLICA = EMS_PROFILE_ROOT + ".firstReplicaCardPaymentAmount";
    public static final String KEY_PAYMENT_AMOUNT_SECOND_REPLICA = EMS_PROFILE_ROOT + ".secondReplicaCardPaymentAmount";
    public static final String KEY_PAYMENT_AMOUNT_THIRD_REPLICA = EMS_PROFILE_ROOT + ".thirdReplicaCardPaymentAmount";
    public static final String KEY_PAYMENT_AMOUNT_REPLACE = EMS_PROFILE_ROOT + ".replaceCardPaymentAmount";
    public static final String KEY_PAYMENT_AMOUNT_EXTEND = EMS_PROFILE_ROOT + ".extendCardPaymentAmount";
    public static final String KEY_OFFICE_CAPACITY_END_DATE = EMS_PROFILE_ROOT + ".officeCapacityEndDate";

    //Bpi
    public static final String KEY_BPI_ENDPOINT = EMS_PROFILE_ROOT + ".bpiEndpoint";
    public static final String KEY_BPI_NAMESPACE = EMS_PROFILE_ROOT + ".bpiNamespace";

    //workstation info
    public static final String KEY_WORKSTATION_INFO_CHECK_PERIOD = EMS_PROFILE_ROOT + ".workstationCheckPeriod";

    //PaymentCodeServiceImpl
    public static final String KEY_CPI_DEPOSIT_ID = EMS_PROFILE_ROOT + ".CPIDepositId";
    public static final String KEY_ORGANIZATION_PAYMENT_CODE = EMS_PROFILE_ROOT + ".organizationPaymentCode";
    public static final String KEY_CPI_INCOME_CODE = EMS_PROFILE_ROOT + ".CPIIncomeCode";


}
