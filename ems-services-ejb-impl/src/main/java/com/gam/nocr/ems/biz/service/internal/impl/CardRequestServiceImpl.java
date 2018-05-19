package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsService;
import com.gam.nocr.ems.biz.service.external.client.ussd.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.LangUtil;
import com.gam.nocr.ems.util.Utils;
import gampooya.tools.date.DateUtil;
import gampooya.tools.security.SecurityContextService;
import org.slf4j.Logger;
import servicePortUtil.ServicePorts;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;
import static com.gam.nocr.ems.data.enums.CardRequestState.VERIFIED_IMS;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
@Stateless(name = "CardRequestService")
@Local(CardRequestServiceLocal.class)
@Remote(CardRequestServiceRemote.class)
public class CardRequestServiceImpl extends EMSAbstractService implements
        CardRequestServiceLocal, CardRequestServiceRemote {

    private static final Logger logger = BaseLog
            .getLogger(CardRequestServiceImpl.class);
    private static final Logger ussdLogger = BaseLog.getLogger("UssdService");

    private static final String DEFAULT_CARD_REQUEST_STATE_WS_WSDL_URL
            = "http://10.7.17.28:7001/ems-web/services/cardRequestState?wsdl";
    private static final String DEFAULT_CARD_REQUEST_STATE_WS_NAMESPACE
            = "http://portalws.ws.web.portal.nocr.gam.com/";
    private static final String DEFAULT_NOCR_SMS_ENDPOINT = "http://sms.sabteahval.ir:8001/SmsProject/SmsPort?wsdl";
    private static final String DEFAULT_NOCR_SMS_NAMESPACE = "http://ws.sms.com/";
    private static final String DEFAULT_NOCR_SMS_SERVICE_USERNAME = "SMARTCARD";
    private static final String DEFAULT_NOCR_SMS_SERVICE_PASSWORD = "sm#asrcb*92";
    @Resource
    SessionContext sessionContext;
    ResourceBundle labels = null;

    @Override
    public Long findRequestCountByAction(CardRequestedAction cardRequestedAction)
            throws BaseException {
        if (cardRequestedAction == null)
            throw new ServiceException(BizExceptionCode.CRE_008,
                    BizExceptionCode.CRE_002_MSG);

        return getCardRequestDAO()
                .findRequestCountByAction(cardRequestedAction);
    }

    @Override
    public List<Long> repealCardRequest(Integer from) throws BaseException {
        List<CardRequestState> cardRequestStateList = new ArrayList<CardRequestState>();
        cardRequestStateList.add(CardRequestState.REGISTERED);
        cardRequestStateList.add(CardRequestState.RECEIVED_BY_EMS);
        cardRequestStateList.add(CardRequestState.PENDING_IMS);
        cardRequestStateList.add(VERIFIED_IMS);
        cardRequestStateList.add(CardRequestState.NOT_VERIFIED_BY_IMS);
        cardRequestStateList.add(CardRequestState.RESERVED);
        cardRequestStateList.add(CardRequestState.REFERRED_TO_CCOS);
        cardRequestStateList.add(CardRequestState.DOCUMENT_AUTHENTICATED);
        cardRequestStateList.add(CardRequestState.APPROVED);

        List<Long> requestIds = new ArrayList<Long>();

        for (CardRequestTO cardRequestTO : getCardRequestDAO()
                .fetchCardRequestByAction(from, 1,
                        CardRequestedAction.REPEAL_ACCEPTED)) {
            if (cardRequestStateList.contains(cardRequestTO.getState())) {
                completeRepealCardRequest(cardRequestTO);
                requestIds.add(cardRequestTO.getId());
                //Anbari
//				ArrayList<Long> repealedIdsList = new ArrayList<Long>(); 
//				repealedIdsList.add(cardRequestTO.getId());
//				getPortalManagementService().doActivityForUpdateState(repealedIdsList);

            } else {
                throw new ServiceException(BizExceptionCode.CRE_015,
                        BizExceptionCode.CRE_015_MSG);
            }
        }

        return requestIds;
    }

    @Override
    @Permissions(value = "ems_repealRequest")
    public void doCardRequestRepealAction(Long cardRequestId,
                                          CardRequestedAction cardRequestedAction, SystemId systemId)
            throws BaseException {
        doRepealAction(cardRequestId, cardRequestedAction, systemId);
    }

    @Override
    @Permissions(value = "ems_transferRequestToNocr")
    public void transferCardRequestToNocr(Long cardRequestId,
                                          CardRequestedAction cardRequestedAction) throws BaseException {
        if (cardRequestId == null)
            throw new ServiceException(BizExceptionCode.CRE_005,
                    BizExceptionCode.CRE_001_MSG);
        if (cardRequestedAction == null)
            throw new ServiceException(BizExceptionCode.CRE_010,
                    BizExceptionCode.CRE_002_MSG);

        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestHistoryDAO cardRequestHistoryDAO = getCardRequestHistoryDAO();

        CardRequestTO cardRequestTO = cardRequestDAO.find(CardRequestTO.class,
                cardRequestId);

        switch (cardRequestedAction) {
            case TRANSFER_IN_PROGRESS:
                Long previousOffice = cardRequestTO.getEnrollmentOffice().getId();

                EnrollmentOfficeTO enrollmentOfficeTO = getSuperiorOffice(cardRequestTO
                        .getEnrollmentOffice().getId());

                if (enrollmentOfficeTO == null)
                    throw new ServiceException(BizExceptionCode.CRE_009,
                            BizExceptionCode.CRE_009_MSG);

                cardRequestTO.setEnrollmentOffice(enrollmentOfficeTO);
                cardRequestTO.setOriginalCardRequestOfficeId(previousOffice);

                cardRequestDAO.update(cardRequestTO);

                cardRequestHistoryDAO.create(cardRequestTO, null, SystemId.CCOS,
                        null, CardRequestHistoryAction.TRANSFER_TO_SUPERIOR_OFFICE,
                        getUserProfileTO().getUserName());
                break;
            case TRANSFER_UNDO:
                CardRequestHistoryTO cardRequestHistoryTO = getCardRequestHistoryDAO()
                        .fetchLastHistoryRecord(cardRequestId);
                if (cardRequestHistoryTO != null) {
                    if (!CardRequestHistoryAction.TRANSFER_TO_SUPERIOR_OFFICE
                            .equals(cardRequestHistoryTO
                                    .getCardRequestHistoryAction()))
                        throw new ServiceException(BizExceptionCode.CRE_011,
                                BizExceptionCode.CRE_011_MSG);
                }

                Long originOffice = cardRequestTO.getOriginalCardRequestOfficeId();

                cardRequestTO.setOriginalCardRequestOfficeId(null);
                cardRequestTO.setEnrollmentOffice(new EnrollmentOfficeTO(
                        originOffice));

                cardRequestDAO.update(cardRequestTO);

                cardRequestHistoryDAO
                        .create(cardRequestTO,
                                null,
                                SystemId.CCOS,
                                null,
                                CardRequestHistoryAction.UNDO_TRANSFER_FROM_SUPERIOR_OFFICE,
                                getUserProfileTO().getUserName());
                break;
        }
    }

    private EnrollmentOfficeTO getSuperiorOffice(Long enrollmentOfficeId)
            throws BaseException {
        return getEnrollmentOfficeDAO().getSuperiorOffice(enrollmentOfficeId);
    }

    private void doRepealAction(Long cardRequestId,
                                CardRequestedAction cardRequestedAction, SystemId systemId)
            throws BaseException {
        if (cardRequestId == null
                && !CardRequestedAction.REPEAL_COMPLETE
                .equals(cardRequestedAction))
            throw new ServiceException(BizExceptionCode.CRE_001,
                    BizExceptionCode.CRE_001_MSG);
        if (cardRequestedAction == null)
            throw new ServiceException(BizExceptionCode.CRE_002,
                    BizExceptionCode.CRE_002_MSG);

        switch (cardRequestedAction) {
            case REPEALING:
                getCardRequestDAO().doCardRequestRepealAction(cardRequestId,
                        cardRequestedAction);
                getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId),
                        cardRequestedAction.name(), systemId, null,
                        CardRequestHistoryAction.REPEAL_CARD_REQUEST,
                        getUserProfileTO().getUserName());
                break;
            case REPEAL_ACCEPTED:
                getCardRequestDAO().doCardRequestRepealAction(cardRequestId,
                        cardRequestedAction);
                getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId),
                        cardRequestedAction.name(), systemId, null,
                        CardRequestHistoryAction.REPEAL_CARD_REQUEST,
                        getUserProfileTO().getUserName());
                break;
            case REPEAL_UNDO:
                getCardRequestDAO().doCardRequestRepealAction(cardRequestId, null);
                getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId),
                        cardRequestedAction.name(), systemId, null,
                        CardRequestHistoryAction.REPEAL_CARD_REQUEST,
                        getUserProfileTO().getUserName());
                break;
        }
    }

    private void completeRepealCardRequest(CardRequestTO cardRequestTO)
            throws BaseException {
        if (cardRequestTO == null)
            throw new ServiceException(BizExceptionCode.CRE_007,
                    BizExceptionCode.CRE_007_MSG);

        try {
            if (EmsUtil.checkListSize(cardRequestTO.getCitizen()
                    .getCitizenInfo().getBiometrics())) {
                if (faceInfoExists(cardRequestTO.getCitizen().getCitizenInfo()
                        .getBiometrics())) {
                    getRegistrationService().removeFaceDataBySystem(
                            cardRequestTO.getCitizen().getId());
                }

                if (fingerInfoExists(cardRequestTO.getCitizen()
                        .getCitizenInfo().getBiometrics())) {
                    getRegistrationService().removeFingerDataBySystem(
                            cardRequestTO.getCitizen().getId());
                }
            }

            if (EmsUtil.checkListSize(cardRequestTO.getCitizen()
                    .getCitizenInfo().getDocuments())) {
                getRegistrationService().removeDocumentBySystem(
                        cardRequestTO.getId(), null);
            }

            CardRequestState previousState = cardRequestTO.getState();
            cardRequestTO.setRequestedAction(null);
            cardRequestTO.setAuthenticity(null);
            cardRequestTO.setEnrolledDate(null);
            cardRequestTO.setReEnrolledDate(null);
            cardRequestTO.setOriginalCardRequestOfficeId(null);

            if (CardRequestType.FIRST_CARD.equals(cardRequestTO.getType())
                    || CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD
                    .equals(cardRequestTO.getType())) {
                cardRequestTO.setState(CardRequestState.REPEALED);
                cardRequestTO.setEnrollmentOffice(null);
                cardRequestTO.setReservationDate(null);

                getReservationDAO().deleteByCardRequest(cardRequestTO.getId());

                getCardRequestHistoryDAO().create(cardRequestTO,
                        "Previous state was " + previousState, SystemId.JOB,
                        null, CardRequestHistoryAction.REPEAL_CARD_REQUEST,
                        null);
            } else {
                cardRequestTO.setState(CardRequestState.REFERRED_TO_CCOS);
                cardRequestTO.setEnrolledDate(new Date());
                cardRequestTO.setReservationDate(EmsUtil
                        .getDateAtMidnight(new Date()));
                cardRequestTO.setEstelam2Flag(Estelam2FlagType.R);

                List<ReservationTO> reservationTOs = cardRequestTO
                        .getReservations();
                for (ReservationTO reservationTO : reservationTOs)
                    reservationTO
                            .setDate(EmsUtil.getDateAtMidnight(new Date()));

                getCardRequestHistoryDAO().create(cardRequestTO,
                        "Previous state was " + previousState, SystemId.JOB,
                        null, CardRequestHistoryAction.REPEAL_CARD_REQUEST,
                        null);
            }

            getCardRequestDAO().update(cardRequestTO);

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_004, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_004,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private Boolean fingerInfoExists(List<BiometricTO> biometricTOList) {
        Boolean result = false;
        for (BiometricTO biometricTO : biometricTOList) {
            switch (biometricTO.getType()) {
                case FING_ALL:
                    result = true;
                    break;
                case FING_CANDIDATE:
                    result = true;
                    break;
                case FING_MIN_1:
                    result = true;
                    break;
                case FING_MIN_2:
                    result = true;
                    break;
            }
            if (result)
                break;
        }
        return result;
    }

    private Boolean faceInfoExists(List<BiometricTO> biometricTOList) {
        Boolean result = false;
        for (BiometricTO biometricTO : biometricTOList) {
            switch (biometricTO.getType()) {
                case FACE_IMS:
                    result = true;
                    break;
                case FACE_CHIP:
                    result = true;
                    break;
                case FACE_MLI:
                    result = true;
                    break;
                case FACE_LASER:
                    result = true;
                    break;
            }
            if (result)
                break;
        }
        return result;
    }

    private RegistrationService getRegistrationService() throws BaseException {
        RegistrationService registrationService;
        try {
            registrationService = ServiceFactoryProvider.getServiceFactory()
                    .getService(getServiceJNDIName(SRV_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_012,
                    BizExceptionCode.GLB_002_MSG, e,
                    SRV_REGISTRATION.split(","));
        }
        registrationService.setUserProfileTO(getUserProfileTO());
        return registrationService;
    }

    //Anbari
    private PortalManagementService getPortalManagementService() throws BaseException {
        PortalManagementService portalManagementService;
        try {
            portalManagementService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_PORTAL_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_075, BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_PORTAL_MANAGEMENT.split(","));
        }
        portalManagementService.setUserProfileTO(getUserProfileTO());
        return portalManagementService;
    }


    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_013,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_CARD_REQUEST.split(","));
        }
    }

    private ReservationDAO getReservationDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_RESERVATION));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_014,
                    BizExceptionCode.GLB_001_MSG, e, DAO_RESERVATION.split(","));
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

    private CitizenDAO getCitizenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_038,
                    BizExceptionCode.GLB_001_MSG, e);
        }
    }

    private PurgeHistoryDAO getPurgeHistoryDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PURGE_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_036, BizExceptionCode.GLB_001_MSG, e);
        }
    }

    private RatingInfoDAO getRatingInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_RATING_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RMS_006, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_RATING_INFO.split(",")
            );
        }
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_015,
                    BizExceptionCode.GLB_001_MSG, e,
                    new String[]{EMSLogicalNames.DAO_CARD_REQUEST_HISTORY});
        }
    }

    @Override
    public List<CardRequestVTO> fetchCardRequests(GeneralCriteria criteria)
            throws BaseException {
        return getCardRequestDAO().fetchCardRequests(criteria);
    }

    @Override
    public Integer countCardRequests(GeneralCriteria criteria)
            throws BaseException {
        return getCardRequestDAO().countCardRequests(criteria);

    }

    @Override
    public List<CitizenWTO> fetchCCOSRegistrationCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        return getCardRequestDAO().fetchCCOSRegistrationCartableCardRequests(
                criteria);
    }

    @Override
    public Integer countCCOSRegistrationCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        return getCardRequestDAO().countCCOSRegistrationCartableCardRequests(
                criteria);
    }

    @Override
    public List<CitizenWTO> fetchCCOSDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        return getCardRequestDAO().fetchCCOSDeliverCartableCardRequests(
                criteria);
    }

    @Override
    public Integer countCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
            throws BaseException {
        return getCardRequestDAO().countCCOSDeliverCartableCardRequests(
                criteria);
    }

    @Override
    public List<CitizenWTO> fetchCCOSReadyToDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        return getCardRequestDAO().fetchCCOSReadyToDeliverCartableCardRequests(
                criteria);
    }

    @Override
    public Integer countCCOSReadyToDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        return getCardRequestDAO().countCCOSReadyToDeliverCartableCardRequests(
                criteria);
    }

    @Override
    public List<Long> getRequestIdsForUpdateState(Integer fetchLimit) throws BaseException {
        return getCardRequestDAO().getRequestIdsForUpdateState(fetchLimit);
    }

    @Override
    public List<SyncCardRequestWTO> getRequestListForUpdateState(
            List<Long> requestIds) throws BaseException {
        return getCardRequestDAO().getRequestListForUpdateState(requestIds);
    }

    //Anbari: new method like completeRepealCardRequest
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public void doRepealCardRequest(CardRequestTO cardRequestTO) throws BaseException {
        if (cardRequestTO == null)
            throw new ServiceException(BizExceptionCode.CRE_007, BizExceptionCode.CRE_007_MSG);

        try {
            if (EmsUtil.checkListSize(cardRequestTO.getCitizen().getCitizenInfo().getBiometrics())) {
                if (faceInfoExists(cardRequestTO.getCitizen().getCitizenInfo().getBiometrics())) {
                    getBiometricDAO().removeFaceInfoByCitizenId(cardRequestTO.getCitizen().getId());
                }

                if (fingerInfoExists(cardRequestTO.getCitizen().getCitizenInfo().getBiometrics())) {
                    getBiometricDAO().removeFingersInfoByCitizenId(cardRequestTO.getCitizen().getId());
                }
            }

            if (EmsUtil.checkListSize(cardRequestTO.getCitizen().getCitizenInfo().getDocuments())) {
                getDocumentDAO().removeByRequestIdAndType(cardRequestTO.getId(), null);
            }

            CardRequestState previousState = cardRequestTO.getState();
            cardRequestTO.setRequestedAction(null);
            cardRequestTO.setAuthenticity(null);
            cardRequestTO.setEnrolledDate(null);
            cardRequestTO.setReEnrolledDate(null);
            cardRequestTO.setOriginalCardRequestOfficeId(null);

            if (CardRequestType.FIRST_CARD.equals(cardRequestTO.getType()) ||
                    CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD.equals(cardRequestTO.getType())) {
                cardRequestTO.setState(CardRequestState.REPEALED);
                cardRequestTO.setEnrollmentOffice(null);
                cardRequestTO.setReservationDate(null);

                getReservationDAO().deleteByCardRequest(cardRequestTO.getId());

                getCardRequestHistoryDAO().create(cardRequestTO, "CardProductionError: Repeal",
                        SystemId.EMS, null, CardRequestHistoryAction.REPEAL_CARD_REQUEST, null);
                getCardRequestDAO().addRequestedSmsForNotification(cardRequestTO.getId(), SMSTypeState.REPEALED_FIRST_CARD);
            } else {
                cardRequestTO.setState(CardRequestState.REFERRED_TO_CCOS);
                cardRequestTO.setEnrolledDate(new Date());
                cardRequestTO.setReservationDate(EmsUtil.getDateAtMidnight(new Date()));

                List<ReservationTO> reservationTOs = cardRequestTO.getReservations();
                for (ReservationTO reservationTO : reservationTOs)
                    reservationTO.setDate(EmsUtil.getDateAtMidnight(new Date()));

                getCardRequestHistoryDAO().create(cardRequestTO, "Previous state was " + previousState,
                        SystemId.EMS, null, CardRequestHistoryAction.REPEAL_CARD_REQUEST, null);
                getCardRequestDAO().addRequestedSmsForNotification(cardRequestTO.getId(), SMSTypeState.REPEALED_OTHERS);
            }

            getCardRequestDAO().update(cardRequestTO);

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_004, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_004, BizExceptionCode.GLB_008_MSG, e);
        }
    }


    //Anbari
    private BiometricDAO getBiometricDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_BIOMETRIC));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_129, BizExceptionCode.GLB_001_MSG, e,
                    DAO_BIOMETRIC.split(","));
        }
    }

    //Anbari
    private DocumentDAO getDocumentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_DOCUMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_130, BizExceptionCode.GLB_001_MSG, e,
                    DAO_DOCUMENT.split(","));
        }
    }

    //Madanipour
    private ImsEstelamImageDAO getImsEstelamImageDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_IMS_ESTELAM_IMAGE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_039, BizExceptionCode.GLB_001_MSG, e,
                    DAO_IMS_ESTELAM_IMAGE.split(","));
        }
    }


    // Anbari
    @Override
    @Permissions(value = "ems_cmsErrorDeleteImage")
    public void doImageDeleteAction(List<Long> cardRequestIds, Long citizenId,
                                    CardRequestState state) throws BaseException {

        try {
            getBiometricDAO().removeFaceInfoByCitizenId(citizenId);
            sessionContext.getBusinessObject(CardRequestServiceLocal.class).updateCardRequestForDocumentAuthenticateState(cardRequestIds.get(0), state);
            getCardRequestDAO().addRequestedSmsForNotification(cardRequestIds.get(0), SMSTypeState.DOCUMENT_AUTHENTICATED);

        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_018, e.getMessage(), e);
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CRE_018, BizExceptionCode.GLB_008_MSG, e);
        }

    }

    // Anbari
    @Override
    @Permissions(value = "ems_cmsErrorRetry")
    public void doCMSRetryAction(List<Long> cardRequestIds,
                                 CardRequestState state) throws BaseException {
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(cardRequestIds.get(0));
            CardRequestState priviousState = cardRequestTO.getState();
            cardRequestTO.setState(state);
            getCardRequestDAO().update(cardRequestTO);
            getCardRequestHistoryDAO().create(cardRequestTO, "CardProductionError: CMSRetry",
                    SystemId.EMS, null, CardRequestHistoryAction.APPROVED_BY_AFIS, null);
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_019, e.getMessage(), e);
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CRE_019, BizExceptionCode.GLB_008_MSG, e);
        }

    }

    // Anbari
    @Override
    @Permissions(value = "ems_cmsErrorRepealed")
    public void doRepealCardAction(List<Long> cardRequestIds)
            throws BaseException {
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(cardRequestIds.get(0));
            sessionContext.getBusinessObject(CardRequestServiceLocal.class).doRepealCardRequest(cardRequestTO);
            //getPortalManagementService().doActivityForUpdateState(cardRequestIds);
            //doRepealAction(cardRequestIds.get(0), CardRequestedAction.REPEAL_ACCEPTED,SystemId.EMS);

        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_020, e.getMessage(), e);
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CRE_020, BizExceptionCode.GLB_008_MSG, e);
        }

    }

    //Anbari
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public void updateCardRequestForDocumentAuthenticateState(Long cardRequestId, CardRequestState state) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(cardRequestId);
        CardRequestState priviousState = cardRequestTO.getState();
        cardRequestTO.setState(state);
        //cardRequestTO.setEnrolledDate(new Date());
        cardRequestTO.setReEnrolledDate(new Date());
        // cardRequestTO.setReservationDate(EmsUtil.getDateAtMidnight(new Date()));

