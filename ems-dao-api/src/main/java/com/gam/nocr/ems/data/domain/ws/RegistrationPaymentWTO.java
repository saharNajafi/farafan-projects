package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.IPGProviderEnum;
import com.gam.nocr.ems.data.enums.YesNoEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sahar on 10/15/17.
 */
public class RegistrationPaymentWTO implements Serializable {
    private Long id;
    private IPGProviderEnum paiedBank;
    private boolean isConfirmed;
    private boolean isSucceed;
    private Date paymentDate;
    private String paymentCode;
    private Integer amountPaied;
    private long orderId;
    private String RRN;
    private String description;
    private String systemTraceNo;
    private String resCode;
    private YesNoEnum ignoreICAOPermitted;
    private YesNoEnum defineNMOCPermitted;

    public RegistrationPaymentWTO() {
    }

    public RegistrationPaymentWTO(Long id) {
        this.id = id;
    }

    public RegistrationPaymentWTO(Long id, IPGProviderEnum paiedBank, boolean isConfirmed,
                                  boolean isSucceed, Integer amountPaied, long orderId) {
        this.id = id;
        this.paiedBank = paiedBank;
        this.isConfirmed = isConfirmed;
        this.isSucceed = isSucceed;
        this.amountPaied = amountPaied;
        this.orderId = orderId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YesNoEnum getDefineNMOCPermitted() {
        return defineNMOCPermitted;
    }

    public void setDefineNMOCPermitted(YesNoEnum defineNMOCPermitted) {
        this.defineNMOCPermitted = defineNMOCPermitted;
    }

    public IPGProviderEnum getPaiedBank() {
        return paiedBank;
    }

    public void setPaiedBank(IPGProviderEnum paiedBank) {
        this.paiedBank = paiedBank;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Integer getAmountPaied() {
        return amountPaied;
    }

    public void setAmountPaied(Integer amountPaied) {
        this.amountPaied = amountPaied;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemTraceNo() {
        return systemTraceNo;
    }

    public void setSystemTraceNo(String systemTraceNo) {
        this.systemTraceNo = systemTraceNo;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public YesNoEnum getIgnoreICAOPermitted() {
        return ignoreICAOPermitted;
    }

    public void setIgnoreICAOPermitted(YesNoEnum ignoreICAOPermitted) {
        this.ignoreICAOPermitted = ignoreICAOPermitted;
    }
}
