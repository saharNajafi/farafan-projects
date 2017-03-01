package com.gam.nocr.ems.web.ws;

import javax.xml.bind.annotation.XmlType;

/**
 * The class InternalInterfaceException is used to wrap the information which
 * are needed in handling exceptions that may happen in services exposed to
 * CCOS. It's used as a property in {@link WebServiceFaultException}
 * 
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@XmlType(namespace = "http://ws.web.ems.nocr.gam.com/fault")
public class InternalInterfaceException {

	private String code;
	private int mode;
	private String data;

	public InternalInterfaceException() {
	}

	public InternalInterfaceException(String code) {
		this.code = code;
	}

	public InternalInterfaceException(String code, int mode) {
		this.code = code;
		this.mode = mode;
	}

	public InternalInterfaceException(String code, int mode, String data) {
		this.code = code;
		this.mode = mode;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
