package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Entity
@Table(name = "EMST_IMS_CITIZEN_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_IMS_CITIZEN_INFO")
public class IMSCitizenInfoTO extends ExtEntityTO {
	private int birthDate;
	private int gender;
	private int birthCertificateId;
	private int birthCertificateSerial;
	private long nationalId;
	private String firstName;
	private String surName;
	private String fatherName;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "ICI_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "ICI_BIRTH_DATE")
	public int getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(int birthDate) {
		this.birthDate = birthDate;
	}

	@Column(name = "ICI_GENDER")
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	@Column(name = "ICI_BIRTH_CERT_ID")
	public int getBirthCertificateId() {
		return birthCertificateId;
	}

	public void setBirthCertificateId(int birthCertificateId) {
		this.birthCertificateId = birthCertificateId;
	}

	@Column(name = "ICI_BIRTH_CERT_SERIAL")
	public int getBirthCertificateSerial() {
		return birthCertificateSerial;
	}

	public void setBirthCertificateSerial(int birthCertificateSerial) {
		this.birthCertificateSerial = birthCertificateSerial;
	}

	@Column(name = "ICI_NATIONAL_ID")
	public long getNationalId() {
		return nationalId;
	}

	public void setNationalId(long nationalId) {
		this.nationalId = nationalId;
	}

	@Column(name = "ICI_FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "ICI_SUR_NAME")
	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	@Column(name = "ICI_FATHER_NAME")
	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
}
