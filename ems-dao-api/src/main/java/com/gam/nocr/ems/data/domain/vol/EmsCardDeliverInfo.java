package com.gam.nocr.ems.data.domain.vol;

import java.util.Date;

import com.gam.nocr.ems.data.enums.CardState;

public class EmsCardDeliverInfo {

	private CardState cardState;
	private long cardbatchId;
	private Date cardDeliveredDate;
	private Date cardlostDate;
	private Date cardreceivedDate;
	private Date cardshipmentDate;
	private long estelamId;
	private int officeCode;
	private long personNin;
	private Date cardIssuanceDate;
	private String crn;
	private Long cardRequestId;
	
	
	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Date getCardIssuanceDate() {
		return cardIssuanceDate;
	}

	public void setCardIssuanceDate(Date cardIssuanceDate) {
		this.cardIssuanceDate = cardIssuanceDate;
	}

	public CardState getCardState() {
		return cardState;
	}

	public void setCardState(CardState cardState) {
		this.cardState = cardState;
	}

	public long getCardbatchId() {
		return cardbatchId;
	}

	public void setCardbatchId(long cardbatchId) {
		this.cardbatchId = cardbatchId;
	}

	public Date getCardDeliveredDate() {
		return cardDeliveredDate;
	}

	public void setCardDeliveredDate(Date cardDeliveredDate) {
		this.cardDeliveredDate = cardDeliveredDate;
	}

	public Date getCardlostDate() {
		return cardlostDate;
	}

	public void setCardlostDate(Date cardlostDate) {
		this.cardlostDate = cardlostDate;
	}

	public Date getCardreceivedDate() {
		return cardreceivedDate;
	}

	public void setCardreceivedDate(Date cardreceivedDate) {
		this.cardreceivedDate = cardreceivedDate;
	}

	public Date getCardshipmentDate() {
		return cardshipmentDate;
	}

	public void setCardshipmentDate(Date cardshipmentDate) {
		this.cardshipmentDate = cardshipmentDate;
	}

	public long getEstelamId() {
		return estelamId;
	}

	public void setEstelamId(long estelamId) {
		this.estelamId = estelamId;
	}

	public int getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(int officeCode) {
		this.officeCode = officeCode;
	}

	public long getPersonNin() {
		return personNin;
	}

	public void setPersonNin(long personNin) {
		this.personNin = personNin;
	}

	public Long getCardRequestId() {
		return cardRequestId;
	}

	public void setCardRequestId(Long cardRequestId) {
		this.cardRequestId = cardRequestId;
	}

}
