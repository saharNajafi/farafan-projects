package com.gam.nocr.ems.data.enums;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum IPGProviderEnum {
    SADAD("SADAD","بانک ملی - سداد"),PARSIAN("PARSIAN","بانک پارسیان"),UNDEFINED("UNDEFINED","نامشخص");

    private final String code;
    private final String name;

    IPGProviderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getProviderName(String code){
        for (IPGProviderEnum ipgProviderEnum : values()) {
            if (ipgProviderEnum.getCode().equals(code)){
                return ipgProviderEnum.getName();
            }
        }
        return "-";
    }

    public static boolean isValidProviderCode(String ipgProviderCode) {
        for (IPGProviderEnum ipgProviderEnum : IPGProviderEnum.values()) {
            if (ipgProviderEnum.getCode().equalsIgnoreCase(ipgProviderCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public static IPGProviderEnum getProvider(String paidBankCode) {
        for (IPGProviderEnum ipgProviderEnum : IPGProviderEnum.values()) {
            if (ipgProviderEnum.getCode().equalsIgnoreCase(paidBankCode)){
                return ipgProviderEnum;
            }
        }

        return null;
    }
}
