package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.RegistrationPaymentService;
import com.gam.nocr.ems.biz.service.ReservationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public class ReservationDelegator implements Delegator {

    private ReservationService getService(UserProfileTO userProfileTO) throws BaseException {
        ReservationService ReservationService;
        try {
            ReservationService = ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(
                            EMSLogicalNames.SRV_RESERVATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.ODL_001, BizExceptionCode.GLB_002_MSG,
                    e, EMSLogicalNames.SRV_RESERVATION.split(","));
        }
        ReservationService.setUserProfileTO(userProfileTO);
        return ReservationService;
    }

    public Long transferReservationsToEMS(UserProfileTO userProfileTO, ReservationTO reservationTo) throws BaseException {
        return getService(userProfileTO).transferReservationsToEMS(reservationTo);
    }
}
