package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.EmsCardDeliverInfo;
import com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;

import java.util.HashMap;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSProxy extends IMSBatchService, IMSOnlineService, IMSFarafanService {

    /**
     * The method sendBatchEnquiryRequest is used to send a request toward IMS for fulfilling batch enquiry
     *
     * @param transferInfoVTO is an instance of type {@link TransferInfoVTO}, which carries the required information for batch enquiry
     * @return an instance of type {@link String}
     * @throws BaseException
     */
    String sendBatchEnquiryRequest(TransferInfoVTO transferInfoVTO) throws BaseException;

    /**
     * The method getBatchEnquiryResponse is used to receive the response of the batch enquiring request from IMS sub
     * system.
     *
     * @return an object of type {@link TransferInfoVTO} which carries the result of batch enquiry
     */
    TransferInfoVTO getBatchEnquiryResponse() throws BaseException;

    /**
     * The method updateCitizensInfo is used to send the request for updating the citizen information.
     *
     * @param cardRequestTOList a list of type {CardRequestTO}
     * @param imsRequestId      is an instance of type {@link String}, which represents the requestId of the update
     *                          request message
     */
    void updateCitizensInfo(List<CardRequestTO> cardRequestTOList,
                            String imsRequestId) throws BaseException;

    /**
     * The method getUpdatedCitizensInfoResult is used to receive the response of updating the citizen information
     * request from IMS sub system.
     *
     * @param imsRequestId is an instance of type {@link String}, which represents the requestId of the update
     *                     request message
     * @return a list of type {@link IMSUpdateResultVTO}
     */
    List<IMSUpdateResultVTO> getUpdatedCitizensResult(String imsRequestId) throws BaseException;

    /**
     * The method setCitizenCardRequested is used to notify the IMS about sending an issuanceCardRequest for a specified citizen
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return true or false (to show whether the alerting operation has done successfully or not)
     */
    boolean setCitizenCardRequested(String nationalId) throws BaseException;

    /**
     * The method setCitizenCardDelivered is used to notify the IMS about the delivering the card by citizen
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return true or false (to show whether the alerting operation has done successfully or not)
     */
    boolean setCitizenCardDelivered(EmsCardDeliverInfo cardDeliverInfo) throws BaseException;

    /**
     * The method fetchCitizenInfo if used to receive all the information about a specified citizen, from the sub system
     * 'IMS'.
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return an instance of type {@link CitizenTO}
     * @throws BaseException
     */
    CitizenTO fetchCitizenInfo(String nationalId) throws BaseException;

    /**
     * The method getOnlineEnquiry is used to get the online enquiry from the sub system 'IMS'
     *
     * @param personEnquiryVTOs is an array of type {@link PersonEnquiryVTO} which carries the necessary attributes for the process of online enquiry
     * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which carries nationalId and the result of the online enquiry(true or false)
     */
    HashMap<String, Boolean> getOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;

    /**
     * The method fetchDataByOnlineEnquiry is used to fetch the citizen info from the IMS sub system
     *
     * @param personEnquiryVTOs is an array of type {@link PersonEnquiryVTO} which carries the necessary attributes for fetching data from IMS
     * @return a hashmap of {@link java.util.HashMap <String, PersonEnquiryVTO>} which carries nationalId and an instance
     *         of type {@link PersonEnquiryVTO}, which was valued By means of the IMS database
     */
    HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;

}
