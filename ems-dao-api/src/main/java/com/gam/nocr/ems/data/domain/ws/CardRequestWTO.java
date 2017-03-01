package com.gam.nocr.ems.data.domain.ws;

import java.util.Date;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CardRequestWTO {

    private Long id;
    private Long citizenId;
    private String firstNamePersian;
    private String surnamePersian;
    private String nationalID;
    private String birthCertificateSerial;
    private String cardRequestState;
    private Date enrolledDate;
    private String cardRequestType;
    private String trackingID;
    private Long enrollmentOfficeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public String getFirstNamePersian() {
        return firstNamePersian;
    }

    public void setFirstNamePersian(String firstNamePersian) {
        this.firstNamePersian = firstNamePersian;
    }

    public String getSurnamePersian() {
        return surnamePersian;
    }

    public void setSurnamePersian(String surnamePersian) {
        this.surnamePersian = surnamePersian;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getBirthCertificateSerial() {
        return birthCertificateSerial;
    }

    public void setBirthCertificateSerial(String birthCertificateSerial) {
        this.birthCertificateSerial = birthCertificateSerial;
    }

    public String getCardRequestState() {
        return cardRequestState;
    }

    public void setCardRequestState(String cardRequestState) {
        this.cardRequestState = cardRequestState;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public String getCardRequestType() {
        return cardRequestType;
    }

    public void setCardRequestType(String cardRequestType) {
        this.cardRequestType = cardRequestType;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }
}
