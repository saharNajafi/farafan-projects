package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.RegistrationDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Fetches card request information from portal
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalFetchingCardRequestsJob implements InterruptableJob {

    private static final Logger LOGGER = BaseLog.getLogger(PortalFetchingCardRequestsJob.class);
    private static final Logger jobLOGGER = BaseLog.getLogger("portalFetchCardRequest");

    private static final String DEFAULT_NUMBER_OF_PORTAL_REQUEST_TO_LOAD = "10";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();
        jobLOGGER.info("Fetching card requests from portal started");
        try {
            RegistrationDelegator registrationDelegator = new RegistrationDelegator();

            //  Fetch a list of portal card request ids to be used in next step for fetching them in batches
            List<Long> portalCardRequestIds = registrationDelegator.fetchPortalCardRequestIdsForTransfer();

            if (EmsUtil.checkListSize(portalCardRequestIds)) {
                Integer requestCount = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_PORTAL_REQUEST_TO_LOAD,
                        DEFAULT_NUMBER_OF_PORTAL_REQUEST_TO_LOAD));

                jobLOGGER.info("Trying to fetch portal requests in batch of {} items", requestCount);

                Integer loopCount = portalCardRequestIds.size() / requestCount;
                Integer modular = portalCardRequestIds.size() % requestCount;

                List<List<Long>> list = new ArrayList<List<Long>>();
                for (int i = 0; i < loopCount; i++) {
                    if (!isJobInterrupted) {
                        list.add(portalCardRequestIds.subList(i * requestCount, (i * requestCount) + requestCount));
                    } else {
                        break;
                    }
                }

                if (modular > 0 && !isJobInterrupted)
                    list.add(portalCardRequestIds.subList(requestCount * loopCount, (requestCount * loopCount) + modular));

                for (List<Long> longList : list) {
                    if (!isJobInterrupted) {
                        try {
                            registrationDelegator.fetchPortalCardRequestsToSave(longList);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                            jobLOGGER.error("Exception has been raised while fetching portal card requests with identification list of: " + longList.toString() , e);
                        }
                    } else {
                        jobLOGGER.warn("Fetching card requests from portal interrupted by user");
                        break;
                    }
                }

            } else {
                jobLOGGER.info("There is no portal request identifiers to load from portal");
            }
        } catch (BaseException e) {
            LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            jobLOGGER.error("Fetching card requests from portal stopped due to an exceptional situation", e);
        } catch (Exception e) {
            jobLOGGER.error("Fetching card requests from portal stopped due to an exceptional situation", e);
            LOGGER.error(e.getMessage(), e);
        }
        jobLOGGER.info("Fetching card requests from portal finished");
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
