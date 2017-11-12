package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.ReportRequestDelegator;
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
public class ProcessRequestedReportsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger reportJobLogger = BaseLog.getLogger("processRequestedReports");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(reportJobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();

        try {
            Long totalCount = reportRequestDelegator.getRequestedReportByJobCount();
            info("Total number of requested reports is " + totalCount);

            Integer from = 0;
            Integer to = 1;
            for (int i = 0; i < totalCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        info("Processing report request " + (i + 1) + " of " + totalCount);
                        reportRequestDelegator.generateRequestedReportByJob(from, to);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a report request. So ignore the
                        //  report request and go to the next one by increasing the start index to load
                        from = from + to;
                        logException(e);
                    }
                } else {
                    info("Job execution interrupted. No further processing will be done");
                    break;
                }
            }
            info("Processing requested reports finished");
        } catch (BaseException e) {
            logException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
