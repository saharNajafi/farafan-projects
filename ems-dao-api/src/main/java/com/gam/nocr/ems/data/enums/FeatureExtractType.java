package com.gam.nocr.ems.data.enums;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/31/18.
 */
public enum FeatureExtractType {
    NORMAL,
    CC,
    ISOCC;

    public static String convertToString(FeatureExtractType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case NORMAL:
                return "1";
            case CC:
                return "2";
            case ISOCC:
                return "3";
        }
        return null;
    }

    public static FeatureExtractType toType(String type) {
        if (type == null || type.trim().equals("")) {
            return null;
        }
        switch (type.trim()) {
            case "1":
                return NORMAL;
            case "2":
                return CC;
            case "3":
                return ISOCC;
        }
        return null;
    }
}
