package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/6/18.
 */
public class PaymentWTO implements Serializable {
    private RegistrationPaymentWTO registrationPaymentWTO;
    private String nationalId;

    public RegistrationPaymentWTO getRegistrationPaymentWTO() {
        return registrationPaymentWTO;
    }

    public void setRegistrationPaymentWTO(RegistrationPaymentWTO registrationPaymentWTO) {
        this.registrationPaymentWTO = registrationPaymentWTO;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
}
