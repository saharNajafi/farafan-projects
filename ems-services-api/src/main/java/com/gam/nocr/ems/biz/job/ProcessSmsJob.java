package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Process all missing tracking number requests and sends their corresponding tracking numbers via sms
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessSmsJob implements InterruptableJob {

    private static final Logger LOGGER = BaseLog.getLogger(ProcessSmsJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();

            Integer from = 0;
            Integer to = 1;
            Integer totalCount = portalManagementDelegator.fetchReadyToProcessSms();

            Boolean loopFlag;

            for (int i = 0; i < totalCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        loopFlag = portalManagementDelegator.processSms(from, from + to);
                        //  An exception happened while trying to send tracking number via SMS. So ignore the
                        //  failed SMS request and go to the next one by increasing the start index to load
                        if (!loopFlag)
                            from++;
                    } catch (BaseException e) {
                        LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
                        if (BizExceptionCode.PSS_003.equals(e.getExceptionCode())
                                || BizExceptionCode.PSS_010.equals(e.getExceptionCode())
                                || BizExceptionCode.PSS_011.equals(e.getExceptionCode()))
                            break;
                        from++;
                    }
                } else {
                    break;
                }
            }
        } catch (BaseException e) {
            LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}