package com.gam.nocr.ems.data.enums;


import com.gam.nocr.ems.data.domain.ReligionTO;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum ReligionEnum {
    ISLAM("1","اسلام"),CHRIST("2","مسیحیت"),ZARTOSHT("3","زرتشت"),KALIMI("4","کلیم"),OTHER("5","سایر");

    ReligionEnum(String code, String name) {
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

    public static String getReligionName(String code) {
        for (ReligionEnum religion : values()) {
            if (code.equals(religion.getCode())){
                return religion.getName();
            }
        }
        return "ندارد";
    }

    public static ReligionEnum getReligion(String religion) {
        for (ReligionEnum religionEnum : values()) {
            if (religion.equals(religionEnum.getCode())){
                return religionEnum;
            }
        }
        return null;
    }

    public static ReligionTO getEMSReligion(ReligionEnum religion) {
        return new ReligionTO(Long.valueOf(religion.getCode()));
    }
}
