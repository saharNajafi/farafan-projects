package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.io.Serializable;

/**
 * Created by safiary on 6/22/19.
 */
public class CardRequestReceiptVTO extends ExtEntityTO implements Serializable {

    private String citizenFirstName;
    private String citizenSurname;
    private String fatherName;
    private String nationalID;
    private String birthCertificateId;
    private String birthDateSolar;
    private String enrolledDate;
    private String trackingID;
    private String receiptDate;
    private String userFirstName;
    private String userLastName;
    private String enrollmentName;

    public CardRequestReceiptVTO() {
    }

    public String getCitizenFirstName() {
        return citizenFirstName;
    }

    public void setCitizenFirstName(String citizenFirstName) {
        this.citizenFirstName = citizenFirstName;
    }

    public String getCitizenSurname() {
        return citizenSurname;
    }

    public void setCitizenSurname(String citizenSurname) {
        this.citizenSurname = citizenSurname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getBirthCertificateId() {
        return birthCertificateId;
    }

    public void setBirthCertificateId(String birthCertificateId) {
        this.birthCertificateId = birthCertificateId;
    }

    public String getBirthDateSolar() {
        return birthDateSolar;
    }

    public void setBirthDateSolar(String birthDateSolar) {
        this.birthDateSolar = birthDateSolar;
    }

    public String getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(String enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getEnrollmentName() {
        return enrollmentName;
    }

    public void setEnrollmentName(String enrollmentName) {
        this.enrollmentName = enrollmentName;
    }
}
