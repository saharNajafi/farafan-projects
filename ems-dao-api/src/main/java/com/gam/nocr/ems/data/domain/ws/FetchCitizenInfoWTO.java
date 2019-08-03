package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.CardRequestType;

import java.io.Serializable;

public class FetchCitizenInfoWTO implements Serializable {

    private String nationalId;
    private String birthDate;
    private String crn;
    private CardRequestType type;

    public FetchCitizenInfoWTO() {
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public CardRequestType getType() {
        return type;
    }

    public void setType(CardRequestType type) {
        this.type = type;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
