package com.gam.nocr.ems.biz.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Estelam3Job  extends BaseEmsJob implements InterruptableJob {

	private static final Logger estelam3Logger = BaseLog.getLogger("Estelam3Logger");
	private static final String DEFAULT_IMS_ESTELAM2_SIZE = "10";
	private static final String DEFAULT_IMS_ESTELAM2_FETCH_LIMIT = "1000";

	private boolean isJobInterrupted = false;
	private JobKey jobKey = null;

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		startLogging(estelam3Logger);
		Integer from = 0;
		
		IMSDelegator imsDelegator = new IMSDelegator();
		try {
			jobKey = jobExecutionContext.getJobDetail().getKey();
			Integer batchSize = Integer.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_NUMBER_OF_Estelam2_IMS_TO_LOAD,
					DEFAULT_IMS_ESTELAM2_SIZE));
			Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_Estelam_FETCH_LIMIT,
					DEFAULT_IMS_ESTELAM2_FETCH_LIMIT));
			// get all requests that must do getEstelam2 for them
			List<Long> requestIds = imsDelegator.getRequestsIdsForEnquiry(fetchLimit);
			int requestCount = requestIds == null ? 0 : requestIds.size();
			info("Request List Size For Estelam3 :"+ requestCount);
			Integer modular = requestCount % batchSize;
			Integer loopCount = requestCount / batchSize;

			for (int i = 0; i < loopCount; i++) {
				if (!isJobInterrupted) {
					try {
						List<Long> ids = new ArrayList<Long>();
						for (int index = 0; index < batchSize; index++) {
							ids.add(requestIds.get(from));
							from++;
						}
						//imsDelegator.updateCitizenInfoByEstelam2(ids);
						//Anbari:Estelam3
						imsDelegator.updateCitizenInfoByEstelam3(ids);
					} catch (Exception e) {
						// An exception happened while trying to send the batch
						// enquiry to IMS for a batch of requests
						// So ignore the batch items and go to the next batch by
						// increasing the start index to load
						logGenerakException(e);
//						isJobInterrupted=true;
					}
				} else {
					break;
				}
			}
			if (modular > 0 && !isJobInterrupted) {
				try {
					List<Long> ids = new ArrayList<Long>();
					for (int index = 0; index < modular; index++) {
						ids.add(requestIds.get(from));
						from++;
					}
					//imsDelegator.updateCitizenInfoByEstelam2(ids);
					//Anbari:Estelam3
					imsDelegator.updateCitizenInfoByEstelam3(ids);
				} catch (Exception e) {
					logGenerakException(e);
				}
			}

		} catch (BaseException e) {
			error("\nRuntime exception in job 'IMSEstelam3Job' : ", e);
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
