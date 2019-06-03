package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.IPGProviderEnum;

import java.io.Serializable;

public class TargetBankWTO implements Serializable {

    private IPGProviderEnum paidBank;
    private String nationalId;

    public IPGProviderEnum getPaidBank() {
        return paidBank;
    }

    public void setPaidBank(IPGProviderEnum paidBank) {
        this.paidBank = paidBank;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
}
