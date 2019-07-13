package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.CardRequestType;

import java.io.Serializable;
import java.util.Date;

public class fetchCitizenInfoWTO implements Serializable {

    private String nationalId;
    private Date birthDate;
    private String crn;
    private CardRequestType type;

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
}
