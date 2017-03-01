package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum LocationType {
    PROVINCE,
    COUNTY,
    TOWNSHIP,
    DISTRICT,
    CITY,
    VILLAGE,
    COUNTRY;

    public static String toNocrString(LocationType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case PROVINCE:
                return "4";
            case COUNTY:
                return "5";
            case TOWNSHIP:
                return "6";
            case DISTRICT:
                return "7";
            case CITY:
                return "1";
            case VILLAGE:
                return "2";
            case COUNTRY:
                return "3";
        }
        return null;
    }

    public static LocationType toType(String nocrString) {
        if (nocrString == null) {
            return null;
        }

        switch (Integer.parseInt(nocrString)) {
            case 4:
                return PROVINCE;
            case 5:
                return COUNTY;
            case 6:
                return TOWNSHIP;
            case 7:
                return DISTRICT;
            case 1:
                return CITY;
            case 2:
                return VILLAGE;
            case 3:
                return COUNTRY;
        }
        return null;
    }
}
