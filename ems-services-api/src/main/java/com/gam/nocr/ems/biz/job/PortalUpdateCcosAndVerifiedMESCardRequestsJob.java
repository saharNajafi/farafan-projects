package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Transfers CCOS and MES registered card requests to portal so that a citizen registered in these subsystem will be
 * able to track his/her registration in portal
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalUpdateCcosAndVerifiedMESCardRequestsJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(PortalUpdateCcosAndVerifiedMESCardRequestsJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    /**
     * <p> Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires
     * that is associated with the <code>Job</code>. </p>
     * <p/>
     * <p> The implementation may wish to set a {@link org.quartz.JobExecutionContext#setResult(Object) result} object on
     * the {@link org.quartz.JobExecutionContext} before this method exits.  The result itself is meaningless to Quartz,
     * but may be informative to <code>{@link org.quartz.JobListener}s</code> or <code>{@link
     * org.quartz.TriggerListener}s</code> that are watching the job's execution. </p>
     *
     * @throws org.quartz.JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
        try {
            Long requestCount = portalManagementDelegator.getCcosAndVerifiedMESRequestsCount();
            int from = 0;
            int to = 1;
            for(int i=0; i< requestCount; i++){
                try {
                    portalManagementDelegator.updateCardRequestFromCCOSAndMES(from, to);
                }catch (Exception ex){
                    logger.error(ex.getMessage(), ex);
                    from++;
                }
            }

        } catch (Exception e) {
            logger.error("Failed to execute update card request from CCOS job.", e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
