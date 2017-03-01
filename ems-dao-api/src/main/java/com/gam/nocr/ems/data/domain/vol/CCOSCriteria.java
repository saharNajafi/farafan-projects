package com.gam.nocr.ems.data.domain.vol;

import java.util.Map;
import java.util.Set;

import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

public class CCOSCriteria extends GeneralCriteria {

	public CCOSCriteria(Map<String, Object> parameters, String orderBy,
			UserProfileTO userProfileTO, Integer pageSize, Integer pageNo,
			int cartableType, Set<String> parts, EnrollmentOfficeTO enrollmentOffice) {
		super(parameters, orderBy, userProfileTO, pageSize, pageNo);
		this.parts = parts;
		this.cartableType = cartableType;
		this.enrollmentOffice = enrollmentOffice;
	}

	private int cartableType;
	private Set<String> parts;
	private EnrollmentOfficeTO enrollmentOffice;

	public int getCartableType() {
		return cartableType;
	}

	public void setCartableType(int cartableType) {
		this.cartableType = cartableType;
	}

	public Set<String> getParts() {
		return parts;
	}

	public void setParts(Set<String> parts) {
		this.parts = parts;
	}

	public EnrollmentOfficeTO getEnrollmentOffice() {
		return enrollmentOffice;
	}

	public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
		this.enrollmentOffice = enrollmentOffice;
	}

}
