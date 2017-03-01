package com.gam.nocr.ems.data.domain;

import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_DISPATCH_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_DISPATCH_INFO", allocationSize = 1)
public class DispatchInfoTO extends CardContainerTO {

    private Long containerId;
    private DepartmentDispatchSendType containerType;
    private Long senderDepartmentId;
    private Timestamp SendDate;
    private Long receiverDepartmentId;
    private Timestamp receiveDate;
    private Timestamp lostDate;
    private DispatchInfoTO dispatchParentId;
    private Timestamp detailReceiveDate;
    private Timestamp detailLostDate;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "DPI_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "DPI_CONTAINER_ID", nullable = false)
    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "DPI_CONTAINER_TYPE")
    public DepartmentDispatchSendType getContainerType() {
        return containerType;
    }

    public void setContainerType(DepartmentDispatchSendType containerType) {
        this.containerType = containerType;
    }

    @Column(name = "DPI_SENDER_DEP_ID", nullable = false)
    public Long getSenderDepartmentId() {
        return senderDepartmentId;
    }

    public void setSenderDepartmentId(Long senderDepartmentId) {
        this.senderDepartmentId = senderDepartmentId;
    }

    @Column(name = "DPI_SEND_DATE")
    @JSON(include = false)
    public Timestamp getSendDate() {
        return SendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        SendDate = sendDate;
    }

    @Column(name = "DPI_RECEIVER_DEP_ID", nullable = false)
    @JSON(include = false)
    public Long getReceiverDepartmentId() {
        return receiverDepartmentId;
    }

    public void setReceiverDepartmentId(Long receiverDepartmentId) {
        this.receiverDepartmentId = receiverDepartmentId;
    }

    @Column(name = "DPI_RECEIVE_DATE")
    @JSON(include = false)
    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Column(name = "DPI_LOST_DATE")
    @JSON(include = false)
    public Timestamp getLostDate() {
        return lostDate;
    }

    public void setLostDate(Timestamp lostDate) {
        this.lostDate = lostDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DPI_PARENT_ID")
    @JSON(include = false)
    public DispatchInfoTO getDispatchParentId() {
        return dispatchParentId;
    }

    public void setDispatchParentId(DispatchInfoTO dispatchParentId) {
        this.dispatchParentId = dispatchParentId;
    }

    @Column(name = "DPI_DETAIL_RECEIVE_DATE")
    @JSON(include = false)
    public Timestamp getDetailReceiveDate() {
        return detailReceiveDate;
    }

    public void setDetailReceiveDate(Timestamp detailReceiveDate) {
        this.detailReceiveDate = detailReceiveDate;
    }

    @Column(name = "DPI_DETAIL_LOST_DATE")
    @JSON(include = false)
    public Timestamp getDetailLostDate() {
        return detailLostDate;
    }

    public void setDetailLostDate(Timestamp detailLostDate) {
        this.detailLostDate = detailLostDate;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
