package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

public class AccessProductionVTO extends ExtEntityTO {

	private Boolean errorRetryAccess;
	private Boolean errorDeleteImageAccess;
	private Boolean errorRepealedAccess;

	public AccessProductionVTO() {
		this.errorDeleteImageAccess = false;
		this.errorRepealedAccess = false;
		this.errorRetryAccess = false;

	}

	public Boolean getErrorRetryAccess() {
		return errorRetryAccess;
	}

	public void setErrorRetryAccess(Boolean errorRetryAccess) {
		this.errorRetryAccess = errorRetryAccess;
	}

	public Boolean getErrorDeleteImageAccess() {
		return errorDeleteImageAccess;
	}

	public void setErrorDeleteImageAccess(Boolean errorDeleteImageAccess) {
		this.errorDeleteImageAccess = errorDeleteImageAccess;
	}

	public Boolean getErrorRepealedAccess() {
		return errorRepealedAccess;
	}

	public void setErrorRepealedAccess(Boolean errorRepealedAccess) {
		this.errorRepealedAccess = errorRepealedAccess;
	}

}
