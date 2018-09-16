package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.ReservationTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface ReservationService extends Service {

    ReservationTO findReservationByCrqId(Long id) throws BaseException;

    CardRequestTO transferReservationsToEMS(ReservationTO reservationTo) throws BaseException;

    ReservationTO create(ReservationTO reservationTO) throws BaseException;
}
