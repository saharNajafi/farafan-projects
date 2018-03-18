package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * Tries to retrieve the result of update citizen request (previously sent to IMS)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSGetUpdatedCitizensResultJob extends BaseEmsJob implements InterruptableJob {
    private static final Logger ImsLogger = BaseLog.getLogger("IMSGetUpdatedCitizensResultJob");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    private static final String DEFAULT_IMS_AFIS_RESULT_FETCH_LIMIT = "1000";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(ImsLogger);
        IMSDelegator imsDelegator = new IMSDelegator();
        try {
            jobKey = jobExecutionContext.getJobDetail().getKey();

            Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_AFIS_RESULT_FETCH_LIMIT, DEFAULT_IMS_AFIS_RESULT_FETCH_LIMIT));
            List<Long> ids = imsDelegator.findAfisResultRequestsCountByState(CardRequestState.SENT_TO_AFIS, fetchLimit);

            if (ids != null) {
                for (Long requestId : ids) {
                    if (!isJobInterrupted) {
                        try {
                            imsDelegator.getUpdatedCitizenResultNew(requestId);
                        } catch (Exception e) {
                            logGeneralException(e);
                        }
                    } else {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            error("\nRuntime exception in job 'IMSGetUpdatedCitizensResultJob' : ", e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
