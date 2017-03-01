package com.gam.nocr.ems.data.enums;

public enum OfficeCalenderType {
	ThuFriFreeTime, FriFreeTime, NoFreeTime,ThuFreeTime;

	public static Long toLong(OfficeCalenderType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case ThuFriFreeTime:
			return 0L;
		case FriFreeTime:
			return 1L;
		case NoFreeTime:
			return 2L;
		case ThuFreeTime:
			return 3L;
		}

		return null;
	}

	public static OfficeCalenderType toCalenderType(Long type) {
		if (type == null) {
			return null;
		}

		switch (type.intValue()) {
		case 0:
			return ThuFriFreeTime;
		case 1:
			return FriFreeTime;
		case 2:
			return NoFreeTime;
		case 3:
			return ThuFreeTime;
		}
		return null;
	}

}
