/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.IPGProviderEnum;
import com.gam.nocr.ems.data.enums.PaymentTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author sahar
 */
@Entity
@Table(name = "EMST_REGISTRATION_PAYMENT")
@NamedQueries({
        @NamedQuery( name = "RegistrationPayment.findByCitizenId"
                , query = " select rp from RegistrationPaymentTO rp" +
                " where rp.citizenTO.id =:citizenId"),
        @NamedQuery( name = "RegistrationPayment.findLastCardRequestPaymentByNationalId"
                , query = "select crq.registrationPaymentTO from CardRequestTO crq  where crq.id " +
                "= (select MAX(crqq.id) from CardRequestTO crqq" +
                " where crqq.citizen.nationalID=:nationalId)")

})
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_REGISTRATION_PAYMENT", allocationSize = 1)
public class RegistrationPaymentTO extends ExtEntityTO implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private boolean isSucceed;
    private boolean isConfirmed;
    private IPGProviderEnum paidBank;
    private Date paymentDate;
    private String paymentCode;
    private Long orderId;
    private Integer amountPaid;
    private String rrn;
    private String systemTraceNo;
    private String description;
    private String resCode;
    private short matchFlag;
    private CitizenTO citizenTO;
    private String terminalCode;
    private String merchantCode;
    private PaymentTypeEnum paymentType;


    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "RPY_ID")
    public Long getId() {
        return super.getId();
    }

    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RPY_PAIED_BANK")
    public IPGProviderEnum getPaidBank() {
        return paidBank;
    }

    public void setPaidBank(IPGProviderEnum paidBank) {
        this.paidBank = paidBank;
    }

    @Basic(optional = false)
    @Column(name = "RPY_IS_CONFIRMED")
    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Basic(optional = false)
    @Column(name = "RPY_IS_SUCCEED")
    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    @Column(name = "RPY_PAYMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Column(name = "RPY_PAYMENT_CODE")
    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    @Basic(optional = false)
    @Column(name = "RPY_AMOUNT_PAIED")
    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    @Column(name = "RPY_RRN")
    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Column(name = "RPY_SYSTEM_TRACE_NO")
    public String getSystemTraceNo() {
        return systemTraceNo;
    }

    public void setSystemTraceNo(String systemTraceNo) {
        this.systemTraceNo = systemTraceNo;
    }

    @Column(name = "RPY_DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "RPY_RES_CODE")
    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    @Basic(optional = false)
    @Column(name = "RPY_MATCH_FLAG")
    public short getMatchFlag() {
        return matchFlag;
    }

    public void setMatchFlag(short matchFlag) {
        this.matchFlag = matchFlag;
    }

    @Basic(optional = false)
    @Column(name = "RPY_ORDER_ID")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "RPY_CITIZEN_ID")
    public CitizenTO getCitizenTO() {
        return citizenTO;
    }

    public void setCitizenTO(CitizenTO citizenTO) {
        this.citizenTO = citizenTO;
    }

    @Column(name = "RPY_TERMINAl_CODE", length = 50, columnDefinition = "VARCHAR2(50)")
    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    @Column(name = "RPY_MERCHANT_CODE", length = 50, columnDefinition = "VARCHAR2(50)")
    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RPY_PAYMENT_TYPE")
    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public RegistrationPaymentTO() {
    }

//    public RegistrationPaymentTO(Long rpyId) {
//        this.setId(rpyId);
//    }

//    public RegistrationPaymentTO(Long id, IPGProviderEnum paidBank, boolean isConfirmed, boolean isSucceed
//            , Integer amountPaid, Long orderId, short matchFlag) {
//        this.setId(id);
//        this.paidBank = paidBank;
//        this.isConfirmed = isConfirmed;
//        this.isSucceed = isSucceed;
//        this.amountPaid = amountPaid;
//        this.orderId = orderId;
//        this.matchFlag = matchFlag;
//    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistrationPaymentTO)) {
            return false;
        }
        RegistrationPaymentTO other = (RegistrationPaymentTO) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gam.nocr.ems.data.domain.RegistrationPaymentTO[ id=" + getId() + " ]";
    }

}
