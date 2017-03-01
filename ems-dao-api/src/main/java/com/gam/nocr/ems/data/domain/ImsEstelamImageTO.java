package com.gam.nocr.ems.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.gam.commons.core.data.domain.ExtEntityTO;

import flexjson.JSON;


@Entity
@Table(name = "EMST_IMS_EST_IMAGE")
//@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_IMSESTIMAGE", allocationSize = 1)
public class ImsEstelamImageTO extends ExtEntityTO {

	private String nationalID;
    private byte[] nationalIdImage;
    private CitizenTO citizen;
    


//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
//    @Column(name = "IEI_ID")
    @Id    
    public Long getId() {
        return super.getId();
    }    

    @Size(min = 10, max = 10)
    @Column(name = "IEI_NATIONAL_ID", nullable = false)
	public String getNationalID() {
		return nationalID;
	}

	public void setNationalID(String nationalID) {
		this.nationalID = nationalID;
	}

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IEI_ID")
	@JSON(include = false)
	public CitizenTO getCitizen() {
		return citizen;
	}

	public void setCitizen(CitizenTO citizen) {
		this.citizen = citizen;
	}

	@Lob
	@Column(name = "IEI_NATIONAL_IMAGE")
	@JSON(include = false)
	public byte[] getNationalIdImage() {
		  return nationalIdImage == null ? nationalIdImage : nationalIdImage.clone();
	}

	public void setNationalIdImage(byte[] nationIdImage) {
		this.nationalIdImage = nationIdImage;
	}

 
   
   
}