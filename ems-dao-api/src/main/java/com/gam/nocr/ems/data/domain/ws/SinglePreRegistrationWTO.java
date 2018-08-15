package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.GenderEnum;
import com.gam.nocr.ems.data.enums.RegistrationStateEnum;
import com.gam.nocr.ems.data.enums.ReligionEnum;
import com.gam.nocr.ems.util.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/14/18.
 */
public class SinglePreRegistrationWTO extends SecureWTO implements Serializable {
    private String nationalId;
    private int certSerialNo;
    private int birthDateSolar;
    private String birthDateLunar;
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date birthDateGregorian;
    private  HealthStatusWTO healthStatusWTO;
    private GenderEnum gender;
    private String cellphoneNumber;
    private RegistrationPaymentWTO registrationPaymentWTO;
    private String motherName;
    private String trackingId;
    private ReligionEnum religion;
    private RegistrationStateEnum state;
    private String createdOn;
    private String origin;
    private Long enrollmentOfficeId;

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public int getCertSerialNo() {
        return certSerialNo;
    }

    public void setCertSerialNo(int certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    public int getBirthDateSolar() {
        return birthDateSolar;
    }

    public void setBirthDateSolar(int birthDateSolar) {
        this.birthDateSolar = birthDateSolar;
    }

    public String getBirthDateLunar() {
        return birthDateLunar;
    }

    public void setBirthDateLunar(String birthDateLunar) {
        this.birthDateLunar = birthDateLunar;
    }

    public Date getBirthDateGregorian() {
        return birthDateGregorian;
    }

    public void setBirthDateGregorian(Date birthDateGregorian) {
        this.birthDateGregorian = birthDateGregorian;
    }

    public HealthStatusWTO getHealthStatusWTO() {
        return healthStatusWTO;
    }

    public void setHealthStatusWTO(HealthStatusWTO healthStatusWTO) {
        this.healthStatusWTO = healthStatusWTO;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public RegistrationPaymentWTO getRegistrationPaymentWTO() {
        return registrationPaymentWTO;
    }

    public void setRegistrationPaymentWTO(RegistrationPaymentWTO registrationPaymentWTO) {
        this.registrationPaymentWTO = registrationPaymentWTO;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public ReligionEnum getReligion() {
        return religion;
    }

    public void setReligion(ReligionEnum religion) {
        this.religion = religion;
    }

    public RegistrationStateEnum getState() {
        return state;
    }

    public void setState(RegistrationStateEnum state) {
        this.state = state;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }
}
