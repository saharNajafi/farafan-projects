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
public class PortalFetchingCardRequestsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLOGGER = BaseLog.getLogger("portalFetchCardRequest");

    private static final String DEFAULT_NUMBER_OF_PORTAL_REQUEST_TO_LOAD = "10";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLOGGER);
        jobKey = jobExecutionContext.getJobDetail().getKey();
        try {
            RegistrationDelegator registrationDelegator = new RegistrationDelegator();

            //  Fetch a list of portal card request ids to be used in next step for fetching them in batches
            List<Long> portalCardRequestIds = registrationDelegator.fetchPortalCardRequestIdsForTransfer();

            if (EmsUtil.checkListSize(portalCardRequestIds)) {
                Integer requestCount = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_PORTAL_REQUEST_TO_LOAD,
                        DEFAULT_NUMBER_OF_PORTAL_REQUEST_TO_LOAD));

                info("Trying to fetch portal requests in batch of {} items", requestCount);

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
                            error("Exception has been raised while fetching portal card requests with identification list of: " + longList.toString(), e);
                        }
                    } else {
                        warn("Fetching card requests from portal interrupted by user");
                        break;
                    }
                }

            } else {
                info("There is no portal request identifiers to load from portal");
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
