package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.util.Date;

public class PrintRegistrationReceiptVTO extends ExtEntityTO {

    private String citizenFirstName;
    private String citizenSurname;
    private String fatherName;
    private String citizenNId;
    private String birthCertId;
    private String citizenBirthDate;
    private Date reservationDate;
    private String trackingId;
    private String userFirstName;
    private String userLastName;
    private Date receiptDate;

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

    public String getCitizenNId() {
        return citizenNId;
    }

    public void setCitizenNId(String citizenNId) {
        this.citizenNId = citizenNId;
    }

    public String getBirthCertId() {
        return birthCertId;
    }

    public void setBirthCertId(String birthCertId) {
        this.birthCertId = birthCertId;
    }

    public String getCitizenBirthDate() {
        return citizenBirthDate;
    }

    public void setCitizenBirthDate(String citizenBirthDate) {
        this.citizenBirthDate = citizenBirthDate;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }
}
