package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.data.enums.Estelam2FlagType;
import com.gam.nocr.ems.data.util.PaymentUtil;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.NationalIDUtil;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;

/**
 * Created by Sahar Najafi on 8/7/18.
 */

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "CCOSPaymentWS", portName = "CCOSPaymentPort",
        targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class CCOSPaymentWS extends EMSWS {

    static final Logger ccosLogger = BaseLog.getLogger("CCOSPaymentWSLogger");

    private CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
    private EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
    private InternalServiceCheckerDelegator internalServiceCheckerDelegator = new InternalServiceCheckerDelegator();
    private RegistrationPaymentDelegator registrationPaymentDelegator = new RegistrationPaymentDelegator();
    private ReservationDelegator reservationDelegator = new ReservationDelegator();

    /**
     * استعلام امکان درج ثبت نام تک مرحله ای
     */
    @WebMethod
    public void checkInsertSingleStageEnrollmentPossible(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfoWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonalInfoWTO personalInfoWTO
    ) throws InternalException {
        if (personalInfoWTO == null) {
            throwInternalException(WebExceptionCode.CPW_019, WebExceptionCode.CPW_019_MSG, ccosLogger);
        }
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            cardRequestDelegator.checkInsertSingleStageEnrollmentPossible(userProfileTO,
                    personalInfoWTO.getNationalId(), personalInfoWTO.getBirthDateSolar()
                    , personalInfoWTO.getCertSerialNo(), personalInfoWTO.getGender());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_001_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_001), e);
        }
    }

    /**
     * استعلام مجاز بودن دفتر برای ثبت نام تک مرحله ای
     *
     * @param personalHealthStatusWTO
     */
    @WebMethod
    public void checkEnrollmentOfficeEligibleForSingleStageEnrollment(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalHealthStatusWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonalHealthStatusWTO personalHealthStatusWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            enrollmentOfficeDelegator.checkEnrollmentOfficeEligibleForSingleStageEnrollment(
                    userProfileTO, personalHealthStatusWTO.getNationalId()
                    , personalHealthStatusWTO.getHealthStatusWTO()
                    , personalHealthStatusWTO.getEnrollmentOfficeId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_002_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_002), e);
        }
    }


    /**
     * انتقال پیش ثبت نام
     *
     * @param singlePreRegistrationWTO
     */
    @WebMethod
    public SingleStagePreRegistrationWTO transferSingleStagePreRegistrationToEms(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "singlePreRegistrationWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) SinglePreRegistrationWTO singlePreRegistrationWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            ReservationTO reservationTo;
            reservationTo = PaymentUtil.convertSingle(singlePreRegistrationWTO);
            reservationTo.getCardRequest().setTrackingID(cardRequestDelegator.generateNewTrackingId(userProfileTO));
            CardRequestTO cardRequestTO = null;
            try {
                cardRequestTO = reservationDelegator.transferReservationsToEMS(userProfileTO, reservationTo);
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.CPW_009_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_009), e);
            }
            SingleStagePreRegistrationWTO singleStagePreRegistrationWTO = new SingleStagePreRegistrationWTO();
            singleStagePreRegistrationWTO.setTrackingId(reservationTo.getCardRequest().getTrackingID());
            Boolean verifiedByIMS = Boolean.FALSE;
            if (reservationTo.getCardRequest().
                    getEstelam2Flag() != null && reservationTo.getCardRequest().
                    getEstelam2Flag().equals(Estelam2FlagType.V)) {
                verifiedByIMS = Boolean.TRUE;
            }
            singleStagePreRegistrationWTO.setCardRequestId(cardRequestTO.getId());
            singleStagePreRegistrationWTO.setVerifiedByIMS(verifiedByIMS);
            return singleStagePreRegistrationWTO;
        } catch (InternalException internalException) {
            ccosLogger.error(internalException.getMessage(), internalException.getFaultInfo()
                    .getCode(), internalException);
            throw internalException;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_013_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_013), e);
        }
    }

    /**
     * استعلام لزوم پرداخت وجه ثبت نام
     *
     * @param personalInfoWTO
     * @return
     * @throws BaseException
     */
    /*@WebMethod
    public Boolean hasCitizenSuccessfulPayment(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfoWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonalInfoWTO personalInfoWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
            return registrationPaymentDelegator.hasCitizenSuccessfulPayment(userProfileTO, personalInfoWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_012_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_012), e);
        }
    }*/

    /**
     * استعلام مبلغ قابل پرداخت
     *
     * @param personalInfoWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public PaymentInfoWTO getPayAmount(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfoWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonalInfoWTO personalInfoWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            return registrationPaymentDelegator.getPayAmountInfo(userProfileTO, personalInfoWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_011_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_011), e);
        }
    }

    /**
     * انتساب بانک به پرداخت
     *
     * @param targetBankWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public void registerTargetBank(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "targetBankWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) TargetBankWTO targetBankWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            if (targetBankWTO == null || targetBankWTO.getPaidBank() == null || targetBankWTO.getNationalId() == null)
                throwInternalException(WebExceptionCode.CPW_024, WebExceptionCode.CPW_024_MSG, ccosLogger);
            registrationPaymentDelegator.registerTargetBank(userProfileTO, targetBankWTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_023_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_023), e);
        }
    }

    /**
     * ثبت پرداخت
     *
     * @param paymentWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public void savePaymentInfo(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "paymentWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PaymentWTO paymentWTO
    ) throws InternalException, BaseException {

        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        if (paymentWTO == null || paymentWTO.getRegistrationPaymentWTO() == null) {
            throw new InternalException(WebExceptionCode.CPW_008_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_008));
        }
        try {
            RegistrationPaymentTO registrationPaymentTO = PaymentUtil.convertToRegistrationPayment(paymentWTO);
            registrationPaymentDelegator.savePaymentInfo(userProfileTO, registrationPaymentTO,
                    paymentWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_010_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_010), e);
        }
    }


    /**
     * استعلام از IMS
     *
     * @param personEnquiry
     */
    @WebMethod
    public void imsInquiry(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personEnquiryWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonEnquiryWTO personEnquiry
    ) throws InternalException, BaseException {
        if (personEnquiry == null) {
            throw new InternalException(WebExceptionCode.CPW_020_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_020));
        }
        if (!NationalIDUtil.checkValidNinStr(personEnquiry.getNationalId())) {
            throw new InternalException(WebExceptionCode.CPW_003_MSG + personEnquiry.getNationalId(), new EMSWebServiceFault(WebExceptionCode.CPW_003));
        }
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        // fetch card request
        CardRequestTO cardRequest = null;
        try {
            cardRequest = cardRequestDelegator.findLastRequestByNationalId(userProfileTO, personEnquiry.getNationalId());
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_018_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_018), e);
        }
        if (cardRequest == null) {
            throw new InternalException(WebExceptionCode.CPW_005_MSG + personEnquiry.getNationalId(), new EMSWebServiceFault(WebExceptionCode.CPW_005));
        }
        PersonEnquiryWTO personEnquiryWTO = null;
        try {
            personEnquiryWTO = cardRequestDelegator.updateCitizenByEstelam(userProfileTO, cardRequest, true, false);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_006_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_006), e);
        }
        if (personEnquiryWTO.getIsServiceDown()
                || personEnquiryWTO.getIsEstelamCorrupt()) {
            throw new InternalException(WebExceptionCode.CPW_007_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_007));
        }
    }

    /**
     * درخواست شناسه پرداخت موجود
     *
     * @param preRegistrationWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public String retrievePaymentUId(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "preRegistrationWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PreRegistrationWTO preRegistrationWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        try {
            CardRequestTO cardRequestTO =
                    internalServiceCheckerDelegator.inquiryHasCardRequest(
                            userProfileTO, String.valueOf(preRegistrationWTO.getNationalId()));
            return EmsUtil.makeFixLengthWithZeroPadding(cardRequestTO.getCitizen().getNationalID(), 30);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_014_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_014), e);
        }
    }

    /**
     * انتساب پرداخت به ثبت نام
     *
     * @param paymentWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public void assignPaymentToEnrollment(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "paymentWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PaymentWTO paymentWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        if (paymentWTO == null || paymentWTO.getRegistrationPaymentWTO() == null) {
            throwInternalException(WebExceptionCode.CPW_008, WebExceptionCode.CPW_008_MSG, ccosLogger);
        }
        try {
            RegistrationPaymentTO registrationPaymentTO = PaymentUtil.convertToRegistrationPayment(paymentWTO);
            registrationPaymentDelegator.assignPaymentToEnrollment(
                    userProfileTO, registrationPaymentTO, paymentWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_015_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_015), e);
        }
    }

    /**
     * استعلام Bpi
     *
     * @param nationalId
     * @return
     * @throws BaseException
     */
    @WebMethod
    public Boolean bankInquiry(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "nationalId", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String nationalId
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateCCOSUser(securityContextWTO, ccosLogger);
        Boolean result = false;
        if (nationalId == null) {
            throw new InternalException(WebExceptionCode.CPW_021_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_021));
        }
        try {
            result = registrationPaymentDelegator.bankInquiry(userProfileTO, nationalId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CPW_022_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_022), e);
        }
        return result;
    }
}
