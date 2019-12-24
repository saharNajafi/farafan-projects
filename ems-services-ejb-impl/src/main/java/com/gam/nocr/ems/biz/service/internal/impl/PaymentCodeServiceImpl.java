package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.EnrollmentOfficeService;
import com.gam.nocr.ems.biz.service.RegistrationPaymentService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.Verhoeff;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "PaymentCodeService")
@Local(PaymentCodeServiceLocal.class)
@Remote(PaymentCodeServiceRemote.class)
public class PaymentCodeServiceImpl extends EMSAbstractService
        implements PaymentCodeServiceLocal, PaymentCodeServiceRemote {

    public final static String KEY_CPI_DEPOSIT_ID_DEFAULT_VALUE = "2";
    public final static String KEY_ORGANIZATION_PAYMENT_CODE_DEFAULT_VALUE = "0111";
    public final static String KEY_CPI_INCOME_CODE_DEFAULT_VALUE = "140149";


    @Override
    public String fetchPaymentCode(Long eofId, String code) throws BaseException {
        String  CPIDepositId;
        String  organizationPaymentCode;
        String  CPIIncomeId;
        String superiorOffice;
        String sequence;
        try {
        // 1
        CPIDepositId =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_CPI_DEPOSIT_ID,
                            KEY_CPI_DEPOSIT_ID_DEFAULT_VALUE);

//       2-3    Verhoeff.generateVerhoeff()
            // 4-7
            organizationPaymentCode =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_ORGANIZATION_PAYMENT_CODE,
                            KEY_ORGANIZATION_PAYMENT_CODE_DEFAULT_VALUE);
            //8-9 code ostan
            //10-15
            CPIIncomeId =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_CPI_INCOME_CODE,
                            KEY_CPI_INCOME_CODE_DEFAULT_VALUE);
            //16-17 code
            //18-27
            sequence = getRegistrationPaymentService().generateNewPaymentCode();
            //28-30
            superiorOffice = String.valueOf(getEnrollmentOfficeService().getSuperiorOffice(eofId).getId());


        } catch (Exception e) {
//            logger.warn(BizExceptionCode.EOS_066, BizExceptionCode.EOS_066_MSG);
            throw new ServiceException(BizExceptionCode.PYC_001, e.getMessage(), e);
        }
        return null;
    }

    private EnrollmentOfficeService getEnrollmentOfficeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PYC_002,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_ENROLLMENT_OFFICE.split(","));
        }
        enrollmentOfficeService.setUserProfileTO(getUserProfileTO());
        return enrollmentOfficeService;
    }

    private RegistrationPaymentService getRegistrationPaymentService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        RegistrationPaymentService registrationPaymentService;
        try {
            registrationPaymentService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION_PAYMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PYC_003,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_REGISTRATION_PAYMENT.split(","));
        }
        registrationPaymentService.setUserProfileTO(getUserProfileTO());
        return registrationPaymentService;
    }
}
