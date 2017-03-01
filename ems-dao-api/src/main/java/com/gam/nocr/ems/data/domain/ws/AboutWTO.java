package com.gam.nocr.ems.data.domain.ws;

import java.util.Date;

import com.gam.nocr.ems.util.EmsUtil;

public class AboutWTO {
	private Long id;
	private Date createDate;
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AboutWTO() {
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
