package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * This job fetch all reserved request, and insert a record into msgt to send
 * SMS
 *
 * @author Madanipour
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReservedSmsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog
            .getLogger("ReservedSmsJob");

    private static final String DEFAULT_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT = "1000";
    private static final String DEFAULT_SEND_RESERVE_SMS_TIME_INTERVAL = "1";


    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();

            Integer numberOfRequestToFetch = Integer.valueOf(EmsUtil
                    .getProfileValue(
                            ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT,
                            DEFAULT_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT));

            Integer dayInterval = Integer.valueOf(EmsUtil
                    .getProfileValue(
                            ProfileKeyName.KEY_SEND_RESERVE_SMS_TIME_INTERVAL,
                            DEFAULT_SEND_RESERVE_SMS_TIME_INTERVAL));

            List<Long> cardRequestIds = cardRequestDelegator.fetchReservedRequest(numberOfRequestToFetch, dayInterval);


            for (Long cardRequestId : cardRequestIds) {
                if (!isJobInterrupted) {
                    try {
                        cardRequestDelegator.addRequestedSmsForReservedReq(cardRequestId);
                    } catch (BaseException e) {
                        logException(e);
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