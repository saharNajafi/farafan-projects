package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.vol.EmsCardDeliverInfo;
import com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;

import java.util.HashMap;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSService extends Service {

	/**
	 * The method sendBatchEnquiryRequestForFirstTime is used to send the batch enquiring request.
	 *
	 * @param transferInfoVTO  is carries the citizens' information
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return an instance of type {@link String}
	 */
	public String sendBatchEnquiryRequest(TransferInfoVTO transferInfoVTO) throws BaseException;

	/**
	 * The method getBatchEnquiryResponse is used to receive the response of the batch enquiring request from IMS sub
	 * system.
	 *
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return an object of type {@link TransferInfoVTO} which carries the citizen's information
	 */
	public TransferInfoVTO getBatchEnquiryResponse() throws BaseException;

	/**
	 * The method updateCitizensInfo is used to send the request for updating the citizen information.
	 *
	 * @param cardRequestTOList a list of type {CardRequestTO}
	 * @param requestId         is an instance of type {@link String} which represents the requestId of the update
	 *                          request
	 * @param imsProxyProvider  is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                          IMSProxyProvider,
	 *                          as regards of the configurations that set in profileManager
	 */
	public void updateCitizensInfo(List<CardRequestTO> cardRequestTOList,
								   String requestId) throws BaseException;

	/**
	 * The method loadUpdatedCitizensInfoResult is used to receive the response of updating the citizen information
	 * request
	 * from IMS sub system.
	 *
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @param imsRequestId     is an String value
	 * @return a list of type {@link IMSUpdateResultVTO}
	 */
	public List<IMSUpdateResultVTO> getUpdatedCitizensResult(String imsRequestId) throws BaseException;

	/**
	 * The method setCitizenCardDelivered is used to notify the IMS about this issue which the card was delivered to
	 * citizen.
	 * @param nationalId       represents the nationalId of a specified citizen
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return true or false (to show whether the alerting operation has done successfully or not)
	 */
	public boolean setCitizenCardDelivered(EmsCardDeliverInfo cardDeliverInfo) throws BaseException;


	/**
	 * The method fetchCitizenInfo if used to receive all the information of a specified citizen from the sub system
	 * 'IMS'.
	 *
	 * @param nationalId       is an string value which represents the nationalId of a specified citizen
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
	 * @throws BaseException
	 */
	public CitizenTO fetchCitizenInfo(String nationalId) throws BaseException;

	/**
	 * The method getOnlineEnquiry is used to get the online enquiry from the sub system 'IMS'
	 *
	 * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which
	 *                          carries the necessary attributes to enquiry
	 * @param imsProxyProvider  is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                          IMSProxyProvider,
	 *                          as regards of the configurations that set in profileManager
	 * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which carries nationalId and the result of
	 *         enquiry(true or false)
	 */
	HashMap<String, Boolean> getOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;


	/**
	 * The method getOnlineEnquiry is used to fetch the citizen info from the IMS sub system
	 *
	 * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which
	 *                          carries the necessary attributes to enquiry
	 * @param imsProxyProvider  is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                          IMSProxyProvider, as regards of the configurations that set in profileManager
	 * @return a hashmap of {@link java.util.HashMap <String, PersonEnquiryVTO>} which carries nationalId and an instance
	 *         of type {@link PersonEnquiryVTO}, which was valued By IMS database
	 */
	HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;
	
	//HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiryByEstelam2(PersonEnquiryVTO[] personEnquiryVTOs,String citizenNID, IMSProxyProvider imsProxyProvider) throws BaseException;
	
	//Anbari:Estelam3
	PersonEnquiryVTO fetchDataByOnlineEnquiryByEstelam3(PersonEnquiryVTO personEnquiryVTOs, String requestedPersonNationaID) throws BaseException;
	
	//Anbari:IMS
	IMSUpdateResultVTO getUpdatedCitizensResultNew(String nationalId) throws BaseException;
	


}
