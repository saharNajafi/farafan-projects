package com.gam.nocr.ems.data.domain.vol;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import flexjson.JSON;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CardRequestVTO extends ExtEntityTO implements Serializable {

	private Long id;
	private String citizenFirstName;
	private String citizenSurname;
	private String citizenNId;
	private Long enrollmentOfficeId;
	private String enrollmentOfficeName;
	private Timestamp enrolledDate;
	private Timestamp portalEnrolledDate;
	private String cardRequestState;
	private String cardType;
	private String cardState;
	private String trackingId;
	private String requestedAction;
	private String requestOrigin;
	private String deliveredOfficeName;
	private String birthDateSolar;
	private Timestamp deliveredDate;
	private Integer priority;
	
	private Integer flag;
	private Boolean faceFlag;
	private Boolean fingerFlag;
	private Boolean documentFlag;
	private Timestamp reservationDate;

	private CitizenInfoVTO citizenInfo;
	private List<SpouseVTO> spouses;
	private List<ChildVTO> children;
	
	public CardRequestVTO() {
	}
    
	public CardRequestVTO(Long id, String citizenFirstName,
			String citizenSurname, String citizenNId, Long enrollmentOfficeId,
			String enrollmentOfficeName, Timestamp enrolledDate,
			Timestamp portalEnrolledDate, String cardRequestState,
			String cardType, String cardState, String trackingId,
			String requestedAction, String requestOrigin,
			String deliveredOfficeName, Timestamp deliveredDate,
			Timestamp reservationDate, Integer flag) {
		this.id = id;
		this.citizenFirstName = citizenFirstName;
		this.citizenSurname = citizenSurname;
		this.citizenNId = citizenNId;
		this.enrollmentOfficeId = enrollmentOfficeId;
		this.enrollmentOfficeName = enrollmentOfficeName;
		this.enrolledDate = enrolledDate;
		this.portalEnrolledDate = portalEnrolledDate;
		this.cardRequestState = cardRequestState;
		this.cardType = cardType;
		this.cardState = cardState;
		this.trackingId = trackingId;
		this.requestedAction = requestedAction;
		this.requestOrigin = requestOrigin;
		this.deliveredOfficeName = deliveredOfficeName;
		this.deliveredDate = deliveredDate;
		this.reservationDate = reservationDate;
		this.flag = flag;

		if (flag != null) {
			faceFlag = (flag & 1) == 1;
			fingerFlag = (flag & 2) == 2;
			documentFlag = (flag & 4) == 4;
		}
	}

	public CardRequestVTO(Long id) {
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

	public Long getEnrollmentOfficeId() {
		return enrollmentOfficeId;
	}

	public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
		this.enrollmentOfficeId = enrollmentOfficeId;
	}

	public String getEnrollmentOfficeName() {
		return enrollmentOfficeName;
	}

	public void setEnrollmentOfficeName(String enrollmentOfficeName) {
		this.enrollmentOfficeName = enrollmentOfficeName;
	}

	@JSON(include = false)
	public Timestamp getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Timestamp enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

	@JSON(include = false)
	public Timestamp getPortalEnrolledDate() {
		return portalEnrolledDate;
	}

	public void setPortalEnrolledDate(Timestamp portalEnrolledDate) {
		this.portalEnrolledDate = portalEnrolledDate;
	}

	public String getCardRequestState() {
		return cardRequestState;
	}

	public void setCardRequestState(String cardRequestState) {
		this.cardRequestState = cardRequestState;
	}

	@JSON(include = false)
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public String getRequestedAction() {
		return requestedAction;
	}

	public void setRequestedAction(String requestedAction) {
		this.requestedAction = requestedAction;
	}

	public String getRequestOrigin() {
		return requestOrigin;
	}

	public void setRequestOrigin(String requestOrigin) {
		this.requestOrigin = requestOrigin;
	}

	public String getDeliveredOfficeName() {
		return deliveredOfficeName;
	}

	public void setDeliveredOfficeName(String deliveredOfficeName) {
		this.deliveredOfficeName = deliveredOfficeName;
	}

	public String getBirthDateSolar() {
		return birthDateSolar;
	}

	public void setBirthDateSolar(String birthDateSolar) {
		this.birthDateSolar = birthDateSolar;
	}
	
	@JSON(include = false)
	public Timestamp getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(Timestamp deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Boolean getFaceFlag() {
		return faceFlag;
	}

	public void setFaceFlag(Boolean faceFlag) {
		this.faceFlag = faceFlag;
	}

	public Boolean getFingerFlag() {
		return fingerFlag;
	}

	public void setFingerFlag(Boolean fingerFlag) {
		this.fingerFlag = fingerFlag;
	}

	public Boolean getDocumentFlag() {
		return documentFlag;
	}

	public void setDocumentFlag(Boolean documentFlag) {
		this.documentFlag = documentFlag;
	}

	public Timestamp getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Timestamp reservationDate) {
		this.reservationDate = reservationDate;
	}

	public CitizenInfoVTO getCitizenInfo() {
		return citizenInfo;
	}

	public void setCitizenInfo(CitizenInfoVTO citizenInfo) {
		this.citizenInfo = citizenInfo;
	}

	public List<SpouseVTO> getSpouses() {
		return spouses;
	}

	public void setSpouses(List<SpouseVTO> spouses) {
		this.spouses = spouses;
	}

	public List<ChildVTO> getChildren() {
		return children;
	}

	public void setChildren(List<ChildVTO> children) {
		this.children = children;
	}

	

}

