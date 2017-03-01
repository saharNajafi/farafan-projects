package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.ReportRequestDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Processes all report requests that have been scheduled for later run. It tries to process just those requests that
 * their scheduled time is passed
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessRequestedReportsJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(ProcessRequestedReportsJob.class);
    private static final Logger reportJobLogger = BaseLog.getLogger("processRequestedReports");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        reportJobLogger.info("Processing requested reports started");
        jobKey = jobExecutionContext.getJobDetail().getKey();

        ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();

        try {
            Long totalCount = reportRequestDelegator.getRequestedReportByJobCount();
            reportJobLogger.info("Total number of requested reports is " + totalCount);

            Integer from = 0;
            Integer to = 1;
            for (int i = 0; i < totalCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        reportJobLogger.info("Processing report request " + (i+1) + " of " + totalCount);
                        reportRequestDelegator.generateRequestedReportByJob(from, to);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a report request. So ignore the
                        //  report request and go to the next one by increasing the start index to load
                        from = from + to;
                        logger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
                        reportJobLogger.info("An exception happened while generating report request. This report request will be ignored", e);
                    }
                } else {
                    reportJobLogger.info("Job execution interrupted. No further processing will be done");
                    break;
                }
            }
            reportJobLogger.info("Processing requested reports finished");
        } catch (BaseException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            reportJobLogger.error("Processing requested reports stopped due to an exception", e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
