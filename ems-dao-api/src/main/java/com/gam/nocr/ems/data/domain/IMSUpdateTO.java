package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.persistence.*;

/**
 * The IMSUpdateTO is a temporary TO which used for testing the IMS Stub and will be omitted on future
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Entity
@Table(name = "EMST_IMS_UPDATE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_IMS_UPDATE")
public class IMSUpdateTO extends ExtEntityTO {

    public enum State {
        PENDING_TO_PROCESS,
        PROCESSED,

    }

    private String requestId;
    private String data;
    private State state;
    private String nationalId;
    private byte[] citizenData;

    public IMSUpdateTO() {
    }

    public IMSUpdateTO(String data, State state) {
        this.data = data;
        this.state = state;
    }

    public IMSUpdateTO(String requestId,
                       String data,
                       byte[] citizenData,
                       State state,
                       String nationalId) {
        this.requestId = requestId;
        this.data = data;
        this.citizenData = citizenData;
        this.state = state;
        this.nationalId = nationalId;
    }

    public IMSUpdateTO(String data, byte[] citizenData, State state, String nationalId) {
        this.data = data;
        this.citizenData = citizenData;
        this.state = state;
        this.nationalId = nationalId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "IUT_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "IUT_REQUEST_ID", nullable = true)
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Lob
    @Column(name = "IUT_DATA", nullable = false)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "IUT_STATE")
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(name = "IUT_NATIONAL_ID")
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Lob
    @Column(name = "IUT_CITIZEN_DATA")
    @JSON(include = false)
    public byte[] getCitizenData() {
        return citizenData == null ? citizenData : citizenData.clone();
    }

    public void setCitizenData(byte[] citizenData) {
        this.citizenData = citizenData;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
