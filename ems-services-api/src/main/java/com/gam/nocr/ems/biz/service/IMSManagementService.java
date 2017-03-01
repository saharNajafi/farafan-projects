package com.gam.nocr.ems.biz.service;

import gampooya.tools.date.DateFormatException;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.IMSEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.ImsEstelamImageType;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSManagementService extends Service {


	/**
	 * <pre>
	 * The method loadUpdatedCitizensInfoResult is used to handle the activities which has been mentioned bellow :
	 * 1. Calling IMS service to get the result of update
	 * 2. Looping on CardRequestHistoryTo
	 * 3. Updating CardRequestHistoryTo
	 * 4. Updating CardRequestTO
	 * 5. Updating CitizenInfoTO
	 * </pre>
	 */
	void loadUpdatedCitizensInfoResult() throws BaseException;

	/**
	 * The method getOnlineEnquiry is used to enquiry a citizen via an online
	 * request
	 * 
	 * @param personEnquiryVTOs
	 *            is an array of type {@link PersonEnquiryVTO} which carries the
	 *            necessary attributes to enquiry
	 * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which
	 *         consists of values of true or false to represent the response of
	 *         enquiry in spite of nationalIds
	 * @throws BaseException
	 */
	HashMap<String, Boolean> getOnlineEnquiry(
			PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;

	/**
	 * The method fetchCitizenInfo is used to fetch the information of a
	 * specified citizen
	 * 
	 * @param nationalId
	 *            is an string value which represents the nationalId of a
	 *            specified citizen
	 * @return an instance of type {@link CitizenTO}
	 * @throws BaseException
	 */
	CitizenTO fetchCitizenInfo(String nationalId) throws BaseException;

	/**
	 * The method updatePersonInfoByOnlineEnquiry is used to update the citizen
	 * information of a request, base on IMS citizen data
	 * 
	 * @param cardRequestTO
	 *            is an instance of type {@link CardRequestTO}, which carries
	 *            the necessary information belongs to a specified citizen
	 * @param personEnquiryVTOs
	 *            is an instance of type {@link PersonEnquiryVTO}
	 * @throws BaseException
	 */
	void updatePersonInfoByOnlineEnquiry(CardRequestTO cardRequestTO,
			PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;

	/**
	 * The method getRequestsCountForSendToAFIS is used to get the count of
	 * requests, which are ready to send to AFIS
	 * 
	 * @return an instance of type {@link Long}, which represents the number of
	 *         ready requests to send
	 * @throws BaseException
	 */
	Long getRequestsCountForSendToAFIS() throws BaseException;

	/**
	 * The method 'updateCitizenInfo' is used to call the AFIS service of update
	 * citizen info to send stored information that associated to a specified
	 * citizen.
	 * 
	 * @param from
	 *            is an instance of type {@link Integer}, which represents the
	 *            index of data that is supposed to be fetched from database to
	 *            send
	 * @throws BaseException
	 */
	void updateCitizenInfo(Integer from) throws BaseException;

	Integer getRequestsCountForBatchEnquiry(String queryName,
			HashMap<String, String> param) throws BaseException;

	void sendBatchEnquiryReq(Integer from, Integer batchSize,
			CardRequestState cardRequestState) throws BaseException;

	/**
	 * The method
	 * getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager is used
	 * to get the required batch enquiry parameters for IMS_PENDING requests
	 * 
	 * @return an array of type {@link String} which represents the required
	 *         parameters
	 * @throws BaseException
	 */
	public String[] getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager()
			throws BaseException;

	TransferInfoVTO getBatchEnquiryResult() throws BaseException;

	/**
	 * The method updateRequestsByBatchEnquiryResult is used to update requests
	 * due to the result of batch enquiry from the sub system 'IMS'
	 * 
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	void updateRequestsByBatchEnquiryResult(TransferInfoVTO transferInfoVTO)
			throws BaseException;

	Integer getRequestCountToFetchFromAFIS() throws BaseException;

	void getUpdatedCitizenResult(Integer from) throws BaseException;

	Long findRequestsCountByState(CardRequestState requestState)
			throws BaseException;

	// ganjyar
	// does getEstelam2 for a batch of citizen
	//void updateBatchOfCitizenInfoByEstelam2(List<Long> requestIds)	throws BaseException;

	// ganjyar
	// does getEstelam2 only one request
	//void doEstelam2(Long cardRequestId) throws BaseException;
	
	
	// Anbari:Estelam3
	PersonEnquiryVTO doEstelam3(Long cardRequestId,Boolean isImageRequested) throws BaseException;	

	
	// ganjyar
	// get all requests' ids for do getEstelam2
	public List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException;
	
	//public void updateInfoByGetEstelam2(Long emsCardRequestId) throws BaseException;

	//public PersonEnquiryVTO getPersonalInfoByGetEstelam2(String nationalID) throws BaseException;

	//Anbari
	public List<Long> getRequestIdsForSendToAFIS(Integer fetchLimit) throws BaseException;

	//Anbari
	public void updateCitizenInfo(Long requestId) throws BaseException;
	
	//Anbari: Estelam3
	PersonEnquiryVTO updateInfoByGetEstelam3(Long emsCardRequestId,Boolean isImageRequested) throws BaseException, DateFormatException;
	
	//Anbari: Estelam3
	PersonEnquiryVTO updateInfoByGetEstelam3WithoutCitizenInfoUpdate(Long emsCardRequestId) throws BaseException;
	
	//Anbari: Estelam3
	PersonEnquiryVTO getPersonalInfoByGetEstelam3(String nationalID) throws BaseException;
	
	//Anbari: Estelam3
	void saveImsEstelamImage(String nationalId,CitizenTO citizenTO, ImsEstelamImageType estelamImageType, byte[] data) throws BaseException;
	
	//Anbari:Estelam3
	void updateBatchOfCitizenInfoByEstelam3(List<Long> requestIds)	throws BaseException;	
	
	//Anbari new getUpdateCitizenResult
	void getUpdatedCitizenResultNew(Long requestId) throws BaseException;	

	public ImsCitizenInfoResponseWTO verificationInDelivery(
			ImsCitizenInfoRequestWTO imsCitizenInfoRequest) throws BaseException;

	//Anbari:Estelam3 without citizenInfoUpdate
	PersonEnquiryVTO doEstelam3WithoutCitizenInfoUpdate(Long cardRequestId) throws BaseException;

	//Anbari:IMS
	List<Long> findAfisResultRequestsCountByState(CardRequestState sentToAfis,Integer fetchLimit) throws BaseException;

	//Anbari : Async
	Future<String> updateCitizenInfoAsync(Long requestId) throws BaseException;

}
