package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.PurgeState;
import com.gam.nocr.ems.util.EmsUtil;

@Entity
@Table(name = "EMST_PURGE_HISTORY")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_PURGE_HISTORY", allocationSize = 1)
public class PurgeHistoryTO extends ExtEntityTO {


	private CitizenTO citizen;
	private String nationalId;
	private Date purgeBiometricDate;
	private String metaData;
	private PurgeState purgeState;

	public PurgeHistoryTO() {
	
	
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "PUH_ID")
	public Long getId() {
		return super.getId();
	}

	@ManyToOne
	@JoinColumn(name = "PUH_CITIZEN_ID",nullable=false)
	public CitizenTO getCitizen() {
		return citizen;
	}

	public void setCitizen(CitizenTO citizen) {
		this.citizen = citizen;
	}

    @Size(min = 10, max = 10)
    @Column(name = "PUH_NATIONAL_ID", nullable = false)
	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUH_PURGEBIO_DATE")
	public Date getPurgeBiometricDate() {
		return purgeBiometricDate;
	}

	public void setPurgeBiometricDate(Date purgeBiometricDate) {
		this.purgeBiometricDate = purgeBiometricDate;
	}

	@Lob
	@Column(name = "PUH_METADATA")
	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

    @Enumerated(EnumType.ORDINAL)
	@Column(name = "PUH_STATE")
	public PurgeState getPurgeState() {
		return purgeState;
	}

	public void setPurgeState(PurgeState purgeState) {
		this.purgeState = purgeState;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
