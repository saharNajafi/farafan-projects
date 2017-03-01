package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.enums.ReportRequestState;
import com.gam.nocr.ems.data.enums.SystemId;
import org.json.JSONObject;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface ReportRequestService extends Service {

    Long save(ReportRequestVTO reportRequestVTO) throws BaseException;

    void remove(Long[] reportRequestIds) throws BaseException;

    /**
     * The method generateRequestedReportsByJob is used to generate the report, which belong to appropriate report
     * requests, via a specified job
     *
     * @throws BaseException
     */
    Long getRequestedReportByJobCount() throws BaseException;

    void generateRequestedReportsByJob(Integer from, Integer to) throws BaseException;

    /**
     * The method loadGeneratedReport is used to load the report belonging to an appropriate request, which is in the
     * state
     * of ReportRequestState.Processed
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link ReportRequestTO}
     * @return an instance of type {@link ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    ReportRequestTO loadGeneratedReport(Long reportRequestId) throws BaseException;

    /**
     * This method is used for testing the process of generating report
     *
     * @param reportRequestTO
     * @throws BaseException
     */
    void generateRequestedReportTest(ReportRequestTO reportRequestTO) throws BaseException;

    /**
     * The method generateRequestedReport is used to generate the requested report
     *
     * @param reportRequestId is an instance of type {@link ReportRequestTO}
     * @return an instance of type {@link ReportRequestTO}
     * @throws BaseException
     */
    ReportRequestTO generateRequestedReport(Long reportRequestId) throws BaseException;

    /**
     * The methods updateReportRequest is used to update an instance of type {@link ReportRequestTO}
     *
     * @param reportRequestTO is an instance of type {@link ReportRequestTO}
     * @throws BaseException
     */
    void updateReportRequest(ReportRequestTO reportRequestTO) throws BaseException;

    /**
     * The method updateState is used to update the state of an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     *
     * @param reportRequestTO    is an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @param reportRequestState is an instance of type {@link ReportRequestState}
     * @throws com.gam.commons.core.BaseException
     */
    void updateState(ReportRequestTO reportRequestTO,
                     ReportRequestState reportRequestState) throws BaseException;

    void validateReport(Long reportId,
                        String userName,
                        SystemId systemId,
                        JSONObject receivedParam) throws BaseException;

}
