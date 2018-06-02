package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/30/18.
 */
public class OfficeCapacityVTO extends ExtEntityTO {
    private Long id;
    private String startDate;
    private String endDate;
    private String shiftNo;
    private short capacity;
    private String isActive;
    private Float workingHoursFrom;
    private Float workingHoursTo;
    private Long enrollmentOffice;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(String shiftNo) {
        this.shiftNo = shiftNo;
    }

    public short getCapacity() {
        return capacity;
    }

    public void setCapacity(short capacity) {
        this.capacity = capacity;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Float getWorkingHoursFrom() {
        return workingHoursFrom;
    }

    public void setWorkingHoursFrom(Float workingHoursFrom) {
        this.workingHoursFrom = workingHoursFrom;
    }

    public Float getWorkingHoursTo() {
        return workingHoursTo;
    }

    public void setWorkingHoursTo(Float workingHoursTo) {
        this.workingHoursTo = workingHoursTo;
    }

    public Long getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(Long enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }
}
