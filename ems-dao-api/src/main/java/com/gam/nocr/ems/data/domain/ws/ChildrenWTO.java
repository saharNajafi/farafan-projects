package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ChildrenWTO {
    private Long id;
    private Long citizenId;
    private String fistNameFA;
    private String fatherName;
    private String birthCertId;
    private Date birthDate;
    private Date deathDate;
    private String nationalId;
    private String gender;
    private String birthCertSeries;

    public ChildrenWTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JSON(include = false)
    public String getFistNameFA() {
        return fistNameFA;
    }

    public void setFistNameFA(String fistNameFA) {
        this.fistNameFA = fistNameFA;
    }

    @JSON(include = false)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBirthCertId() {
        return birthCertId;
    }

    @JSON(include = false)
    public void setBirthCertId(String birthCertId) {
        this.birthCertId = birthCertId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @JSON(include = false)
    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @JSON(include = false)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JSON(include = false)
    public String getBirthCertSeries() {
        return birthCertSeries;
    }

    public void setBirthCertSeries(String birthCertSeries) {
        this.birthCertSeries = birthCertSeries;
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
