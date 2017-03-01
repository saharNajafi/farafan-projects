package com.gam.nocr.ems.data.enums;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public enum CMSCardState {

    /**
     * Card states in CMS sub system
     */
    ISSUED(2),
    SHIPPED(3),
    ARRIVED(4),
    HANDED_OUT(5),
    MISSING(6),
    EXPIRED(7),
    SUSPENDED(8),
    REVOKED(9),
    DESTROYED(10);

    private int cmsCardState;

    private CMSCardState(int cmsCardState) {
        this.cmsCardState = cmsCardState;
    }

    public int getCmsCardState() {
        return cmsCardState;
    }

    public static boolean contains(String value) {
        try {
            CMSCardState.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
