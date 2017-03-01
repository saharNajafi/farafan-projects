package com.gam.nocr.ems.data.domain.vol;

import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

public class CardDispatchInfoVTO extends ExtEntityTO {

	private Long cardId;
	private String crn;
	private Timestamp cardLostDate;
	// private String citizenFirstName;
	// private String citizenLastName;
	private String batchId;
	private String isConfirm;
	private String fname;
	private String lname;
	private String nationalId;

	
	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Timestamp getCardLostDate() {
		return cardLostDate;
	}

	public void setCardLostDate(Timestamp cardLostDate) {
		this.cardLostDate = cardLostDate;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

}