//          List<ReservationTO> reservationTOs = cardRequestTO.getReservations();
//          for (ReservationTO reservationTO : reservationTOs)
//              reservationTO.setDate(EmsUtil.getDateAtMidnight(new Date()));
        getCardRequestDAO().update(cardRequestTO);
        getCardRequestHistoryDAO().create(cardRequestTO, "CardProductionError: DeleteFace",
                SystemId.EMS, null, CardRequestHistoryAction.COMPLETE_REGISTRATION, null);

    }


    // ganjyar
    private IMSManagementService getIMSManagementService(
            UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        IMSManagementService imsManagementService = null;
        try {
            imsManagementService = (IMSManagementService) factory
                    .getService(EMSLogicalNames
                            .getServiceJNDIName(EMSLogicalNames.SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.CRE_016,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_IMS_MANAGEMENT.split(","));
        }
        imsManagementService.setUserProfileTO(userProfileTO);
        return imsManagementService;
    }

// ganjyar
//		@Override
//		public void doEstelam2(Long cardRequestId) throws BaseException {
//			try {
//				getIMSManagementService(null).doEstelam2(cardRequestId);
//			} catch (BaseException e) {
//				sessionContext.setRollbackOnly();
//				throw e;
//			} catch (Exception e) {
//				sessionContext.setRollbackOnly();
//				throw new ServiceException(BizExceptionCode.CRE_017,
//						BizExceptionCode.GLB_008_MSG, e);
//			}
//		}

    //Commented By Anbari
    @Override
    public AccessProductionVTO getAccessProduction() throws BaseException {

        try {
            AccessProductionVTO accessProductionVTO = new AccessProductionVTO();

            SecurityContextService securityContextService = new SecurityContextService();
            if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_cmsErrorDeleteImage")) {
                accessProductionVTO.setErrorDeleteImageAccess(true);

            }
            if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_cmsErrorRepealed")) {
                accessProductionVTO.setErrorRepealedAccess(true);
            }
            if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_cmsErrorRetry")) {

                accessProductionVTO.setErrorRetryAccess(true);
            }

            return accessProductionVTO;

        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_021,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    //Anbari -userPerm-commented
    /*public AccessProductionVTO getAccessProduction() throws BaseException {

		try {
			AccessProductionVTO accessProductionVTO = new AccessProductionVTO();

			if (getUserManagementService().hasAccess(userProfileTO.getUserName(),"ems_cmsErrorDeleteImage"))
			{
				accessProductionVTO.setErrorDeleteImageAccess(true);

			}
			if (getUserManagementService().hasAccess(userProfileTO.getUserName(),"ems_cmsErrorRepealed"))
			{
				accessProductionVTO.setErrorRepealedAccess(true);
			}
			if (getUserManagementService().hasAccess(userProfileTO.getUserName(),"ems_cmsErrorRetry"))
			{

				accessProductionVTO.setErrorRetryAccess(true);
			}

			return accessProductionVTO;

		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CRE_021,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}
	*/

    //Anbari
    private UserManagementService getUserManagementService() throws BaseException {
        UserManagementService userManagementService;
        try {
            userManagementService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RMG_016, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_USER.split(","));
        }
        return userManagementService;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void increamentPriorityForSyncJob(Long requestId) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(requestId);
        cardRequestTO.setPriority(2);
        getCardRequestDAO().update(cardRequestTO);
    }

    /**
     * this method is used in change priority process .this method finds a card request by id
     *
     * @author ganjyar
     */
    @Override
    public CardRequestVTO findCardRequestById(String cardRequestId)
            throws BaseException {
        try {

            CardRequestTO cardRequestTO = getCardRequestDAO().find(
                    CardRequestTO.class, Long.parseLong(cardRequestId));
            CardRequestVTO cardRequestVTO = new CardRequestVTO();
            cardRequestVTO.setCardState(cardRequestTO.getState().toString());
            cardRequestVTO.setTrackingId(cardRequestTO.getTrackingID());
            cardRequestVTO.setCitizenFirstName(cardRequestTO.getCitizen()
                    .getFirstNamePersian());
            cardRequestVTO.setCitizenSurname(cardRequestTO.getCitizen()
                    .getSurnamePersian());
            cardRequestVTO.setCitizenNId(cardRequestTO.getCitizen()
                    .getNationalID());
            cardRequestVTO.setPriority(cardRequestTO.getPriority());
            return cardRequestVTO;

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_022, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_022,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * this method is used in change priority process. the given priority must be between 0 or 99
     *
     * @author ganjyar
     */
    @Override
    public void updateCardRequestPriority(String cardRequestId, String priority)
            throws BaseException {
        try {

            if (EmsUtil.checkString(priority)
                    && EmsUtil.checkString(cardRequestId)) {

                CardRequestTO cardRequestTO = getCardRequestDAO().find(
                        CardRequestTO.class, Long.parseLong(cardRequestId));
                if (cardRequestTO != null) {
                    int inPriority = Integer.parseInt(priority);
                    if (inPriority > 99 || inPriority < 0)
                        throw new ServiceException(BizExceptionCode.CRE_024,
                                BizExceptionCode.CRE_024_MSG);
                    cardRequestTO.setPriority(inPriority);
                }
            } else
                throw new ServiceException(BizExceptionCode.CRE_026,
                        BizExceptionCode.CRE_026_MSG);

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_023, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_023,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * this method is used to check a person has access to change priority
     *
     * @author ganjyar
     */

    @Override
    public boolean hasChangePriorityAccess() throws BaseException {
        try {
            SecurityContextService securityContextService = new SecurityContextService();
            if (securityContextService.hasAccess(userProfileTO.getUserName(),
                    "ems_changePriority")) {
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_025, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_025,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }


    //*********** Anbari - userPerm-commented
    /*
    @Override
	public boolean hasChangePriorityAccess() throws BaseException {
		try {
			if (getUserManagementService().hasAccess(userProfileTO.getUserName(),
					"ems_changePriority")) {
				return true;
			}

			return false;
		} catch (Exception e) {
			logger.error(BizExceptionCode.CRE_025, e.getMessage(), e);
			throw new ServiceException(BizExceptionCode.CRE_025,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}
	*/


    // hossein 8 feature start
    @Override
    @Permissions(value = "ems_viewCardRequestInfo")
    @BizLoggable(logAction = "LOAD", logEntityName = "REQUEST")
    public CardRequestVTO viewCardRequestInfo(Long cardRequestId)
            throws BaseException {
        try {
            CardRequestTO cardRequestTO = loadById(cardRequestId);
            CardRequestVTO cardRequestVTO = CardRequestMapper
                    .convertToVTO(cardRequestTO);
            return cardRequestVTO;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_028,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    private CardRequestTO loadById(Long cardRequestId) throws BaseException {
        if (cardRequestId == 0) {
            throw new ServiceException(BizExceptionCode.CRE_027,
                    BizExceptionCode.CRE_027_MSG);
        }
        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestTO cardRequestTO = cardRequestDAO.find(CardRequestTO.class,
                cardRequestId);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CRE_029,
                    BizExceptionCode.GLB_021_MSG);
        } else {
            return cardRequestTO;
        }
    }
    // hossein 8 feature end

    //Madanipour
    @Override
    public List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,
                                           Integer dayInterval) throws BaseException {
        try {
            List<Long> ids = getCardRequestDAO().fetchReservedRequest(
                    numberOfRequestToFetch, dayInterval);
            return ids;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_032,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    //Madanipour
    @Override
    public void addRequestedSmsForReservedReq(Long cardRequestId)
            throws BaseException {

        try {

            getCardRequestDAO().addRequestedSmsForReservedReq(cardRequestId);
            getCardRequestDAO().updateCardRequestRequestedSmsStatus(cardRequestId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_033,
                    BizExceptionCode.GLB_008_MSG, e);
        }


    }

    /**
     * this method is called in unsuccessful card delivery process when
     * card state is 'stopped'.then revokes card and repeals card request.
     *
     * @author ganjyar
     */
    @Override
    public void repealCardRequestInDelivery(CardRequestTO cardRequestTO)
            throws BaseException {
        try {

            completeRepealCardRequest(cardRequestTO);

        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CRE_028,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }


    /**
     * this fetches citizen ids belongs to card requests must be purged.
     *
     * @author ganjyar
     */
    @Override
    public List<Long> getCitizenIdsForPurgeBioAndDocs(Integer fetchLimit)
            throws BaseException {
        try {

            return getCardRequestDAO().getCitizenIdsForPurgeBioAndDocs(fetchLimit);

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_034,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    @Override
    public void purgeBiometricsAndDocuments(Long citizenId,
                                            String savePurgeHistory) throws BaseException {
        try {
            getBiometricDAO().emptyBiometricData(citizenId);
            getDocumentDAO().emptyDocumentData(citizenId);
            deleteImsEstelamImage(citizenId);

            CitizenTO citizenTO = getCitizenDAO().find(CitizenTO.class,
                    citizenId);
            citizenTO.setPurgeBio(Boolean.TRUE);
            citizenTO.setPurgeBioDate(new Date());
            if (savePurgeHistory.toLowerCase().equals("true")) {
                sessionContext.getBusinessObject(CardRequestServiceLocal.class).savePurgeHistory(citizenId, PurgeState.SUCCESSFULL, null);
            }

        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            if (savePurgeHistory.toLowerCase().equals("true")) {
                sessionContext.getBusinessObject(CardRequestServiceLocal.class).savePurgeHistory(citizenId, PurgeState.FAILED, e.toString());
            }
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            if (savePurgeHistory.toLowerCase().equals("true")) {
                sessionContext.getBusinessObject(CardRequestServiceLocal.class).savePurgeHistory(citizenId, PurgeState.FAILED, e.toString());
            }
            throw new ServiceException(BizExceptionCode.CRE_035,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }


    private void deleteImsEstelamImage(Long id) throws BaseException {
        try {

            ImsEstelamImageTO imsEstelamImageTO = getImsEstelamImageDAO().find(ImsEstelamImageTO.class, id);

            if (imsEstelamImageTO != null)
                getImsEstelamImageDAO().delete(imsEstelamImageTO);

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);

        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_040, e.getMessage(), e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void savePurgeHistory(Long citizenId, PurgeState purgeState,
                                 String metaData) throws BaseException {
        try {
            PurgeHistoryTO purgeHistoryTO = new PurgeHistoryTO();
            CitizenTO citizenTO = new CitizenTO();
            citizenTO.setId(citizenId);
            String nationalID = getCitizenDAO()
                    .find(CitizenTO.class, citizenId).getNationalID();
            purgeHistoryTO.setCitizen(citizenTO);
            purgeHistoryTO.setPurgeBiometricDate(new Date());
            purgeHistoryTO.setPurgeState(purgeState);
            purgeHistoryTO.setNationalId(nationalID);
            purgeHistoryTO.setMetaData(metaData);
            getPurgeHistoryDAO().create(purgeHistoryTO);

        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_036, e.getMessage(), e);
        }

    }

    @Override
    public String findCardRequestStateByTrackingId(
            String trackingId) throws BaseException {
        labels = ResourceBundle.getBundle("ussd-request-state");
        String state = "";
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().findCardRequestStateByTrackingId(trackingId);
            if (cardRequestTO == null)
                state = labels.getString("state.invalidTrackingId");
            else
                state = getState(cardRequestTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_053,
                    BizExceptionCode.CRE_053_MSG);
        }
        return state;
    }

    @Override
    public String findCardRequestStateByNationalIdAndMobile(
            String nationalId, String mobile) throws BaseException {
        labels = ResourceBundle.getBundle("ussd-request-state");
        String state = "";
        try {
            nationalId = LangUtil.getEnglishNumber(nationalId);
            if (!Utils.isValidNin(nationalId)) {
                state = labels.getString("state.invalidNationalId");
            } else {
                CardRequestTO cardRequestTO = getCardRequestDAO().findCardRequestStateByNationalId(nationalId);
                if (cardRequestTO == null)
                    state = "-1";
                else if (cardRequestTO != null) {
                    if (cardRequestTO.getCitizen().getCitizenInfo().getMobile() != null) {
                        if (cardRequestTO.getCitizen().getCitizenInfo().getMobile().equals(mobile))
                            state = getState(cardRequestTO);
                        else if (!cardRequestTO.getCitizen().getCitizenInfo().getMobile().equals(mobile))
                            state = "-1";
                    } else {
                        state = "-1";
                    }
                }
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_054,
                    BizExceptionCode.CRE_054_MSG);
        }
        return state;
    }

    @Override
    public String findCardRequestStateByNationalIdAndBirthCertificateSeries(
            String nationalId, String birthCertificateSeries, String citizenBirthDate) throws BaseException {
        labels = ResourceBundle.getBundle("ussd-request-state");
        boolean checkWhiteList;
        String state = "";
        try {
            nationalId = LangUtil.getEnglishNumber(nationalId);
            if (!Utils.isValidNin(nationalId)) {
                state = labels.getString("state.invalidNationalId");
            } else if (birthCertificateSeries == null) {
                state = labels.getString("state.nullBirthCertificateSeries");
            } else {
                CardRequestTO cardRequestTO =
                        getCardRequestDAO().findCardRequestStateByNationalId(nationalId);
                if (cardRequestTO != null) {
                    if (!cardRequestTO.getCitizen().getCitizenInfo()
                            .getBirthCertificateSeries().equals(birthCertificateSeries))
                        state = labels.getString("state.invalidBirthCertificateSeries");
                    else
                        state = getState(cardRequestTO);
                }
                if (cardRequestTO == null) {
                    String checkCrq = getService().checkCardRequestState(nationalId);
                    if (checkCrq == null) {
                        checkWhiteList =
                                getService().checkWhiteList(citizenBirthDate, nationalId, birthCertificateSeries);
                        if (checkWhiteList)
                            state = labels.getString("state.inWhiteList");
                        else
                            state = labels.getString("state.NotInWhiteList");
                    }
                    if (checkCrq != null) {
                        if (checkCrq.equals("notPaid"))
                            state = labels.getString("state.notPaid");
                        if (checkCrq.equals("notReserved"))
                            state = labels.getString("state.notReserved");
                    }
                }
            }

        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_051,
                    BizExceptionCode.CRE_051_MSG);
        } catch (BaseException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_052 + BizExceptionCode.CRE_052_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_052,
                    BizExceptionCode.CRE_052_MSG);
        } catch (ExternalInterfaceException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_050 + BizExceptionCode.CRE_050_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_050,
                    BizExceptionCode.CRE_050_MSG);
        } catch (UnsupportedEncodingException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_052 + BizExceptionCode.CRE_052_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_052,
                    BizExceptionCode.CRE_052_MSG);
        }
        return state;

    }

    private boolean findReservationAttended(CardRequestTO cardRequestTO) throws BaseException {
        boolean notAttended = false;
        try {
            if (cardRequestTO.getState() == CardRequestState.RESERVED &&
                    cardRequestTO.getEstelam2Flag() == Estelam2FlagType.V &&
                    cardRequestTO.getReservationDate().before(new Date()) &&
                    !getService().findCitizenIsAttended(cardRequestTO.getPortalRequestId()))
                notAttended = true;
        }catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_043,
                    BizExceptionCode.CRE_043_MSG);
        } catch (ExternalInterfaceException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_049 + BizExceptionCode.CRE_049_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_049,
                    BizExceptionCode.CRE_049_MSG);
        }
        return notAttended;
    }

    private String findReadyToDeliverState(CardRequestTO cardRequestTO) throws BaseException  {
        String state = "";
        String result;
        try {
            String deliveryOffice = null;
            try {
                deliveryOffice = getService().findDeliveryOfficeByNationalId(
                        cardRequestTO.getCitizen().getNationalID());
            }catch (ExternalInterfaceException_Exception e) {
                ussdLogger.error(BizExceptionCode.CRE_045 + BizExceptionCode.CRE_045_MSG + e.getMessage(), e);
                throw new ServiceException(BizExceptionCode.CRE_045,
                        BizExceptionCode.CRE_045_MSG);
            }
            if (deliveryOffice.equals("PRT_W_CRSW_004")) {
                EnrollmentOfficeTO eofByCardRequest =
                        getEnrollmentOfficeDAO().findEnrollmentOfficeById(cardRequestTO.getEnrollmentOffice().getId());
                if (EnrollmentOfficeType.NOCR.equals(eofByCardRequest.getType())) {
                    state = MessageFormat.format(
                                    labels.getString("state.readyToDeliverState"), eofByCardRequest.getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    eofByCardRequest.getAddress());
                }
                if (EnrollmentOfficeType.OFFICE.equals(eofByCardRequest.getType()) &&
                        EnrollmentOfficeDeliverStatus.DISABLED.equals(eofByCardRequest.getDeliver()) &&
                        eofByCardRequest.getSuperiorOffice().getId() != null) {
                    state = MessageFormat.format(
                                    labels.getString("state.readyToDeliverState"),
                                    eofByCardRequest.getSuperiorOffice().getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    eofByCardRequest.getSuperiorOffice().getAddress());
                }
                if (EnrollmentOfficeType.OFFICE.equals(eofByCardRequest.getType()) &&
                        EnrollmentOfficeDeliverStatus.ENABLED.equals(eofByCardRequest.getDeliver())) {
                    state = MessageFormat.format(
                                    labels.getString("state.readyToDeliverState"),
                                    eofByCardRequest.getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    eofByCardRequest.getAddress());
                }
            } else {
                EnrollmentOfficeTO eofDeliveredOfficeId =
                        getEnrollmentOfficeDAO().findEnrollmentOfficeById(Long.parseLong(deliveryOffice));
                if (EnrollmentOfficeType.OFFICE.equals(eofDeliveredOfficeId.getType())) {
                    state = MessageFormat.format(
                                 labels.getString("state.readyToDeliverState"), eofDeliveredOfficeId.getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    eofDeliveredOfficeId.getAddress());
                }
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_046,
                    BizExceptionCode.CRE_046_MSG);
        }
        return state;
    }

    private String findCrqFlagByCardRequest(CardRequestTO cardRequestTO) throws BaseException {
        String state = "";
        String result = "";
        try {
            Integer crqFlag = getCardRequestDAO().fetchBiometricFlag(cardRequestTO.getId());
            CardRequestHistoryTO crhList = getCardRequestHistoryDAO().findByCardRequestId(cardRequestTO.getId());
            EnrollmentOfficeTO enrollmentOfficeTO =
                    getEnrollmentOfficeDAO().findEnrollmentOfficeById(cardRequestTO.getEnrollmentOffice().getId());
//			Condition 1
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                    cardRequestTO.getAuthenticity() == CardRequestAuthenticity.AUTHENTIC &&
                    cardRequestTO.getOriginalCardRequestOfficeId() == null
                    ) {
                if (crqFlag == 7)
                    state = labels.getString("state.crqFlag7");
                if (crqFlag == 5) {
                    state = MessageFormat.format(labels.getString("state.crqFlag5"), enrollmentOfficeTO.getPhone());
                    result = getNocrSmsService().send(
                                      cardRequestTO.getCitizen().getCitizenInfo().getMobile()
                                     , enrollmentOfficeTO.getAddress());
                }
                if (crqFlag == 4 || crqFlag == 0) {
                    state = MessageFormat.format(labels.getString("state.crqFlag4&0"), enrollmentOfficeTO.getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile()
                                    , enrollmentOfficeTO.getAddress());
                }

                if (crqFlag == 3 || crqFlag == 1 || crqFlag == 2 || crqFlag == 6)
                    state = labels.getString("state.crqFlag");

                // 		Condition 2
                if (crhList.getResult() != null) {
                    if (crhList.getResult().equals("current state is CMS_PRODUCTION_ERROR")) {
                        if (crqFlag == 6) {
                            state = MessageFormat.format(labels.getString("state.crqFlag6")
                                            , enrollmentOfficeTO.getPhone());
                            result = getNocrSmsService().send(
                                            cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                            enrollmentOfficeTO.getAddress());
                        }
                    }
                }
            }
//			Condition 3
            if (crhList.getResult() != null) {
                if (cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS &&
                        (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                                enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                        crhList.getResult().equals("current state is CMS_PRODUCTION_ERROR") &&
                        cardRequestTO.getAuthenticity() == CardRequestAuthenticity.AUTHENTIC &&
                        cardRequestTO.getOriginalCardRequestOfficeId() == null) {
                    if (crqFlag == 6) {
                        state = MessageFormat.format(labels.getString("state.crqFlag6"), enrollmentOfficeTO.getPhone());
                        result = getNocrSmsService().send(
                                        cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                        enrollmentOfficeTO.getAddress());
                    }
                }
            }

//			Condition 5
            if (cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                    (cardRequestTO.getAuthenticity() == CardRequestAuthenticity.NOT_AUTHENTIC ||
                            cardRequestTO.getAuthenticity() == null) &&
                    cardRequestTO.getOriginalCardRequestOfficeId() == null) {
                if (crqFlag == 0)
                    state = labels.getString("state.crqFlag");
            }
//			Condition 6
            if ((cardRequestTO.getAuthenticity() == CardRequestAuthenticity.NOT_AUTHENTIC ||
                    cardRequestTO.getAuthenticity() == null) &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE))) {
                if (crqFlag == 1 || crqFlag == 2 || crqFlag == 3 || crqFlag == 4 ||
                        crqFlag == 5 || crqFlag == 6 || crqFlag == 7)
                    state = labels.getString("state.crqFlag");
            }
//			condition 8
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED &&
                    enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE) &&
                    cardRequestTO.getAuthenticity() == CardRequestAuthenticity.AUTHENTIC &&
                    cardRequestTO.getOriginalCardRequestOfficeId() != null
                    ) {
                if (crqFlag == 5) {
                    state = MessageFormat.format(labels.getString("state.crqFlagOffice5")
                                    , getEnrollmentOfficeDAO().findEnrollmentOfficeById(
                                            enrollmentOfficeTO.getSuperiorOffice().getId()).getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    enrollmentOfficeTO.getSuperiorOffice().getAddress());
                }
                if (crqFlag == 4 || crqFlag == 0) {
                    state = MessageFormat.format(labels.getString("state.crqFlagOffice0")
                                    , getEnrollmentOfficeDAO().findEnrollmentOfficeById(
                                            enrollmentOfficeTO.getSuperiorOffice().getId()).getPhone());
                    result = getNocrSmsService().send(
                                    cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                    enrollmentOfficeTO.getSuperiorOffice().getAddress());
                }
            }
            ussdLogger.info("incoming result from NOCR SMS service is : " + result);
            if (SmsMessages.INVALID_USER.name().equals(result)) {
                ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_010_MSG);
                throw new ServiceException(BizExceptionCode.OSS_003,
                        BizExceptionCode.PSS_010_MSG);
            }
            else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result)) {
                ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_011_MSG);
                throw new ServiceException(BizExceptionCode.OSS_004,
                        BizExceptionCode.PSS_011_MSG);
            }
            else if (SmsMessages.SEND_ERROR.name().equals(result)) {
                ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_012_MSG);
                throw new ServiceException(BizExceptionCode.OSS_005,
                        BizExceptionCode.PSS_012_MSG);
            }
            else if (SmsMessages.SMS_VERY_LONG.name().equals(result)) {
                ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_013_MSG);
                throw new ServiceException(BizExceptionCode.OSS_006,
                        BizExceptionCode.PSS_013_MSG);
            }
