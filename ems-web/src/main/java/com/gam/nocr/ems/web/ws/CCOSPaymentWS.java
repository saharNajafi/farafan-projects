package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.*;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.Estelam2FlagType;
import com.gam.nocr.ems.data.enums.GenderEnum;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.*;
import gampooya.tools.date.DateFormatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
    private static final Logger logger = BaseLog.getLogger(CardWS.class);
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
            @WebParam(name = "personalInfo", targetNamespace = "")
            @XmlElement(required = true, nillable = false) PersonalInfoWTO personalInfo
    ) throws InternalException, BaseException {
        if (personalInfo == null) {
            throw new InternalException(WebExceptionCode.GLB_005_MSG, new EMSWebServiceFault(WebExceptionCode.GLB_005));
        }
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
            cardRequestDelegator.checkInsertSingleStageEnrollmentPossible(userProfileTO,
                    personalInfo.getNationalId(), personalInfo.getBirthDateSolar()
                    , personalInfo.getCertSerialNo(), personalInfo.getGender());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
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
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
            enrollmentOfficeDelegator.checkEnrollmentOfficeEligibleForSingleStageEnrollment(
                    userProfileTO, personalHealthStatusWTO.getNationalId()
                    , personalHealthStatusWTO.getHealthStatusWTO()
                    , personalHealthStatusWTO.getEnrollmentOfficeId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
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

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        ReservationTO reservationTo;
        reservationTo = convertSingle(singlePreRegistrationWTO);
        Long emsPortalRequestId;
        try {
            emsPortalRequestId = reservationDelegator.transferReservationsToEMS(userProfileTO, reservationTo);
        } catch (Exception ex) {
            throw new InternalException(WebExceptionCode.CPW_009_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_009), ex);
        }
        SingleStagePreRegistrationWTO singleStagePreRegistrationWTO = new SingleStagePreRegistrationWTO();
        singleStagePreRegistrationWTO.setOrderId(reservationTo.getCardRequest().getRegistrationPaymentTO().getOrderId());
        singleStagePreRegistrationWTO.setTrackingId(reservationTo.getCardRequest().getTrackingID());
        singleStagePreRegistrationWTO.setPaymentCode(reservationTo.getCardRequest().getRegistrationPaymentTO().getPaymentCode());
        singleStagePreRegistrationWTO.setPortalRequestId(emsPortalRequestId);
        Boolean verifiedByIMS = Boolean.FALSE;
        if (reservationTo.getCardRequest().
                getEstelam2Flag() != null && reservationTo.getCardRequest().
                getEstelam2Flag().equals(Estelam2FlagType.V)) {
            verifiedByIMS = Boolean.TRUE;
        }
        singleStagePreRegistrationWTO.setVerifiedByIMS(verifiedByIMS);
        return singleStagePreRegistrationWTO;
    }

    /**
     * استعلام لزوم پرداخت وجه ثبت نام
     *
     * @param personalInfoWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public Boolean hasCitizenSuccessfulPayment(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfoWTO", targetNamespace = "")
            @XmlElement(name = "personalInfoWTO") PersonalInfoWTO personalInfoWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        return registrationPaymentDelegator.hasCitizenSuccessfulPayment(userProfileTO, personalInfoWTO.getNationalId());
    }

    /**
     * استعلام مبلغ قابل پرداخت
     *
     * @param personalInfoWTO
     * @return
     * @throws BaseException
     */
    @WebMethod
    public Integer getPayAmount(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfoWTO", targetNamespace = "")
            @XmlElement(name = "personalInfoWTO") PersonalInfoWTO personalInfoWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        return registrationPaymentDelegator.getPayAmount(userProfileTO, personalInfoWTO.getNationalId());
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
            @XmlElement(name = "paymentWTO") PaymentWTO paymentWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        RegistrationPaymentTO registrationPaymentTO = convertToRegistrationPayment(paymentWTO);
        registrationPaymentDelegator.savePaymentInfo(userProfileTO, registrationPaymentTO,
                paymentWTO.getNationalId(), paymentWTO.getPreRegistrationId());
    }

    /**
     * استعلام از IMS
     *
     * @param personalInfo
     */
    @WebMethod
    public void imsInquiry(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "personalInfo", targetNamespace = "")
            @XmlElement(name = "personalInfo") PersonEnquiryWTO personalInfo
    ) throws InternalException, BaseException {
        if (personalInfo == null) {
            throw new InternalException(WebExceptionCode.GLB_001_MSG, new EMSWebServiceFault(WebExceptionCode.GLB_001));
        }
        if (!NationalIDUtil.checkValidNinStr(personalInfo.getNationalId())) {
            throw new InternalException((WebExceptionCode.CPW_003_MSG + personalInfo.getNationalId()),
                    new EMSWebServiceFault(WebExceptionCode.CPW_003));
        }
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        // fetch card request
        CardRequestTO cardRequest = cardRequestDelegator.findLastRequestByNationalId(userProfileTO, personalInfo.getNationalId());
        if (cardRequest == null) {
            throw new InternalException(WebExceptionCode.CPW_005_MSG + personalInfo.getNationalId(),
                    new EMSWebServiceFault(WebExceptionCode.CPW_005));
        }
        PersonEnquiryWTO personEnquiryWTO = null;
        try {
            personEnquiryWTO = cardRequestDelegator.updateCitizenByEstelam(userProfileTO, cardRequest, true, false);
        } catch (Exception ex) {
            throw new InternalException(
                    WebExceptionCode.CPW_006_MSG,
                    new EMSWebServiceFault(WebExceptionCode.CPW_006));
        }
        if (personEnquiryWTO.getIsServiceDown()
                || personEnquiryWTO.getIsEstelamCorrupt()) {
            throw new InternalException(WebExceptionCode.CPW_007_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_007));
        }
    }

    /**
     * درخواست شناسه پرداخت موجود
     *
     * @param preRegistration
     * @return
     * @throws BaseException
     */
    @WebMethod
    public String retrievePaymentUId(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "preRegistration", targetNamespace = "")
            @XmlElement(name = "preRegistration") PreRegistrationWTO preRegistration
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        CardRequestTO cardRequestTO =
                internalServiceCheckerDelegator.inquiryHasCardRequest(
                        userProfileTO, String.valueOf(preRegistration.getNationalId()));
        return EmsUtil.makeFixLengthWithZeroPadding(cardRequestTO.getCitizen().getNationalID(), 30);
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
            @XmlElement(name = "paymentWTO") PaymentWTO paymentWTO
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        RegistrationPaymentTO registrationPaymentTO = convertToRegistrationPayment(paymentWTO);
        registrationPaymentDelegator.assignPaymentToEnrollment(
                userProfileTO, registrationPaymentTO, paymentWTO.getNationalId());
    }


    private RegistrationPaymentTO convertToRegistrationPayment(
            PaymentWTO paymentWTO) throws InternalException, BaseException {
        if (paymentWTO == null || paymentWTO.getRegistrationPaymentWTO() == null) {
            throw new InternalException(WebExceptionCode.CPW_008_MSG, new EMSWebServiceFault(WebExceptionCode.CPW_008));
        }
        RegistrationPaymentTO registrationPaymentTO = new RegistrationPaymentTO();
        RegistrationPaymentWTO registrationPaymentWTO = paymentWTO.getRegistrationPaymentWTO();
        registrationPaymentTO.setAmountPaid(registrationPaymentWTO.getAmountPaied());
        registrationPaymentTO.setConfirmed(registrationPaymentWTO.isConfirmed());
        registrationPaymentTO.setSucceed(registrationPaymentWTO.isSucceed());
        registrationPaymentTO.setOrderId(registrationPaymentWTO.getOrderId());
        registrationPaymentTO.setPaymentDate(registrationPaymentWTO.getPaymentDate());
        registrationPaymentTO.setPaymentCode(registrationPaymentWTO.getPaymentCode());
        registrationPaymentTO.setPaidBank(registrationPaymentWTO.getPaiedBank());
        registrationPaymentTO.setSystemTraceNo(registrationPaymentWTO.getSystemTraceNo());
        registrationPaymentTO.setResCode(registrationPaymentWTO.getResCode());
        registrationPaymentTO.setRrn(registrationPaymentWTO.getRRN());
        registrationPaymentTO.setDescription(registrationPaymentWTO.getDescription());
        registrationPaymentTO.setMatchFlag((short) 1);
        return registrationPaymentTO;
    }

    public ReservationTO convertSingle(SinglePreRegistrationWTO singlePreRegistrationWTO) throws BaseException {
        try {
            checkSinglePreRegistrationTransfer(singlePreRegistrationWTO);
            ReservationTO reservationTO = new ReservationTO();
            CardRequestTO cardRequestTO = new CardRequestTO();
            CitizenTO ctz = new CitizenTO();
            CitizenInfoTO czi = new CitizenInfoTO();
            String doesNotExist = Configuration.getProperty("dont.exit");
            ctz.setFirstNamePersian(doesNotExist);
            ctz.setSurnamePersian(doesNotExist);
            ctz.setNationalID(StringUtils.leftPad(singlePreRegistrationWTO.getNationalId(), 10, "0"));
            cardRequestTO.setPortalEnrolledDate(new Date());
            cardRequestTO.setTrackingID(NationalIDUtil.generateTrackingId(ctz.getNationalID()));
            cardRequestTO.setOrigin(CardRequestOrigin.valueOf(singlePreRegistrationWTO.getOrigin()));
            cardRequestTO.setPaid(Boolean.FALSE);
            String notValue = Configuration.getProperty("not.value");
            czi.setFirstNameEnglish(notValue);
            czi.setSurnameEnglish(notValue);
            czi.setBirthCertificateId(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()));
            czi.setBirthDateGregorian(singlePreRegistrationWTO.getBirthDateGregorian());
            czi.setBirthDateSolar(CalendarUtil.addSlashToPersianDate(String.valueOf(singlePreRegistrationWTO.getBirthDateSolar())));
            czi.setBirthDateLunar(singlePreRegistrationWTO.getBirthDateLunar());
            try {
                czi.setGender(GenderEnum.getEMSGender(singlePreRegistrationWTO.getGender()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(MapperExceptionCode.CRM_005, MapperExceptionCode.CRM_005_MSG, e, new String[]{singlePreRegistrationWTO.getGender().toString()});
            }
            czi.setReligion(new ReligionTO(Long.valueOf(singlePreRegistrationWTO.getReligion().getCode())));
            czi.setMobile(singlePreRegistrationWTO.getCellphoneNumber());
            czi.setBirthCertificateSeries(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()));
            try {
                czi.setFatherBirthDateSolar(gampooya.tools.date.DateUtil.convert(ConstValues.DEFAULT_DATE, gampooya.tools.date.DateUtil.JALALI));
            } catch (DateFormatException e) {
                throw new BaseException(MapperExceptionCode.CRM_006, MapperExceptionCode.GLB_001_MSG, e);
            }
            czi.setFatherNationalID("0000000000");
            czi.setMotherNationalID("0000000000");
            czi.setMotherFirstNamePersian(singlePreRegistrationWTO.getMotherName());
            try {
                czi.setMotherBirthDateSolar(gampooya.tools.date.DateUtil.convert(ConstValues.DEFAULT_DATE, gampooya.tools.date.DateUtil.JALALI));
            } catch (DateFormatException e) {
                throw new BaseException(MapperExceptionCode.CRM_007, MapperExceptionCode.GLB_001_MSG, e);
            }
            czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);

            if (singlePreRegistrationWTO.getRegistrationPaymentWTO() != null) {
                RegistrationPaymentTO registrationPaymentTO = new RegistrationPaymentTO();
                RegistrationPaymentWTO registrationPaymentWTO = singlePreRegistrationWTO.getRegistrationPaymentWTO();
                registrationPaymentTO.setAmountPaid(registrationPaymentWTO.getAmountPaied());
                registrationPaymentTO.setDescription(registrationPaymentWTO.getDescription());
                registrationPaymentTO.setConfirmed(registrationPaymentWTO.isConfirmed());
                registrationPaymentTO.setSucceed(registrationPaymentWTO.isSucceed());
                registrationPaymentTO.setOrderId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
                registrationPaymentTO.setPaymentCode(Configuration.getProperty("PAYMENT.CODE"));
                registrationPaymentTO.setPaidBank(registrationPaymentWTO.getPaiedBank());
                registrationPaymentTO.setResCode(registrationPaymentWTO.getResCode());
                registrationPaymentTO.setSystemTraceNo(registrationPaymentWTO.getSystemTraceNo());
                registrationPaymentTO.setMatchFlag((short) 1);
                cardRequestTO.setRegistrationPaymentTO(registrationPaymentTO);
                reservationTO.setPaidDate(registrationPaymentWTO.getPaymentDate());
                reservationTO.setPaid(registrationPaymentWTO.isSucceed());
            }
            ctz.setCitizenInfo(czi);
            czi.setCitizen(ctz);
            cardRequestTO.setCitizen(ctz);
            reservationTO.setCardRequest(cardRequestTO);
            SimpleDateFormat hours = new SimpleDateFormat("HH");
            ShiftEnum shiftNo = ShiftEnum.getShift(String.valueOf(hours.parse(hours.format(
                    new Date())).before(hours.parse("12:00")) ? 0 : 1));
            reservationTO.setShiftNo(shiftNo);
            reservationTO.setEnrollmentOffice(new EnrollmentOfficeTO(singlePreRegistrationWTO.getEnrollmentOfficeId()));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date reservationDate = df.parse(df.format(new Date()) + "T00:00:00.000000");
            reservationTO.setDate(reservationDate);
            cardRequestTO.setReservationDate(reservationDate);
            return reservationTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PRR_004, e.getMessage(), e);
        }
    }

    private void checkSinglePreRegistrationTransfer(SinglePreRegistrationWTO singlePreRegistrationWTO) throws BaseException {
        if (singlePreRegistrationWTO == null) {
            throw new BaseException(MapperExceptionCode.CPM_001, MapperExceptionCode.CPM_001_MSG);
        }
        if (!NationalIDUtil.checkValidNinStr(singlePreRegistrationWTO.getNationalId())) {
            throw new BaseException(MapperExceptionCode.CPM_002, MapperExceptionCode.CPM_002_MSG, new String[]{String.valueOf(singlePreRegistrationWTO.getTrackingId())});
        }
        /*if (StringUtils.isEmpty(singlePreRegistrationWTO.getTrackingId())) {
            throw new BaseException(MapperExceptionCode.CRM_009, MapperExceptionCode.CRM_009_MSG);
        }*/
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getOrigin())) {
            throw new BaseException(MapperExceptionCode.CPM_003, MapperExceptionCode.CPM_003_MSG);
        }
        if (!NationalIDUtil.checkValidCertSerialNo(String.valueOf(singlePreRegistrationWTO.getCertSerialNo()))) {
            throw new BaseException(MapperExceptionCode.CPM_004, MapperExceptionCode.CPM_004_MSG);
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getCellphoneNumber())) {
            throw new BaseException(MapperExceptionCode.CPM_005, MapperExceptionCode.CPM_005_MSG);
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getMotherName())) {
            throw new BaseException(MapperExceptionCode.CPM_006, MapperExceptionCode.CPM_006_MSG);
        }
        if (singlePreRegistrationWTO.getBirthDateGregorian() == null) {
            throw new BaseException(MapperExceptionCode.CPM_007, MapperExceptionCode.CPM_007_MSG);
        }
        if (StringUtils.isEmpty(singlePreRegistrationWTO.getBirthDateLunar())) {
            throw new BaseException(MapperExceptionCode.CPM_008, MapperExceptionCode.CPM_008_MSG);
        }
        if (singlePreRegistrationWTO.getGender() == null) {
            throw new BaseException(MapperExceptionCode.CPM_009, MapperExceptionCode.CPM_009_MSG);
        }
        if (singlePreRegistrationWTO.getReligion() == null) {
            throw new BaseException(MapperExceptionCode.CPM_010, MapperExceptionCode.CPM_010_MSG);
        }
    }

}
