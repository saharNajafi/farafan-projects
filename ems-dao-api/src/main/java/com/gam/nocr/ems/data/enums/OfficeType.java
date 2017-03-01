package com.gam.nocr.ems.data.enums;

public enum OfficeType {
	NOCR, OFFICE, POST;

	public Long  toTOLong(OfficeType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case NOCR:
			return 1L;
		case OFFICE:
			return 2L;
		case POST:
			return 3L;
		}

		return null;
	}
}
