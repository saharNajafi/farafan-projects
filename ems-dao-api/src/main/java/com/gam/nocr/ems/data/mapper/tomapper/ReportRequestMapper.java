package com.gam.nocr.ems.data.mapper.tomapper;

import java.sql.Timestamp;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.domain.ws.ReportRequestWTO;
import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.data.enums.ReportRequestState;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class ReportRequestMapper {

    private ReportRequestMapper() {
    }

    public static ReportRequestTO convert(ReportRequestVTO reportRequestVTO) throws BaseException {
        ReportRequestTO reportRequestTO = new ReportRequestTO();
        reportRequestTO.setRequestDate(reportRequestVTO.getRequestDate());
        reportRequestTO.setState(ReportRequestState.valueOf(reportRequestVTO.getRequestState()));
        reportRequestTO.setType(ReportOutputType.valueOf(reportRequestVTO.getRequestOutputType()));
        reportRequestTO.setParameters(reportRequestVTO.getParameters());
        return reportRequestTO;
    }

    public static ReportRequestVTO convert(ReportRequestWTO reportRequestWTO) throws BaseException {
        ReportRequestVTO reportRequestVTO = new ReportRequestVTO();

//        if (reportRequestWTO.getRequestDate() != null)
//            reportRequestVTO.setRequestDate(DateUtil.convert(reportRequestWTO.getRequestDate(), DateUtil.JALALI));
//        reportRequestVTO.setRequestState(reportRequestWTO.getState().name());
        reportRequestVTO.setReportId(reportRequestWTO.getReportId().toString());
        reportRequestVTO.setRequestOutputType(reportRequestWTO.getType().name());
        reportRequestVTO.setParameters(reportRequestWTO.getParameters());

        return reportRequestVTO;
    }

    public static ReportRequestWTO convert(ReportRequestTO reportRequestTO) throws BaseException {
        ReportRequestWTO reportRequestWTO = new ReportRequestWTO();

        reportRequestWTO.setRequestDate((Timestamp) reportRequestTO.getRequestDate());
        reportRequestWTO.setState(reportRequestTO.getState());
        reportRequestWTO.setReportId(reportRequestTO.getReportTO().getId());
        reportRequestWTO.setGenerateDate((Timestamp) reportRequestTO.getGenerateDate());
        reportRequestWTO.setType(reportRequestTO.getType());
        reportRequestWTO.setResult(reportRequestTO.getResult());
        reportRequestWTO.setDetail(reportRequestTO.getDetail());
        reportRequestWTO.setParameters(reportRequestTO.getParameters());
        reportRequestWTO.setPersonId(reportRequestTO.getPerson().getId());

        return reportRequestWTO;
    }
}
