package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.ReservationTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PortalReservationService extends Service {

//    List<Long> fetchReservationIds() throws BaseException;

    /**
     * The method doActivityForReservations is used to transfer the reservations which were done on the sub system 'Profile'
     *
     * @return a list of type {@link ReservationTO}
     * @throws BaseException
     */
//    List<ReservationTO> transferReservations(List<Long> reservationIds) throws BaseException;

    /**
     * The method receivedByEMS is used to inform the sub system 'Portal' about the reservations which are saved by EMS.
     *
     * @param portalReservationIds a list of type {@link Long} which represents reservations which are save by EMS
     * @return true or false
     * @throws BaseException
     */
//    Boolean receivedByEMS(List<Long> portalReservationIds) throws BaseException;
}
