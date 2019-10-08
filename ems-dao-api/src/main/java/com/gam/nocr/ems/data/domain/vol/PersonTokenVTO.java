package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.sql.Timestamp;

public class PersonTokenVTO extends ExtEntityTO {

	private String tokenState;
	private String tokenType;
	private String tokenReason;
	private String requestID;
	private Timestamp requestDate;
	private Timestamp issuanceDate;
	private Timestamp deliverDate;
	private String adminName;
	private String departmentName;
	private PersonVTO person;

	@Override
	public Long getId() {
		return super.getId();
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	public String getTokenState() {
		return tokenState;
	}

	public void setTokenState(String tokenState) {
		this.tokenState = tokenState;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(Timestamp issuanceDate) {
		this.issuanceDate = issuanceDate;
	}

	public PersonVTO getPerson() {
		return person;
	}

	public void setPerson(PersonVTO person) {
		this.person = person;
	}

	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getTokenReason() {
		return tokenReason;
	}

	public void setTokenReason(String tokenReason) {
		this.tokenReason = tokenReason;
	}
}
