package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.sql.Timestamp;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class CardInfo {

    private String requestId;
    private String crn;
    private Timestamp issuanceDate;
    private Timestamp shipmentDate;
    private String keySetVersion;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    @JSON(include = false)
    public Timestamp getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Timestamp issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @JSON(include = false)
    public Timestamp getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Timestamp shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    @JSON(include = false)
    public String getKeySetVersion() {
        return keySetVersion;
    }

    public void setKeySetVersion(String keySetVersion) {
        this.keySetVersion = keySetVersion;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
