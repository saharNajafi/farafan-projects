package com.gam.nocr.ems.data.enums;REPLICA

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public enum CardRequestType {
    FIRST_CARD,
    UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD,

    EXTEND,
    UNSUCCESSFUL_DELIVERY_FOR_EXTEND,

    REPLICA,
    UNSUCCESSFUL_DELIVERY_FOR_REPLICA,

    REPLACE,
    UNSUCCESSFUL_DELIVERY_FOR_REPLACE,

    UNSUCCESSFUL_DELIVERY;

    public static Long toLong(CardRequestType crt) {
        if (crt == null) {
            return null;
        }

        switch (crt) {
            case FIRST_CARD:
                return 1L;
            case UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD:
                return 1L;
            case EXTEND:
                return 3L;
            case UNSUCCESSFUL_DELIVERY_FOR_EXTEND:
                return 3L;
            case REPLICA:
                return 2L;
            case UNSUCCESSFUL_DELIVERY_FOR_REPLICA:
                return 2L;
            case REPLACE:
                return 4L;
            case UNSUCCESSFUL_DELIVERY_FOR_REPLACE:
                return 4L;
            case UNSUCCESSFUL_DELIVERY:
                return 5L;
        }
        return null;
    }

    public static CardRequestType toType(Long type) {
        if (type == null) {
            return null;
        }

        switch (type.intValue()) {
            case 1:
                return FIRST_CARD;
            case 2:
                return REPLICA;
            case 3:
                return EXTEND;
            case 4:
                return REPLACE;
            case 5:
                return REPLACE;
        }
        return null;
    }
}
