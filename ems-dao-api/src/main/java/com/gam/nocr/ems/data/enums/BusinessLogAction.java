package com.gam.nocr.ems.data.enums;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public enum BusinessLogAction {
    INSERT,//
    UPDATE,//
    DELETE,//
    LOAD,//
    SAVE,
    REMOVE,
    FIND,
    APPROVE,//
    REJECT,//
    CHANGE_STATE,
    ISSUE,
    REISSUE,
    REPLICATE,
    REVOKE,
    DELIVER,//
    UNSUCCESSFUL_DELIVER,//
    LOAD_ROLES,
    LOAD_PERMISSIONS,
    LOST,//
    FOUND,//
    RECEIVED,//
    NOT_RECEIVED,//
    SENT,//
    UNDO,//
    RESET,//

    INVOKE_EXTERNAL_SERVICE,
    EXECUTE_JOB,
    AFTER_DELIVERY_CHANGE_PIN,
    AFTER_DELIVERY_REPEAL_SIGNATURE,
    AFTER_DELIVERY_RESUME,
    AFTER_DELIVERY_REVOKE,
    AFTER_DELIVERY_SUSPEND,
    AFTER_DELIVERY_UNBLOCK,
    AFTER_DELIVERY_UPDATE_DYNAMIC_DATA,

    PRODUCTION_ERROR, //
    CHANGE_PASSWORD, //
    AUTHENTICATE, //
    NOTIFY_MISSED, //
    NOTIFY_RECEIVED, //
    NOTIFY_ADDED, //
    NOTIFY_REMOVED, //
    NOTIFY_MODIFIED, //
    NOTIFY_HANDOUT, //
    BATCH_ENQUIRY_REQUEST, //
    BATCH_ENQUIRY_RESPONSE, //
    LOAD_UPDATED_CITIZENS,//
    UPDATE_CITIZENS_INFO, //
    PROCESS_UNSUCCESSFUL_DELIVERY,//
    VERIFY_PENDING_TOKENS,//
    VERIFY_READY_TOKENS,     //

    PERSON_TOKEN_REQUEST,
    PERSON_TOKEN_RESPONSE,
    NETWORK_TOKEN_REQUEST,
    NETWORK_TOKEN_RESPONSE,

    /**
     * Portal Actions
     */
    FETCH_RESERVATION_INFO,
    LOAD_FROM_PORTAL,
    UPDATE_CCOS_AND_IMS_VERIFIED_MES_REQUESTS,
    UPDATE_NOT_VERIFIED_MES_REQUESTS,
    UPDATE_PROVINCE_LIST,
    UPDATE_REQUEST_STATES,

    /**
     * IMS Actions
     */
    SEND_REQUEST_FOR_OFFLINE_ENQUIRY,
    RECEIVE_REQUEST_FROM_OFFLINE_ENQUIRY,
    SEND_REQUEST_FOR_ONLINE_ENQUIRY,
    RECEIVE_REQUEST_FROM_ONLINE_ENQUIRY,
    FETCH_CITIZEN_INFO_FROM_IMS, 
    
    /**
     * EMKS Actions
     */
    GET_PINS,
    GET_MOC_KEYS,
    /**
     * VIP Actions
     */
    INSERT_VIP
    
    

}
