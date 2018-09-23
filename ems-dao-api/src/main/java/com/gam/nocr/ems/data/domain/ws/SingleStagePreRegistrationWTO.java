package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by safiary on 6/2/18.
 */
public class SingleStagePreRegistrationWTO implements Serializable {

    private String trackingId;
    private Boolean verifiedByIMS;
    private Long cardRequestId;

    public SingleStagePreRegistrationWTO() {
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public void setVerifiedByIMS(Boolean verifiedByIMS) {
        this.verifiedByIMS = verifiedByIMS;
    }

    public Boolean getVerifiedByIMS() {
        return verifiedByIMS;
    }

    public void setCardRequestId(Long cardRequestId) {
        this.cardRequestId = cardRequestId;
    }

    public Long getCardRequestId() {
        return cardRequestId;
    }
}
