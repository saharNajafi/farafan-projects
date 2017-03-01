package com.gam.nocr.ems.biz.delegator;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.IMSManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class IMSDelegator implements Delegator {

	private IMSManagementService getService(UserProfileTO userProfileTO)
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
		imsManagementService.setUserProfileTO(userProfileTO);
		return imsManagementService;
	}

	public void getUpdatedCitizensResult() throws BaseException {
		getService(null).loadUpdatedCitizensInfoResult();
	}

	/**
	 * The method fetchCitizenInfo is used to fetch the citizen information from
	 * the sub system 'IMS'
	 * 
	 * @param nationalId
	 *            is an instance of type {@link String} which represents the
	 *            citizen national id
	 * @return an instance of type {@link CitizenTO}
	 * @throws BaseException
	 */
	public CitizenTO fetchCitizenInfo(String nationalId) throws BaseException {
		return getService(null).fetchCitizenInfo(nationalId);
	}

	/**
	 * The method getRequestsCountForSendToAFIS is used to get the count of
	 * requests, which are ready to send to AFIS
	 * 
	 * @return an instance of type {@link Long}, which represents the number of
	 *         ready requests to send
	 * @throws BaseException
	 */
	public Long getRequestsCountForSendToAFIS() throws BaseException {
		return getService(null).getRequestsCountForSendToAFIS();
	}

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
	public void updateCitizenInfo(Integer from) throws BaseException {
		getService(null).updateCitizenInfo(from);
	}

	public Integer getRequestsCountForBatchEnquiry(String queryName,
			HashMap<String, String> param) throws BaseException {
		return getService(null).getRequestsCountForBatchEnquiry(queryName,
				param);
	}
	
	public List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException {
		return getService(null).getRequestsIdsForEnquiry(limit);
	}
	
	public void sendBatchEnquiryReqForFirstTime(Integer from,
			Integer batchSize, CardRequestState cardRequestState)
			throws BaseException {
		getService(null).sendBatchEnquiryReq(from, batchSize, cardRequestState);
	}

	public String[] getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager()
			throws BaseException {
		return getService(null)
				.getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager();
	}

	public TransferInfoVTO fetchBatchEnquiryResult() throws BaseException {
		return getService(null).getBatchEnquiryResult();
	}

	public void updateRequestsByBatchEnquiryResult(
			TransferInfoVTO transferInfoVTO) throws BaseException {
		getService(null).updateRequestsByBatchEnquiryResult(transferInfoVTO);
	}

	public Integer getRequestCountToFetchFromAFIS() throws BaseException {
		return getService(null).getRequestCountToFetchFromAFIS();
	}

	public void getUpdatedCitizenResult(Integer from) throws BaseException {
		getService(null).getUpdatedCitizenResult(from);
	}

	public Long findRequestsCountByState(CardRequestState requestState)
			throws BaseException {
		return getService(null).findRequestsCountByState(requestState);
	}

//	// does getEstelam2 for a batch of citizen
//	// ganjyar
//	public void updateCitizenInfoByEstelam2(List<Long> requestIds)
//			throws BaseException {
//		getService(null).updateBatchOfCitizenInfoByEstelam2(requestIds);
//		// getService(null).doEstelam2(198L);
//
//	}
	
	//Anbari : change AFIS mechanism to fetch card request ids
	public List<Long> getRequestIdsForSendToAFIS(Integer fetchLimit) throws BaseException {
		return getService(null).getRequestIdsForSendToAFIS(fetchLimit);
	}

	//Anbari
	public void updateCitizenInfoById(Long requestId) throws BaseException  {
		getService(null).updateCitizenInfo(requestId);
		
	}
	
	//Anbari:Estelam3
	public void updateCitizenInfoByEstelam3(List<Long> requestIds)
			throws BaseException {		
		getService(null).updateBatchOfCitizenInfoByEstelam3(requestIds);
	}
	
	//Anbari:IMS
		public void getUpdatedCitizenResultNew(Long id) throws BaseException {
			getService(null).getUpdatedCitizenResultNew(id);
	}

	//Anbari:IMS
	public List<Long> findAfisResultRequestsCountByState(
			CardRequestState sentToAfis, Integer fetchLimit) throws BaseException {
		return getService(null).findAfisResultRequestsCountByState(sentToAfis,fetchLimit);
	}

	//Anbari: Async
	public Future<String> updateCitizenInfoByIdAsync(Long requestId) throws BaseException {
		return getService(null).updateCitizenInfoAsync(requestId);
	}
}
