package com.gam.nocr.ems.biz.job;

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
import com.gam.nocr.ems.biz.delegator.MessageDelegator;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessMessageJob implements InterruptableJob {

	private static final Logger LOGGER = BaseLog.getLogger(ProcessMessageJob.class);

	private boolean isJobInterrupted = false;
	private JobKey jobKey = null;

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		jobKey = jobExecutionContext.getJobDetail().getKey();

		try {
			
			MessageDelegator messageDelegator = new MessageDelegator();

			List<Long> messageIds  = messageDelegator.fetchReadyToProcessMessage();

			if (messageIds != null) {
				for (Long id : messageIds) {
					if (!isJobInterrupted) {
						try {
							messageDelegator.processMessage(id);
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}
					} else {
						break;
					}
				}
			}
		} catch (BaseException e) {
			LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.err.println("calling interrupt: jobKey ==> " + jobKey);
		isJobInterrupted = true;
	}
}