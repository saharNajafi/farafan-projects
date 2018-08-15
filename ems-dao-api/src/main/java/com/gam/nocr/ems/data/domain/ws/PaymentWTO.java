package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/6/18.
 */
public class PaymentWTO extends SecureWTO implements Serializable {
    private long preRegistrationId;
    private RegistrationPaymentWTO registrationPaymentWTO;
    private String nationalId;

    public long getPreRegistrationId() {
        return preRegistrationId;
    }

    public void setPreRegistrationId(long preRegistrationId) {
        this.preRegistrationId = preRegistrationId;
    }

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
