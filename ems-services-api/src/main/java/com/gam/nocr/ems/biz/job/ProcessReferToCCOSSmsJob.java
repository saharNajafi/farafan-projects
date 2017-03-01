package com.gam.nocr.ems.biz.job;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.OutgoingSMSDelegator;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.SendSmsType;
import com.gam.nocr.ems.util.EmsUtil;

import org.quartz.*;
import org.slf4j.Logger;

/**
 * Process all missing tracking number requests and sends their corresponding tracking numbers via sms
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessReferToCCOSSmsJob implements InterruptableJob {



    private static final Logger LOGGER = BaseLog.getLogger(ProcessReservedSmsJob.class);
    
    private static final String DEFAULT_DELETE_FROM_MSGT_ENABLE= "F";
    private static final String DEFAULT_DELETE_FROM_MSGT_TIME_INTERVAL= "2";
    private static final String DEFAULT_PROCESS_REFER_TO_CCOS_SMS_FETCH_LIMIT= "100";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
        	OutgoingSMSDelegator outgoingSMSDelegator = new OutgoingSMSDelegator();
        	
        	Integer fetchLimit = Integer
					.valueOf(EmsUtil
							.getProfileValue(
									ProfileKeyName.KEY_NUMBER_OF_PROCESS_REFER_TO_CCOS_SMS_FETCH_LIMIT,
									DEFAULT_PROCESS_REFER_TO_CCOS_SMS_FETCH_LIMIT));
            
            List<Long> msgIds = outgoingSMSDelegator.fetchMessagesId(SendSmsType.REFER_TO_CCOS_SMS.getIntValue(),fetchLimit);
            
            
            if (EmsUtil.checkListSize(msgIds)) {

				for (Long id : msgIds) {
					if (!isJobInterrupted) {
						try {
							outgoingSMSDelegator.processSmsToSend(id);

						} catch (BaseException e) {

							LOGGER.error(e.getExceptionCode() + " : "+ e.getMessage(), e);
							
							if (BizExceptionCode.OSS_002.equals(e.getExceptionCode())
									|| BizExceptionCode.OSS_003.equals(e.getExceptionCode())
									|| BizExceptionCode.OSS_004.equals(e.getExceptionCode()))
								break;
						}
					} else {
						break;
					}
				}
			}
        }catch (BaseException e) {
			LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
        

            
        /**
         * delete older records
         */
		if (Boolean.valueOf(EmsUtil.getProfileValue(
				ProfileKeyName.KEY_DELETE_FROM_MSGT_ENABLE,
				DEFAULT_DELETE_FROM_MSGT_ENABLE))) {
			try {
				PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
				portalManagementDelegator
						.deleteOldRecordsFromMsgt(Integer.valueOf(EmsUtil.getProfileValue(
								ProfileKeyName.KEY_DELETE_FROM_MSGT_TIME_INTERVAL,
								DEFAULT_DELETE_FROM_MSGT_TIME_INTERVAL)), SendSmsType.REFER_TO_CCOS_SMS.getIntValue());

			} catch (BaseException e) {
				LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }

}