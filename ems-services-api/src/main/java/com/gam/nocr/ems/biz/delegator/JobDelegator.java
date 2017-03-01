package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.scheduler.SchedulerService;
import com.gam.nocr.ems.biz.service.JobService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class JobDelegator implements Delegator {

    private JobService getService(UserProfileTO userProfileTO) throws BaseException {
        JobService jobService = null;
        try {
            jobService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_JOB), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.JBD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_JOB.split(","));
        }
        jobService.setUserProfileTO(userProfileTO);
        return jobService;
    }

    public void pause(UserProfileTO userProfileTO, SchedulerService schedulerService, String jobKey) throws BaseException {
        getService(userProfileTO).pause(schedulerService, jobKey);
    }

    public void resume(UserProfileTO userProfileTO, SchedulerService schedulerService, String jobKey) throws BaseException {
        getService(userProfileTO).resume(schedulerService, jobKey);
    }

    public void run(UserProfileTO userProfileTO, SchedulerService schedulerService, String jobKey) throws BaseException {
        getService(userProfileTO).run(schedulerService, jobKey);
    }

    public void update(UserProfileTO userProfileTO, SchedulerService schedulerService, String jobKey, String cron) throws BaseException {
        getService(userProfileTO).update(schedulerService, jobKey, cron);
    }

    public void interrupt(UserProfileTO userProfileTO, SchedulerService schedulerService, String jobKey) throws BaseException {
        getService(userProfileTO).interrupt(schedulerService, jobKey);
    }

}
