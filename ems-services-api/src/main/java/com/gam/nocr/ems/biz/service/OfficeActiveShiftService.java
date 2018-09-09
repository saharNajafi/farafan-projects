package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import java.util.Date;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface OfficeActiveShiftService extends Service{

    OfficeActiveShiftTO findActiveShiftByEofId(Long enrollmentOfficeId) throws BaseException;

    OfficeActiveShiftTO OfficeActiveShiftByOfficeIdAndRsvDate(
            Long officeId, ShiftEnum shiftEnum, int myDate) throws BaseException;

    void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException;

    void activeShiftSaveOrUpdate(OfficeCapacityTO officeCapacityTO, OfficeActiveShiftTO officeActiveShiftTO, EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate) throws BaseException;
}
