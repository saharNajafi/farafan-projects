package com.gam.nocr.ems.data.domain.ws;

import java.util.Date;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class OutgoingSMSWTO {

    private Date requestDate;
    private String cellNo;
    private String messageBody;
    private Byte priority;
    private Integer retryLimit;
    private Integer retryDuration;

    /**
     * @return the requestDate
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the cellNo
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * @param cellNo the cellNo to set
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * @return the priority
     */
    public Byte getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    /**
     * @return the retryLimit
     */
    public Integer getRetryLimit() {
        return retryLimit;
    }

    /**
     * @param retryLimit the retryLimit to set
     */
    public void setRetryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
    }

    /**
     * @return the retryDuration
     */
    public Integer getRetryDuration() {
        return retryDuration;
    }

    /**
     * @param retryDuration the retryDuration to set
     */
    public void setRetryDuration(Integer retryDuration) {
        this.retryDuration = retryDuration;
    }
}
