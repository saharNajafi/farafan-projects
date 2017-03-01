package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportVTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.SystemId;

import java.util.Map;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface ReportManagementService extends Service {

    Long save(ReportVTO reportVTO) throws BaseException;

    Long update(ReportVTO reportVTO) throws BaseException;

    ReportVTO load(Long reportId) throws BaseException;

    boolean remove(String reportId) throws BaseException;

    /**
     * The method getVariables is used to prepare the variables, which belong to an appropriate report
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return an instance of type {@link java.util.Map <Object, Object>}, which represents the variables and their
     *         values
     * @throws BaseException
     */
    Map<Object, Object> getVariables(ReportTO reportTO) throws BaseException;

    /**
     * The method getParameters is used to prepare the parameters, which belong to an appropriate report
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return an instance of type {@link String}, which represents the parameters in a JSON String
     * @throws BaseException
     */
    String getParameters(ReportTO reportTO, SystemId systemId) throws BaseException;

    /**
     * The method getMetaData is used to prepare the metadata, such as parameters or ..., which belong to an appropriate report
     *
     * @param reportId is an instance of type {@link Long}, which represents the id of a specified instance of type {@link ReportTO}
     * @param username is an instance of type {@link String}
     * @return an instance of type {@link String}, which represents the parameters in a JSON String
     * @throws BaseException
     */
    String getMetaData(Long reportId,
                       String username,
                       SystemId systemId) throws BaseException;

    /**
     * The method getQueryString is used to extract the query string attribute from the report
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return an array of type {@link Object}, which represents the query and parameters belong to that query
     * @throws BaseException
     */
    Object[] getQueryString(ReportTO reportTO) throws BaseException;

    /**
     * Changes the state of given report. It would be used to enable/disable a report
     *
     * @param reportId       Identifier of the record to change its state
     * @param newReportState New state of the report
     */
    void changeReportState(Long reportId, BooleanType newReportState) throws BaseException;
}
