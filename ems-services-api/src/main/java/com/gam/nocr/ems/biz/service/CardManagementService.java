package com.gam.nocr.ems.biz.service;

import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.UnSuccessfulDeliveryRequestReason;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CardManagementService extends Service {

    /**
     * The method checkRequestValidation is used for checking some required validations before applying/saving a new request
     *
     * @param citizenNationalId is an instance of type {@link}
     * @param cardRequestType   is a parameter of type {@link com.gam.nocr.ems.data.enums.CardRequestType} which represents the type of given request
     * @throws {@link BaseException}
     */
    void checkRequestValidation(String citizenNationalId, CardRequestType cardRequestType) throws BaseException;

    /**
     * The method notifyReceivedBatches is used to be sent the report of receiving a specified batches by EMS
     *
     * @throws {@link BaseException}
     */
    String notifyReceivedBatches(Integer from, Integer to) throws BaseException;

    /**
     * The method notifyMissedBatches is used to be sent the report of missing for the specified batches
     *
     * @throws {@link BaseException}
     */
    String notifyMissedBatches(Integer from, Integer to) throws BaseException;

    /**
     * The method notifyMissedBoxes is used to be sent the report of missing for the specified boxes
     *
     * @throws {@link BaseException}
     */
    String notifyMissedBoxes(Integer from, Integer to) throws BaseException;

    /**
     * The method notifyMissedCards is used to be sent the report of missing for the specified cards
     *
     * @throws {@link BaseException}
     */
    String notifyMissedCards(Integer from, Integer to) throws BaseException;

    /**
     * The method notifyCardsHandedOut is used to be sent the report of handing out a specified card by EMS
     *
     * @param from is an instance of type {@link Integer}, which represents the first record to be processed
     * @param to   is an instance of type {@link Integer}, which represents the last record to be processed
     * @return an instance of type {@link Boolean}, which specifies by the value of true or false, whether the process
     * of calling the method will be replicated or not
     * @throws {@link BaseException}
     */
    //Boolean notifyCardsHandedOut(Integer from, Integer to) throws BaseException;
    
    Boolean notifyCardsHandedOut(Long cardRequestId) throws BaseException;
    
    
    Future<String> notifyCardsHandedOutAsync(Long cardRequestId) throws BaseException;

    /**
     * The method deliver is used to set some updates which are depend on the delivery service
     *
     * @param requestId     is an object of type {@link Long} which represents a specified id of the object of type {@link
     *                      com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param receipt       is a String value which represents the plane text that is used to create a receipt
     * @param signedReceipt is a String value which represents a signed message
     * @throws {@link BaseException}
     */
    String deliver(Long requestId,
                   String receipt,
                   byte[] signedReceipt) throws BaseException;

    String retrieveDeliverMessage(Long cardRequestId) throws BaseException;

    /**
     * The method notifyUnsuccessfulDelivery is used to handle all the businesses which are needed for the unsuccessful
     * delivery requests
     *
     * @param requestID is an object of type {@link Long} which represents a specified id of the object of type {@link
     *                  com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param reason    is an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason} which represents the
     *                  reason of activities that should be done
     * @throws {@link BaseException}
     */
    String notifyUnsuccessfulDelivery(Long requestID,
                                      UnSuccessfulDeliveryRequestReason reason) throws BaseException;

    String processOnDamagedCards(Integer from) throws BaseException;

    String processOnRequestsWithBiometricProblem(Integer from) throws BaseException;

    Long findRequestsCountByState(CardRequestState cardRequestState) throws BaseException;
    List<Long> findRequestsIdByState(CardRequestState cardRequestState, Integer fetchLimit) throws BaseException;
    
    Long findReceivedBatchesCount() throws BaseException;

    Long findMissedBatchesCount() throws BaseException;

    Long findMissedBoxesCount() throws BaseException;

    Long findMissedCardsCount() throws BaseException;

    /**
     * This method is responsible for checking the validity of given CRN
     *
     * @param requestID is an instance of type {@link Long} which identifies an appropriate card request
     * @param crn       is an instance of type {@link String} which identifies an appropriate card
     * @return true if the given CRN is matched to the card crn which is associated to the given card request. Otherwise it returns false
     * @throws BaseException
     */
    Boolean checkCRNValidation(Long requestID, String crn) throws BaseException;


	/**
	 * @author ganjyar 
	 */
	public void doConfirmLostCard(Long cardId) throws BaseException;

	/**
	 * @author ganjyar
	 * 
	 */
	public List<CardDispatchInfoVTO> fetchCardLostTempList(
			GeneralCriteria criteria) throws BaseException;

	/**
	 * @author ganjyar
	 * 
	 */
	public Integer countCardLostTemp(GeneralCriteria criteria) throws BaseException;

	/**
	 * @author ganjyar
	 * 
	 */
	void doConfirmLostBatch(Long batchId) throws BaseException;


	/**
	 * @author ganjyar
	 * 
	 */
	Integer countBatchLostTemp(GeneralCriteria criteria) throws BaseException;

	/**
	 * @author ganjyar
	 * 
	 */
	List<BatchDispatchInfoVTO> fetchBatchLostTempList(GeneralCriteria criteria) throws BaseException;

	ImsCitizenInfoResponseWTO doImsVerificationInDelivery(
			ImsCitizenInfoRequestWTO imsCitizenInfoRequestWTO) throws BaseException;

	String processOnRequestsWithIdentifyChanged(Integer from) throws BaseException;

}
