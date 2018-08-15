package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface OfficeActiveShiftDAO extends EmsBaseDAO<OfficeActiveShiftTO> {

    OfficeActiveShiftTO findByEnrollmentOfficeId(
            Long enrollmentOfficeId, int activeDate, ShiftEnum shiftNo) throws BaseException;

     void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException;

    OfficeActiveShiftTO OfficeActiveShiftByOfficeIdAndRsvDate(Long officeId, ShiftEnum shiftEnum, int myDate) throws BaseException;
}
