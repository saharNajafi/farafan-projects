package com.gam.nocr.ems.data.domain.vol;

import java.util.Date;

import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class AboutVTO extends ExtEntityTO {

	private Date createDateAbout;
	private String content;

	public Date getCreateDateAbout() {
		return createDateAbout;
	}

	public void setCreateDateAbout(Date createDateAbout) {
		this.createDateAbout = createDateAbout;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
