package com.gam.nocr.ems.biz.service.internal.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.PortalBaseInfoService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.HolidayDAO;
import com.gam.nocr.ems.data.domain.HolidayTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
@Stateless(name = "HolidayService")
@Local(HolidayServiceLocal.class)
@Remote(HolidayServiceRemote.class)
public class HolidayServiceImpl extends EMSAbstractService implements
		HolidayServiceLocal, HolidayServiceRemote {

	private HolidayDAO getHolidayDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_HOLIDAY));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.HMS_006,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_HOLIDAY.split(","));
		}
	}

	/**
	 * =================== Getter for Services ===================
	 */

	@Override
	@Permissions(value = "ems_holiday")
	public Long save(HolidayTO to) throws BaseException {
		try {
			to.setFlag(1);
			return getHolidayDAO().create(to).getId();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HMS_015,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_holiday")
	public Long update(HolidayTO to) throws BaseException {
		try {
			to.setFlag(1);
			getHolidayDAO().update(to);
			return to.getId();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HMS_016,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_holiday")
	public boolean remove(String holidayIds) throws BaseException {
		try {
			if (holidayIds == null || holidayIds.trim().length() == 0)
				throw new ServiceException(BizExceptionCode.HMS_012,
						BizExceptionCode.HMS_012_MSG);
			String[] ids = holidayIds.split(",");
			try {
				for (String id : ids) {
					HolidayTO holidayTO = getHolidayDAO().find(HolidayTO.class,
							Long.valueOf(id));

					holidayTO.setFlag(2);
					getHolidayDAO().update(holidayTO);
				}
			} catch (NumberFormatException e) {
				throw new ServiceException(BizExceptionCode.HMS_014,
						BizExceptionCode.HMS_014_MSG);
			}
			return true;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HMS_018,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	public void notifyPortalAboutModifiedHoliday() throws BaseException {
		List<HolidayTO> holidayTOList = getHolidayDAO().getAllChangedHoliday();

		List<HolidayTO> holidayChangeList = new ArrayList<HolidayTO>();
		List<HolidayTO> holidayRemoveList = new ArrayList<HolidayTO>();

		for (HolidayTO holidayTO : holidayTOList) {

			if (holidayTO.getFlag() == 1)
				holidayChangeList.add(holidayTO);
			else if (holidayTO.getFlag() == 2)
				holidayRemoveList.add(holidayTO);

		}

//		if (EmsUtil.checkListSize(holidayTOList))
//			getPortalBaseInfoService().updateHoliday(holidayTOList);
		if (EmsUtil.checkListSize(holidayChangeList))
			getHolidayDAO().notifyUpdateHolidays(holidayChangeList);
		if (EmsUtil.checkListSize(holidayRemoveList))
			getHolidayDAO().notifyRemoveHolidays(holidayRemoveList);
	}

	private PortalBaseInfoService getPortalBaseInfoService()
			throws BaseException {
		try {
			return ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_BASE_INFO), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.HMS_020,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
		}
	}

	private String dayOfWeekString(int dayOfWeek) {

		switch (dayOfWeek) {
		case 0:
			return "یکشنبه";

		case 1:
			return "دوشنبه";

		case 2:
			return "سه شنبه";

		case 3:
			return "چهارشنبه";

		case 4:
			return "پنج شنبه";

		case 5:
			return "جمعبه";

		case 6:
			return "شنبه";

		default:
			return null;
		}

	}
}
