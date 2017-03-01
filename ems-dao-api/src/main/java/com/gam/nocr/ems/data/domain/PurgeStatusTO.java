package com.gam.nocr.ems.data.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.JSONable;

@Entity
@Table(name = "EMST_PURGE_STATUS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_PURGE_STATUS", allocationSize = 1)
public class PurgeStatusTO extends ExtEntityTO implements Serializable, JSONable {

    private Long lastPurgesCRQId;
    private Date lastPurgeDate;
	
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "PSS_ID")
    public Long getId() {
        return super.getId();
    }
    
    @Column(name = "PSS_LAST_CRQ_ID")
	public Long getLastPurgesCRQId() {
		return lastPurgesCRQId;
	}

	public void setLastPurgesCRQId(Long lastPurgesCRQId) {
		this.lastPurgesCRQId = lastPurgesCRQId;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PSS_LAST_RUN_DATE")
	public Date getLastPurgeDate() {
		return lastPurgeDate;
	}

	public void setLastPurgeDate(Date lastPurgeDate) {
		this.lastPurgeDate = lastPurgeDate;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
