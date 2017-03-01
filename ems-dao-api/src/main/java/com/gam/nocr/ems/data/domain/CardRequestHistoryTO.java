package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CARD_REQUEST_HISTORY")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CARD_REQUEST_HISTORY", allocationSize = 1)
public class CardRequestHistoryTO extends ExtEntityTO implements JSONable {

    private CardRequestTO cardRequest;
    private String result;
    private Date date;
    private SystemId systemID;
    private String requestID;
    private CardRequestHistoryAction cardRequestHistoryAction;
    private String actor;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CRH_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "CRH_CARD_REQUEST_ID")
    @JSON(include = false)
    public CardRequestTO getCardRequest() {
        return cardRequest;
    }

    public void setCardRequest(CardRequestTO cardRequest) {
        this.cardRequest = cardRequest;
    }

    @Column(name = "CRH_RESULT", length = 255)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRH_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//	@Column(name = "CRH_SYSTEM_ID", length = 255)
//	public String getSystemID() {
//		return systemID;
//	}
//
//	public void setSystemID(String systemID) {
//		this.systemID = systemID;
//	}

    @Enumerated(EnumType.STRING)
    @Column(name = "CRH_SYSTEM_ID")
    public SystemId getSystemID() {
        return systemID;
    }

    public void setSystemID(SystemId systemID) {
        this.systemID = systemID;
    }


    @Column(name = "CRH_REQUEST_ID", length = 255)
    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRH_ACTION")
    public CardRequestHistoryAction getCardRequestHistoryAction() {
        return cardRequestHistoryAction;
    }

    public void setCardRequestHistoryAction(CardRequestHistoryAction cardRequestHistoryAction) {
        this.cardRequestHistoryAction = cardRequestHistoryAction;
    }

    @Column(name = "CRH_ACTOR")
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (cardRequest == null) {
            jsonObject += "," + "cardRequestId:" + cardRequest;
        } else {
            jsonObject += "," + "cardRequestId:" + cardRequest.getId() +
                    "," + "cardRequestTrackingID:" + "\"" + cardRequest.getTrackingID() + "\"" +
                    "," + "cardRequestReason:" + "\"" + cardRequest.getReason() + "\"" +
                    "," + "cardRequestPriority:" + cardRequest.getPriority();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
