package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.enums.ReportRequestState;
import com.gam.nocr.ems.data.domain.ReportRequestTO;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface ReportRequestDAO extends EmsBaseDAO<ReportRequestTO> {

    /**
     * The method findRequestsByState is used to find a number of request by means of a specified state
     *
     * @param reportRequestState is an instance of type {@link ReportRequestState}, which represents the state of the
     *                           requests to find
     * @return a list of type{@link ReportRequestTO}
     * @throws BaseException
     */
    List<ReportRequestTO> findRequestsByState(ReportRequestState reportRequestState) throws BaseException;

    Long getRequestsToGenerateReportByJobCount() throws BaseException;

    /**
     * The method findRequestsToGenerateReportByJob is used to find requested reports for generating via a specified job
     *
     * @return a list of type{@link ReportRequestTO}
     * @throws BaseException
     */
    List<ReportRequestTO> findRequestsToGenerateReportByJob(Integer from, Integer to) throws BaseException;

    /**
     * The method findRequestIdsToGenerateReportByJob is used to find report request ids for generating reports
     *
     * @return a list of type {@link Long}, which identifies a list of type {@link ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    public List<Long> findRequestIdsToGenerateReportByJob() throws BaseException;

    /**
     * The method updateRequestsState is used to update the state of the requests in a bulky way
     *
     * @param reportRequestIds is a list of type {@link Long}, which represents the ids of the report requests
     * @param oldState         is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}
     * @param newState         is an instance of type {@link com.gam.nocr.ems.data.enums.ReportRequestState}
     */
    void updateRequestsState(List<Long> reportRequestIds,
                             ReportRequestState oldState,
                             ReportRequestState newState) throws BaseException;

    /**
     * The method loadGeneratedReport is used to load the report belonging to an appropriate request, which is in the
     * state of ReportRequestState.Processed
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    ReportRequestTO loadGeneratedReport(Long reportRequestId) throws BaseException;

    /**
     * The method updateRequestsState is used to fulfill a bulk update regarding to states in the parameters
     *
     * @param currentState is an instance of type {@link ReportRequestState}, which represents the current state
     * @param newState     is an instance of type {@link ReportRequestState}, which represents the new state
     * @throws BaseException
     */
    void updateRequestsByState(ReportRequestState currentState,
                               ReportRequestState newState) throws BaseException;

    /**
     * The method findByReportIdAndStates is used to find instances of type {@link ReportRequestTO}, regards to the two parameters
     * of reportId and reportRequestStates
     *
     * @param reportId            is an instance of type {@link Long}, which is an identifier of an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @param reportRequestStates a list of type {@link ReportRequestState}
     * @return a list of type {@link ReportRequestTO}
     * @throws BaseException
     */
    List<ReportRequestTO> findByReportIdAndStates(Long reportId,
                                                  List<ReportRequestState> reportRequestStates) throws BaseException;

    /**
     * The method findRequestWithAllAttributes is used to find an instance of type {@link ReportRequestTO} along with all its attributes, regards to its id
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @return an instance of type {@link ReportRequestTO} or null
     * @throws BaseException
     */
    ReportRequestTO findRequestWithAllAttributes(Long reportRequestId) throws BaseException;

    /**
     * Given a list of identifiers of records in DB and removes them
     *
     * @param idList List of identifier of records in DB to be removed
     * @throws BaseException
     */
    void delete(Long[] idList) throws BaseException;
}
