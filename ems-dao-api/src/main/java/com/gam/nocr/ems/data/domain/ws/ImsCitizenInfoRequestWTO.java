package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

import java.io.Serializable;

public class ImsCitizenInfoRequestWTO implements Serializable{

	private Long requestId;

	private String imsSerialNumber;

	private String cardFirstname;

	private String cardLastname;

	private String cardNationalNumber;

	private String cardBirthDate;

	private String cardFatherFirstname;

	public ImsCitizenInfoRequestWTO() {
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getImsSerialNumber() {
		return imsSerialNumber;
	}

	public void setImsSerialNumber(String imsSerialNumber) {
		this.imsSerialNumber = imsSerialNumber;
	}

	public String getCardFirstname() {
		return cardFirstname;
	}

	public void setCardFirstname(String cardFirstname) {
		this.cardFirstname = cardFirstname;
	}

	public String getCardLastname() {
		return cardLastname;
	}

	public void setCardLastname(String cardLastname) {
		this.cardLastname = cardLastname;
	}

	public String getCardNationalNumber() {
		return cardNationalNumber;
	}

	public void setCardNationalNumber(String cardNationalNumber) {
		this.cardNationalNumber = cardNationalNumber;
	}

	public String getCardBirthDate() {
		return cardBirthDate;
	}

	public void setCardBirthDate(String cardBirthDate) {
		this.cardBirthDate = cardBirthDate;
	}

	public String getCardFatherFirstname() {
		return cardFatherFirstname;
	}

	public void setCardFatherFirstname(String cardFatherFirstname) {
		this.cardFatherFirstname = cardFatherFirstname;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

}
