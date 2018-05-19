package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.SequenceName;
import com.gam.nocr.ems.data.enums.SystemId;

import java.util.List;
import java.util.Map;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CardRequestHistoryDAO extends EmsBaseDAO<CardRequestHistoryTO> {

    CardRequestHistoryTO create(CardRequestTO cardRequestTO,
                                String result,
                                SystemId systemId,
                                String requestId,
                                CardRequestHistoryAction cardRequestHistoryAction,
                                String actor) throws BaseException;

    /**
     * The method findByCardRequestToAndResult is used to find a list of type {@link CardRequestHistoryTO} in spite of
     * cardRequestTO id and the result value
     *
     * @param cardRequestId represents the id of an instance of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param result        an instance of type {@link String} which represents a brief description about the done
     *                      operation
     * @retur list of type {@lin CardRequestHistoryTO} or null if there is nothing to retur
     */
    List<CardRequestHistoryTO> findByCardRequestToAndResult(long cardRequestId, String result) throws BaseException;

    CardRequestHistoryTO findByCmsRequestId(String cmsRequestId) throws BaseException;

    /**
     * The method getRequestIdFromSequence is used to return a requestId of a specified sequence from data base.
     *
     * @param sequenceName is an enum instance of type {@link SequenceName}
     * @return a number of type long as requestId
     */
    long getRequestIdFromSequence(SequenceName sequenceName) throws BaseException;

    /**
     * @param systemId
     * @return
     */
    List<CardRequestHistoryTO> findBySystemId(SystemId systemId) throws BaseException;

    /**
     * @param cmsRequestId
     * @param cardTO
     */
    void updateRequestHistory(String cmsRequestId, CardTO cardTO) throws BaseException;

    List<CardRequestHistoryTO> findByCmsRequestIdAndResult(String cmsRequestId, String result) throws BaseException;

    /**
     * The method checkAcceptabilityOfRetryRequest is used to check whether the retry process for sending issuance request
     * is
     * possible or not
     *
     * @param cardRequestTO is an instance of type {@link com.gam.nocr.ems.data.domain.CardRequestHistoryTO} which carries
     *                      the necessary
     *                      attributes for checking
     * @param systemId      is an enum object of type {@link SystemId}
     * @return true if the retry process is acceptable otherwise return false
     */
    boolean checkAcceptabilityOfRetryRequest(CardRequestTO cardRequestTO,
                                             SystemId systemId) throws BaseException;

    /**
     * The method bulkUpdateCardRequestHistoryBySubSystemRequestId is use to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CardRequestHistoryTO} in spite of requestId list
     *
     * @param result
     * @param cardStringReqIds a list of type String which represents a specified number of requestIDs
     */
    void bulkUpdateCardRequestHistoryBySubSystemRequestId(List<String> cardStringReqIds,
                                                          String result) throws BaseException;

    /**
     * The method findSubSystemRequestIdsByRequestStateAndSystemId is used to find a list of type {@link String} which
     * represents the subsystemRequestIds
     *
     * @param requestState an instance of type {@link CardRequestState}
     * @param systemId     an enum type of {@link SystemId}
     * @return a list of type {@link String} which represents the subsystem requestId
     * @throws com.gam.commons.core.BaseException
     *
     */
    List<String> findSubSystemRequestIdsByRequestStateAndSystemId(CardRequestState requestState,
                                                                  SystemId systemId) throws BaseException;

    /**
     * The method findUniqueByNationalId is used to find an instance of type {@link CardRequestHistoryTO} which refers to
     * a unique record by using the nationalId
     *
     * @param nationalId an instance of type {@link String } which represents the citizen national Id
     * @return a list of type {@link CardRequestHistoryTO}
     * @throws BaseException
     */
    List<CardRequestHistoryTO> findUniqueByNationalId(String nationalId) throws BaseException;

    /**
     * The method findCardRequestIdsBySubSystemRequestId is used to find the id of the request in spite of the
     * subsystemRequestId
     *
     * @param subsystemRequestId is an instance of type {@link String} which represents the subsystem request id
     * @return a list of type {@link Long} which represents a specified cardRequestId
     * @throws com.gam.commons.core.BaseException
     *
     */
    List<Long> findCardRequestIdsBySubSystemRequestId(String subsystemRequestId) throws BaseException;


//	/**
//	 * The method findMaxCardRequestHistoryIdByREQUESTState is used to find the max cardRequestHistory in spite of a
//	 * specified state of request
//	 *
//	 * @param state is an instance of type {@link CardRequestState}, which represents the state of the request
//	 * @return an instance of type {@link Long} or null, which represents an specified id of an instance of type{@link
//	 *         CardRequestHistoryTO}
//	 * @throws BaseException
//	 */
//	Long findMaxCardRequestHistoryIdByREQUESTState(CardRequestState state) throws BaseException;

    /**
     * The method updateCardRequestHistoryTOs is use to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CardRequestHistoryTO} in spite of requestId list
     *
     * @param map              is a map of type {@link java.util.Map <Long, String>}, which carries the cardRequestIds as keys and
     *                         their appropriate result as values
     * @param historyOldResult is an instance of type {@link String}, which represents the old value of the result field that belongs to a specified record
     */
    void updateCardRequestHistoryTOs(Map<Long, String> map,
                                     String historyOldResult) throws BaseException;

    CardRequestHistoryTO fetchLastHistoryRecord(Long cardRequestId) throws BaseException;

    Integer getRequestCountToFetchFromAFIS() throws BaseException;

    String findSentToAFISMessageRequestId(Long requestId) throws BaseException;
    
    Boolean existInHistoryByCmsReqId(String cmsReqId , Long requestId);

	CardRequestHistoryTO findHistoryByRequestIdAndAction(Long id,
			CardRequestHistoryAction authenticateDocument) throws BaseException;

	List<CardRequestHistoryTO> fetchAllHistoryByRequestId(Long id) throws BaseException;

    CardRequestHistoryTO findByCardRequestId(Long cardRequestId) throws BaseException;


}
