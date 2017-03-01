package com.gam.nocr.ems.biz.service.internal.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_BIOMETRIC;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CARD_REQUEST;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CARD_REQUEST_HISTORY;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_DOCUMENT;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_ENROLLMENT_OFFICE;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_RESERVATION;
import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_REGISTRATION;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;
import static com.gam.nocr.ems.config.EMSLogicalNames.getServiceJNDIName;
import gampooya.tools.security.SecurityContextService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;

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
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.IMSManagementService;
import com.gam.nocr.ems.biz.service.PortalManagementService;
import com.gam.nocr.ems.biz.service.RegistrationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.BiometricDAO;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.dao.DocumentDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.ReservationDAO;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.Estelam2FlagType;
import com.gam.nocr.ems.data.enums.SMSTypeState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

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

	@Resource
	SessionContext sessionContext;

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

	private CardRequestHistoryDAO getCardRequestHistoryDAO()
			throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_CARD_REQUEST_HISTORY));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.CRE_003,
					BizExceptionCode.GLB_001_MSG, e,
					DAO_CARD_REQUEST_HISTORY.split(","));
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
    
    
 // Anbari
 	@Override
 	@Permissions(value = "ems_cmsErrorDeleteImage")
 	public void doImageDeleteAction(List<Long> cardRequestIds, Long citizenId,
 			CardRequestState state) throws BaseException {
 		
 		try {
 			getBiometricDAO().removeFaceInfoByCitizenId(citizenId);
 			sessionContext.getBusinessObject(CardRequestServiceLocal.class).updateCardRequestForDocumentAuthenticateState(cardRequestIds.get(0),state); 
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
 	public void updateCardRequestForDocumentAuthenticateState(Long cardRequestId,CardRequestState state) throws BaseException 
 	{
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
	
//ganjyar
	@Override
	public AccessProductionVTO getAccessProduction() throws BaseException {

		try {
			AccessProductionVTO accessProductionVTO = new AccessProductionVTO();

			SecurityContextService securityContextService = new SecurityContextService();
			if (securityContextService.hasAccess(userProfileTO.getUserName(),
					"ems_cmsErrorDeleteImage")) {
				accessProductionVTO.setErrorDeleteImageAccess(true);

			}
			if (securityContextService.hasAccess(userProfileTO.getUserName(),
					"ems_cmsErrorRepealed")) {
				accessProductionVTO.setErrorRepealedAccess(true);
			}
			if (securityContextService.hasAccess(userProfileTO.getUserName(),
					"ems_cmsErrorRetry")) {

				accessProductionVTO.setErrorRetryAccess(true);
			}

			return accessProductionVTO;

		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CRE_021,
					BizExceptionCode.GLB_008_MSG, e);
		}

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
		
		try{
			
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
	 * @author ganjyar
	 * 
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
}
