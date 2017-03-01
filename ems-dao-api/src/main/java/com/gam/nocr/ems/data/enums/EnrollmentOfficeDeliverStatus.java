package com.gam.nocr.ems.data.enums;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum EnrollmentOfficeDeliverStatus {
	DISABLED, ENABLED; // 1 : ENABLED

	public static String toStr(EnrollmentOfficeDeliverStatus state) {
		if (state == null) {
			return "";
		}

		switch (state) {
		case ENABLED:
			return "ENABLED";
		case DISABLED:
			return "DISABLED";
		}
		return "";
	}

	public static EnrollmentOfficeDeliverStatus toState(Long state) {
		if (state == null) {
			return null;
		}

		switch (state.intValue()) {
		case 0:
			return DISABLED;
		case 1:
			return ENABLED;
		}
		return null;
	}

}
