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
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PurgeBiometricAndDocumentJob implements InterruptableJob {

	private static final Logger logger = BaseLog.getLogger(PurgeBiometricAndDocumentJob.class);
	private static final Logger purgeBioAndDocsLogger = BaseLog
			.getLogger("PurgeBiometricAndDocumentLogger");
	private static final String DEFAULT_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT = "1";
	private static final String DEFAULT_SAVE_PURGE_HISTORY = "true";

	private boolean isJobInterrupted = false;
	private JobKey jobKey = null;

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		
		CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
		try {
			jobKey = jobExecutionContext.getJobDetail().getKey();
			Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.
					KEY_NUMBER_OF_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT,DEFAULT_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT));
			
			String savePurgeHistory = String.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_SAVE_PURGE_HISTORY,DEFAULT_SAVE_PURGE_HISTORY));
			
			List<Long> citizenIds = cardRequestDelegator.getCitizenIdsForPurgeBioAndDocs(fetchLimit);
			int requestCount = citizenIds.size();
			logger.info("Citizen List Size For Purge Biometrics and Docs :"+ requestCount);
			purgeBioAndDocsLogger.info("Citizen List Size For Purge Biometrics and Docs :"+requestCount);

			for (Long id : citizenIds) {
				if (!isJobInterrupted) {
					try {
						cardRequestDelegator.purgeBiometricsAndDocuments(id,savePurgeHistory);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						purgeBioAndDocsLogger.error(e.getMessage(), e);
					}
				} else {
					break;
				}
			}

			

		} catch (BaseException e) {
			logger.error("\nRuntime exception in job 'PurgeBiometricAndDocumentJob' : ", e);
			purgeBioAndDocsLogger.error(
					"\nRuntime exception in job 'PurgeBiometricAndDocumentJob' : ", e);
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.err.println("calling interrupt: jobKey ==> " + jobKey);
		isJobInterrupted = true;
	}
}
