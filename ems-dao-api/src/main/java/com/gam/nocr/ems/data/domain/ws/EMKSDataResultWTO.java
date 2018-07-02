package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

import java.io.Serializable;

public class EMKSDataResultWTO implements Serializable{

	private String id;
	private String sign;
	private String nmoc;

	public EMKSDataResultWTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNmoc() {
		return nmoc;
	}

	public void setNmoc(String nmoc) {
		this.nmoc = nmoc;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
