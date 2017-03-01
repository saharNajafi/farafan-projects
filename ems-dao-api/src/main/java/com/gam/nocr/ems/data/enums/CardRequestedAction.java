package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum CardRequestedAction {
    REPEALING,
    REPEAL_UNDO,
    REPEAL_ACCEPTED,
    REPEAL_COMPLETE,

    TRANSFER_IN_PROGRESS,
    TRANSFER_UNDO,
    TRANSFERRED,

    card_production_error_face,
    card_production_error_retry,
    cms_error_face,
    cms_error_finger

}
