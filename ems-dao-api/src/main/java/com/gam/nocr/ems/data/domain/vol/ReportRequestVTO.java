package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportRequestVTO extends ExtEntityTO {

    private Timestamp requestDate;
    private Timestamp generateDate;
    private Timestamp jobScheduleDate;
    private String requestState;
    private String requestOutputType;
    private String parameters;

    private String reportId;
    private String reportName;

    private List<ReportFileVTO> reportFileVTOs = new ArrayList<ReportFileVTO>(0);

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public Timestamp getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Timestamp generateDate) {
        this.generateDate = generateDate;
    }

    public Timestamp getJobScheduleDate() {
        return jobScheduleDate;
    }

    public void setJobScheduleDate(Timestamp jobScheduleDate) {
        this.jobScheduleDate = jobScheduleDate;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getRequestOutputType() {
        return requestOutputType;
    }

    public void setRequestOutputType(String requestOutputType) {
        this.requestOutputType = requestOutputType;
    }

    public List<ReportFileVTO> getReportFileVTOs() {
        return reportFileVTOs;
    }

    public void setReportFileVTOs(List<ReportFileVTO> reportFileVTOs) {
        this.reportFileVTOs = reportFileVTOs;
    }
}
