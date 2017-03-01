package com.gam.nocr.ems.data.domain.vol;

import java.sql.Timestamp;

import com.gam.commons.core.data.domain.ExtEntityTO;

public class PreparedMessageVTO extends ExtEntityTO {

	private String title;
	private Timestamp createDate;
	private String senderUsername;
	private Boolean hasFile;
	private String preparedState;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	public String getPreparedState() {
		return preparedState;
	}

	public void setPreparedState(String preparedState) {
		this.preparedState = preparedState;
	}

}