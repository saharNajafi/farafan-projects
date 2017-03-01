package com.gam.nocr.ems.data.enums;

public enum BiometricInfoType {
	 PIN, MOC;

	public Long  toTOLong(BiometricInfoType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case PIN:
			return 0L;
		case MOC:
			return 1L;
		}

		return null;
	}
}
