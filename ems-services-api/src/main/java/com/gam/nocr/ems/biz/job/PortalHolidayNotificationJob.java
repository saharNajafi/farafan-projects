package com.gam.nocr.ems.biz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.HolidayDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;

/**
 * Updates holiday info information in portal
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalHolidayNotificationJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(PortalHolidayNotificationJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

		HolidayDelegator holidayDelegator = new HolidayDelegator();
        try {
			holidayDelegator.notifyPortalAboutHoliday();
        } catch (BaseException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
