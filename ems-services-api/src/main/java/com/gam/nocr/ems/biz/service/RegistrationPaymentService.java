package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.data.domain.ws.PaymentInfoWTO;
import com.gam.nocr.ems.data.domain.ws.TargetBankWTO;
import com.gam.nocr.ems.data.enums.CardRequestType;

import java.util.Map;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface RegistrationPaymentService extends Service {

    Boolean hasCitizenSuccessfulPayment(String nationalId) throws BaseException;

    PaymentInfoWTO getPayAmountInfo(String nationalId) throws BaseException;

    void savePaymentInfo(RegistrationPaymentTO registrationPaymentTO,
                         String nationalId) throws BaseException;

    void assignPaymentToEnrollment(RegistrationPaymentTO registrationPaymentTO, String nationalId) throws BaseException;

    RegistrationPaymentTO addRegistrationPayment(RegistrationPaymentTO registrationPaymentTO) throws BaseException;

    Map getPaymentAmountAndPaymentCode(CardRequestType cardRequestType, String nationalId, Long id)throws BaseException;

    Boolean bankInquiry(String nationalId) throws BaseException;

    void registerTargetBank(TargetBankWTO targetBankWTO) throws BaseException;

    String generateNewPaymentCode() throws BaseException;
}
