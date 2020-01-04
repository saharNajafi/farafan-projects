package com.gam.nocr.ems.data.domain;

import com.gam.nocr.ems.data.enums.TokenReason;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.JSONable;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haeri (haeri@gamelectronics.com)
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_PERSON_TOKEN")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_PERSON_TOKEN", allocationSize = 1)
public class PersonTokenTO extends TokenTO implements JSONable {

    private TokenType type;
    private PersonTO person;
    private Date deliverDate;
    private String tokenState;
    private String tokenType;
    private TokenReason ptReason = TokenReason.FIRST_TOKEN;
    private String tokenReason;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "PTK_ID")
    public Long getId() {
        return super.getId();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "PTK_TYPE", nullable = false)
    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "PTK_REASON")
    public TokenReason getPtReason() {
        return ptReason;
    }

    public void setPtReason(TokenReason reason) {
        this.ptReason = reason;
    }

    @ManyToOne
    @JoinColumn(name = "PTK_PERSON_ID", nullable = false)
    public PersonTO getPerson() {
        return person;
    }

    public void setPerson(PersonTO person) {
        this.person = person;
    }

    @Override
    @Column(name = "PTK_REQUEST_ID", length = 32, nullable = true)
    public String getRequestID() {
        return super.getRequestID();
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "PTK_STATE", nullable = false)
    public TokenState getState() {
        return super.getState();
    }

    @Override
    @Column(name = "PTK_AKI", length = 40, nullable = true)
    public String getAKI() {
        return super.getAKI();
    }

    @Override
    @Column(name = "PTK_CERT_SN", length = 40, nullable = true)
    public String getCertificateSerialNumber() {
        return super.getCertificateSerialNumber();
    }

    @Override
    @Column(name = "PTK_REQUEST_DATE", nullable = false)
    public Date getRequestDate() {
        return super.getRequestDate();
    }

    @Override
    @Column(name = "PTK_ISSUANCE_DATE")
    public Date getIssuanceDate() {
        return super.getIssuanceDate();
    }

    @Transient
    public String getTokenState() {
        return tokenState;
    }

    public void setTokenState(String lState) {
        this.tokenState = lState;
    }

    @Transient
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String lType) {
        this.tokenType = lType;
    }

    @Column(name = "PTK_DELIVER_DATE")
    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
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
        String jsonObject = super.toString();
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (person == null) {
            jsonObject += ", personId:" + person;
        } else {
            jsonObject += ", personId:" + person.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }

    @Transient
    public String getTokenReason() {
        return tokenReason;
    }

    public void setTokenReason(String tokenReason) {
        this.tokenReason = tokenReason;
    }
}
