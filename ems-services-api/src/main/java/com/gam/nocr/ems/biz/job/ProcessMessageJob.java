package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.MessageDelegator;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.List;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessMessageJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("ProcessMessageJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {

            MessageDelegator messageDelegator = new MessageDelegator();

            List<Long> messageIds = messageDelegator.fetchReadyToProcessMessage();

            if (messageIds != null) {
                for (Long id : messageIds) {
                    if (!isJobInterrupted) {
                        try {
                            messageDelegator.processMessage(id);
                        } catch (Exception e) {
                            logGeneralException(e);
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (BaseException e) {
            logException(e);
        } catch (Exception e) {
            logGeneralException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}