package com.gam.nocr.ems.data.domain.ws;

import java.sql.Timestamp;

import flexjson.JSON;

public class CitizenBirthDateAndGenderWTO {
	
	private Timestamp birthDate;
	private String gender;

	@JSON(include = false)
	public Timestamp getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}

	@JSON(include = false)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
