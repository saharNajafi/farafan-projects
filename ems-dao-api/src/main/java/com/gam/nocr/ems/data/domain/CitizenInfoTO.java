package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.AFISState;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CITIZEN_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CITIZEN_INFO", allocationSize = 1)
@XmlRootElement(name = "CitizenInfo")
public class CitizenInfoTO extends ExtEntityTO implements JSONable {

    private CitizenTO citizen;
    private String firstNameEnglish;
    private String surnameEnglish;
    private Date birthDateGregorian;
    private String birthDateSolar;
    private String birthDateLunar;
    private String fatherFirstNamePersian;
    private String fatherFirstNameEnglish;
    private Gender gender;
    private LocationTO birthCertificateIssuancePlaceProvince;
    private String fatherNationalID;
    private String motherFirstNamePersian;
    private String motherNationalID;
    private Integer identityChanged;
    private String email;
    private Date fatherBirthDateSolar;
    private Date motherBirthDateSolar;
    private ReligionTO religion;
    private String postcode;
    private String birthCertificateId;
    private String birthCertificateSeries;
    private LocationTO living;
    private LocationTO livingCity;
    private String fatherFatherName;
    private String fatherSurname;
    private String fatherBirthCertificateId;
    private String fatherBirthCertificateSeries;
    private String motherSurname;
    private String motherFatherName;
    private String motherBirthCertificateId;
    private String motherBirthCertificateSeries;
    private String address;
    private String phone;
    private String mobile;
    private AFISState afisState;
    private String birthCertificateIssuancePlace;
    private Integer faceDisabilityStatus;
//    private int identityChange;

    private List<SpouseTO> spouses = new ArrayList<SpouseTO>(0);
    private List<ChildTO> children = new ArrayList<ChildTO>(0);
    private List<BiometricTO> biometrics = new ArrayList<BiometricTO>(0);
    private List<DocumentTO> documents = new ArrayList<DocumentTO>(0);

    public CitizenInfoTO() {
    }

    public CitizenInfoTO(LocationTO birthCertificateIssuancePlaceProvince, Date fatherBirthDateSolar,
                         Date motherBirthDateSolar, ReligionTO religion, String firstNameEnglish,
                         String surnameEnglish, String birthCertificateSerial, LocationTO living,
                         LocationTO livingCity) {
        this.birthCertificateIssuancePlaceProvince = birthCertificateIssuancePlaceProvince;
        this.fatherBirthDateSolar = fatherBirthDateSolar;
        this.motherBirthDateSolar = motherBirthDateSolar;
        this.religion = religion;
        this.firstNameEnglish = firstNameEnglish;
        this.surnameEnglish = surnameEnglish;
        this.birthCertificateSeries = birthCertificateSerial;
        this.living = living;
        this.livingCity = livingCity;
    }

    @Id
    public Long getId() {
        return super.getId();
    }


