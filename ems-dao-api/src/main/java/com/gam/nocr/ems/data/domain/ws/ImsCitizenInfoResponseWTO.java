package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

public class ImsCitizenInfoResponseWTO {

	private Long requestId;
	private Boolean imsIsDown;
	private Boolean estelamIsFalse;
	private Boolean imsIsDead;
	private Boolean imsIsForbidden;
	private Boolean imsIsInfoError;
	private Boolean imsIsNameError;
	private Boolean imsIsLastNameError;
	private Boolean imsIsBrithDateError;
	private Boolean imsIsFatherNameError;
	private byte[] nistHeader;
	private String logInfo; 

	public ImsCitizenInfoResponseWTO() {
		// this.serviceResult= false;
		this.imsIsDead = false;
		this.imsIsForbidden = false;
		this.imsIsInfoError = false;
		this.imsIsNameError = false;
		this.imsIsLastNameError = false;
		this.imsIsBrithDateError = false;
		this.imsIsFatherNameError = false;
		this.imsIsDown = false;
		this.estelamIsFalse = false;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Boolean getImsIsDead() {
		return imsIsDead;
	}

	public void setImsIsDead(Boolean imsIsDead) {
		this.imsIsDead = imsIsDead;
	}

	public Boolean getImsIsForbidden() {
		return imsIsForbidden;
	}

	public void setImsIsForbidden(Boolean imsIsForbidden) {
		this.imsIsForbidden = imsIsForbidden;
	}

	public Boolean getImsIsInfoError() {
		return imsIsInfoError;
	}

	public void setImsIsInfoError(Boolean imsIsInfoError) {
		this.imsIsInfoError = imsIsInfoError;
	}

	public Boolean getImsIsNameError() {
		return imsIsNameError;
	}

	public void setImsIsNameError(Boolean imsIsNameError) {
		this.imsIsNameError = imsIsNameError;
	}

	public Boolean getImsIsLastNameError() {
		return imsIsLastNameError;
	}

	public void setImsIsLastNameError(Boolean imsIsLastNameError) {
		this.imsIsLastNameError = imsIsLastNameError;
	}

	public Boolean getImsIsBrithDateError() {
		return imsIsBrithDateError;
	}

	public void setImsIsBrithDateError(Boolean imsIsBrithDateError) {
		this.imsIsBrithDateError = imsIsBrithDateError;
	}

	public Boolean getImsIsFatherNameError() {
		return imsIsFatherNameError;
	}

	public void setImsIsFatherNameError(Boolean imsIsFatherNameError) {
		this.imsIsFatherNameError = imsIsFatherNameError;
	}

	public byte[] getNistHeader() {
		return nistHeader;
	}

	public void setNistHeader(byte[] nistHeader) {
		this.nistHeader = nistHeader;
	}

	public Boolean getImsIsDown() {
		return imsIsDown;
	}

	public void setImsIsDown(Boolean imsIsDown) {
		this.imsIsDown = imsIsDown;
	}

	public Boolean getEstelamIsFalse() {
		return estelamIsFalse;
	}

	public void setEstelamIsFalse(Boolean estelamIsFalse) {
		this.estelamIsFalse = estelamIsFalse;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

}
