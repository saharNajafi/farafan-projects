package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WorkstationWTO {

    private Long id;
    private String code;
    private String activationCode;
    private Long enrollmentOfficeId;
    private String status;

    public WorkstationWTO() {
    }

    public WorkstationWTO(Long id, String code, String activationCode, Long enrollmentOfficeId, String status) {
        this.id = id;
        this.code = code;
        this.activationCode = activationCode;
        this.enrollmentOfficeId = enrollmentOfficeId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
