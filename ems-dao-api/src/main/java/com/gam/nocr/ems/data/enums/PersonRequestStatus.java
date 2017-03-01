package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum PersonRequestStatus {

    REQUESTED,
    APPROVED,
    REJECTED;

    public static Long toLong(PersonRequestStatus state) {
        if (state == null) {
            return null;
        }

        switch (state) {
            case REQUESTED:
                return 1L;
            case APPROVED:
                return 2L;
            case REJECTED:
                return 3L;
        }

        return null;
    }

    public static PersonRequestStatus toState(Long state) {
        if (state == null) {
            return null;
        }

        switch (state.intValue()) {
            case 1:
                return REQUESTED;
            case 2:
                return APPROVED;
            case 3:
                return REJECTED;
        }

        return null;
    }
}
