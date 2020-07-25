package com.gam.nocr.ems.data.enums;

/**
 * @author Ali Amiri
 */
public enum TokenReason {
    FIRST_TOKEN(1,"N","صدوراولیه"),
    REPLACED(2,"D","صدور مجدد به دلیل خرابی"),
    REPLICA(3,"R","صدورالمثنی"),
    EXTEND(4,"E","صدور به دلیل انقضاء");

    private final Integer id;
    private final String code;
    private final String name;

    TokenReason(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static String getTokenReasonName(String code) {
        for (TokenReason tokenReason : values()) {
            if (code.equals(tokenReason.getCode())){
                return tokenReason.getName();
            }
        }
        return "ندارد";
    }

    public static TokenReason getGender(String tokenReason) {
        for (TokenReason reason : values()) {
            if (tokenReason.equalsIgnoreCase(reason.getCode())){
                return reason;
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
