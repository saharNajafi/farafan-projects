package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * Created by sahar on 10/15/17.
 */
public class OfficeAppointmentWTO implements Serializable {
    private long id;
    private long preRegistrationId;
    private Integer appointmentDate;
    private long nationalId;
    private RegistrationOfficeWTO registrationOffice;
    private ActiveShiftWTO activeShift;

    public OfficeAppointmentWTO() {
    }

    public OfficeAppointmentWTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPreRegistrationId() {
        return preRegistrationId;
    }

    public void setPreRegistrationId(long preRegistrationId) {
        this.preRegistrationId = preRegistrationId;
    }

    public Integer getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Integer appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public RegistrationOfficeWTO getRegistrationOffice() {
        return registrationOffice;
    }

    public void setRegistrationOffice(RegistrationOfficeWTO registrationOffice) {
        this.registrationOffice = registrationOffice;
    }

    public ActiveShiftWTO getActiveShift() {
        return activeShift;
    }

    public void setActiveShift(ActiveShiftWTO activeShift) {
        this.activeShift = activeShift;
    }

    public long getNationalId() {
        return nationalId;
    }

    public void setNationalId(long nationalId) {
        this.nationalId = nationalId;
    }
}
