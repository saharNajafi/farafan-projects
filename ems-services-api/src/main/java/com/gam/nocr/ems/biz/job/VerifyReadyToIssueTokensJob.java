package com.gam.nocr.ems.biz.job;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.nocr.ems.biz.delegator.TokenManagementDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.EmsUtil;

import org.quartz.*;
import org.slf4j.Logger;

/**
 * Processes any token request by sending them to the PKI
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class VerifyReadyToIssueTokensJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(VerifyReadyToIssueTokensJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        logger.info("========================================================");
        logger.info("VerifyReadyToIssueTokensJob execute method is started...");
        TokenManagementDelegator tokenManagementDelegator = new TokenManagementDelegator();
        try {
            // Finding certificate
            CertificateTO certificateTO = tokenManagementDelegator.findCertificateByUsage(CertificateUsage.PKI_SIGN);
            if (certificateTO == null) {
                throw new ServiceException("", "Unsuccessful find. The appropriate certificate with the usage of " + CertificateUsage.PKI_SIGN + "has not found.");
            }

            try {
                // PersonToken
                Long totalPersonTokenCount = tokenManagementDelegator.getCountByState(null, TokenState.READY_TO_ISSUE);
                Integer personTokenIndex = 0;
                for (int i = 0; i < totalPersonTokenCount; i++) {
                    if (!isJobInterrupted) {
                        try {
                            Long tokenId = tokenManagementDelegator.processPersonTokenRequest(personTokenIndex, certificateTO);
                            if (tokenId != null) {
                                try {
                                    tokenManagementDelegator.createBusinessLog(BusinessLogAction.PERSON_TOKEN_REQUEST,
                                            BusinessLogEntity.PKI, "System", "The request has been sent successfully. PersonTokenID : '" + tokenId + "'", true);
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        } catch (BaseException e) {
                            //  An exception happened while trying to process a token token issuance request.
                            //  So ignore the failed request and go to the next one by increasing the start index to
                            //  load
                            personTokenIndex++;
                            logger.error(e.getExceptionCode(), e.getMessage(), e);
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            
            try {
                // RenewalPersonToken
            	String DEFAULT_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD = "1";
                Long totalPersonTokenCount = tokenManagementDelegator.getCountByState(null, TokenState.READY_TO_RENEWAL_ISSUE);
                int autoAccept = 1;
                try
                {
                	//it will ignore 'Pending for ems' requests if and only if autoAccept profile key is 0, else it will append pending for ems requests too
	                autoAccept = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.Auto_Accept_Renewal_Request,
	                        DEFAULT_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD));
                }
                catch(Exception exp)
                {
                	autoAccept = 1;
                }
                if(autoAccept != 0)
                {
                	totalPersonTokenCount += tokenManagementDelegator.getCountByState(null, TokenState.PENDING_FOR_EMS);
                }
                Integer personTokenIndex = 0;
                for (int i = 0; i < totalPersonTokenCount; i++) {
                    if (!isJobInterrupted) {
                        try {
                            //Long tokenId = tokenManagementDelegator.processPersonTokenRequest(personTokenIndex, certificateTO);
                            Long tokenId = tokenManagementDelegator.processPersonTokenRenewalRequest(personTokenIndex, certificateTO);
//                            if (tokenId != null) {
//                                try {
//                                    tokenManagementDelegator.createBusinessLog(BusinessLogAction.PERSON_TOKEN_REQUEST,
//                                            BusinessLogEntity.PKI, "System", "The request has been sent successfully. PersonTokenID : '" + tokenId + "'", true);
//                                } catch (Exception e) {
//                                    logger.error(e.getMessage(), e);
//                                }
//                            }
                        } catch (BaseException e) {
                            //  An exception happened while trying to process a token token issuance request.
                            //  So ignore the failed request and go to the next one by increasing the start index to
                            //  load
                            personTokenIndex++;
                            logger.error(e.getExceptionCode(), e.getMessage(), e);
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            // NetworkToken
            try {
//Commented By Adldoost
//                Long totalNetworkTokenCount = 0L;
//                if (!isJobInterrupted)
//                    totalNetworkTokenCount = tokenManagementDelegator.getCountByState(TokenType.NETWORK, TokenState.READY_TO_ISSUE);
//
//                Integer networkTokenIndex = 0;
//                for (int i = 0; i < totalNetworkTokenCount; i++) {
//                    if (!isJobInterrupted) {
//                        try {
//                            Long tokenId = tokenManagementDelegator.processNetworkTokenRequest(networkTokenIndex, certificateTO);
//                            if (tokenId != null) {
//                                try {
//                                    tokenManagementDelegator.createBusinessLog(BusinessLogAction.NETWORK_TOKEN_REQUEST,
//                                            BusinessLogEntity.PKI, "System", "The request has been sent successfully. NetworkTokenID : '" + tokenId + "'", true);
//                                } catch (Exception e) {
//                                    logger.error(e.getMessage(), e);
//                                }
//                            }
//                        } catch (BaseException e) {
//                            //  An exception happened while trying to process a token token issuance request.
//                            //  So ignore the failed request and go to the next one by increasing the start index to
//                            //  load
//                            networkTokenIndex++;
//                            logger.error(e.getExceptionCode(), e.getMessage(), e);
//                        }
//                    } else {
//                        break;
//                    }
//                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("VerifyReadyToIssueTokensJob execute method is finished.");
        logger.info("=======================================================");
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
