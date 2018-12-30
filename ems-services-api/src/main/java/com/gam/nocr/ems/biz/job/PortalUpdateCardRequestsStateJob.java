package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Syncs card request states in portal
 * 
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalUpdateCardRequestsStateJob extends BaseEmsJob implements InterruptableJob {

	private static final Logger jobLogger = BaseLog
			.getLogger("portalUpdateCardRequest");

	private static final String DEFAULT_NUMBER_OF_REQUEST_TO_UPDATE = "100";
	private static final String DEFAULT_NUMBER_OF_REQUEST_TO_UPDATE_FETCH_LIMIT = "1000";

	private boolean isJobInterrupted = false;
	private JobKey jobKey = null;

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		startLogging(jobLogger);
		info("Updating requests state in portal started");
		jobKey = jobExecutionContext.getJobDetail().getKey();

//		Boolean loopFlag = true;
		Integer from = 0;
		Integer numberOfRequestToUpdate = Integer.valueOf(EmsUtil
				.getProfileValue(
						ProfileKeyName.KEY_NUMBER_OF_REQUEST_TO_UPDATE_STATE,
						DEFAULT_NUMBER_OF_REQUEST_TO_UPDATE));
		Integer fetchLimit = Integer.valueOf(EmsUtil
				.getProfileValue(
						ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_UPDATE_STATE_FETCH_LIMIT,
						DEFAULT_NUMBER_OF_REQUEST_TO_UPDATE_FETCH_LIMIT));
		Integer to = numberOfRequestToUpdate;

		try {
			List<Long> requestIds = new CardRequestDelegator()
					.getRequestIdsForUpdateState(fetchLimit);

			info("Total number of requests for update state "
					+ requestIds.size());

			if (requestIds.size() > 0) {
				List<List<Long>> batches = new ArrayList<List<Long>>();

				int divison = requestIds.size() / numberOfRequestToUpdate;
				int remainder = requestIds.size() % numberOfRequestToUpdate;

				for (int i = 0; i < divison; i++) {
					List<Long> batch = requestIds.subList(from, to);
					batches.add(batch);
					from = from + numberOfRequestToUpdate;
					to = to + numberOfRequestToUpdate;
				}

				if (remainder > 0) {
					List<Long> batch = requestIds.subList(divison
							* numberOfRequestToUpdate, requestIds.size());
					batches.add(batch);
				}

				info("Updating requests in bunch of "
						+ numberOfRequestToUpdate);

				info("Total number of batch for update state "
						+ batches.size());

				int batchNum = 1;
				for (List<Long> batch : batches) {
					if (!isJobInterrupted) {
						try {
							info("Going to update batch " + batchNum);
							new PortalManagementDelegator()
									.updateRequestStates(batch);
							info("batch " + batchNum
									+ " updated successfully");
						} catch (Exception e) {
							String excepStr = StringUtils.getStringFromException(e);
							error("An error occurred while updating batch "
											+ batchNum + " : " + excepStr);
						}
					} else {
						info("Job execution interrupted. No further processing will be done");
						break;
					}
					batchNum++;
				}
			}
			else{
				info("No need to update reruest states");
			}

		} catch (BaseException e) {
			error("An error occurred in updating state of some requests in portal - "
							+ e.getExceptionCode() + " : " + e.getMessage());
		}

		// while (loopFlag) {
		// if (!isJobInterrupted) {
		// try {
		// loopFlag = new PortalManagementDelegator()
		// .updateRequestStates(from, to);
		// } catch (BaseException e) {
		// // An exception happened while trying to sync a batch of
		// // card request with portal. So ignore the
		// // missed batch and go to the next one by increasing the
		// // start index to load
		// from = from + numberOfRequestToUpdate;
		// to = to + numberOfRequestToUpdate;
		// LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(),
		// e);
		// error("An error occurred in updating state of some requests in portal - "
		// + e.getExceptionCode()
		// + " : "
		// + e.getMessage());
		// }
		// } else {
		// info("Job execution interrupted. No further processing will be done");
		// break;
		// }
		// }
		info("Updating requests state in portal finished");
		endLogging();
	}


	@Override
	public void interrupt() throws UnableToInterruptJobException {
		error("calling interrupt: jobKey ==> " + jobKey);
		isJobInterrupted = true;
	}
}