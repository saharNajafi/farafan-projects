package com.gam.nocr.ems.data.domain.ws;

import java.sql.Timestamp;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.util.EmsUtil;

import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class CitizenWTO {
    private Long requestId;
    private Long id;
    private String firstNameFA;
    private String firstNameEN;
    private String sureNameFA;
    private String sureNameEN;
    private String birthCertId;
    private Timestamp birthDate;
    private String birthDateHijri;
    private String nationalId;
    private String gender;
    private Long type;
    private Long religionId;
    private String postCode;
    private String email;
    private String birthCertPrvName;
    private Long birthCertPrvId;
    // geo-parts
    private String livingPrvName;
    private Long livingPrvId;
    //
    private String livingCityName;
    private Long livingCityId;
    //
    private Long livingStateId;
    private String livingStateName;
    //
    private Long livingVillageId;
    private String livingVillageName;
    //
    private Long livingSectorId;
    private String livingSectorName;
    //
    private String userCityType;
    //
    private String birthCertSerial;
    private String fatherFirstNameFA;
    private String fatherFirstNameEN;
    private String fatherFatherName;
    private String fatherSureName;
    private String fatherNationalId;
    private Timestamp fatherBirthDate;
    private String fatherBirthCertId;
    private String fatherBirthCertSeries;
    private String motherFirstNameFA;
    private String motherSureName;
    private String motherNationalId;
    private Timestamp motherBirthDate;
    private String motherFatherName;
    private String motherBirthCertId;
    private String motherBirthCertSeries;
    private String address;
    private Timestamp enrolledDate;
    private Long state;
    private String requestedAction;
    private Timestamp reservationDate;
    private String trackingId;
    private String phone;
    private String mobile;
    private Long originalOfficeId;
    private String originalOfficeName;
    private Timestamp attendDate;

    private SpouseWTO[] spouses;
    private ChildrenWTO[] children;

    private String cmsBatchID;
    private String hasVipImage;

    private Long isPaid;
    private Timestamp paidDate;

    private static final Logger logger = BaseLog.getLogger(EmsUtil.class);
    private Boolean paymentStatus = Boolean.FALSE;

    public CitizenWTO() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JSON(include = false)
    public String getFirstNameFA() {
        return firstNameFA;
    }

    public void setFirstNameFA(String firstNameFA) {
        this.firstNameFA = firstNameFA;
    }

    @JSON(include = false)
    public String getFirstNameEN() {
        return firstNameEN;
    }

    public void setFirstNameEN(String firstNameEN) {
        this.firstNameEN = firstNameEN;
    }

    @JSON(include = false)
    public String getSureNameFA() {
        return sureNameFA;
    }

    public void setSureNameFA(String sureNameFA) {
        this.sureNameFA = sureNameFA;
    }

    @JSON(include = false)
    public String getSureNameEN() {
        return sureNameEN;
    }

    public void setSureNameEN(String sureNameEN) {
        this.sureNameEN = sureNameEN;
    }

    @JSON(include = false)
    public String getBirthCertId() {
        return birthCertId;
    }

    public void setBirthCertId(String birthCertId) {
        this.birthCertId = birthCertId;
    }

    @JSON(include = false)
    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    @JSON(include = false)
    public String getBirthDateHijri() {
        return birthDateHijri;
    }

    public void setBirthDateHijri(String birthDateHijri) {
        this.birthDateHijri = birthDateHijri;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @JSON(include = false)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JSON(include = false)
    public Long getType() {
        return type;
    }

    public void setStringType(String type) {
        try {
            this.type = CardRequestType.toLong(CardRequestType.valueOf(type));
        } catch (Exception ex) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, ex);
        }
    }

    public void setType(Long type) {
        this.type = type;
    }

    @JSON(include = false)
    public Long getReligionId() {
        return religionId;
    }

    public void setReligionId(Long religionId) {
        this.religionId = religionId;
    }

    @JSON(include = false)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @JSON(include = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JSON(include = false)
    public String getBirthCertPrvName() {
        return birthCertPrvName;
    }

    public void setBirthCertPrvName(String birthCertPrvName) {
        this.birthCertPrvName = birthCertPrvName;
    }

    @JSON(include = false)
    public Long getBirthCertPrvId() {
        return birthCertPrvId;
    }

    public void setBirthCertPrvId(Long birthCertPrvId) {
        this.birthCertPrvId = birthCertPrvId;
    }

    @JSON(include = false)
    public String getLivingPrvName() {
        return livingPrvName;
    }

    public void setLivingPrvName(String livingPrvName) {
        this.livingPrvName = livingPrvName;
    }

    @JSON(include = false)
    public Long getLivingPrvId() {
        return livingPrvId;
    }

    public void setLivingPrvId(Long livingPrvId) {
        this.livingPrvId = livingPrvId;
    }

    @JSON(include = false)
    public String getLivingCityName() {
        return livingCityName;
    }

    public void setLivingCityName(String livingCityName) {
        this.livingCityName = livingCityName;
    }

    @JSON(include = false)
    public Long getLivingStateId() {
        return livingStateId;
    }

    public void setLivingStateId(Long livingStateId) {
        this.livingStateId = livingStateId;
    }

    @JSON(include = false)
    public String getLivingStateName() {
        return livingStateName;
    }

    public void setLivingStateName(String livingStateName) {
        this.livingStateName = livingStateName;
    }

    @JSON(include = false)
    public Long getLivingCityId() {
        return livingCityId;
    }

    public void setLivingCityId(Long livingCityId) {
        this.livingCityId = livingCityId;
    }

    @JSON(include = false)
    public String getBirthCertSerial() {
        return birthCertSerial;
    }

    public void setBirthCertSerial(String birthCertSerial) {
        this.birthCertSerial = birthCertSerial;
    }

    @JSON(include = false)
    public String getFatherFirstNameFA() {
        return fatherFirstNameFA;
    }

    public void setFatherFirstNameFA(String fatherFirstNameFA) {
        this.fatherFirstNameFA = fatherFirstNameFA;
    }

    @JSON(include = false)
    public String getFatherFirstNameEN() {
        return fatherFirstNameEN;
    }

    public void setFatherFirstNameEN(String fatherFirstNameEN) {
        this.fatherFirstNameEN = fatherFirstNameEN;
    }

    @JSON(include = false)
    public String getFatherFatherName() {
        return fatherFatherName;
    }

    public void setFatherFatherName(String fatherFatherName) {
        this.fatherFatherName = fatherFatherName;
    }

    @JSON(include = false)
    public String getFatherSureName() {
        return fatherSureName;
    }

    public void setFatherSureName(String fatherSureName) {
        this.fatherSureName = fatherSureName;
    }

    @JSON(include = false)
    public String getFatherNationalId() {
        return fatherNationalId;
    }

    public void setFatherNationalId(String fatherNationalId) {
        this.fatherNationalId = fatherNationalId;
    }

    @JSON(include = false)
    public Timestamp getFatherBirthDate() {
        return fatherBirthDate;
    }

    public void setFatherBirthDate(Timestamp fatherBirthDate) {
        this.fatherBirthDate = fatherBirthDate;
    }

    @JSON(include = false)
    public String getFatherBirthCertId() {
        return fatherBirthCertId;
    }

    public void setFatherBirthCertId(String fatherBirthCertId) {
        this.fatherBirthCertId = fatherBirthCertId;
    }

    @JSON(include = false)
    public String getFatherBirthCertSeries() {
        return fatherBirthCertSeries;
    }

    public void setFatherBirthCertSeries(String fatherBirthCertSeries) {
        this.fatherBirthCertSeries = fatherBirthCertSeries;
    }

    @JSON(include = false)
    public String getMotherFirstNameFA() {
        return motherFirstNameFA;
    }

    public void setMotherFirstNameFA(String motherFirstNameFA) {
        this.motherFirstNameFA = motherFirstNameFA;
    }

    @JSON(include = false)
    public String getMotherSureName() {
        return motherSureName;
    }

    public void setMotherSureName(String motherSureName) {
        this.motherSureName = motherSureName;
    }

    @JSON(include = false)
    public String getMotherNationalId() {
        return motherNationalId;
    }

    public void setMotherNationalId(String motherNationalId) {
        this.motherNationalId = motherNationalId;
    }

    @JSON(include = false)
    public Timestamp getMotherBirthDate() {
        return motherBirthDate;
    }

    public void setMotherBirthDate(Timestamp motherBirthDate) {
        this.motherBirthDate = motherBirthDate;
    }

    @JSON(include = false)
    public String getMotherFatherName() {
        return motherFatherName;
    }

    public void setMotherFatherName(String motherFatherName) {
        this.motherFatherName = motherFatherName;
    }

    @JSON(include = false)
    public String getMotherBirthCertId() {
        return motherBirthCertId;
    }

    public void setMotherBirthCertId(String motherBirthCertId) {
        this.motherBirthCertId = motherBirthCertId;
    }

    @JSON(include = false)
    public String getMotherBirthCertSeries() {
        return motherBirthCertSeries;
    }

    public void setMotherBirthCertSeries(String motherBirthCertSeries) {
        this.motherBirthCertSeries = motherBirthCertSeries;
    }

    @JSON(include = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JSON(include = false)
    public Timestamp getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Timestamp enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    @JSON(include = false)
    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    @JSON(include = false)
    public String getRequestedAction() {
        return requestedAction;
    }

    public void setRequestedAction(String requestedAction) {
        this.requestedAction = requestedAction;
    }

    @JSON(include = false)
    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    @JSON(include = false)
    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @JSON(include = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JSON(include = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOriginalOfficeId() {
        return originalOfficeId;
    }

    public void setOriginalOfficeId(Long originalOfficeId) {
        this.originalOfficeId = originalOfficeId;
    }

    public String getOriginalOfficeName() {
        return originalOfficeName;
    }

    public void setOriginalOfficeName(String originalOfficeName) {
        this.originalOfficeName = originalOfficeName;
    }

    public SpouseWTO[] getSpouses() {
        return spouses == null ? spouses : spouses.clone();
    }

    public void setSpouses(SpouseWTO[] spouses) {
        this.spouses = spouses;
    }

    public ChildrenWTO[] getChildren() {
        return children == null ? children : children.clone();
    }

    public void setChildren(ChildrenWTO[] children) {
        this.children = children;
    }

    public String getCmsBatchID() {
        return cmsBatchID;
    }

    public void setCmsBatchID(String cmsBatchID) {
        this.cmsBatchID = cmsBatchID;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }

    public String getHasVipImage() {
        return hasVipImage;
    }

    public void setHasVipImage(String hasVipImage) {
        this.hasVipImage = hasVipImage;
    }

    public Long getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Long isPaid) {
        this.isPaid = isPaid;
    }

    public Timestamp getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Timestamp paidDate) {
        this.paidDate = paidDate;
    }

    public Long getLivingVillageId() {
        return livingVillageId;
    }

    public void setLivingVillageId(Long livingVillageId) {
        this.livingVillageId = livingVillageId;
    }

    public String getLivingVillageName() {
        return livingVillageName;
    }

    public void setLivingVillageName(String livingVillageName) {
        this.livingVillageName = livingVillageName;
    }

    public Long getLivingSectorId() {
        return livingSectorId;
    }

    public void setLivingSectorId(Long livingSectorId) {
        this.livingSectorId = livingSectorId;
    }

    public String getLivingSectorName() {
        return livingSectorName;
    }

    public void setLivingSectorName(String livingSectorName) {
        this.livingSectorName = livingSectorName;
    }

    public String getUserCityType() {
        return userCityType;
    }

    public void setUserCityType(String userCityType) {
        this.userCityType = userCityType;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public Timestamp getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(Timestamp attendDate) {
        this.attendDate = attendDate;
    }
}
