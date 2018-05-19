package com.gam.nocr.ems.data.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.data.enums.SMSTypeState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

/**
 * @author Haeri (haeri@gamelectronics.com)
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CardRequestDAO extends EmsBaseDAO<CardRequestTO> {

	boolean deleteByID(long requestId) throws BaseException;

	/**
	 * The method findByCardRequestState is used to find a list of CardRequestTO
	 * in spite of their state field.
	 * 
	 * @param cardRequestState
	 *            is an enumeration of type CardRequestState
	 * @return a list of type {@link CardRequestTO}
	 */
	List<CardRequestTO> findByCardRequestState(CardRequestState cardRequestState)
			throws BaseException;

	public List<CardRequestTO> findByCardRequestState(
			CardRequestState cardRequestState, Integer from, Integer to)
			throws BaseException;

	/**
	 * The method findLimitedByCardRequestState is used to find a limited list
	 * of CardRequestTO in spite of their state field.
	 * 
	 * @param cardRequestState
	 *            is an enumeration of type CardRequestState
	 * @param countProfileKey
	 *            is an instance of type {@link String} which represents the
	 *            profile key for fetching the count by using profileManager
	 * @param defaultCountValue
	 *            is an instance of type {@link Integer} which represents the
	 *            default count for the query
	 * @return a list of type {@link CardRequestTO}
	 */
	List<CardRequestTO> findLimitedByCardRequestState(
			CardRequestState cardRequestState, String countProfileKey,
			Integer defaultCountValue) throws BaseException;

	public Long getCcosAndVerifiedMESRequestsCount() throws BaseException;

	public List<CardRequestTO> findCcosAndVerifiedMESRequests(int from, int to)
			throws BaseException;

	CardRequestTO findByCmsRequestId(String cmsRequestId) throws BaseException;

	List<CardRequestTO> findByCitizen(CitizenTO ctz) throws BaseException;

	boolean existsCardRequest(Long requestId) throws BaseException;

	void updateRequest(String cmsRequestId, CardTO cardTO) throws BaseException;

	/**
	 * The method updateRequestState is used to update the state of a specified
	 * instance of type {@link CardRequestTO} from one state to another one
	 * 
	 * @param cardRequestTO
	 *            carries the required attributes which are needed to update
	 * @param from
	 *            represents the primitive state that is wanted to be changed
	 * @param to
	 *            represents the intended state
	 */
	void updateRequestState(CardRequestTO cardRequestTO, CardRequestState from,
			CardRequestState to) throws BaseException;

	/**
	 * The method updateCardRequestsState is use to update the state of the
	 * requests in spite of their ids
	 * 
	 * @param cardRequestIdList
	 *            a list of type Long which represents the ids for the specified
	 *            requests
	 * @param cardRequestState
	 *            an enum value of type {@link CardRequestState}
	 */
	void updateCardRequestsState(List<Long> cardRequestIdList,
			CardRequestState cardRequestState) throws BaseException;

	int updateCardRequestOfficeId(Long cardRequestId, Long enrollmentOfficeId)
			throws BaseException;

	/**
	 * The method updateCardRequestByRequestIdOfHistory is used to update a list
	 * of cardRequestTo in spite of a list of requestID which are existed in
	 * {@CardRequestHistoryTO}
	 * 
	 * @param requestIDs
	 * @param cardRequestState
	 */
	void updateCardRequestsByRequestIdOfHistory(List<String> requestIDs,
			CardRequestState cardRequestState, SystemId systemId)
			throws BaseException;

	/**
	 * The method updateSyncDatesByCurrentDate is used to set lastSyncDate
	 * attribute in spite of portalRequestIds in the list
	 * 
	 * @param portalRequestIdList
	 *            a list of type {@link Long} which represents a specified
	 *            number of portalRequestIds
	 * @throws BaseException
	 */
	void updateSyncDatesByCurrentDate(List<Long> portalRequestIdList)
			throws BaseException;

	void updateLockDatesByCurrentDate(List<Long> portalRequestIdList)
			throws BaseException;

	public void updateReEnrolledDateByCardRequestId(Long cardRequestId,
			Date reEnrolledDate) throws BaseException;

	public void updatePortalCardRequestId(Long requestId, Long portalRequestId)
			throws BaseException;

	/**
	 * The method getReplicaCardRequestsCount is used to get the number of the
	 * requests which are applied by a specified citizen
	 * 
	 * @param nationalId
	 * @return the count of requests or null
	 * @throws BaseException
	 */
	Long getReplicaCardRequestsCount(String nationalId) throws BaseException;

	public CardRequestTO findByPortalRequestId(Long portalRequestId)
			throws BaseException;

	/**
	 * The method findByRequestIds is used to fetch the instances of type
	 * {@link CardRequestTO}, in spite of input parameter
	 * 
	 * @param cardRequestIdList
	 *            a list of type {@link Long} which represents a list of card
	 *            request ids
	 * @return a list of type {@link CardRequestTO}
	 * @throws BaseException
	 */
	List<CardRequestTO> findByRequestIds(List<Long> cardRequestIdList)
			throws BaseException;

	/**
	 * The method updateStateAndMetaData is use to update the request in spite
	 * of state and metadata
	 * 
	 * @param id
	 *            an instance of type {@link Long}
	 * @param cardRequestState
	 *            an enum value of type {@link CardRequestState}
	 * @param metadata
	 *            an instance of type {@link String}
	 */
	void updateStateAndMetaData(Long id, CardRequestState cardRequestState,
			String metadata) throws BaseException;

	/**
	 * The method updateRequestFromOldStateToNewState is used to update the
	 * state of the records which have a specified status
	 * 
	 * @param oldState
	 *            an instance of type {@link CardRequestState} which represents
	 *            the old state
	 * @param newState
	 *            an instance of type {@link CardRequestState} which represents
	 *            the new state
	 * @throws BaseException
	 */
	void updateRequestFromOldStateToNewState(CardRequestState oldState,
			CardRequestState newState) throws BaseException;

	CardRequestTO fetchCardRequest(Long cardRequestId) throws BaseException;

	void idleR2DCardRequest(String idlePeriod) throws BaseException;

	/**
	 * The method updateCardRequestsStateInSpiteOfOrigin is used to update the
	 * state of the requests by using a list of request Ids and a map for
	 * specifying the new states, in spite of their origins that used as key in
	 * the map
	 * 
	 * @param cardRequestIdList
	 *            a list of type Long which represents the ids for the specified
	 *            requests
	 * @param map
	 *            a map with key/value of type {@link Map<CardRequestOrigin,
	 *            CardRequestState>}, which carries the necessary states in
	 *            spite of the appropriate origins
	 */
	void updateCardRequestsStateInSpiteOfOrigin(List<Long> cardRequestIdList,
			Map<CardRequestOrigin, CardRequestState> map) throws BaseException;

	/**
	 * The method updateCardRequestsStateRegardsToOrigin is used to update the
	 * state of the requests by using a list of type {@link CardRequestTO} and a
	 * map for specifying the new states, regards to their origins that used as
	 * a key in the map
	 * 
	 * @param cardRequestTOList
	 *            a list of type {@link CardRequestTO}
	 * @param map
	 *            a map with key/value of type {@link Map<CardRequestOrigin,
	 *            CardRequestState>}, which carries the necessary states,
	 *            regarding the appropriate origins
	 */
	void updateCardRequestsStateRegardsToOrigin(
			List<CardRequestTO> cardRequestTOList,
			Map<CardRequestOrigin, CardRequestState> map) throws BaseException;

	/**
	 * The method updateCardRequestsStateInSpiteOfOrigin is used to update the
	 * state of the requests by using a map for specifying the new states, in
	 * spite of their origins that used as key in the map
	 * 
	 * @param map
	 *            a map with key/value of type {@link java.util.Map <
	 *            com.gam.nocr.ems.data.enums.CardRequestOrigin ,
	 *            CardRequestState>}, which carries the necessary states in
	 *            spite of the appropriate origins
	 * @param oldState
	 *            is an instance of type
	 *            {@link com.gam.nocr.ems.data.enums.CardRequestState}
	 */
	void updateCardRequestsStateInSpiteOfOriginAndOldState(
			Map<CardRequestOrigin, CardRequestState> map,
			CardRequestState oldState) throws BaseException;

	/**
	 * The method findNotPortalRequestsByStateAndOrigin is used to find a list
	 * of type {@link CardRequestTO}, in spite of state and origin parameters
	 * 
	 * @param state
	 *            is an instance of type {@link CardRequestState}
	 * @param origin
	 *            is an instance of type {@link CardRequestOrigin}
	 * @return a list of type {@link CardRequestTO} or null
	 * @throws BaseException
	 */
	List<CardRequestTO> findNotPortalRequestsByStateAndOrigin(
			CardRequestState state, CardRequestOrigin origin)
			throws BaseException;

	void updateCardRequests(List<CardRequestTO> cardRequestTOList)
			throws BaseException;

	/**
	 * The method findRequestsByBoundaryLimitAndState is used to find a list of
	 * CardRequestTO in spite of their state field.
	 * 
	 * @param from
	 *            is an instance of type {@link Integer}, which represents the
	 *            first record to be fetched
	 * @param to
	 *            is an instance of type {@link Integer}, which represents the
	 *            last record to be fetched
	 * @param cardRequestState
	 *            is an enumeration of type {@link CardRequestState}, which
	 *            represents the state of the request
	 * @return a list of type {@link CardRequestTO}
	 */
	List<CardRequestTO> findRequestsByBoundaryLimitAndState(Integer from,
			Integer to, CardRequestState cardRequestState) throws BaseException;

	void updateArchiveId(Long cardRequestId, String archiveId)
			throws BaseException;

	void addRequestedSms(Long portalCardRequestId) throws BaseException;

	CardRequestTO findByContainerId(Long containerId,
			DepartmentDispatchSendType containerType) throws BaseException;

	public CardRequestTO findByNIdBirthDateSolar(String nationalID,
			String birthDate) throws BaseException;

	void doCardRequestRepealAction(Long cardRequestId,
			CardRequestedAction cardRequestedAction) throws BaseException;

	Long findRequestCountByAction(CardRequestedAction cardRequestedAction)
			throws BaseException;

	List<CardRequestTO> fetchCardRequestByAction(Integer from, Integer to,
			CardRequestedAction cardRequestedAction) throws BaseException;

	/**
	 * The method findByEnrollmentOfficeIdAndStates is used to find a list of
	 * type {@link CardRequestTO}, regards to parameters of enrollmentOfficeId
	 * and a comma separated string, which represents the sequence of desired
	 * states.
	 * 
	 * @param enrollmentOfficeId
	 *            is an instance of type {@link Long}, which represents a
	 *            specified instance of type
	 *            {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
	 * @param states
	 *            is a list of type {@link CardRequestState}, which represents
	 *            the sequence of desired states
	 * @return a list of type {@link Long}, which represents the ids for
	 *         specified instances of type {@link CardRequestTO}
	 * @throws BaseException
	 */
	List<Long> findByEnrollmentOfficeIdAndStates(Long enrollmentOfficeId,
			List<CardRequestState> states) throws BaseException;

	// Adldoost
	long findEnrollmentOfficeCardRequestCount(Long enrollmentOfficeId)
			throws BaseException;

	void replaceSuperiorWithOfficeId(Long superiorEnrollmentOfficeId,
			Long enrollmentOfficeId, List<Long> cardRequestIds) throws BaseException;

	Long getRequestsCountForSendToAFIS() throws BaseException;

	/**
	 * The method findRequestForSendToAFIS is used to find a request in the
	 * state of 'CardRequestState.APPROVED' to prepare data for sending to AFIS
	 * 
	 * @param from
	 *            is an instance of type {@link Integer}, which represents a
	 *            specified index for the first record
	 * @return an instance of type {@link CardRequestTO} or null
	 * @throws BaseException
	 */
	CardRequestTO findRequestForSendToAFIS(Integer from) throws BaseException;

	List<CardRequestTO> findByNationalId(String nationalId)
			throws BaseException;

	int updateRequest(CardRequestTO cardRequest) throws BaseException;

	int updateRequestByStateAndOrigin(CardRequestTO cardRequestTO,
			Map<CardRequestOrigin, CardRequestState> map) throws BaseException;

	CardRequestOrigin getOrigin(Long requestId) throws BaseException;

	Long getRequestsCountForIssue() throws BaseException;

	/**
	 * The method findRequestForIssueCard is used to find a request in the state
	 * of 'CardRequestState.APPROVED_BY_AFIS' to prepare data for sending to
	 * AFIS
	 * 
	 * @param from
	 *            is an instance of type {@link Integer}, which represents a
	 *            specified index for the first record
	 * @return an instance of type {@link CardRequestTO} or null
	 * @throws BaseException
	 */
	CardRequestTO findRequestForIssueCard(Integer from) throws BaseException;

	Long findRequestsCountByState(CardRequestState cardRequestState)
			throws BaseException;
    List<Long> findRequestsIdByState(CardRequestState cardRequestState, Integer fetchLimit) throws BaseException;    
    
    
    

	/**
	 * The method getCardRequestIdsByAction is used to find request ids
	 * according to the cardRequestedAction that passed as the parameter
	 * 
	 * @param cardRequestedAction
	 *            is an instance of type {@link CardRequestedAction}
	 * @return a list with type of {@link Long}, which represents cardRequestIds
	 *         , or null
	 * @throws BaseException
	 */
	List<Long> getCardRequestIdsByAction(CardRequestedAction cardRequestedAction)
			throws BaseException;

	List<CardRequestTO> findByCardId(Long cardId) throws BaseException;

	/**
	 * This method is responsible for fetching biometric flag
	 * 
	 * @param id
	 *            is an instance of type {@link java.lang.Long} which identifies
	 *            an appropriate card request
	 * @return an instance of type {@link Integer} which represents value of
	 *         flag field
	 * @throws BaseException
	 */
	Integer fetchBiometricFlag(Long id) throws BaseException;

	List<CardRequestVTO> fetchCardRequests(GeneralCriteria criteria)
			throws BaseException;

	Integer countCardRequests(GeneralCriteria criteria) throws BaseException;

	List<CitizenWTO> fetchCCOSRegistrationCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	Integer countCCOSRegistrationCartableCardRequests(CCOSCriteria criteria)
			throws BaseException;

	List<CitizenWTO> fetchCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
			throws BaseException;

	Integer countCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
			throws BaseException;

	List<CitizenWTO> fetchCCOSReadyToDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	Integer countCCOSReadyToDeliverCartableCardRequests(CCOSCriteria criteria)
			throws BaseException;

	List<Long> getRequestIdsForUpdateState(Integer fetchLimit);

	List<SyncCardRequestWTO> getRequestListForUpdateState(List<Long> requestIds);

	// Anbari
	void addRequestedSmsForNotification(Long cardRequestId, SMSTypeState state)
			throws BaseException;

	void readyEstelam2Flag(List<Long> cardRequestTOIdsForUpdateState)
			throws BaseException;

	List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException;

	List<CardRequestTO> findByCardRequestStateByPriority(
			CardRequestState cardRequestState, Integer from, Integer to)
			throws BaseException;

	List<Long> getRequestIdsForIssue(Integer fetchLimit) throws BaseException;
	
	List<CardRequestTO> findNextBatchDeliveredCRQFromIdBeforeDate(long id, Calendar cal, int batchSize) throws BaseException;
	
	void refreshCRQ(CardRequestTO crq) throws BaseException;

	//Madanipour
	List<Long> getCardRequestForSubstituteAndDeleteEOF(Long eofID)
			throws BaseException;
    //Anbari
	void updateCardRequestEOFdeliverId(Long officeEOFID, Long supperiorOfficeId) throws BaseException;

	//Madanipour
	void addRequestedSmsForReadyToDeliverReq(Long cardRequestID) throws BaseException;

	//Madanipour
	List<Long> getCardRequestsByBatchID(Long batchId) throws BaseException;

	//Madanipour
	List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,Integer dayInterval) throws BaseException;

	//Madanipour
	void addRequestedSmsForReservedReq(Long cardRequestId) throws BaseException;

	//Madanipour
	void updateCardRequestRequestedSmsStatus(Long cardRequestId) throws BaseException;

	//Anbari
	List<Long> getRequestIdsForSendToAFIS(Integer fetchLimit) throws BaseException;
	void updateSyncDatesAndFlag(List<Long> portalRequestIdList) throws BaseException;
	
	void updateLockDatesAndFlag(List<Long> portalRequestIdList) throws BaseException;

	//IMS:Anbari
	List<Long> findAfisResultRequestsCountByState(CardRequestState cardRequestState, Integer fetchLimit) throws BaseException;
	
	List<Long> getCitizenIdsForPurgeBioAndDocs(Integer fetchLimit) throws BaseException;

	CardRequestTO findCardRequestStateByTrackingId(String trackingId)throws BaseException;

	CardRequestTO findCardRequestStateByNationalIdAndMobile(String nationalId, String mobile)throws BaseException;

	CardRequestTO findCardRequestStateByNationalIdAndBirthCertificateSeries(String nationalId, String birthCertificateSeries)throws BaseException;

	CardRequestTO findCardRequestStateByNationalId(String nationalId)throws BaseException;


}
