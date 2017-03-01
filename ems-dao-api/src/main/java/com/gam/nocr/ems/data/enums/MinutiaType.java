package com.gam.nocr.ems.data.enums;

public enum MinutiaType {
	NULL, MIN1, BOTH;

	public Long  toTOLong(MinutiaType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case NULL:
			return 0L;
		case MIN1:
			return 1L;
		case BOTH:
			return 2L;
		}

		return null;
	}
}
