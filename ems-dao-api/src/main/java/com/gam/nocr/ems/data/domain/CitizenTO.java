package com.gam.nocr.ems.data.domain;

import java.util.Date;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CITIZEN")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CITIZEN", allocationSize = 1)
public class CitizenTO extends ExtEntityTO {

    private CitizenInfoTO citizenInfo;
    private String firstNamePersian;
    private String surnamePersian;
    private String nationalID;
    private Long reduplicate;
    private Boolean purgeBio = Boolean.FALSE;
    private Date purgeBioDate;

    public CitizenTO() {
    }

    public CitizenTO(CitizenInfoTO citizen, String firstNamePersian, String surnamePersian, String nationalID) {
        this.citizenInfo = citizen;
        this.firstNamePersian = firstNamePersian;
        this.surnamePersian = surnamePersian;
        this.nationalID = nationalID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CTZ_ID")
    public Long getId() {
        return super.getId();
    }

    @OneToOne(mappedBy = "citizen", cascade = CascadeType.PERSIST)
    public CitizenInfoTO getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(CitizenInfoTO citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Column(name = "CTZ_FIRST_NAME_FA", length = 42, nullable = false)
    public String getFirstNamePersian() {
        return firstNamePersian;
    }

    public void setFirstNamePersian(String firstNamePersian) {
        this.firstNamePersian = firstNamePersian;
    }

    @Column(name = "CTZ_SURNAME_FA", length = 42, nullable = false)
    public String getSurnamePersian() {
        return surnamePersian;
    }

    public void setSurnamePersian(String surnamePersian) {
        this.surnamePersian = surnamePersian;
    }

    @Size(min = 10, max = 10)
    @Column(name = "CTZ_NATIONAL_ID", nullable = false)
    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }


    @Column(name = "CTZ_REDUPLICATE")
    public Long getReduplicate() {
        return reduplicate;
    }

    public void setReduplicate(Long reduplicate) {
        this.reduplicate = reduplicate;
    }
    
    @Column(name = "CTZ_PURGE_BIO")
    public Boolean getPurgeBio() {
		return purgeBio;
	}

	public void setPurgeBio(Boolean purgeBio) {
		this.purgeBio = purgeBio;
	}

	@Column(name = "CTZ_PURGE_BIO_DATE")
	public Date getPurgeBioDate() {
		return purgeBioDate;
	}

	public void setPurgeBioDate(Date purgeBioDate) {
		this.purgeBioDate = purgeBioDate;
	}


    @Override
    public String toString() {
        String string = "CitizenTO : { " +
                "id = " + getId() +
                ", nationalID = " + nationalID +
                ", reduplicate = " + reduplicate;

        string += " }";
        return string;
    }
}
