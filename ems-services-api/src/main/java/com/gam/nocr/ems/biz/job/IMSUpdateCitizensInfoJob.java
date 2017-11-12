package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Sends updated information of a citizen to IMS
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSUpdateCitizensInfoJob extends BaseEmsJob implements InterruptableJob {
    private static final Logger ImsLogger = BaseLog.getLogger("IMSUpdateCitizensInfoJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;
    private static final String DEFAULT_UPDATE_AFIS_FETCH_LIMIT = "1000";
    String DEFAULT_NUMBER_OF_IMS_UPDATE_ASYNC_CALL_COUNT = "5";
    String DEFAULT_ASYNC_ENABLE = "1";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(ImsLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        Integer fetchLimit = Integer
                .valueOf(EmsUtil
                        .getProfileValue(
                                ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_UPDATE_AFIS_FETCH_LIMIT,
                                DEFAULT_UPDATE_AFIS_FETCH_LIMIT));
        String isAsyncEnable = EmsUtil.getProfileValue(ProfileKeyName.KEY_ASYNC_ENABLE_IMSUPDATE, DEFAULT_ASYNC_ENABLE);

        Integer updateImsAsyncCallCount = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_IMSUPDATE_ASYNC_CALL_COUNT,
                DEFAULT_NUMBER_OF_IMS_UPDATE_ASYNC_CALL_COUNT));


        try {
            IMSDelegator imsDelegator = new IMSDelegator();
            List<Long> ids = imsDelegator.getRequestIdsForSendToAFIS(fetchLimit);

            int asyncCallCount = 0;
            int finishedThreads = 0;
            int errorThreads = 0;

            List<Future<String>> currentSyncResult = new ArrayList<Future<String>>();
            List<Future<String>> succAsyncResult = new ArrayList<Future<String>>();
            List<Future<String>> failAsyncResult = new ArrayList<Future<String>>();

            //******************"*

            if (isAsyncEnable.equals("1")) {
                if (ids != null) {
                    for (Long requestId : ids) {
                        if (!isJobInterrupted) {
                            try {
                                Future<String> res = imsDelegator.updateCitizenInfoByIdAsync(requestId);

                                if (res != null) {
                                    currentSyncResult.add(res);
                                    asyncCallCount++;
                                }
                                while ((currentSyncResult.size() >= updateImsAsyncCallCount)
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
                                                        BizExceptionCode.IUC_01,
                                                        " exception on calling IMS Update Citizen job thread async : "
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
                                logGenerakException(e);
                            }
                        } else {
                            break;
                        }
                    }


                    while ((currentSyncResult.size() > 0 && currentSyncResult
                            .size() <= updateImsAsyncCallCount)) {

                        for (Future<String> f : currentSyncResult) {
                            if (f.isDone() == true) {
                                try {
                                    String tmp = f.get();
                                    finishedThreads++;
                                    succAsyncResult.add(f);
                                } catch (Exception e) {
                                    errorThreads++;
                                    failAsyncResult.add(f);
                                    error(BizExceptionCode.IUC_02,
                                            " exception on calling HandingOut job thread async : "
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
                                imsDelegator.updateCitizenInfoById(requestId);
                            } catch (Exception e) {
                                logGenerakException(e);
                            }
                        } else {
                            break;
                        }
                    }
                }

            }

            info("imsUpdateCitizen job finished on : " + new Date()
                    + " and all threads = " + asyncCallCount
                    + " and success threads = " + finishedThreads
                    + " and exception threads = " + errorThreads);

        } catch (Exception e) {
            logGenerakException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
