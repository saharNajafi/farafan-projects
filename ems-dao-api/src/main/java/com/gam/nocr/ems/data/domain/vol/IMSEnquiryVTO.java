package com.gam.nocr.ems.data.domain.vol;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class IMSEnquiryVTO {

    Long requestId;
    String citizenInfo;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(String citizenInfo) {
        this.citizenInfo = citizenInfo;
    }
}
