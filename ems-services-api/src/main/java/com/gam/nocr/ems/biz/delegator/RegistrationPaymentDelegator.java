package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.RegistrationPaymentService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public class RegistrationPaymentDelegator implements Delegator {

    private RegistrationPaymentService getService(UserProfileTO userProfileTO) throws BaseException {
        RegistrationPaymentService registrationPaymentService;
        try {
            registrationPaymentService = ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(
                            EMSLogicalNames.SRV_REGISTRATION_PAYMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.ODL_001, BizExceptionCode.GLB_002_MSG,
                    e, EMSLogicalNames.SRV_REGISTRATION_PAYMENT.split(","));
        }
        registrationPaymentService.setUserProfileTO(userProfileTO);
        return registrationPaymentService;
    }

    public Boolean hasCitizenSuccessfulPayment(UserProfileTO userProfileTO, String nationalId) throws BaseException {
        return getService(userProfileTO).hasCitizenSuccessfulPayment(nationalId);
    }

    public Integer getPayAmount(UserProfileTO userProfileTO, String nationalId) throws BaseException {
        return getService(userProfileTO).getPayAmount(nationalId);
    }

    public void savePaymentInfo(UserProfileTO userProfileTO, RegistrationPaymentTO registrationPaymentTO,
                                String nationalId, long preRegistrationId) throws BaseException {
        getService(userProfileTO).savePaymentInfo(registrationPaymentTO, nationalId, preRegistrationId);
    }

    public void assignPaymentToEnrollment(UserProfileTO userProfileTO,
                                          RegistrationPaymentTO registrationPaymentTO, String nationalId) throws BaseException {
        getService(userProfileTO).assignPaymentToEnrollment(registrationPaymentTO, nationalId);

    }
}
