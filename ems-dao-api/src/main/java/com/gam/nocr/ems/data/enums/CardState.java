package com.gam.nocr.ems.data.enums;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public enum CardState {
    READY,
    SHIPPED,
    RECEIVED,
    MISSED,
    DISPATCH,
    DELIVERED,
    DESTROYED,
    REVOKED,
    LOST;
	public static Long toLong(CardState state) {
		if (state == null) {
			return null;
		}

		switch (state) {
		case READY:
			return 1L;
		case SHIPPED:
			return 2L;
		case RECEIVED:
			return 3L;
		case MISSED:
			return 4L;
		case DISPATCH:
			return 5L;
		case DELIVERED:
			return 6L;
		case DESTROYED:
			return 7L;
		case REVOKED:
			return 8L;
		case LOST:
			return 9L;
		}
		return null;
	}
    
}
