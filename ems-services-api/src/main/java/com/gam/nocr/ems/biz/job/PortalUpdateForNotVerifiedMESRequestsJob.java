package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Transfers MES registered card requests to portal if they have not been verified by IMS. The citizen has to edit
 * his/her registration information in portal
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalUpdateForNotVerifiedMESRequestsJob  extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("PortalUpdateForNotVerifiedMESRequestsJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
        try {
            Boolean loopFlag = true;
            while (loopFlag) {
                if (!isJobInterrupted) {
                    loopFlag = portalManagementDelegator.transferNotVerifiedMESRequestsToPortal();
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
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
