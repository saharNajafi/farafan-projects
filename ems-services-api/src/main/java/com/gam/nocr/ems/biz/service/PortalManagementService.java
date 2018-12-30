package com.gam.nocr.ems.biz.service;

import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.data.domain.ws.ItemWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.LocationType;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PortalManagementService extends Service {

	/**
	 * The method transferCardRequests is used to get a number of card request
	 * from the sub system 'Portal'
	 * 
	 * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
	 * @throws BaseException
	 */
	List<CardRequestTO> transferCardRequests(List<Long> portalCardRequestIds)
			throws BaseException;

	/**
	 * The method doActivityForUpdateState is used to prepare a list of type
	 * {@link ItemWTO} and calls the method 'updateCardRequestStates' to notify
	 * the sub system 'Portal' about the state of each request. This method is
	 * triggered by a specified job
	 * 
	 * @throws BaseException
	 */
	Boolean doActivityForUpdateState(List<Long> batchIds) throws BaseException;

	List<Long> fetchReservationIds() throws BaseException;

	/**
	 * The method doActivityForReservations is used to get a number of
	 * reservations from the sub system 'Portal'
	 * 
	 * @return an instance of type {@link String}, which is applicable in
	 *         logging
	 * @throws BaseException
	 */
	Boolean doActivityForReservations(List<Long> reservationIds)
			throws BaseException;

	/**
	 * replacement of {@link #doActivityForReservations(List)}
	 * 
	 * @param reservationIdsList
	 * @return
	 * @throws BaseException
	 */
	Boolean transferReservationsToEMSAndDoEstelam2(List<Long> longList)
			throws BaseException;

	public Long getCcosAndVerifiedMESRequestsCount() throws BaseException;

	String doActivityForUpdateCcosAndMESCardRequest(int from, int to)
			throws BaseException;

	Long fetchModifiedLocationCount(LocationType locationType)
			throws BaseException;

	/**
	 * The method notifyPortalAboutModifiedProvinces is used to notify the
	 * subsystem 'Portal' about any modification at the instances of type
	 * {@link com.gam.nocr.ems.data.domain.LocationTO}
	 * 
	 * @throws BaseException
	 */
	String notifyPortalAboutModifiedProvinces(LocationType locationType,
			Integer from, Integer to) throws BaseException;

	public List<Long> fetchPortalCardRequestIdsForTransfer()
			throws BaseException;

	/**
	 * The method transferNotVerifiedMESRequestsToPortal is used to transfer the
	 * requests that have not been verified by IMS and have origins of type
	 * 'MES'
	 * 
	 * @return an instance of type {@link Boolean}, which is filled by true or
	 *         false. If there are other data to send, then the return value
	 *         will be true, otherwise it will be valued by false
	 * @throws BaseException
	 */
	Boolean transferNotVerifiedMESRequestsToPortal() throws BaseException;

	List<Long> fetchRequestedSmsIds() throws BaseException;

	void addRequestedSms(Long portalCardRequestId) throws BaseException;

	Integer fetchReadyToProcessSms() throws BaseException;

	Boolean processSms(Integer from, Integer to) throws BaseException;

	void updateState(
			List<SyncCardRequestWTO> syncCardRequestWTOList,
			List<com.gam.nocr.ems.biz.service.external.client.portal.ItemWTO> itemWTOList)
			throws BaseException;

	Integer fetchReferToCCOSProcessSms() throws BaseException;

	Boolean processReferToCCOSSms(Integer from, Integer to)
			throws BaseException;

	// Anbari
	void notifyPortalRezervationFreeTime(List<Long> eofIds, Long date)
			throws BaseException;

	// Anbari
	int deleteReservationDateFromOfficeRSVFreeTime(Long dateForDelete)
			throws BaseException;

	// Anbari
	Boolean transferReservationsToEMS(ReservationTO reservationTO)
			throws BaseException;

	// Anbari
	// void doEstelam2ForReservations(List<Long> reservationIdsList)
	// throws BaseException;

	// Anbari
	Future syncResevationFreeTimeByNewRating(Long eofId, RatingInfoTO ratingInfo,String newCalender)
			throws BaseException;

	//Madanipour
	Integer fetchSmsCount(int smsType) throws BaseException;

	//Madanipour
	Boolean processSmsToSend(Integer from, int to, int smsType)throws BaseException;

	//Madanipour
	void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType) throws BaseException;

}
