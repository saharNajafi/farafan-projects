package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardManagementDelegator;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Takes a list of missed cards and notifies the CMS about the missing
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CardMissedNotificationJob  extends BaseEmsJob implements InterruptableJob {
    private static final Logger jobLogger = BaseLog.getLogger("cardMissedNotification");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            CardManagementDelegator cardManagementDelegator = new CardManagementDelegator();
            Integer from = 0;
            Integer to = 1;
            Long totalCount = cardManagementDelegator.findMissedCardsCount();
            for (int i = 0; i < totalCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.notifyMissedCards(from, to);
                    } catch (BaseException e) {
                        //  An exception happened while trying to notify the CMS about missing a card. So ignore the
                        //  missed card and go to the next one by increasing the start index to load
                        from++;
                        logException(e);
                    }
                } else {
                    break;
                }
            }
        } catch (BaseException e) {
            logException(e);
        }
        endLogging();
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey,null);
        isJobInterrupted = true;
    }
}
