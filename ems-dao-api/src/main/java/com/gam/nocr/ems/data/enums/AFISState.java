package com.gam.nocr.ems.data.enums;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public enum AFISState {
    NOT_VALID,
    VALID,
    VIP;

    public static String convertToString(AFISState afisState) {
        switch (afisState) {
            case NOT_VALID:
                return "0";

            case VALID:
                return "1";

            case VIP:
                return "2";

            default:
                return null;
        }
    }

    public static AFISState convertToAFISState(String afisState) {
        switch (afisState.charAt(0)) {
            case '0':
                return NOT_VALID;

            case '1':
                return VALID;

            case '2':
                return VIP;

            default:
                return null;
        }
    }
}
