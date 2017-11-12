package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.nocr.ems.biz.delegator.CardIssuanceRequestDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Sends card issuance request to CMS for all card requests already approved by
 * enrollment office manager and their information are successfully updated in
 * IMS
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CardIssuanceRequestJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog
            .getLogger("cardIssuanceRequest");

    private static final String DEFAULT_CARD_ISSUANCE_FETCH_LIMIT = "1000";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        CardIssuanceRequestDelegator cardIssuanceRequestDelegator = new CardIssuanceRequestDelegator();

        String DEFAULT_NUMBER_OF_Issuance_ASYNC_CALL_COUNT = "10";
        String DEFAULT_ASYNC_ENABLE = "1";

        String isAsyncEnable = EmsUtil.getProfileValue(ProfileKeyName.KEY_ASYNC_ENABLE, DEFAULT_ASYNC_ENABLE);

        Integer issuanceAsyncCallCount = Integer.valueOf(EmsUtil
                .getProfileValue(
                        ProfileKeyName.KEY_NUMBER_OF_Issuace_ASYNC_CALL_COUNT,
                        DEFAULT_NUMBER_OF_Issuance_ASYNC_CALL_COUNT));

        info("issuance job started on : " + new Date());
        try {

            Integer fetchLimit = Integer
                    .valueOf(EmsUtil
                            .getProfileValue(
                                    ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_CARD_ISSUANCE_FETCH_LIMIT,
                                    DEFAULT_CARD_ISSUANCE_FETCH_LIMIT));
            List<Long> ids = cardIssuanceRequestDelegator
                    .getRequestIdsForIssue(fetchLimit);

            CertificateTO certificateTO = cardIssuanceRequestDelegator
                    .findCertificateByUsage(CertificateUsage.CMS_SIGN);
            if (certificateTO == null) {
                // No certificate found to sign card issuance request before
                // sending to CMS
                throw new ServiceException("ERROR : ",
                        "The specified certificate was not found.");
            }

            int asyncCallCount = 0;
            int finishedThreads = 0;
            int errorThreads = 0;

            List<Future<String>> currentSyncResult = new ArrayList<Future<String>>();
            List<Future<String>> succAsyncResult = new ArrayList<Future<String>>();
            List<Future<String>> failAsyncResult = new ArrayList<Future<String>>();

            if (isAsyncEnable.equals("1")) {
                if (ids != null) {
                    for (Long requestId : ids) {
                        if (!isJobInterrupted) {
                            try {
                                Future<String> res = cardIssuanceRequestDelegator
                                        .prepareDataAndSendIssuanceRequestByIdAsync(
                                                requestId, certificateTO);
                                if (res != null) {
                                    currentSyncResult.add(res);
                                    asyncCallCount++;
                                }
                                while ((currentSyncResult.size() >= issuanceAsyncCallCount)
                                        && !isJobInterrupted) {

                                    for (Future<String> f : currentSyncResult) {
                                        if (f.isDone() == true) {
                                            try {
                                                String tmp = f.get();
                                                finishedThreads++;
                                                succAsyncResult.add(f);
                                            } catch (Exception e) {
                                                errorThreads++;
                                                failAsyncResult.add(f);
                                                error(
                                                        BizExceptionCode.ISU_01,
                                                        " exception on calling issuance job thread async : "
                                                                + e.getMessage(),
                                                        e);
                                            }
                                        }
                                    }

                                    for (Future<String> f : succAsyncResult) {
                                        currentSyncResult.remove(f);
                                    }

                                    for (Future<String> f : failAsyncResult) {
                                        currentSyncResult.remove(f);
                                    }

                                    succAsyncResult.clear();
                                    failAsyncResult.clear();

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                }

                            } catch (Exception e) {
                                // An exception happened while trying to send an
                                // issuance request to the CMS. So ignore the
                                // current request and go to the next one by
                                // increasing the start index to load
                                logGenerakException(e);
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    // this piece of code prevent java from getting closed
                    // before
                    // ending all of async calls
                    while ((currentSyncResult.size() > 0 && currentSyncResult
                            .size() <= issuanceAsyncCallCount)) {

                        for (Future<String> f : currentSyncResult) {
                            if (f.isDone() == true) {
                                try {
                                    String tmp = f.get();
                                    finishedThreads++;
                                    succAsyncResult.add(f);
                                } catch (Exception e) {
                                    errorThreads++;
                                    failAsyncResult.add(f);
                                    error(BizExceptionCode.ISU_02,
                                            " exception on calling issuance job thread async : "
                                                    + e.getMessage(), e);
                                }
                            }
                        }

                        for (Future<String> f : succAsyncResult) {
                            currentSyncResult.remove(f);
                        }

                        for (Future<String> f : failAsyncResult) {
                            currentSyncResult.remove(f);
                        }

                        succAsyncResult.clear();
                        failAsyncResult.clear();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }

            }//End if isAsyncEnable
            else {

                if (ids != null) {
                    for (Long requestId : ids) {
                        if (!isJobInterrupted) {
                            try {
                                cardIssuanceRequestDelegator
                                        .prepareDataAndSendIssuanceRequestById(
                                                requestId, certificateTO);
                            } catch (Exception e) {
                                // An exception happened while trying to send an
                                // issuance request to the CMS. So ignore the
                                // current request and go to the next one by
                                // increasing the start index to load
                                logGenerakException(e);
                            }
                        } else {
                            break;
                        }
                    }
                }

            }

            info("issuance job finished on : " + new Date()
                    + " and all threads = " + asyncCallCount
                    + " and success threads = " + finishedThreads
                    + " and exception threads = " + errorThreads);
        } catch (Exception e) {
            logGenerakException( e);
        }

        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
