package com.gam.nocr.ems.data.mapper.tomapper;

import java.util.ArrayList;
import java.util.List;

import com.gam.commons.core.BaseException;
//import com.gam.nocr.ems.biz.service.external.client.portal.HolidayWTO;
import com.gam.nocr.ems.data.domain.HolidayTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class HolidayMapper {
	private HolidayMapper() {
	}

//	public static HolidayWTO convert(HolidayTO holidayTO) {
//		HolidayWTO holidayWTO = null;
//		if (holidayTO != null) {
//			holidayWTO = new HolidayWTO();
//			holidayWTO.setId(holidayTO.getId());
//			holidayWTO.setFlag(holidayTO.getFlag());
//			holidayWTO.setHoliday(holidayTO.getHoliday().getTime());
//		}
//		return holidayWTO;
//	}

//	public static List<HolidayWTO> convert(List<HolidayTO> holidayTOList)
//			throws BaseException {
//		List<HolidayWTO> holidayWTOList = null;
//		if (holidayTOList != null && !holidayTOList.isEmpty()) {
//			holidayWTOList = new ArrayList<HolidayWTO>();
//			for (HolidayTO holidayTO : holidayTOList) {
//				holidayWTOList.add(convert(holidayTO));
//			}
//		}
//		return holidayWTOList;
//	}
}
