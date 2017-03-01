package com.gam.nocr.ems.util;

import com.gam.commons.core.data.domain.ExtEntityTO;

public class NistResult{

	private byte[] nistHeader;
	private String ampFingers;

	public NistResult(byte[] nistHeader, String ampFingers) {
		this.nistHeader = nistHeader;
		this.ampFingers = ampFingers;

	}

	public byte[] getNistHeader() {
		return nistHeader;
	}

	public void setNistHeader(byte[] nistHeader) {
		this.nistHeader = nistHeader;
	}

	public String getAmpFingers() {
		return ampFingers;
	}

	public void setAmpFingers(String ampFingers) {
		this.ampFingers = ampFingers;
	}

}
