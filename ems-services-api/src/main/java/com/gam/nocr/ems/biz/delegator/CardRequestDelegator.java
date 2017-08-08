package com.gam.nocr.ems.biz.delegator;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.CardManagementService;
import com.gam.nocr.ems.biz.service.CardRequestService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CardRequestDelegator implements Delegator {

	private CardRequestService getService(UserProfileTO userProfileTO)
			throws BaseException {
		CardRequestService cardRequestService;
		try {
			cardRequestService = ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.CRD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_CARD_REQUEST.split(","));
		}
		cardRequestService.setUserProfileTO(userProfileTO);
		return cardRequestService;
	}

	/**
	 * @author ganjyar
	 */
	private CardManagementService getCardManagementService(
			UserProfileTO userProfileTO) throws BaseException {
		CardManagementService cardManagementService = null;
		try {
			cardManagementService = ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_CARD_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.CRD_002,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_CARD_MANAGEMENT.split(","));
		}
		cardManagementService.setUserProfileTO(userProfileTO);
		return cardManagementService;
	}

	public Long findRequestCountByAction(CardRequestedAction cardRequestedAction)
			throws BaseException {
		return getService(null).findRequestCountByAction(cardRequestedAction);
	}

	public void doCardRequestRepealActionBySystem(Integer from)
			throws BaseException {
		List<Long> requestIds = getService(null).repealCardRequest(from);
		PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
		portalManagementDelegator.updateRequestStates(requestIds);

	}

	public void doCardRequestRepealAction(UserProfileTO userProfileTO,
			Long cardRequestId, CardRequestedAction cardRequestedAction,
			SystemId systemId) throws BaseException {
		getService(userProfileTO).doCardRequestRepealAction(cardRequestId,
				cardRequestedAction, systemId);
	}

	public void transferCardRequestToNocr(UserProfileTO userProfileTO,
			Long cardRequestId, CardRequestedAction cardRequestedAction)
			throws BaseException {
		getService(userProfileTO).transferCardRequestToNocr(cardRequestId,
				cardRequestedAction);
	}

	public List<CardRequestVTO> fetchCardRequests(GeneralCriteria criteria)
			throws BaseException {
		return getService(criteria.getUserProfileTO()).fetchCardRequests(
				criteria);
	}

	public Integer countCardRequests(GeneralCriteria criteria)
			throws BaseException {
		return getService(criteria.getUserProfileTO()).countCardRequests(
				criteria);
	}

	public List<CitizenWTO> fetchCCOSRegistrationCartableCardRequests(
			CCOSCriteria criteria) throws BaseException {
		return getService(criteria.getUserProfileTO())
				.fetchCCOSRegistrationCartableCardRequests(criteria);
	}

	public Integer countCCOSRegistrationCartableCardRequests(
			CCOSCriteria criteria) throws BaseException {
		return getService(criteria.getUserProfileTO())
				.countCCOSRegistrationCartableCardRequests(criteria);
	}

	public List<CitizenWTO> fetchCCOSDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException {
		return getService(criteria.getUserProfileTO())
				.fetchCCOSDeliverCartableCardRequests(criteria);
	}

	public Integer countCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
			throws BaseException {
		return getService(criteria.getUserProfileTO())
				.countCCOSDeliverCartableCardRequests(criteria);
	}

	public List<CitizenWTO> fetchCCOSReadyToDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException {
		return getService(criteria.getUserProfileTO())
				.fetchCCOSReadyToDeliverCartableCardRequests(criteria);
	}

	public Integer countCCOSReadyToDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException {
		return getService(criteria.getUserProfileTO())
				.countCCOSReadyToDeliverCartableCardRequests(criteria);
	}

	public List<Long> getRequestIdsForUpdateState(Integer fetchLimit)
			throws BaseException {
		return getService(null).getRequestIdsForUpdateState(fetchLimit);
	}

	public List<SyncCardRequestWTO> getRequestListForUpdateState(
			List<Long> requestIds) throws BaseException {
		return getService(null).getRequestListForUpdateState(requestIds);
	}

	// Anbari
	public void doImageDeleteAction(UserProfileTO userProfileTO,
			List<Long> cardRequestIds, Long citizenId, CardRequestState state)
			throws BaseException {
		getService(userProfileTO).doImageDeleteAction(cardRequestIds,
				citizenId, state);
	}

	// Anbari
	public void doCMSRetryAction(UserProfileTO userProfileTO,
			List<Long> cardRequestIds, CardRequestState state)
			throws BaseException {
		getService(userProfileTO).doCMSRetryAction(cardRequestIds, state);
	}

	// Anbari
	public void doRepealCardAction(UserProfileTO userProfileTO,
			List<Long> cardRequestIds) throws BaseException {
		getService(userProfileTO).doRepealCardAction(cardRequestIds);
		PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
		portalManagementDelegator.updateRequestStates(cardRequestIds);

	}

	// ganjyar
	public AccessProductionVTO getAccessProduction(UserProfileTO userProfile)
			throws BaseException {
		return getService(userProfile).getAccessProduction();
	}

	/**
	 * 
	 * @param userProfile
	 * @param cardRequestId
	 * @return
	 * @throws BaseException
	 * @author ganjyar
	 */
	public CardRequestVTO findCardRequestById(UserProfileTO userProfile,
			String cardRequestId) throws BaseException {
		return getService(userProfile).findCardRequestById(cardRequestId);
	}

	/**
	 * 
	 * @param userProfile
	 * @param cardRequestId
	 * @param priority
	 * @throws BaseException
	 * @author ganjyar
	 */
	public void updateCardRequestPriority(UserProfileTO userProfile,
			String cardRequestId, String priority) throws BaseException {
		getService(userProfile).updateCardRequestPriority(cardRequestId,
				priority);
	}

	public boolean hasChangePriorityAccess(UserProfileTO userProfile)
			throws BaseException {
		return getService(userProfile).hasChangePriorityAccess();

	}


	// hossein 8feature start
	public CardRequestVTO viewCardRequestInfo(UserProfileTO userProfileTO,
			Long cardRequestId) throws BaseException {
		return getService(userProfileTO).viewCardRequestInfo(cardRequestId);
	}

	// hossein 8feature end

	/**
	 * @author ganjyar
	 */
	public void doConfirmLostCard(UserProfileTO userProfile, Long cardId)
			throws BaseException {

		getCardManagementService(userProfile).doConfirmLostCard(cardId);

	}

	/**
	 * this method is used to fetch card lost temp list. The list is used in
	 * card lost cartable which are waiting to confirm by 3s admin
	 * 
	 * @author ganjyar
	 * 
	 */
	public List<CardDispatchInfoVTO> fetchCardLostTempList(
			GeneralCriteria criteria) throws BaseException {

		return getCardManagementService(criteria.getUserProfileTO())
				.fetchCardLostTempList(criteria);
	}

	/**
	 * this method is used to fetch card lost temp count. The count is used in
	 * card lost cartable which are waiting to confirm by 3s admin
	 * 
	 * @author ganjyar
	 * 
	 */
	public Integer countCardLostTemp(GeneralCriteria criteria)
			throws BaseException {

		return getCardManagementService(criteria.getUserProfileTO())
				.countCardLostTemp(criteria);
	}

	/**
	 * @author ganjyar
	 */
	public void doConfirmLostBatch(UserProfileTO userProfile, Long batchId)
			throws BaseException {
		getCardManagementService(userProfile).doConfirmLostBatch(batchId);
	}

	/**
	 * this method is used to fetch batch lost temp count. The count is used in
	 * card lost cartable which are waiting to confirm by 3s admin
	 * 
	 * @author ganjyar
	 */
	public List<BatchDispatchInfoVTO> fetchBatchLostTempList(
			GeneralCriteria criteria) throws BaseException {

		return getCardManagementService(criteria.getUserProfileTO())
				.fetchBatchLostTempList(criteria);
	}

	public Integer countBatchLostTemp(GeneralCriteria criteria)
			throws BaseException {

		return getCardManagementService(criteria.getUserProfileTO())
				.countBatchLostTemp(criteria);
	}
	
	// Madanipour
	public List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,
			Integer dayInterval) throws BaseException {
		return getService(null).fetchReservedRequest(numberOfRequestToFetch,
				dayInterval);
	}

	// Madanipour
	public void addRequestedSmsForReservedReq(Long cardRequestId)
			throws BaseException {
		getService(null).addRequestedSmsForReservedReq(cardRequestId);

	}
	

	/**
	 * this method aims to fetch all citizen ids to purge its bio and docs
	 * 
	 * @author ganjyar
	 * @param fetchLimit
	 * @return
	 */
	public List<Long> getCitizenIdsForPurgeBioAndDocs(Integer fetchLimit)
			throws BaseException {
		return getService(null).getCitizenIdsForPurgeBioAndDocs(fetchLimit);
	}

	/**
	 * this method sets all bio and document data to null
	 * 
	 * @author ganjyar
	 * @param citizenIds
	 */

	public void purgeBiometricsAndDocuments(Long citizenId,String savePurgeHistory)
			throws BaseException {
		getService(null).purgeBiometricsAndDocuments(citizenId,savePurgeHistory);

	}


	/**
	 * this method is used to find card request state
	 * @param trackingId
	 * @author Sahar Najafi
	 * @return citizenGuidance
	 *
	 */

	public void findCardRequestStateByTrackingId(
			String trackingId) throws BaseException{
		getService(null)
					.findCardRequestStateByTrackingId(trackingId);
	}

	/**
	 * this method is used to find card request state
	 * @param nationalId
	 * @param mobile
	 * @author Sahar Najafi
	 * @return card request state
	 *
	 */


	public String findCardRequestStateByNationalIdAndMobile(
			String nationalId, String mobile) throws BaseException{
		return  getService(null)
					.findCardRequestStateByNationalIdAndMobile(nationalId, mobile);
	}

	/**
	 * this method is used to find card request state
	 * @param nationalId
	 * @param birthCertificateSeries
	 * @author Sahar Najafi
	 * @return citizenGuidance
	 *
	 */


	public String findCardRequestStateByNationalIdAndBirthCertificateSeries(
			String nationalId, String birthCertificateSeries) throws BaseException{
		return  getService(null)
					.findCardRequestStateByNationalIdAndBirthCertificateSeries(nationalId, birthCertificateSeries);
	}


}
