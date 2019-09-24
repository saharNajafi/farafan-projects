package com.gam.nocr.ems.data.dao.impl;

import com.gam.nocr.ems.data.domain.*;
import gampooya.tools.date.DateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.data.enums.Estelam2FlagType;
import com.gam.nocr.ems.data.enums.SMSTypeState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "CardRequestDAO")
@Local(CardRequestDAOLocal.class)
@Remote(CardRequestDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CardRequestDAOImpl extends EmsBaseDAOImpl<CardRequestTO> implements
        CardRequestDAOLocal, CardRequestDAORemote {

    private static final Logger logger = BaseLog
            .getLogger(CardRequestDAOImpl.class);

    private static final String FOREIGN_KEY_CARD_REQ_CARD_ID = "FK_CARD_REQ_CARD_ID";
    private static final String FOREIGN_KEY_CARD_REQ_CITIZEN_ID = "FK_CARD_REQ_CITIZEN_ID";
    private static final String FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID = "FK_CARD_REQ_ENROLL_OFC_ID";
    private static final String FOREIGN_KEY_CRD_REQ_REQUEST_ID = "FK_CRD_REQ_REQUEST_ID";
    private static final String FOREIGN_KEY_RESERV_REQ_ID = "FK_RESERV_REQ_ID";
    private static final String INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS = "CKC_CRQ_STATE_CHANGE";
    private static final String INTEGRITY_CONSTRAINT_CRQ_REQUESTED_ACTION = "CKC_CRQ_REQUESTED_ACTION";

    private static final String DEFAULT_NUMBER_OF_REQUEST_TO_LOAD = "100";
    private static final String DEFAULT_SMS_BODY = "100";
    private static final String DEFAULT_PURGE_BIO_TIME_INTERVAL = "100";
    private static final String DEFAULT_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_UP = "48";
    private static final String DEFAULT_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_DOWN = "48";

    private static final String DEFAULT_INTERVAL_PURGE_UP = "1394/02/01";
    private static final String DEFAULT_INTERVAL_PURGE_DOWN = "1394/01/01";

    /**
     * The method extractSpecifiedTime is used to extract the specified number
     * which represents the time interval, in spite of the input parameter of
     * type {@link String}
     *
     * @param timeIntervalFromConfig an instance of type {@link String} which carries a value that
     *                               represents day, hour, or minute
     * @param defaultInterval        an instance of type {@link Integer} which represents the
     *                               default value for interval
     * @return an instance of type {@link Timestamp}
     * @throws BaseException
     */
    private Timestamp extractSpecifiedTime(String timeIntervalFromConfig,
                                           Integer defaultInterval) throws BaseException {
        Date now = new Date();
        Timestamp timeIntervalForQuery;
        if (timeIntervalFromConfig == null) {
            timeIntervalForQuery = new Timestamp(now.getTime()
                    - (defaultInterval * 60 * 60 * 1000));
        } else {
            if (timeIntervalFromConfig.toLowerCase().endsWith("d")) {
                Integer dayTimeInterval;
                try {
                    dayTimeInterval = Integer.parseInt(timeIntervalFromConfig
                            .split("d")[0]);
                } catch (Exception e) {
                    throw new DAOException(DataExceptionCode.CDI_036,
                            DataExceptionCode.CDI_036_MSG, e);
                }
                if (dayTimeInterval < 1) {
                    throw new DAOException(DataExceptionCode.CDI_039,
                            DataExceptionCode.CDI_036_MSG);
                }
                timeIntervalForQuery = new Timestamp(now.getTime()
                        - (dayTimeInterval * 24 * 60 * 60 * 1000));

            } else if (timeIntervalFromConfig.toLowerCase().endsWith("m")) {
                Integer minuteTimeInterval;
                try {
                    minuteTimeInterval = Integer
                            .parseInt(timeIntervalFromConfig.split("m")[0]);
                } catch (Exception e) {
                    throw new DAOException(DataExceptionCode.CDI_037,
                            DataExceptionCode.CDI_036_MSG, e);
                }
                if (minuteTimeInterval < 1 || minuteTimeInterval > 59) {
                    throw new DAOException(DataExceptionCode.CDI_040,
                            DataExceptionCode.CDI_036_MSG);
                }
                timeIntervalForQuery = new Timestamp(now.getTime()
                        - (minuteTimeInterval * 60 * 1000));

            } else {
                Integer hourTimeInterval;
                try {
                    hourTimeInterval = Integer.parseInt(timeIntervalFromConfig
                            .split("h")[0]);
                } catch (Exception e) {
                    throw new DAOException(DataExceptionCode.CDI_038,
                            DataExceptionCode.CDI_036_MSG, e);
                }
                if (hourTimeInterval < 1 || hourTimeInterval > 23) {
                    throw new DAOException(DataExceptionCode.CDI_040,
                            DataExceptionCode.CDI_036_MSG);
                }
                timeIntervalForQuery = new Timestamp(now.getTime()
                        - (hourTimeInterval * 60 * 60 * 1000));
            }
        }
        return timeIntervalForQuery;
    }

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public CardRequestTO create(CardRequestTO cardRequestTO)
            throws BaseException {
        try {
            CardRequestTO to = super.create(cardRequestTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_CARD_ID)) {
                throw new DAOException(DataExceptionCode.CDI_002,
                        DataExceptionCode.CDI_002_MSG, e,
                        new Long[]{cardRequestTO.getCard().getId()});
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_CITIZEN_ID)) {
                throw new DAOException(DataExceptionCode.CDI_003,
                        DataExceptionCode.CDI_003_MSG, e,
                        new Long[]{cardRequestTO.getCitizen().getId()});
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID)) {
                throw new DAOException(DataExceptionCode.CDI_004,
                        DataExceptionCode.CDI_004_MSG, e,
                        new Long[]{cardRequestTO.getEnrollmentOffice()
                                .getId()});
            }
            throw new DAOException(DataExceptionCode.CDI_001,
                    DataExceptionCode.CDI_001_MSG, e);
        }
    }

    @Override
    public CardRequestTO update(CardRequestTO cardRequestTO)
            throws BaseException {
        try {
            CardRequestTO to = super.update(cardRequestTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_CARD_ID)) {
                throw new DAOException(DataExceptionCode.CDI_022,
                        DataExceptionCode.CDI_022_MSG, e,
                        new Long[]{cardRequestTO.getCard().getId()});
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_CITIZEN_ID)) {
                throw new DAOException(DataExceptionCode.CDI_023,
                        DataExceptionCode.CDI_023_MSG, e,
                        new Long[]{cardRequestTO.getCitizen().getId()});
            }
            if (err.contains(FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID)) {
                throw new DAOException(DataExceptionCode.CDI_024,
                        DataExceptionCode.CDI_024_MSG, e,
                        new Long[]{cardRequestTO.getEnrollmentOffice()
                                .getId()});
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS)) {
                throw new DAOException(DataExceptionCode.CDI_042,
                        DataExceptionCode.CDI_042_MSG, e);
            }
            throw new DAOException(DataExceptionCode.CDI_025,
                    DataExceptionCode.CDI_025_MSG, e);
        }
    }

    @Override
    public boolean deleteByID(long requestId) throws BaseException {
        int result;
        try {
            result = em
                    .createQuery(
                            "delete from CardRequestTO cr where cr.id =:requestId")
                    .setParameter("requestId", requestId).executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_CRD_REQ_REQUEST_ID))
                throw new DataException(DataExceptionCode.CDI_009,
                        DataExceptionCode.CDI_009_MSG, e,
                        new Long[]{requestId});
            if (err.contains(FOREIGN_KEY_RESERV_REQ_ID))
                throw new DataException(DataExceptionCode.CDI_016,
                        DataExceptionCode.CDI_016_MSG, e,
                        new Long[]{requestId});
            throw new DataException(DataExceptionCode.CDI_010,
                    DataExceptionCode.CDI_010_MSG, e, new Long[]{requestId});
        }
        return result == 1;
    }

    /**
     * The method findByCardRequestState is used to find a list of CardRequestTO
     * in spite of their state field.
     *
     * @param cardRequestState is an enumeration of type CardRequestState
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     */
    @Override
    public List<CardRequestTO> findByCardRequestState(
            CardRequestState cardRequestState) throws BaseException {
        // List<CardRequestTO> cardRequestList =
        // em.createQuery("SELECT CRT FROM CardRequestTO CRT" +
        // " WHERE CRT.state =:CARD_REQUEST_STATE")
        // .setParameter("CARD_REQUEST_STATE", cardRequestState)
        // .getResultList();
        List<CardRequestTO> cardRequestList = null;
        try {
            cardRequestList = em
                    .createQuery(
                            "SELECT CRT "
                                    + " FROM CardRequestTO CRT, CitizenTO CTZ"
                                    + " WHERE CRT.citizen.id = CTZ.id "
                                    + " AND CRT.state =:CARD_REQUEST_STATE "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_008,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        // TODO : Check the 'Lazy loading' on future
        /*
         * for(CardRequestTO cardRequestTO : cardRequestList){
         * cardRequestTO.getCitizen().getId();
         * cardRequestTO.getCitizen().getCitizenInfo();
         * cardRequestTO.getCitizen().getCitizenInfo().getSpouses();
         * cardRequestTO.getCitizen().getCitizenInfo().getChildren(); }
         */
        return cardRequestList;
    }

    @Override
    public List<CardRequestTO> findByCardRequestState(
            CardRequestState cardRequestState, Integer from, Integer to)
            throws BaseException {
        List<CardRequestTO> cardRequestList;
        try {
            cardRequestList = em
                    .createQuery(
                            "SELECT CRT " + " FROM CardRequestTO CRT "
                                    + "WHERE CRT.state = :CARD_REQUEST_STATE",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_008,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return cardRequestList;
    }


    @Override
    public List<CardRequestTO> findByCardRequestStateByPriority(
            CardRequestState cardRequestState, Integer from, Integer to)
            throws BaseException {
        List<CardRequestTO> cardRequestList;
        try {
            cardRequestList = em
                    .createQuery(
                            "SELECT CRT " + " FROM CardRequestTO CRT "
                                    + "WHERE CRT.state = :CARD_REQUEST_STATE order by CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_008,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return cardRequestList;
    }

    /**
     * The method findLimitedByCardRequestState is used to find a limited list
     * of CardRequestTO in spite of their state field.
     *
     * @param cardRequestState  is an enumeration of type CardRequestState
     * @param countProfileKey   is an instance of type {@link String} which represents the
     *                          profile key for fetching the count by using profileManager
     * @param defaultCountValue is an instance of type {@link Integer} which represents the
     *                          default count for the query
     * @return a list of type {@link CardRequestTO}
     */
    @Override
    public List<CardRequestTO> findLimitedByCardRequestState(
            CardRequestState cardRequestState, String countProfileKey,
            Integer defaultCountValue) throws BaseException {
        // Getting the count of query from Config
        Integer cardRequestsCount = null;
        int DEFAULT_VALUE_FOR_COUNT = 1;
        int DEFAULT_TIME_INTERVAL = 1;
        SystemId systemId = null;
        String timeIntervalKey = null;

        /**
         * EXAMPLES which represents the time interval from config: For day:3d
         * For hour:3h or 3 For minute:3m
         */
        String timeIntervalFromConfig = null;

        if (countProfileKey != null && !countProfileKey.isEmpty()) {
            if (ProfileKeyName.KEY_CARD_REQUESTS_COUNT_FOR_SENDING_TO_CMS
                    .equals(countProfileKey)) {
                systemId = SystemId.CMS;
                timeIntervalKey = ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_CMS;

            } else if (ProfileKeyName.KEY_CARD_REQUESTS_COUNT_FOR_SENDING_TO_IMS
                    .equals(countProfileKey)) {
                systemId = SystemId.IMS;
                timeIntervalKey = ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS;
            } else {
                throw new DAOException(DataExceptionCode.CDI_028,
                        DataExceptionCode.CDI_028_MSG,
                        new String[]{countProfileKey});
            }

            ProfileManager pm = null;
            try {
                pm = ProfileHelper.getProfileManager();
            } catch (Exception e) {
                throw new DAOException(DataExceptionCode.CDI_029,
                        DataExceptionCode.CDI_029_MSG, e);
            }

            try {
                if (systemId.equals(SystemId.IMS)) {
                    cardRequestsCount = defaultCountValue;
                } else {
                    cardRequestsCount = Integer.parseInt((String) pm
                            .getProfile(countProfileKey, true, null, null));
                }
            } catch (Exception e) {
                logger.warn(DataExceptionCode.CDI_026,
                        DataExceptionCode.CDI_026_MSG,
                        new String[]{systemId.name()});
            }

            try {
                timeIntervalFromConfig = (String) pm.getProfile(
                        timeIntervalKey, true, null, null);
            } catch (Exception e) {
                logger.warn(DataExceptionCode.CDI_030,
                        DataExceptionCode.CDI_030_MSG,
                        new String[]{systemId.name()});
            }

        } else {
            if (defaultCountValue != null) {
                cardRequestsCount = defaultCountValue;
            }
        }

        if (cardRequestsCount == null) {
            cardRequestsCount = DEFAULT_VALUE_FOR_COUNT;
        }

        Timestamp specifiedTime = extractSpecifiedTime(timeIntervalFromConfig,
                DEFAULT_TIME_INTERVAL);
        List<CardRequestTO> cardRequestList;
        try {
            /**
             * Query details : If the systemId = CMS : 1. fetches cardRequests
             * which has no records in history table or in spite of the
             * timeInterval(older than (timeIntervalForQuery(which can be day,
             * hour, or minute))) and systemId(CMS), fetches the records which
             * saved with an specified exception.
             *
             * If the systemId = IMS : 1. fetches cardRequests which has no
             * records in history table or in spite of the timeInterval(older
             * than (timeIntervalForQuery(which can be day, hour, or minute)))
             * and systemId(IMS), fetches the records which saved with an
             * specified exception.
             */
            cardRequestList = em
                    .createQuery(
                            "SELECT CRT "
                                    + "FROM CardRequestTO CRT, CitizenTO CTZ "
                                    + "WHERE "
                                    + "CRT.citizen.id = CTZ.id AND "
                                    + "CRT.state =:CARD_REQUEST_STATE AND "
                                    + "("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE CRH.systemID = :SYSTEM_ID "
                                    + "AND CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))"
                                    + ") "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setParameter("SYSTEM_ID", systemId)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .setMaxResults(cardRequestsCount).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_027,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return cardRequestList;
    }

    @Override
    public CardRequestTO findByCmsRequestId(String cmsRequestId)
            throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            cardRequestTOList = em
                    .createQuery(
                            "SELECT CRT from CardRequestTO CRT "
                                    + "WHERE CRT.id = (SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH "
                                    + "WHERE CRH.requestID = (:cmsRequestId) AND CRH.systemID = :SYSTEM_ID and CRH.cardRequestHistoryAction = :action) "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("cmsRequestId", cmsRequestId)
                    .setParameter("SYSTEM_ID", SystemId.CMS)
                    .setParameter("action",
                            CardRequestHistoryAction.PENDING_ISSUANCE)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_019,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestTOList))
            return cardRequestTOList.get(0);
        else
            return null;
    }

    @Override
    public Long getCcosAndVerifiedMESRequestsCount() throws BaseException {

        List<CardRequestState> acceptableStatesForMESRequests = new ArrayList<CardRequestState>();
        acceptableStatesForMESRequests.add(CardRequestState.APPROVED);
        acceptableStatesForMESRequests.add(CardRequestState.SENT_TO_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.REVOKED_BY_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.APPROVED_BY_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.PENDING_ISSUANCE);
        acceptableStatesForMESRequests.add(CardRequestState.ISSUED);
        acceptableStatesForMESRequests.add(CardRequestState.READY_TO_DELIVER);
        acceptableStatesForMESRequests
                .add(CardRequestState.PENDING_TO_DELIVER_BY_CMS);
        acceptableStatesForMESRequests.add(CardRequestState.DELIVERED);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
        acceptableStatesForMESRequests.add(CardRequestState.NOT_DELIVERED);
        acceptableStatesForMESRequests.add(CardRequestState.STOPPED);
        acceptableStatesForMESRequests.add(CardRequestState.REPEALED);
        acceptableStatesForMESRequests.add(CardRequestState.CMS_ERROR);
        acceptableStatesForMESRequests
                .add(CardRequestState.CMS_PRODUCTION_ERROR);
        acceptableStatesForMESRequests.add(CardRequestState.IMS_ERROR);
        try {
            List<Long> count = em
                    .createQuery(
                            " "
                                    + "SELECT COUNT(CRQ.id) "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.portalRequestId IS NULL AND "
                                    + "("
                                    + "CRQ.origin = :CCOS_ORIGIN OR "
                                    + "CRQ.origin = :VIP_ORIGIN OR "
                                    + "(CRQ.origin = :MES_ORIGIN AND CRQ.state IN (:ACCEPTABLE_STATES_FOR_MES_REQUESTS))"
                                    + ") ", Long.class)
                    .setParameter("CCOS_ORIGIN", CardRequestOrigin.C)
                    .setParameter("VIP_ORIGIN", CardRequestOrigin.V)
                    .setParameter("MES_ORIGIN", CardRequestOrigin.M)
                    .setParameter("ACCEPTABLE_STATES_FOR_MES_REQUESTS",
                            acceptableStatesForMESRequests).getResultList();
            if (EmsUtil.checkListSize(count)) {
                return count.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_075,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<CardRequestTO> findCcosAndVerifiedMESRequests(int from, int to)
            throws BaseException {
        // String maxResult =
        // EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_REQUEST_TO_LOAD,
        // DEFAULT_NUMBER_OF_REQUEST_TO_LOAD);
        //
        // if (maxResult == null)
        // maxResult = DEFAULT_NUMBER_OF_REQUEST_TO_LOAD;

        List<CardRequestTO> cardRequestTOList;
        List<CardRequestState> acceptableStatesForMESRequests = new ArrayList<CardRequestState>();
        acceptableStatesForMESRequests.add(CardRequestState.APPROVED);
        acceptableStatesForMESRequests.add(CardRequestState.SENT_TO_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.REVOKED_BY_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.APPROVED_BY_AFIS);
        acceptableStatesForMESRequests.add(CardRequestState.PENDING_ISSUANCE);
        acceptableStatesForMESRequests.add(CardRequestState.ISSUED);
        acceptableStatesForMESRequests.add(CardRequestState.READY_TO_DELIVER);
        acceptableStatesForMESRequests
                .add(CardRequestState.PENDING_TO_DELIVER_BY_CMS);
        acceptableStatesForMESRequests.add(CardRequestState.DELIVERED);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE);
        acceptableStatesForMESRequests
                .add(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
        acceptableStatesForMESRequests.add(CardRequestState.NOT_DELIVERED);
        acceptableStatesForMESRequests.add(CardRequestState.NOT_DELIVERED);
        acceptableStatesForMESRequests.add(CardRequestState.STOPPED);
        acceptableStatesForMESRequests.add(CardRequestState.REPEALED);
        acceptableStatesForMESRequests.add(CardRequestState.CMS_ERROR);
        acceptableStatesForMESRequests
                .add(CardRequestState.CMS_PRODUCTION_ERROR);
        acceptableStatesForMESRequests.add(CardRequestState.IMS_ERROR);
        try {
            cardRequestTOList = em
                    .createQuery(
                            ""
                                    + "SELECT CRQ FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.portalRequestId IS NULL AND "
                                    + "("
                                    + "CRQ.origin = :CCOS_ORIGIN OR "
                                    + "CRQ.origin = :VIP_ORIGIN OR "
                                    + "(CRQ.origin = :MES_ORIGIN AND CRQ.state IN (:ACCEPTABLE_STATES_FOR_MES_REQUESTS))"
                                    + ") ", CardRequestTO.class)
                    .setParameter("CCOS_ORIGIN", CardRequestOrigin.C)
                    .setParameter("MES_ORIGIN", CardRequestOrigin.M)
                    .setParameter("VIP_ORIGIN", CardRequestOrigin.V)
                    .setParameter("ACCEPTABLE_STATES_FOR_MES_REQUESTS",
                            acceptableStatesForMESRequests)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_031,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestTOList))
            return cardRequestTOList;
        else
            return null;
    }

    @Override
    public boolean existsCardRequest(Long requestId) throws BaseException {
        try {
            Number count = (Number) em
                    .createQuery(
                            "select count(crq) " + "from CardRequestTO crq "
                                    + "where crq.id = :requestId")
                    .setParameter("requestId", requestId).getSingleResult();
            return (count.intValue() != 0);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_015,
                    DataExceptionCode.CDI_015_MSG, e, new Long[]{requestId});
        }
    }

    @Override
    public List<CardRequestTO> findByCitizen(CitizenTO ctz)
            throws BaseException {
        try {
            return em
                    .createQuery(
                            "select crq " + "from CardRequestTO crq "
                                    + "where crq.citizen.id = :citizenId "
                                    + "order by crq.id desc ",
                            CardRequestTO.class)
                    .setParameter("citizenId", ctz.getId()).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_014,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public void updateRequest(String cmsRequestId, CardTO cardTO)
            throws BaseException {
        try {

            //By Adldoost
            notifyCardRequestIssued(cmsRequestId);
            //////////
            em.createQuery(
                    " update CardRequestTO crs "
                            + " set crs.card.id = :cardId, crs.state = :cardRequestState"
                            + " where crs.id = (select crh.cardRequest.id from CardRequestHistoryTO crh"
                            + " where crh.requestID = :cmsId and crh.result = :cardRequestHistoryState)")
                    .setParameter("cardId", cardTO.getId())
                    .setParameter("cardRequestState", CardRequestState.ISSUED)
                    .setParameter("cardRequestHistoryState",
                            CardRequestState.PENDING_ISSUANCE.name())
                    .setParameter("cmsId", cmsRequestId).executeUpdate();
            em.flush();

        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_045,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DAOException(DataExceptionCode.CDI_005,
                    DataExceptionCode.GLB_006_MSG, e);
        }

    }

    //By Adldoost
    //should be run where card request state changes from "not issued" to "issued"
    //implementation of trigger : BI_Card_Req
    private void notifyCardRequestIssued(String cmsRequestId) throws BaseException {
        logger.info("BI_Card_Req : notify CardRequest Issued for cmsId : '{}'", cmsRequestId);
        try {
            CardRequestTO selectedRequest = findByCmsRequestId(cmsRequestId);
            //			List<CardRequestTO> request = em.createQuery(
            //					" select crs from CardRequestTO crs "
            //							+ " where crs.id = (select crh.cardRequest.id from CardRequestHistoryTO crh"
            //							+ " where crh.requestID = :cmsId)", CardRequestTO.class)
            //					.setParameter("cmsId", cmsRequestId).getResultList();
            if (selectedRequest == null) {
                throw new DAOException(DataExceptionCode.DSI_070, DataExceptionCode.DSI_070_MSG);
            }
            //			CardRequestTO selectedRequest = request.get(0);
            if (selectedRequest.getEnrollmentOffice().getType() == EnrollmentOfficeType.OFFICE &&
                    selectedRequest.getEnrollmentOffice().getDeliver() == EnrollmentOfficeDeliverStatus.DISABLED) {
                selectedRequest.setDeliveredOfficeId(selectedRequest.getEnrollmentOffice().getSuperiorOffice().getId());
            } else
                selectedRequest.setDeliveredOfficeId(selectedRequest.getEnrollmentOffice().getId());
            selectedRequest.setLastModifiedDate(new Date());
            create(selectedRequest);
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_071, exp);
        }
    }

    /**
     * The method updateRequestState is used to update the state of a specified
     * instance of type {@link com.gam.nocr.ems.data.domain.CardRequestTO} from
     * one state to another one
     *
     * @param cardRequestTO carries the required attributes which are needed to update
     * @param from          represents the primitive state that is wanted to be changed
     * @param to            represents the intended state
     */


//*********************************** Anbari: Commented for inserting CardRequest Lobs
    @Override
    public void updateRequestState(CardRequestTO cardRequestTO,
                                   CardRequestState from, CardRequestState to) throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crs "
                            + "SET crs.receiptText = :RECEIPT, "
                            + "crs.signedReceipt = :SIGNED_RECEIPT, "
                            + "crs.state = :CARD_REQUEST_NEW_STATE"
                            + " WHERE crs.id = :CARD_REQUEST_ID AND "
                            + "crs.state = :CARD_REQUEST_OLD_STATE")
                    .setParameter("RECEIPT", cardRequestTO.getReceiptText())
                    .setParameter("SIGNED_RECEIPT",
                            cardRequestTO.getSignedReceipt())
                    .setParameter("CARD_REQUEST_NEW_STATE", to)
                    .setParameter("CARD_REQUEST_ID", cardRequestTO.getId())
                    .setParameter("CARD_REQUEST_OLD_STATE", from)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_047,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DAOException(DataExceptionCode.CDI_011,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    //****************************** Anbari : new Update CardRequest without inserting CardResquest Lobs

//	@Override
//	public void updateRequestState(CardRequestTO cardRequestTO,
//			CardRequestState from, CardRequestState to) throws BaseException {
//		try {
//			em.createQuery(
//					"UPDATE CardRequestTO crs "
//							+ "SET crs.state = :CARD_REQUEST_NEW_STATE"
//							+ " WHERE crs.id = :CARD_REQUEST_ID AND "
//							+ "crs.state = :CARD_REQUEST_OLD_STATE")
//									.setParameter("CARD_REQUEST_NEW_STATE", to)
//									.setParameter("CARD_REQUEST_ID", cardRequestTO.getId())
//									.setParameter("CARD_REQUEST_OLD_STATE", from)
//									.executeUpdate();
//			em.flush();
//		} catch (Exception e) {
//			String err = e.getMessage();
//			if (e.getCause() != null) {
//				if (e.getCause().getCause() != null)
//					err = e.getCause().getCause().getMessage();
//				else
//					err = e.getCause().getMessage();
//			}
//			if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
//				throw new DAOException(DataExceptionCode.CDI_047,
//						DataExceptionCode.CDI_042_MSG, e);
//
//			throw new DAOException(DataExceptionCode.CDI_011,
//					DataExceptionCode.GLB_006_MSG, e);
//		}
//	}

    //*********************************************

    /**
     * The method updateCardRequestsState is use to update the state of the
     * requests in spite of their ids
     *
     * @param cardRequestIdList a list of type Long which represents the ids for the specified
     *                          requests
     * @param cardRequestState  an enum value of type
     *                          {@link com.gam.nocr.ems.data.enums.CardRequestState}
     */
    @Override
    public void updateCardRequestsState(List<Long> cardRequestIdList,
                                        CardRequestState cardRequestState) throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crs "
                            + "SET crs.state = :CARD_REQUEST_STATE "
                            + "WHERE crs.id IN (:ID_LIST)")
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setParameter("ID_LIST", cardRequestIdList).executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_044,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_012,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public int updateCardRequestOfficeId(Long cardRequestId,
                                         Long enrollmentOfficeId) throws BaseException {
        try {
            // This query only updates enrollment office of that card request
            // which has not been transferred to the superior office. Enrollment
            // office of the transferred request should be remained unchanged.
            int affectedRecords = em
                    .createQuery(
                            "UPDATE CardRequestTO crs "
                                    + "SET crs.enrollmentOffice.id = :enrollmentOfficeId "
                                    + "WHERE crs.id = :cardRequestId "
                                    + "and crs.originalCardRequestOfficeId is null")
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
            return affectedRecords;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_062,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public CardRequestTO find(Class type, Object id) throws BaseException {
        try {
            return super.find(type, id);
        } catch (BaseException e) {
            throw new DataException(DataExceptionCode.CDI_018,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method updateCardRequestByRequestIdOfHistory is used to update a list
     * of cardRequestTo in spite of a list of requestID which are existed in
     * {@CardRequestHistoryTO}
     *
     * @param requestIDs
     * @param cardRequestState
     */
    @Override
    public void updateCardRequestsByRequestIdOfHistory(List<String> requestIDs,
                                                       CardRequestState cardRequestState, SystemId systemId)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crs "
                            + "SET crs.state = :CARD_REQUEST_STATE "
                            + "WHERE crs.id IN (SELECT crh.cardRequest.id FROM CardRequestHistoryTO crh "
                            + "WHERE crh.requestID IN (:REQUEST_ID_LIST) AND crh.systemID = :SYSTEM_ID) ")
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setParameter("REQUEST_ID_LIST", requestIDs)
                    .setParameter("SYSTEM_ID", systemId).executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_043,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_013,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    /**
     * The method updateSyncDatesByCurrentDate is used to set lastSyncDate
     * attribute in spite of portalRequestIds in the list
     *
     * @param portalRequestIdList a list of type {@link Long} which represents a specified
     *                            number of portalRequestIds
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void updateSyncDatesByCurrentDate(List<Long> portalRequestIdList)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crq "
                            + "SET crq.lastSyncDate = :CURRENT_DATE "
                            + "WHERE crq.portalRequestId IN :PORTAL_CARD_REQUEST_ID_LIST")
                    .setParameter("PORTAL_CARD_REQUEST_ID_LIST",
                            portalRequestIdList)
                    .setParameter("CURRENT_DATE", new Date()).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_017,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updateSyncDatesAndFlag(List<Long> portalRequestIdList) throws BaseException {
        try {

            Query query = em.createNativeQuery(
                    "update emst_card_request set CRQ_LAST_SYNC_DATE = sysdate, CRQ_SYNC_FLAG = 0 where CRQ_PORTAL_REQUEST_ID in (:ids)");
            query.setParameter("ids", portalRequestIdList);
            query.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_017, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updateLockDatesByCurrentDate(List<Long> portalRequestIdList)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crq "
                            + "SET crq.lockDate = CURRENT_DATE "
                            + "WHERE crq.portalRequestId IN :PORTAL_CARD_REQUEST_ID_LIST")
                    .setParameter("PORTAL_CARD_REQUEST_ID_LIST",
                            portalRequestIdList).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_017,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updateLockDatesAndFlag(List<Long> portalRequestIdList) throws BaseException {
        try {

            Query query = em.createNativeQuery(
                    "update emst_card_request set CRQ_LOCK_DATE = sysdate, CRQ_SYNC_FLAG = LEAST(CRQ_SYNC_FLAG + 1, 9) where CRQ_PORTAL_REQUEST_ID in (:ids)");
            query.setParameter("ids", portalRequestIdList);
            query.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_017, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updateReEnrolledDateByCardRequestId(Long cardRequestId,
                                                    Date reEnrolledDate) throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crq "
                            + "SET crq.reEnrolledDate = :reEnrolledDate "
                            + "WHERE crq.id IN (:cardRequestId)")
                    .setParameter("reEnrolledDate", reEnrolledDate)
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_021,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updatePortalCardRequestId(Long requestId, Long portalRequestId)
            throws BaseException {
        try {
            em.createQuery(
                    "update CardRequestTO cr "
                            + "set cr.portalRequestId = :portalRequestId "
                            + "where cr.id = :requestId")
                    .setParameter("portalRequestId", portalRequestId)
                    .setParameter("requestId", requestId).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_035,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    /**
     * The method getReplicaCardRequestsCount is used to get the number of the
     * requests which are applied by a specified citizen
     *
     * @param nationalId
     * @return the count of requests or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public Long getReplicaCardRequestsCount(String nationalId)
            throws BaseException {
        List<Long> count;
        try {
            count = em
                    .createQuery(
                            "SELECT COUNT(CRQ) "
                                    + "FROM CardRequestTO CRQ, CitizenTO CTZ "
                                    + "WHERE CRQ.citizen.id = CTZ.id AND "
                                    + "CTZ.nationalID = :NATIONAL_ID AND "
                                    + "CRQ.type = :REPLICA_TYPE", Long.class)
                    .setParameter("NATIONAL_ID", nationalId)
                    .setParameter("REPLICA_TYPE", CardRequestType.REPLICA)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_018,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        if (count != null && !count.isEmpty()) {
            return count.get(0);
        } else {
            return null;
        }
    }

    @Override
    public CardRequestTO findByPortalRequestId(Long portalRequestId)
            throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            cardRequestTOList = em
                    .createQuery(
                            " select cr from CardRequestTO cr "
                                    + " where cr.portalRequestId = :portalRequestId order by cr.id asc",
                            CardRequestTO.class)
                    .setParameter("portalRequestId", portalRequestId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_020,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (!cardRequestTOList.isEmpty())
            return cardRequestTOList.get(0);
        else
            return null;
    }

    /**
     * The method findByRequestIds is used to fetch the instances of type
     * {@link com.gam.nocr.ems.data.domain.CardRequestTO}, in spite of input
     * parameter
     *
     * @param cardRequestIdList a list of type {@link Long} which represents a list of card
     *                          request ids
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<CardRequestTO> findByRequestIds(List<Long> cardRequestIdList)
            throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            cardRequestTOList = em
                    .createQuery(
                            "SELECT CRQ "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.id IN (:CARD_REQUEST_ID_LIST) ORDER BY CRQ.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_ID_LIST", cardRequestIdList)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_031,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return cardRequestTOList;
    }

    /**
     * The method updateStateAndMetaData is use to update the request in spite
     * of state and metadata
     *
     * @param id               an instance of type {@link Long}
     * @param cardRequestState an enum value of type {@link CardRequestState}
     * @param metadata         an instance of type {@link String}
     */
    @Override
    public void updateStateAndMetaData(Long id,
                                       CardRequestState cardRequestState, String metadata)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.state = :CARD_REQUEST_STATE, "
                            + "CRQ.metadata = :META_DATA "
                            + "WHERE CRQ.id = :CARD_REQUEST_ID")
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setParameter("META_DATA", metadata)
                    .setParameter("CARD_REQUEST_ID", id).executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_048,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_032,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    /**
     * The method updateRequestFromOldStateToNewState is used to update the
     * state of the records which have a specified status
     *
     * @param oldState an instance of type
     *                 {@link com.gam.nocr.ems.data.enums.CardRequestState} which
     *                 represents the old state
     * @param newState an instance of type
     *                 {@link com.gam.nocr.ems.data.enums.CardRequestState} which
     *                 represents the new state
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void updateRequestFromOldStateToNewState(CardRequestState oldState,
                                                    CardRequestState newState) throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.state = :NEW_CARD_REQUEST_STATE "
                            + "WHERE CRQ.state = :OLD_CARD_REQUEST_STATE")
                    .setParameter("NEW_CARD_REQUEST_STATE", newState)
                    .setParameter("OLD_CARD_REQUEST_STATE", oldState)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_046,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_033,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public CardRequestTO fetchCardRequest(Long cardRequestId)
            throws BaseException {
        List<CardRequestTO> cardRequestList;
        try {
            cardRequestList = em
                    .createQuery(
                            "select cr from CardRequestTO cr "
                                    + "where cr.id = :cardRequestId",
                            CardRequestTO.class)
                    .setParameter("cardRequestId", cardRequestId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_034,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestList))
            return cardRequestList.get(0);
        else
            return null;
    }

    @Override
    public void idleR2DCardRequest(String idlePeriod) throws BaseException {
        // try {
        // em.createQuery("UPDATE CardRequestTO CRQ " +
        // "SET CRQ.state = :NEW_CARD_REQUEST_STATE " +
        // "WHERE CRQ. = :OLD_CARD_REQUEST_STATE")
        // .setParameter("NEW_CARD_REQUEST_STATE",
        // CardRequestState.NOT_DELIVERED)
        // .executeUpdate();
        // em.flush();
        // } catch (Exception e) {
        // String err = e.getMessage();
        // if (e.getCause() != null) {
        // if (e.getCause().getCause() != null)
        // err = e.getCause().getCause().getMessage();
        // else
        // err = e.getCause().getMessage();
        // }
        // if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
        // throw new DAOException(DataExceptionCode.CDI_046,
        // DataExceptionCode.CDI_042_MSG, e);
        //
        // throw new DataException(DataExceptionCode.CDI_033,
        // DataExceptionCode.GLB_006_MSG, e);
        // }
    }

    /**
     * The method updateCardRequestsStateInSpiteOfOrigin is used to update the
     * state of the requests by using a list of request Ids and a map for
     * specifying the new states, in spite of their origins that used as key in
     * the map
     *
     * @param cardRequestIdList a list of type Long which represents the ids for the specified
     *                          requests
     * @param map               a map with key/value of type {@link Map<CardRequestOrigin,
     *                          CardRequestState>}, which carries the necessary states in
     *                          spite of the appropriate origins
     */
    @Override
    public void updateCardRequestsStateInSpiteOfOrigin(
            List<Long> cardRequestIdList,
            Map<CardRequestOrigin, CardRequestState> map) throws BaseException {

        try {
            if (cardRequestIdList != null && map != null) {
                Object[] origins = map.keySet().toArray();
                if (origins != null && origins.length != 0) {
                    for (Object origin : origins) {
                        if (CardRequestOrigin.M.equals(origin)) {
                            em.createQuery(
                                    "UPDATE CardRequestTO CRQ "
                                            + "SET CRQ.state = :CARD_REQUEST_STATE "
                                            + "WHERE CRQ.id IN (:ID_LIST) AND CRQ.origin = :CARD_REQUEST_ORIGIN")
                                    .setParameter("CARD_REQUEST_ORIGIN", origin)
                                    .setParameter("CARD_REQUEST_STATE",
                                            map.get(origin))
                                    .setParameter("ID_LIST", cardRequestIdList)
                                    .executeUpdate();
                            em.flush();
                        }

                        if (!CardRequestOrigin.M.equals(origin)) {
                            em.createQuery(
                                    "UPDATE CardRequestTO CRQ "
                                            + "SET CRQ.state = :CARD_REQUEST_STATE "
                                            + "WHERE CRQ.id IN (:ID_LIST) AND CRQ.origin != :CARD_REQUEST_ORIGIN")
                                    .setParameter("CARD_REQUEST_ORIGIN",
                                            CardRequestOrigin.M)
                                    .setParameter("CARD_REQUEST_STATE",
                                            map.get(origin))
                                    .setParameter("ID_LIST", cardRequestIdList)
                                    .executeUpdate();
                            em.flush();
                        }
                    }
                }
            }

        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_049,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_050,
                    DataExceptionCode.GLB_006_MSG, e);

        }
    }

    /**
     * The method updateCardRequestsStateRegardsToOrigin is used to update the
     * state of the requests by using a list of type
     * {@link com.gam.nocr.ems.data.domain.CardRequestTO} and a map for
     * specifying the new states, regards to their origins that used as a key in
     * the map
     *
     * @param cardRequestTOList a list of type
     *                          {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param map               a map with key/value of type {@link java.util.Map <
     *                          com.gam.nocr.ems.data.enums.CardRequestOrigin ,
     *                          com.gam.nocr.ems.data.enums.CardRequestState >}, which carries
     *                          the necessary states, regarding the appropriate origins
     */
    @Override
    public void updateCardRequestsStateRegardsToOrigin(
            List<CardRequestTO> cardRequestTOList,
            Map<CardRequestOrigin, CardRequestState> map) throws BaseException {

        try {
            if (EmsUtil.checkListSize(cardRequestTOList)) {
                for (CardRequestTO cardRequestTO : cardRequestTOList) {
                    Object[] origins = map.keySet().toArray();
                    if (origins != null && origins.length != 0) {
                        for (Object origin : origins) {
                            if (CardRequestOrigin.M.equals(origin)) {
                                em.createQuery(
                                        ""
                                                + "UPDATE CardRequestTO CRQ "
                                                + "SET CRQ.state = :CARD_REQUEST_STATE, CRQ.metadata = :CARD_REQUEST_METADATA "
                                                + "WHERE CRQ.id = (:CARD_REQUEST_ID) AND CRQ.origin = :CARD_REQUEST_ORIGIN")
                                        .setParameter("CARD_REQUEST_STATE",
                                                map.get(origin))
                                        .setParameter("CARD_REQUEST_METADATA",
                                                cardRequestTO.getMetadata())
                                        .setParameter("CARD_REQUEST_ORIGIN",
                                                origin)
                                        .setParameter("CARD_REQUEST_ID",
                                                cardRequestTO.getId())
                                        .executeUpdate();
                                em.flush();
                            }

                            if (!CardRequestOrigin.M.equals(origin)) {
                                em.createQuery(
                                        ""
                                                + "UPDATE CardRequestTO CRQ "
                                                + "SET CRQ.state = :CARD_REQUEST_STATE, CRQ.metadata = :CARD_REQUEST_METADATA "
                                                + "WHERE CRQ.id = (:CARD_REQUEST_ID) AND CRQ.origin != :CARD_REQUEST_ORIGIN")
                                        .setParameter("CARD_REQUEST_STATE",
                                                map.get(origin))
                                        .setParameter("CARD_REQUEST_METADATA",
                                                cardRequestTO.getMetadata())
                                        .setParameter("CARD_REQUEST_ORIGIN",
                                                CardRequestOrigin.M)
                                        .setParameter("CARD_REQUEST_ID",
                                                cardRequestTO.getId())
                                        .executeUpdate();
                                em.flush();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_056,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_057,
                    DataExceptionCode.GLB_006_MSG, e);

        }

    }

    /**
     * The method updateCardRequestsStateInSpiteOfOrigin is used to update the
     * state of the requests by using a map for specifying the new states, in
     * spite of their origins that used as key in the map
     *
     * @param map      a map with key/value of type {@link java.util.Map <
     *                 com.gam.nocr.ems.data.enums.CardRequestOrigin ,
     *                 CardRequestState>}, which carries the necessary states in
     *                 spite of the appropriate origins
     * @param oldState is an instance of type
     *                 {@link com.gam.nocr.ems.data.enums.CardRequestState}
     */
    @Override
    public void updateCardRequestsStateInSpiteOfOriginAndOldState(
            Map<CardRequestOrigin, CardRequestState> map,
            CardRequestState oldState) throws BaseException {
        try {
            Object[] origins = map.keySet().toArray();
            if (origins != null && origins.length != 0) {
                for (Object origin : origins) {
                    if (CardRequestOrigin.M.equals(origin)) {
                        em.createQuery(
                                "UPDATE CardRequestTO CRQ "
                                        + "SET CRQ.state = :NEW_CARD_REQUEST_STATE "
                                        + "WHERE CRQ.state = :OLD_CARD_REQUEST_STATE AND CRQ.origin = :CARD_REQUEST_ORIGIN")
                                .setParameter("NEW_CARD_REQUEST_STATE",
                                        map.get(origin))
                                .setParameter("OLD_CARD_REQUEST_STATE",
                                        oldState)
                                .setParameter("CARD_REQUEST_ORIGIN", origin)
                                .executeUpdate();
                        em.flush();
                    }

                    if (!CardRequestOrigin.M.equals(origin)) {
                        em.createQuery(
                                "UPDATE CardRequestTO CRQ "
                                        + "SET CRQ.state = :NEW_CARD_REQUEST_STATE "
                                        + "WHERE CRQ.state = :OLD_CARD_REQUEST_STATE AND CRQ.origin != :CARD_REQUEST_ORIGIN")
                                .setParameter("NEW_CARD_REQUEST_STATE",
                                        map.get(origin))
                                .setParameter("OLD_CARD_REQUEST_STATE",
                                        oldState)
                                .setParameter("CARD_REQUEST_ORIGIN",
                                        CardRequestOrigin.M).executeUpdate();
                        em.flush();
                    }
                }
            }

        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_049,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_050,
                    DataExceptionCode.GLB_006_MSG, e);

        }
    }

    /**
     * The method findNotPortalRequestsByStateAndOrigin is used to find a list
     * of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}, in spite of
     * state and origin parameters
     *
     * @param state  is an instance of type
     *               {@link com.gam.nocr.ems.data.enums.CardRequestState}
     * @param origin is an instance of type
     *               {@link com.gam.nocr.ems.data.enums.CardRequestOrigin}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<CardRequestTO> findNotPortalRequestsByStateAndOrigin(
            CardRequestState state, CardRequestOrigin origin)
            throws BaseException {
        Integer DEFAULT_MAX_RESULT = 10;
        Integer maxResult = null;
        ProfileManager pm;
        try {
            pm = ProfileHelper.getProfileManager();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_052,
                    DataExceptionCode.CDI_029_MSG, e);
        }

        try {
            maxResult = Integer
                    .parseInt((String) pm
                            .getProfile(
                                    ProfileKeyName.KEY_CARD_REQUEST_FIND_BY_STATE_AND_ORIGIN_MAX_RESULT,
                                    true, null, null));
        } catch (Exception e) {
            logger.warn(DataExceptionCode.CDI_053,
                    DataExceptionCode.CDI_052_MSG);
        }

        if (maxResult == null) {
            maxResult = DEFAULT_MAX_RESULT;
        }

        List<CardRequestTO> cardRequestList = null;
        try {
            cardRequestList = em
                    .createQuery(
                            "" + "SELECT CRQ " + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.state =:CARD_REQUEST_STATE "
                                    + "AND CRQ.origin =:CARD_REQUEST_ORIGIN "
                                    + "AND CRQ.portalRequestId IS NULL "
                                    + "ORDER BY CRQ.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", state)
                    .setParameter("CARD_REQUEST_ORIGIN", origin)
                    .setMaxResults(maxResult).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_051,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return cardRequestList;
    }

    @Override
    public void updateCardRequests(List<CardRequestTO> cardRequestTOList)
            throws BaseException {
        if (EmsUtil.checkListSize(cardRequestTOList)) {
            try {
                for (CardRequestTO cardRequestTO : cardRequestTOList) {
                    em.createQuery(
                            "" + "UPDATE CardRequestTO CRQ "
                                    + "SET CRQ.state = :CARD_REQUEST_STATE, "
                                    + "CRQ.metadata = :CARD_REQUEST_METADATA "
                                    + "WHERE CRQ.id = :CARD_REQUEST_ID")
                            .setParameter("CARD_REQUEST_STATE",
                                    cardRequestTO.getState())
                            .setParameter("CARD_REQUEST_METADATA",
                                    cardRequestTO.getMetadata())
                            .setParameter("CARD_REQUEST_ID",
                                    cardRequestTO.getId()).executeUpdate();
                    em.flush();
                }
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                    throw new DAOException(DataExceptionCode.CDI_051,
                            DataExceptionCode.CDI_042_MSG, e);

                throw new DAOException(DataExceptionCode.CDI_052,
                        DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }

    /**
     * The method findRequestsByBoundaryLimitAndState is used to find a list of
     * CardRequestTO in spite of their state field.
     *
     * @param from             is an instance of type {@link Integer}, which represents the
     *                         first record to be fetched
     * @param to               is an instance of type {@link Integer}, which represents the
     *                         last record to be fetched
     * @param cardRequestState is an enumeration of type
     *                         {@link com.gam.nocr.ems.data.enums.CardRequestState}, which
     *                         represents the state of the request
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     */
    @Override
    public List<CardRequestTO> findRequestsByBoundaryLimitAndState(
            Integer from, Integer to, CardRequestState cardRequestState)
            throws BaseException {
        try {
            return em
                    .createQuery(
                            "" + "SELECT CRQ "
                                    + "FROM CardRequestTO CRQ, CitizenTO CTZ "
                                    + "WHERE CRQ.citizen.id = CTZ.id "
                                    + "AND CRQ.state =:CARD_REQUEST_STATE "
                                    + "ORDER BY CRQ.priority DESC, CRQ.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_054,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public void updateArchiveId(Long cardRequestId, String archiveId)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.archiveId = :archiveId "
                            + "WHERE CRQ.id = :CARD_REQUEST_ID")
                    .setParameter("archiveId", archiveId)
                    .setParameter("CARD_REQUEST_ID", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_054,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void addRequestedSms(Long portalCardRequestId) throws BaseException {
        String smsBody = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_BODY,
                DEFAULT_SMS_BODY);

        try {
            StringBuilder sql = new StringBuilder(
                    "call fill_outgoing_sms(:portalCardRequestId, '" + smsBody
                            + "')");
            em.createNativeQuery(sql.toString())
                    .setParameter("portalCardRequestId", portalCardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_096,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public CardRequestTO findByContainerId(Long containerId,
                                           DepartmentDispatchSendType containerType) throws BaseException {
        List<CardRequestTO> cardRequestTOList = new ArrayList<CardRequestTO>();
        try {
            switch (containerType) {
                case CARD:
                    cardRequestTOList = em
                            .createQuery(
                                    "SELECT CRQ "
                                            + "FROM CardRequestTO crq, CardTO crd "
                                            + "WHERE crq.card.id = crd.id "
                                            + "and crd.id = :containerId "
                                            + "ORDER BY crd.id DESC",
                                    CardRequestTO.class)
                            .setParameter("containerId", containerId)
                            .getResultList();
                    break;
                case BATCH:
                    cardRequestTOList = em
                            .createQuery(
                                    "SELECT CRQ "
                                            + "FROM CardRequestTO crq, CardTO crd "
                                            + "WHERE crq.card.id = crd.id "
                                            + "and crd.id in (select c.id from CardTO c, BatchTO b "
                                            + "where c.batch.id = b.id "
                                            + "and b.id = :containerId)"
                                            + "ORDER BY crd.id DESC",
                                    CardRequestTO.class)
                            .setParameter("containerId", containerId)
                            .getResultList();
                    break;
                case BOX:
                    cardRequestTOList = em
                            .createQuery(
                                    "SELECT CRQ "
                                            + "FROM CardRequestTO crq, CardTO crd "
                                            + "WHERE crq.card.id = crd.id "
                                            + "and crd.id in (select c.id from CardTO c, BatchTO b, BoxTO bo "
                                            + "where c.batch.id = b.id "
                                            + "and b.box.id = bo.id "
                                            + "and bo.id = :containerId) "
                                            + "ORDER BY crd.id DESC",
                                    CardRequestTO.class)
                            .setParameter("containerId", containerId)
                            .getResultList();
                    break;
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_055,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestTOList))
            return cardRequestTOList.get(0);
        else
            return null;
    }

    @Override
    public CardRequestTO findByNIdBirthDateSolar(String nationalID,
                                                 String birthDate) throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            cardRequestTOList = em
                    .createQuery(
                            " select crq from CardRequestTO crq "
                                    + " where crq.citizen.citizenInfo.birthDateSolar = :birthDate "
                                    + " and crq.citizen.nationalID = :nid "
                                    + " order by crq.id desc",
                            CardRequestTO.class)
                    .setParameter("nid", nationalID)
                    .setParameter("birthDate", birthDate).getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_061,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(cardRequestTOList))
            return cardRequestTOList.get(0);
        else
            return null;
    }

    @Override
    public void doCardRequestRepealAction(Long cardRequestId,
                                          CardRequestedAction cardRequestedAction) throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.requestedAction = :cardRequestedAction "
                            + "WHERE CRQ.id = :CARD_REQUEST_ID")
                    .setParameter("cardRequestedAction", cardRequestedAction)
                    .setParameter("CARD_REQUEST_ID", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_REQUESTED_ACTION))
                throw new DAOException(DataExceptionCode.CDI_058,
                        DataExceptionCode.CDI_058_MSG, e);

            throw new DAOException(DataExceptionCode.CDI_059,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public Long findRequestCountByAction(CardRequestedAction cardRequestedAction)
            throws BaseException {
        List<Long> totalCountList;
        try {
            totalCountList = em
                    .createQuery(
                            "SELECT count(CRQ.id) "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.requestedAction =:REQUEST_ACTION "
                                    + "ORDER BY CRQ.id", Long.class)
                    .setParameter("REQUEST_ACTION", cardRequestedAction)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_060,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(totalCountList))
            return totalCountList.get(0);
        else
            return null;
    }

    @Override
    public List<CardRequestTO> fetchCardRequestByAction(Integer from,
                                                        Integer to, CardRequestedAction cardRequestedAction)
            throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            cardRequestTOList = em
                    .createQuery(
                            "SELECT CRQ "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.requestedAction =:REQUEST_ACTION "
                                    + "ORDER BY CRQ.id", CardRequestTO.class)
                    .setParameter("REQUEST_ACTION", cardRequestedAction)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_060,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        for (CardRequestTO cardRequestTO : cardRequestTOList) {
            cardRequestTO.getCitizen().getFirstNamePersian();
            cardRequestTO.getCitizen().getCitizenInfo()
                    .getFatherFirstNamePersian();
            cardRequestTO.getCitizen().getCitizenInfo().getBiometrics().size();
            cardRequestTO.getCitizen().getCitizenInfo().getDocuments().size();
        }
        return cardRequestTOList;
    }

    /**
     * The method findByEnrollmentOfficeIdAndStates is used to find a list of
     * type {@link com.gam.nocr.ems.data.domain.CardRequestTO}, regards to
     * parameters of enrollmentOfficeId and a comma separated string, which
     * represents the sequence of desired states.
     *
     * @param enrollmentOfficeId is an instance of type {@link Long}, which represents a
     *                           specified instance of type
     *                           {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @param states             is a list of type {@link CardRequestState}, which represents
     *                           the sequence of desired states
     * @return a list of type {@link Long}, which represents the ids for
     * specified instances of type {@link CardRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<Long> findByEnrollmentOfficeIdAndStates(
            Long enrollmentOfficeId, List<CardRequestState> states)
            throws BaseException {
        try {
            return em
                    .createQuery(
                            "SELECT count(CRQ.id) "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE "
                                    + "CRQ.enrollmentOffice.id = :ENROLLMENT_OFFICE_ID "
                                    + "AND " + "CRQ.state IN (:STATES)",
                            Long.class)
                    .setParameter("ENROLLMENT_OFFICE_ID", enrollmentOfficeId)
                    .setParameter("STATES", states).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_062,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    // Adldoost
    @Override
    public long findEnrollmentOfficeCardRequestCount(Long enrollmentOfficeId)
            throws BaseException {
        try {
            return em
                    .createQuery(
                            "SELECT count(CRQ.id) "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE "
                                    + "CRQ.enrollmentOffice.id = :ENROLLMENT_OFFICE_ID ",
                            Long.class)
                    .setParameter("ENROLLMENT_OFFICE_ID", enrollmentOfficeId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_062,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    //Madanipour
    @Override
    public void replaceSuperiorWithOfficeId(Long superiorEnrollmentOfficeId, Long enrollmentOfficeId,
                                            List<Long> cardRequestIds) throws BaseException {
        try {
            // em.createQuery(
            // "UPDATE CardRequestTO CRQ "
            // + "SET CRQ.enrollmentOffice.id = :superiorEnrollmentOfficeId,"
            // + "CRQ.originalCardRequestOfficeId = :enrollmentOfficeId "
            // + "WHERE CRQ.enrollmentOffice.id = :enrollmentOfficeId")
            // .setParameter("enrollmentOfficeId", enrollmentOfficeId)
            // .setParameter("superiorEnrollmentOfficeId",
            // superiorEnrollmentOfficeId).executeUpdate();
            em.createQuery(
                    "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.enrollmentOffice.id = :superiorEnrollmentOfficeId,"
                            + " CRQ.originalCardRequestOfficeId = :enrollmentOfficeId"
                            + " WHERE CRQ.id in( :cardRequestIds)")
                    .setParameter("cardRequestIds", cardRequestIds)
                    .setParameter("superiorEnrollmentOfficeId",
                            superiorEnrollmentOfficeId).setParameter("enrollmentOfficeId", enrollmentOfficeId).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_059,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public Long getRequestsCountForSendToAFIS() throws BaseException {
        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }

        List<CardRequestedAction> notAllowedCardRequestedActions = new ArrayList<CardRequestedAction>();
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEALING);
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEAL_ACCEPTED);

        try {
            List<Long> countList = em
                    .createQuery(
                            "SELECT COUNT(CRT.id) "
                                    + "FROM CardRequestTO CRT "
                                    + "WHERE "
                                    + "CRT.state =:CARD_REQUEST_STATE "
                                    + "AND (CRT.requestedAction is null or CRT.requestedAction not in (:CARD_REQUESTED_ACTION)) "
                                    + "AND ("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE CRH.systemID = :SYSTEM_ID "
                                    + "AND CRH.cardRequestHistoryAction = (:HISTORY_ACTION) "
                                    + "AND CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))"
                                    + ") "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED)
                    .setParameter("CARD_REQUESTED_ACTION",
                            notAllowedCardRequestedActions)
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .setParameter("HISTORY_ACTION",
                            CardRequestHistoryAction.AFIS_SEND_ERROR)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .getResultList();
            if (EmsUtil.checkListSize(countList)) {
                return countList.get(0);
            }
            return null;

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_064,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    //Anbari
    @Override
    public List<Long> getRequestIdsForSendToAFIS(Integer fetchLimit) throws BaseException {

        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }

        List<CardRequestedAction> notAllowedCardRequestedActions = new ArrayList<CardRequestedAction>();
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEALING);
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEAL_ACCEPTED);
        try {
            List<Long> idList = em
                    .createQuery(
                            "SELECT CRT.id "
                                    + "FROM CardRequestTO CRT "
                                    + "WHERE "
                                    + "CRT.state =:CARD_REQUEST_STATE "
                                    + "AND (CRT.requestedAction is null or CRT.requestedAction not in (:CARD_REQUESTED_ACTION)) "
                                    + "AND ("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE CRH.systemID = :SYSTEM_ID "
                                    + "AND CRH.cardRequestHistoryAction = (:HISTORY_ACTION) "
                                    + "AND CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))"
                                    + ") "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED)
                    .setParameter("CARD_REQUESTED_ACTION",
                            notAllowedCardRequestedActions)
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .setParameter("HISTORY_ACTION",
                            CardRequestHistoryAction.AFIS_SEND_ERROR)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .setMaxResults(fetchLimit)
                    .getResultList();
            if (EmsUtil.checkListSize(idList)) {
                return idList;
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_095,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }


    /**
     * The method findRequestForSendToAFIS is used to find a request in the
     * state of 'CardRequestState.APPROVED' to prepare data for sending to AFIS
     *
     * @param from is an instance of type {@link Integer}, which represents a
     *             specified index for the first record
     * @return an instance of type
     * {@link com.gam.nocr.ems.data.domain.CardRequestTO} or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public CardRequestTO findRequestForSendToAFIS(Integer from)
            throws BaseException {
        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }

        List<CardRequestedAction> notAllowedCardRequestedActions = new ArrayList<CardRequestedAction>();
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEALING);
        notAllowedCardRequestedActions.add(CardRequestedAction.REPEAL_ACCEPTED);

        try {
            List<CardRequestTO> cardRequestList = em
                    .createQuery(
                            "SELECT CRT "
                                    + "FROM CardRequestTO CRT, CitizenTO CTZ "
                                    + "WHERE "
                                    + "CRT.citizen.id = CTZ.id AND "
                                    + "CRT.state =:CARD_REQUEST_STATE "
                                    + "AND (CRT.requestedAction is null or CRT.requestedAction not in (:CARD_REQUESTED_ACTION)) "
                                    + "AND ("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE CRH.systemID = :SYSTEM_ID "
                                    + "AND CRH.cardRequestHistoryAction = (:HISTORY_ACTION) "
                                    + "AND CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))"
                                    + ") "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED)
                    .setParameter("CARD_REQUESTED_ACTION",
                            notAllowedCardRequestedActions)
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .setParameter("HISTORY_ACTION",
                            CardRequestHistoryAction.AFIS_SEND_ERROR)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .setFirstResult(from).setMaxResults(1).getResultList();

            if (EmsUtil.checkListSize(cardRequestList)) {
                return cardRequestList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_063,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<CardRequestTO> findByNationalId(String nationalId)
            throws BaseException {
        try {
            return em
                    .createQuery(
                            "SELECT CRT "
                                    + "FROM CardRequestTO CRT, CitizenTO CTZ "
                                    + "WHERE " + "CRT.citizen.id = CTZ.id AND "
                                    + "CTZ.nationalID = :CITIZEN_NATIONAL_ID "
                                    + "ORDER BY CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CITIZEN_NATIONAL_ID", nationalId)
                    .getResultList();

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_065,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public int updateRequest(CardRequestTO cardRequest) throws BaseException {
        try {
            return em
                    .createQuery(
                            ""
                                    + "UPDATE CardRequestTO CRQ "
                                    + "SET CRQ.state = :NEW_STATE, CRQ.metadata = :META_DATA "
                                    + "WHERE CRQ.id = :REQUEST_ID " + "AND "
                                    + "CRQ.state = :OLD_STATE ")
                    .setParameter("NEW_STATE", cardRequest.getState())
                    .setParameter("META_DATA", cardRequest.getMetadata())
                    .setParameter("REQUEST_ID", cardRequest.getId())
                    .setParameter("OLD_STATE", CardRequestState.PENDING_IMS)
                    .executeUpdate();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_069,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_070,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public int updateRequestByStateAndOrigin(CardRequestTO cardRequestTO,
                                             Map<CardRequestOrigin, CardRequestState> map) throws BaseException {
        int updateResult = 0;
        try {
            if (cardRequestTO != null) {
                Object[] origins = map.keySet().toArray();
                if (EmsUtil.checkArraySize(origins)) {
                    for (Object origin : origins) {
                        if (CardRequestOrigin.M.equals(origin)) {
                            updateResult = em
                                    .createQuery(
                                            ""
                                                    + "UPDATE CardRequestTO CRQ "
                                                    + "SET CRQ.state = :REQUEST_NEW_STATE, CRQ.metadata = :REQUEST_METADATA "
                                                    + "WHERE CRQ.id = :REQUEST_ID "
                                                    + "AND "
                                                    + "CRQ.state = :REQUEST_OLD_STATE "
                                                    + "AND "
                                                    + "CRQ.origin = :REQUEST_ORIGIN")
                                    .setParameter("REQUEST_OLD_STATE",
                                            CardRequestState.PENDING_IMS)
                                    .setParameter("REQUEST_NEW_STATE",
                                            map.get(origin))
                                    .setParameter("REQUEST_METADATA",
                                            cardRequestTO.getMetadata())
                                    .setParameter("REQUEST_ORIGIN", origin)
                                    .setParameter("REQUEST_ID",
                                            cardRequestTO.getId())
                                    .executeUpdate();
                            em.flush();
                            if (updateResult != 0) {
                                return updateResult;
                            }
                        }

                        if (!CardRequestOrigin.M.equals(origin)) {
                            updateResult = em
                                    .createQuery(
                                            ""
                                                    + "UPDATE CardRequestTO CRQ "
                                                    + "SET CRQ.state = :REQUEST_NEW_STATE, CRQ.metadata = :REQUEST_METADATA "
                                                    + "WHERE CRQ.id = :REQUEST_ID "
                                                    + "AND "
                                                    + "CRQ.state = :REQUEST_OLD_STATE "
                                                    + "AND "
                                                    + "CRQ.origin != :REQUEST_ORIGIN")
                                    .setParameter("REQUEST_OLD_STATE",
                                            CardRequestState.PENDING_IMS)
                                    .setParameter("REQUEST_NEW_STATE",
                                            map.get(origin))
                                    .setParameter("REQUEST_METADATA",
                                            cardRequestTO.getMetadata())
                                    .setParameter("REQUEST_ORIGIN",
                                            CardRequestOrigin.M)
                                    .setParameter("REQUEST_ID",
                                            cardRequestTO.getId())
                                    .executeUpdate();
                            em.flush();
                            if (updateResult != 0) {
                                return updateResult;
                            }
                        }
                    }
                }
            }
            return updateResult;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_067,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_068,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public CardRequestOrigin getOrigin(Long requestId) throws BaseException {
        try {
            List<CardRequestOrigin> requestOrigins = em
                    .createQuery(
                            "" + "SELECT CRQ.origin "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.id = :REQUEST_ID",
                            CardRequestOrigin.class)
                    .setParameter("REQUEST_ID", requestId).getResultList();
            if (EmsUtil.checkListSize(requestOrigins)) {
                return requestOrigins.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_071,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public Long getRequestsCountForIssue() throws BaseException {
        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_CMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.warn(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }

        try {
            List<Long> countList = em
                    .createQuery(
                            "SELECT COUNT(CRT.id) "
                                    + "FROM CardRequestTO CRT "
                                    + "WHERE "
                                    + "CRT.state =:CARD_REQUEST_STATE AND "
                                    + "("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE "
                                    + "CRH.systemID = :SYSTEM_ID AND CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))"
                                    + ") "
                            /*+ "ORDER BY CRT.priority DESC, CRT.id ASC"*/,
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED_BY_AFIS)
                    .setParameter("SYSTEM_ID", SystemId.CMS)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .getResultList();
            if (EmsUtil.checkListSize(countList)) {
                return countList.get(0);
            }
            return null;

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_066,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<Long> getRequestIdsForIssue(Integer fetchLimit) throws BaseException {
        try {
            List<Long> idList = em
                    .createQuery(
                            "SELECT CRT.id "
                                    + "FROM CardRequestTO CRT "
                                    + "WHERE "
                                    + "CRT.state =:CARD_REQUEST_STATE "
                                    + "ORDER BY CRT.priority DESC, CRT.id ASC",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED_BY_AFIS)
                    .setMaxResults(fetchLimit)
                    .getResultList();
            if (EmsUtil.checkListSize(idList)) {
                return idList;
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_088,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }


    /**
     * The method findRequestForIssueCard is used to find a request in the state
     * of 'CardRequestState.APPROVED_BY_AFIS' to prepare data for sending to
     * AFIS
     *
     * @param from is an instance of type {@link Integer}, which represents a
     *             specified index for the first record
     * @return an instance of type
     * {@link com.gam.nocr.ems.data.domain.CardRequestTO} or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public CardRequestTO findRequestForIssueCard(Integer from)
            throws BaseException {
        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_CMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.warn(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }

        try {
            List<CardRequestTO> cardRequestList = em
                    .createQuery(
                            "SELECT CRT "
                                    + "FROM CardRequestTO CRT, CitizenTO CTZ "
                                    + "WHERE "
                                    + "CRT.citizen.id = CTZ.id AND "
                                    + "CRT.state =:CARD_REQUEST_STATE AND "
                                    + "("
                                    + "CRT.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE CRH.systemID = :SYSTEM_ID AND "
                                    + "CRH.result like '%GAM_EW%' AND CRH.date > (:SPECIFIED_TIME))) order by CRT.priority DESC, CRT.id ASC",
                            CardRequestTO.class)
                    .setParameter("CARD_REQUEST_STATE",
                            CardRequestState.APPROVED_BY_AFIS)
                    .setParameter("SYSTEM_ID", SystemId.CMS)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .setFirstResult(from).setMaxResults(1).getResultList();

            if (EmsUtil.checkListSize(cardRequestList)) {
                return cardRequestList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_072,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public Long findRequestsCountByState(CardRequestState cardRequestState)
            throws BaseException {
        try {
            List<Long> longList = em
                    .createQuery(
                            "" + "SELECT COUNT(CRQ) "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.state =:CARD_REQUEST_STATE ",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .getResultList();
            if (EmsUtil.checkListSize(longList)) {
                return longList.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_073,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }


    //IMS:Anbari : COMMENTED
	/*
	@Override
	public List<Long> findAfisResultRequestsCountByState(CardRequestState cardRequestState,Integer fetchLimit)
			throws BaseException {
		try {
			List<Long> longList = em
					.createQuery(
							"" + "SELECT CRQ.id "
									+ "FROM CardRequestTO CRQ "
									+ "WHERE CRQ.state =:CARD_REQUEST_STATE ORDER BY CRQ.priority DESC, CRQ.id ASC ",
									Long.class)
									.setParameter("CARD_REQUEST_STATE", cardRequestState)
									.setMaxResults(fetchLimit)
									.getResultList();
			if (EmsUtil.checkListSize(longList)) {
				return longList;
			}
			return null;
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CDI_073,
					DataExceptionCode.GLB_005_MSG, e);
		}

	}
	*/


    //IMS:Anbari
    @Override
    public List<Long> findAfisResultRequestsCountByState(CardRequestState cardRequestState, Integer fetchLimit)
            throws BaseException {
        Timestamp specifiedTime = null;
        String timeIntervalFromConfig = null;

        try {
            timeIntervalFromConfig = (String) ProfileHelper
                    .getProfileManager()
                    .getProfile(
                            ProfileKeyName.KEY_CARD_REQUESTS_COUNT_QUERY_TIME_INTERVAL_FOR_IMS,
                            true, null, null);
        } catch (ProfileException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            specifiedTime = extractSpecifiedTime(timeIntervalFromConfig, 1);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        if (specifiedTime == null) {
            specifiedTime = new Timestamp(new Date().getTime());
        }


        try {

            List<Long> longList = em
                    .createQuery(
                            " SELECT CRQ.id "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.state =:CARD_REQUEST_STATE AND "
                                    + "CRQ.id NOT IN "
                                    + "(SELECT CRH.cardRequest.id FROM CardRequestHistoryTO CRH WHERE "
                                    + "CRH.systemID = :SYSTEM_ID AND CRH.cardRequestHistoryAction = (:HISTORY_ACTION) AND CRH.date > (:SPECIFIED_TIME)) "
                                    + "ORDER BY CRQ.priority DESC, CRQ.id ASC ",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setParameter("HISTORY_ACTION", CardRequestHistoryAction.AFIS_WAITING)
                    .setParameter("SPECIFIED_TIME", specifiedTime)
                    .setParameter("SYSTEM_ID", SystemId.IMS)
                    .setMaxResults(fetchLimit)
                    .getResultList();
            if (EmsUtil.checkListSize(longList)) {
                return longList;
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_073,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }


    /**
     * The method getCardRequestIdsByAction is used to find request ids
     * according to the cardRequestedAction that passed as the parameter
     *
     * @param cardRequestedAction is an instance of type
     *                            {@link com.gam.nocr.ems.data.enums.CardRequestedAction}
     * @return a list with type of {@link Long}, which represents cardRequestIds
     * , or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<Long> getCardRequestIdsByAction(
            CardRequestedAction cardRequestedAction) throws BaseException {
        List<Long> cardRequestIds = em
                .createQuery(
                        ""
                                + "SELECT CRQ.id "
                                + "FROM CardRequestTO CRQ "
                                + "WHERE CRQ.requestedAction in (:CARD_REQUEST_ACTION)",
                        Long.class)
                .setParameter("CARD_REQUEST_ACTION", cardRequestedAction)
                .getResultList();
        if (EmsUtil.checkListSize(cardRequestIds)) {
            return cardRequestIds;
        }
        return null;
    }

    @Override
    public List<CardRequestTO> findByCardId(Long cardId) throws BaseException {
        try {
            return em
                    .createQuery(
                            "" + "SELECT CRQ FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.card.id = :cardId",
                            CardRequestTO.class).setParameter("cardId", cardId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_074,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public Integer fetchBiometricFlag(Long id) throws BaseException {
        BigDecimal biometricFlag = new BigDecimal(0);
        try {
            List<BigDecimal> result = em.createNativeQuery(
                    "select crq.crq_flag from emst_card_request crq where crq.crq_id = :ID")
                    .setParameter("ID", id).getResultList();
            if (EmsUtil.checkListSize(result)) {
                biometricFlag = result.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_076,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return biometricFlag.intValue();
    }

    @Override
    public List<CardRequestVTO> fetchCardRequests(GeneralCriteria criteria)
            throws BaseException {
        List<CardRequestVTO> result = new ArrayList<CardRequestVTO>();

        if (criteria.getParameters() == null
                || (criteria.getParameters().containsKey("checkSecurity") && criteria
                .getParameters().size() < 3)
                || (!criteria.getParameters().containsKey("checkSecurity") && criteria
                .getParameters().size() < 1)) {
            return result;
        }

        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select "
                            + "cr.crq_id,"
                            + "cz.ctz_first_name_fa,"
                            + "cz.ctz_surname_fa,"
                            + "cz.ctz_national_id,"
                            + "d.dep_name,"
                            + "cr.crq_enrolled_date,"
                            + "cr.crq_portal_enrolled_date,"
                            + "cr.crq_state,"
                            + "cr.crq_type,"
                            + "c.crd_state,"
                            + "cr.crq_tracking_id,"
                            + "cr.crq_requested_action,"
                            + "to_char(cr.crq_origin),"
                            + "(select dep.dep_name from emst_department dep "
                            + "where dep.dep_id = cr.crq_delivered_office_id) crq_delivered_office_name,"
                            + "c.CRD_DELIVER_DATE, cr.crq_rsv_date, cr.crq_flag "
                            + ",cr.crq_priority "
                            + ",cr.CRQ_REENROLLED_DATE "
                            + "from " + "emst_card_request cr,"
                            + "emst_citizen cz," + "emst_card c,"
                            + "emst_department d " + "where "
                            + "cr.crq_citizen_id = cz.ctz_id and "
                            + "cr.crq_card_id=c.crd_id(+) and "
                            + "cr.crq_enroll_office_id=d.dep_id(+) ");

            if (criteria.getParameters() != null) {
                Set<String> keySet = criteria.getParameters().keySet();
                if (keySet != null) {

                    if (keySet.contains("checkSecurity")) {
                        //queryBuffer
                        //.append(" and cr.crq_enroll_office_id in (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id=:perid union select e.eof_id from emst_enrollment_office e connect by prior e.eof_id=e.eof_superior_office start with e.eof_id in (select p.per_dep_id from emst_person p where p.per_id=:perid )))");
                        queryBuffer.append(" and nvl(cr.crq_enroll_office_id,-1) in (:depIds)");
                    } else {
                        queryBuffer.append(" and nvl(cr.crq_enroll_office_id,-1) in (select EOF_ID from EMST_ENROLLMENT_OFFICE where eof_is_deleted = 0 union select -1 from dual)");
                    }

                    int flag = 0;

                    for (String key : keySet) {
                        if ("citizenFirstName".equals(key)) {
                            queryBuffer
                                    .append(" and cz.ctz_first_name_fa like :"
                                            + key);
                        } else if ("citizenSurname".equals(key)) {
                            queryBuffer.append(" and cz.ctz_surname_fa like :"
                                    + key);
                        } else if ("citizenNId".equals(key)) {
                            queryBuffer.append(" and cz.ctz_national_id = :"
                                    + key);
                        } else if ("enrollmentOfficeId".equals(key)) {
                            queryBuffer.append(" and d.dep_id = :" + key);
                        } else if ("fromEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_enrolled_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_enrolled_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("fromPortalEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_portal_enrolled_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toPortalEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_portal_enrolled_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        }

                        //hossein 8 feature start
                        else if ("reservationDateFrom".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_rsv_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("reservationDateTo".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_rsv_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("documentFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 4) = 4");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 4) != 4");
                            }
                            flag += 4;
                        } else if ("faceFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 1) = 1");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 1) != 1");
                            }
                            flag += 1;
                        } else if ("fingerFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 2) = 2");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 2) != 2");
                            }
                            flag += 2;
                        }
                        //hossein 8 feature end

                        else if ("fromDeliveredDate".equals(key)) {
                            queryBuffer
                                    .append(" and c.CRD_DELIVER_DATE > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toDeliveredDate".equals(key)) {
                            queryBuffer
                                    .append(" and c.CRD_DELIVER_DATE < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("cardRequestState".equals(key)) {
                            queryBuffer.append(" and cr.crq_state = :" + key);
                        } else if ("cardType".equals(key)) {
                            queryBuffer.append(" and cr.crq_type = :" + key);
                        } else if ("cardState".equals(key)) {
                            queryBuffer.append(" and c.crd_state = :" + key);
                        } else if ("requestedAction".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_requested_action = :" + key);
                        } else if ("trackingId".equals(key)) {
                            queryBuffer.append(" and cr.crq_tracking_id = :"
                                    + key);
                        } else if ("deliveredOfficeId".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_delivered_office_id = :"
                                            + key);
                        }
                    }
                    // hossein 8 feature start
                    if ((flag & 1) == 1) {
                        criteria.getParameters().remove("faceFlag");
                    }

                    if ((flag & 2) == 2) {
                        criteria.getParameters().remove("fingerFlag");
                    }

                    if ((flag & 4) == 4) {
                        criteria.getParameters().remove("documentFlag");
                    }
                    // hossein 8 feature end
                }
            }

            queryBuffer.append(" order by nvl(cr.crq_enroll_office_id,-1), cr.crq_id ");

/*			if (EmsUtil.checkString(criteria.getOrderBy())) {
				String orderBy = criteria.getOrderBy();
				String sortKey = "cr.crq_id";
				String dir = "asc";

				Set<String> keySet = criteria.getParameters().keySet();
				if (keySet != null) {
					if (keySet.contains("checkSecurity")) {
						queryBuffer.append(" order by cr.crq_enroll_office_id, cr.crq_id ");
					}
					else
					{
						String[] split = orderBy.split(" ");
						if (split.length >= 2) {
							sortKey = split[0].trim();
							dir = split[1].trim();
						}

						if ("citizenFirstName".equals(sortKey)) {
							sortKey = "cz.ctz_first_name_fa";
						} else if ("citizenSurname".equals(sortKey)) {
							sortKey = "cz.ctz_surname_fa";
						} else if ("citizenNId".equals(sortKey)) {
							sortKey = "cz.ctz_national_id";
						} else if ("enrollmentOfficeName".equals(sortKey)) {
							sortKey = "d.dep_name";
						} else if ("enrolledDate".equals(sortKey)) {
							sortKey = "cr.crq_enrolled_date";
						} else if ("portalEnrolledDate".equals(sortKey)) {
							sortKey = "cr.crq_portal_enrolled_date";
						} else if ("cardRequestState".equals(sortKey)) {
							sortKey = "cr.crq_state";
						} else if ("cardType".equals(sortKey)) {
							sortKey = "cr.crq_type";
						} else if ("cardState".equals(sortKey)) {
							sortKey = "c.crd_state";
						} else if ("requestedAction".equals(sortKey)) {
							sortKey = "cr.crq_requested_action";
						} else if ("trackingId".equals(sortKey)) {
							sortKey = "cr.crq_tracking_id";
						} else if ("deliveredOfficeName".equals(sortKey)) {
							sortKey = "crq_delivered_office_name";
						} else if ("requestOrigin".equals(sortKey)) {
							sortKey = "cr.crq_origin";
						} else if ("deliveredDate".equals(sortKey)) {
							sortKey = "c.CRD_DELIVER_DATE";
						} else if ("reservationDate".equals(sortKey)) {
							sortKey = "CR.CRQ_RSV_DATE";
						}

						queryBuffer.append(" order by ").append(sortKey).append(" ")
						.append(dir).append(",cr.crq_id asc");

					}
				}

			} else {
				queryBuffer.append(" order by cr.crq_enroll_office_id, cr.crq_id ");
			}*/

            Query query = em.createNativeQuery(queryBuffer.toString());
            Map<String, Object> parameters = criteria.getParameters();
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (!"checkSecurity".equals(key) && !"faceFlag".equals(key) && !"fingerFlag".equals(key) && !"documentFlag".equals(key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            query.setMaxResults(criteria.getPageSize()).setFirstResult(
                    criteria.getPageNo() * criteria.getPageSize());

            // query.setHint("javax.persistence.query.timeout", 30000);
            List resultList = query.getResultList();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    Timestamp attendDate = null;
                    if (data[18] != null)//reenrolled date
                        attendDate = (Timestamp) data[18];
                    else if (data[5] != null)//enrolled date
                        attendDate = (Timestamp) data[5];
                    else if (data[15] != null)//reserved date
                        attendDate = (Timestamp) data[15];

                    CardRequestVTO request = new CardRequestVTO(
                            ((BigDecimal) data[0]).longValue(),
                            (String) data[1], (String) data[2],
                            (String) data[3], null, (String) data[4],
                            (Timestamp) data[5], (Timestamp) data[6],
                            (String) data[7], (String) data[8],
                            (String) data[9], (String) data[10],
                            (String) data[11], (String) data[12],
                            (String) data[13], (Timestamp) data[14],
                            (Timestamp) data[15],
                            ((BigDecimal) data[16]).intValue(), ((BigDecimal) data[17]).intValue(), attendDate);
                    result.add(request);
                }
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_077,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return result;
    }

    @Override
    public Integer countCardRequests(GeneralCriteria criteria)
            throws BaseException {
        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select count(cr.crq_id) " + "from "
                            + "emst_card_request cr," + "emst_citizen cz,"
                            + "emst_card c," + "emst_department d " + "where "
                            + "cr.crq_citizen_id = cz.ctz_id and "
                            + "cr.crq_card_id=c.crd_id(+) and "
                            + "cr.crq_enroll_office_id=d.dep_id(+) ");

            if (criteria.getParameters() != null) {
                Set<String> keySet = criteria.getParameters().keySet();
                if (keySet != null) {

                    if (keySet.contains("checkSecurity")) {
                        //queryBuffer
                        //.append(" and cr.crq_enroll_office_id in (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id=:perid union select e.eof_id from emst_enrollment_office e connect by prior e.eof_id=e.eof_superior_office start with e.eof_id in (select p.per_dep_id from emst_person p where p.per_id=:perid )))");
                        //queryBuffer.append(" and cr.crq_enroll_office_id in (:depIds)");
                        queryBuffer.append(" and nvl(cr.crq_enroll_office_id,-1) in (:depIds)");
                    } else {
                        //queryBuffer.append(" and cr.crq_enroll_office_id in (select EOF_ID from EMST_ENROLLMENT_OFFICE)");
                        queryBuffer.append(" and nvl(cr.crq_enroll_office_id,-1) in (select EOF_ID from EMST_ENROLLMENT_OFFICE where eof_is_deleted = 0 union select -1 from dual)");
                    }


                    int flag = 0;


                    for (String key : keySet) {
                        if ("citizenFirstName".equals(key)) {
                            queryBuffer
                                    .append(" and cz.ctz_first_name_fa like :"
                                            + key);
                        } else if ("citizenSurname".equals(key)) {
                            queryBuffer.append(" and cz.ctz_surname_fa like :"
                                    + key);
                        } else if ("citizenNId".equals(key)) {
                            queryBuffer.append(" and cz.ctz_national_id = :"
                                    + key);
                        } else if ("enrollmentOfficeId".equals(key)) {
                            queryBuffer.append(" and d.dep_id = :" + key);
                        } else if ("fromEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_enrolled_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_enrolled_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("fromPortalEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_portal_enrolled_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toPortalEnrolledDate".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_portal_enrolled_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");

                        }


                        //hossein 8 feature start
                        else if ("reservationDateFrom".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_rsv_date > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("reservationDateTo".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_rsv_date < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("documentFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 4) = 4");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 4) != 4");
                            }
                            flag += 4;
                        } else if ("faceFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 1) = 1");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 1) != 1");
                            }
                            flag += 1;
                        } else if ("fingerFlag".equals(key)) {
                            if ("true".equals(criteria.getParameters().get(key))) {
                                queryBuffer.append(" and bitand(cr.crq_flag, 2) = 2");
                            } else {
                                queryBuffer.append(" and bitand(cr.crq_flag, 2) != 2");
                            }
                            flag += 2;
                        }
                        //hossein 8 feature end

                        else if ("fromDeliveredDate".equals(key)) {
                            queryBuffer
                                    .append(" and c.CRD_DELIVER_DATE > to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("toDeliveredDate".equals(key)) {
                            queryBuffer
                                    .append(" and c.CRD_DELIVER_DATE < to_date(:"
                                            + key + ", 'YYYY/MM/DD HH24:MI')");
                        } else if ("cardRequestState".equals(key)) {
                            queryBuffer.append(" and cr.crq_state = :" + key);
                        } else if ("cardType".equals(key)) {
                            queryBuffer.append(" and cr.crq_type = :" + key);
                        } else if ("cardState".equals(key)) {
                            queryBuffer.append(" and c.crd_state = :" + key);
                        } else if ("requestedAction".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_requested_action = :" + key);
                        } else if ("trackingId".equals(key)) {
                            queryBuffer.append(" and cr.crq_tracking_id = :"
                                    + key);
                        } else if ("deliveredOfficeId".equals(key)) {
                            queryBuffer
                                    .append(" and cr.crq_delivered_office_id= :"
                                            + key);
                        }
                    }

                    // hossein 8 feature start
                    if ((flag & 1) == 1) {
                        criteria.getParameters().remove("faceFlag");
                    }

                    if ((flag & 2) == 2) {
                        criteria.getParameters().remove("fingerFlag");
                    }

                    if ((flag & 4) == 4) {
                        criteria.getParameters().remove("documentFlag");
                    }
                    // hossein 8 feature end
                }
            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            Map<String, Object> parameters = criteria.getParameters();
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (!"checkSecurity".equals(key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            // query.setHint("javax.persistence.query.timeout", 30000);
            Number number = (Number) query.getSingleResult();
            if (number != null) {
                return number.intValue();
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_078,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }

    @Override
    public List<CitizenWTO> fetchCCOSRegistrationCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {

        List<CitizenWTO> result = new ArrayList<CitizenWTO>();
        StringBuffer queryBuffer = new StringBuffer();
        try {
            queryBuffer
                    .append("select firstname, lastname, fathername, nationalId, enrolledDate, reservedDate, requestId, citizenId, state, originalOfficeId, originalOfficeName, trackingId, requestType, hasVipImage, isPaid, paidDate, requestedAction, paymentSuccess, paymentResCode ,reenrollDate ")
                    .append(" from ( ")
                    .append("select ct.ctz_first_name_fa as firstname, ct.ctz_surname_fa as lastname, ")
                    .append("(select inf.czi_father_first_name_fa from emst_citizen_info inf where inf.czi_id = ct.ctz_id) fathername, ")
                    .append("ct.ctz_national_id as nationalId, ")
                    .append("r.crq_enrolled_date as enrolledDate, ")
                    .append("r.crq_rsv_date as reservedDate, ")
                    .append("r.crq_id as requestId, ")
                    .append("ct.ctz_id as citizenId, "
                            // +
                            // "(select d.dep_name from emst_department d where d.dep_id = :departmentId) departmentName, "
                    )
                    .append("(case ")
                    .append("when r.crq_enrolled_date is null then 1 ")
                    .append("when r.crq_state='REFERRED_TO_CCOS' then 2 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 3 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 4 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 5 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 6 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 7 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 8 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 9 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 10 ")
                    .append("when r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED' then 11 ")
                    .append("else 0 ")
                    .append("end) state, ")
                    .append("r.crq_original_enroll_office_id as originalOfficeId, ")
                    .append("(select ed.dep_name from emst_department ed where ed.dep_id = r.crq_original_enroll_office_id) originalOfficeName, ")
                    .append("r.crq_tracking_id as trackingId, ")
                    .append("r.crq_paid_state as isPaid, ")
                    .append("r.crq_paid_date as paidDate, ")
                    .append("r.crq_requested_action as requestedAction, ")
                    .append("r.crq_type as requestType, ")
                    .append("(case when r.crq_origin= 'V' then 1 else 0 end) hasVipImage, ")
                    .append("pay.rpy_is_succeed paymentSuccess, pay.rpy_res_code  paymentResCode, ")
                    .append("r.crq_reenrolled_date  AS reenrollDate ")
                    .append("from emst_card_request r inner join emst_citizen ct ")
                    .append("on r.crq_citizen_id = ct.ctz_id ")
                    .append("left join emst_registration_payment pay on r.crq_payment_id = pay.rpy_id ")
                    .append("where r.CRQ_ESTELAM2_FLAG = 'V' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            EnrollmentOfficeTO enrollmentOffice = criteria
                    .getEnrollmentOffice();
            int cartableType = criteria.getCartableType();

            boolean firstAND = false;

            // 1- cartableFilter
            if (parts.contains("cartableFilter")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }

                boolean first = true;
                queryBuffer.append(" ( ( ");
                if (parts.contains("repealed")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer
                            .append(" (r.crq_requested_action in ('REPEALING','REPEAL_ACCEPTED')) ");
                }
                if (parts.contains("notRepealed")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer.append(" (r.crq_requested_action is null) ");
                }
                queryBuffer.append(" ) AND ( ");
                first = true;
                if (parts.contains("transferred")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }

                    if (enrollmentOffice.getType().equals(
                            EnrollmentOfficeType.NOCR)) {
                        queryBuffer
                                .append(" (r.crq_enroll_office_id = :departmentId and r.crq_original_enroll_office_id is not null) ");
                    } else {
                        queryBuffer
                                .append(" (r.crq_original_enroll_office_id = :departmentId) ");
                    }
                }
                if (parts.contains("notTransferred")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer
                            .append(" (r.crq_enroll_office_id = :departmentId and r.crq_original_enroll_office_id is null) ");
                }
                queryBuffer.append(" ) ) ");
            }

            // 2- criteria filter
            if (parts.contains("firstName")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_national_id = :nationalId) ");
            }
//			if (parts.contains("notMES")) {
//				if (!firstAND) {
//					queryBuffer.append(" AND ");
//				} else {
//					firstAND = false;
//				}
//				queryBuffer.append(" (r.crq_origin not in ('M') and r.CRQ_ESTELAM2_FLAG = 'V') ");
//			}

            // 3-fetch earch cartable & sum their data
            boolean selectCartables = false;
            if ((cartableType & 1) == 1 || (cartableType & 1024) == 1024
                    || (cartableType & 2048) == 2048
                    || (cartableType & 16) == 16 || (cartableType & 2) == 2
                    || (cartableType & 4) == 4 || (cartableType & 8) == 8
                    || (cartableType & 64) == 64 || (cartableType & 128) == 128) {
                selectCartables = true;
            }

            if (selectCartables) {
                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }

                boolean cartableOR = true;
                queryBuffer.append(" ( ");

                // first filter todayReservation pastReservation
                // futureReservation
                if ((cartableType & 1) == 1 || (cartableType & 1024) == 1024
                        || (cartableType & 2048) == 2048) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    boolean first = true;
                    queryBuffer.append(" ((r.crq_state='RESERVED') AND ( ");
                    if (parts.contains("todayReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" (trunc(r.crq_rsv_date)=trunc(sysdate)) ");
                    }
                    if (parts.contains("pastReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" (trunc(r.crq_rsv_date) between trunc(current_date - :reservationRange) and trunc(current_date - 1)) ");
                    }
                    if (parts.contains("futureReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer

                                .append(" (trunc(r.crq_rsv_date) between trunc(current_date + 1) and trunc(current_date + :reservationRange)) ");

                    }
                    queryBuffer.append(" )) ");
                }

                // second filter readyToAuth
                if ((cartableType & 16) == 16) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" ((r.crq_state='REFERRED_TO_CCOS') AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // third filter readyToScan Face Finger
                if ((cartableType & 2) == 2 || (cartableType & 4) == 4
                        || (cartableType & 8) == 8) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    boolean first = true;
                    queryBuffer.append(" (( ");
                    if (parts.contains("readyToScan")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) ) ) ");
                    }
                    if (parts.contains("readyToFing")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) ) ) ");
                    }
                    if (parts.contains("readyToFace")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4) ) ) ");
                    }

                    queryBuffer.append(" ) AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // forth filter readyToApprove
                if ((cartableType & 64) == 64) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" ((r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4) AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // fifth filter underIssuance
                if ((cartableType & 128) == 128) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" (r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED') ");
                }

                queryBuffer.append(" ) ) ");
            } else {
                return new ArrayList<CitizenWTO>();
            }

            if (EmsUtil.checkString(criteria.getOrderBy())) {
                String orderBy = criteria.getOrderBy();
                String sortKey = "requestId";
                String dir = "asc";
                String[] split = orderBy.split(" ");
                if (split.length >= 2) {
                    sortKey = split[0].trim();
                    dir = split[1].trim();
                }

                if ("firstNameFA".equals(sortKey)) {
                    sortKey = "firstname";
                } else if ("sureNameFA".equals(sortKey)) {
                    sortKey = "lastname";
                } else if ("fatherFirstNameFA".equals(sortKey)) {
                    sortKey = "fathername";
                } else if ("nationalId".equals(sortKey)) {
                    sortKey = "nationalId";
                } else if ("enrolledDate".equals(sortKey)) {
                    sortKey = "enrolledDate";
                } else if ("reservationDate".equals(sortKey)) {
                    sortKey = "reservedDate";
                } else if ("state".equals(sortKey)) {
                    sortKey = "state";
                } else if ("originalOfficeName".equals(sortKey)) {
                    sortKey = "originalOfficeName";
                } else if ("originalOfficeId".equals(sortKey)) {
                    sortKey = "originalOfficeId";
                }
                queryBuffer.append(" order by ").append(sortKey).append(" ")
                        .append(dir).append(",requestId asc");
            } else {
                queryBuffer.append(" order by requestId asc ");
            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            query.setMaxResults(criteria.getPageSize()).setFirstResult(
                    criteria.getPageNo() * criteria.getPageSize());

            List resultList = query.getResultList();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    CitizenWTO obj = new CitizenWTO();
                    obj.setFirstNameFA((String) data[0]);
                    obj.setSureNameFA((String) data[1]);
                    obj.setFatherFirstNameFA((String) data[2]);
                    obj.setNationalId((String) data[3]);
                    obj.setEnrolledDate((Timestamp) data[4]);
                    obj.setReservationDate((Timestamp) data[5]);
                    obj.setRequestId(((BigDecimal) data[6]).longValue());
                    obj.setId(((BigDecimal) data[7]).longValue());
                    obj.setState(((BigDecimal) data[8]).longValue());
                    obj.setOriginalOfficeId(data[9] == null ? null
                            : ((BigDecimal) data[9]).longValue());
                    obj.setOriginalOfficeName((String) data[10]);
                    obj.setTrackingId((String) data[11]);
                    obj.setStringType((String) data[12]);
                    obj.setHasVipImage(((BigDecimal) data[13]).toString());
                    Timestamp attendDate = null;
                    if (data[19] != null)//reenrolled date
                        attendDate = (Timestamp) data[19];
                    else if (data[4] != null)//enrolled date
                        attendDate = (Timestamp) data[4];
                    else if (data[5] != null)//reserved date
                        attendDate = (Timestamp) data[5];
                    obj.setAttendDate(attendDate);
                    if (data[14] == null)
                        obj.setIsPaid((long) 0);
                    else {
                        if (data[14].toString().equals("1"))
                            obj.setIsPaid((long) 1);
                        else
                            obj.setIsPaid((long) 0);

                    }

                    obj.setPaidDate(data[15] == null ? null : (Timestamp) data[15]);
                    obj.setRequestedAction(data[16] == null ? null : data[16].toString());
                    Boolean isSuccessfulPayment = Boolean.FALSE;
                    if (data[17] != null && "1".equals(data[17].toString())) {
                        isSuccessfulPayment = Boolean.TRUE;
                    }
                    String paymentResCode = data[18] == null ? null : data[18].toString();
                    obj.setPaymentStatus(isSuccessfulPayment && "0".equals(paymentResCode));
                    result.add(obj);
                }
            }
        } catch (Exception e) {
            logger.error("CCOS Registration Cartable QUERY = "
                    + queryBuffer.toString());
            throw new DataException(DataExceptionCode.CDI_079,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return result;
    }

    @Override
    public Integer countCCOSRegistrationCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {

        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select count(r.crq_id) "
                            + "from emst_card_request r inner join emst_citizen ct "
                            + "on r.crq_citizen_id = ct.ctz_id " + "where r.CRQ_ESTELAM2_FLAG = 'V' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            EnrollmentOfficeTO enrollmentOffice = criteria
                    .getEnrollmentOffice();
            int cartableType = criteria.getCartableType();

            boolean firstAND = false;

            // 1- cartableFilter
            if (parts.contains("cartableFilter")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }

                boolean first = true;
                queryBuffer.append(" ( ( ");
                if (parts.contains("repealed")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer
                            .append(" (r.crq_requested_action in ('REPEALING','REPEAL_ACCEPTED')) ");
                }
                if (parts.contains("notRepealed")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer.append(" (r.crq_requested_action is null) ");
                }
                queryBuffer.append(" ) AND ( ");
                first = true;
                if (parts.contains("transferred")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }

                    if (enrollmentOffice.getType().equals(
                            EnrollmentOfficeType.NOCR)) {
                        queryBuffer
                                .append(" (r.crq_enroll_office_id = :departmentId and r.crq_original_enroll_office_id is not null) ");
                    } else {
                        queryBuffer
                                .append(" (r.crq_original_enroll_office_id = :departmentId) ");
                    }
                }
                if (parts.contains("notTransferred")) {
                    if (!first) {
                        queryBuffer.append(" OR ");
                    } else {
                        first = false;
                    }
                    queryBuffer
                            .append(" (r.crq_enroll_office_id = :departmentId and r.crq_original_enroll_office_id is null) ");
                }
                queryBuffer.append(" ) ) ");
            }

            // 2- criteria filter
            if (parts.contains("firstName")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }
                queryBuffer.append(" (ct.ctz_national_id = :nationalId) ");
            }
//			if (parts.contains("notMES")) {
//				if (!firstAND) {
//					queryBuffer.append(" AND ");
//				} else {
//					firstAND = false;
//				}
//				queryBuffer.append(" (r.crq_origin not in ('M') and r.CRQ_ESTELAM2_FLAG = 'V') ");
//			}

            // 3-fetch earch cartable & sum their data
            boolean selectCartables = false;
            if ((cartableType & 1) == 1 || (cartableType & 1024) == 1024
                    || (cartableType & 2048) == 2048
                    || (cartableType & 16) == 16 || (cartableType & 2) == 2
                    || (cartableType & 4) == 4 || (cartableType & 8) == 8
                    || (cartableType & 64) == 64 || (cartableType & 128) == 128) {
                selectCartables = true;
            }

            if (selectCartables) {

                if (!firstAND) {
                    queryBuffer.append(" AND ");
                } else {
                    firstAND = false;
                }

                boolean cartableOR = true;
                queryBuffer.append(" ( ");

                // first filter todayReservation pastReservation
                // futureReservation
                if ((cartableType & 1) == 1 || (cartableType & 1024) == 1024
                        || (cartableType & 2048) == 2048) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    boolean first = true;
                    queryBuffer.append(" ((r.crq_state='RESERVED') AND ( ");
                    if (parts.contains("todayReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" (trunc(r.crq_rsv_date)=trunc(sysdate)) ");
                    }
                    if (parts.contains("pastReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" (trunc(r.crq_rsv_date) between trunc(current_date - :reservationRange) and trunc(current_date - 1)) ");
                    }
                    if (parts.contains("futureReservation")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer

                                .append(" (trunc(r.crq_rsv_date) between trunc(current_date + 1) and trunc(current_date + :reservationRange)) ");

                    }
                    queryBuffer.append(" )) ");
                }

                // second filter readyToAuth
                if ((cartableType & 16) == 16) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" ((r.crq_state='REFERRED_TO_CCOS') AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // third filter readyToScan Face Finger
                if ((cartableType & 2) == 2 || (cartableType & 4) == 4
                        || (cartableType & 8) == 8) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    boolean first = true;
                    queryBuffer.append(" (( ");
                    if (parts.contains("readyToScan")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) ) ) ");
                    }
                    if (parts.contains("readyToFing")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) ) ) ");
                    }
                    if (parts.contains("readyToFace")) {
                        if (!first) {
                            queryBuffer.append(" OR ");
                        } else {
                            first = false;
                        }
                        queryBuffer
                                .append(" ( (r.crq_state='DOCUMENT_AUTHENTICATED') AND ( ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0) OR ");
                        queryBuffer
                                .append(" (bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4) ) ) ");
                    }

                    queryBuffer.append(" ) AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // forth filter readyToApprove
                if ((cartableType & 64) == 64) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" ((r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4) AND ( ");
                    queryBuffer
                            .append(" (trunc(r.crq_enrolled_date)>=trunc(sysdate-30)) OR ");
                    queryBuffer
                            .append(" (r.crq_reenrolled_date between (current_date - 30) and current_date))) ");
                }

                // fifth filter underIssuance
                if ((cartableType & 128) == 128) {

                    if (!cartableOR) {
                        queryBuffer.append(" OR ");
                    } else {
                        cartableOR = false;
                    }

                    queryBuffer
                            .append(" (r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED') ");
                }

                queryBuffer.append(" ) ");
            } else {
                return 0;
            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            Number number = (Number) query.getSingleResult();
            if (number != null) {
                return number.intValue();
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_080,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }

    @Override
    public List<CitizenWTO> fetchCCOSDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        List<CitizenWTO> result = new ArrayList<CitizenWTO>();
        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select requestId,citizenId,firstname,lastname,nationalId,trackingId,enrollmentDate,state,requestType,batchCode,hasVipImage ")
                    .append("from ")
                    .append("( ")
                    .append("select ")
                    .append("ct.ctz_first_name_fa as firstname, ")
                    .append("ct.ctz_surname_fa as lastname, ")
                    .append("ct.ctz_national_id as nationalId, ")
                    .append("r.crq_tracking_id as trackingId, ")
                    .append("r.crq_enrolled_date as enrollmentDate, ")
                    .append("r.crq_id as requestId, ")
                    .append("ct.ctz_id as citizenId, ")
                    .append("r.crq_type as requestType, ")
                    .append("(case when r.crq_origin= 'V' then 1 else 0 end) hasVipImage, ")
                    .append("bb.bat_cms_id batchCode, ")
                    .append("(case ")
                    .append("when r.crq_enrolled_date is null then 1 ")
                    .append("when r.crq_state='REFERRED_TO_CCOS' then 2 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 3 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 4 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 5 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 6 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 7 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 8 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 9 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 10 ")
                    .append("when r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED' then 11 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state ='REVOKED' then 12 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state='LOST' then 13 ")
                    .append("else 0 ").append("end) state ")
                    .append("from emst_card_request r ")
                    .append("inner join emst_citizen ct ")
                    .append("on r.crq_citizen_id = ct.ctz_id ")
                    .append("inner join emst_citizen_info cinf ")
                    .append("on cinf.czi_id = ct.ctz_id ")
                    .append("inner join emst_card cc ")
                    .append("on cc.crd_id = r.crq_card_id ")
                    .append("inner join emst_batch bb ")
                    .append("on cc.crd_batch_id = bb.bat_id ")
                    .append("where  ")
                    /*
                     * .append("r.crq_delivered_office_id = :departmentId ")
                     * .append("and ")
                     */.append("r.crq_state = 'DELIVERED' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            // EnrollmentOfficeTO enrollmentOffice = criteria
            // .getEnrollmentOffice();
            // int cartableType = criteria.getCartableType();

            // 2- criteria filter
            if (parts.contains("firstName")) {
                queryBuffer
                        .append(" AND (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {
                queryBuffer.append(" AND (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {
                queryBuffer.append(" AND (ct.ctz_national_id = :nationalId) ");
            }
            if (parts.contains("trackingId")) {
                queryBuffer.append(" AND (r.crq_tracking_id = :trackingId) ");
            }
            if (parts.contains("enrollmentDate")) {

                queryBuffer
                        .append(" AND (r.crq_enrolled_date between to_date(:fromEnrollmentDate, 'YYYY/MM/DD HH24:MI') and to_date(:toEnrollmentDate, 'YYYY/MM/DD HH24:MI')) ");
            }
            if (parts.contains("birthDate")) {
                queryBuffer
                        .append(" AND (trunc(cinf.czi_birth_date_greg) = to_date(:birthDate, 'YYYY/MM/DD')) ");
            }

            queryBuffer.append(" ) where state in (12,13,0) ");

            if (EmsUtil.checkString(criteria.getOrderBy())) {
                String orderBy = criteria.getOrderBy();
                String sortKey = "requestId";
                String dir = "asc";
                String[] split = orderBy.split(" ");
                if (split.length >= 2) {
                    sortKey = split[0].trim();
                    dir = split[1].trim();
                }

                if ("firstNameFA".equals(sortKey)) {
                    sortKey = "firstname";
                } else if ("sureNameFA".equals(sortKey)) {
                    sortKey = "lastname";
                } else if ("trackingId".equals(sortKey)) {
                    sortKey = "trackingId";
                } else if ("nationalId".equals(sortKey)) {
                    sortKey = "nationalId";
                } else if ("enrolledDate".equals(sortKey)) {
                    sortKey = "enrollmentDate";
                } else if ("state".equals(sortKey)) {
                    sortKey = "state";
                } else if ("cmsBatchID".equals(sortKey)) {
                    sortKey = "batchCode";
                }
                queryBuffer.append(" order by ").append(sortKey).append(" ")
                        .append(dir).append(",requestId asc");
            } else {
                queryBuffer.append(" order by requestId asc ");
            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            query.setMaxResults(criteria.getPageSize()).setFirstResult(
                    criteria.getPageNo() * criteria.getPageSize());

            List resultList = query.getResultList();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    CitizenWTO obj = new CitizenWTO();
                    obj.setRequestId(((BigDecimal) data[0]).longValue());
                    obj.setId(((BigDecimal) data[1]).longValue());
                    obj.setFirstNameFA((String) data[2]);
                    obj.setSureNameFA((String) data[3]);
                    obj.setNationalId((String) data[4]);
                    obj.setTrackingId((String) data[5]);
                    obj.setEnrolledDate((Timestamp) data[6]);
                    obj.setState(((BigDecimal) data[7]).longValue());
                    obj.setStringType((String) data[8]);
                    obj.setCmsBatchID((String) data[9]);
                    obj.setHasVipImage(((BigDecimal) data[10]).toString());
                    result.add(obj);
                }
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_081,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return result;
    }

    @Override
    public Integer countCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
            throws BaseException {
        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select count(requestId) ")
                    .append("from ")
                    .append("( ")
                    .append("select ")
                    .append("r.crq_id as requestId, ")
                    .append("(case ")
                    .append("when r.crq_enrolled_date is null then 1 ")
                    .append("when r.crq_state='REFERRED_TO_CCOS' then 2 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 3 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 4 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 5 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 6 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 7 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 8 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 9 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 10 ")
                    .append("when r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED' then 11 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state ='REVOKED' then 12 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state='LOST' then 13 ")
                    .append("else 0 ").append("end) state ")
                    .append("from emst_card_request r ")
                    .append("inner join emst_citizen ct ")
                    .append("on r.crq_citizen_id = ct.ctz_id ")
                    .append("inner join emst_citizen_info cinf ")
                    .append("on cinf.czi_id = ct.ctz_id ")
                    .append("inner join emst_card cc ")
                    .append("on cc.crd_id = r.crq_card_id ")
                    .append("inner join emst_batch bb ")
                    .append("on cc.crd_batch_id = bb.bat_id ")
                    .append("where  ")
                    /*
                     * .append("r.crq_delivered_office_id = :departmentId ")
                     * .append("and ")
                     */.append("r.crq_state = 'DELIVERED' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            // EnrollmentOfficeTO enrollmentOffice = criteria
            // .getEnrollmentOffice();
            // int cartableType = criteria.getCartableType();

            // 2- criteria filter
            if (parts.contains("firstName")) {
                queryBuffer
                        .append(" AND (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {
                queryBuffer.append(" AND (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {
                queryBuffer.append(" AND (ct.ctz_national_id = :nationalId) ");
            }
            if (parts.contains("trackingId")) {
                queryBuffer.append(" AND (r.crq_tracking_id = :trackingId) ");
            }
            if (parts.contains("enrollmentDate")) {

                queryBuffer
                        .append(" AND (r.crq_enrolled_date between to_date(:fromEnrollmentDate, 'YYYY/MM/DD HH24:MI') and to_date(:toEnrollmentDate, 'YYYY/MM/DD HH24:MI')) ");
            }
            if (parts.contains("birthDate")) {
                queryBuffer
                        .append(" AND (trunc(cinf.czi_birth_date_greg) = to_date(:birthDate, 'YYYY/MM/DD')) ");
            }

            queryBuffer.append(" ) where state in (12,13,0) ");

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            Number number = (Number) query.getSingleResult();
            if (number != null) {
                return number.intValue();
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_082,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }

    @Override
    public List<CitizenWTO> fetchCCOSReadyToDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        List<CitizenWTO> result = new ArrayList<CitizenWTO>();
        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select requestId,citizenId,firstname,lastname,nationalId,trackingId,enrollmentDate,state,requestType,batchCode,hasVipImage ")
                    .append("from ")
                    .append("( ")
                    .append("select ")
                    .append("ct.ctz_first_name_fa as firstname, ")
                    .append("ct.ctz_surname_fa as lastname, ")
                    .append("ct.ctz_national_id as nationalId, ")
                    .append("r.crq_tracking_id as trackingId, ")
                    .append("r.crq_enrolled_date as enrollmentDate, ")
                    .append("r.crq_id as requestId, ")
                    .append("ct.ctz_id as citizenId, ")
                    .append("r.crq_type as requestType, ")
                    .append("(case when r.crq_origin= 'V' then 1 else 0 end) hasVipImage, ")
                    .append("bb.bat_cms_id batchCode, ")
                    .append("(case ")
                    .append("when r.crq_enrolled_date is null then 1 ")
                    .append("when r.crq_state='REFERRED_TO_CCOS' then 2 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 3 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 4 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 5 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 6 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 7 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 8 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 9 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 10 ")
                    .append("when r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED' then 11 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state ='REVOKED' then 12 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state='LOST' then 13 ")
                    .append("else 0 ").append("end) state ")
                    .append("from emst_card_request r ")
                    .append("inner join emst_citizen ct ")
                    .append("on r.crq_citizen_id = ct.ctz_id ")
                    .append("inner join emst_card cc ")
                    .append("on cc.crd_id = r.crq_card_id ")
                    .append("inner join emst_batch bb ")
                    .append("on cc.crd_batch_id = bb.bat_id ")
                    .append("where  ")
                    .append("r.crq_delivered_office_id = :departmentId ")
                    .append("and ").append("r.crq_state = 'READY_TO_DELIVER' ")
                    .append("and  ").append("cc.crd_lost_date is null ")
                    .append("and ").append("bb.bat_state = 'RECEIVED' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            // EnrollmentOfficeTO enrollmentOffice = criteria
            // .getEnrollmentOffice();
            // int cartableType = criteria.getCartableType();

            // 2- criteria filter
            if (parts.contains("firstName")) {
                queryBuffer
                        .append(" AND (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {
                queryBuffer.append(" AND (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {
                queryBuffer.append(" AND (ct.ctz_national_id = :nationalId) ");
            }
            if (parts.contains("trackingId")) {
                queryBuffer.append(" AND (r.crq_tracking_id = :trackingId) ");
            }
            if (parts.contains("enrollmentDate")) {

                queryBuffer
                        .append(" AND (r.crq_enrolled_date between to_date(:fromEnrollmentDate, 'YYYY/MM/DD HH24:MI') and to_date(:toEnrollmentDate, 'YYYY/MM/DD HH24:MI')) ");
            }

            queryBuffer.append(" ) where state = 0 ");

            if (EmsUtil.checkString(criteria.getOrderBy())) {
                String orderBy = criteria.getOrderBy();
                String sortKey = "requestId";
                String dir = "asc";
                String[] split = orderBy.split(" ");
                if (split.length >= 2) {
                    sortKey = split[0].trim();
                    dir = split[1].trim();
                }

                if ("firstNameFA".equals(sortKey)) {
                    sortKey = "firstname";
                } else if ("sureNameFA".equals(sortKey)) {
                    sortKey = "lastname";
                } else if ("trackingId".equals(sortKey)) {
                    sortKey = "trackingId";
                } else if ("nationalId".equals(sortKey)) {
                    sortKey = "nationalId";
                } else if ("enrolledDate".equals(sortKey)) {
                    sortKey = "enrollmentDate";
                } else if ("state".equals(sortKey)) {
                    sortKey = "state";
                } else if ("cmsBatchID".equals(sortKey)) {
                    sortKey = "batchCode";
                }
                queryBuffer.append(" order by ").append(sortKey).append(" ")
                        .append(dir).append(",requestId asc");
            } else {
                queryBuffer.append(" order by requestId asc ");
            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            query.setMaxResults(criteria.getPageSize()).setFirstResult(
                    criteria.getPageNo() * criteria.getPageSize());

            List resultList = query.getResultList();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    CitizenWTO obj = new CitizenWTO();
                    obj.setRequestId(((BigDecimal) data[0]).longValue());
                    obj.setId(((BigDecimal) data[1]).longValue());
                    obj.setFirstNameFA((String) data[2]);
                    obj.setSureNameFA((String) data[3]);
                    obj.setNationalId((String) data[4]);
                    obj.setTrackingId((String) data[5]);
                    obj.setEnrolledDate((Timestamp) data[6]);
                    obj.setState(((BigDecimal) data[7]).longValue());
                    obj.setStringType((String) data[8]);
                    obj.setCmsBatchID((String) data[9]);
                    obj.setHasVipImage(((BigDecimal) data[10]).toString());
                    result.add(obj);
                }
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_083,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return result;
    }

    @Override
    public Integer countCCOSReadyToDeliverCartableCardRequests(
            CCOSCriteria criteria) throws BaseException {
        try {
            StringBuffer queryBuffer = new StringBuffer(
                    "select count(requestId) ")
                    .append("from ")
                    .append("( ")
                    .append("select ")
                    .append("r.crq_id as requestId, ")
                    .append("(case ")
                    .append("when r.crq_enrolled_date is null then 1 ")
                    .append("when r.crq_state='REFERRED_TO_CCOS' then 2 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 3 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 4 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=0 then 5 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 6 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=0 and bitand(r.crq_flag,4)=4 then 7 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=0 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 8 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=0 then 9 ")
                    .append("when r.crq_state='DOCUMENT_AUTHENTICATED' and bitand(r.crq_flag,1)=1 and bitand(r.crq_flag,2)=2 and bitand(r.crq_flag,4)=4 then 10 ")
                    .append("when r.crq_state='APPROVED' or r.crq_state='SENT_TO_AFIS' or r.crq_state='APPROVED_BY_AFIS' or r.crq_state='PENDING_ISSUANCE' or r.crq_state='ISSUED' then 11 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state ='REVOKED' then 12 ")
                    .append("when r.crq_state='DELIVERED' and cc.crd_state='LOST' then 13 ")
                    .append("else 0 ").append("end) state ")
                    .append("from emst_card_request r ")
                    .append("inner join emst_citizen ct ")
                    .append("on r.crq_citizen_id = ct.ctz_id ")
                    .append("inner join emst_card cc ")
                    .append("on cc.crd_id = r.crq_card_id ")
                    .append("inner join emst_batch bb ")
                    .append("on cc.crd_batch_id = bb.bat_id ")
                    .append("where  ")
                    .append("r.crq_delivered_office_id = :departmentId ")
                    .append("and ").append("r.crq_state = 'READY_TO_DELIVER' ")
                    .append("and  ").append("cc.crd_lost_date is null ")
                    .append("and ").append("bb.bat_state = 'RECEIVED' ");

            Set<String> parts = criteria.getParts();
            Map<String, Object> parameters = criteria.getParameters();
            // EnrollmentOfficeTO enrollmentOffice = criteria
            // .getEnrollmentOffice();
            // int cartableType = criteria.getCartableType();

            // 2- criteria filter
            if (parts.contains("firstName")) {
                queryBuffer
                        .append(" AND (ct.ctz_first_name_fa like :firstName) ");
            }
            if (parts.contains("lastName")) {
                queryBuffer.append(" AND (ct.ctz_surname_fa like :lastName) ");
            }
            if (parts.contains("nationalId")) {
                queryBuffer.append(" AND (ct.ctz_national_id = :nationalId) ");
            }
            if (parts.contains("trackingId")) {
                queryBuffer.append(" AND (r.crq_tracking_id = :trackingId) ");
            }
            if (parts.contains("enrollmentDate")) {

                queryBuffer
                        .append(" AND (r.crq_enrolled_date between to_date(:fromEnrollmentDate, 'YYYY/MM/DD HH24:MI') and to_date(:toEnrollmentDate, 'YYYY/MM/DD HH24:MI')) ");
            }

            queryBuffer.append(" ) where state = 0 ");

            Query query = em.createNativeQuery(queryBuffer.toString());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            Number number = (Number) query.getSingleResult();
            if (number != null) {
                return number.intValue();
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_084,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getRequestIdsForUpdateState(Integer fetchLimit) {

        List<Long> ids = new ArrayList<Long>();

        StringBuffer queryBuffer = new StringBuffer();
//		queryBuffer
//		.append("SELECT /* INDEX(IX_CRQ_LAST_MODIFIED_DATE) */ CRQ.CRQ_ID FROM EMST_CARD_REQUEST CRQ WHERE ");
//		queryBuffer
//		.append(" (CRQ.CRQ_LAST_SYNC_DATE is NULL OR CRQ.CRQ_LAST_MODIFIED_DATE > CRQ.CRQ_LAST_SYNC_DATE) ");
//		queryBuffer.append(" AND CRQ.CRQ_PORTAL_REQUEST_ID is NOT NULL ");
//		queryBuffer
//		.append(" AND (CRQ.CRQ_LOCK_DATE is null or CRQ.CRQ_LOCK_DATE <= (sysdate - INTERVAL '600' minute)) ");


        List<Integer> flagRange = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        queryBuffer.append("select CRQ_ID FROM EMST_CARD_REQUEST where CRQ_SYNC_FLAG in (:flagRange) order by CRQ_SYNC_FLAG asc");

        Query query = em.createNativeQuery(queryBuffer.toString()).setParameter("flagRange", flagRange);
        List<BigDecimal> result = query.setMaxResults(fetchLimit)
                .getResultList();

        if (EmsUtil.checkListSize(result)) {
            for (BigDecimal b : result) {
                ids.add(b.longValue());
            }
        }
        return ids;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SyncCardRequestWTO> getRequestListForUpdateState(
            List<Long> requestIds) {
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer
                .append("select New com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO(portalRequestId, state, enrollmentOffice.id, metadata, originalCardRequestOfficeId) from CardRequestTO where id in (:ids)");
        Query query = em.createQuery(queryBuffer.toString(),
                SyncCardRequestWTO.class);
        query.setParameter("ids", requestIds);
        List<SyncCardRequestWTO> result = query.getResultList();
        return result == null ? new ArrayList<SyncCardRequestWTO>() : result;
    }

    // Anbari
    @Override
    public void addRequestedSmsForNotification(Long cardRequestId,
                                               SMSTypeState state) throws BaseException {


        String smsBody = null;
        switch (state) {
            case DOCUMENT_AUTHENTICATED:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_DOCUMENT_AUTHENTICATED,
                        DEFAULT_SMS_BODY);
                break;
            case REPEALED_FIRST_CARD:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_REPEALED_FIRST_CARD,
                        DEFAULT_SMS_BODY);
                break;
            case REPEALED_OTHERS:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_REPEALED_OTHERS,
                        DEFAULT_SMS_BODY);
                break;

            case IMS_DELETE_FACE:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_DELETE_IMAGE,
                        DEFAULT_SMS_BODY);
                break;
            case IMS_DELETE_DOC:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_DELETE_DOC,
                        DEFAULT_SMS_BODY);
                break;
            case IMS_DELETE_FINGER:
                smsBody = EmsUtil.getProfileValue(
                        ProfileKeyName.KEY_SMS_BODY_DELETE_FINGER,
                        DEFAULT_SMS_BODY);
                break;
            default:
                break;
        }
        try {
            StringBuilder sql = new StringBuilder(
                    "call fill_outgoing_refer_sms(:cardRequestId, '" + smsBody
                            + "')");
            em.createNativeQuery(sql.toString())
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_085,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }


    @Override
    public void readyEstelam2Flag(List<Long> cardRequestTOIdsForUpdate)
            throws BaseException {
        try {
            em.createQuery(
                    "UPDATE CardRequestTO crs "
                            + "SET crs.estelam2Flag = :READY, crs.requestedSmsStatus = :status "
                            + "WHERE crs.id IN (:ID_LIST)")
                    .setParameter("READY", Estelam2FlagType.R)
                    .setParameter("ID_LIST", cardRequestTOIdsForUpdate)
                    .setParameter("READY", Estelam2FlagType.R)
                    .setParameter("status", 0)
                    .setParameter("ID_LIST", cardRequestTOIdsForUpdate)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(INTEGRITY_CONSTRAINT_CRQ_CARD_REQUEST_STATUS))
                throw new DAOException(DataExceptionCode.CDI_044,
                        DataExceptionCode.CDI_042_MSG, e);

            throw new DataException(DataExceptionCode.CDI_012,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

//	// ganjyar
//	// get all requests' ids for do getEstelam2
//	@Override
//	public List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException {
//        try {
//
//        	List<CardRequestState> stateList = Arrays.asList(CardRequestState.RESERVED, CardRequestState.REFERRED_TO_CCOS, CardRequestState.DOCUMENT_AUTHENTICATED);
//
//            return em.createQuery("" +
//                    "SELECT CRQ.id FROM CardRequestTO CRQ " +
//                    "WHERE (CRQ.estelam2Flag = :readyFlag OR CRQ.estelam2Flag = :notVerifiedFlag) AND CRQ.state in (:states) ORDER BY CRQ.priority DESC, CRQ.reservationDate ASC" , Long.class)
//                    .setParameter("readyFlag",Estelam2FlagType.R)
//                    .setParameter("notVerifiedFlag",Estelam2FlagType.N)
//                    .setParameter("states",stateList)
//                    .setMaxResults(limit)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.CDI_086, DataExceptionCode.GLB_005_MSG, e);
//        }
//	}

    //Anbari:Estelam3(change IMS Estelam job Fetch acording to reservation perid)
    @Override
    public List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException {
        try {

            Integer durationRSVtoFetchUp = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_UP, DEFAULT_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_UP));
            Integer durationRSVtoFetchDown = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_DOWN, DEFAULT_DURATION_OF_IMS_ONLINE_RESERVATION_TO_FETCH_DOWN));
            Date TIME_INTERVAL_UP = EmsUtil.getDateAtMidnight(EmsUtil.differDay(new Date(), durationRSVtoFetchUp));
            Date TIME_INTERVAL_DOWN = EmsUtil.getDateAtMidnight(EmsUtil.differDay(new Date(), 0 - durationRSVtoFetchDown));
            List<CardRequestState> stateList = Arrays.asList(CardRequestState.RESERVED, CardRequestState.REFERRED_TO_CCOS, CardRequestState.DOCUMENT_AUTHENTICATED);

            List<Long> longList = em.createQuery("" +
                    "SELECT CRQ.id FROM CardRequestTO CRQ " +
                    "WHERE (CRQ.estelam2Flag = :readyFlag OR CRQ.estelam2Flag = :notVerifiedFlag) AND CRQ.state in (:states) AND ( :time_interval_down < CRQ.reservationDate) AND (CRQ.reservationDate < :time_interval_up) ORDER BY CRQ.priority DESC, CRQ.reservationDate ASC", Long.class)
                    .setParameter("readyFlag", Estelam2FlagType.R)
                    .setParameter("notVerifiedFlag", Estelam2FlagType.N)
                    .setParameter("states", stateList)
                    .setParameter("time_interval_up", TIME_INTERVAL_UP)
                    .setParameter("time_interval_down", TIME_INTERVAL_DOWN)
                    .setMaxResults(limit)
                    .getResultList();
            if (EmsUtil.checkListSize(longList)) {
                return longList;
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_086, DataExceptionCode.GLB_005_MSG, e);
        }
    }


    // Madanipour
    @Override
    public List<Long> findRequestsIdByState(CardRequestState cardRequestState, Integer fetchLimit)
            throws BaseException {
        try {
            List<Long> longList = em
                    .createQuery(
                            "" + "SELECT CRQ.id "
                                    + "FROM CardRequestTO CRQ "
                                    + "WHERE CRQ.state =:CARD_REQUEST_STATE ",
                            Long.class)
                    .setParameter("CARD_REQUEST_STATE", cardRequestState)
                    .setMaxResults(fetchLimit)
                    .getResultList();
            if (EmsUtil.checkListSize(longList)) {
                return longList;
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_087,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<CardRequestTO> findNextBatchDeliveredCRQFromIdBeforeDate(long id, Calendar cal, int batchSize) throws BaseException {

        try {
            return em.createQuery("SELECT CRQ FROM CardRequestTO CRQ WHERE CRQ.state = :state and CRQ.id > :lastId and CRQ.enrolledDate < :date ORDER BY CRQ.id ASC", CardRequestTO.class).setParameter("lastId", id).setParameter("state", CardRequestState.DELIVERED).setParameter("date", cal.getTime()).setMaxResults(batchSize).getResultList();
        } catch (Exception ex) {
            throw new DataException(DataExceptionCode.CDI_100, DataExceptionCode.GLB_005_MSG, ex);
        }
    }

    @Override
    public void refreshCRQ(CardRequestTO crq) throws BaseException {
        try {
            em.refresh(crq);
        } catch (Exception ex) {
            throw new DataException(DataExceptionCode.CDI_089, DataExceptionCode.CDI_089_MSG, ex);
        }
    }

    // Madanipour
    @Override
    public List<Long> getCardRequestForSubstituteAndDeleteEOF(Long eofID)
            throws BaseException {

        try {
            return em
                    .createQuery("SELECT crq.id FROM CardRequestTO crq where crq.enrollmentOffice.id = :eofID ",
                            Long.class).setParameter("eofID", eofID)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_090,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }

    //Anbari
    public void updateCardRequestEOFdeliverId(Long officeEOFID, Long supperiorOfficeId) throws BaseException {
        try {
            em.createQuery("UPDATE CardRequestTO cr "
                    + "SET cr.deliveredOfficeId = :supperiorOfficeId "
                    + "WHERE cr.deliveredOfficeId = :officeEOFID ")
                    .setParameter("officeEOFID", officeEOFID)
                    .setParameter("supperiorOfficeId", supperiorOfficeId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_062,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }


    //Madanipour
    @Override
    public void addRequestedSmsForReadyToDeliverReq(Long cardRequestId) throws BaseException {
        String smsBody = null;

        smsBody = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_SMS_BODY_READY_TO_DELIVER,
                DEFAULT_SMS_BODY);

        try {
            StringBuilder sql = new StringBuilder(
                    "call FILL_OUTGOING_CARD_READY_SMS(:cardRequestId, '" + smsBody
                            + "')");
            em.createNativeQuery(sql.toString())
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_091,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }

    //Madanipour
    @Override
    public List<Long> getCardRequestsByBatchID(Long batchId) throws BaseException {
        try {
            return em
                    .createQuery(
                            "SELECT crq.id FROM CardRequestTO crq  "
                                    + "where crq.state = :state "
                                    + "and crq.card.id in (select crd.id from CardTO crd where crd.batch.id = :batchID and crd.receiveDate is not null) ",
                            Long.class)
                    .setParameter("state", CardRequestState.READY_TO_DELIVER)
                    .setParameter("batchID", batchId).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_092,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }

    //Madanipour
    @Override
    public List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,
                                           Integer dayInterval) throws BaseException {
        try {
            return em
                    .createQuery(
                            "SELECT crq.id FROM CardRequestTO crq  "
                                    + "where crq.state in ('RESERVED', 'REFERRED_TO_CCOS', 'DOCUMENT_AUTHENTICATED') "
                                    + "and crq.reservationDate <= :specifiedDate  "
                                    + "and trunc(crq.reservationDate) > trunc(sysdate) "
                                    + "and crq.requestedSmsStatus = 0 "
                                    + "and crq.estelam2Flag = 'V' ",
                            Long.class)
                    .setParameter("specifiedDate", EmsUtil.differDay(new Date(), dayInterval))
                    .setMaxResults(numberOfRequestToFetch).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_093,

                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public void addRequestedSmsForReservedReq(Long cardRequestId)
            throws BaseException {

        String smsBody = null;

        smsBody = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_SMS_BODY_RESERVED_REQUEST, DEFAULT_SMS_BODY);

        try {
            StringBuilder sql = new StringBuilder(
                    "call FILL_OUTGOING_RESERVED_REQ_SMS(:cardRequestId, '"
                            + smsBody + "')");
            em.createNativeQuery(sql.toString())
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_094,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }

    @Override
    public void updateCardRequestRequestedSmsStatus(Long cardRequestId)
            throws BaseException {

        try {

            em.createQuery(
                    ""
                            + "UPDATE CardRequestTO CRQ "
                            + "SET CRQ.requestedSmsStatus = :requestedSmsStatus "
                            + "WHERE CRQ.id = :cardRequestId")
                    .setParameter("requestedSmsStatus", 1)
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_097,
                    DataExceptionCode.GLB_005_MSG, e);
        }

    }


    /**
     * this method fetches all citizen ids which are ready to purge.
     *
     * @author ganjyar
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getCitizenIdsForPurgeBioAndDocs(Integer fetchLimit)
            throws BaseException {

        try {
//			Integer dayInterval = Integer.valueOf(EmsUtil.getProfileValue(
//					ProfileKeyName.KEY_PURGE_BIO_TIME_INTERVAL,
//					DEFAULT_PURGE_BIO_TIME_INTERVAL));
            String purgeIntervalUp = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_INTERVAL_PURGE_UP, DEFAULT_INTERVAL_PURGE_UP));
            String purgeIntervalDown = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_INTERVAL_PURGE_DOWN, DEFAULT_INTERVAL_PURGE_DOWN));

            Date upDate = DateUtil.convert(DateUtil.convert(purgeIntervalUp, DateUtil.JALALI, DateUtil.GREGORIAN), DateUtil.GREGORIAN);
            Date downDate = DateUtil.convert(DateUtil.convert(purgeIntervalDown, DateUtil.JALALI, DateUtil.GREGORIAN), DateUtil.GREGORIAN);

            Date TIME_INTERVAL_UP = EmsUtil.getDateAtMidnight(upDate);
            Date TIME_INTERVAL_DOWN = EmsUtil.getDateAtMidnight(downDate);

            List<BigDecimal> results = em
                    .createNativeQuery(
                            "select distinct crq1.CRQ_CITIZEN_ID from "
                                    + "EMST_CARD_REQUEST crq1, "
                                    + "EMST_CARD crd1, "
                                    + "EMST_CITIZEN ctz  "
                                    + "where "
                                    + "crq1.CRQ_CARD_ID=crd1.CRD_ID  "
                                    + "and crq1.CRQ_CITIZEN_ID=ctz.CTZ_ID  "
                                    + "and crq1.CRQ_STATE='DELIVERED'  "
//									+ "and crd1.CRD_DELIVER_DATE<:specifiedDate "
                                    + "and (:time_interval_down < crd1.CRD_DELIVER_DATE) AND (crd1.CRD_DELIVER_DATE < :time_interval_up )  "
                                    + "and ctz.CTZ_PURGE_BIO!= :purgeBioState  "
                                    + "and ( "
                                    + "crq1.CRQ_CITIZEN_ID not in  ( "
                                    + "select "
                                    + "crq2.CRQ_CITIZEN_ID  "
                                    + "from "
                                    + "EMST_CARD_REQUEST crq2 left join "
                                    + "EMST_CARD crd2  "
                                    + "on "
                                    + "crq2.CRQ_CARD_ID=crd2.CRD_ID  "
                                    + "where ( "
                                    + "crq1.CRQ_CITIZEN_ID=crq2.CRQ_CITIZEN_ID  "
                                    + "and ( "
                                    + "crq2.CRQ_STATE<>'DELIVERED'  "
                                    + "or (crq2.CRQ_STATE='DELIVERED'  "
                                    + "and ((:time_interval_down > crd2.CRD_DELIVER_DATE) OR (crd2.CRD_DELIVER_DATE > :time_interval_up )) )) "
//									+ "and crd2.CRD_DELIVER_DATE>=:specifiedDate)) "
                                    + ") " + ") " + ") ")
                    .setParameter("purgeBioState", Boolean.TRUE)
//					.setParameter("specifiedDate",
//							EmsUtil.differDay(new Date(), -dayInterval))
                    .setParameter("time_interval_up", TIME_INTERVAL_UP)
                    .setParameter("time_interval_down", TIME_INTERVAL_DOWN)
                    .setMaxResults(fetchLimit).getResultList();

            List<Long> resultList = new ArrayList<Long>();
            if (EmsUtil.checkListSize(results)) {
                for (BigDecimal result : results) {

                    resultList.add(result.longValue());

                }
            }

            return resultList;


        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_098,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }


    @Override
    public CardRequestTO findCardRequestStateByTrackingId(
            String trackingId) throws DAOException {
        try {
            List<CardRequestTO> cardRequestTOList =
                    em.createNamedQuery("CardRequestTO.findCardRequestStateByTrackingId")
                            .setParameter("trackingId", trackingId)
                            .getResultList();
            return cardRequestTOList.size() > 0 ? cardRequestTOList.get(0) : null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_106,
                    DataExceptionCode.CDI_106_MSG, e);
        }
    }

    @Override
    public CardRequestTO findCardRequestStateByNationalIdAndMobile(
            String nationalId, String mobile) throws DAOException {
        try {
            List<CardRequestTO> cardRequestTOList =
                    em.createNamedQuery("CardRequestTO.findCardRequestStateByNationalIdAndMobile")
                            .setParameter("nationalId", nationalId)
                            .setParameter("mobile", mobile)
                            .getResultList();
            return cardRequestTOList.size() > 0 ? cardRequestTOList.get(0) : null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_103,
                    DataExceptionCode.CDI_103_MSG, e);
        }
    }

    @Override
    public CardRequestTO findCardRequestStateByNationalIdAndBirthCertificateSeries(
            String nationalId, String birthCertificateSeries) throws DAOException {
        try {
            List<CardRequestTO> cardRequestTOList =
                    em.createNamedQuery(
                            "CardRequestTO.findCardRequestStateByNationalIdAndBirthCertificateSeries")
                            .setParameter("nationalId", nationalId)
                            .setParameter("birthCertificateSeries", birthCertificateSeries)
                            .getResultList();
            return cardRequestTOList.size() > 0 ? cardRequestTOList.get(0) : null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_104,
                    DataExceptionCode.CDI_104_MSG, e);
        }
    }

    @Override
    public CardRequestTO findCardRequestStateByNationalId(String nationalId) throws DAOException {
        try {
            List<CardRequestTO> cardRequestTOList =
                    em.createNamedQuery("CardRequestTO.findCardRequestStateByNationalId")
                            .setParameter("nationalId", nationalId)
                            .getResultList();
            return cardRequestTOList.size() > 0 ? cardRequestTOList.get(0) : null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CDI_101, DataExceptionCode.CDI_101_MSG, e);
        }
    }

    @Override
    public CardRequestTO findByCitizenId(CitizenTO ctz)
            throws BaseException {
        List<CardRequestTO> cardRequestTO;
        try {
            cardRequestTO = em.createNamedQuery("CardRequestTO.findByCitizen")
                    .setParameter("citizenId", ctz.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CDI_003,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return cardRequestTO.size() != 0 ? cardRequestTO.get(0) : null;
    }

    @Override
    public CardRequestTO findLastRequestByNationalId(String nationalId)
            throws BaseException {
        List<CardRequestTO> cardRequestTOList;
        try {
            nationalId = StringUtils.leftPad(nationalId, 10, "0");
            cardRequestTOList = em.createNamedQuery("CardRequestTO.findLastRequestByNationalId")
                    .setParameter("nationalId", nationalId)
                    .getResultList();
        } catch (Exception e) {
            logger.error(DataExceptionCode.CDI_107_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new DataException(DataExceptionCode.CDI_107,
                    DataExceptionCode.CDI_107_MSG, e);
        }
        if (!cardRequestTOList.isEmpty())
            return cardRequestTOList.get(0);
        else
            return null;
    }

    @Override
    public Long countCardRequestByNationalIdAndType(String nationalId, CardRequestType cardRequestType) throws BaseException {
        Long replicaTypeCount;
        try {
            Query query = em.createQuery(
                    "select count(*) " +
                            "from CardRequestTO crq " +
                            "where crq.citizen.nationalID=:NATIONALID " +
                            "and crq.type=:TYPE");
            query.setParameter("NATIONALID", nationalId);
            query.setParameter("TYPE", cardRequestType);
            replicaTypeCount = (Long) query.getSingleResult();
        } catch (Exception e) {
            logger.error(DataExceptionCode.CDI_108_MSG, new Object[]{"nationalId", String.valueOf(nationalId)});
            throw new DataException(DataExceptionCode.CDI_108,
                    DataExceptionCode.CDI_108_MSG, e);
        }
        return replicaTypeCount;
    }

    public String nextValueOfRequestTrackingId() throws BaseException {
        try {
            return em.createNativeQuery("SELECT SEQ_CARD_REQUEST_TRACKING_ID.NEXTVAL FROM DUAL").getSingleResult().toString();
        } catch (Exception e) {
            logger.error("error in get next value of database-sequence for generating trackingId:" + e.getMessage());
            throw new DataException(DataExceptionCode.CDI_109,
                    DataExceptionCode.CDI_109_MSG, e);
        }
    }

}
