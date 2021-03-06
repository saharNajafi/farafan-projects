package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public enum CardRequestHistoryAction {

    TRANSFER_FROM_PORTAL,
    PENDING_IMS,
    IMS_SEND_ERROR,
    IMS_RECEIVE_ERROR,
    RECEIVED_FROM_IMS,
    VERIFIED_IMS,
    NOT_VERIFIED_BY_IMS,
    TRANSFER_RESERVE,
    COMPLETE_REGISTRATION,
    AUTHENTICATE_DOCUMENT,
    DOCUMENT_SCAN,
    FINGER_SCAN,
    FACE_SCAN,
    MANAGER_APPROVAL,
    SENT_TO_AFIS,
    AFIS_SEND_ERROR,
    AFIS_RECEIVE_ERROR,
    APPROVED_BY_AFIS,
    PENDING_ISSUANCE,
    CMS_ERROR,
    CMS_XSD_ERROR,
    ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY,
    BATCH_PRODUCTION,
    BOX_SHIPMENT,
    PRODUCTION_ERROR,
    BATCH_RECEIVED,
    READY_TO_DELIVER,
    DELIVERED_TO_CITIZEN,
    NOTIFY_CARD_DELIVER,
    NOTIFY_CARD_MISSED,
    REPEAL_CARD_REQUEST,
    TRANSFER_TO_SUPERIOR_OFFICE,
    UNDO_TRANSFER_FROM_SUPERIOR_OFFICE,

    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IDENTITY_CHANGE,
    //ganjyar
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_FORBIDDEN,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_DIED,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_CFINGER,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER_IMAGE,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IMAGE,
    //
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC,
    
    SYNC_SUCCESS,
    SYNC_FAILED,
    
    IMS_IMAGE_NOT_FOUND,
    IMS_ESTELAM_RETRY,    
    AFIS_WAITING,
    AFIS_DOCUMENT_ERROR,
    AFIS_IMAGE_ERROR,
    AFIS_FINGER_ERROR,
    AFIS_IMAGES_UPDATED,
    AFIS_REJECT,
    AFIS_DELIVERED_ERROR,
    
    
    BIO_DUC_PURGED,
    AFTER_DELIVERY_UNBLOCK,
    CHANGE_PIN,
    REJECT_FACE_INFO_BY_MANAGER,
    REJECT_DOC_INFO_BY_MANAGER,
    PRINT_REGISTRATION_RECEIPT,
    UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER, // if user tried 14 times and finger print didn't match
    PRIORITY_IS_CHANGED,
    AFTER_DELIVERY_SUSPEND,
    AFTER_DELIVERY_RESUME,
    Delivery_Office_Change
}
