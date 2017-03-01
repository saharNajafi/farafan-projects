package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.enums.ReportOutputType;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface ReportFileDAO extends EmsBaseDAO<ReportFileTO> {

    /**
     * The method findReportFilesByReportId is used to find a list of type {@link ReportFileTO}, regarding to the parameter
     * of reportId
     *
     * @param reportId is an instance of type {@link Long}, which represents the id belongs to an appropriate instance of
     *                 type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @return a list of type {@link ReportFileTO}
     * @throws BaseException
     */
    List<ReportFileTO> findReportFilesByReportId(Long reportId) throws BaseException;

    /**
     * The method findReportFilesByReportIdAndOutputType is used to fetch a list of type {@link ReportFileTO},
     * regards to its reportId and requestType
     *
     * @param reportId is an instance of type {@link Long}, which represents an appropriate report
     * @param type     is an instance of type {@link ReportOutputType}
     * @return a list of type {@link ReportFileTO}
     * @throws BaseException
     */
    List<ReportFileTO> findReportFilesByReportIdAndOutputType(Long reportId,
                                                              ReportOutputType type) throws BaseException;
}
