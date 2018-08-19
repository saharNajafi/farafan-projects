package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.GenderEnum;
import com.gam.nocr.ems.data.enums.RegistrationStateEnum;
import com.gam.nocr.ems.data.enums.ReligionEnum;
import com.gam.nocr.ems.data.enums.YesNoEnum;
import com.gam.nocr.ems.util.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sahar on 10/15/17.
 */
public class PreRegistrationWTO implements Serializable {

    private long id;
    private long nationalId;
    private int certSerialNo;
    private int birthDateSolar;
    private String birthDateLunar;
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date birthDateGregorian;
    private YesNoEnum pupilIsVisible;
    private YesNoEnum climbingStairsAbility;
    private YesNoEnum abilityToGo;
    private YesNoEnum hasTwoFingersScanable;
    private GenderEnum gender;
    private OfficeAppointmentWTO officeAppointment;
    private RegistrationPaymentWTO registrationPayment;
    private String cellphoneNumber;
    private String motherName;
    private String trackingId;
    private ReligionEnum religion;
    private RegistrationStateEnum state;
    private String createdOn;
    private String origin;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNationalId() {
        return nationalId;
    }

    public void setNationalId(long nationalId) {
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

    public YesNoEnum getPupilIsVisible() {
        return pupilIsVisible;
    }

    public void setPupilIsVisible(YesNoEnum pupilIsVisible) {
        this.pupilIsVisible = pupilIsVisible;
    }

    public YesNoEnum getClimbingStairsAbility() {
        return climbingStairsAbility;
    }

    public void setClimbingStairsAbility(YesNoEnum climbingStairsAbility) {
        this.climbingStairsAbility = climbingStairsAbility;
    }

    public YesNoEnum getAbilityToGo() {
        return abilityToGo;
    }

    public void setAbilityToGo(YesNoEnum abilityToGo) {
        this.abilityToGo = abilityToGo;
    }

    public YesNoEnum getHasTwoFingersScanable() {
        return hasTwoFingersScanable;
    }

    public void setHasTwoFingersScanable(YesNoEnum hasTwoFingersScanable) {
        this.hasTwoFingersScanable = hasTwoFingersScanable;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
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

    public OfficeAppointmentWTO getOfficeAppointment() {
        return officeAppointment;
    }

    public void setOfficeAppointment(OfficeAppointmentWTO officeAppointment) {
        this.officeAppointment = officeAppointment;
    }

    public RegistrationPaymentWTO getRegistrationPayment() {
        return registrationPayment;
    }

    public void setRegistrationPayment(RegistrationPaymentWTO registrationPayment) {
        this.registrationPayment = registrationPayment;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
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
}
