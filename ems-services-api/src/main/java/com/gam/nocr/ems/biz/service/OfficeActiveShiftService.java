package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface OfficeActiveShiftService extends Service{

    OfficeActiveShiftTO findActiveShiftByEofId(Long enrollmentOfficeId) throws BaseException;

    OfficeActiveShiftTO OfficeActiveShiftByOfficeIdAndRsvDate(
            Long officeId, ShiftEnum shiftEnum, int myDate) throws BaseException;

    void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException;
}
