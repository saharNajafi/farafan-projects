package com.gam.nocr.ems.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BiometricInfoType;
import com.gam.nocr.ems.data.enums.FingerQualityType;
import com.gam.nocr.ems.data.enums.MinutiaType;
import com.gam.nocr.ems.util.EmsUtil;


@Entity
@Table(name = "EMST_BIOMETRIC_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BIOMETRIC_INFO")
public class BiometricInfoTO extends ExtEntityTO  {

	private String nationalID;
    private BiometricInfoType type;
    private MinutiaType minType;
    private CitizenTO citizen;
    private FingerQualityType fingerQualityType = FingerQualityType.UNDEFINED;
    private String featureExtractorID = "0001";
    

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BII_ID")
    public Long getId() {
        return super.getId();
    }

    @Size(min = 10, max = 10)
    @Column(name = "BII_NID", nullable = false)
    public String getNationalID() {
        return nationalID;
    }
    
    @OneToOne
    @JoinColumn(name = "BII_CITIZEN_ID")
    public CitizenTO getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenTO citizen) {
        this.citizen = citizen;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }


    @Enumerated(EnumType.ORDINAL)
   	@Column(name = "BII_TYPE")
    public BiometricInfoType getType() {
		return type;
	}
    
    public void setType(BiometricInfoType type) {
		this.type = type;
	}

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "BII_MINUTIA_TYPE")
    public MinutiaType getMinType() {
    	return minType;
    }
    
    public void setMinType(MinutiaType minType) {
    	this.minType = minType;
    }
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "BII_FING_QUALITY")
    public FingerQualityType getFingerQualityType() {
		return fingerQualityType;
	}

	public void setFingerQualityType(FingerQualityType fingerQualityType) {
		this.fingerQualityType = fingerQualityType;
	}

    @Column(name = "BMI_FEATURE_EXTRACTOR_ID",nullable = false,length = 4, columnDefinition = "varchar2(4) default '1'")
    public String getFeatureExtractorID() {
        return featureExtractorID;
    }

    public void setFeatureExtractorID(String featureExtractorID) {
        this.featureExtractorID = featureExtractorID;
    }

    @Override
    public String toString() {
    	return EmsUtil.toJSON(this);
    }

    



}