package com.gam.nocr.ems.biz.job;

import java.util.List;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;

import org.quartz.*;
import org.slf4j.Logger;

/**
 * Tries to retrieve the result of update citizen request (previously sent to IMS)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IMSGetUpdatedCitizensResultJob implements InterruptableJob {
    private static final Logger logger = BaseLog.getLogger(IMSGetUpdatedCitizensResultJob.class);
    private static final Logger ImsLogger = BaseLog.getLogger("ImsLogger");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;
    
	private static final String DEFAULT_IMS_AFIS_RESULT_FETCH_LIMIT = "1000";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        IMSDelegator imsDelegator = new IMSDelegator();
        try {
        	jobKey = jobExecutionContext.getJobDetail().getKey();
        	
			Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_AFIS_RESULT_FETCH_LIMIT,DEFAULT_IMS_AFIS_RESULT_FETCH_LIMIT));
        	List<Long> ids = imsDelegator.findAfisResultRequestsCountByState(CardRequestState.SENT_TO_AFIS,fetchLimit);
        	
        	if (ids != null) {
				for (Long requestId : ids) {
					if (!isJobInterrupted) {
						try {
							imsDelegator.getUpdatedCitizenResultNew(requestId);
						} catch (Exception e) {							
							 logger.error(e.getMessage(), e);
	                         ImsLogger.error(e.getMessage(), e);
						}
					} else {
						break;
					}
				}
			}        	
 
        } catch (Exception e) {
            logger.error("\nRuntime exception in job 'IMSGetUpdatedCitizensResultJob' : ", e);
            ImsLogger.error("\nRuntime exception in job 'IMSGetUpdatedCitizensResultJob' : ", e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
