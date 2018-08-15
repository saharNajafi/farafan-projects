package com.gam.nocr.ems.data.enums;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum RegistrationStateEnum {
   DRAFT("DRAFT","پیش نویس"),PENDING("PENDING","منتظر ارسال"),IN_PROGRESS("IN_PROGRESS","در حال پردازش"),
    REPEALED("REPEALED","باطل شده"),DELIVERED("DELIVERED","تحویل شده"),DAMAGED_CARD("DAMAGED_CARD","کارت خراب"),
    UNAVAILABLE_CARD("UNAVAILABLE_CARD","کارت غیرقابل دسترس"),EXPIRE_CARD("EXPIRE_CARD","کارت منقضی");

    RegistrationStateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private final String code;
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static String getStateName(String code) {
        for (RegistrationStateEnum registrationStateEnum : values()) {
            if (code.equals(registrationStateEnum.getCode())){
                return registrationStateEnum.getName();
            }
        }
        return "ندارد";
    }

    public static RegistrationStateEnum getState(String code) {
        for (RegistrationStateEnum registrationStateEnum : values()) {
            if (code.equals(registrationStateEnum.getCode())){
                return registrationStateEnum;
            }
        }
        return null;
    }
}
