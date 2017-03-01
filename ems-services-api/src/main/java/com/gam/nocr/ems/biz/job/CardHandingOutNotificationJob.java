package com.gam.nocr.ems.biz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;

import org.quartz.*;
import org.slf4j.Logger;

/**
 * Notifies the CMS about successfully delivered cards
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CardHandingOutNotificationJob implements InterruptableJob {

    private static final Logger LOGGER = BaseLog.getLogger(CardHandingOutNotificationJob.class);
    private static final Logger jobLOGGER = BaseLog.getLogger("cardHandingOutNotification");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobLOGGER.info("The process of notifying card handout in CardHandingOutNotificationJob has been started...");
        LOGGER.info("The process of notifying card handout in CardHandingOutNotificationJob has been started...");
        jobKey = jobExecutionContext.getJobDetail().getKey();
        
        String DEFAULT_NUMBER_OF_HandingOut_ASYNC_CALL_COUNT = "10";
        String DEFAULT_NUMBER_OF_HandingOut_FetchLimit = "1000";
		String DEFAULT_ASYNC_ENABLE = "1";

		String isAsyncEnable = EmsUtil.getProfileValue(ProfileKeyName.KEY_ASYNC_ENABLE_HANDOUT, DEFAULT_ASYNC_ENABLE);

		Integer handingOutAsyncCallCount = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_HandOut_ASYNC_CALL_COUNT,
						DEFAULT_NUMBER_OF_HandingOut_ASYNC_CALL_COUNT));
		
		Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_REQUEST_FOR_NOTIFICATION_CARD_FOR_IMS_CMS,
				DEFAULT_NUMBER_OF_HandingOut_FetchLimit));		
		
        
        try {
        	CardManagementDelegator cardManagementDelegator = new CardManagementDelegator();
			List<Long> cardRequestIdList = cardManagementDelegator.findRequestsIdByState(CardRequestState.PENDING_TO_DELIVER_BY_CMS,fetchLimit);
			
			int asyncCallCount = 0;
			int finishedThreads = 0;
			int errorThreads = 0;

			List<Future<String>> currentSyncResult = new ArrayList<Future<String>>();
			List<Future<String>> succAsyncResult = new ArrayList<Future<String>>();
			List<Future<String>> failAsyncResult = new ArrayList<Future<String>>();
			
			
			//******************"*
			
			if (isAsyncEnable.equals("1")) {
				if (cardRequestIdList != null) {
					for (Long requestId : cardRequestIdList) {
						if (!isJobInterrupted) {
							try {								
								Future<String> res = cardManagementDelegator.notifyCardsHandedOutAsync(requestId);
								
								if (res != null) {
									currentSyncResult.add(res);
									asyncCallCount++;
								}
								while ((currentSyncResult.size() >= handingOutAsyncCallCount)
										&& !isJobInterrupted) {

									for (Future<String> f : currentSyncResult) {
										if (f.isDone() == true) {
											try {
												String tmp = f.get();
												finishedThreads++;
												succAsyncResult.add(f);
											} catch (Exception e) {
												errorThreads++;
												failAsyncResult.add(f);
												LOGGER.error(
														BizExceptionCode.CHO_01,
														" exception on calling handing out job thread async : "
																+ e.getMessage(),
														e);
												
												jobLOGGER.error(
														BizExceptionCode.CHO_01,
														" exception on calling handing out job thread async : "
																+ e.getMessage(),
														e);
											}
										}
									}

									for (Future<String> f : succAsyncResult) {
										currentSyncResult.remove(f);
									}

									for (Future<String> f : failAsyncResult) {
										currentSyncResult.remove(f);
									}

									succAsyncResult.clear();
									failAsyncResult.clear();

									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
									}
								}

							} catch (Exception e) {								
								LOGGER.error(e.getMessage(), e);
								jobLOGGER.error(e.getMessage(), e);
								break;
							}
						} else {
							break;
						}
					}

					
					while ((currentSyncResult.size() > 0 && currentSyncResult
							.size() <= handingOutAsyncCallCount)) {

						for (Future<String> f : currentSyncResult) {
							if (f.isDone() == true) {
								try {
									String tmp = f.get();
									finishedThreads++;
									succAsyncResult.add(f);
								} catch (Exception e) {
									errorThreads++;
									failAsyncResult.add(f);
									LOGGER.error(BizExceptionCode.CHO_02,
											" exception on calling HandingOut job thread async : "
													+ e.getMessage(), e);
								}
							}
						}

						for (Future<String> f : succAsyncResult) {
							currentSyncResult.remove(f);
						}

						for (Future<String> f : failAsyncResult) {
							currentSyncResult.remove(f);
						}

						succAsyncResult.clear();
						failAsyncResult.clear();

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}

			}//End if isAsyncEnable	
			else{
			
			//********************				
				for (Long cardRequestId : cardRequestIdList) {
					 if (!isJobInterrupted) {			 					 
						 try {
							cardManagementDelegator.notifyCardsHandedOut(cardRequestId);
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
	                        jobLOGGER.info("An exception happened within notifying card handout. " + e.getMessage(), e);
						}					 					 
					 }else
					 {
						 break;
					 }
					
				}				
				
			}
			
			LOGGER.info("handingOut job finished on : " + new Date()
			+ " and all threads = " + asyncCallCount
			+ " and success threads = " + finishedThreads
			+ " and exception threads = " + errorThreads);
			
			
			jobLOGGER.info("handingOut job finished on : " + new Date()
			+ " and all threads = " + asyncCallCount
			+ " and success threads = " + finishedThreads
			+ " and exception threads = " + errorThreads);
			
		} catch (BaseException e) {
			LOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
			jobLOGGER.error(e.getExceptionCode() + " : " + e.getMessage(), e);
		}        
        
        jobLOGGER.info("The process of notifying card handout in CardHandingOutNotificationJob has been finished.");
        LOGGER.info("The process of notifying card handout in CardHandingOutNotificationJob has been finished.");
        

    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
