package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * Sends batch enquiry request to IMS for a batch of card requests
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSBatchEnquiryRequestJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(IMSBatchEnquiryRequestJob.class);
    private static final Logger ImsLogger = BaseLog.getLogger("ImsLogger");

    private static final String DEFAULT_IMS_OFFLINE_ENQUIRY_SIZE = "10";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        Integer from = 0;
        IMSDelegator imsDelegator = new IMSDelegator();
        try {
            Integer batchSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_IMS_OFFLINE_ENQUIRY_SIZE, DEFAULT_IMS_OFFLINE_ENQUIRY_SIZE));

            //  Preparing parameters that would be used in a query to count the number of requests to send for IMS
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("requestState", CardRequestState.RECEIVED_BY_EMS.name());
            param.put("mesRequestState", CardRequestState.APPROVED_BY_MES.name());
            param.put("mesOrigin", CardRequestOrigin.M.name());
            Integer requestCount = imsDelegator.getRequestsCountForBatchEnquiry("imsBatchEnquiryList", param);
            Integer modular = requestCount % batchSize;
            Integer loopCount = requestCount / batchSize;
            for (int i = 0; i < loopCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        imsDelegator.sendBatchEnquiryReqForFirstTime(from, batchSize, CardRequestState.RECEIVED_BY_EMS);
                    } catch (Exception e) {
                        //  An exception happened while trying to send the batch enquiry to IMS for a batch of requests
                        //  So ignore the batch items and go to the next batch by increasing the start index to load
                        logger.error(e.getMessage(), e);
                        ImsLogger.error(e.getMessage(), e);
                        from += batchSize;
                    }
                } else {
                    break;
                }
            }
            if (modular > 0 && !isJobInterrupted) {
                try {
                    imsDelegator.sendBatchEnquiryReqForFirstTime(from, modular, CardRequestState.RECEIVED_BY_EMS);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    ImsLogger.error(e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ImsLogger.error(e.getMessage(), e);
        }
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
