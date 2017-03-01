package com.gam.nocr.ems.data.domain.ws;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.sql.Timestamp;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class CardDispatchInfoWTO extends ExtEntityTO {

    private String firstNameFA;
    private String sureNameFA;
    private String nationalId;
    private String cardSerialNum;
    private Timestamp cardReceiveDate;
    private Timestamp cardLostDate;
    private String cmsBatchId;

    private String status;

    public String getFirstNameFA() {
        return firstNameFA;
    }

    public void setFirstNameFA(String firstNameFA) {
        this.firstNameFA = firstNameFA;
    }

    public String getSureNameFA() {
        return sureNameFA;
    }

    public void setSureNameFA(String sureNameFA) {
        this.sureNameFA = sureNameFA;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getCardSerialNum() {
        return cardSerialNum;
    }

    public void setCardSerialNum(String cardSerialNum) {
        this.cardSerialNum = cardSerialNum;
    }

    public Timestamp getCardReceiveDate() {
        return cardReceiveDate;
    }

    public void setCardReceiveDate(Timestamp cardReceiveDate) {
        this.cardReceiveDate = cardReceiveDate;
    }

    public Timestamp getCardLostDate() {
        return cardLostDate;
    }

    public void setCardLostDate(Timestamp cardLostDate) {
        this.cardLostDate = cardLostDate;
    }

    public String getCmsBatchId() {
        return cmsBatchId;
    }

    public void setCmsBatchId(String cmsBatchId) {
        this.cmsBatchId = cmsBatchId;
    }

    public String getStatus() {
        if (getCardReceiveDate() == null && getCardLostDate() == null)
            setStatus("0");
        else if (getCardReceiveDate() != null && getCardLostDate() == null)
            setStatus("1");
        else if (getCardReceiveDate() == null && getCardLostDate() != null)
            setStatus("2");
        else if (getCardReceiveDate() != null && getCardLostDate() != null)
            setStatus("3");

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
