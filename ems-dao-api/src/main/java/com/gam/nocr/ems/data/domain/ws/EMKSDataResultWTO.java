package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

import java.io.Serializable;

public class EMKSDataResultWTO implements Serializable{

	private String id;
	private String sign;
	private String nmoc;
	private String smPin;
	private String smd;
	private String asd;
	private String mac;
	private String enc;

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

	public void setSmPin(String smPin) {
		this.smPin = smPin;
	}

	public String getSmPin() {
		return smPin;
	}

	public void setSmd(String smd) {
		this.smd = smd;
	}

	public String getSmd() {
		return smd;
	}


	public void setAsd(String asd) {
		this.asd = asd;
	}

	public String getAsd() {
		return asd;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMac() {
		return mac;
	}


	public void setEnc(String enc) {
		this.enc = enc;
	}

	public String getEnc() {
		return enc;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
