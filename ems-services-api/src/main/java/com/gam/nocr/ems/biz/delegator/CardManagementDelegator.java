package com.gam.nocr.ems.biz.delegator;

import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.CardManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.UnSuccessfulDeliveryRequestReason;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class CardManagementDelegator implements Delegator {

    private CardManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        CardManagementService cardManagementService = null;
        try {
            cardManagementService = factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_CARD_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.CMD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_CARD_MANAGEMENT.split(","));
        }
        cardManagementService.setUserProfileTO(userProfileTO);
        return cardManagementService;
    }

    public void deliver(UserProfileTO userProfileTO, long requestId, String message, byte[] messageDigest) throws BaseException {
        getService(userProfileTO).deliver(requestId, message, messageDigest);
    }

    public String retrieveDeliverMessage(UserProfileTO userProfileTO, Long cardRequestId) throws BaseException {
        return getService(userProfileTO).retrieveDeliverMessage(cardRequestId);
    }

    /**
     * The method notifyReceivedBatches is called by the job {@link com.gam.nocr.ems.biz.job.BatchReceivedNotificationJob}
     */
    public void notifyReceivedBatches(Integer from, Integer to) throws BaseException {
        getService(null).notifyReceivedBatches(from, to);
    }

    /**
     * The method notifyMissedBatches is called by the job {@link com.gam.nocr.ems.biz.job.BatchMissedNotificationJob}
     */
    public void notifyMissedBatches(Integer from, Integer to) throws BaseException {
        getService(null).notifyMissedBatches(from, to);
    }

    /**
     * The method notifyMissedBoxes is called by the job {@link com.gam.nocr.ems.biz.job.BoxMissedNotificationJob}
     */
    public void notifyMissedBoxes(Integer from, Integer to) throws BaseException {
        getService(null).notifyMissedBoxes(from, to);
    }

    /**
     * The method notifyMissedCards is called by the job {@link com.gam.nocr.ems.biz.job.CardMissedNotificationJob}
     */
    public void notifyMissedCards(Integer from, Integer to) throws BaseException {
        getService(null).notifyMissedCards(from, to);
    }

    /**
     * The method notifyCardsHandedOut is called by the job {@link com.gam.nocr.ems.biz.job.CardHandingOutNotificationJob}
     *
     * @param from is an instance of type {@link Integer}, which represents the first record to be processed
     * @param to   is an instance of type {@link Integer}, which represents the last record to be processed
     * @return an instance of type {@link Boolean}, which specifies by the value of true or false, whether the process
     * of calling the method will be replicated or not
     */
//    public Boolean notifyCardsHandedOut(Integer from, Integer to) throws BaseException {
//        return getService(null).notifyCardsHandedOut(from, to);
//    }
    
    public Boolean notifyCardsHandedOut(Long cardRequestId) throws BaseException {
        return getService(null).notifyCardsHandedOut(cardRequestId);
    }
    
    public Future<String> notifyCardsHandedOutAsync(Long cardRequestId) throws BaseException {
        return getService(null).notifyCardsHandedOutAsync(cardRequestId);
    }

    /**
     * The method notifyUnsuccessfulDelivery is used to handle all the businesses which are needed for the unsuccessful
     * delivery requests
     *
     * @param userProfileTO is an object of type {@link UserProfileTO} which is used to check the permissions
     * @param requestID     is an object of type {@link Long} which represents a specified id of the object of type {@link
     *                      com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param reason
     */
    public void notifyUnsuccessfulDelivery(UserProfileTO userProfileTO,
                                           Long requestID,
                                           UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        getService(userProfileTO).notifyUnsuccessfulDelivery(requestID, reason);
    }

    public Long findRequestsCountByState(CardRequestState cardRequestState) throws BaseException {
        return getService(null).findRequestsCountByState(cardRequestState);
    }
    
    public List<Long> findRequestsIdByState(CardRequestState cardRequestState, Integer fetchLimit) throws BaseException {
        return getService(null).findRequestsIdByState(cardRequestState,fetchLimit);
    }

    public Long findReceivedBatchesCount() throws BaseException {
        return getService(null).findReceivedBatchesCount();
    }

    public Long findMissedBatchesCount() throws BaseException {
        return getService(null).findMissedBatchesCount();
    }

    public Long findMissedBoxesCount() throws BaseException {
        return getService(null).findMissedBoxesCount();
    }

    public Long findMissedCardsCount() throws BaseException {
        return getService(null).findMissedCardsCount();
    }

    public void processOnDamagedCards(Integer from) throws BaseException{
        getService(null).processOnDamagedCards(from);
    }

    public void processOnRequestsWithBiometricProblem(Integer from) throws BaseException{
        getService(null).processOnRequestsWithBiometricProblem(from);
    }

    public Boolean checkCRNValidation(UserProfileTO up, Long requestID, String crn) throws BaseException{
        return getService(up).checkCRNValidation(requestID, crn);
    }

	public void processOnRequestsWithIdentifyChanged(Integer from) throws BaseException{
		getService(null).processOnRequestsWithIdentifyChanged(from);
		
	}

}
