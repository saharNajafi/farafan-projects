package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface RegistrationPaymentService extends Service {

    Boolean hasCitizenSuccessfulPayment(String nationalId) throws BaseException;

    Integer getPayAmount(String nationalId) throws BaseException;

    void savePaymentInfo(RegistrationPaymentTO registrationPaymentTO,
                         String nationalId, long preRegistrationId) throws BaseException;

    void assignPaymentToEnrollment(RegistrationPaymentTO registrationPaymentTO, String nationalId) throws BaseException;

    RegistrationPaymentTO addRegistrationPayment(RegistrationPaymentTO registrationPaymentTO) throws BaseException;
}
