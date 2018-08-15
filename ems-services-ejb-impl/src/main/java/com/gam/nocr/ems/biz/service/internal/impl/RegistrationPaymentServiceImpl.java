package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.CardRequestService;
import com.gam.nocr.ems.biz.service.CitizenService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.RegistrationPaymentDAO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.util.Configuration;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_REGISTRATION_PAYMENT;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * Created by safiary on 12/30/17.
 */

@Stateless(name = "RegistrationPaymentService")
@Local(RegistrationPaymentServiceLocal.class)
@Remote(RegistrationPaymentServiceRemote.class)
public class RegistrationPaymentServiceImpl extends EMSAbstractService
        implements RegistrationPaymentServiceLocal, RegistrationPaymentServiceRemote {


    public RegistrationPaymentTO addRegistrationPayment(RegistrationPaymentTO entity) throws BaseException {

        try {
            return getRegistrationPaymentDAO().create(entity);
        } catch (BaseException e) {
            throw e;
        }
    }

    /**
     * استعلام وجه پرداخت تهاتر نشده
     *
     * @param nationalId
     */
    public Boolean hasCitizenFreePayment(String nationalId) throws BaseException {
        boolean res = false;
        CitizenTO citizenTO;
        RegistrationPaymentTO registrationPaymentTO = null;
        citizenTO = getCitizenService().findByNationalId(nationalId);
        if (citizenTO != null) {
            registrationPaymentTO = getRegistrationPaymentDAO().findByCitizenId(citizenTO.getId());
        }
        if (registrationPaymentTO != null) {
            if (registrationPaymentTO.getMatchFlag() == 0) {
                res = true;
            }
        }
        return res;
    }

    /**
     * استعلام لزوم پرداخت وجه ثبت نام
     *
     * @param nationalId
     */
    public Boolean hasCitizenSuccessfulPayment(String nationalId) throws BaseException {
        boolean res = false;
        if (hasCitizenFreePayment(nationalId)) {
            res = true;
        }
//        TODO
// pardakht koli barai sabtenam khanevadegi
        return res;
    }


    /**
     * ثبت پرداخت
     *
     * @param registrationPaymentTO
     * @param preRegistrationId
     * @param nationalId
     */
    public void savePaymentInfo(RegistrationPaymentTO registrationPaymentTO, String nationalId, long preRegistrationId) throws BaseException {
        try {
            CardRequestTO cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            registrationPaymentTO.setCitizenTO(cardRequestTO.getCitizen());
            RegistrationPaymentTO registrationPayment = getRegistrationPaymentDAO().create(registrationPaymentTO);
            if (registrationPayment != null) {
                cardRequestTO.setRegistrationPaymentTO(registrationPaymentTO);
                cardRequestTO.setPaidDate(registrationPayment.getPaymentDate());
                cardRequestTO.setPaid(registrationPayment.isSucceed());
                getCardRequestService().update(cardRequestTO);
            }
        } catch (BaseException e) {
           throw new ServiceException(BizExceptionCode.EMS_REG_020, BizExceptionCode.EMS_REG_020_MSG, e);
        }
    }

    /**
     * انتساب پرداخت به ثبت نام
     *
     * @param registrationPaymentTO
     * @param nationalId
     */
    public void assignPaymentToEnrollment(
            RegistrationPaymentTO registrationPaymentTO, String nationalId) throws BaseException {
        CardRequestTO cardRequestTO;
        try {
            cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            if (cardRequestTO != null) {
                cardRequestTO.setRegistrationPaymentTO(registrationPaymentTO);
            }
            getCardRequestService().update(cardRequestTO);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * استعلام مبلغ قابل پرداخت
     *
     * @param nationalId
     */
    public Integer getPayAmount(String nationalId) {
        return Integer.valueOf(Configuration.getProperty("payAmount"));
    }


    private RegistrationPaymentDAO getRegistrationPaymentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_REGISTRATION_PAYMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RS_001,
                    BizExceptionCode.GLB_001_MSG, e);
        }
    }

    private CitizenService getCitizenService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CitizenService citizenService;
        try {
            citizenService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CITIZEN), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CITIZEN.split(","));
        }
        citizenService.setUserProfileTO(getUserProfileTO());
        return citizenService;
    }

    private CardRequestService getCardRequestService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestService cardRequestService;
        try {
            cardRequestService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST.split(","));
        }
        cardRequestService.setUserProfileTO(getUserProfileTO());
        return cardRequestService;
    }
}
