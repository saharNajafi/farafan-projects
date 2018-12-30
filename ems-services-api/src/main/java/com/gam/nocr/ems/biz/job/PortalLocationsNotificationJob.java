package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.LocationType;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Notifies portal about updated location information
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalLocationsNotificationJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("PortalLocationsNotificationJob");

    private static final String DEFAULT_NUMBER_OF_LOCATION_TO_UPDATE = "10";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();

        Integer count = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_LOCATION_TO_UPDATE,
                DEFAULT_NUMBER_OF_LOCATION_TO_UPDATE));

        try {
            for (LocationType locationType : LocationType.values()) {
                Integer modifiedLocationCount = Integer.parseInt(portalManagementDelegator.fetchModifiedLocationCount(locationType).toString());

                if (modifiedLocationCount > 0) {
                    Integer loopCount = modifiedLocationCount / count;
                    Integer modular = modifiedLocationCount % count;

                    for (int i = 0; i < loopCount; i++) {
                        if (!isJobInterrupted) {
                            portalManagementDelegator.notifyPortalAboutModifiedProvinces(locationType, i * count, (i * count) + count);
                        } else {
                            break;
                        }
                    }

                    if (modular > 0 && !isJobInterrupted)
                        portalManagementDelegator.notifyPortalAboutModifiedProvinces(locationType,
                                count * loopCount, (count * loopCount) + modular);
                }
            }
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
