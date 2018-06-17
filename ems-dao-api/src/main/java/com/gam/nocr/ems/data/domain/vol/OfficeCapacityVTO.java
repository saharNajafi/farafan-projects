package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ConstValues;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.gam.nocr.ems.util.CalendarUtil;
import java.util.Date;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/30/18.
 */
public class OfficeCapacityVTO extends ExtEntityTO {
    private Long id;
    private String startDate;
    private String endDate;
    private String shiftNo;
    private String capacity;
    private Float workingHoursFrom;
    private Float workingHoursTo;
    private Long enrollmentOfficeId;
    private Boolean editable;

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
        String date = null;
        if(!startDate.contains("T")) {
            date = convertPersianToGregorian(startDate);
            this.startDate = date.replace("/", "-").concat("T00:00:00");
        }else
            this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        String date = null;
        date = convertPersianToGregorian(endDate);
        this.endDate = date.replace("/", "-").concat("T00:00:00");
    }

    public String getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(String shiftNo) {
        this.shiftNo = shiftNo;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
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

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }

    public Boolean getEditable() {
        editable = false;
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(startDate.substring(0, 9));
            if(date.after(new Date()))
                editable = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    private String convertPersianToGregorian(String date){
        String DELIMITER = "/";
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String DateWithSlash = year + DELIMITER + month + DELIMITER + day;
        return CalendarUtil.convertPersianToGregorian(DateWithSlash);
    }
}
