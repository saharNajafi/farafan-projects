package com.gam.nocr.ems.data.domain.vol;

import java.io.Serializable;
import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class EstelamFalseVTO extends ExtEntityTO implements Serializable {

	private Long id;
	private String citizenFirstName;
	private String citizenSurname;
	private String citizenNId;
	private String trackingId;
	private String birthDateSolar;
	private String cardRequestState;
	private String registrationDate;
	private String officeName;
	private Timestamp reservationDate;

	public EstelamFalseVTO() {
	}

	public EstelamFalseVTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCitizenFirstName() {
		return citizenFirstName;
	}

	public void setCitizenFirstName(String citizenFirstName) {
		this.citizenFirstName = citizenFirstName;
	}

	public String getCitizenSurname() {
		return citizenSurname;
	}

	public void setCitizenSurname(String citizenSurname) {
		this.citizenSurname = citizenSurname;
	}

	public String getCitizenNId() {
		return citizenNId;
	}

	public void setCitizenNId(String citizenNId) {
		this.citizenNId = citizenNId;
	}

	public String getCardRequestState() {
		return cardRequestState;
	}

	public void setCardRequestState(String cardRequestState) {
		this.cardRequestState = cardRequestState;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getBirthDateSolar() {
		return birthDateSolar;
	}

	public void setBirthDateSolar(String birthDateSolar) {
		this.birthDateSolar = birthDateSolar;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Timestamp getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Timestamp reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
