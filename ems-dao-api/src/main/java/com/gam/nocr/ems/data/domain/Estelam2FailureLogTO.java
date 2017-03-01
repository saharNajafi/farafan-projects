package com.gam.nocr.ems.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.EstelamFailureType;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 * 
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_ESTELAM2_FAILURE_LOG")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_ESTELAM2_FAILURE_LOG", allocationSize = 1)
public class Estelam2FailureLogTO extends ExtEntityTO {

	private Date createDate;
	private EstelamFailureType type;
	private String nationalID;
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESL_TYPE")
	public EstelamFailureType getType() {
		return type;
	}

	public void setType(EstelamFailureType type) {
		this.type = type;
	}

	@Size(min = 10, max = 10)
	@Column(name = "ESL_NATIONAL_ID", nullable = false)
	public String getNationalID() {
		return nationalID;
	}

	public void setNationalID(String nationalID) {
		this.nationalID = nationalID;
	}

	@Column(name = "ESL_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Estelam2FailureLogTO() {
	}

	public Estelam2FailureLogTO(Long id) {
		this.setId(id);
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ESL_CREATE_DATE", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "ESL_ID")
	public Long getId() {
		return super.getId();
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
