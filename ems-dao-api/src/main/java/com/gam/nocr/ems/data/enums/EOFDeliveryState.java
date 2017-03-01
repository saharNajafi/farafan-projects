package com.gam.nocr.ems.data.enums;

public enum EOFDeliveryState {
	ENABLE, DISABLE;

	public static Long toLong(EOFDeliveryState state) {
		if (state == null)
			return null;
		switch (state) {
		case ENABLE:
			return 1L;

		case DISABLE:
			return 0L;
		}
		return null;

	}
}
