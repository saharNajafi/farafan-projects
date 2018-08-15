package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.GenderEnum;

import java.io.Serializable;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/1/18.
 */
public class PersonalInfoWTO extends SecureWTO implements Serializable {
    private String nationalId;
    private String birthDateSolar;
    private String certSerialNo;
    private GenderEnum gender;

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getBirthDateSolar() {
        return birthDateSolar;
    }

    public void setBirthDateSolar(String birthDateSolar) {
        this.birthDateSolar = birthDateSolar;
    }

    public String getCertSerialNo() {
        return certSerialNo;
    }

    public void setCertSerialNo(String certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }
}
