package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.EnrollmentOfficeService;
import com.gam.nocr.ems.biz.service.ProvinceCodeService;
import com.gam.nocr.ems.biz.service.RegistrationPaymentService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.VerhoefPaymentUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "PaymentCodeService")
@Local(PaymentCodeServiceLocal.class)
@Remote(PaymentCodeServiceRemote.class)
public class PaymentCodeServiceImpl extends EMSAbstractService
        implements PaymentCodeServiceLocal, PaymentCodeServiceRemote {

    private static final Logger paymentCodeGeneratorLogger = BaseLog.getLogger("RegistrationPaymentCodeServiceLogger");
    public static final String KEY_CPI_DEPOSIT_ID_DEFAULT_VALUE = "2";
    public static final String KEY_ORGANIZATION_PAYMENT_CODE_DEFAULT_VALUE = "0111";
    public static final String KEY_CPI_INCOME_CODE_DEFAULT_VALUE = "140149";

    @Override
    public String fetchPaymentCode(Long officeId, String serviceCode, String paymentAmount) throws BaseException {

        StringBuilder paymentCode = new StringBuilder();
        String sequence;
        String verhoeffCode;
        String depCode = null;

        try {
            EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeService().findEnrollmentOfficeById(officeId);

            //the value of deposit Id
            paymentCode.append(EmsUtil.getProfileValue(ProfileKeyName.KEY_CPI_DEPOSIT_ID,
                    KEY_CPI_DEPOSIT_ID_DEFAULT_VALUE));

            //the value of organization payment Code
            paymentCode.append(EmsUtil.getProfileValue(ProfileKeyName.KEY_ORGANIZATION_PAYMENT_CODE,
                    KEY_ORGANIZATION_PAYMENT_CODE_DEFAULT_VALUE));

            //the value of province code
            try {
                paymentCode.append(enrollmentOfficeTO.getLocation().getProvinceCodeTO().getProvinceCode().toString());
            } catch (Exception e) {
                paymentCodeGeneratorLogger.error("Error Occurred in province code", e);
                throw new ServiceException(BizExceptionCode.PYC_001, BizExceptionCode.PYC_001_MSG, e);
            }

            //the value of CPI IncomeId
            paymentCode.append(EmsUtil.getProfileValue(ProfileKeyName.KEY_CPI_INCOME_CODE,
                    KEY_CPI_INCOME_CODE_DEFAULT_VALUE));

            //the value of Service Code
            paymentCode.append(serviceCode);

            //the variable that are got from a sequence
            try {
                sequence = getRegistrationPaymentService().generateNewPaymentCode();
                paymentCode.append(sequence);
            } catch (BaseException e) {
                paymentCodeGeneratorLogger.error("Error Occurred in get sequence", e);
                throw new ServiceException(BizExceptionCode.PYC_004, BizExceptionCode.PYC_004_MSG, e);
            }

            //the nocr code of a office
            if (enrollmentOfficeTO != null) {
                if (enrollmentOfficeTO.getCode().length() < 3) {
                    depCode = StringUtils.leftPad(enrollmentOfficeTO.getCode(), 3, "0");
                } else if (enrollmentOfficeTO.getCode().length() == 3) {
                    depCode = enrollmentOfficeTO.getCode();
                } else if (enrollmentOfficeTO.getCode().length() > 3) {
                    depCode = enrollmentOfficeTO.getCode().substring(enrollmentOfficeTO.getCode().length() - 3);
                }
            } else {
                paymentCodeGeneratorLogger.error("Error Occurred in nocr code of a office");
                throw new ServiceException(BizExceptionCode.PYC_003, BizExceptionCode.PYC_003_MSG);
            }
            paymentCode.append(depCode);

            //creating verhoefcode
            try {
                verhoeffCode = VerhoefPaymentUtil.generate(paymentCode.toString(), paymentAmount);
                paymentCode = paymentCode.insert(1, verhoeffCode);
            } catch (Exception e) {
                paymentCodeGeneratorLogger.error("Error Occurred in creating verhoefcode ", e);
                throw new ServiceException(BizExceptionCode.PYC_005, BizExceptionCode.PYC_005_MSG, e);
            }

        } catch (Exception e) {
            if (e instanceof BaseException) {
                throw (BaseException) e;
            }
            paymentCodeGeneratorLogger.error("Error Occurred in generating payment Code", e);
            throw new ServiceException(BizExceptionCode.PYC_002, e.getMessage(), e);
        }
        return paymentCode.toString();
    }


    private EnrollmentOfficeService getEnrollmentOfficeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE), EmsUtil.getUserInfo(userProfileTO));
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
