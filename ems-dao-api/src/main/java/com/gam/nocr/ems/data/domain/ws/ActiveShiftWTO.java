package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.ShiftEnum;

import java.io.Serializable;

/**
 * Created by sahar on 10/15/17.
 */
public class ActiveShiftWTO implements Serializable {

    private long id;
    private int activeDate;
    private ShiftEnum shiftNo;
    private short remainCapacity;
    private RegistrationOfficeWTO registrationOffice;


    public ActiveShiftWTO() {
    }

    public ActiveShiftWTO(long id) {
        this.id = id;
    }

    public ActiveShiftWTO(long id, int activeDate, ShiftEnum shiftNo,
                          short remainCapacity, String createdBy, String createdOn) {
        this.id = id;
        this.activeDate = activeDate;
        this.shiftNo = shiftNo;
        this.remainCapacity = remainCapacity;
        createdBy = createdBy;
        createdOn = createdOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(int activeDate) {
        this.activeDate = activeDate;
    }

    public ShiftEnum getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(ShiftEnum shiftNo) {
        this.shiftNo = shiftNo;
    }

    public RegistrationOfficeWTO getRegistrationOffice() {
        return registrationOffice;
    }

    public void setRegistrationOffice(RegistrationOfficeWTO registrationOffice) {
        this.registrationOffice = registrationOffice;
    }

    public short getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(short remainCapacity) {
        this.remainCapacity = remainCapacity;
    }


}
