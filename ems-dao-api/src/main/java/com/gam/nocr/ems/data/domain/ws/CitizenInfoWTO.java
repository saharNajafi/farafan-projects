package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public class CitizenInfoWTO implements Serializable {

    private String firstNameFA;
    private String sureNameFA;
    private String nationalId;
    private Date birthDate;
    private String birthDateHijri;
    private String gender;
    private Long religionId;
    private String birthCertSerial;
    private String userCityType;
    private Long livingPrvId;
    private Long livingCityId;
    private Long livingStateId;
    private Long livingVillageId;
    private Long livingSectorId;
    private String address;
    private String postCode;
    private String phone;
    private String mobile;
    private String fatherFirstNameFA;
    private String fatherFatherName;
    private String fatherSureName;
    private String fatherNationalId;
    private String motherFirstNameFA;
    private String motherSureName;
    private String motherNationalId;
    private Timestamp motherBirthDate;

    public String getFirstNameFA() {
        return firstNameFA;
    }

    public void setFirstNameFA(String firstNameFA) {
        this.firstNameFA = firstNameFA;
    }

    public String getSureNameFA() {
        return sureNameFA;
    }

    public void setSureNameFA(String sureNameFA) {
        this.sureNameFA = sureNameFA;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDateHijri() {
        return birthDateHijri;
    }

    public void setBirthDateHijri(String birthDateHijri) {
        this.birthDateHijri = birthDateHijri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getReligionId() {
        return religionId;
    }

    public void setReligionId(Long religionId) {
        this.religionId = religionId;
    }

    public String getBirthCertSerial() {
        return birthCertSerial;
    }

    public void setBirthCertSerial(String birthCertSerial) {
        this.birthCertSerial = birthCertSerial;
    }

    public String getUserCityType() {
        return userCityType;
    }

    public void setUserCityType(String userCityType) {
        this.userCityType = userCityType;
    }

    public Long getLivingPrvId() {
        return livingPrvId;
    }

    public void setLivingPrvId(Long livingPrvId) {
        this.livingPrvId = livingPrvId;
    }

    public Long getLivingCityId() {
        return livingCityId;
    }

    public void setLivingCityId(Long livingCityId) {
        this.livingCityId = livingCityId;
    }

    public Long getLivingStateId() {
        return livingStateId;
    }

    public void setLivingStateId(Long livingStateId) {
        this.livingStateId = livingStateId;
    }

    public Long getLivingVillageId() {
        return livingVillageId;
    }

    public void setLivingVillageId(Long livingVillageId) {
        this.livingVillageId = livingVillageId;
    }

    public Long getLivingSectorId() {
        return livingSectorId;
    }

    public void setLivingSectorId(Long livingSectorId) {
        this.livingSectorId = livingSectorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    public String getFatherFirstNameFA() {
        return fatherFirstNameFA;
    }

    public void setFatherFirstNameFA(String fatherFirstNameFA) {
        this.fatherFirstNameFA = fatherFirstNameFA;
    }

    public String getFatherFatherName() {
        return fatherFatherName;
    }

    public void setFatherFatherName(String fatherFatherName) {
        this.fatherFatherName = fatherFatherName;
    }

    public String getFatherSureName() {
        return fatherSureName;
    }

    public void setFatherSureName(String fatherSureName) {
        this.fatherSureName = fatherSureName;
    }

    public String getFatherNationalId() {
        return fatherNationalId;
    }

    public void setFatherNationalId(String fatherNationalId) {
        this.fatherNationalId = fatherNationalId;
    }

    public String getMotherFirstNameFA() {
        return motherFirstNameFA;
    }

    public void setMotherFirstNameFA(String motherFirstNameFA) {
        this.motherFirstNameFA = motherFirstNameFA;
    }

    public String getMotherSureName() {
        return motherSureName;
    }

    public void setMotherSureName(String motherSureName) {
        this.motherSureName = motherSureName;
    }

    public String getMotherNationalId() {
        return motherNationalId;
    }

    public void setMotherNationalId(String motherNationalId) {
        this.motherNationalId = motherNationalId;
    }

    public Timestamp getMotherBirthDate() {
        return motherBirthDate;
    }

    public void setMotherBirthDate(Timestamp motherBirthDate) {
        this.motherBirthDate = motherBirthDate;
    }
}
