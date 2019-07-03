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
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsService;
import com.gam.nocr.ems.biz.service.external.impl.ims.NOCRIMSOnlineService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestReceiptVTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.PersonEnquiryWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.LangUtil;
import com.gam.nocr.ems.util.NationalIDUtil;
import com.gam.nocr.ems.util.Utils;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.security.SecurityContextService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

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
        cardRequestStateList.add(CardRequestState.VERIFIED_IMS);
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
                case FING_NORMAL_1:
                    result = true;
                    break;
                case FING_NORMAL_2:
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

    @Override
    public boolean hasPrintRegistrationReceipt() throws BaseException {
        try {
            SecurityContextService securityContextService = new SecurityContextService();
            if (securityContextService.hasAccess(userProfileTO.getUserName(),
                    "ems_PrintRegistrationReceipt")) {
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_077, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_077,
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
        labels = ResourceBundle.getBundle("ussd-request-state", new Locale("fa"));
        String state = "";
        if (!EmsUtil.checkString(trackingId)) {
            return labels.getString("state.trackingIdIsEmpty");
        }
        try {

            trackingId = LangUtil.getEnglishNumber(trackingId);
            CardRequestTO cardRequestTO = getCardRequestDAO().findCardRequestStateByTrackingId(trackingId);
            if (cardRequestTO == null) {
                state = labels.getString("state.invalidTrackingId");
            } else {
                state = getState(cardRequestTO);
            }
        } catch (BaseException e) {
            ussdLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_053,
                    BizExceptionCode.CRE_053_MSG, e);
        }
        return state;
    }

    @Override
    public String findCardRequestStateByNationalIdAndMobile(
            String nationalId, String mobile) throws BaseException {
        labels = ResourceBundle.getBundle("ussd-request-state", new Locale("fa"));
        String state = "";
        try {
            nationalId = LangUtil.getEnglishNumber(nationalId);
            if (!Utils.isValidNin(nationalId)) {
                state = labels.getString("state.invalidNationalId");
            } else {
                CardRequestTO cardRequestTO = getCardRequestDAO().findCardRequestStateByNationalId(nationalId);
                if (cardRequestTO == null) {
                    state = "-1";
                } else if (cardRequestTO != null) {
                    String mobileNumber = cardRequestTO.getCitizen().getCitizenInfo().getMobile();
                    if (EmsUtil.checkString(mobileNumber)) {
                        if (mobileNumber.equalsIgnoreCase(mobile)) {
                            state = getState(cardRequestTO);
                        } else {
                            state = "-1";
                        }
                    } else {
                        state = "-1";
                    }
                }
            }
        } catch (BaseException e) {
            ussdLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_054,
                    BizExceptionCode.CRE_054_MSG, e);
        }
        return state;
    }

    @Override
    public String findCardRequestStateByNationalIdAndBirthCertificateSeries(
            String nationalId, String birthCertificateSeries, String citizenBirthDate) throws BaseException {
        labels = ResourceBundle.getBundle("ussd-request-state", new Locale("fa"));
        String state = "";
        try {
            nationalId = LangUtil.getEnglishNumber(nationalId);
            if (!Utils.isValidNin(nationalId)) {
                state = labels.getString("state.invalidNationalId");
            } else if (!EmsUtil.checkString(birthCertificateSeries)) {
                state = labels.getString("state.nullBirthCertificateSeries");
            } else {
                CardRequestTO cardRequestTO =
                        getCardRequestDAO().findCardRequestStateByNationalId(nationalId);
                if (cardRequestTO != null) {
                    String series = cardRequestTO.getCitizen().getCitizenInfo().getBirthCertificateSeries();
                    String birthDate = cardRequestTO.getCitizen().getCitizenInfo().getBirthDateSolar();

                    //======================
                    birthDate = birthDate.replaceAll("/", "");
                    citizenBirthDate = citizenBirthDate.replaceAll("/", "");
                    //======================

                    if (!series.equalsIgnoreCase(birthCertificateSeries)) {
                        state = labels.getString("state.invalidBirthCertificateSeries");
                    } else if (!birthDate.equalsIgnoreCase(citizenBirthDate))
                        state = labels.getString("state.citizenBirthDate");
                    else {
                        state = getState(cardRequestTO);
                    }
                } else {
                    state = labels.getString("state.inWhiteList");
                }
            }

        } catch (BaseException e) {
            ussdLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_056,
                    BizExceptionCode.CRE_056_MSG, e);
        }
        return state;

    }

    private boolean findReservationAttended(CardRequestTO cardRequestTO) throws BaseException {
        boolean notAttended = false;
        String reservationRange = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_CCOS_RESERVATION_RANGE_TO_SHOW, "5");
        try {
            if (cardRequestTO.getState() == CardRequestState.RESERVED &&
                    (cardRequestTO.getEstelam2Flag() == Estelam2FlagType.V ||
                            cardRequestTO.getEstelam2Flag() == Estelam2FlagType.R) &&
                    cardRequestTO.getReservationDate().before(new Date()) &&
                    cardRequestTO.getAuthenticity() == null)
                notAttended = true;
        } catch (Exception e) {
            ussdLogger.error(e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_043,
                    BizExceptionCode.CRE_043_MSG, e);
        }
        return notAttended;
    }

    private Date incrementDateUtil(Date curentDate, Integer increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curentDate);
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }


    private String findReadyToDeliverState(CardRequestTO cardRequestTO) throws BaseException {
        try {
            EnrollmentOfficeTO eofByCardRequest = cardRequestTO.getEnrollmentOffice();
            if (EnrollmentOfficeType.NOCR.equals(eofByCardRequest.getType())) {
                String outgoingSMSTO = MessageFormat.format(
                        labels.getString("state.sms.readyToDeliverState"), cardRequestTO.getCitizen().getFirstNamePersian(),
                        cardRequestTO.getCitizen().getSurnamePersian()
                        , cardRequestTO.getCard().getBatch().getCmsID(),
                        eofByCardRequest.getName()
                        , eofByCardRequest.getAddress());
                sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                return labels.getString("state.readyToDeliverState");
            }
            if (EnrollmentOfficeType.OFFICE.equals(eofByCardRequest.getType()) &&
                    EnrollmentOfficeDeliverStatus.DISABLED.equals(eofByCardRequest.getDeliver()) &&
                    eofByCardRequest.getSuperiorOffice().getId() != null) {
                String outgoingSMSTO = MessageFormat.format(
                        labels.getString("state.sms.readyToDeliverState"), cardRequestTO.getCitizen().getFirstNamePersian(),
                        cardRequestTO.getCitizen().getSurnamePersian()
                        , cardRequestTO.getCard().getBatch().getCmsID(),
                        eofByCardRequest.getSuperiorOffice().getName()
                        , eofByCardRequest.getSuperiorOffice().getAddress());
                sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);

                return labels.getString("state.readyToDeliverState");

            }
            if (EnrollmentOfficeType.OFFICE.equals(eofByCardRequest.getType()) &&
                    EnrollmentOfficeDeliverStatus.ENABLED.equals(eofByCardRequest.getDeliver())) {
                String outgoingSMSTO = MessageFormat.format(
                        labels.getString("state.sms.readyToDeliverState"), cardRequestTO.getCitizen().getFirstNamePersian(),
                        cardRequestTO.getCitizen().getSurnamePersian()
                        , cardRequestTO.getCard().getBatch().getCmsID(),
                        eofByCardRequest.getName()
                        , eofByCardRequest.getAddress());
                sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                return labels.getString("state.readyToDeliverState");
            }
        } catch (Exception e) {
            ussdLogger.error(e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_046,
                    BizExceptionCode.CRE_046_MSG, e);
        }
        return "";
    }


    private String findCrqFlagByCardRequest(CardRequestTO cardRequestTO) throws BaseException {
        try {
            Integer crqFlag = getCardRequestDAO().fetchBiometricFlag(cardRequestTO.getId());
            CardRequestHistoryTO crhList = getCardRequestHistoryDAO().findByCardRequestId(cardRequestTO.getId());
            EnrollmentOfficeTO enrollmentOfficeTO = cardRequestTO.getEnrollmentOffice();
//			Condition 1
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                    CardRequestAuthenticity.AUTHENTIC.equals(cardRequestTO.getAuthenticity()) &&
                    cardRequestTO.getOriginalCardRequestOfficeId() == null) {
                if (crqFlag == 7) {
                    return labels.getString("state.crqFlag7");
                }
                if (crqFlag == 5) {
                    String outgoingSMSTO = MessageFormat.format(
                            labels.getString("state.sms.flag5"), enrollmentOfficeTO.getAddress());
                    sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                    return labels.getString("state.crqFlag5");
                }
                if (crqFlag == 4 || crqFlag == 0) {
                    String outgoingSMSTO = MessageFormat.format(
                            labels.getString("state.sms.flag4&0"), enrollmentOfficeTO.getAddress());
                    sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                    return labels.getString("state.crqFlag4&0");
                }

                if (crqFlag == 3 || crqFlag == 1 || crqFlag == 2 || crqFlag == 6)
                    return labels.getString("state.crqFlag");

                // 		Condition 2
                if (crhList.getResult() != null) {
                    if ("current state is CMS_PRODUCTION_ERROR".equals(crhList.getResult())) {
                        if (crqFlag == 6) {
                            String outgoingSMSTO = MessageFormat.format(
                                    labels.getString("state.sms.flag6"), enrollmentOfficeTO.getAddress());
                            sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                            return labels.getString("state.crqFlag6");

                        }
                    }
                }
            }
//			Condition 3
            if (crhList.getResult() != null) {
                if (cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS &&
                        (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                                enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                        "current state is CMS_PRODUCTION_ERROR".equals(crhList.getResult()) &&
                        CardRequestAuthenticity.AUTHENTIC.equals(cardRequestTO.getAuthenticity()) &&
                        cardRequestTO.getOriginalCardRequestOfficeId() == null) {
                    if (crqFlag == 6) {
                        String outgoingSMSTO = MessageFormat.format(
                                labels.getString("state.sms.flag6"), enrollmentOfficeTO.getAddress());
                        sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                        return labels.getString("state.crqFlag6");
                    }
                }
            }

//			Condition 5
            if (cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE)) &&
                    (CardRequestAuthenticity.NOT_AUTHENTIC.equals(cardRequestTO.getAuthenticity()) ||
                            cardRequestTO.getAuthenticity() == null) &&
                    cardRequestTO.getOriginalCardRequestOfficeId() == null) {
                if (crqFlag == 0)
                    return labels.getString("state.crqFlag");
            }
