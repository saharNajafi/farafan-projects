package com.gam.nocr.ems.data.enums;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author: Haeri (haeri@gamelectronics.com)
 */
public enum Gender {
    M,
    F,
    NA,
    UNDEFINED;

    public static String convertToString(Gender gender) {
        switch (gender) {
            case M:
                return "1";

            case F:
                return "2";

            default:
                return "255";
        }
    }

    public static Gender convertFromIMSToEMS(int imsGender) {
        switch (imsGender) {
            case 0:
                return M;

            case 1:
                return F;

            default:
                return UNDEFINED;
        }
    }

    public static Gender convertFromIMSEstelamResultGender(int imsGender) {
        switch (imsGender) {
            case 0:
                return F;

            case 1:
                return M;

            default:
                return UNDEFINED;
        }
    }
}
