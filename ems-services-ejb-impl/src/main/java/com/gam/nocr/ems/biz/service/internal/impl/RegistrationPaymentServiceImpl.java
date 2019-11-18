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
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiInquiryWTO;
import com.gam.nocr.ems.biz.service.external.impl.BpiInquiryService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.RegistrationPaymentDAO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.data.domain.ws.PaymentInfoWTO;
import com.gam.nocr.ems.data.domain.ws.TargetBankWTO;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.IPGProviderEnum;
import com.gam.nocr.ems.util.Configuration;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private static final String DEFAULT_PAYMENT_AMOUNT_FIRST_REPLICA = "400000";
    private static final String DEFAULT_PAYMENT_AMOUNT_SECOND_REPLICA = "700000";
    private static final String DEFAULT_PAYMENT_AMOUNT_THIRD_REPLICA = "1000000";
    private static final String DEFAULT_KEY_PAYMENT_AMOUNT_REPLACE = "300000";
    private static final String DEFAULT_KEY_PAYMENT_AMOUNT_EXTEND = "200000";

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
                if (registrationPaymentTO.getPaidBank().equals(IPGProviderEnum.SADAD)) {
                    if (registrationPaymentTO.getTerminalId() != null) {
                        cardRequestPayment.setTerminalId(registrationPaymentTO.getTerminalId());
                    }
                    if (registrationPaymentTO.getMerchantId() != null) {
                        cardRequestPayment.setMerchantId(registrationPaymentTO.getMerchantId());
                    }
                }
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
            throw new ServiceException(BizExceptionCode.RGP_014, BizExceptionCode.RGP_014_MSG, e);
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
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException(BizExceptionCode.RGP_005,
                    BizExceptionCode.RGP_005_MSG, e);
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
            PaymentInfoWTO result = new PaymentInfoWTO();
            CardRequestTO cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            RegistrationPaymentTO registrationPaymentTO = cardRequestTO.getRegistrationPaymentTO();
            if (registrationPaymentTO == null) {
                throw new ServiceException(BizExceptionCode.RGP_015, BizExceptionCode.RGP_015_MSG, new Object[]{nationalId});
            }
            // Long orderId = cardRequestTO.getRegistrationPaymentTO().getOrderId();
            //implement dynamic payment amount based on card-request state history
            //first card, delivered, multiple delivered,...
            Map<String, String> registrationPaymentResult =
                    getPaymentAmountAndPaymentCode(cardRequestTO.getType(), nationalId);
            result.setPaymentAmount(Integer.valueOf(registrationPaymentResult.get("paymentAmount")));
            result.setOrderId(String.valueOf(registrationPaymentTO.getOrderId()));
            result.setPaymentCode(registrationPaymentResult.get("paymentCode"));
            return result;
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException(BizExceptionCode.RGP_002,
                    BizExceptionCode.RGP_002_MSG, e);
        }
    }

    public Map<String, String> getPaymentAmountAndPaymentCode(CardRequestType cardRequestType, String nationalId) throws BaseException {
        String paymentAmount = null;
        String paymentCode = null;
        Map map = new HashMap<String, String>();
        if (cardRequestType.equals(CardRequestType.REPLICA)) {
            Long replicaTypeCount = 0L;
            try {
                replicaTypeCount =
                        getCardRequestService().countCardRequestByNationalIdAndType(nationalId, cardRequestType);
            } catch (BaseException e) {
                throw e;
            }
            if (replicaTypeCount == 0) {
                paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_FIRST_REPLICA,
                        DEFAULT_PAYMENT_AMOUNT_FIRST_REPLICA);
                paymentCode = Configuration.getProperty("PAYMENT.FIRST.REPLICA.CODE");
            }
            if (replicaTypeCount == 1) {
                paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_SECOND_REPLICA,
                        DEFAULT_PAYMENT_AMOUNT_SECOND_REPLICA);
                paymentCode = Configuration.getProperty("PAYMENT.SECOND.REPLICA.CODE");
            }
            if (replicaTypeCount >= 2) {
                paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_THIRD_REPLICA,
                        DEFAULT_PAYMENT_AMOUNT_THIRD_REPLICA);
                paymentCode = Configuration.getProperty("PAYMENT.THIRD.REPLICA.CODE");
            }
        } else if (cardRequestType.equals(CardRequestType.FIRST_CARD)) {
            paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_FIRST_CARD,
                    DEFAULT_PAYMENT_AMOUNT_FIRST_CARD);
            paymentCode = Configuration.getProperty("PAYMENT.FIRST.CARD.CODE");
        } else if (cardRequestType.equals(CardRequestType.REPLACE)) {
            paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_REPLACE,
                    DEFAULT_KEY_PAYMENT_AMOUNT_REPLACE);
            paymentCode = Configuration.getProperty("PAYMENT.REPLACE.CODE");
        } else if (cardRequestType.equals(CardRequestType.EXTEND)) {
            paymentAmount = EmsUtil.getProfileValue(ProfileKeyName.KEY_PAYMENT_AMOUNT_EXTEND,
                    DEFAULT_KEY_PAYMENT_AMOUNT_EXTEND);
            //todo:should change to extend payment code (Namjoofar).
            paymentCode = Configuration.getProperty("PAYMENT.FIRST.CARD.CODE");
        }
        map.put("paymentAmount", paymentAmount);
        map.put("paymentCode", paymentCode);
        return map;
    }

    /**
     * انتساب بانک به پرداخت
     *
     * @param targetBankWTO
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void registerTargetBank(TargetBankWTO targetBankWTO) throws BaseException {
        CardRequestTO cardRequestTO;
        try {
            if (targetBankWTO.getPaidBank().equals(IPGProviderEnum.UNDEFINED))
                throw new ServiceException(BizExceptionCode.RGP_006, BizExceptionCode.RGP_006_MSG
                        , new Object[]{targetBankWTO.getNationalId()});
            cardRequestTO =
                    getCardRequestService().findLastRequestByNationalId(targetBankWTO.getNationalId());
            if (cardRequestTO.getRegistrationPaymentTO() == null)
                throw new ServiceException(BizExceptionCode.RGP_007, BizExceptionCode.ISC_011_MSG
                        , new Object[]{targetBankWTO.getNationalId()});

            RegistrationPaymentTO registrationPaymentTO = cardRequestTO.getRegistrationPaymentTO();
            registrationPaymentTO.setPaidBank(targetBankWTO.getPaidBank());
            getRegistrationPaymentDAO().update(registrationPaymentTO);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RGP_004,
                    BizExceptionCode.RGP_004_MSG, e);
        }
    }

    public Boolean bankInquiry(String nationalId) throws BaseException {
        CardRequestTO cardRequestTO;
        BpiInquiryWTO bpiInquiryWTO;
        RegistrationPaymentTO registrationPaymentTO;
        Boolean result = false;
        try {
            cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            if (cardRequestTO.getRegistrationPaymentTO() == null)
                throw new ServiceException(
                        BizExceptionCode.RGP_008, BizExceptionCode.ISC_011_MSG, new Object[]{nationalId});

            registrationPaymentTO = cardRequestTO.getRegistrationPaymentTO();
            if (registrationPaymentTO.getPaidBank().equals(IPGProviderEnum.UNDEFINED))
                throw new ServiceException(
                        BizExceptionCode.RGP_009, BizExceptionCode.RGP_006_MSG, new Object[]{nationalId});
            if (registrationPaymentTO.getPaidBank().equals(IPGProviderEnum.SADAD)) {
                bpiInquiryWTO = getBpiInquiryService().bpiInquiry(registrationPaymentTO);
                if (bpiInquiryWTO != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000000");
                    Date paidDate = df.parse(bpiInquiryWTO.getPaidDate().substring(0, 10) + " " + "00:00:00.000000");
                    registrationPaymentTO.setConfirmed(true);
                    registrationPaymentTO.setSucceed(true);
                    registrationPaymentTO.setResCode("0");
                    registrationPaymentTO.setPaymentDate(paidDate);
                    registrationPaymentTO.setRrn(bpiInquiryWTO.getRrn() != null ? bpiInquiryWTO.getRrn() : "");
                    registrationPaymentTO.setSystemTraceNo(bpiInquiryWTO.getSystemTraceNo() != null ? bpiInquiryWTO.getSystemTraceNo() : "");
                    cardRequestTO.setPaid(true);
                    cardRequestTO.setPaidDate(paidDate);
                    getCardRequestService().update(cardRequestTO);
                    getRegistrationPaymentDAO().update(registrationPaymentTO);
                    result = true;
                }
            }
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException(BizExceptionCode.RGP_003,
                    BizExceptionCode.RGP_003_MSG, e);
        }
        return result;
    }

    private RegistrationPaymentDAO getRegistrationPaymentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_REGISTRATION_PAYMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RGP_013,
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
            throw new ServiceException(BizExceptionCode.RGP_010,
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
            throw new ServiceException(BizExceptionCode.RGP_011,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST.split(","));
        }
        cardRequestService.setUserProfileTO(getUserProfileTO());
        return cardRequestService;
    }

    private BpiInquiryService getBpiInquiryService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        BpiInquiryService bpiInquiryService;
        try {
            bpiInquiryService = serviceFactory.getService(EMSLogicalNames
                    .getExternalServiceJNDIName(EMSLogicalNames.SRV_BPI), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RGP_012,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BPI.split(","));
        }
        return bpiInquiryService;
    }
}
