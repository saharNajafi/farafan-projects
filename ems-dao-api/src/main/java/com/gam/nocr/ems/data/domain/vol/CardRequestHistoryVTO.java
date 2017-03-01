package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.util.EmsUtil;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CardRequestHistoryVTO {

    private Long id;
    private String result;
    private Timestamp date;
    private String systemId;
    private String cmsRequestId;
    private String cardRequestId;
    private String action;
    private String actor;

    public CardRequestHistoryVTO() {
    }

    public CardRequestHistoryVTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getCmsRequestId() {
        return cmsRequestId;
    }

    public void setCmsRequestId(String cmsRequestId) {
        this.cmsRequestId = cmsRequestId;
    }

    public String getCardRequestId() {
        return cardRequestId;
    }

    public void setCardRequestId(String cardRequestId) {
        this.cardRequestId = cardRequestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
