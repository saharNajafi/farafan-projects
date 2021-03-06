package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Tries to retrieve a batch enquiry response from IMS
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSBatchEnquiryResultJob  extends BaseEmsJob implements InterruptableJob {

    private static final Logger ImsLogger = BaseLog.getLogger("IMSBatchEnquiryResultJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(ImsLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();
        IMSDelegator imsDelegator = new IMSDelegator();
        try {
            //  As we don't know how much result is prepared in IMS side, we'll try to fetch a batch result while it
            //  answers with a non-empty response
            TransferInfoVTO transferInfoVTO = imsDelegator.fetchBatchEnquiryResult();
            while (transferInfoVTO != null) {
                if (!isJobInterrupted) {
                    imsDelegator.updateRequestsByBatchEnquiryResult(transferInfoVTO);
                    transferInfoVTO = imsDelegator.fetchBatchEnquiryResult();
                } else {
                    break;
                }
            }

        } catch (BaseException e) {
            error("\nRuntime exception in job 'IMSBatchEnquiryResultJob' : ", e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
