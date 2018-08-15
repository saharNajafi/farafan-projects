package com.gam.nocr.ems.data.enums;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum YesNoEnum {
    YES("YES","بله"),NO("NO","خیر");

    YesNoEnum(String code, String name) {
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

    public static YesNoEnum getOption(String option) {
        for (YesNoEnum yesNoEnum : values()) {
            if (option.equalsIgnoreCase(yesNoEnum.getCode())){
                return yesNoEnum;
            }
        }
        return null;
    }
}
