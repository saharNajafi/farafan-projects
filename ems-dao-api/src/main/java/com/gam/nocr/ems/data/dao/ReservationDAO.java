package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import java.util.Date;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface ReservationDAO extends EmsBaseDAO<ReservationTO> {

    public ReservationTO fetchReservationByPortalReservationId(Long portalReservationId) throws BaseException;

    public Boolean deleteByCardRequest(Long cardRequestId) throws BaseException;

    ReservationTO findReservationByCrqId(Long id) throws BaseException;

    Integer findByEnrolAndReserveDateAndShift(Long id, Date fromDate, ShiftEnum shiftNo) throws DataException;
}
