package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.biz.service.external.client.portal.CardRequestWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.ItemWTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.ReservationVTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateUtil;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.UserTransaction;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */

@Stateless(name = "PortalManagementService")
@Local(PortalManagementServiceLocal.class)
@Remote(PortalManagementServiceRemote.class)
public class PortalManagementServiceImpl extends EMSAbstractService implements
		PortalManagementServiceLocal, PortalManagementServiceRemote {

	@Resource
	SessionContext sessionContext;

	private static final Logger logger = BaseLog
			.getLogger(PortalManagementServiceImpl.class);
	private static final Logger jobLOGGER = BaseLog
			.getLogger("portalUpdateCardRequest");

	@Resource
	UserTransaction utx;

	/**
	 * =============== Getter for DAOs ===============
	 */

	/**
	 * getCardRequestDAO
	 * 
	 * @return an instance of type CardRequestDAO
	 * @throws {@link BaseException}
	 */
	private CardRequestDAO getCardRequestDAO() throws BaseException {
		DAOFactory factory = DAOFactoryProvider.getDAOFactory();
		CardRequestDAO cardRequestDAO;
		try {
			cardRequestDAO = factory.getDAO(EMSLogicalNames
					.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_002,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CARD_REQUEST.split(","));
		}
		return cardRequestDAO;
	}
	
	private CitizenInfoDAO getCitizenInfoDAO() throws BaseException {
		DAOFactory factory = DAOFactoryProvider.getDAOFactory();
		CitizenInfoDAO citizenInfoDAO;
		try {
			citizenInfoDAO = factory.getDAO(EMSLogicalNames
					.getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN_INFO));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_024,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CITIZEN_INFO.split(","));
		}
		return citizenInfoDAO;
	}

	private CardRequestHistoryDAO getCardRequestHistoryDAO()
			throws BaseException {
		try {
			return DAOFactoryProvider
					.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_018,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
		}
	}

	/**
	 * getReservationDAO
	 * 
	 * @return an instance of type ReservationDAO
	 * @throws {@link BaseException}
	 */
	private ReservationDAO getReservationDAO() throws BaseException {
		DAOFactory factory = DAOFactoryProvider.getDAOFactory();
		ReservationDAO reservationDAO;
		try {
			reservationDAO = factory.getDAO(EMSLogicalNames
					.getDaoJNDIName(EMSLogicalNames.DAO_RESERVATION));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_006,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_RESERVATION.split(","));
		}
		return reservationDAO;
	}

	private LocationDAO getLocationDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_LOCATION));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_006,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_LOCATION.split(","));
		}
	}

	/**
	 * =================== Getter for Services ===================
	 */

	/**
	 * getPortalRegistrationService
	 * 
	 * @return an instance of type {@link PortalRegistrationService}
	 * @throws {@link BaseException}
	 */
	private PortalRegistrationService getPortalRegistrationService()
			throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		PortalRegistrationService portalRegistrationService;
		try {
			portalRegistrationService = serviceFactory
					.getService(EMSLogicalNames
							.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_005,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
		}
		portalRegistrationService.setUserProfileTO(getUserProfileTO());
		return portalRegistrationService;
	}

	private RegistrationService getRegistrationService() throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		RegistrationService registrationService;
		try {
			registrationService = serviceFactory.getService(EMSLogicalNames
					.getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_005,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
		}
		registrationService.setUserProfileTO(getUserProfileTO());
		return registrationService;
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
					EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
		}
		cardRequestService.setUserProfileTO(getUserProfileTO());
		return cardRequestService;
	}

	/**
	 * getPortalReservationService
	 * 
	 * @return an instance of type {@link PortalReservationService}
	 * @throws {@link BaseException}
	 */
	private PortalReservationService getPortalReservationService()
			throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		PortalReservationService portalReservationService = null;
		try {
			portalReservationService = serviceFactory
					.getService(EMSLogicalNames
							.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_RESERVATION), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_016,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
		}
		portalReservationService.setUserProfileTO(getUserProfileTO());
		return portalReservationService;
	}

	/**
	 * getPortalBaseInfoService
	 * 
	 * @return an instance of type {@link PortalBaseInfoService}
	 * @throws {@link BaseException}
	 */
	private PortalBaseInfoService getPortalBaseInfoService()
			throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		PortalBaseInfoService portalBaseInfoService;
		try {
			portalBaseInfoService = serviceFactory
					.getService(EMSLogicalNames
							.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_BASE_INFO), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_012,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
		}
		portalBaseInfoService.setUserProfileTO(getUserProfileTO());
		return portalBaseInfoService;
	}

	private PortalSmsService getPortalSmsService() throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		PortalSmsService portalSmsService;
		try {
			portalSmsService = serviceFactory
					.getService(EMSLogicalNames
							.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_SMS), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_017,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PORTAL_SMS.split(","));
		}
		portalSmsService.setUserProfileTO(getUserProfileTO());
		return portalSmsService;
	}

	/**
	 * getBusinessLogService
	 * 
	 * @return an instance of type {@link BusinessLogService}
	 * @throws BaseException
	 */
	private BusinessLogService getBusinessLogService() throws BaseException {
		ServiceFactory serviceFactory = ServiceFactoryProvider
				.getServiceFactory();
		BusinessLogService businessLogService;
		try {
			businessLogService = serviceFactory.getService(EMSLogicalNames
					.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new ServiceException(BizExceptionCode.PTL_019,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
		}
		return businessLogService;
	}

	/**
	 * The method updateCardRequestStates is used to alert the sub system
	 * 'Portal' about the current state of the request.
	 * 
	 * @param syncCardRequestWTOList
	 *            a list of type
	 *            {@link com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO}
	 *            which carries a number of portalRequestId with the specified
	 *            state for each id
	 * @return an object of type {@Boolean} which represents whether
	 *         the batch update is done correctly or not
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	private List<ItemWTO> updateCardRequestStates(
			List<SyncCardRequestWTO> syncCardRequestWTOList)
			throws BaseException {
		return getPortalRegistrationService().updateCardRequestsState(
				syncCardRequestWTOList);
	}

	/**
	 * @param logAction
	 * @param logEntityName
	 * @param logActor
	 * @param additionalData
	 * @throws BaseException
	 */
	private void createBusinessLog(BusinessLogAction logAction,
			BusinessLogEntity logEntityName, String logActor,
			String additionalData, BusinessLogActionAttitude actionAttitude)
			throws BaseException {
		BusinessLogTO businessLogTO = new BusinessLogTO();
		businessLogTO.setEntityID(" ");
		businessLogTO.setAction(logAction);
		businessLogTO.setEntityName(logEntityName);
		businessLogTO.setActor(logActor);
		businessLogTO.setAdditionalData(additionalData);
		businessLogTO.setDate(new Timestamp(new Date().getTime()));
		businessLogTO.setActionAttitude(actionAttitude);
		getBusinessLogService().insertLog(businessLogTO);

	}

	/**
	 * The method transferCardRequests is used to get a number of card request
	 * from the sub system 'Portal'
	 * 
	 * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	@Override
	public List<CardRequestTO> transferCardRequests(
			List<Long> portalCardRequestIds) throws BaseException {
		return getPortalRegistrationService().transferCardRequests(
				portalCardRequestIds);
	}

	/**
	 * The method doActivityForUpdateState is used to prepare a list of type
	 * {@link com.gam.nocr.ems.data.domain.ws.ItemWTO} and calls the method
	 * 'updateCardRequestStates' to notify the sub system 'Portal' about the
	 * state of each request. This method is triggered by a specified job.
	 * 
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean doActivityForUpdateState(List<Long> batchIds)
			throws BaseException {
		Boolean loopFlag = true;

		try {

			List<SyncCardRequestWTO> syncCardRequestWTOList = getCardRequestDAO()
					.getRequestListForUpdateState(batchIds);
			/*List<ItemWTO> itemWTOList = getPortalRegistrationService()
					.updateCardRequestsState(syncCardRequestWTOList);
			sessionContext
					.getBusinessObject(PortalManagementServiceLocal.class)
					.updateState(syncCardRequestWTOList, itemWTOList);*/

			return loopFlag;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PTL_008,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateState(List<SyncCardRequestWTO> syncCardRequestWTOList,
			List<ItemWTO> itemWTOList) throws BaseException {
		try {

			List<Long> successfulIds = new ArrayList<Long>();
			Map<Long, String> failedDetails = new HashMap<Long, String>();

			List<Long> portalCardRequestIdListToUpdate = new ArrayList<Long>();
			List<Long> portalCardRequestIdListToWait = new ArrayList<Long>();

			if (EmsUtil.checkListSize(itemWTOList)) {
				for (ItemWTO itemWTO : itemWTOList) {
					if ("updated".equals(itemWTO.getValue())) {
						successfulIds.add(Long.valueOf(itemWTO.getKey()));
					} else {
						failedDetails.put(Long.valueOf(itemWTO.getKey()),
								itemWTO.getValue());
					}
				}
			}

			for (SyncCardRequestWTO syncCardRequestWTO : syncCardRequestWTOList) {
				if (successfulIds.contains(syncCardRequestWTO.getId())) {
					portalCardRequestIdListToUpdate.add(syncCardRequestWTO
							.getId());
				} else {
					portalCardRequestIdListToWait.add(syncCardRequestWTO
							.getId());
				}
			}

			if (EmsUtil.checkListSize(portalCardRequestIdListToUpdate)) {
				getCardRequestDAO().updateSyncDatesAndFlag(
						portalCardRequestIdListToUpdate);
				createBusinessLog(
						BusinessLogAction.UPDATE_REQUEST_STATES,
						BusinessLogEntity.PORTAL,
						"System",
						EmsUtil.toJSON("portalCardRequestIds:"
								+ portalCardRequestIdListToUpdate),
						BusinessLogActionAttitude.T);
				jobLOGGER.info("Successfully updated card requests: "
						+ portalCardRequestIdListToUpdate);
				for (Long portalRequestId : portalCardRequestIdListToUpdate) {
					CardRequestTO cardRequestTO = getCardRequestDAO()
							.findByPortalRequestId(portalRequestId);
					getCardRequestHistoryDAO().create(
							cardRequestTO,
							"current state is "
									+ cardRequestTO.getState().name(),
							SystemId.PORTAL, null,
							CardRequestHistoryAction.SYNC_SUCCESS, null);
				}
			}
			if (EmsUtil.checkListSize(portalCardRequestIdListToWait)) {
				getCardRequestDAO().updateLockDatesAndFlag(
						portalCardRequestIdListToWait);
				createBusinessLog(
						BusinessLogAction.UPDATE_REQUEST_STATES,
						BusinessLogEntity.PORTAL,
						"System",
						EmsUtil.toJSON("portalCardRequestIds:"
								+ portalCardRequestIdListToWait),
						BusinessLogActionAttitude.F);
				jobLOGGER.error("Update card request failed for : "
						+ portalCardRequestIdListToWait);
				for (Long portalRequestId : portalCardRequestIdListToWait) {
					String result = "";
					String separator = System.getProperty("line.separator");
					String err = failedDetails.get(portalRequestId);
					if (err != null) {
						if (err.contains("PRT_D_CRI_016")) {
							try {
								int beginIndex = err
										.indexOf("State Transition");
								int endIndex = err.indexOf("Is Not Defined");
								result = err.substring(beginIndex,
										endIndex + 15);
							} catch (Exception e) {
								result = "PRT_D_CRI_016 - Undefined"
										+ separator + err.substring(0, Math.min(err.length(),100));
							}
						} else if (err.contains("PRT_D_CRI_008")) {
							try {
								int beginIndex = err.lastIndexOf("Caused by");
								int endIndex = err.indexOf(separator,
										beginIndex);
								result = "PRT_D_CRI_008" + separator
										+ err.substring(beginIndex, endIndex);
							} catch (Exception e) {
								result = "PRT_D_CRI_008 - Undefined"
										+ separator + err.substring(0, Math.min(err.length(),100));
							}
						} else {
							try {
								int beginIndex = err.lastIndexOf("Caused by");
								int endIndex = err.indexOf(separator,
										beginIndex);
								result = err.substring(beginIndex, endIndex);
							} catch (Exception e) {
								result = "Undefined-" + separator
										+ err.substring(0, Math.min(err.length(),100));
							}
						}
						if (result.length() > 255) {
							result = result.substring(0, 255);
						}
					}
					jobLOGGER.error("The error for portal_request_id =  "
							+ portalRequestId + " is : " + separator + err);
					CardRequestTO cardRequestTO = getCardRequestDAO()
							.findByPortalRequestId(portalRequestId);
					getCardRequestHistoryDAO().create(cardRequestTO, result,
							SystemId.PORTAL, null,
							CardRequestHistoryAction.SYNC_FAILED, null);
				}
			}

		} catch (BaseException e) {
			sessionContext.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			sessionContext.setRollbackOnly();
			throw new ServiceException(BizExceptionCode.PTL_014,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	public List<Long> fetchReservationIds() throws BaseException {
		return getPortalReservationService().fetchReservationIds();
	}

	/**
	 * The method doActivityForReservations is used to get a number of
	 * reservations from the sub system 'Portal'
	 * 
	 * @return an instance of type {@link String}, which is applicable in
	 *         logging
	 * @throws com.gam.commons.core.BaseException
	 * @deprecated {@link PortalManagementServiceImpl#transferReservationsToEMSAndDoEstelam2(List)}
	 */
	@Override
	// @BizLoggable(logAction = "FETCH_RESERVATION_INFO", logEntityName =
	// "PORTAL", logActor = "System")
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Boolean doActivityForReservations(List<Long> reservationIds)
			throws BaseException {
		boolean loopFlag = true;

		List<ReservationTO> reservationTOList = getPortalReservationService()
				.transferReservations(reservationIds);

		HashMap<Long, Long> cardRequestTOIdsForUpdateOffice = new HashMap<Long, Long>();
		List<Long> cardRequestTOIdsForUpdateState = new ArrayList<Long>();
		HashMap<Long, Date> cardRequestTOIdsForUpdateReEnrolledDate = new HashMap<Long, Date>();
		List<Long> portalReserveTOIds = new ArrayList<Long>();

		if (reservationTOList != null && !reservationTOList.isEmpty()) {

			for (ReservationTO reservationTO : reservationTOList) {
				try {
					ReservationVTO reservationVTO = reserve(reservationTO);

					getCardRequestHistoryDAO().create(
							reservationTO.getCardRequest(),
							"Reservation Date: "
									+ DateUtil.convert(reservationTO.getDate(),
											DateUtil.JALALI), SystemId.PORTAL,
							null, CardRequestHistoryAction.TRANSFER_RESERVE,
							null);

					if (reservationVTO.getCardRequestTOIdForUpdateState() != null) {
						cardRequestTOIdsForUpdateOffice.put(reservationVTO
								.getCardRequestTOIdForUpdateState(),
								reservationTO.getEnrollmentOffice().getId());
						cardRequestTOIdsForUpdateState.add(reservationVTO
								.getCardRequestTOIdForUpdateState());
					}
					if (reservationVTO
							.getCardRequestTOIdForUpdateReEnrolledDate() != null)
						// cardRequestTOIdsForUpdateReEnrolledDate.add(reservationVTO.getCardRequestTOIdForUpdateReEnrolledDate());
						cardRequestTOIdsForUpdateReEnrolledDate
								.put(reservationVTO
										.getCardRequestTOIdForUpdateReEnrolledDate(),
										reservationVTO.getReservationDate());
					if (reservationVTO.getPortalReserveTOId() != null)
						portalReserveTOIds.add(reservationVTO
								.getPortalReserveTOId());
				} catch (BaseException e) {
					logger.error(e.getExceptionCode() + " : " + e.getMessage(),
							e);
				} catch (Exception e) {
					logger.error(BizExceptionCode.PTL_009 + " : "
							+ BizExceptionCode.GLB_008_MSG, e);
				}
			}

			if (EmsUtil.checkListSize(cardRequestTOIdsForUpdateState)
					&& !cardRequestTOIdsForUpdateState.isEmpty()) {
				getCardRequestDAO().updateCardRequestsState(
						cardRequestTOIdsForUpdateState,
						CardRequestState.RESERVED);
				for (Long cardRequestId : cardRequestTOIdsForUpdateState)
					getCardRequestDAO().updateCardRequestOfficeId(
							cardRequestId,
							cardRequestTOIdsForUpdateOffice.get(cardRequestId));
			}
			if (EmsUtil.checkMapSize(cardRequestTOIdsForUpdateReEnrolledDate)) {
				for (Long cardRequestTOIdForUpdateReEnrolledDate : cardRequestTOIdsForUpdateReEnrolledDate
						.keySet()) {
					getCardRequestDAO()
							.updateReEnrolledDateByCardRequestId(
									cardRequestTOIdForUpdateReEnrolledDate,
									cardRequestTOIdsForUpdateReEnrolledDate
											.get(cardRequestTOIdForUpdateReEnrolledDate));
				}
			}

			if (EmsUtil.checkListSize(portalReserveTOIds))
				getPortalReservationService().receivedByEMS(portalReserveTOIds);
		} else {
			loopFlag = false;
		}

		createBusinessLog(BusinessLogAction.FETCH_RESERVATION_INFO,
				BusinessLogEntity.PORTAL, "System",
				EmsUtil.toJSON("cardRequestTOIds:" + portalReserveTOIds),
				BusinessLogActionAttitude.T);

		return loopFlag;

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean transferReservationsToEMSAndDoEstelam2(
			List<Long> reservationIdsList) throws BaseException {

		List<ReservationTO> reservationTOList = getPortalReservationService()
				.transferReservations(reservationIdsList);

		List<Long> emsCardRequestIds = new ArrayList<Long>();
		List<Long> portalReservationIds = new ArrayList<Long>();

		for (ReservationTO reservationTo : reservationTOList) {
			try {
				logger.info("******************  Going for transer card request id : "
						+ reservationTo.getId() + "******************");
				// List<Long> reservationList = new ArrayList<Long>();
				// reservationList.add(reservationId);
				Boolean transferReservationsToEMSResult = sessionContext
						.getBusinessObject(PortalManagementServiceLocal.class)
						.transferReservationsToEMS(reservationTo);
				if (transferReservationsToEMSResult) {
					emsCardRequestIds.add(reservationTo.getCardRequest()
							.getId());
					portalReservationIds.add(reservationTo
							.getPortalReservationId());
					logger.info("******************  Going for estelam2 card request id : "
							+ reservationTo.getId() + "******************");
				}
			} catch (Exception e) {
				logger.error(BizExceptionCode.PTL_023 + " : "
						+ BizExceptionCode.PTL_012_MSG + " = "
						+ reservationTo.getCardRequest().getId(), e);
			}
		}

		if (EmsUtil.checkListSize(portalReservationIds)) {
			getPortalReservationService().receivedByEMS(portalReservationIds);
		}

		// if (!emsCardRequestIds.isEmpty()) {
		// for (Long cardRequestId : emsCardRequestIds) {
		// try {
		// getCardRequestService().doEstelam2(cardRequestId);
		// } catch (Exception e) {
		// logger.error(BizExceptionCode.PTL_020 + " : " +
		// BizExceptionCode.GLB_027_MSG + cardRequestId, e);
		// }
		// }
		// }
		return !emsCardRequestIds.isEmpty();
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// @Override
	// public void doEstelam2ForReservations(List<Long> reservationIdsList)
	// throws BaseException {
	// for (Long portalReservationId : reservationIdsList) {
	// Long cardRequestId = null;
	// try {
	// ReservationTO reservationTO =
	// getReservationDAO().fetchReservationByPortalReservationId(portalReservationId);
	// cardRequestId = reservationTO.getCardRequest().getId();
	// getCardRequestService().doEstelam2(cardRequestId);
	// } catch (Exception e) {
	// logger.error(BizExceptionCode.PTL_020 + " : " +
	// BizExceptionCode.GLB_027_MSG + cardRequestId, e);
	// }
	// }
	// }

	/**
	 * T1 Method
	 * 
	 * @param reservationTO
	 * @return
	 * @throws BaseException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public Boolean transferReservationsToEMS(ReservationTO reservationTO)
			throws BaseException {
		boolean loopFlag = true;

		HashMap<Long, Long> cardRequestTOIdsForUpdateOffice = new HashMap<Long, Long>();
		List<Long> cardRequestTOIdsForUpdateState = new ArrayList<Long>();
		HashMap<Long, Date> cardRequestTOIdsForUpdateReEnrolledDate = new HashMap<Long, Date>();
		List<Long> portalReserveTOIds = new ArrayList<Long>();

		// if (reservationTOList != null && !reservationTOList.isEmpty()) {
		// ReservationTO reservationTO = reservationTOList.get(0);

		// for (ReservationTO reservationTO : reservationTOList) {
		try {

			CardRequestState toState = reservationTO.getCardRequest()
					.getState();
			CardRequestTO emsCardRequest = getCardRequestDAO()
					.findByPortalRequestId(
							reservationTO.getCardRequest().getPortalRequestId());
			if (emsCardRequest != null) {
				CardRequestState fromState = emsCardRequest.getState();
				if (!CardRequestState.checkStateChangeValidation(fromState,
						toState)) {
					// getCardRequestService().increamentPriorityForSyncJob(emsCardRequest.getId());
					throw new BaseException(BizExceptionCode.PTL_022,
							BizExceptionCode.PTL_011_MSG + " from state "
									+ fromState + " to state " + toState);
				}
			}

			ReservationVTO reservationVTO = null;

			if (CardRequestState.REFERRED_TO_CCOS.equals(reservationTO
					.getCardRequest().getState())
					|| CardRequestState.DOCUMENT_AUTHENTICATED
							.equals(reservationTO.getCardRequest().getState())) {
				reservationVTO = reserve(reservationTO);
			} else {
				reservationVTO = newReserve(reservationTO);
			}

			// transfer reserve old

			if (reservationVTO.getCardRequestTOIdForUpdateState() != null) {
				cardRequestTOIdsForUpdateOffice.put(
						reservationVTO.getCardRequestTOIdForUpdateState(),
						reservationTO.getEnrollmentOffice().getId());
				cardRequestTOIdsForUpdateState.add(reservationVTO
						.getCardRequestTOIdForUpdateState());
			}
			if (reservationVTO.getCardRequestTOIdForUpdateReEnrolledDate() != null)
				// cardRequestTOIdsForUpdateReEnrolledDate.add(reservationVTO.getCardRequestTOIdForUpdateReEnrolledDate());
				cardRequestTOIdsForUpdateReEnrolledDate.put(reservationVTO
						.getCardRequestTOIdForUpdateReEnrolledDate(),
						reservationVTO.getReservationDate());
			if (reservationVTO.getPortalReserveTOId() != null)
				portalReserveTOIds.add(reservationVTO.getPortalReserveTOId());

			if (EmsUtil.checkListSize(cardRequestTOIdsForUpdateState)
					&& !cardRequestTOIdsForUpdateState.isEmpty()) {
				// for old requests which have been transfered to EMS and now
				// their reservation has arrived!
				getCardRequestDAO().updateCardRequestsState(
						cardRequestTOIdsForUpdateState,
						CardRequestState.RESERVED);
				getCardRequestDAO().readyEstelam2Flag(
						cardRequestTOIdsForUpdateState);
				for (Long cardRequestId : cardRequestTOIdsForUpdateState)
					getCardRequestDAO().updateCardRequestOfficeId(
							cardRequestId,
							cardRequestTOIdsForUpdateOffice.get(cardRequestId));
			}
			if (EmsUtil.checkMapSize(cardRequestTOIdsForUpdateReEnrolledDate)) {
				getCardRequestDAO().readyEstelam2Flag(
						new ArrayList<Long>(
								cardRequestTOIdsForUpdateReEnrolledDate
										.keySet()));
				for (Long cardRequestTOIdForUpdateReEnrolledDate : cardRequestTOIdsForUpdateReEnrolledDate
						.keySet()) {
					getCardRequestDAO()
							.updateReEnrolledDateByCardRequestId(
									cardRequestTOIdForUpdateReEnrolledDate,
									cardRequestTOIdsForUpdateReEnrolledDate
											.get(cardRequestTOIdForUpdateReEnrolledDate));
				}
			}

			getCardRequestHistoryDAO().create(
					reservationTO.getCardRequest(),
					"Reservation Date: "
							+ DateUtil.convert(reservationTO.getDate(),
									DateUtil.JALALI), SystemId.PORTAL, null,
					CardRequestHistoryAction.TRANSFER_RESERVE, null);

			// } else {
			// loopFlag = false;
			// }

			createBusinessLog(BusinessLogAction.FETCH_RESERVATION_INFO,
					BusinessLogEntity.PORTAL, "System",
					EmsUtil.toJSON("cardRequestTOIds:" + portalReserveTOIds),
					BusinessLogActionAttitude.T);

		} catch (BaseException e) {
			loopFlag = false;
			logger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
			if (!BizExceptionCode.PTL_022.equals(e.getExceptionCode())) {
				sessionContext.setRollbackOnly();
			}
		} catch (Exception e) {
			loopFlag = false;
			logger.error(BizExceptionCode.PTL_009 + " : "
					+ BizExceptionCode.GLB_008_MSG, e);
			sessionContext.setRollbackOnly();
		}
		// }

		// if (loopFlag){
		// if (EmsUtil.checkListSize(portalReserveTOIds)){
		// getPortalReservationService().receivedByEMS(portalReserveTOIds);
		// }
		// }

		return loopFlag;
	}

	@javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRED)
	private ReservationVTO newReserve(ReservationTO reservationTO)
			throws BaseException {
		ReservationVTO reservationVTO = new ReservationVTO();
		try {
			ReservationDAO reservationDAO = getReservationDAO();
			CardRequestDAO cardRequestDAO = getCardRequestDAO();

			try {
				CardRequestTO cardRequestTO = cardRequestDAO
						.findByPortalRequestId(reservationTO.getCardRequest()
								.getPortalRequestId());

				CardRequestTO portalCardRequest = reservationTO
						.getCardRequest();
				//*************Anbari:Payment
				portalCardRequest.setPaid(reservationTO.isPaid());
				portalCardRequest.setPaidDate(reservationTO.getPaidDate());
				//*************
				Long savedPortalCardRequestId = getRegistrationService()
						.saveReservationCardRequest(portalCardRequest);
				cardRequestTO = cardRequestDAO
						.fetchCardRequest(savedPortalCardRequestId);

				reservationTO.getCardRequest().setId(cardRequestTO.getId());

				ReservationTO loadedReservationTO = reservationDAO
						.fetchReservationByPortalReservationId(reservationTO
								.getPortalReservationId());
				if (loadedReservationTO != null)
					reservationTO.setId(loadedReservationTO.getId());

				if (cardRequestTO.getOriginalCardRequestOfficeId() != null) { // In
																				// cases
																				// which
																				// the
																				// request
																				// has
																				// been
																				// transferred
																				// to
																				// the
																				// superior
																				// office,
																				// there
																				// is
																				// no
																				// way
																				// to
																				// change
																				// the
																				// EOF
																				// for
																				// the
																				// reservation
																				// purpose
					reservationTO.setEnrollmentOffice(cardRequestTO
							.getEnrollmentOffice());
				}
				reservationDAO.create(reservationTO);

				if (CardRequestState.REFERRED_TO_CCOS.equals(cardRequestTO
						.getState())
						|| CardRequestState.DOCUMENT_AUTHENTICATED
								.equals(cardRequestTO.getState())) {
					reservationVTO
							.setCardRequestTOIdForUpdateReEnrolledDate(cardRequestTO
									.getId());
					reservationVTO.setReservationDate(reservationTO.getDate());
				} else
					reservationVTO
							.setCardRequestTOIdForUpdateState(cardRequestTO
									.getId());

				reservationVTO.setPortalReserveTOId(reservationTO
						.getPortalReservationId());
			} catch (BaseException e) {
				if (DataExceptionCode.RSI_002.equals(e.getExceptionCode())) {
					logger.error(e.getMessage());
				} else {
					throw e;
				}
			}
		} catch (BaseException e) {
			// sessionContext.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			// sessionContext.setRollbackOnly();
			throw new ServiceException(BizExceptionCode.PTL_013,
					BizExceptionCode.GLB_008_MSG, e);
		}
		return reservationVTO;
	}

	/**
	 * 
	 * @param reservationTO
	 * @return
	 * @throws BaseException
	 * @deprecated {@link PortalManagementServiceImpl#newReserve(ReservationTO)}
	 */
	@javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRED)
	private ReservationVTO reserve(ReservationTO reservationTO)
			throws BaseException {
		ReservationVTO reservationVTO = new ReservationVTO();
		try {
			ReservationDAO reservationDAO = getReservationDAO();
			CardRequestDAO cardRequestDAO = getCardRequestDAO();

			try {
				CardRequestTO cardRequestTO = cardRequestDAO
						.findByPortalRequestId(reservationTO.getCardRequest()
								.getPortalRequestId());

				if (cardRequestTO == null)
					throw new ServiceException(BizExceptionCode.PTL_007,
							BizExceptionCode.PTL_007_MSG);

				reservationTO.getCardRequest().setId(cardRequestTO.getId());

				ReservationTO loadedReservationTO = reservationDAO
						.fetchReservationByPortalReservationId(reservationTO
								.getPortalReservationId());
				if (loadedReservationTO != null)
					reservationTO.setId(loadedReservationTO.getId());

				if (cardRequestTO.getOriginalCardRequestOfficeId() != null) { // In
																				// cases
																				// which
																				// the
																				// request
																				// has
																				// been
																				// transferred
																				// to
																				// the
																				// superior
																				// office,
																				// there
																				// is
																				// no
																				// way
																				// to
																				// change
																				// the
																				// EOF
																				// for
																				// the
																				// reservation
																				// purpose
					reservationTO.setEnrollmentOffice(cardRequestTO
							.getEnrollmentOffice());
				}
				reservationDAO.create(reservationTO);

				if (CardRequestState.REFERRED_TO_CCOS.equals(cardRequestTO
						.getState())
						|| CardRequestState.DOCUMENT_AUTHENTICATED
								.equals(cardRequestTO.getState())) {
					reservationVTO
							.setCardRequestTOIdForUpdateReEnrolledDate(cardRequestTO
									.getId());
					reservationVTO.setReservationDate(reservationTO.getDate());
				} else
					reservationVTO
							.setCardRequestTOIdForUpdateState(cardRequestTO
									.getId());

				reservationVTO.setPortalReserveTOId(reservationTO
						.getPortalReservationId());
				//******************Anbari:Payment
				cardRequestTO.setPaid(reservationTO.isPaid());
				cardRequestTO.setPaidDate(reservationTO.getPaidDate());
				getCardRequestDAO().update(cardRequestTO);
				//******************
			} catch (BaseException e) {
				if (DataExceptionCode.RSI_002.equals(e.getExceptionCode())) {
					logger.error(e.getMessage());
				} else {
					throw e;
				}
			}
		} catch (BaseException e) {
			// sessionContext.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			// sessionContext.setRollbackOnly();
			throw new ServiceException(BizExceptionCode.PTL_013,
					BizExceptionCode.GLB_008_MSG, e);
		}
		return reservationVTO;
	}

	public Long getCcosAndVerifiedMESRequestsCount() throws BaseException {
		return getCardRequestDAO().getCcosAndVerifiedMESRequestsCount();
	}

	@Override
	@BizLoggable(logAction = "UPDATE_CCOS_AND_IMS_VERIFIED_MES_REQUESTS", logEntityName = "PORTAL", logActor = "System")
	public String doActivityForUpdateCcosAndMESCardRequest(int from, int to)
			throws BaseException {
		List<Long> cardRequestTOIdsForLog = new ArrayList<Long>();
		try {
			CardRequestDAO cardRequestDAO = getCardRequestDAO();
			CitizenInfoDAO citizenInfoDAO = getCitizenInfoDAO();

			List<CardRequestTO> cardRequestTOList = cardRequestDAO
					.findCcosAndVerifiedMESRequests(from, to);
			if (!EmsUtil.checkListSize(cardRequestTOList))
				throw new ServiceException(BizExceptionCode.PTL_010,
						BizExceptionCode.PTL_010_MSG);

			for (CardRequestTO crq : cardRequestTOList) {
				CitizenInfoTO citizenInfoTO = citizenInfoDAO.getCitizenInfoById(crq.getCitizen().getId());
				crq.getCitizen().setCitizenInfo(citizenInfoTO);
				CardRequestWTO cardRequestWTO = CardRequestMapper.convert(crq);
				Long portalRequestId = getPortalRegistrationService()
						.updateCcosCardRequests(cardRequestWTO);
				cardRequestDAO.updatePortalCardRequestId(crq.getId(),
						portalRequestId);
				cardRequestTOIdsForLog.add(crq.getId());
			}
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PTL_011,
					BizExceptionCode.GLB_008_MSG, e);
		}
		return EmsUtil.toJSON("cardRequestIds:" + cardRequestTOIdsForLog);
	}

	@Override
	public Long fetchModifiedLocationCount(LocationType locationType)
			throws BaseException {
		return getLocationDAO().fetchModifiedLocationCount(locationType);
	}

	/**
	 * The method notifyPortalAboutModifiedProvinces is used to notify the
	 * subsystem 'Portal' about any modification at the instances of type
	 * {@link com.gam.nocr.ems.data.domain.LocationTO}
	 * 
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	@Override
	@BizLoggable(logAction = "UPDATE_PROVINCE_LIST", logEntityName = "PORTAL", logActor = "System")
	public String notifyPortalAboutModifiedProvinces(LocationType locationType,
			Integer from, Integer to) throws BaseException {
		LocationDAO locationDAO = getLocationDAO();

		List<LocationTO> locationTOList = locationDAO
				.findModifiedLocationsByType(locationType, from, to - from);

		List<Long> updatedLocationIdList = new ArrayList<Long>();

		if (locationTOList != null && !locationTOList.isEmpty()) {
			updatedLocationIdList = getPortalBaseInfoService().updateLocations(
					locationTOList);

			locationDAO.updateModifiedFields(updatedLocationIdList,
					Boolean.FALSE);
		}

		return EmsUtil.toJSON("updatedLocationIdList:" + updatedLocationIdList);
	}

	@Override
	public List<Long> fetchPortalCardRequestIdsForTransfer()
			throws BaseException {
		return getPortalRegistrationService()
				.fetchPortalCardRequestIdsForTransfer();
	}

	/**
	 * The method transferNotVerifiedMESRequestsToPortal is used to transfer the
	 * requests that have not been verified by IMS and have origins of type
	 * 'MES'
	 * 
	 * @return an instance of type {@link Boolean}, which is filled by true or
	 *         false. If there are other data to send, then the return value
	 *         will be true, otherwise it will be valued by false
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	@Override
	@BizLoggable(logAction = "UPDATE_NOT_VERIFIED_MES_REQUESTS", logEntityName = "PORTAL", logActor = "System")
	public Boolean transferNotVerifiedMESRequestsToPortal()
			throws BaseException {
		List<Long> cardRequestTOIdsForLog = new ArrayList<Long>();
		boolean returnFlag = true;
		try {
			CardRequestDAO cardRequestDAO = getCardRequestDAO();

			List<CardRequestTO> cardRequestTOList = cardRequestDAO
					.findNotPortalRequestsByStateAndOrigin(
							CardRequestState.NOT_VERIFIED_BY_IMS,
							CardRequestOrigin.M);

			if (!EmsUtil.checkListSize(cardRequestTOList)) {
				returnFlag = false;

			} else {
				for (CardRequestTO cardRequestTO : cardRequestTOList) {
					CitizenWTO citizenWTO = CardRequestMapper
							.convertToCitizenWTO(cardRequestTO);
					Long portalRequestId = getPortalRegistrationService()
							.updateNotVerifiedMESRequest(citizenWTO);
					cardRequestTO.setPortalRequestId(portalRequestId);
					cardRequestTOIdsForLog.add(cardRequestTO.getId());
				}
			}

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PTL_011,
					BizExceptionCode.GLB_008_MSG, e);
		}
		createBusinessLog(BusinessLogAction.UPDATE_NOT_VERIFIED_MES_REQUESTS,
				BusinessLogEntity.PORTAL, "System",
				EmsUtil.toJSON("cardRequestIds:" + cardRequestTOIdsForLog),
				BusinessLogActionAttitude.T);
		return returnFlag;
	}

	@Override
	public List<Long> fetchRequestedSmsIds() throws BaseException {
		return getPortalSmsService().retrieveRequestedSms();
	}

	@Override
	public void addRequestedSms(Long portalCardRequestId) throws BaseException {
		getPortalSmsService().addRequestedSms(portalCardRequestId);
	}

	// Hossein
	@Override
	public Integer fetchReferToCCOSProcessSms() throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.fetchReferToCCOSProcessSms();
	}

	@Override
	public Integer fetchReadyToProcessSms() throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.fetchReadyToProcessSms();
	}

	@Override
	public Boolean processSms(Integer from, Integer to) throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.processSms(from, to);
	}

	// Hossein
	@Override
	public Boolean processReferToCCOSSms(Integer from, Integer to)
			throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.processReferToCCOSSms(from, to);
	}

	// Anbari
	@Override
	public void notifyPortalRezervationFreeTime(List<Long> eofIds, Long date)
			throws BaseException {
		getPortalBaseInfoService()
				.notifyPortalRezervationFreeTime(eofIds, date);

	}

	// Anbari
	public int deleteReservationDateFromOfficeRSVFreeTime(Long dateForDelete)
			throws BaseException {
		return getPortalBaseInfoService()
				.deleteReservationDateFromOfficeRSVFreeTime(dateForDelete);
	}

	@Override
	@Asynchronous
	public Future syncResevationFreeTimeByNewRating(Long eofId,
			RatingInfoTO ratingInfo,String newCalender) throws BaseException {
		getPortalBaseInfoService().syncResevationFreeTimeByNewRating(eofId,
				ratingInfo,newCalender);
		return new AsyncResult("");
	}

	//Madanipour
	@Override
	public Integer fetchSmsCount(int smsType) throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.fetchSmsCount(smsType);
	}

	//Madanipour
	@Override
	public Boolean processSmsToSend(Integer from, int to, int smsType)
			throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		return portalSmsService.processSmsToSend(from, to, smsType);
	}

	@Override
	public void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType)throws BaseException {
		PortalSmsService portalSmsService = getPortalSmsService();
		portalSmsService.deleteOldRecordsFromMsgt(timeInterval, smsType);
		
	}
}
