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
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.RegistrationPaymentDAO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.data.domain.ws.PaymentInfoWTO;
import com.gam.nocr.ems.util.Configuration;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;

import java.util.Date;

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

    private static final String DEFAULT_PAYMENT_AMOUNT_FIRST_CARD = "200000";

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
     * @param nationalId
     */
    public void savePaymentInfo(RegistrationPaymentTO registrationPaymentTO, String nationalId) throws BaseException {
        try {
            CardRequestTO cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            RegistrationPaymentTO cardRequestPayment = cardRequestTO.getRegistrationPaymentTO();
            if (cardRequestPayment != null) {
                cardRequestPayment.setResCode(registrationPaymentTO.getResCode());
                cardRequestPayment.setRrn(registrationPaymentTO.getRrn());
                cardRequestPayment.setSystemTraceNo(registrationPaymentTO.getSystemTraceNo());
                cardRequestPayment.setDescription(registrationPaymentTO.getDescription());
                cardRequestPayment.setConfirmed(registrationPaymentTO.isConfirmed());
                cardRequestPayment.setSucceed(registrationPaymentTO.isSucceed());
                cardRequestPayment.setAmountPaid(registrationPaymentTO.getAmountPaid());
                cardRequestPayment.setPaidBank(registrationPaymentTO.getPaidBank());
                cardRequestPayment.setPaymentDate(new Date());
                cardRequestTO.setPaidDate(registrationPaymentTO.getPaymentDate());
                cardRequestTO.setPaid(registrationPaymentTO.isSucceed());
                getCardRequestService().update(cardRequestTO);
            } else {
                registrationPaymentTO.setCitizenTO(cardRequestTO.getCitizen());
                registrationPaymentTO.setPaymentDate(new Date());
                RegistrationPaymentTO registrationPayment = getRegistrationPaymentDAO().create(registrationPaymentTO);
                cardRequestTO.setRegistrationPaymentTO(registrationPaymentTO);
                cardRequestTO.setPaidDate(registrationPayment.getPaymentDate());
                cardRequestTO.setPaid(registrationPayment.isSucceed());
                getCardRequestService().update(cardRequestTO);
            }
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RGP_020, BizExceptionCode.RGP_020_MSG, e);
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
                getCardRequestService().update(cardRequestTO);
            }
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * استعلام مبلغ قابل پرداخت
     *
     * @param nationalId
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public PaymentInfoWTO getPayAmountInfo(String nationalId) throws BaseException {
        try {
            RegistrationPaymentTO registrationPaymentTO = getRegistrationPaymentDAO().findLastCardRequestPaymentByNationalId(nationalId);
            if (registrationPaymentTO == null) {
                throw new ServiceException(BizExceptionCode.ISC_010, BizExceptionCode.ISC_011_MSG, new Object[]{nationalId});
            }
            Long orderId = registrationPaymentTO.getOrderId();
            PaymentInfoWTO result = new PaymentInfoWTO();
            // TODO implement dynamic payment amount based on card-request state history
            //first card, delivered, multiple delivered,...
            String paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_FIRST_CARD,
                    DEFAULT_PAYMENT_AMOUNT_FIRST_CARD);
            Integer amount = Integer.valueOf(paymentAmount);
            result.setPaymentAmount(amount);
            result.setOrderId(String.valueOf(orderId));
            result.setPaymentCode(Configuration.getProperty("PAYMENT.CODE"));
            //todo add payment code  & order id

            return result;
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException(BizExceptionCode.RGP_002,
                    BizExceptionCode.RGP_002_MSG, e);
        }
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