//			Approved
            if (cardRequestTO.getState() == CardRequestState.APPROVED &&
                    enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR)) {
                if (crqFlag == 7)
                    state = labels.getString("state.crqFlag7Approved");
                if (crqFlag == 5 || crqFlag == 6 || crqFlag == 4 ||
                        crqFlag == 3 || crqFlag == 1 || crqFlag == 2 || crqFlag == 0)
                    state = labels.getString("state.crqFlag");
            }

        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_044,
                    BizExceptionCode.CRE_044_MSG);
        }
        return state;
    }

    private String findCardRequestHistory(Long cardRequestId) throws BaseException {
        String state = "";
        try {
            List<String> crhResult = new ArrayList<String>();
            crhResult.add("Peson is dead");
            crhResult.add("{\"exceptions\":{\"marked\":{\"message\":\"Sanad_Is_Marked\",\"code\":\"400\"}}}");

            CardRequestHistoryTO cardRequestHistoryTO = getCardRequestHistoryDAO().findByCardRequestId(cardRequestId);
            if (cardRequestHistoryTO != null) {
                if (cardRequestHistoryTO.getResult() != null && cardRequestHistoryTO.getResult().contains("Peson is dead")) {
                    state = labels.getString("state.personIsDead");
                }
                if (cardRequestHistoryTO.getResult() != null && cardRequestHistoryTO.getResult().contains("{\"exceptions\":{\"marked\":{\"message\":\"Sanad_Is_Marked\",\"code\":\"400\"}}}")) {
                    state = labels.getString("state.SanadIsMarked");
                }
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_041,
                    BizExceptionCode.CRE_041_MSG);
        }

        return state;
    }

    private Boolean findReserved(CardRequestTO cardRequestTO) throws Exception {
        Boolean state = false;
        try {
            if (cardRequestTO.getEstelam2Flag() == Estelam2FlagType.V
                    && cardRequestTO.isPaid() == true
                    && cardRequestTO.getReservationDate().after(new Date())
                    && !cardRequestTO.getReservations().isEmpty())
                state = true;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_042,
                    BizExceptionCode.CRE_042_MSG);
        }
        return state;
    }

    private String findEnrollmentOffice(CardRequestTO cardRequestTO) throws BaseException {
        String state = "";
        String result;
        EnrollmentOfficeTO enrollmentOfficeTO;
        try {
            if (findReserved(cardRequestTO)) {
                enrollmentOfficeTO = cardRequestTO.getEnrollmentOffice();
                if (cardRequestTO.getEnrollmentOffice().getRatingInfo().getClazz().equals("0"))
                    state = labels.getString("state.disableEnrollmentOffice");
                else {
                    state = MessageFormat.format(
                                    labels.getString("state.enableEnrollmentOffice"),
                                    DateUtil.convert(cardRequestTO.getReservationDate(), DateUtil.JALALI));
                    String outgoingSMSTO = MessageFormat.format(
                                                labels.getString("state.sms.reserved"),
                                                DateUtil.convert(cardRequestTO.getReservationDate(), DateUtil.JALALI),
                                                enrollmentOfficeTO.getAddress());
                    try {
                        result = getNocrSmsService().send(
                                cardRequestTO.getCitizen().getCitizenInfo().getMobile(),
                                outgoingSMSTO);
                        ussdLogger.info("incoming result from NOCR SMS service is : " + result);

                        if (SmsMessages.INVALID_USER.name().equals(result))
                            ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_010_MSG);
                        else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result))
                            ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_011_MSG);
                        else if (SmsMessages.SEND_ERROR.name().equals(result))
                            ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_012_MSG);
                        else if (SmsMessages.SMS_VERY_LONG.name().equals(result))
                            ussdLogger.error("incoming result from NOCR SMS service is : " + BizExceptionCode.PSS_013_MSG);
                    } catch (Exception e) {
                        ussdLogger.error(e.getMessage(), e);
                    }
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            ussdLogger.error(e.getMessage(), e);
        }
        return state;
    }

    private String getState(CardRequestTO cardRequestTO) throws BaseException {
        String state = "";
        try {
            if (cardRequestTO.getState() == CardRequestState.RESERVED &&
                    cardRequestTO.getEstelam2Flag() == Estelam2FlagType.V &&
                    cardRequestTO.getReservationDate().before(new Date()) &&
                    cardRequestTO.getCitizen().getCitizenInfo().getAddress() == null &&
                    cardRequestTO.getCitizen().getCitizenInfo().getPhone() == null) {
                Long reservationHistoryCount = getService().countResrevationHistoryByCardRequestId(
                                                    cardRequestTO.getPortalRequestId());
                if (reservationHistoryCount >= 9)
                    state = labels.getString("state.multiReservation");
            }
            if (cardRequestTO.getState() == CardRequestState.RESERVED) {
                state = findEnrollmentOffice(cardRequestTO);
            }
            if (cardRequestTO.getEstelam2Flag() == Estelam2FlagType.N) {
                state = findCardRequestHistory(cardRequestTO.getId());
            }
            if (findReservationAttended(cardRequestTO)) {
                state = labels.getString("state.notAttend");
            }
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED ||
                    cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS ||
                    cardRequestTO.getState() == CardRequestState.APPROVED) {
                state = findCrqFlagByCardRequest(cardRequestTO);
            }

            if (cardRequestTO.getState() == CardRequestState.SENT_TO_AFIS) {
                state = labels.getString("state.sendToAFIS");
            }
            if (cardRequestTO.getState() == CardRequestState.APPROVED_BY_AFIS) {
                state = labels.getString("state.ApprovedByAFIS");
            }
            if (cardRequestTO.getState() == CardRequestState.PENDING_ISSUANCE)
                state = labels.getString("state.pendingIssuance");
            if (cardRequestTO.getState() == CardRequestState.CMS_PRODUCTION_ERROR)
                state = labels.getString("state.CMSProductionError");
            if (cardRequestTO.getState() == CardRequestState.ISSUED)
                state = labels.getString("state.Issued");
            if (cardRequestTO.getState() == CardRequestState.READY_TO_DELIVER)
                state = findReadyToDeliverState(cardRequestTO);
            switch (cardRequestTO.getState()) {
                case VERIFIED_IMS:
                    state = labels.getString("state.registered");
                    break;
                case PENDING_FOR_EMS:
                case RECEIVED_BY_EMS:
                case PENDING_IMS:
                    state = labels.getString("state.pendingForEmsOrIms");
                    break;
                case NOT_VERIFIED_BY_IMS:
                    state = labels.getString("state.notVerifiedByEms");
                    break;
                case PENDING_TO_DELIVER_BY_CMS:
                    state = labels.getString("state.pendingIssuance");
                    break;
                case DELIVERED:
                    state = labels.getString("state.deliver");
                    break;
                case UNSUCCESSFUL_DELIVERY:
                case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
                case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE:
                    state = labels.getString("state.unsuccessfulDeliveryDamageBiometric");
                    break;
                case STOPPED:
                    state = labels.getString("state.stopped");
                    break;
                case REPEALED:
                    state = labels.getString("state.repealed");
                    break;
                case CMS_ERROR:
                    state = labels.getString("state.CMSProductionError");
                    break;
                case IMS_ERROR:
                    state = labels.getString("state.imsError");
                    break;
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_048,
                    BizExceptionCode.CRE_048_MSG);
        } catch (ExternalInterfaceException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_047 + BizExceptionCode.CRE_047_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_047,
                    BizExceptionCode.CRE_047_MSG);
        }
        return state;
    }

    public CardRequestStateWS getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();

            String wsdlUrl = (String) pm.getProfile(
                    ProfileKeyName.KEY_CARD_REQUEST_STATE_WS_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(
                    ProfileKeyName.KEY_CARD_REQUEST_STATE_WS_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_CARD_REQUEST_STATE_WS_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_CARD_REQUEST_STATE_WS_NAMESPACE;
            String serviceName = "CardRequestStateWS";
            CardRequestStateWS port = ServicePorts.getCardRequestStatePort();
            if (port == null) {
                port = new CardRequestStateWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getCardRequestStatePort();
                ServicePorts.setCardRequestStatePort(port);
            }
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_055, BizExceptionCode.CRE_055_MSG, e);
        }
    }

    private SmsDelegate getNocrSmsService() throws BaseException {
        try {
            String wsdlUrl = EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_NOCR_SMS_ENDPOINT,
                    DEFAULT_NOCR_SMS_ENDPOINT);
            String namespace = EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_NOCR_SMS_NAMESPACE,
                    DEFAULT_NOCR_SMS_NAMESPACE);
            String serviceName = "SmsService";

            SmsDelegate smsDelegate = new SmsService(new URL(wsdlUrl),
                    new QName(namespace, serviceName)).getSmsPort();
            // SmsDelegate smsDelegate = new SmsService().getSmsPort();
            setNocrSmsUserCredential(smsDelegate);
            EmsUtil.setJAXWSWebserviceProperties(smsDelegate, wsdlUrl);
            return smsDelegate;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.OSS_002,
                    BizExceptionCode.PSS_003_MSG, e);
        }
    }


    private void setNocrSmsUserCredential(SmsDelegate smsDelegate) {
        String username =
                EmsUtil.getProfileValue(ProfileKeyName.KEY_NOCR_SMS_SERVICE_USERNAME,
                        DEFAULT_NOCR_SMS_SERVICE_USERNAME);
        String password = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_NOCR_SMS_SERVICE_PASSWORD,
                DEFAULT_NOCR_SMS_SERVICE_PASSWORD);

        /******************* UserName & Password ******************************/
        // *** this method use when authenticate SOAP request header and send to
        // server
        Map<String, Object> req_ctx = ((BindingProvider) smsDelegate)
                .getRequestContext();
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Username", Collections.singletonList(username));
        headers.put("Password", Collections.singletonList(password));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

}
