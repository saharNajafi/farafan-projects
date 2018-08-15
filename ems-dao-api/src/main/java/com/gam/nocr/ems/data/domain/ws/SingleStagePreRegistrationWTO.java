package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by safiary on 6/2/18.
 */
public class SingleStagePreRegistrationWTO implements Serializable {

    private Long portalRequestId;
    private String trackingId;
    private String paymentCode;
    private Long orderId;
    private Boolean verifiedByIMS;

    public SingleStagePreRegistrationWTO() {
    }

    public Long getPortalRequestId() {
        return portalRequestId;
    }

    public void setPortalRequestId(Long portalRequestId) {
        this.portalRequestId = portalRequestId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public void setVerifiedByIMS(Boolean verifiedByIMS) {
        this.verifiedByIMS = verifiedByIMS;
    }

    public Boolean getVerifiedByIMS() {
        return verifiedByIMS;
    }
}
