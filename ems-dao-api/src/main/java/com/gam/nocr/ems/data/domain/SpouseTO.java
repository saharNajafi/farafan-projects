package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
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
@Table(name = "EMST_SPOUSE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_SPOUSE", allocationSize = 1)
public class SpouseTO extends ExtEntityTO implements JSONable {

    private CitizenInfoTO citizenInfo;
    private String spouseFirstNamePersian;
    private String spouseSurnamePersian;
    private String spouseNationalID;
    private Date spouseMarriageDate;
    private Date spouseDeathOrDivorceDate;
    private MaritalStatusTO maritalStatus;
    private String spouseFatherName;
    private String spouseBirthCertificateId;
    private String spouseBirthCertificateSeries;
    private Date spouseBirthDate;

    public SpouseTO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "SPS_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "SPS_CITIZEN_INFO_ID")
    @JSON(include = false)
    public CitizenInfoTO getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(CitizenInfoTO citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Column(name = "SPS_FIRST_NAME_FA", length = 84)
    @JSON(include = false)
    public String getSpouseFirstNamePersian() {
        return spouseFirstNamePersian;
    }

    public void setSpouseFirstNamePersian(String spouseFirstNamePersian) {
        this.spouseFirstNamePersian = spouseFirstNamePersian;
    }

    @Column(name = "SPS_SURNAME_FA", length = 84)
    @JSON(include = false)
    public String getSpouseSurnamePersian() {
        return spouseSurnamePersian;
    }

    public void setSpouseSurnamePersian(String spouseSurnamePersian) {
        this.spouseSurnamePersian = spouseSurnamePersian;
    }

    @Size(min = 10, max = 10)
    @Column(name = "SPS_NATIONAL_ID")
    public String getSpouseNationalID() {
        return spouseNationalID;
    }

    public void setSpouseNationalID(String spouseNationalID) {
        this.spouseNationalID = spouseNationalID;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SPS_MARRIAGE_DATE", nullable = false)
    @JSON(include = false)
    public Date getSpouseMarriageDate() {
        return spouseMarriageDate;
    }

    public void setSpouseMarriageDate(Date spouseMarriageDate) {
        this.spouseMarriageDate = spouseMarriageDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SPS_DEATH_DIVORCE_DATE")
    @JSON(include = false)
    public Date getSpouseDeathOrDivorceDate() {
        return spouseDeathOrDivorceDate;
    }

    public void setSpouseDeathOrDivorceDate(Date spouseDeathOrDivorceDate) {
        this.spouseDeathOrDivorceDate = spouseDeathOrDivorceDate;
    }

    @ManyToOne
    @JoinColumn(name = "SPS_MARITAL_STATUS_ID", nullable = false)
    @JSON(include = false)
    public MaritalStatusTO getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusTO maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Column(name = "SPS_FATHER_NAME", length = 84)
    @JSON(include = false)
    public String getSpouseFatherName() {
        return spouseFatherName;
    }

    public void setSpouseFatherName(String spouseFatherName) {
        this.spouseFatherName = spouseFatherName;
    }

    @Column(name = "SPS_BIRTH_CERT_ID", length = 20)
    @JSON(include = false)
    public String getSpouseBirthCertificateId() {
        return spouseBirthCertificateId;
    }

    public void setSpouseBirthCertificateId(String spouseBirthCertificateId) {
        this.spouseBirthCertificateId = spouseBirthCertificateId;
    }

    @Column(name = "SPS_BIRTH_CERT_SERIES")
    @JSON(include = false)
    public String getSpouseBirthCertificateSeries() {
        return spouseBirthCertificateSeries;
    }

    public void setSpouseBirthCertificateSeries(String spouseBirthCertificateSeries) {
        this.spouseBirthCertificateSeries = spouseBirthCertificateSeries;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SPS_BIRTH_DATE_SOL", nullable = false)
    @JSON(include = false)
    public Date getSpouseBirthDate() {
        return spouseBirthDate;
    }

    public void setSpouseBirthDate(Date spouseBirthDate) {
        this.spouseBirthDate = spouseBirthDate;
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