    @MapsId
    @OneToOne
    @JoinColumn(name = "CZI_ID")
    @JSON(include = false)
    public CitizenTO getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenTO citizen) {
        this.citizen = citizen;
    }

    @Column(name = "CZI_FIRST_NAME_EN", length = 255, nullable = false)
    @XmlElement(name = "FirstNameEnglish")
    public String getFirstNameEnglish() {
        return firstNameEnglish;
    }

    public void setFirstNameEnglish(String firstNameEnglish) {
        this.firstNameEnglish = firstNameEnglish;
    }

    @Column(name = "CZI_SURNAME_EN", length = 255, nullable = false)
    @XmlElement(name = "SurnameEnglish")
    public String getSurnameEnglish() {
        return surnameEnglish;
    }

    public void setSurnameEnglish(String surnameEnglish) {
        this.surnameEnglish = surnameEnglish;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CZI_BIRTH_DATE_GREG", nullable = false)
    @JSON(include = false)
    public Date getBirthDateGregorian() {
        return birthDateGregorian;
    }

    public void setBirthDateGregorian(Date birthDateGregorian) {
        this.birthDateGregorian = birthDateGregorian;
    }

    @Column(name = "CZI_BIRTH_DATE_SOL", length = 255, nullable = false)
    @XmlElement(name = "BirthDateJalali")
    @JSON(include = false)
    public String getBirthDateSolar() {
        return birthDateSolar;
    }

    public void setBirthDateSolar(String birthDateSolar) {
        this.birthDateSolar = birthDateSolar;
    }

    @Column(name = "CZI_BIRTH_DATE_LUN", length = 255, nullable = false)
    @XmlElement(name = "BirthDateHijri")
    @JSON(include = false)
    public String getBirthDateLunar() {
        return birthDateLunar;
    }

    public void setBirthDateLunar(String birthDateLunar) {
        this.birthDateLunar = birthDateLunar;
    }

    @Column(name = "CZI_FATHER_FIRST_NAME_FA", length = 42)
    @XmlElement(name = "FatherFirstNamePersian")
    @JSON(include = false)
    public String getFatherFirstNamePersian() {
        return fatherFirstNamePersian;
    }

    public void setFatherFirstNamePersian(String fatherFirstNamePersian) {
        this.fatherFirstNamePersian = fatherFirstNamePersian;
    }

    @Column(name = "CZI_FATHER_FIRST_NAME_EN", length = 25)
    @JSON(include = false)
    public String getFatherFirstNameEnglish() {
        return fatherFirstNameEnglish;
    }

    public void setFatherFirstNameEnglish(String fatherFirstNameEnglish) {
        this.fatherFirstNameEnglish = fatherFirstNameEnglish;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CZI_GENDER")
    @XmlElement(name = "Sex")
    @JSON(include = false)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @ManyToOne
    @JoinColumn(name = "CZI_BIRTH_CERT_PRV_ID")
    @XmlElement(name = "BirthCertificateIssuancePlacePersian")
    @JSON(include = false)
    public LocationTO getBirthCertificateIssuancePlaceProvince() {
        return birthCertificateIssuancePlaceProvince;
    }

    public void setBirthCertificateIssuancePlaceProvince(LocationTO birthCertificateIssuancePlaceProvince) {
        this.birthCertificateIssuancePlaceProvince = birthCertificateIssuancePlaceProvince;
    }

    @Size(min = 10, max = 10)
    @Column(name = "CZI_FATHER_NATIONAL_ID")
    @JSON(include = false)
    public String getFatherNationalID() {
        return fatherNationalID;
    }

    public void setFatherNationalID(String fatherNationalID) {
        this.fatherNationalID = fatherNationalID;
    }

    @Column(name = "CZI_MOTHER_FIRST_NAME_FA", length = 42)
    @XmlElement(name = "MotherFirstNamePersian")
    @JSON(include = false)
    public String getMotherFirstNamePersian() {
        return motherFirstNamePersian;
    }

    public void setMotherFirstNamePersian(String motherFirstNamePersian) {
        this.motherFirstNamePersian = motherFirstNamePersian;
    }

    @Size(min = 10, max = 10)
    @Column(name = "CZI_MOTHER_NATIONAL_ID")
    @JSON(include = false)
    public String getMotherNationalID() {
        return motherNationalID;
    }

    public void setMotherNationalID(String motherNationalID) {
        this.motherNationalID = motherNationalID;
    }

    @Column(name = "CZI_IDENTITY_CHANGED")
    @XmlElement(name = "IdentityChange")
    @JSON(include = false)
    public Integer getIdentityChanged() {
        return identityChanged;
    }

    public void setIdentityChanged(Integer identityChanged) {
        this.identityChanged = identityChanged;
    }

    @Column(name = "CZI_EMAIL", length = 255)
    @XmlElement(name = "Email")
    @JSON(include = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CZI_FATHER_BIRTH_DATE_SOL", nullable = false)
    @JSON(include = false)
    public Date getFatherBirthDateSolar() {
        return fatherBirthDateSolar;
    }

    public void setFatherBirthDateSolar(Date fatherBirthDateSolar) {
        this.fatherBirthDateSolar = fatherBirthDateSolar;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CZI_MOTHER_BIRTH_DATE_SOL", nullable = false)
    @JSON(include = false)
    public Date getMotherBirthDateSolar() {
        return motherBirthDateSolar;
    }

    public void setMotherBirthDateSolar(Date motherBirthDateSolar) {
        this.motherBirthDateSolar = motherBirthDateSolar;
    }

    @ManyToOne
    @JoinColumn(name = "CZI_RELIGION_ID", nullable = false)
    @JSON(include = false)
    public ReligionTO getReligion() {
        return religion;
    }

    public void setReligion(ReligionTO religion) {
        this.religion = religion;
    }

    @Size(max = 10, min = 10)
    @Column(name = "CZI_POSTCODE")
    @JSON(include = false)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "CZI_BIRTH_CERT_ID", length = 20)
    @JSON(include = false)
    public String getBirthCertificateId() {
        return birthCertificateId;
    }

    public void setBirthCertificateId(String birthCertificateId) {
        this.birthCertificateId = birthCertificateId;
    }

    @Column(name = "CZI_BIRTH_CERT_SERIES", nullable = false, length = 10)
    @JSON(include = false)
    public String getBirthCertificateSeries() {
        return birthCertificateSeries;
    }

    public void setBirthCertificateSeries(String birthCertificateSeries) {
        this.birthCertificateSeries = birthCertificateSeries;
    }

    @ManyToOne
    @JoinColumn(name = "CZI_LIVING_PRV_ID")
    @JSON(include = false)
    public LocationTO getLiving() {
        return living;
    }

    public void setLiving(LocationTO living) {
        this.living = living;
    }

    @ManyToOne
    @JoinColumn(name = "CZI_LIVING_CITY_ID")
    @JSON(include = false)
    public LocationTO getLivingCity() {
        return livingCity;
    }

    public void setLivingCity(LocationTO livingCity) {
        this.livingCity = livingCity;
    }

    @Column(name = "CZI_FATHER_FATHER_NAME", length = 84)
    @JSON(include = false)
    public String getFatherFatherName() {
        return fatherFatherName;
    }

    public void setFatherFatherName(String fatherFatherName) {
        this.fatherFatherName = fatherFatherName;
    }

    @Column(name = "CZI_FATHER_SURE_NAME", length = 84)
    @JSON(include = false)
    public String getFatherSurname() {
        return fatherSurname;
    }

    public void setFatherSurname(String fatherSurname) {
        this.fatherSurname = fatherSurname;
    }

    @Column(name = "CZI_FATHER_BIRTH_CERT_ID", length = 20)
    @JSON(include = false)
    public String getFatherBirthCertificateId() {
        return fatherBirthCertificateId;
    }

    public void setFatherBirthCertificateId(String fatherBirthCertificateId) {
        this.fatherBirthCertificateId = fatherBirthCertificateId;
    }

    @Column(name = "CZI_FATHER_BIRTH_CERT_SERIES", length = 10)
    @JSON(include = false)
    public String getFatherBirthCertificateSeries() {
        return fatherBirthCertificateSeries;
    }

    public void setFatherBirthCertificateSeries(String fatherBirthCertificateSeries) {
        this.fatherBirthCertificateSeries = fatherBirthCertificateSeries;
    }

    @Column(name = "CZI_MOTHER_SURENAME", length = 84)
    @JSON(include = false)
    public String getMotherSurname() {
        return motherSurname;
    }

    public void setMotherSurname(String motherSurname) {
        this.motherSurname = motherSurname;
    }

    @Column(name = "CZI_MOTHER_FATHER_NAME", length = 84)
    @JSON(include = false)
    public String getMotherFatherName() {
        return motherFatherName;
    }

    public void setMotherFatherName(String motherFatherName) {
        this.motherFatherName = motherFatherName;
    }

    @Column(name = "CZI_MOTHER_BIRTH_CERT_ID", length = 20)
    @JSON(include = false)
    public String getMotherBirthCertificateId() {
        return motherBirthCertificateId;
    }

    public void setMotherBirthCertificateId(String motherBirthCertificateId) {
        this.motherBirthCertificateId = motherBirthCertificateId;
    }

    @Column(name = "CZI_MOTHER_BIRTH_CERT_SERIES", length = 10)
    @JSON(include = false)
    public String getMotherBirthCertificateSeries() {
        return motherBirthCertificateSeries;
    }

    public void setMotherBirthCertificateSeries(String motherBirthCertificateSeries) {
        this.motherBirthCertificateSeries = motherBirthCertificateSeries;
    }

    @Column(name = "CZI_ADDRESS", length = 300)
    @JSON(include = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CZI_PHONE", length = 20)
    @JSON(include = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "CZI_MOBILE", length = 20)
    @JSON(include = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CZI_AFIS_STATE")
    @JSON(include = false)
    public AFISState getAfisState() {
        return afisState;
    }

    public void setAfisState(AFISState afisState) {
        this.afisState = afisState;
    }

    @Column(name = "CZI_BIRTH_CERT_ISS_PLACE", length = 40)
    @JSON(include = false)
    public String getBirthCertificateIssuancePlace() {
        return birthCertificateIssuancePlace;
    }

    public void setBirthCertificateIssuancePlace(String birthCertificateIssuancePlace) {
        this.birthCertificateIssuancePlace = birthCertificateIssuancePlace;
    }

    @OneToMany(mappedBy = "citizenInfo", cascade = {CascadeType.PERSIST})
    public List<SpouseTO> getSpouses() {
        return spouses;
    }

    public void setSpouses(List<SpouseTO> spouses) {
        this.spouses = spouses;
    }

    @OneToMany(mappedBy = "citizenInfo", cascade = {CascadeType.PERSIST})
    public List<ChildTO> getChildren() {
        return children;
    }

    public void setChildren(List<ChildTO> children) {
        this.children = children;
    }

    @OneToMany(mappedBy = "citizenInfo", cascade = {CascadeType.PERSIST})
    public List<BiometricTO> getBiometrics() {
        return biometrics;
    }

    public void setBiometrics(List<BiometricTO> biometrics) {
        this.biometrics = biometrics;
    }

    @OneToMany(mappedBy = "citizenInfo", cascade = {CascadeType.PERSIST})
    public List<DocumentTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentTO> documents) {
        this.documents = documents;
    }


    @Column(name = "CZI_FACE_DISABILITY_STATUS", columnDefinition = "NUMBER(1,0)")
    public Integer getFaceDisabilityStatus() {
        return faceDisabilityStatus;
    }

    public void setFaceDisabilityStatus(Integer faceDisabilityStatus) {
        this.faceDisabilityStatus = faceDisabilityStatus;
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
        if (citizen == null) {
            jsonObject += ", citizenId:" + citizen;
        } else {
            jsonObject += ", citizenId:" + citizen.getId() +
                    ", citizenReduplicate = " + citizen.getReduplicate();
        }
        jsonObject += "}";

        return jsonObject;
    }
}