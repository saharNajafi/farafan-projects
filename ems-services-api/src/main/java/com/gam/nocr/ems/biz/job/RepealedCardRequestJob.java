package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Process all card requests flagged as repealed. It removed all registration information (like face, finger or docs)
 * and in the next run of sync job, the portal detects repealed requests and removes their registration information in
 * portal too
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RepealedCardRequestJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("RepealedCardRequestJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();

            Long totalCount = cardRequestDelegator.findRequestCountByAction(CardRequestedAction.REPEAL_ACCEPTED);

            Integer from = 0;
            for (int i = 0; i < totalCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardRequestDelegator.doCardRequestRepealActionBySystem(from);
                    } catch (Exception e) {
                        //  An exception happened while trying to delete a registration information. So ignore the
                        //  failed request and go to the next one by increasing the start index to load
                        error(e.getMessage(), e);
                        from++;
                    }
                } else {
                    break;
                }
            }
        } catch (BaseException e) {
            logException(e);
        } catch (Exception e) {
            logGenerakException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
