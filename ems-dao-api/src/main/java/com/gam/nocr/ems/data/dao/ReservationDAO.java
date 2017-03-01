package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReservationTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface ReservationDAO extends EmsBaseDAO<ReservationTO> {

    public ReservationTO fetchReservationByPortalReservationId(Long portalReservationId) throws BaseException;

    public Boolean deleteByCardRequest(Long cardRequestId) throws BaseException;
}
