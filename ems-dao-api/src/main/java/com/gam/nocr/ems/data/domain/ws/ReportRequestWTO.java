package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.data.enums.ReportRequestState;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class ReportRequestWTO {

    private Timestamp requestDate;
    private ReportRequestState state;
    private Long reportId;
    private Timestamp generateDate; // TimeStamp
    private ReportOutputType type;
    private byte[] result;
    private String detail;  // Error Result
    private String parameters;
    private Long personId;

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public ReportRequestState getState() {
        return state;
    }

    public void setState(ReportRequestState state) {
        this.state = state;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Timestamp getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Timestamp generateDate) {
        this.generateDate = generateDate;
    }

    public ReportOutputType getType() {
        return type;
    }

    public void setType(ReportOutputType type) {
        this.type = type;
    }

    public byte[] getResult() {
        return result == null ? result : result.clone();
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
