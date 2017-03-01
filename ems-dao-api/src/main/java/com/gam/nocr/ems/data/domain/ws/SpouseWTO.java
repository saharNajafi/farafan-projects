package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class SpouseWTO {
    private Long id;
    private Long citizenId;
    private String firstNameFA;
    private String sureNameFA;
    private String fatherName;
    private String birthCerId;
    private String nationalId;
    private String birthCertSeries;
    private Date birthDate;
    private Date marriageDate;
    private Date deathOrDivorceDate;
    private Long maritalStatusId;

    public SpouseWTO() {
    }

    @JSON(include = false)
    public Long getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(Long maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JSON(include = false)
    public String getFirstNameFA() {
        return firstNameFA;
    }

    public void setFirstNameFA(String firstNameFA) {
        this.firstNameFA = firstNameFA;
    }

    @JSON(include = false)
    public String getSureNameFA() {
        return sureNameFA;
    }

    public void setSureNameFA(String sureNameFA) {
        this.sureNameFA = sureNameFA;
    }

    @JSON(include = false)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @JSON(include = false)
    public String getBirthCerId() {
        return birthCerId;
    }

    public void setBirthCerId(String birthCerId) {
        this.birthCerId = birthCerId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @JSON(include = false)
    public String getBirthCertSeries() {
        return birthCertSeries;
    }

    public void setBirthCertSeries(String birthCertSeries) {
        this.birthCertSeries = birthCertSeries;
    }

    @JSON(include = false)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @JSON(include = false)
    public Date getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(Date marriageDate) {
        this.marriageDate = marriageDate;
    }

    public Date getDeathOrDivorceDate() {
        return deathOrDivorceDate;
    }

    public void setDeathOrDivorceDate(Date deathOrDivorceDate) {
        this.deathOrDivorceDate = deathOrDivorceDate;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
