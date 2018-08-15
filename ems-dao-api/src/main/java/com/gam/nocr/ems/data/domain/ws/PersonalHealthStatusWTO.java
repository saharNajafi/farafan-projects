package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/3/18.
 */
public class PersonalHealthStatusWTO extends SecureWTO implements Serializable {
    private HealthStatusWTO healthStatusWTO;
    private String nationalId;
    private Long enrollmentOfficeId;

    public HealthStatusWTO getHealthStatusWTO() {
        return healthStatusWTO;
    }

    public void setHealthStatusWTO(HealthStatusWTO healthStatusWTO) {
        this.healthStatusWTO = healthStatusWTO;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }
}
