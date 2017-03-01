package com.gam.nocr.ems.data.domain.ws;

import java.sql.Timestamp;

import com.gam.nocr.ems.util.EmsUtil;

public class HelpWTO {
	private Long helpId;
	private Timestamp helpDate;
	private String helpTitle;
	private byte[] helpFile;
	private String helpExtention;

	public Long getHelpId() {
		return helpId;
	}

	public void setHelpId(Long helpId) {
		this.helpId = helpId;
	}

	public Timestamp getHelpDate() {
		return helpDate;
	}

	public void setHelpDate(Timestamp helpDate) {
		this.helpDate = helpDate;
	}

	public String getHelpTitle() {
		return helpTitle;
	}

	public void setHelpTitle(String helpTitle) {
		this.helpTitle = helpTitle;
	}

	public byte[] getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(byte[] helpFile) {
		this.helpFile = helpFile;
	}

	public String getHelpExtention() {
		return helpExtention;
	}

	public void setHelpExtention(String helpExtention) {
		this.helpExtention = helpExtention;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
