package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.nocr.ems.data.domain.HolidayTO;

public interface HolidayDAO extends EmsBaseDAO<HolidayTO> {

	List<HolidayTO> getAllChangedHoliday();

	void notifyUpdateHolidays(List<HolidayTO> tos);
	
	
	void notifyRemoveHolidays(List<HolidayTO> tos);

}
