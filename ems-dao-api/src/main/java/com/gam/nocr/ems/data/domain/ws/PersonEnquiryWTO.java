package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.Gender;
import flexjson.JSON;

import java.io.Serializable;

/**
 * @author Saeed Rastak
 */
public class PersonEnquiryWTO implements Serializable {

    private String nationalId;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String birthCertificateSeries;
    private String birthCertificateSerial;
    private String birthCertificateId;
    private String solarBirthDate;
    private Gender gender;
    private String metadata;
    private String logInfo;
    private String description;
    private String imageDescription;
    private byte[] nidImage;
    private byte[] certImage;
    private int isDead = 0;
    private Boolean isExceptionMessage = false;
    private Boolean isServiceDown = false;
    private Boolean isEstelamCorrupt = false;
    private Boolean isRecordNotFound = false;
    private String isEstelamMessage;
    private Boolean notVerified;


    public Boolean getIsEstelamCorrupt() {
        return isEstelamCorrupt;
    }

    public void setIsEstelamCorrupt(Boolean isEstelamCorrupt) {
        this.isEstelamCorrupt = isEstelamCorrupt;
    }

    public String getIsEstelamMessage() {
        return isEstelamMessage;
    }

    public void setIsEstelamMessage(String isEstelamMessage) {
        this.isEstelamMessage = isEstelamMessage;
    }

    public Boolean getIsServiceDown() {
        return isServiceDown;
    }

    public void setIsServiceDown(Boolean isServiceDown) {
        this.isServiceDown = isServiceDown;
    }

    public PersonEnquiryWTO() {
    }

    public PersonEnquiryWTO(
            String nationalId,
            String firstName,
            String lastName,
            String fatherName,
            String birthCertificateSeries,
            String birthCertificateSerial,
            String birthCertificateId,
            String solarBirthDate,
            Gender gender) {
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.birthCertificateSeries = birthCertificateSeries;
        this.birthCertificateSerial = birthCertificateSerial;
        this.birthCertificateId = birthCertificateId;
        this.solarBirthDate = solarBirthDate;
        this.gender = gender;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @JSON(include = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JSON(include = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JSON(include = false)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @JSON(include = false)
    public String getBirthCertificateSeries() {
        return birthCertificateSeries;
    }

    public void setBirthCertificateSeries(String birthCertificateSeries) {
        this.birthCertificateSeries = birthCertificateSeries;
    }

    @JSON(include = false)
    public String getBirthCertificateSerial() {
        return birthCertificateSerial;
    }

    public void setBirthCertificateSerial(String birthCertificateSerial) {
        this.birthCertificateSerial = birthCertificateSerial;
    }

    @JSON(include = false)
    public String getBirthCertificateId() {
        return birthCertificateId;
    }

    public void setBirthCertificateId(String birthCertificateId) {
        this.birthCertificateId = birthCertificateId;
    }

    @JSON(include = false)
    public String getSolarBirthDate() {
        return solarBirthDate;
    }

    public void setSolarBirthDate(String solarBirthDate) {
        this.solarBirthDate = solarBirthDate;
    }

    @JSON(include = false)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @JSON(include = false)
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    @JSON(include = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JSON(include = false)
    public byte[] getNidImage() {
        return nidImage;
    }

    public void setNidImage(byte[] nidImage) {
        this.nidImage = nidImage;
    }

    @JSON(include = false)
    public byte[] getCertImage() {
        return certImage;
    }

    public void setCertImage(byte[] certImage) {
        this.certImage = certImage;
    }

    public int getIsDead() {
        return isDead;
    }

    public void setIsDead(int isDead) {
        this.isDead = isDead;
    }

    public Boolean getIsExceptionMessage() {
        return isExceptionMessage;
    }

    public void setIsExceptionMessage(Boolean isExceptionMessage) {
        this.isExceptionMessage = isExceptionMessage;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public Boolean getIsRecordNotFound() {
        return isRecordNotFound;
    }

    public void setIsRecordNotFound(Boolean isRecordNotFound) {
        this.isRecordNotFound = isRecordNotFound;
    }


    public void setNotVerified(Boolean notVerified) {
        this.notVerified = notVerified;
    }

    public Boolean isNotVerified() {
        return notVerified;
    }
}
