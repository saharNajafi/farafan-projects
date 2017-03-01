package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum EnrollmentOfficeStatus {
    UNKNOWN,    // 0 : UNKNOWN
    ENABLED,    // 1 : ENABLED
    DISABLED;    // 2 : DISABLED

    public static Integer toInteger(EnrollmentOfficeStatus state) {
        if (state == null) {
            return null;
        }

        switch (state) {
            case UNKNOWN:
                return 0;
            case ENABLED:
                return 1;
            case DISABLED:
                return 2;
        }
        return null;
    }

    public static EnrollmentOfficeStatus toState(Integer state) {
        if (state == null) {
            return null;
        }

        switch (state) {
            case 0:
                return UNKNOWN;
            case 1:
                return ENABLED;
            case 2:
                return DISABLED;
        }
        return null;
    }
}
