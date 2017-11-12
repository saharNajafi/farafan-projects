package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * Fetches all forget tracking number requests (that need to be sent via SMS) from portal
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalRequestedSmsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("PortalRequestedSmsJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();

            List<Long> portalCardRequestIds = portalManagementDelegator.fetchRequestedSmsIds();

            //  While portal returns a value, save them in database and call the portal for more items again
            while (EmsUtil.checkListSize(portalCardRequestIds)) {
                for (Long portalCardRequestId : portalCardRequestIds) {
                    if (!isJobInterrupted) {
                        try {
                            portalManagementDelegator.addRequestedSms(portalCardRequestId);
                        } catch (BaseException e) {
                            logException(e);
                        }
                    } else {
                        break;
                    }
                }
                portalCardRequestIds = portalManagementDelegator.fetchRequestedSmsIds();
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
