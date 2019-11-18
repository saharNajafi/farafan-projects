package com.gam.nocr.ems.data.enums;

public enum PaymentTypeEnum {

    IPG(1), PCPOSE(2);

    private final Integer code;

    PaymentTypeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
