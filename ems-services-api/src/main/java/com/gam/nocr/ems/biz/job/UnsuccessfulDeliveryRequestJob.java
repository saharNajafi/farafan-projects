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
public class UnsuccessfulDeliveryRequestJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger unsuccessfulDeliveryLogger = BaseLog.getLogger("unsuccessfulDeliveryRequest");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(unsuccessfulDeliveryLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        CardManagementDelegator cardManagementDelegator = new CardManagementDelegator();
        try {
            //identify changed OR forbidden Or death
            Long stopedCardCount = cardManagementDelegator.findRequestsCountByState(CardRequestState.STOPPED);
            info("\n-------------------------------------------------------------------------------------------");
            info("\n-------------------------- job just started fro STOPPED requests --------------------------");
            info("\n-------------------------------------------------------------------------------------------");
            info("\nthe requests count is:" + stopedCardCount);
            Integer indexForStoppedCard = 0;
            for (int i = 0; i < stopedCardCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.processOnRequestsWithIdentifyChanged(indexForStoppedCard);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a damaged unsuccessfull request. So ignore the
                        //  failed request and go to the next one by increasing the start index to load
                        indexForStoppedCard++;
                        logException(e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logGenerakException(e);
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
                        logException(e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logGenerakException(e);
        }

        try {
            // Biometric Problem OR unmatched cut fingers OR unmatched face 
            // OR (unmatched cut fingers AND unmatched face)
            Long biometricCount = 0L;
            if (!isJobInterrupted)
                biometricCount = cardManagementDelegator.findRequestsCountByState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            info("\n-------------------------------------------------------------------------------------------");
            info("\n---- job just started fro UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC requests -------------");
            info("\n-------------------------------------------------------------------------------------------");
            info("\nthe requests count is:" + biometricCount);
            Integer indexForBiometric = 0;
            for (int i = 0; i < biometricCount; i++) {
                if (!isJobInterrupted) {
                    try {
                        cardManagementDelegator.processOnRequestsWithBiometricProblem(indexForBiometric);
                    } catch (BaseException e) {
                        //  An exception happened while trying to process a biometric unsuccessfull request. So
                        //  ignore the failed request and go to the next one by increasing the start index to load
                        indexForBiometric++;
                        logException(e);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logGenerakException(e);
        }
        endLogging();
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
