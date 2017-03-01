package com.gam.nocr.ems.data.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_HOLIDAY")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_HOLIDAY", allocationSize = 1)
public class HolidayTO extends ExtEntityTO {

	private Timestamp holiday;

	private int flag;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "HOL_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "HOL_HOLIDAY")
	public Timestamp getHoliday() {
		return holiday;
	}

	public void setHoliday(Timestamp holiday) {
		this.holiday = holiday;
	}

	@Column(name = "HOL_FLAG")
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
