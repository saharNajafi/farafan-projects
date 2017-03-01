package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CHILD")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CHILD", allocationSize = 1)
public class ChildTO extends ExtEntityTO implements JSONable {
    private CitizenInfoTO citizenInfo;
    private String childFirstNamePersian;
    private Gender childGender;
    private String childNationalID;
    private Date childBirthDateSolar;
    private Date childDeathDateSolar;
    private String childFatherName;
    private String childBirthCertificateId;
    private String childBirthCertificateSeries;

    public ChildTO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CHI_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "CHI_CITIZEN_INFO_ID")
    @JSON(include = false)
    public CitizenInfoTO getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(CitizenInfoTO citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Column(name = "CHI_FIRST_NAME_FA", length = 84)
    @JSON(include = false)
    public String getChildFirstNamePersian() {
        return childFirstNamePersian;
    }

    public void setChildFirstNamePersian(String childFirstNamePersian) {
        this.childFirstNamePersian = childFirstNamePersian;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CHI_GENDER")
    @JSON(include = false)
    public Gender getChildGender() {
        return childGender;
    }

    public void setChildGender(Gender childGender) {
        this.childGender = childGender;
    }

    @Size(min = 10, max = 10)
    @Column(name = "CHI_NATIONAL_ID")
    public String getChildNationalID() {
        return childNationalID;
    }

    public void setChildNationalID(String childNationalID) {
        this.childNationalID = childNationalID;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHI_BIRTH_DATE_SOL", nullable = false)
    public Date getChildBirthDateSolar() {
        return childBirthDateSolar;
    }

    public void setChildBirthDateSolar(Date childBirthDateSolar) {
        this.childBirthDateSolar = childBirthDateSolar;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHI_DEATH_DATE_SOL")
    @JSON(include = false)
    public Date getChildDeathDateSolar() {
        return childDeathDateSolar;
    }

    public void setChildDeathDateSolar(Date childDeathDateSolar) {
        this.childDeathDateSolar = childDeathDateSolar;
    }

    @Column(name = "CHI_FATHER_NAME", length = 84)
    @JSON(include = false)
    public String getChildFatherName() {
        return childFatherName;
    }

    public void setChildFatherName(String childFatherName) {
        this.childFatherName = childFatherName;
    }

    @Column(name = "CHI_BIRTH_CERT_ID", length = 20)
    @JSON(include = false)
    public String getChildBirthCertificateId() {
        return childBirthCertificateId;
    }

    public void setChildBirthCertificateId(String childBirthCertificateId) {
        this.childBirthCertificateId = childBirthCertificateId;
    }

    @Column(name = "CHI_BIRTH_CERT_SERIES")
    @JSON(include = false)
    public String getChildBirthCertificateSeries() {
        return childBirthCertificateSeries;
    }

    public void setChildBirthCertificateSeries(String childBirthCertificateSeries) {
        this.childBirthCertificateSeries = childBirthCertificateSeries;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (citizenInfo == null) {
            jsonObject += ", citizenInfoId:" + citizenInfo;
        } else {
            jsonObject += ", citizenInfoId:" + citizenInfo.getId();
        }
        jsonObject += "}";

        return jsonObject;
    }
}
