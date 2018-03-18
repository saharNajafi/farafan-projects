package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * Sends batch enquiry request to IMS for those card requests that are already in enquiry process (PENDING_IMS) and
 * a specific time is passed from the last enquiry to IMS (without receiving any result). <br/>
 * As IMS have no request id for an enquiry request, there is no way to track the state of previous enquires. We have
 * to call the 'download' service periodically (through {@link com.gam.nocr.ems.biz.job.IMSBatchEnquiryResultJob} in
 * order to fetch the result of one of previously sent requests. So we have no idea about what responses we will get
 * from IMS till we call them. The problem is if we encounter a problem while processing and persisting a result, there
 * is no way to retrieve the same result from IMS again. So we have to send those requests to IMS again in order to
 * retrieve their result on next call to 'download' web service.<br/>
 *
 * As we have no idea about what card requests (in PENDING_IMS state) have encountered such situation, we will try to
 * resend a batch enquiry request for all such requests IF a specific time is passed from the last enquiry to IMS.
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSBatchEnquiryRequestJobForIMSPending  extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("IMSBatchEnquiryRequestJobForIMSPending");

    private static final String DEFAULT_IMS_OFFLINE_ENQUIRY_SIZE = "2";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();
        Integer from = 0;
        IMSDelegator imsDelegator = new IMSDelegator();
        try {
            Integer batchSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_IMS_OFFLINE_ENQUIRY_SIZE, DEFAULT_IMS_OFFLINE_ENQUIRY_SIZE));
            HashMap<String, String> param = new HashMap<String, String>();
            String[] parameters = imsDelegator.getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager();
            param.put("interval", parameters[0]);
            param.put("intervalType", parameters[1]);
            param.put("requestState", CardRequestState.PENDING_IMS.name());
            Integer requestCount = imsDelegator.getRequestsCountForBatchEnquiry("imsBatchEnquiryListForPendingIMSRequests", param);
            Integer loopCount = requestCount / batchSize;
            Integer modular = requestCount % batchSize;

            info("IMSBatchEnquiryRequestJobForIMSPending: Total number of items to be send for enquiry : " + requestCount);

            for (int i = 0; i < loopCount; i++) {
                if (!isJobInterrupted) {
                    info("IMSBatchEnquiryRequestJobForIMSPending: iteration " + i + " of " + loopCount);
                    try {
                        imsDelegator.sendBatchEnquiryReqForFirstTime(from, batchSize, CardRequestState.PENDING_IMS);
                    } catch (Exception e) {
                        //  An exception happened while trying to send the batch enquiry to IMS for a batch of requests
                        //  So ignore the batch items and go to the next batch by increasing the start index to load
                        String message = "IMSBatchEnquiryRequestJobForIMSPending: An error occurred while trying send enquiry request to IMS - " + e.getMessage();
                        error(message, e);
                        from += batchSize;
                    }
                } else {
                    break;
                }
            }
            if (modular > 0 && !isJobInterrupted) {
                info("IMSBatchEnquiryRequestJobForIMSPending: last iteration ");
                try {
                    imsDelegator.sendBatchEnquiryReqForFirstTime(from, modular, CardRequestState.PENDING_IMS);
                } catch (Exception e) {
                    String message = "IMSBatchEnquiryRequestJobForIMSPending: An error occurred while trying send enquiry request to IMS - " + e.getMessage();
                  logGeneralException(e);
                }
            }
        } catch (Exception e) {
            String message = "IMSBatchEnquiryRequestJobForIMSPending: An error occurred while trying send enquiry request to IMS - " + e.getMessage();
            error(message, e);
        }

        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
