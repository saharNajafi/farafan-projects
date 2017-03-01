package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.RatingDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Updates rating info information in portal
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalRatingInfoNotificationJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(PortalRatingInfoNotificationJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        RatingDelegator ratingDelegator = new RatingDelegator();
        try {
            ratingDelegator.notifyPortalAboutRatingInfo();
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
