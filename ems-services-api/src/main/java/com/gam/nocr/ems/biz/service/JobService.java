package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.scheduler.SchedulerService;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface JobService extends Service {

    public void pause(SchedulerService schedulerService, String jobKey) throws BaseException;

    public void resume(SchedulerService schedulerService, String jobKey) throws BaseException;

    public void run(SchedulerService schedulerService, String jobKey) throws BaseException;

    public void update(SchedulerService schedulerService, String jobKey, String cron) throws BaseException;

    public void interrupt(SchedulerService schedulerService, String jobKey) throws BaseException;

}
