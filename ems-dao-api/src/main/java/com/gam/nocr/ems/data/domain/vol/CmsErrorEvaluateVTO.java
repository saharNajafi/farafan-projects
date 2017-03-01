package com.gam.nocr.ems.data.domain.vol;

import java.io.Serializable;
import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import flexjson.JSON;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CmsErrorEvaluateVTO extends ExtEntityTO implements Serializable {

	private Long id;

	private String citizenFirstName;
	private String citizenSurname;
	private String citizenNId;

	private String result;

	private Long cardRequestId;

	private Long citizenId;

	private String cmsRequestId;

	private String officeName;

	private Timestamp sendDate;

	private Timestamp backDate;

	public CmsErrorEvaluateVTO() {
	}

	public CmsErrorEvaluateVTO(Long id) {
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

	public Long getCardRequestId() {
		return cardRequestId;
	}

	public void setCardRequestId(Long cardRequestId) {
		this.cardRequestId = cardRequestId;
	}

	public String getCmsRequestId() {
		return cmsRequestId;
	}

	public void setCmsRequestId(String cmsRequestId) {
		this.cmsRequestId = cmsRequestId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(Long citizenId) {
		this.citizenId = citizenId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	
	@JSON(include = false)
	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	@JSON(include = false)
	public Timestamp getBackDate() {
		return backDate;
	}

	public void setBackDate(Timestamp backDate) {
		this.backDate = backDate;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
