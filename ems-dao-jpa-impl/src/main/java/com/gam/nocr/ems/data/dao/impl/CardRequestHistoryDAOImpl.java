package com.gam.nocr.ems.data.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.SequenceName;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "CardRequestHistoryDAO")
@Local(CardRequestHistoryDAOLocal.class)
@Remote(CardRequestHistoryDAORemote.class)
public class CardRequestHistoryDAOImpl extends EmsBaseDAOImpl<CardRequestHistoryTO> implements CardRequestHistoryDAOLocal, CardRequestHistoryDAORemote {

    private static final Logger logger = BaseLog.getLogger(CardRequestHistoryDAOImpl.class);

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public CardRequestHistoryTO create(CardRequestTO cardRequestTO,
                                       String result,
                                       SystemId systemId,
                                       String requestId,
                                       CardRequestHistoryAction cardRequestHistoryAction,
                                       String actor) throws BaseException {
        CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();
        cardRequestHistoryTO.setCardRequest(cardRequestTO);
        cardRequestHistoryTO.setResult(result);
        cardRequestHistoryTO.setDate(new Date());
        cardRequestHistoryTO.setSystemID(systemId);
        cardRequestHistoryTO.setRequestID(requestId);
        cardRequestHistoryTO.setCardRequestHistoryAction(cardRequestHistoryAction);
        cardRequestHistoryTO.setActor(actor);

        try {
            super.create(cardRequestHistoryTO);
            em.flush();
            return cardRequestHistoryTO;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("PK_CRH_ID"))
                throw new DAOException(DataExceptionCode.CRH_017, DataExceptionCode.CRH_017_MSG, e);
            if (err.contains("FK_CRD_REQ_REQUEST_ID"))
                throw new DAOException(DataExceptionCode.CRH_018, DataExceptionCode.CRH_018_MSG, e);
            if (err.contains("CKC_CRH_SYSTEM_ID"))
                throw new DAOException(DataExceptionCode.CRH_019, DataExceptionCode.CRH_019_MSG, e);
            if (err.contains("CKC_CRH_CARD_REQUEST_HISTORY_ACTION"))
                throw new DAOException(DataExceptionCode.CRH_020, DataExceptionCode.CRH_020_MSG, e);
            throw new DAOException(DataExceptionCode.CRH_016, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    /**
     * The method findByCardRequestToAndResult is used to find a list of type {@link com.gam.nocr.ems.data.domain.CardRequestHistoryTO}
     * in spite of cardRequestTO id and the result value
     *
     * @param cardRequestId represents the id of an instance of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param result        an instance of type {@link String} which represents a brief description about the done
     *                      operation
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestHistoryTO} or null if there is nothing to
     *         return
     */
    @Override
    public List<CardRequestHistoryTO> findByCardRequestToAndResult(long cardRequestId,
                                                                   String result) throws BaseException {
        try {
            return em.createQuery("SELECT CRH FROM CardRequestHistoryTO CRH WHERE " +
                    "CRH.cardRequest.id = :CARD_REQUEST_ID AND " +
                    "CRH.result LIKE :RESULT ORDER BY CRH.id DESC", CardRequestHistoryTO.class)
                    .setParameter("CARD_REQUEST_ID", cardRequestId)
                    .setParameter("RESULT", result)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_015, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public CardRequestHistoryTO findByCmsRequestId(String cmsRequestId) throws BaseException {
        List<CardRequestHistoryTO> cardRequestHistoryTOList;
        try {
            //  TODO : add where clause to filter records just by system id cms
            cardRequestHistoryTOList = em.createQuery(" select crh from CardRequestHistoryTO crh " +
                    " where crh.requestID = :cmsRequestId ", CardRequestHistoryTO.class)
                    .setParameter("cmsRequestId", cmsRequestId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_008, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestHistoryTOList))
            return cardRequestHistoryTOList.get(0);
        else
            return null;
    }

    /**
     * The method getRequestIdFromSequence is used to return a requestId of a specified sequence from data base.
     *
     * @param sequenceName is an enum instance of type {@link SequenceName}
     * @return a number of type long as requestId
     */
    @Override
    public long getRequestIdFromSequence(SequenceName sequenceName) throws BaseException {
        Long seqNumber = 0L;
        try {
            String stringSeq = "";
            switch (sequenceName) {
                case SEQ_EMS_REQUEST_ID_CMS:
                    stringSeq = em.createNativeQuery("SELECT SEQ_EMS_REQID_CMS.NEXTVAL FROM DUAL").getSingleResult().toString();
                    break;

                case SEQ_EMS_REQUEST_ID_IMS_UPDATE_CITIZEN_INFO:
                    stringSeq = em.createNativeQuery("SELECT SEQ_EMS_REQID_IMS.NEXTVAL FROM DUAL").getSingleResult().toString();
                    break;

                case SEQ_EMS_REQUEST_ID_IMS_BATCH_ENQUIRY:
                    stringSeq = em.createNativeQuery("SELECT SEQ_EMS_REQID_IMS_BAT_ENQ.NEXTVAL FROM DUAL").getSingleResult().toString();
                    break;
            }
            seqNumber = Long.parseLong(stringSeq);
        } catch (Exception e) {
            throw new DAOException(
                    DataExceptionCode.CRH_001,
                    DataExceptionCode.GLB_002_MSG,
                    e);
        }
        return seqNumber;
    }

    @Override
    public List<CardRequestHistoryTO> findBySystemId(SystemId systemId) throws BaseException {
        if (systemId == null) {
            throw new DAOException(DataExceptionCode.CRH_007, DataExceptionCode.CRH_007_MSG);
        }
        try {
            return em.createQuery("select crh from CardRequestHistoryTO crh " +
                    "where crh.systemID = :systemId", CardRequestHistoryTO.class)
                    .setParameter("systemId", systemId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_006, DataExceptionCode.CRH_006_MSG, e, new Object[]{systemId.name()});
        }

    }

    @Override
    public void updateRequestHistory(String cmsRequestId, CardTO cardTO) throws BaseException {
        try {
            em.createQuery(" update CardRequestHistoryTO crh " +
                    " set crh.result = :result" +
                    " where crh.requestID = :cmsRequestId")
                    .setParameter("result", "crn=" + cardTO.getCrn())
                    .setParameter("cmsRequestId", cmsRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_002, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public List<CardRequestHistoryTO> findByCmsRequestIdAndResult(String cmsRequestId, String result) throws BaseException {
        try {
            return em.createQuery(" select crh from CardRequestHistoryTO crh " +
                    " where crh.result = :result " +
                    " and crh.requestID = :cmsRequestId ", CardRequestHistoryTO.class)
                    .setParameter("result", result)
                    .setParameter("cmsRequestId", cmsRequestId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_003, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method checkAcceptabilityOfRetryRequest is used to check whether the retry process for sending issuance request
     * is possible or not
     *
     * @param cardRequestTO is an instance of type {@link com.gam.nocr.ems.data.domain.CardRequestTO} which
     *                      carries the necessary attributes for checking
     * @param systemId      is an enum object of type {@link SystemId}
     * @return true if the retry process is acceptable otherwise return false
     */
    @Override
    public boolean checkAcceptabilityOfRetryRequest(CardRequestTO cardRequestTO,
                                                    SystemId systemId) throws BaseException {


        Long DEFAULT_TRY_COUNTER = 3L;
        Long tryCounter = null;
        Long count;
        boolean returnFlag = true;
        try {
            count = (Long) em.createQuery("" +
                    "SELECT COUNT(crh.id) FROM CardRequestHistoryTO crh " +
                    "WHERE " +
                    "crh.cardRequest.id = :CARD_REQUEST_ID AND " +
                    "crh.result LIKE :RESULT ")
                    .setParameter("CARD_REQUEST_ID", cardRequestTO.getId())
                    .setParameter("RESULT", "%" + systemId.name() + ":" + ConstValues.GAM_ERROR_WITH_LIMITED_RETRY + "%")
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_004, DataExceptionCode.GLB_005_MSG, e);
        }

        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            switch (systemId) {
                case CMS:
                    tryCounter = Long.parseLong((String) pm.getProfile(ProfileKeyName.KEY_CMS_SEND_CARD_REQUEST_TRY_COUNTER, true, null, null));
                    break;
                case IMS:
                    tryCounter = Long.parseLong((String) pm.getProfile(ProfileKeyName.KEY_IMS_UPDATE_CITIZEN_INFO_RESULT_TRY_COUNTER, true, null, null));
                    break;
            }
        } catch (Exception e) {
            logger.warn(DataExceptionCode.CRH_009, DataExceptionCode.CRH_009_MSG, new Long[]{DEFAULT_TRY_COUNTER});
        }

        if (tryCounter == null) {
            tryCounter = DEFAULT_TRY_COUNTER;
        }

        if (count >= tryCounter) {
            returnFlag = false;
        }

        return returnFlag;
    }

    /**
     * The method bulkUpdateCardRequestHistoryBySubSystemRequestId is use to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CardRequestHistoryTO} in spite of requestId list
     *
     * @param result
     * @param cardStringReqIds a list of type String which represents a specified number of requestIDs
     */
    @Override
    public void bulkUpdateCardRequestHistoryBySubSystemRequestId(List<String> cardStringReqIds,
                                                                 String result) throws BaseException {
        try {
            em.createQuery(" UPDATE CardRequestHistoryTO crh " +
                    "SET crh.result = :RESULT " +
                    "WHERE crh.requestID IN (:REQUEST_ID_LIST)")
                    .setParameter("RESULT", result)
                    .setParameter("REQUEST_ID_LIST", cardStringReqIds)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_005, DataExceptionCode.GLB_006_MSG, e);
        }
    }

//	public List<CardRequestHistoryTO> findByCmsRequestId(String cmsRequestId) throws BaseException {
//		try {
//			em.createQuery(" select crh from CardRequestHistoryTO crh " +
//					" where crh.requestID = :cmsRequestId ", CardRequestHistoryTO.class)
//					.setParameter("cmsRequestId", cmsRequestId)
//					.getResultList();
//		} catch (Exception e) {
//			throw new DAOException(DataExceptionCode.CRH_00, DataExceptionCode.GLB_005_MSG, e);
//		}
//	}

    /**
     * The method findSubSystemRequestIdsByRequestStateAndSystemId is used to find a list of type {@link String} which
     * represents the subsystemRequestIds
     *
     * @param requestState an instance of type {@link CardRequestState}
     * @param systemId     an enum type of {@link com.gam.nocr.ems.data.enums.SystemId}
     * @return a list of type {@link String} which represents the subsystem requestId
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<String> findSubSystemRequestIdsByRequestStateAndSystemId(CardRequestState requestState,
                                                                         SystemId systemId) throws BaseException {
        try {
            return em.createQuery("" +
                    "SELECT CRH.requestID " +
                    "FROM CardRequestHistoryTO CRH, CardRequestTO CRQ " +
                    "WHERE CRH.cardRequest.id = CRQ.id AND " +
                    "CRQ.state =:REQUEST_STATE AND " +
                    "CRH.result LIKE :RESULT AND " +
                    "CRH.systemID = :SYSTEM_ID " +
                    "GROUP BY CRH.requestID " +
                    "ORDER BY CRH.requestID ASC", String.class)
                    .setParameter("REQUEST_STATE", requestState)
                    .setParameter("RESULT", "%" + requestState.name() + "%")
                    .setParameter("SYSTEM_ID", systemId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_010, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findUniqueByNationalId is used to find an instance of type {@link com.gam.nocr.ems.data.domain.CardRequestHistoryTO}
     * which refers to
     * a unique record by using the nationalId
     *
     * @param nationalId an instance of type {@link String } which represents the citizen national Id
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestHistoryTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<CardRequestHistoryTO> findUniqueByNationalId(String nationalId) throws BaseException {
        try {
            return em.createQuery("" +
                    "SELECT CRH FROM CardRequestHistoryTO CRH, CardRequestTO CRQ, CitizenTO CTZ " +
                    "WHERE CRQ.citizen.id = CTZ.id AND " +
                    "CTZ.nationalID LIKE :NATIONAL_ID AND " +
                    "CRQ.id = CRH.cardRequest.id AND " +
                    "CRH.result LIKE '%SENT_TO_AFIS%'", CardRequestHistoryTO.class)
                    .setParameter("NATIONAL_ID", nationalId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_011, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findCardRequestIdsBySubSystemRequestId is used to find the id of the request in spite of the
     * subsystemRequestId
     *
     * @param subsystemRequestId is an instance of type {@link String} which represents the subsystem request id
     * @return a list of type {@link Long} which represents a specified cardRequestId
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<Long> findCardRequestIdsBySubSystemRequestId(String subsystemRequestId) throws BaseException {
        try {
            return em.createQuery("" +
                    "SELECT DISTINCT CRH.cardRequest.id FROM CardRequestHistoryTO CRH " +
                    "WHERE CRH.requestID LIKE :SUBSYSTEM_REQUEST_ID", Long.class)
                    .setParameter("SUBSYSTEM_REQUEST_ID", subsystemRequestId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_012, DataExceptionCode.GLB_005_MSG, e);
        }

    }

    //	TODO : Correct it to log the state of 'VERIFIED_IMS' in the service of Dummy getBatchEnquiryResult
//	/**
//	 * The method findMaxCardRequestHistoryIdByREQUESTState is used to find the max cardRequestHistory in spite of a
//	 * specified state of request
//	 *
//	 * @param state is an instance of type {@link com.gam.nocr.ems.data.enums.CardRequestState}, which represents the
//	 *              state
//	 *              of the request
//	 * @return an instance of type {@link Long} or null, which represents an specified id of an instance of type{@link
//	 *         com.gam.nocr.ems.data.domain.CardRequestHistoryTO}
//	 * @throws com.gam.commons.core.BaseException
//	 *
//	 */
//	@Override
//	public Long findMaxCardRequestHistoryIdByREQUESTState(CardRequestState state) throws BaseException {
//
//		try {
//			return em.createQuery("" +
//					"SELECT MAX(CRH.id) FROM CardRequestHistoryTO CRH " +
//					"WHERE CRH.cardRequest.id IN (SELECT CRQ.id FROM CardRequestTO CRQ WHERE CRQ.state = :CARD_REQUEST_STATE)", Long.class)
//					.setParameter("CARD_REQUEST_STATE", state)
//					.getSingleResult();
//		} catch (Exception e) {
//			throw new DataException(DataExceptionCode.CRH_013, DataExceptionCode.GLB_005_MSG, e);
//		}
//	}

    /**
     * The method updateCardRequestHistoryTOs is use to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CardRequestHistoryTO} in spite of requestId list
     *
     * @param map              is a map of type {@link java.util.Map <Long, String>}, which carries the cardRequestIds as keys and
     *                         their appropriate result as values
     * @param historyOldResult is an instance of type {@link String}, which represents the old value of the result field that belongs to a specified record
     */
    @Override
    public void updateCardRequestHistoryTOs(Map<Long, String> map,
                                            String historyOldResult) throws BaseException {
        try {
            Object[] requestIds = map.keySet().toArray();
            for (Object requestId : requestIds) {
                try {
                    em.createQuery(" UPDATE CardRequestHistoryTO CRH " +
                            "SET CRH.result = :NEW_RESULT " +
                            "WHERE CRH.cardRequest.id = :CARD_REQUEST_ID AND " +
                            "CRH.id IN " +
                            "(" +
                            "SELECT MAX(CRH2.id) " +
                            "FROM CardRequestHistoryTO CRH2 " +
                            "WHERE CRH2.result = :OLD_RESULT AND " +
                            "CRH2.cardRequest.id = :CARD_REQUEST_ID" +
                            ")")
                            .setParameter("NEW_RESULT", map.get(requestId))
                            .setParameter("CARD_REQUEST_ID", requestId)
                            .setParameter("OLD_RESULT", historyOldResult)
                            .executeUpdate();
                    em.flush();
                } catch (Exception e) {
                    throw new DAOException(DataExceptionCode.CRH_013, DataExceptionCode.GLB_006_MSG, e);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CRH_014, DataExceptionCode.GLB_003_MSG, e);
        }
    }

    @Override
    public CardRequestHistoryTO fetchLastHistoryRecord(Long cardRequestId) throws BaseException {
        List<CardRequestHistoryTO> cardRequestHistoryTOs;
        List<CardRequestHistoryAction> skippableActions = new ArrayList<CardRequestHistoryAction>();
        skippableActions.add(CardRequestHistoryAction.SYNC_SUCCESS);
        skippableActions.add(CardRequestHistoryAction.SYNC_FAILED);
        try {
            cardRequestHistoryTOs = em.createQuery("SELECT CRH FROM CardRequestHistoryTO CRH " +
                    "WHERE CRH.cardRequest.id = :cardRequestId AND CRH.cardRequestHistoryAction not in (:actions) order by crh.date desc ", CardRequestHistoryTO.class)
                    .setParameter("cardRequestId", cardRequestId)
                    .setParameter("actions", skippableActions)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_021, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestHistoryTOs))
            return cardRequestHistoryTOs.get(0);
        else
            return null;
    }

    @Override
    public Integer getRequestCountToFetchFromAFIS() throws BaseException {

        try {
            List<Long> countList = em.createQuery("" +
                    "SELECT COUNT(DISTINCT CRQ.id) " +
                    "FROM CardRequestHistoryTO CRH, CardRequestTO CRQ " +
                    "WHERE CRH.cardRequest.id = CRQ.id AND " +
                    "CRQ.state =:REQUEST_STATE AND " +
                    "CRH.result LIKE :RESULT AND " +
                    "CRH.systemID = :SYSTEM_ID " +
                    "GROUP BY CRH.requestID " +
                    "ORDER BY CRH.requestID ASC", Long.class)
                    .setParameter("REQUEST_STATE", CardRequestState.SENT_TO_AFIS)
                    .setParameter("RESULT", "%" + CardRequestState.SENT_TO_AFIS.name() + "%")
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .getResultList();

            if (EmsUtil.checkListSize(countList)) {
                return countList.size();
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_021, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public String findSentToAFISMessageRequestId(Long requestId) throws BaseException {
        try {
            List<String> msgReqIds = em.createQuery("" +
                    "SELECT CRH.requestID " +
                    "FROM CardRequestHistoryTO CRH " +
                    "WHERE CRH.cardRequest.id = :REQUEST_ID AND " +
                    "CRH.result LIKE :RESULT AND " +
                    "CRH.systemID = :SYSTEM_ID " +
                    "ORDER BY CRH.requestID DESC", String.class)
                    .setParameter("REQUEST_ID", requestId)
                    .setParameter("RESULT", "%" + CardRequestState.SENT_TO_AFIS.name() + "%")
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .getResultList();
            if (EmsUtil.checkListSize(msgReqIds)) {
                return msgReqIds.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRH_022, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    
	@Override
	public Boolean existInHistoryByCmsReqId(String cmsReqId, Long requestId) {
		Long count = null;
		try{
			count = em
					.createQuery(
							"select count(crh.id) from CardRequestHistoryTO crh where crh.requestID = :cmsReqId and crh.cardRequest.id = :requestId and crh.systemID = :systemID and crh.cardRequestHistoryAction = :action",
							Long.class).setParameter("cmsReqId", cmsReqId)
					.setParameter("systemID", SystemId.CMS)
					.setParameter("requestId", requestId).setParameter("action", CardRequestHistoryAction.PENDING_ISSUANCE).getSingleResult();
		}catch(NoResultException e){
			logger.error("ERROR_00000 : There is no CardRequestTO for cmsReqId = " + cmsReqId);
		}
		return (count == null || count.longValue() == 0) ? false : true;
	}

	@Override
	public CardRequestHistoryTO findHistoryByRequestIdAndAction(Long reqId,
			CardRequestHistoryAction action) throws BaseException {

		try {
			List<CardRequestHistoryTO> resultList = em
					.createQuery(
							"SELECT CRH FROM CardRequestHistoryTO CRH "
									+ "WHERE CRH.cardRequest.id = :cardRequestId " +
									"AND CRH.cardRequestHistoryAction in (:action) " +
									"AND CRH.actor is not null  order by crh.date desc ",
							CardRequestHistoryTO.class)
					.setParameter("cardRequestId", reqId)
					.setParameter("action", action).getResultList();
			if (EmsUtil.checkListSize(resultList))
				return resultList.get(0);
			else
				return null;

		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CRH_011,
					DataExceptionCode.GLB_005_MSG, e);
		}

	}
/**
 * this method is used to fetch all nearly history base on given cardRequestId
 * @author ganjyar
 */
	@Override
	public List<CardRequestHistoryTO> fetchAllHistoryByRequestId(Long requestId)
			throws BaseException {

		try {
			List<CardRequestHistoryTO> resultList = em
					.createQuery(
							"SELECT CRH FROM CardRequestHistoryTO CRH "
									+ "WHERE CRH.cardRequest.id = :cardRequestId order by crh.date desc ",
							CardRequestHistoryTO.class)
					.setParameter("cardRequestId", requestId).getResultList();
			if (EmsUtil.checkListSize(resultList))
				return resultList;
			else
				return null;

		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CRH_011,
					DataExceptionCode.GLB_005_MSG, e);
		}

	}
}
