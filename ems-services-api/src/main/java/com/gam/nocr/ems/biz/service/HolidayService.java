package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.HolidayTO;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface HolidayService extends Service {
	
	public Long save(HolidayTO to) throws BaseException;

    public Long update(HolidayTO to) throws BaseException;

    public boolean remove(String holidayIds) throws BaseException;

	public void notifyPortalAboutModifiedHoliday() throws BaseException;

}
