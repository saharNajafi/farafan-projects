package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.data.enums.ReportScope;
import com.gam.nocr.ems.data.enums.ReportRequestState;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_REPORT_REQUEST")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_REPORT_REQUEST", allocationSize = 1)
public class ReportRequestTO extends ExtEntityTO {

    private Date requestDate;
    private ReportRequestState state;
    private ReportTO reportTO;
    private Date generateDate; // TimeStamp
    private Date jobScheduleDate; // TimeStamp
    private Date generateRetryDate; // TimeStamp
    private ReportOutputType type;
    private byte[] result;
    private String detail;  // Error Result
    private String parameters;
    private Integer tryCounter = 0;
    private ReportScope scope;
    private PersonTO person;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "RRQ_ID")
    public Long getId() {
        return super.getId();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RRQ_REQUEST_DATE")
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "RRQ_STATE")
    public ReportRequestState getState() {
        return state;
    }

    public void setState(ReportRequestState state) {
        this.state = state;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RRQ_REPORT_ID")
    public ReportTO getReportTO() {
        return reportTO;
    }

    public void setReportTO(ReportTO reportTO) {
        this.reportTO = reportTO;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RRQ_GENERATE_DATE")
    public Date getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RRQ_JOB_SCHEDULE_DATE")
    public Date getJobScheduleDate() {
        return jobScheduleDate;
    }

    public void setJobScheduleDate(Date jobScheduleDate) {
        this.jobScheduleDate = jobScheduleDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "RRQ_TYPE")
    public ReportOutputType getType() {
        return type;
    }

    public void setType(ReportOutputType type) {
        this.type = type;
    }

    @Lob
    @Column(name = "RRQ_RESULT")
    public byte[] getResult() {
        return result == null ? result : result.clone();
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    @Lob
    @Column(name = "RRQ_DETAIL")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Lob
    @Column(name = "RRQ_PARAMETERS")
    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Column(name = "RRQ_TRY_COUNTER")
    public Integer getTryCounter() {
        return tryCounter;
    }

    public void setTryCounter(Integer tryCounter) {
        this.tryCounter = tryCounter;
    }

    @ManyToOne
    @JoinColumn(name = "RRQ_PERSON_ID")
    public PersonTO getPerson() {
        return person;
    }

    public void setPerson(PersonTO personTO) {
        this.person = personTO;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RRQ_GENERATE_RETRY_DATE")
    public Date getGenerateRetryDate() {
        return generateRetryDate;
    }

    public void setGenerateRetryDate(Date lastGenerateRetryDate) {
        this.generateRetryDate = lastGenerateRetryDate;
    }
}
