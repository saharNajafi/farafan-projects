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
import com.gam.nocr.ems.biz.service.ProvinceCodeService;
import com.gam.nocr.ems.biz.service.RegistrationPaymentService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.ProvinceCodeTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.Verhoeff;
import com.gam.nocr.ems.util.VerhoeffForPaymentCode;

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
    public String fetchPaymentCode(Long superiorOffice, String serviceType) throws BaseException {
        String CPIDepositId;
        String organizationPaymentCode;
        String CPIIncomeId;
        String sequence;
        String provinceCode = null;
        String verhoeffCode;
        String paymentCode;
        try {
            CPIDepositId =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_CPI_DEPOSIT_ID,
                            KEY_CPI_DEPOSIT_ID_DEFAULT_VALUE);
            organizationPaymentCode =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_ORGANIZATION_PAYMENT_CODE,
                            KEY_ORGANIZATION_PAYMENT_CODE_DEFAULT_VALUE);
            ProvinceCodeTO provinceCodeTO =
                    getProvinceCodeService().findByEnrollmentOfficeId(superiorOffice);
            if (provinceCodeTO != null) {
                provinceCode =
                        String.valueOf(provinceCodeTO.getProvinceCode());
            } else {
                throw new ServiceException(BizExceptionCode.PYC_001, BizExceptionCode.PYC_001_MSG);
            }
            CPIIncomeId =
                    EmsUtil.getProfileValue(
                            ProfileKeyName.KEY_CPI_INCOME_CODE,
                            KEY_CPI_INCOME_CODE_DEFAULT_VALUE);
            sequence =
                    getRegistrationPaymentService().generateNewPaymentCode();
            String verhoeffNum =
                    CPIDepositId + organizationPaymentCode + provinceCode
                    + CPIIncomeId + serviceType + sequence + superiorOffice;
            verhoeffCode =
                    VerhoeffForPaymentCode.generate(verhoeffNum);
            paymentCode =
                    CPIDepositId + verhoeffCode + organizationPaymentCode + provinceCode
                    + CPIIncomeId + serviceType + sequence + superiorOffice;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PYC_002, e.getMessage(), e);
        }
        return paymentCode;
    }

    private EnrollmentOfficeService getEnrollmentOfficeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = serviceFactory.getService(EMSLogicalNames
                            .getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE)
                    , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PYC_003,
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
                            .getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION_PAYMENT)
                    , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PYC_004,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_REGISTRATION_PAYMENT.split(","));
        }
        registrationPaymentService.setUserProfileTO(getUserProfileTO());
        return registrationPaymentService;
    }

    private ProvinceCodeService getProvinceCodeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        ProvinceCodeService provinceCodeService;
        try {
            provinceCodeService = serviceFactory.getService(EMSLogicalNames
                            .getServiceJNDIName(EMSLogicalNames.SRV_PROVINCE_CODE)
                    , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PYC_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_PROVINCE_CODE.split(","));
        }
        provinceCodeService.setUserProfileTO(getUserProfileTO());
        return provinceCodeService;
    }
}
