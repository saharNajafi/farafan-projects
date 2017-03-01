package com.gam.nocr.ems.data.domain.vol;

import java.util.Date;

import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by hossein on 12/21/2015.
 */
public class CitizenInfoVTO{
    private String citizenFirstName;
    private String citizenNId;
    private String citizenSurname;
    private Date citizenBirthDate;
    private String citizenBirthDateSOL;
    private String citizenBirthDateLUN;
    private Gender gender;
    private String postalCode;
    private String livingProvinceName;
    private String livingCityName;
    private String religion;
    private String address;
    private String phone;
    private String mobile;
    private String birthCertiIssuancePlace;

    private String fatherName;
    private String fatherNID;
    private String fatherBirthCertID;

    private String motherName;
    private String motherNID;
    private String motherBirthCertID;

    public CitizenInfoVTO(){

    }
    
    public CitizenInfoVTO(CitizenInfoTO citizen){
    	
    }

    public String getCitizenFirstName() {
        return citizenFirstName;
    }

    public void setCitizenFirstName(String citizenFirstName) {
        this.citizenFirstName = citizenFirstName;
    }

    public String getCitizenNId() {
        return citizenNId;
    }

    public void setCitizenNId(String citizenNId) {
        this.citizenNId = citizenNId;
    }

    public String getCitizenSurname() {
        return citizenSurname;
    }

    public void setCitizenSurname(String citizenSurname) {
        this.citizenSurname = citizenSurname;
    }

    public Date getCitizenBirthDate() {
        return citizenBirthDate;
    }

    public void setCitizenBirthDate(Date citizenBirthDate) {
        this.citizenBirthDate = citizenBirthDate;
    }

    public String getCitizenBirthDateSOL() {
        return citizenBirthDateSOL;
    }

    public void setCitizenBirthDateSOL(String citizenBirthDateSOL) {
        this.citizenBirthDateSOL = citizenBirthDateSOL;
    }

    public String getCitizenBirthDateLUN() {
        return citizenBirthDateLUN;
    }

    public void setCitizenBirthDateLUN(String citizenBirthDateLUN) {
        this.citizenBirthDateLUN = citizenBirthDateLUN;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLivingProvinceName() {
        return livingProvinceName;
    }

    public void setLivingProvinceName(String livingProvinceName) {
        this.livingProvinceName = livingProvinceName;
    }

    public String getLivingCityName() {
        return livingCityName;
    }

    public void setLivingCityName(String livingCityName) {
        this.livingCityName = livingCityName;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthCertiIssuancePlace() {
        return birthCertiIssuancePlace;
    }

    public void setBirthCertiIssuancePlace(String birthCertiIssuancePlace) {
        this.birthCertiIssuancePlace = birthCertiIssuancePlace;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherNID() {
        return fatherNID;
    }

    public void setFatherNID(String fatherNID) {
        this.fatherNID = fatherNID;
    }

    public String getFatherBirthCertID() {
        return fatherBirthCertID;
    }

    public void setFatherBirthCertID(String fatherBirthCertID) {
        this.fatherBirthCertID = fatherBirthCertID;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherNID() {
        return motherNID;
    }

    public void setMotherNID(String motherNID) {
        this.motherNID = motherNID;
    }

    public String getMotherBirthCertID() {
        return motherBirthCertID;
    }

    public void setMotherBirthCertID(String motherBirthCertID) {
        this.motherBirthCertID = motherBirthCertID;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }

}
