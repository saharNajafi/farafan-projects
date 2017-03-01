package com.gam.nocr.ems.data.enums;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public enum TokenState {
    READY_TO_ISSUE,
    
    PENDING_TO_ISSUE,
    READY_TO_DELIVER,
    DELIVERED,
    REVOKED,
    PKI_ERROR,
    
    //Adldoost
    PENDING_FOR_EMS,
    READY_TO_RENEWAL_ISSUE,
    PENDING_TO_RENEWAL_ISSUE,
    READY_TO_RENEWAL_DELIVER,
    EMS_REJECT,
    SUSPENDED,

    /**
     * PKI Token States
     */
    PROCESSED,
    WAITING_FOR_PICKUP,
    PICKUP_IN_PROGRESS;
}
