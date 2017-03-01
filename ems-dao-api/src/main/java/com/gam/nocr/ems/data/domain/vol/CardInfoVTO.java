package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class CardInfoVTO {

    private String citizenID;
    private String crn;
    private Date issuanceDate;
    private String productID;
    private String productVersion;
    private String reason;
    private String requestID;
    private int status;

    public CardInfoVTO(
            String citizenID,
            String crn,
            XMLGregorianCalendar issuanceDate,
            String productID,
            String productVersion,
            String reason,
            String requestID,
            int status) {

        this.citizenID = citizenID;
        this.crn = crn;
        this.issuanceDate = issuanceDate.toGregorianCalendar().getTime();
        this.productID = productID;
        this.productVersion = productVersion;
        this.reason = reason;
        this.requestID = requestID;
        this.status = status;

    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    @JSON(include = false)
    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @JSON(include = false)
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @JSON(include = false)
    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    @JSON(include = false)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
