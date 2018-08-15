package com.gam.nocr.ems.data.domain.vol;

import java.io.Serializable;

/**
 * Created by Saeid on 8/15/2018.
 */
public class IMSErrorInfo implements Serializable{

    private int code;
    private String desc;

    public IMSErrorInfo() {
    }

    public IMSErrorInfo(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "IMSErrorInfo{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
