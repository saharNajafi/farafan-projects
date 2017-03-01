package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.HolidayService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.HolidayTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class HolidayDelegator {

	private HolidayService getService(UserProfileTO userProfileTO)
			throws BaseException {
		HolidayService holidayService = null;
		try {
			holidayService = (HolidayService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_HOLIDAY), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.LDL_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_HOLIDAY.split(","));
		}
		holidayService.setUserProfileTO(userProfileTO);
		return holidayService;
	}

	public long save(UserProfileTO userProfileTO, HolidayTO to)
			throws BaseException {
		return getService(userProfileTO).save(to);
	}

	public long update(UserProfileTO userProfileTO, HolidayTO to)
			throws BaseException {
		return getService(userProfileTO).update(to);
	}

	public boolean remove(UserProfileTO userProfileTO, String holidayIds)
			throws BaseException {
		return getService(userProfileTO).remove(holidayIds);
	}

	public void notifyPortalAboutHoliday() throws BaseException {
		getService(null).notifyPortalAboutModifiedHoliday();
	}

}