//			Condition 6
            if ((cardRequestTO.getAuthenticity() == CardRequestAuthenticity.NOT_AUTHENTIC ||
                    cardRequestTO.getAuthenticity() == null) &&
                    (enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR) ||
                            enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE))) {
                if (crqFlag == 1 || crqFlag == 2 || crqFlag == 3 || crqFlag == 4 ||
                        crqFlag == 5 || crqFlag == 6 || crqFlag == 7)
                    return labels.getString("state.crqFlag");
            }
//			condition 8
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED &&
                    enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.OFFICE) &&
                    cardRequestTO.getAuthenticity() == CardRequestAuthenticity.AUTHENTIC &&
                    cardRequestTO.getOriginalCardRequestOfficeId() != null
                    ) {
                if (crqFlag == 5) {
                    String outgoingSMSTO = MessageFormat.format(
                            labels.getString("state.sms.crqFlagOffice5"), enrollmentOfficeTO.getSuperiorOffice().getAddress());
                    sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                    return labels.getString("state.crqFlagOffice5");
                }
                if (crqFlag == 4 || crqFlag == 0) {
                    String outgoingSMSTO = MessageFormat.format(
                            labels.getString("state.sms.crqFlagOffice0"), enrollmentOfficeTO.getSuperiorOffice().getAddress());
                    sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);
                    return labels.getString("state.crqFlagOffice0");
                }
            }
