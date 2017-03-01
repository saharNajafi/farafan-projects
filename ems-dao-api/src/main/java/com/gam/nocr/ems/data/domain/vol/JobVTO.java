package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.Date;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class JobVTO extends ExtEntityTO {

    private String name;
    private String description;
    private String state;
    private String cronState;
    private String simpleState;
    private String cron;
    private Date previousFireTime;
    private Date nextFireTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCronState() {
        return cronState;
    }

    public void setCronState(String cronState) {
        this.cronState = cronState;
    }

    public String getSimpleState() {
        return simpleState;
    }

    public void setSimpleState(String simpleState) {
        this.simpleState = simpleState;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
