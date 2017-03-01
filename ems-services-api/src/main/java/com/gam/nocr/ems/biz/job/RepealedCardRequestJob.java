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
public class RepealedCardRequestJob implements InterruptableJob {

    private static final Logger LOGGER = BaseLog.getLogger(RepealedCardRequestJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
                        LOGGER.error(e.getMessage(), e);
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
