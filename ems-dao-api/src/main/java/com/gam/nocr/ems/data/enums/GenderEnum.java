package com.gam.nocr.ems.data.enums;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum GenderEnum {
    FEMALE("0","زن","F"),MALE("1","مرد","M");

    GenderEnum(String code, String name, String emsCode) {
        this.code = code;
        this.name = name;
        this.emsCOde = emsCode;
    }

    private final String code;
    private final String emsCOde;
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmsCOde() {
        return emsCOde;
    }

    @Override
    public String toString() {
        return name;
    }

    public static String getGenderName(String code) {
        for (GenderEnum sexEnum : values()) {
            if (code.equals(sexEnum.getCode())){
                return sexEnum.getName();
            }
        }
        return "ندارد";
    }

    public static GenderEnum getGender(String gender) {
        for (GenderEnum sexEnum : values()) {
            if (gender.equalsIgnoreCase(sexEnum.getCode())){
                return sexEnum;
            }
        }
        return null;
    }

    public static Gender getEMSGender(GenderEnum sex) {
        return Gender.valueOf(sex.getEmsCOde());
    }
}