//			Approved
            if (cardRequestTO.getState() == CardRequestState.APPROVED &&
                    enrollmentOfficeTO.getType().equals(EnrollmentOfficeType.NOCR)) {
                if (crqFlag == 7)
                    return labels.getString("state.crqFlag7Approved");
                if (crqFlag == 5 || crqFlag == 6 || crqFlag == 4 ||
                        crqFlag == 3 || crqFlag == 1 || crqFlag == 2 || crqFlag == 0)
                    return labels.getString("state.crqFlag");
            }

        } catch (BaseException e) {
            ussdLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_044,
                    BizExceptionCode.CRE_044_MSG, e);
        }
        return "";
    }

    private String findCardRequestHistory(Long cardRequestId) throws BaseException {
        try {
            List<String> crhResult = new ArrayList<String>();
            crhResult.add("Peson is dead");
            crhResult.add("{\"exceptions\":{\"marked\":{\"message\":\"Sanad_Is_Marked\",\"code\":\"400\"}}}");

            CardRequestHistoryTO cardRequestHistoryTO = getCardRequestHistoryDAO().findByCardRequestId(cardRequestId);
            if (cardRequestHistoryTO != null) {
                if (cardRequestHistoryTO.getResult() != null && cardRequestHistoryTO.getResult().contains("Peson is dead")) {
                    return labels.getString("state.personIsDead");
                }
                if (cardRequestHistoryTO.getResult() != null && cardRequestHistoryTO.getResult().contains("{\"exceptions\":{\"marked\":{\"message\":\"Sanad_Is_Marked\",\"code\":\"400\"}}}")) {
                    return labels.getString("state.SanadIsMarked");
                }
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_041,
                    BizExceptionCode.CRE_041_MSG, e);
        }

        return "";
    }

    private Boolean findReserved(CardRequestTO cardRequestTO) throws Exception {
        Boolean state = false;
        try {
            if ((cardRequestTO.getEstelam2Flag() == Estelam2FlagType.R ||
                    cardRequestTO.getEstelam2Flag() == Estelam2FlagType.V)
                    && cardRequestTO.getReservationDate().compareTo(new Date()) >= 0
                    && !cardRequestTO.getReservations().isEmpty()) {
                state = true;
            }
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_042,
                    BizExceptionCode.CRE_042_MSG, e);
        }
        return state;
    }

    private String findEnrollmentOffice(CardRequestTO cardRequestTO) throws BaseException {
        EnrollmentOfficeTO enrollmentOfficeTO;
        try {
            if (findReserved(cardRequestTO)) {
                enrollmentOfficeTO = cardRequestTO.getEnrollmentOffice();
                if (enrollmentOfficeTO.getActive().equals(Boolean.FALSE)) {
                    return labels.getString("state.disableEnrollmentOffice");
                } else {
                    String outgoingSMSTO = MessageFormat.format(
                            labels.getString("state.sms.reserved"),
                            cardRequestTO.getCitizen().getFirstNamePersian(),
                            cardRequestTO.getCitizen().getSurnamePersian(),
                            DateUtil.convert(cardRequestTO.getReservationDate(), DateUtil.JALALI),
                            enrollmentOfficeTO.getAddress());
                    sendSmsToCitizen(cardRequestTO.getCitizen().getCitizenInfo().getMobile(), outgoingSMSTO);

                    return MessageFormat.format(
                            labels.getString("state.enableEnrollmentOffice"),
                            DateUtil.convert(cardRequestTO.getReservationDate(), DateUtil.JALALI));
                }
            } else if (findReservationAttended(cardRequestTO)) {
                return labels.getString("state.notAttend");
            } else {
                return labels.getString("state.dontExistReservation");
            }
        } catch (Exception e) {
            ussdLogger.error(e.getMessage(), e);
        }
        return "";
    }

    private String getState(CardRequestTO cardRequestTO) throws BaseException {
        String state = "";
        try {
            if (cardRequestTO.getState() == CardRequestState.RESERVED ||
                    cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED ||
                    cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS) {
                return findEnrollmentOffice(cardRequestTO);
            }

            if (cardRequestTO.getEstelam2Flag() == Estelam2FlagType.N) {
                return findCardRequestHistory(cardRequestTO.getId());
            }
            /*if (findReservationAttended(cardRequestTO)) {
                return labels.getString("state.notAttend");
            }*/
            if (cardRequestTO.getState() == CardRequestState.DOCUMENT_AUTHENTICATED ||
                    cardRequestTO.getState() == CardRequestState.REFERRED_TO_CCOS ||
                    cardRequestTO.getState() == CardRequestState.APPROVED) {
                return findCrqFlagByCardRequest(cardRequestTO);
            }

            if (cardRequestTO.getState() == CardRequestState.SENT_TO_AFIS) {
                return labels.getString("state.sendToAFIS");
            }
            if (cardRequestTO.getState() == CardRequestState.APPROVED_BY_AFIS) {
                return labels.getString("state.ApprovedByAFIS");
            }
            if (cardRequestTO.getState() == CardRequestState.PENDING_ISSUANCE)
                return labels.getString("state.pendingIssuance");
            if (cardRequestTO.getState() == CardRequestState.CMS_PRODUCTION_ERROR)
                return labels.getString("state.CMSProductionError");
            if (cardRequestTO.getState() == CardRequestState.ISSUED) {
                if (cardRequestTO.getCard().getReceiveDate() != null) {
                    return labels.getString("state.Issued");
                } else {
                    return labels.getString("state.notReceipt");
                }
            }

            if (cardRequestTO.getState() == CardRequestState.READY_TO_DELIVER)
                return findReadyToDeliverState(cardRequestTO);
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
            ussdLogger.error(BizExceptionCode.CRE_048 + BizExceptionCode.CRE_048_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_048,
                    BizExceptionCode.CRE_048_MSG, e);
        } /*catch (ExternalInterfaceException_Exception e) {
            ussdLogger.error(BizExceptionCode.CRE_047 + BizExceptionCode.CRE_047_MSG + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_047,
                    BizExceptionCode.CRE_047_MSG, e);
        }*/
        return state;
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

    public void sendSmsToCitizen(String phoneNumber, String outgoingMessage) {
        String result = "";
        try {
            result = getNocrSmsService().send(
                    phoneNumber, outgoingMessage);
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

    /**
     * استعلام امکان درج ثبت نام تک مرحله ای
     *
     * @param nationalId
     * @param birthDateSolar
     * @param certSerialNo
     * @param gender
     */
    public void checkInsertSingleStageEnrollmentPossible(
            String nationalId, String birthDateSolar, String certSerialNo, GenderEnum gender) throws BaseException {
        CardRequestTO cardRequestTO = findLastRequestByNationalId(nationalId);
        checkThereAnyProcessedPreRegistration(cardRequestTO);
        checkThereAnyProcessedSingleStageEnrollment(cardRequestTO);
        checkThereAnyReproductionRequest(cardRequestTO);
        checkThereAnyReplaceRequest(cardRequestTO);
        checkThereAnyExtendRequest(cardRequestTO);
        checkHasCitizenAnyShippedCard(cardRequestTO);
        checkHasCitizenAnyDeliveredCard(cardRequestTO);
        checkCitizenLastCardExpired(cardRequestTO);
        checkCitizenPersonalInfoValid(nationalId, birthDateSolar, certSerialNo, gender);
    }


    /**
     * استعلام وجود ثبت نام در حال پردازش
     *
     * @param cardRequestTO
     */
    public void checkThereAnyProcessedPreRegistration(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestHistoryAction afisSendError = CardRequestHistoryAction.AFIS_SEND_ERROR;
        if (cardRequestTO != null) {
            if (cardRequestTO.getOrigin() == CardRequestOrigin.P) {
                if (!(cardRequestTO.getState().equals(CardRequestState.STOPPED)
                        || cardRequestTO.getState().equals(CardRequestState.IMS_ERROR)
                        || cardRequestTO.getState().equals(CardRequestState.REPEALED)
                        || (cardRequestTO.getState().equals(CardRequestState.APPROVED)
                        && getCardRequestHistoryService().findByCardRequestAndCrhAction(cardRequestTO.getId(), afisSendError)))) {
                    throw new ServiceException(BizExceptionCode.CRE_058, BizExceptionCode.CRE_058_MSG, new Object[]{cardRequestTO.getId()});
                }
            }
        }
    }

    /**
     * استعلام وجود ثبت نام تک مرحله ای در حال پردازش
     *
     * @param cardRequestTO
     */
    public void checkThereAnyProcessedSingleStageEnrollment(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestHistoryAction afisSendError = CardRequestHistoryAction.AFIS_SEND_ERROR;
        if (cardRequestTO != null) {
            if (cardRequestTO.getOrigin() == CardRequestOrigin.C) {
                if (!(cardRequestTO.getState().equals(CardRequestState.STOPPED)
                        || cardRequestTO.getState().equals(CardRequestState.IMS_ERROR)
                        || cardRequestTO.getState().equals(CardRequestState.REPEALED)
                        || (cardRequestTO.getState().equals(CardRequestState.APPROVED)
                        && getCardRequestHistoryService().findByCardRequestAndCrhAction(cardRequestTO.getId(), afisSendError)))) {
                    throw new ServiceException(BizExceptionCode.CRE_069, BizExceptionCode.CRE_069_MSG, new Object[]{cardRequestTO.getId()});
                }
            }

        }
    }

    /**
     * استعلام وجود ثبت نام(درخواست صدور مجدد)
     *
     * @param cardRequestTO
     */
    public void checkThereAnyReproductionRequest(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestHistoryAction afisSendError = CardRequestHistoryAction.AFIS_SEND_ERROR;
        if (cardRequestTO != null) {
            if (cardRequestTO.getType().equals(CardRequestType.REPLICA)) {
                if (!(cardRequestTO.getState().equals(CardRequestState.STOPPED)
                        || cardRequestTO.getState().equals(CardRequestState.IMS_ERROR)
                        || (cardRequestTO.getState().equals(CardRequestState.APPROVED)
                        && getCardRequestHistoryService().findByCardRequestAndCrhAction(cardRequestTO.getId(), afisSendError)))) {
                    throw new ServiceException(BizExceptionCode.CRE_059, BizExceptionCode.CRE_059_MSG, new Object[]{cardRequestTO.getId()});
                }
            }
        }
    }

    /**
     * استعلام وجود ثبت نام(درخواست المثنی)
     *
     * @param cardRequestTO
     */
    public void checkThereAnyReplaceRequest(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestHistoryAction afisSendError = CardRequestHistoryAction.AFIS_SEND_ERROR;
        if (cardRequestTO != null) {
            if (cardRequestTO.getType().equals(CardRequestType.REPLACE)) {
                if (!(cardRequestTO.getState().equals(CardRequestState.STOPPED)
                        || cardRequestTO.getState().equals(CardRequestState.IMS_ERROR)
                        || (cardRequestTO.getState().equals(CardRequestState.APPROVED)
                        && getCardRequestHistoryService().findByCardRequestAndCrhAction(cardRequestTO.getId(), afisSendError)))) {
                    throw new ServiceException(BizExceptionCode.CRE_075, BizExceptionCode.CRE_075_MSG, new Object[]{cardRequestTO.getId()});
                }
            }
        }
    }

    /**
     * استعلام وجود ثبت نام(درخواست صدور به علت تاریخ انقضا)
     *
     * @param cardRequestTO
     */
    public void checkThereAnyExtendRequest(CardRequestTO cardRequestTO) throws BaseException {
        CardRequestHistoryAction afisSendError = CardRequestHistoryAction.AFIS_SEND_ERROR;
        if (cardRequestTO != null) {
            if (cardRequestTO.getType().equals(CardRequestType.EXTEND)) {
                if (!(cardRequestTO.getState().equals(CardRequestState.STOPPED)
                        || cardRequestTO.getState().equals(CardRequestState.IMS_ERROR)
                        || (cardRequestTO.getState().equals(CardRequestState.APPROVED)
                        && getCardRequestHistoryService().findByCardRequestAndCrhAction(cardRequestTO.getId(), afisSendError)))) {
                    throw new ServiceException(BizExceptionCode.CRE_076, BizExceptionCode.CRE_076_MSG, new Object[]{cardRequestTO.getId()});
                }
            }
        }
    }

    /**
     * استعلام وجود کارت صادر شده برای شهروند
     *
     * @param cardRequestTO
     */
    public void checkHasCitizenAnyShippedCard(CardRequestTO cardRequestTO) throws ServiceException {
        if (cardRequestTO != null) {
            if (cardRequestTO.getState().equals(CardRequestState.ISSUED)
                    || cardRequestTO.getState().equals(CardRequestState.READY_TO_DELIVER)
                    || cardRequestTO.getState().equals(CardRequestState.DELIVERED)) {
                throw new ServiceException(BizExceptionCode.CRE_060, BizExceptionCode.CRE_060_MSG, new Object[]{cardRequestTO.getId()});
            }
        }
    }

    /**
     * استعلام وجود کارت تحویل شده برای شهروند
     *
     * @param cardRequestTO
     */
    public void checkHasCitizenAnyDeliveredCard(CardRequestTO cardRequestTO) throws ServiceException {
        if (cardRequestTO != null) {
            if (cardRequestTO.getState().equals(CardRequestState.DELIVERED)) {
                throw new ServiceException(BizExceptionCode.CRE_062, BizExceptionCode.CRE_062_MSG, new Object[]{cardRequestTO.getId()});
            }
        }
    }

    /**
     * استعلام وجود کارت منقضی شده  برای شهروند
     *
     * @param cardRequestTO
     */
    public void checkCitizenLastCardExpired(CardRequestTO cardRequestTO) throws ServiceException {
        if (cardRequestTO != null) {
            Calendar cal = Calendar.getInstance();
            if (cardRequestTO.getCard() != null) {
                cal.setTime(cardRequestTO.getCard().getIssuanceDate());
                cal.add(Calendar.YEAR, 7);
                if (!(cal.getTime().equals(new Date())
                        || cal.getTime().after(new Date()))) {
                    throw new ServiceException(BizExceptionCode.CRE_061, BizExceptionCode.CRE_061_MSG, new Object[]{cardRequestTO.getId()});
                }
            }
        }
    }

    /**
     * استعلام درستی مشخصات فردی
     *
     * @param nationalId
     * @param gender
     */
    public void checkCitizenPersonalInfoValid(String nationalId, String birthDateSolar
            , String certSerialNo, GenderEnum gender) throws BaseException {
        if (!NationalIDUtil.checkValidNinStr(nationalId)) {
            throw new ServiceException(BizExceptionCode.CRE_064, BizExceptionCode.CRE_064_MSG, new Object[]{"nationalId", nationalId});
        }
        if (StringUtils.isEmpty(birthDateSolar) || birthDateSolar.length() != 8) {
            throw new ServiceException(BizExceptionCode.CRE_065, BizExceptionCode.CRE_064_MSG, new Object[]{"birthDateSolar", birthDateSolar});
        }
        if (StringUtils.isEmpty(certSerialNo)) {
            throw new ServiceException(BizExceptionCode.CRE_066, BizExceptionCode.CRE_064_MSG, new Object[]{"certSerialNo", certSerialNo});
        }
        if (gender == null) {
            throw new ServiceException(BizExceptionCode.CRE_067, BizExceptionCode.CRE_064_MSG, new Object[]{"gender", gender});
        }
        PersonEnquiryWTO personEnquiryVTOResult = getIMSOnlineService().fetchDataByOnlineEnquiryAndCheck(nationalId, true);
        if (personEnquiryVTOResult.getSolarBirthDate() == null || !(certSerialNo.equals(String.valueOf(personEnquiryVTOResult.getBirthCertificateSerial()))
                && nationalId.equals(String.valueOf(personEnquiryVTOResult.getNationalId()))
                && birthDateSolar.equals(String.valueOf(personEnquiryVTOResult.getSolarBirthDate().replace("/", "")))
                && gender.getEmsCOde().equals(personEnquiryVTOResult.getGender().toString()))) {
            throw new ServiceException(BizExceptionCode.CRE_063, BizExceptionCode.CRE_063_MSG, new Object[]{nationalId});
        }
        if (calculateAge(personEnquiryVTOResult.getSolarBirthDate()) < 15) {
            throw new ServiceException(BizExceptionCode.CRE_090, BizExceptionCode.CRE_090_MSG, new Object[]{nationalId});
        }
    }

    public int calculateAge(String solarBirthDate) throws BaseException {
        try {
            Date currentDate = new Date();
            Date birthDate = DateUtil.convert(solarBirthDate, DateUtil.JALALI);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            int d1 = Integer.parseInt(formatter.format(birthDate));
            int d2 = Integer.parseInt(formatter.format(currentDate));
            int age = (d2 - d1) / 10000;
            return age;
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.CRE_089, BizExceptionCode.CRE_089_MSG);
        }
    }

    public CardRequestTO findLastRequestByNationalId(String nationalId)
            throws BaseException {
        try {
            return getCardRequestDAO().findLastRequestByNationalId(nationalId);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRE_068, BizExceptionCode.CRE_068_MSG, e);
        }
    }

    public CardRequestTO findByCitizenId(CitizenTO citizenTO) throws BaseException {
        try {
            return getCardRequestDAO().findByCitizenId(citizenTO);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_073, BizExceptionCode.CRE_073_MSG);
        }
    }

    public CardRequestTO update(CardRequestTO cardRequestTO) throws BaseException {
        try {
            return getCardRequestDAO().create(cardRequestTO);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_072, BizExceptionCode.CRE_072_MSG);
        }
    }

    private NOCRIMSOnlineService getIMSOnlineService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        NOCRIMSOnlineService nocrImsOnlineService;
        try {
            nocrImsOnlineService = serviceFactory.getService(EMSLogicalNames.getExternalIMSServiceJNDIName(EMSLogicalNames.SRV_NIO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.NIF_016,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_NIO.split(","));
        }
        return nocrImsOnlineService;
    }

    public PersonEnquiryWTO updateCitizenByEstelam(CardRequestTO cardRequestTO, boolean updateCardRequest, boolean isNewCardRequest) throws BaseException {
        String description = "";
        String imageDescription;
        boolean verified = false;
        String nationalIdStr = cardRequestTO.getCitizen().getNationalID();
        PersonEnquiryWTO personEnquiryVTOResult = getIMSOnlineService().fetchDataByOnlineEnquiryAndCheck(nationalIdStr, false);
        if (personEnquiryVTOResult.getIsServiceDown() || personEnquiryVTOResult.getIsEstelamCorrupt() || personEnquiryVTOResult.getIsExceptionMessage() || personEnquiryVTOResult.getIsDead() == 1) {

            if (personEnquiryVTOResult.getIsExceptionMessage()) {
                cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
                personEnquiryVTOResult.setNotVerified(true);
                getCardRequestDAO().update(cardRequestTO);
            }

            if (personEnquiryVTOResult.getIsDead() == 1) {
                cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
                personEnquiryVTOResult.setNotVerified(true);
                getCardRequestDAO().update(cardRequestTO);
            }

            description = personEnquiryVTOResult.getDescription();
            if (EmsUtil.checkString(description)) {
                if (!EmsUtil.checkMaxFieldLength(description, 600))
                    description = description.substring(0, 600);
            }
            getCardRequestHistoryService().create(cardRequestTO,
                    description, SystemId.EMS, null,
                    CardRequestHistoryAction.NOT_VERIFIED_BY_IMS, null);
            return personEnquiryVTOResult;
        }
        StringBuilder metadata = new StringBuilder();
        if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
            metadata.append(cardRequestTO.getPortalRequestId()).append("-");
            logEstelamFailed("Citizen", cardRequestTO.getPortalRequestId(), nationalIdStr, null);
            if (EmsUtil.checkString(personEnquiryVTOResult.getLogInfo())) {

                Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
                estelam2LogTO.setNationalID(nationalIdStr);
                estelam2LogTO.setCreateDate(new Date());
                EstelamFailureType type = EstelamFailureType.NOT_FOUND;
                description = personEnquiryVTOResult.getDescription();
                if (EmsUtil.checkString(description)) {
                    if (!EmsUtil.checkMaxFieldLength(description, 600))
                        description = description.substring(0, 600);
                }
                estelam2LogTO.setDescription(description);
                type = checkImsErrorType(personEnquiryVTOResult, type);
                estelam2LogTO.setType(type);
                logInsertIntoEstelam2FailureTable(estelam2LogTO, null, null);
                getEstelam2LogDAO().create(estelam2LogTO);
                verified = false;
            }
        } else {
            verified = true;
            imageDescription = personEnquiryVTOResult.getImageDescription();
            updateCitizen(cardRequestTO.getCitizen(), personEnquiryVTOResult, true, isNewCardRequest);
            if (EmsUtil.checkString(imageDescription)) {
                if (!EmsUtil.checkMaxFieldLength(imageDescription, 600))
                    imageDescription = imageDescription.substring(0, 600);
                getCardRequestHistoryService().create(cardRequestTO, imageDescription, SystemId.EMS, null, CardRequestHistoryAction.IMS_IMAGE_NOT_FOUND, null);
            }
        }
        CardRequestHistoryAction historyAction = null;
        if (verified) {
            historyAction = CardRequestHistoryAction.VERIFIED_IMS;
            cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
            personEnquiryVTOResult.setNotVerified(false);
        } else {
            historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
            cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
            personEnquiryVTOResult.setNotVerified(true);
        }
        description = personEnquiryVTOResult.getDescription();
        if (EmsUtil.checkString(description)) {
            if (!EmsUtil.checkMaxFieldLength(description, 600))
                description = description.substring(0, 600);
        }
        getCardRequestHistoryService().create(cardRequestTO, description, SystemId.EMS, null, historyAction, "");
        cardRequestTO.setMetadata(metadata.toString());
        if (updateCardRequest) {
            update(cardRequestTO);
        }
        logger.info("\n\nThe cardRequestTO has been set via IMS sub system.\n");
        return personEnquiryVTOResult;
    }


    private void updateCitizen(CitizenTO ctz, PersonEnquiryWTO personEnquiryVTO,
                               Boolean isImageRequested, boolean isNewCardRequest) throws BaseException {
        CitizenInfoTO czi = ctz.getCitizenInfo();
        ctz.setFirstNamePersian(personEnquiryVTO.getFirstName());
        ctz.setSurnamePersian(personEnquiryVTO.getLastName());
        czi.setBirthCertificateId(personEnquiryVTO.getBirthCertificateId());
        // Anbari: set Serial
        czi.setBirthCertificateSeries(personEnquiryVTO
                .getBirthCertificateSerial());
        // Anbari : set NIDImage to requested citizen nidImage
        // field
        if (isImageRequested && personEnquiryVTO.getNidImage() != null) {
            try {
                getImsManagementervice().saveImsEstelamImage(
                        ctz.getNationalID(), ctz, ImsEstelamImageType.IMS_NID_IMAGE,
                        personEnquiryVTO.getNidImage(), isNewCardRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        czi.setFatherFirstNamePersian(personEnquiryVTO.getFatherName());
        czi.setGender(personEnquiryVTO.getGender());
        //Anbari: overwrite birthDate
        czi.setBirthDateSolar(personEnquiryVTO.getSolarBirthDate());
        try {
            czi.setBirthDateGregorian(DateUtil.convert(personEnquiryVTO.getSolarBirthDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            e.printStackTrace();
        }
        insertLogForCitizenInfo(ctz, czi);

        getCitizenDAO().update(ctz);
        getCitizenInfoDAO().update(czi);
    }

    /**
     * getCitizenInfoDAO
     *
     * @return an instance of type CitizenInfoDAO
     */
    private CitizenInfoDAO getCitizenInfoDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CitizenInfoDAO citizenInfoDAO;
        try {
            citizenInfoDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_004,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CITIZEN_INFO.split(","));
        }
        return citizenInfoDAO;
    }

    private void logEstelamFailed(String person, Long reqId,
                                  String citizenNationalID, String nId) {
        logger.info("\n======================== Estelam Failed For " + person + "========================\n");
        logger.info("\nRequest Id : " + reqId + "\n");
        logger.info("\nCitizen NID : " + citizenNationalID + "\n");
        if (!person.equals("Citizen")) {
            logger.info("\nCitizen " + person + " NID : " + nId + "\n");
        }
    }

    private EstelamFailureType checkImsErrorType(
            PersonEnquiryWTO personEnquiryVTO, EstelamFailureType type) {
        if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_029)) {
            logPersonNotFound("Citizen");
            type = EstelamFailureType.NOT_FOUND;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_010)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_021)) {
            type = EstelamFailureType.INVAILD_CERTIFICATE_ID;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_011)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_022)) {
            type = EstelamFailureType.INVAILD_BIRTH_DATE;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_012)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_023)) {
            type = EstelamFailureType.INVAILD_CERTIFICATE_SERIAL;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_013)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_024)) {
            type = EstelamFailureType.INVAILD_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_014)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_025)) {
            type = EstelamFailureType.INVAILD_LAST_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_015)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_026)) {
            type = EstelamFailureType.INVAILD_FATHER_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_016)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_027)) {
            type = EstelamFailureType.INVAILD_GENDER;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_017)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_028)) {
            type = EstelamFailureType.UNKNOWN_IMS_MESSAGE;
        }
        return type;
    }

    private void logInsertIntoEstelam2FailureTable(Estelam2FailureLogTO estelam2Log, Gender imsGender, Gender citizenGender) {
        logger.info("\n======================== Insert Into Estelam 2 Failure Table ========================\n");
        if (citizenGender != null && imsGender != null) {
            logger.info("\n Citizen Gender : " + citizenGender + "\n");
            logger.info("\n Ims Gender : " + imsGender + "\n");
        }
        logger.info("\nestelam2Log NID : " + estelam2Log.getNationalID() + "\n");
        logger.info("\nestelam2Log createDate : " + estelam2Log.getCreateDate() + "\n");
        logger.info("\nestelam2Log type : " + estelam2Log.getType() + "\n");
        logger.info("\n=====================================================================================\n");
    }

    private Estelam2FailureLogDAO getEstelam2LogDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        Estelam2FailureLogDAO estelam2LogDAO = null;
        try {
            estelam2LogDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_ESTELAM2_FAILURE_LOG));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_057,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ESTELAM2_FAILURE_LOG.split(","));
        }
        return estelam2LogDAO;
    }

    private void insertLogForCitizenInfo(CitizenTO ctz, CitizenInfoTO czi) {
        logger.info("\nCitizen FirstName : " + ctz.getFirstNamePersian() + "\n");
        logger.info("\nCitizen LastName : " + ctz.getSurnamePersian() + "\n");
        logger.info("\nCitizen BirthCertId : " + czi.getBirthCertificateId()
                + "\n");
        logger.info("\nCitizen BirthCertSerial : "
                + czi.getBirthCertificateSeries() + "\n");
        logger.info("\nCitizen BirthDateSolar : " + czi.getBirthDateSolar()
                + "\n");
    }

    private IMSManagementService getImsManagementervice()
            throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        IMSManagementService imsManagementService = null;
        try {
            imsManagementService = (IMSManagementService) factory
                    .getService(EMSLogicalNames
                            .getServiceJNDIName(EMSLogicalNames.SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.IMD_001,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_IMS_MANAGEMENT.split(","));
        }
        return imsManagementService;
    }

    private void logPersonNotFound(String person) {
        logger.info("\n=====================================================================================\n");
        logger.info("\n======================== " + person + " Not Found In Ims ========================\n");
        logger.info("\n=====================================================================================\n");
    }

    public CardRequestTO addCardRequest(CardRequestTO entity) throws BaseException {
        CardRequestTO newCardRequest = null;
        try {
            entity.setType(CardRequestType.FIRST_CARD);
            entity.setState(CardRequestState.RESERVED);
            entity.setEstelam2Flag(Estelam2FlagType.R);
            entity.setRequestedSmsStatus(0);
            entity.setOriginalCardRequestOfficeId(null);
            checkCardRequestValid(entity);

            if (entity.getId() == null) {
                if (entity.getTrackingID() == null || entity.getTrackingID().trim().length() == 0 ||
                        entity.getTrackingID().equals("0000000000")) {
                    //entity.setTrackingID(EmsUtil.generateTrackingId(entity.getCitizen().getNationalID()));
                    entity.setTrackingID(generateNewTrackingId());
                }
                newCardRequest = getCardRequestDAO().create(entity);
            }

            return newCardRequest;

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            String nationalId = entity.getCitizen() != null ? entity.getCitizen().getNationalID() : null;
            logger.error(BizExceptionCode.RSI_089_MSG,
                    new Object[]{newCardRequest.getId()},
                    new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new ServiceException(
                    BizExceptionCode.RSI_089, BizExceptionCode.RSI_089_MSG, e, new Object[]{newCardRequest.getId()});
        }
    }

    private void checkCardRequestValid(CardRequestTO crq) throws BaseException {
        String nationalId = crq.getCitizen() != null ? crq.getCitizen().getNationalID() : null;
        if (crq == null) {
            throw new ServiceException(BizExceptionCode.RSI_006, BizExceptionCode.RSI_006_MSG, new Object[]{nationalId});
        }
        if (crq.getState() == null) {
            throw new ServiceException(BizExceptionCode.RSI_012, BizExceptionCode.RSI_012_MSG, new Object[]{nationalId});
        }
        if (crq.getType() == null) {
            throw new ServiceException(BizExceptionCode.RSI_013, BizExceptionCode.RSI_013_MSG, new Object[]{nationalId});
        }
        if (!EmsUtil.checkString(crq.getTrackingID()) && (crq.getTrackingID().length() != 10)) {
            throw new ServiceException(BizExceptionCode.RSI_014, BizExceptionCode.RSI_014_MSG, new Object[]{nationalId});
        }
    }

    private CardRequestHistoryService getCardRequestHistoryService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestHistoryService cardRequestHistoryService;
        try {
            cardRequestHistoryService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST_HISTORY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_081,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST_HISTORY.split(","));
        }
        cardRequestHistoryService.setUserProfileTO(getUserProfileTO());
        return cardRequestHistoryService;
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

    public Long countCardRequestByNationalIdAndType(String nationalId, CardRequestType type) throws BaseException {
        Long replicaTypeCount = null;
        try {
            replicaTypeCount = getCardRequestDAO().countCardRequestByNationalIdAndType(nationalId, type);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_074, BizExceptionCode.CRE_074_MSG);
        }
        return replicaTypeCount;
    }


    public CardRequestReceiptVTO printRegistrationReceipt(long cardRequestId) throws BaseException {
        CardRequestReceiptVTO cardRequestReceiptVTO = new CardRequestReceiptVTO();
        try {
            CardRequestTO cardRequestTO = loadById(cardRequestId);
            if (CardRequestState.IMS_ERROR.equals(cardRequestTO.getState())) {
                throw new ServiceException(BizExceptionCode.CRE_083,
                        BizExceptionCode.CRE_083_MSG, new Object[]{cardRequestTO.getState()});
            } else if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
                throw new ServiceException(BizExceptionCode.CRE_084,
                        BizExceptionCode.CRE_083_MSG, new Object[]{cardRequestTO.getState()});
            } else if (CardRequestState.DELIVERED.equals(cardRequestTO.getState())) {
                throw new ServiceException(BizExceptionCode.CRE_085,
                        BizExceptionCode.CRE_083_MSG, new Object[]{cardRequestTO.getState()});
            } else if (CardRequestState.RESERVED.equals(cardRequestTO.getState())) {
                throw new ServiceException(BizExceptionCode.CRE_086,
                        BizExceptionCode.CRE_083_MSG, new Object[]{cardRequestTO.getState()});
            } else {
                Long personID = getPersonService().findPersonIdByUsername(getUserProfileTO().getUserName());
                PersonTO personTO = getPersonService().find(personID);
                cardRequestReceiptVTO.setCitizenFirstName(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getFirstNamePersian() : "");
                cardRequestReceiptVTO.setNationalID(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getNationalID() : "");
                cardRequestReceiptVTO.setEnrolledDate(cardRequestTO.getEnrolledDate() != null ? DateUtil.convert(cardRequestTO.getEnrolledDate(), DateUtil.JALALI) : "");
                cardRequestReceiptVTO.setCitizenSurname(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getSurnamePersian() : "");
                cardRequestReceiptVTO.setBirthCertificateId(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getCitizenInfo().getBirthCertificateId() : "");
                cardRequestReceiptVTO.setTrackingID(cardRequestTO.getTrackingID() != null ? cardRequestTO.getTrackingID() : "");
                cardRequestReceiptVTO.setFatherName(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getCitizenInfo().getFatherFirstNamePersian() : "");
                cardRequestReceiptVTO.setBirthDateSolar(cardRequestTO.getCitizen() != null ? cardRequestTO.getCitizen().getCitizenInfo().getBirthDateSolar() : "");
                cardRequestReceiptVTO.setReceiptDate(DateUtil.convert(new Date(), DateUtil.JALALI));
                cardRequestReceiptVTO.setUserFirstName(personTO.getFirstName() != null ? personTO.getFirstName() : "");
                cardRequestReceiptVTO.setUserLastName(personTO.getLastName() != null ? personTO.getLastName() : "");
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_079,
                    BizExceptionCode.CRE_079_MSG, e);
        }
        return cardRequestReceiptVTO;
    }

    public void print(Long cardRequestId) throws BaseException {
        try {
            getCardRequestHistoryService().create(
                    new CardRequestTO(cardRequestId),
                    "Registration receipt is printed"
                    , SystemId.EMS
                    , cardRequestId.toString()
                    , null
                    , getUserProfileTO().getUserName());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CRE_082,
                    BizExceptionCode.CRE_082_MSG, e);
        }
    }

    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.CRE_080, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }

    public String generateNewTrackingId() throws BaseException {
        Number nextValue = null;
        try {
            nextValue = getCardRequestDAO().nextValueOfRequestTrackingId();
        } catch (BaseException e) {
            throw e;
        }

        if (nextValue == null) {
            logger.error("Error Occurred in get next value of sequence of TrackingId. NextValue is null. ");
            throw new ServiceException(
                    BizExceptionCode.CRE_087,
                    BizExceptionCode.CRE_087_MSG);
        }

        try {
            Long nextValueAsLong = nextValue.longValue();
            String str = String.valueOf(nextValueAsLong);
            char[] charArray = str.toCharArray();
            int sumOfNumber = 0;
            for (int i = 0; i < charArray.length; i++) {
                sumOfNumber += Character.getNumericValue(charArray[i]);
            }
            long mod = sumOfNumber % 10;


            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(nextValueAsLong)).append(String.valueOf(mod));
            return stringBuilder.toString();
        } catch (Exception e) {
            logger.error("Error Occurred in generating TrackingId:" + e.getMessage());

            throw new ServiceException(
                    BizExceptionCode.CRE_088,
                    BizExceptionCode.CRE_088_MSG,
                    e);
        }
    }

}
