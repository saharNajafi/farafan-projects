package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

import java.io.Serializable;

public class EMKSCardMoCKeysWTO implements Serializable{

	private String moc_enc;
	private String moc_mac;

	public EMKSCardMoCKeysWTO() {
	}

	public String getMoc_enc() {
		return moc_enc;
	}

	public void setMoc_enc(String moc_enc) {
		this.moc_enc = moc_enc;
	}

	public String getMoc_mac() {
		return moc_mac;
	}

	public void setMoc_mac(String moc_mac) {
		this.moc_mac = moc_mac;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
