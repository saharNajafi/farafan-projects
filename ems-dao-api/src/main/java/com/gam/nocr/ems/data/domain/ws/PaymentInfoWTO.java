package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by Saeid on 9/15/2018.
 */
public class PaymentInfoWTO implements Serializable {

    private Integer paymentAmount;
    private String paymentCode;
    private String orderId;

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
