package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "MSGT_OUTGOING_SMS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_OUTGING_SMS")
public class OutgoingSMSTO extends ExtEntityTO implements JSONable {

    private Timestamp requestDate;
    private String cellNo;
    private String messageBody;
    private Timestamp lastTryDate;
    private Integer retryCount;
    private Integer priority;
    private Integer retryLimit;
    private Integer retryDuration;
    private Timestamp sentDate;
    private String result;
    private Long sendSmsType;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "OSM_ID")
    @JSON(include = false)
    public Long getId() {
        return super.getId();
    }

    @Column(name = "OSM_REQUEST_DATE")
    @JSON(include = false)
    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    @Column(name = "OSM_CELL_NO", length = 12)
    @JSON(include = false)
    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    @Column(name = "OSM_MESSAGE_BODY", length = 140)
    @JSON(include = false)
    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Column(name = "OSM_LAST_TRY_DATE")
    @JSON(include = false)
    public Timestamp getLastTryDate() {
        return lastTryDate;
    }

    public void setLastTryDate(Timestamp lastTryDate) {
        this.lastTryDate = lastTryDate;
    }

    @Column(name = "OSM_RETRY_COUNT")
    @JSON(include = false)
    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Column(name = "OSM_PRIORITY")
    @JSON(include = false)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "OSM_RETRY_LIMIT")
    @JSON(include = false)
    public Integer getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
    }

    @Column(name = "OSM_RETRY_DURATION")
    @JSON(include = false)
    public Integer getRetryDuration() {
        return retryDuration;
    }

    public void setRetryDuration(Integer retryDuration) {
        this.retryDuration = retryDuration;
    }

    @Column(name = "OSM_SENT_DATE")
    @JSON(include = false)
    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    @Column(name = "OSM_RESULT")
    @JSON(include = false)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
 
    @Column(name = "OSM_TYPE")
    @JSON(include = false)
    public Long getSendSmsType() {
		return sendSmsType;
	}

	public void setSendSmsType(Long sendSmsType) {
		this.sendSmsType = sendSmsType;
	}

	@Override
    public String toString() {
        return "OutgoingSMSTO : { " +
                "\nid = " + getId() +
                "\n, requestDate = " + getRequestDate() +
                "\n, cellNo = " + getCellNo() +
                "\n, messageBody = " + getMessageBody() +
                "\n, lastTryDate = " + getLastTryDate() +
                "\n, retryCount = " + getRetryCount() +
                "\n, priority = " + getPriority() +
                "\n, retryLimit = " + getRetryLimit() +
                "\n, retryDuration = " + getRetryDuration() +
                "\n, sentDate = " + getSentDate() +
                "\n }";
    }

    @Override
    public String toJSON() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
