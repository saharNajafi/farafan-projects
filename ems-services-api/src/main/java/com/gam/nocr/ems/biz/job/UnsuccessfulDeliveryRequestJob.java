package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardManagementDelegator;
import com.gam.nocr.ems.data.enums.CardRequestState;
import org.quartz.*;
import org.slf4j.Logger;

/**
 * Processes all unsuccessfull card requests and changing their state back to an appropriate state base on the type of
 * unsuccessfullness :)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class UnsuccessfulDeliveryRequestJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(UnsuccessfulDeliveryRequestJob.class);
    private static final Logger cmsLogger = BaseLog.getLogger("CmsLogger");
    private static final Logger unsuccessfulDeliveryLogger = BaseLog.getLogger("unsuccessfulDeliveryRequest");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        CardManagementDelegator cardManagementDelegator = new CardManagementDelegator();
        try {
        	//identify changed OR forbidden Or death
            Long stopedCardCount = cardManagementDelegator.findRequestsCountByState(CardRequestState.STOPPED);
            unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
            unsuccessfulDeliveryLogger.info("\n-------------------------- job just started fro STOPPED requests --------------------------");
            unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
            unsuccessfulDeliveryLogger.info("\nthe requests count is:"+stopedCardCount);
            Integer indexForStoppedCard = 0;
            for (int i = 0; i < stopedCardCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.processOnRequestsWithIdentifyChanged(indexForStoppedCard);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a damaged unsuccessfull request. So ignore the
                        //  failed request and go to the next one by increasing the start index to load
                    	indexForStoppedCard++;
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        cmsLogger.error(e.getExceptionCode(), e.getMessage(), e);
                        unsuccessfulDeliveryLogger.error(e.getExceptionCode(), e.getMessage(), e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            cmsLogger.error(e.getMessage(), e);
            unsuccessfulDeliveryLogger.error(e.getMessage(), e);
        }

        try {
            // Damaged Cards
            Long damagedCardCount = cardManagementDelegator.findRequestsCountByState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE);
            Integer indexForDamagedCard = 0;
            for (int i = 0; i < damagedCardCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.processOnDamagedCards(indexForDamagedCard);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a damaged unsuccessfull request. So ignore the
                        //  failed request and go to the next one by increasing the start index to load
                        indexForDamagedCard++;
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        cmsLogger.error(e.getExceptionCode(), e.getMessage(), e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            cmsLogger.error(e.getMessage(), e);
        }

        try {
            // Biometric Problem OR unmatched cut fingers OR unmatched face 
        	// OR (unmatched cut fingers AND unmatched face)
            Long biometricCount = 0L;
            if (!isJobInterrupted)
                biometricCount = cardManagementDelegator.findRequestsCountByState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
            unsuccessfulDeliveryLogger.info("\n---- job just started fro UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC requests -------------");
            unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
            unsuccessfulDeliveryLogger.info("\nthe requests count is:"+biometricCount);
            Integer indexForBiometric = 0;
            for (int i = 0; i < biometricCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.processOnRequestsWithBiometricProblem(indexForBiometric);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a biometric unsuccessfull request. So
                        //  ignore the failed request and go to the next one by increasing the start index to load
                        indexForBiometric++;
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        cmsLogger.error(e.getExceptionCode(), e.getMessage(), e);
                        unsuccessfulDeliveryLogger.error(e.getExceptionCode(), e.getMessage(), e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            cmsLogger.error(e.getMessage(), e);
            unsuccessfulDeliveryLogger.error(e.getMessage(), e);
        }
     
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
