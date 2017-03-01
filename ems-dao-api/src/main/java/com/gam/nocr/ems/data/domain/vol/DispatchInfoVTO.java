package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DispatchInfoVTO extends ExtEntityTO {

    private String cmsId;
    private Long containerId;
    private String receivedType;
    private String sendType;
    private Integer itemCount;
    private Timestamp dispatchSentDate;
    private Timestamp dispatchReceiveDate;
    private Timestamp dispatchLostDate;
    private Timestamp mySendDate;
    private Long nextReceiverId;
    private String nextReceiverName;
    private Timestamp nextReceiverReceiveDate;
    private String status;

    public DispatchInfoVTO() {
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    @JSON(include = false)
    public String getReceivedType() {
        return receivedType;
    }

    public void setReceivedType(String receivedType) {
        this.receivedType = receivedType;
    }

    @JSON(include = false)
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @JSON(include = false)
    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @JSON(include = false)
    public Timestamp getDispatchSentDate() {
        return dispatchSentDate;
    }

    public void setDispatchSentDate(Timestamp dispatchSentDate) {
        this.dispatchSentDate = dispatchSentDate;
    }

    @JSON(include = false)
    public Timestamp getDispatchReceiveDate() {
        return dispatchReceiveDate;
    }

    public void setDispatchReceiveDate(Timestamp dispatchReceiveDate) {
        this.dispatchReceiveDate = dispatchReceiveDate;
    }

    @JSON(include = false)
    public Timestamp getDispatchLostDate() {
        return dispatchLostDate;
    }

    public void setDispatchLostDate(Timestamp dispatchLostDate) {
        this.dispatchLostDate = dispatchLostDate;
    }

    @JSON(include = false)
    public Timestamp getMySendDate() {
        return mySendDate;
    }

    public void setMySendDate(Timestamp mySendDate) {
        this.mySendDate = mySendDate;
    }

    @JSON(include = false)
    public Long getNextReceiverId() {
        return nextReceiverId;
    }

    public void setNextReceiverId(Long nextReceiverId) {
        this.nextReceiverId = nextReceiverId;
    }

    @JSON(include = false)
    public String getNextReceiverName() {
        return nextReceiverName;
    }

    public void setNextReceiverName(String nextReceiverName) {
        this.nextReceiverName = nextReceiverName;
    }

    @JSON(include = false)
    public Timestamp getNextReceiverReceiveDate() {
        return nextReceiverReceiveDate;
    }

    public void setNextReceiverReceiveDate(Timestamp nextReceiverReceiveDate) {
        this.nextReceiverReceiveDate = nextReceiverReceiveDate;
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
