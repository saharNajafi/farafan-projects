package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;
import java.util.List;

import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

public class PermissionsWTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> name;

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}

}
