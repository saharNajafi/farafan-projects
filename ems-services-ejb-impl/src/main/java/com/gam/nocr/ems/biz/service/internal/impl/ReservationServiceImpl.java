package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.ReservationDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.ActiveShiftWTO;
import com.gam.nocr.ems.data.domain.ws.OfficeAppointmentWTO;
import com.gam.nocr.ems.data.domain.ws.RegistrationOfficeWTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.data.util.CrsChecker;
import com.gam.nocr.ems.util.CalendarUtil;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.LangUtil;
import gampooya.tools.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_RESERVATION;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
@Stateless(name = "ReservationService")
@Local(ReservationServiceLocal.class)
@Remote(ReservationServiceRemote.class)
public class ReservationServiceImpl extends EMSAbstractService
        implements ReservationServiceLocal, ReservationServiceRemote {
    @Resource
    SessionContext sessionContext;
    private static final Logger logger = BaseLog.getLogger(ReservationServiceImpl.class);

    public ReservationTO findReservationByCrqId(Long carqId) throws BaseException {

        try {
            return getReservationDAO().findReservationByCrqId(carqId);
        } catch (DataException e) {
            throw e;
        }
    }

    public CardRequestTO transferReservationsToEMS(ReservationTO reservationTO) throws BaseException {
        String nationalId = null;
        try {
            if(reservationTO.getEnrollmentOffice() != null) {
                OfficeCapacityTO officeCapacityTO =
                        getOfficeCapacityService().findByEnrollmentOfficeIdAndDateAndWorkingHour(reservationTO.getEnrollmentOffice().getId());
                reservationTO.setShiftNo(officeCapacityTO.getShiftNo());
            }
            CardRequestTO emsCardRequest = null;
            CardRequestState toState = CardRequestState.RESERVED;
            if (reservationTO.getCardRequest().getCitizen().getNationalID() != null) {
                nationalId = reservationTO.getCardRequest().getCitizen().getNationalID();
                emsCardRequest =
                        getCardRequestService().findLastRequestByNationalId(reservationTO.getCardRequest().getCitizen().getNationalID());
            }

            if (emsCardRequest != null) {
                CardRequestState fromState = emsCardRequest.getState();
                if (!CardRequestState.checkStateChangeValidation(fromState,
                        toState)) {
                    if (nationalId == null) {
                        nationalId = emsCardRequest.getCitizen().getNationalID();
                    }
                    logger.error(BizExceptionCode.RS_003_MSG, new Object[]{String.valueOf(emsCardRequest.getPortalRequestId())});
                    throw new BaseException(BizExceptionCode.RS_003,
                            BizExceptionCode.RS_003_MSG + " from state "
                                    + fromState + " to state " + toState, new Object[]{String.valueOf(emsCardRequest.getPortalRequestId())});
                } else {
                    if (CardRequestState.REPEALED.equals(fromState)
                            || CardRequestState.STOPPED.equals(fromState)) {
                        return reserve(reservationTO, emsCardRequest);
                    } else if (CardRequestState.DOCUMENT_AUTHENTICATED.equals(fromState)
                            || CardRequestState.REFERRED_TO_CCOS.equals(fromState)
                            || CardRequestState.RESERVED.equals(fromState)) {
                        return updateReserve(reservationTO, emsCardRequest);
                    }
                }

            } else {
                return newReserve(reservationTO);
            }

        } catch (BaseException e) {
            if (!BizExceptionCode.RS_003.equals(e.getExceptionCode())) {
                sessionContext.setRollbackOnly();
            }
            logger.error(BizExceptionCode.RS_006_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.RS_006_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new BaseException(BizExceptionCode.GLB_009, BizExceptionCode.GLB_009_MSG, e);
        }
        return null;
    }

    private ReservationDAO getReservationDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_RESERVATION));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RS_001,
                    BizExceptionCode.GLB_001_MSG, e);
        }
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

    private RegistrationPaymentService getRegistrationPaymentService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        RegistrationPaymentService registrationPaymentService;
        try {
            registrationPaymentService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION_PAYMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_REGISTRATION_PAYMENT.split(","));
        }
        registrationPaymentService.setUserProfileTO(getUserProfileTO());
        return registrationPaymentService;
    }

    private CardRequestTO reserve(ReservationTO reservationTO, CardRequestTO cardRequestTO)
            throws BaseException {
        String nationalId = cardRequestTO != null && cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getNationalID() : null;
        CardRequestTO emsCardRequest = new CardRequestTO();
        try {
            emsCardRequest = reservationTO.getCardRequest();
            if (reservationTO.getCardRequest().getRegistrationPaymentTO() != null) {
                emsCardRequest.setPaid(reservationTO.isPaid());
                emsCardRequest.setPaidDate(reservationTO.getPaidDate());
            }
            CitizenTO citizenTO = cardRequestTO.getCitizen();
            emsCardRequest.setCitizen(citizenTO);
            emsCardRequest.setType(cardRequestTO.getType());

            RegistrationPaymentTO emsPayment = cardRequestTO.getRegistrationPaymentTO();
            if (emsPayment != null) {
                emsCardRequest.setRegistrationPaymentTO(emsPayment);
            } else {
                RegistrationPaymentTO crsPayment = reservationTO.getCardRequest().getRegistrationPaymentTO();
                crsPayment.setCitizenTO(citizenTO);
                crsPayment = getRegistrationPaymentService().addRegistrationPayment(crsPayment);
                emsCardRequest.setRegistrationPaymentTO(crsPayment);
            }

            emsCardRequest.setEnrollmentOffice(reservationTO.getEnrollmentOffice());
            getCardRequestService().addCardRequest(emsCardRequest);
            Integer activeDate = Integer.valueOf(CalendarUtil.getDate(reservationTO.getDate(), LangUtil.LOCALE_FARSI).replace("/", ""));
            estelamCardRequestForTodayReservation(emsCardRequest, activeDate, false);
            /*if (emsCardRequest.getPortalRequestId() == null) {
                emsCardRequest.setPortalRequestId(emsCardRequest.getId() + INIT_BIAS_ID);
            }*/
           /* getCardRequestHistoryService().create(emsCardRequest, "CRS Request Id: " + emsCardRequest.getPortalRequestId().toString(),
                    SystemId.PORTAL, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);*/
            reservationTO.getCardRequest().setId(emsCardRequest.getId());
            getEnrollmentOfficeService().updateActiveShift(emsCardRequest, reservationTO.getEnrollmentOffice().getId(),
                    activeDate, reservationTO.getShiftNo());
            reservationTO = addReservation(reservationTO);
            getCardRequestHistoryService().create(
                    reservationTO.getCardRequest(),
                    "CCOS Reservation Date: "
                            + DateUtil.convert(reservationTO.getDate(),
                            DateUtil.JALALI), SystemId.CCOS, null,
                    CardRequestHistoryAction.TRANSFER_RESERVE, null);
            return emsCardRequest;
        } catch (BaseException e) {
            logger.error(BizExceptionCode.RS_004_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.RS_004_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new ServiceException(BizExceptionCode.RS_004,
                    BizExceptionCode.RS_004_MSG, e);
        }
    }

    private CardRequestTO updateReserve(ReservationTO reservationTO, CardRequestTO cardRequestTO)
            throws BaseException {
        String nationalId = cardRequestTO != null && cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getNationalID() : null;
        try {
            checkEnrollmentInProgressAndGetCardRequest(Long.valueOf(cardRequestTO.getCitizen().getNationalID()));
            CardRequestTO emsCardRequest = reservationTO.getCardRequest();
            editPersonalInformation(emsCardRequest, cardRequestTO);
            OfficeAppointmentWTO officeAppointment = createOfficeAppointmentWTO(reservationTO, emsCardRequest);
            getEnrollmentOfficeService().editEnrollmentOfficeAppointment(cardRequestTO, officeAppointment);
            return cardRequestTO;
        } catch (BaseException e) {
            logger.error(BizExceptionCode.RS_005_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw e;
        } catch (Exception ex) {
            logger.error(BizExceptionCode.RS_005_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new ServiceException(BizExceptionCode.RS_005,
                    BizExceptionCode.RS_005_MSG, ex);
        }
    }

    private CardRequestTO newReserve(ReservationTO reservationTO)
            throws BaseException {
        CardRequestTO emsCardRequest = reservationTO.getCardRequest();
        String nationalId = emsCardRequest != null && emsCardRequest.getCitizen() != null ? emsCardRequest.getCitizen().getNationalID() : null;
        try {
            if (reservationTO.getCardRequest().getRegistrationPaymentTO() != null) {
                emsCardRequest.setPaid(reservationTO.isPaid());
                emsCardRequest.setPaidDate(reservationTO.getPaidDate());
            }
            CitizenTO citizenTO = getCitizenService().addCitizen(reservationTO.getCardRequest().getCitizen());
            emsCardRequest.setCitizen(citizenTO);
            emsCardRequest.setType(CardRequestType.FIRST_CARD);
            emsCardRequest.setEnrollmentOffice(reservationTO.getEnrollmentOffice());
            fillRegistrationPayment(reservationTO, emsCardRequest, citizenTO);
            getCardRequestService().addCardRequest(emsCardRequest);
            Integer activeDate = Integer.valueOf(CalendarUtil.getDate(reservationTO.getDate(), LangUtil.LOCALE_FARSI).replace("/", ""));
            estelamCardRequestForTodayReservation(emsCardRequest, activeDate, true);
            /*if (emsCardRequest.getPortalRequestId() == null) {
                emsCardRequest.setPortalRequestId(emsCardRequest.getId() + INIT_BIAS_ID);
            }*/
           /* getCardRequestHistoryService().create(emsCardRequest, "CRS Request Id: " + emsCardRequest.getPortalRequestId().toString(),
                    SystemId.CCOS, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);*/
            reservationTO.getCardRequest().setId(emsCardRequest.getId());
            getEnrollmentOfficeService().updateActiveShift(emsCardRequest, reservationTO.getEnrollmentOffice().getId(),
                    activeDate, reservationTO.getShiftNo());
            reservationTO = addReservation(reservationTO);
            getCardRequestHistoryService().create(
                    reservationTO.getCardRequest(),
                    "CCOS Reservation Date: " + DateUtil.convert(reservationTO.getDate(), DateUtil.JALALI)
                    , SystemId.CCOS, null, CardRequestHistoryAction.TRANSFER_RESERVE, null);
            return emsCardRequest;

        } catch (BaseException e) {
            logger.error(BizExceptionCode.RS_002_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.RS_002_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new ServiceException(BizExceptionCode.RS_002,
                    BizExceptionCode.RS_002_MSG, e);
        }

    }

    private void fillRegistrationPayment(ReservationTO reservationTO, CardRequestTO emsCardRequest, CitizenTO citizenTO) throws BaseException {
        if (reservationTO.getCardRequest().getRegistrationPaymentTO() != null) {
            RegistrationPaymentTO registrationPaymentTO = reservationTO.getCardRequest().getRegistrationPaymentTO();
            registrationPaymentTO.setCitizenTO(citizenTO);
            Long officeId =  emsCardRequest.getEnrollmentOffice().getId();
            Map<String, String> registrationPaymentResult =
                    getRegistrationPaymentService().getPaymentAmountAndPaymentCode(
                            emsCardRequest.getType(), citizenTO.getNationalID()
                            , emsCardRequest.getId(), officeId);
            registrationPaymentTO.setAmountPaid(Integer.valueOf(registrationPaymentResult.get("paymentAmount")));
            registrationPaymentTO.setPaymentCode(registrationPaymentResult.get("paymentCode"));
            registrationPaymentTO = getRegistrationPaymentService().addRegistrationPayment(registrationPaymentTO);
            emsCardRequest.setRegistrationPaymentTO(registrationPaymentTO);
        } else {
            throw new ServiceException(BizExceptionCode.RS_006,
                    BizExceptionCode.RS_007_MSG);
        }
    }

    private void estelamCardRequestForTodayReservation(CardRequestTO cardRequestTO, Integer activeDate, boolean isNewCardRequest) throws BaseException {
        Integer today = Integer.valueOf(CalendarUtil.getDate(new Date(), new Locale("fa")).replace("/", ""));
        if (today.equals(activeDate)) {
            getCardRequestService().updateCitizenByEstelam(cardRequestTO, false, isNewCardRequest);
        }
    }

    public ReservationTO addReservation(ReservationTO entity)
            throws BaseException {
        try {
            return getReservationDAO().create(entity);
        } catch (BaseException e) {
            if (DataExceptionCode.RSI_002.equals(e.getExceptionCode())) {
                throw e;
            }
            throw new DataException(BizExceptionCode.GLB_001, BizExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PTL_013,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private ReservationService getReservationService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        ReservationService reservationService;
        try {
            reservationService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_RESERVATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_RESERVATION.split(","));
        }
        reservationService.setUserProfileTO(getUserProfileTO());
        return reservationService;
    }

    private CardRequestHistoryService getCardRequestHistoryService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestHistoryService cardRequestHistoryService;
        try {
            cardRequestHistoryService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST_HISTORY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST_HISTORY.split(","));
        }
        cardRequestHistoryService.setUserProfileTO(getUserProfileTO());
        return cardRequestHistoryService;
    }

    private EnrollmentOfficeService getEnrollmentOfficeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_ENROLLMENT_OFFICE.split(","));
        }
        enrollmentOfficeService.setUserProfileTO(getUserProfileTO());
        return enrollmentOfficeService;
    }

    private InternalServiceChecker getInternalServiceChecker() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        InternalServiceChecker internalServiceChecker;
        try {
            internalServiceChecker = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_INTERNAL_SERVICE_CHEKER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_INTERNAL_SERVICE_CHEKER.split(","));
        }
        internalServiceChecker.setUserProfileTO(getUserProfileTO());
        return internalServiceChecker;
    }

    public CardRequestTO checkEnrollmentInProgressAndGetCardRequest(Long nationalId) throws BaseException {
        Boolean result = Boolean.FALSE;
        if (nationalId == null) {
            throw new ServiceException(BizExceptionCode.PRR_006, BizExceptionCode.PRR_006_MSG);
        }
        String nationalIdStr = StringUtils.leftPad(String.valueOf(nationalId), 10, "0");
        CardRequestTO cardRequest = getCardRequestService().findLastRequestByNationalId(nationalIdStr);
        if (cardRequest != null) {
            result = checkEnrollmentInProgress(cardRequest);
        }
        if (result) {
            return cardRequest;
        } else {
            throw new ServiceException(BizExceptionCode.PRR_007, BizExceptionCode.PRR_007_MSG);
        }
    }

    private boolean checkEnrollmentInProgress(CardRequestTO cardRequest) throws BaseException {
        return getInternalServiceChecker().checkEnrollmentInProgress(cardRequest);
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

    public void editPersonalInformation(CardRequestTO emsCardRequestTO, CardRequestTO cardRequestTO)
            throws BaseException {
        // check edit personal information forbidden now?
        getInternalServiceChecker().checkEditPersonalInformationEnabled(cardRequestTO);

        // check personal information
        if (emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateSolar() == null) {
            throw new DataException(DataExceptionCode.REG_018, DataExceptionCode.REG_18_MSG);
        }
        CrsChecker.checkLunarDate(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateLunar());
        CrsChecker.checkBirthDateGregorian(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateGregorian());
        if (emsCardRequestTO.getCitizen().getCitizenInfo().getGender() == null) {
            throw new DataException(DataExceptionCode.REG_009, DataExceptionCode.REG_09_MSG);
        }
        if (emsCardRequestTO.getCitizen().getCitizenInfo().getReligion() == null) {
            throw new DataException(DataExceptionCode.REG_008, DataExceptionCode.REG_08_MSG);
        }

        cardRequestTO.setPortalRequestId(emsCardRequestTO.getPortalRequestId());
        CrsChecker.checkMotherName(emsCardRequestTO.getCitizen().getCitizenInfo().getMotherFirstNamePersian());
        CrsChecker.checkMobileNumber(emsCardRequestTO.getCitizen().getCitizenInfo().getMobile());
        CrsChecker.checkSerialNumber(Integer.valueOf(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthCertificateSeries()));

        //update personal information
        cardRequestTO.getCitizen().getCitizenInfo().setBirthDateSolar(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateSolar());
        cardRequestTO.getCitizen().getCitizenInfo().setBirthDateLunar(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateLunar());
        cardRequestTO.getCitizen().getCitizenInfo().setBirthDateGregorian(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthDateGregorian());
        cardRequestTO.getCitizen().getCitizenInfo().setGender(emsCardRequestTO.getCitizen().getCitizenInfo().getGender());
        cardRequestTO.getCitizen().getCitizenInfo().setReligion(emsCardRequestTO.getCitizen().getCitizenInfo().getReligion());
        cardRequestTO.getCitizen().getCitizenInfo().setMotherFirstNamePersian(emsCardRequestTO.getCitizen().getCitizenInfo().getMotherFirstNamePersian());
        cardRequestTO.getCitizen().getCitizenInfo().setMobile(emsCardRequestTO.getCitizen().getCitizenInfo().getMobile());
        cardRequestTO.getCitizen().getCitizenInfo().setBirthCertificateSeries(emsCardRequestTO.getCitizen().getCitizenInfo().getBirthCertificateSeries());
        updateCardRequest(cardRequestTO);
    }

    private OfficeAppointmentWTO createOfficeAppointmentWTO(ReservationTO reservationTO, CardRequestTO emsCardRequest) {
        OfficeAppointmentWTO officeAppointment = new OfficeAppointmentWTO();
        officeAppointment.setId(reservationTO.getPortalReservationId());
        ActiveShiftWTO activeShift = new ActiveShiftWTO();
        Integer appointmentDate = Integer.valueOf(CalendarUtil.getDate(reservationTO.getDate(), new Locale("fa")).replace("/", ""));
        activeShift.setActiveDate(appointmentDate);
        activeShift.setShiftNo(reservationTO.getShiftNo());
        officeAppointment.setAppointmentDate(appointmentDate);
        officeAppointment.setActiveShift(activeShift);
        officeAppointment.setPreRegistrationId(emsCardRequest.getPortalRequestId());
        RegistrationOfficeWTO registrationOffice = new RegistrationOfficeWTO();
        registrationOffice.setId(reservationTO.getEnrollmentOffice().getId());
        officeAppointment.setRegistrationOffice(registrationOffice);
        return officeAppointment;
    }

    public void updateCardRequest(CardRequestTO cardRequestTO) throws BaseException {
        getCardRequestService().update(cardRequestTO);
       /* getCardRequestHistoryService().create(cardRequestTO, "Portal Request Id: " + cardRequestTO.getPortalRequestId() != null ? cardRequestTO.getPortalRequestId().toString() : "",
                SystemId.PORTAL, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);*/
    }

    public ReservationTO create(ReservationTO reservationTO) throws BaseException {
        try {
            return getReservationDAO().create(reservationTO);
        } catch (BaseException e) {
            throw e;
        }
    }

    private OfficeCapacityService getOfficeCapacityService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        OfficeCapacityService officeCapacityService;
        try {
            officeCapacityService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_OFFICE_CAPACITY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RS_007,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_OFFICE_CAPACITY.split(","));
        }
        officeCapacityService.setUserProfileTO(getUserProfileTO());
        return officeCapacityService;
    }

}
