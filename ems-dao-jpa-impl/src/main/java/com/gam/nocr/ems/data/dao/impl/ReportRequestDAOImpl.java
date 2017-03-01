package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.enums.ReportRequestState;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "ReportRequestDAO")
@Local(ReportRequestDAOLocal.class)
@Remote(ReportRequestDAORemote.class)
public class ReportRequestDAOImpl extends EmsBaseDAOImpl<ReportRequestTO> implements ReportRequestDAOLocal, ReportRequestDAORemote {

    private static final Logger logger = BaseLog.getLogger(ReportRequestDAOImpl.class);

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        super.setEm(em);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * The method findRequestsByState is used to find a number of request by means of a specified state
     *
     * @param reportRequestState is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}, which
     *                           represents the state of the
     *                           requests to find
     * @return a list of type{@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<ReportRequestTO> findRequestsByState(ReportRequestState reportRequestState) throws BaseException {
        try {
            List<ReportRequestTO> reportRequestTOs = em.createQuery(" " +
                    "SELECT RRQ FROM ReportRequestTO RRQ " +
                    "WHERE RRQ.state = :REPORT_REQUEST_STATE", ReportRequestTO.class)
                    .setParameter("REPORT_REQUEST_STATE", reportRequestState)
                    .getResultList();
            return reportRequestTOs;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public Long getRequestsToGenerateReportByJobCount() throws BaseException {
        String DEFAULT_TRY_COUNTER = "3";
        Integer tryCounter = Integer.parseInt(EmsUtil.getProfileValue(ProfileKeyName.KEY_REPORT_REQUEST_TRY_COUNTER, DEFAULT_TRY_COUNTER));

        List<Long> count;
        try {
            count = em.createQuery("" +
                    "SELECT count(RRQ.id) FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "(RRQ.state = :REQUESTED_STATE AND RRQ.jobScheduleDate IS NOT NULL AND RRQ.jobScheduleDate <= CURRENT_DATE)" +
                    "OR " +
                    "(RRQ.state = :IN_PROGRESSED_STATE AND RRQ.tryCounter <= :MAXIMUM_TRY_COUNTER AND RRQ.generateRetryDate < CURRENT_DATE - 1)", Long.class)
                    .setParameter("REQUESTED_STATE", ReportRequestState.REQUESTED)
                    .setParameter("IN_PROGRESSED_STATE", ReportRequestState.IN_PROGRESSED)
                    .setParameter("MAXIMUM_TRY_COUNTER", tryCounter)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_010, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(count))
            return count.get(0);
        else
            return null;
    }

    /**
     * The method findRequestsToGenerateReportByJob is used to find requested reports for generating via a specified job
     *
     * @return a list of type{@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<ReportRequestTO> findRequestsToGenerateReportByJob(Integer from, Integer to) throws BaseException {
        String DEFAULT_TRY_COUNTER = "3";
        Integer tryCounter = Integer.parseInt(EmsUtil.getProfileValue(ProfileKeyName.KEY_REPORT_REQUEST_TRY_COUNTER, DEFAULT_TRY_COUNTER));

        try {
            return em.createQuery("" +
                    "SELECT RRQ FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "(RRQ.state = :REQUESTED_STATE AND RRQ.jobScheduleDate IS NOT NULL AND RRQ.jobScheduleDate <= CURRENT_DATE)" +
                    "OR " +
                    "(RRQ.state = :IN_PROGRESSED_STATE AND RRQ.tryCounter <= :MAXIMUM_TRY_COUNTER AND RRQ.generateRetryDate < CURRENT_DATE - 1)", ReportRequestTO.class)
                    .setParameter("REQUESTED_STATE", ReportRequestState.REQUESTED)
                    .setParameter("IN_PROGRESSED_STATE", ReportRequestState.IN_PROGRESSED)
                    .setParameter("MAXIMUM_TRY_COUNTER", tryCounter)
                    .setFirstResult(from)
                    .setMaxResults(to)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_002, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findRequestIdsToGenerateReportByJob is used to find report request ids for generating reports
     *
     * @return a list of type {@link Long}, which identifies a list of type {@link ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<Long> findRequestIdsToGenerateReportByJob() throws BaseException {
        try {
            List<Long> reportRequestIds = em.createQuery("" +
                    "SELECT RRQ.id FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "RRQ.state = :REPORT_REQUEST_STATE AND " +
                    "RRQ.generateDate IS NOT NULL", Long.class)
                    .setParameter("REPORT_REQUEST_STATE", ReportRequestState.REQUESTED)
                    .getResultList();
            return reportRequestIds;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_003, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method updateRequestsState is used to update the state of the requests in a bulky way
     *
     * @param reportRequestIds is a list of type {@link Long}, which represents the ids of the report requests
     * @param oldState         is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}
     * @param newState         is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}
     */
    @Override
    public void updateRequestsState(List<Long> reportRequestIds,
                                    ReportRequestState oldState,
                                    ReportRequestState newState) throws BaseException {
        try {
            em.createQuery("" +
                    "UPDATE ReportRequestTO RRQ " +
                    "SET RRQ.state = :NEW_STATE " +
                    "WHERE RRQ.id IN (:ID_LIST) AND " +
                    "RRQ.state = :OLD_STATE")
                    .setParameter("NEW_STATE", newState)
                    .setParameter("ID_LIST", reportRequestIds)
                    .setParameter("OLD_STATE", oldState)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_004, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method loadGeneratedReport is used to load the report belonging to an appropriate request, which is in the
     * state of ReportRequestState.Processed
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public ReportRequestTO loadGeneratedReport(Long reportRequestId) throws BaseException {
        try {
            List<ReportRequestTO> reportRequestTOs = em.createQuery("" +
                    "SELECT RRQ " +
                    "FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "RRQ.id = :ID " +
                    "AND " +
                    "RRQ.state = :STATE", ReportRequestTO.class)
                    .setParameter("ID", reportRequestId)
                    .setParameter("STATE", ReportRequestState.PROCESSED)
                    .getResultList();
            if (EmsUtil.checkListSize(reportRequestTOs)) {

                //  Fetching some required attribute (before closing database session) that would be used by caller
                reportRequestTOs.get(0).getReportTO().getName();
                reportRequestTOs.get(0).getResult();
                return reportRequestTOs.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_005, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method updateRequestsState is used to fulfill a bulk update regarding to states in the parameters
     *
     * @param currentState is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}, which
     *                     represents the current state
     * @param newState     is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}, which
     *                     represents the new state
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void updateRequestsByState(ReportRequestState currentState, ReportRequestState newState) throws BaseException {
        try {
            em.createQuery("" +
                    "UPDATE ReportRequestTO RRQ " +
                    "SET RRQ.state = :NEW_STATE " +
                    "WHERE RRQ.state = :CURRENT_STATE")
                    .setParameter("NEW_STATE", newState)
                    .setParameter("CURRENT_STATE", currentState)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_006, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findByReportIdAndStates is used to find instances of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}, regards to the two parameters
     * of reportId and reportRequestStates
     *
     * @param reportId            is an instance of type {@link Long}, which is an identifier of an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @param reportRequestStates a list of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<ReportRequestTO> findByReportIdAndStates(Long reportId,
                                                         List<ReportRequestState> reportRequestStates) throws BaseException {
        try {
            return em.createQuery(" " +
                    "SELECT RRQ " +
                    "FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "RRQ.reportTO.id = :REPORT_ID " +
                    "AND " +
                    "RRQ.state IN (:STATES)", ReportRequestTO.class)
                    .setParameter("REPORT_ID", reportId)
                    .setParameter("STATES", reportRequestStates)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_007, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findRequestWithAllAttributes is used to find an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO} along with all its attributes, regards to its id
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO} or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public ReportRequestTO findRequestWithAllAttributes(Long reportRequestId) throws BaseException {
        try {
            List<ReportRequestTO> reportRequestTOs = em.createQuery("" +
                    "SELECT RRQ " +
                    "FROM ReportRequestTO RRQ " +
                    "WHERE " +
                    "RRQ.id = :ID ", ReportRequestTO.class)
                    .setParameter("ID", reportRequestId)
                    .getResultList();
            if (EmsUtil.checkListSize(reportRequestTOs)) {
                ReportRequestTO reportRequestTO = reportRequestTOs.get(0);
                ReportTO reportTO = reportRequestTO.getReportTO();
                reportTO.getName();
                reportTO.getReportFiles().size();
                return reportRequestTO;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_008, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * Due to some restrictions in oracle, using more than 1000 item in a list of "IN" statement is prohibited
     * so we have to split the query to multiple sub-query and execute each one separately (all in same transaction
     * context)
     *
     * @param idList List of identifier of records in DB to be removed
     * @throws BaseException
     */
    public void delete(Long[] idList) throws BaseException {
        try {
            if (idList.length <= 1000) {
                Query deleteQuery = em.createQuery(
                        "DELETE " +
                                "FROM ReportRequestTO RRQ " +
                                "WHERE " +
                                "RRQ.id IN :REPORT_ID_LIST ")
                        .setParameter("REPORT_ID_LIST", Arrays.asList(idList));
                deleteQuery.executeUpdate();
            } else {
                //  TODO: This exception has to be removed whenever someone implements removing more than 10000 records
                throw new DAOException(DataExceptionCode.RRI_009, DataExceptionCode.GLB_007_MSG);
            }

        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RRI_009, DataExceptionCode.GLB_007_MSG, e);
        }
    }
}
