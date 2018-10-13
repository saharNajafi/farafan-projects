package com.gam.nocr.ems.data.enums;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public enum BiometricType {
    FACE_IMS,
    FACE_CHIP,
    FACE_MLI,
    FACE_LASER,

    FING_ALL,
    FING_CANDIDATE,
    FING_MIN_1,
    FING_MIN_2,
    //vip
    VIP_IMAGE,
    FING_NORMAL_1,
    FING_NORMAL_2;


    public static String toWTOString(BiometricType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case FACE_IMS:
                return "1";
            case FACE_CHIP:
                return "2";
            case FACE_MLI:
                return "3";
            case FACE_LASER:
                return "4";
            case FING_ALL:
                return "5";
            case FING_CANDIDATE:
                return "6";
            case FING_MIN_1:
                return "7";
            case FING_MIN_2:
                return "8";
            case VIP_IMAGE:
                return "9";
            case FING_NORMAL_1:
                return "10";
            case FING_NORMAL_2:
                return "11";
            
        }

        return null;
    }

    public static BiometricType toType(String type) {
        if (type == null || type.trim().equals("")) {
            return null;
        }
        switch (type.trim().charAt(0)) {
            case '1':
                return FACE_IMS;
            case '2':
                return FACE_CHIP;
            case '3':
                return FACE_MLI;
            case '4':
                return FACE_LASER;
            case '5':
                return FING_ALL;
            case '6':
                return FING_CANDIDATE;
            case '7':
                return FING_MIN_1;
            case '8':
                return FING_MIN_2;
            case '9':
                return VIP_IMAGE;
        }
        if(type.equals("10")){
            return FING_NORMAL_1;
        }
        if(type.equals("11")){
            return FING_NORMAL_2;
        }
        return null;
    }
}
