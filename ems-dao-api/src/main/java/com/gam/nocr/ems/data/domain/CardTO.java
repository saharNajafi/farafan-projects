package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CARD")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CARD", allocationSize = 1)
@NamedQueries(
        @NamedQuery( name = "CardTO.countCardLostDate"
                , query = " select count(*) from CardTO crd " +
                "where crd.batch.id = :batchId " +
                " and crd.lostDate is not null")
)
public class CardTO extends ExtEntityTO implements JSONable {

    private CardState state;
    private BatchTO batch;
    private String crn;
    private Timestamp issuanceDate;
    private Timestamp shipmentDate;
    private Timestamp receiveDate;
    private Timestamp lostDate;
    private Timestamp deliverDate;

    private String cmsRequestId;
    private Boolean isLostCardConfirmed;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CRD_ID")
    public Long getId() {
        return super.getId();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRD_STATE")
    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    @ManyToOne
    @JoinColumn(name = "CRD_BATCH_ID", nullable = false)
    @JSON(include = false)
    public BatchTO getBatch() {
        return batch;
    }

    public void setBatch(BatchTO batch) {
        this.batch = batch;
    }

    @Column(name = "CRD_CRN", length = 255)
    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    @Column(name = "CRD_ISSUANCE_DATE")
    public Timestamp getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Timestamp issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @Column(name = "CRD_SHIPMENT_DATE")
    public Timestamp getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Timestamp shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    @Column(name = "CRD_RECEIVE_DATE")
    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Column(name = "CRD_LOST_DATE")
    public Timestamp getLostDate() {
        return lostDate;
    }

    public void setLostDate(Timestamp lostDate) {
        this.lostDate = lostDate;
    }

    @Column(name = "CRD_DELIVER_DATE")
    public Timestamp getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Timestamp deliverDate) {
        this.deliverDate = deliverDate;
    }

    @Transient
    public String getCmsRequestId() {
        return cmsRequestId;
    }

    public void setCmsRequestId(String cmsRequestId) {
        this.cmsRequestId = cmsRequestId;
    }
    @Column(name = "CRD_LOSTCONFIRM")
    public Boolean getIsLostCardConfirmed() {
		return isLostCardConfirmed;
	}

	public void setIsLostCardConfirmed(Boolean isLostCardConfirmed) {
		this.isLostCardConfirmed = isLostCardConfirmed;
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

        if (batch == null) {
            jsonObject += "," + "batchId:" + batch;
        } else {
            jsonObject += "," + "batchId:" + batch.getId() +
                    "," + "batchCMSId:" + batch.getCmsID() +
                    "," + "batchState:" + batch.getState();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
