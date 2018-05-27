package com.gam.nocr.ems.data.enums;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/4/2017.
 */
public enum ShiftEnum {
    MORNING("0","صبح"),EVENING("1","عصر");

    ShiftEnum(String code, String name) {
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

    public static String getShiftName(String code) {
        for (ShiftEnum shiftEnum : values()) {
            if (code.equals(shiftEnum.getCode())){
                return shiftEnum.getName();
            }
        }
        return "ندارد";
    }

    public static ShiftEnum getShift(String shiftCode) {
        for (ShiftEnum shiftEnum : values()) {
            if (shiftCode.equalsIgnoreCase(shiftEnum.getCode())){
                return shiftEnum;
            }
        }
        return null;
    }
}
