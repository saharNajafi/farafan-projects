package com.gam.nocr.ems.biz.job;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;

import org.quartz.*;
import org.slf4j.Logger;

/**
 * This job fetch all reserved request, and insert a record into msgt to send
 * SMS
 * 
 * @author Madanipour
 * 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReservedSmsJob implements InterruptableJob {

	private static final Logger LOGGER = BaseLog
			.getLogger(ReservedSmsJob.class);
	
	private static final String DEFAULT_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT = "1000";
	private static final String DEFAULT_SEND_RESERVE_SMS_TIME_INTERVAL = "1";
	

	private boolean isJobInterrupted = false;
	private JobKey jobKey = null;

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		jobKey = jobExecutionContext.getJobDetail().getKey();

		try {
			CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
			
			Integer numberOfRequestToFetch = Integer.valueOf(EmsUtil
					.getProfileValue(
							ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT,
							DEFAULT_NUMBER_OF_REQUEST_FOR_SEND_RESERVED_DATE_RMINDING_SMS_FETCH_LIMIT));
			
			Integer dayInterval= Integer.valueOf(EmsUtil
					.getProfileValue(
							ProfileKeyName.KEY_SEND_RESERVE_SMS_TIME_INTERVAL,
							DEFAULT_SEND_RESERVE_SMS_TIME_INTERVAL));

			List<Long> cardRequestIds = cardRequestDelegator.fetchReservedRequest(numberOfRequestToFetch, dayInterval);

			 
	                for (Long cardRequestId : cardRequestIds) {
	                    if (!isJobInterrupted) {
	                        try {
	                            cardRequestDelegator.addRequestedSmsForReservedReq(cardRequestId);
	                        } catch (BaseException e) {
	                            LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
	                        }
	                    } else {
	                        break;
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