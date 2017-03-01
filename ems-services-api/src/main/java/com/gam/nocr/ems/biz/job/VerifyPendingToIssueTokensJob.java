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
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.nocr.ems.biz.delegator.TokenManagementDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;

/**
 * Verifies all token requests that are in pending issuance state by checking their latest state in PKI
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class VerifyPendingToIssueTokensJob implements InterruptableJob {

    private static final Logger logger = BaseLog.getLogger(VerifyPendingToIssueTokensJob.class);

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobKey = jobExecutionContext.getJobDetail().getKey();

        logger.info("==========================================================");
        logger.info("VerifyPendingToIssueTokensJob execute method is started...");
        TokenManagementDelegator tokenManagementDelegator = new TokenManagementDelegator();
        try {
            // Finding certificate
            CertificateTO certificateTO = tokenManagementDelegator.findCertificateByUsage(CertificateUsage.PKI_SIGN);
            if (certificateTO == null) {
                throw new ServiceException("", "Unsuccessful find. The appropriate certificate with the usage of " + CertificateUsage.PKI_SIGN + "has not found.");
            }

            try {
                // PersonToken
                List<Long> personTokenIds = tokenManagementDelegator.getIdsByState(null, TokenState.PENDING_TO_ISSUE);
                if (personTokenIds != null){
                	for (Long ptid : personTokenIds) {
                		if (!isJobInterrupted) {
                            try {
                                Long tokenId = tokenManagementDelegator.processPersonTokenResponse(ptid, certificateTO);
                                if (tokenId != null) {
                                    try {
                                        tokenManagementDelegator.createBusinessLog(BusinessLogAction.PERSON_TOKEN_RESPONSE, BusinessLogEntity.PKI, "System", "The Token has been created successfully. PersonTokenID : '" + tokenId + "'", true);
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                    }
                                }
                            } catch (BaseException e) {
                                logger.error(e.getExceptionCode(), e.getMessage(), e);
                            }
                        } else {
                            break;
                        }
					}
                }
                
                
//                Integer personTokenIndex = 0;
//                for (int i = 0; i < totalPersonTokenCount; i++) {
//                    if (!isJobInterrupted) {
//                        try {
//                            Long tokenId = tokenManagementDelegator.processPersonTokenResponse(personTokenIndex, certificateTO);
//                            if (tokenId != null) {
//                                try {
//                                    tokenManagementDelegator.createBusinessLog(BusinessLogAction.PERSON_TOKEN_RESPONSE, BusinessLogEntity.PKI, "System", "The Token has been created successfully. PersonTokenID : '" + tokenId + "'", true);
//                                } catch (Exception e) {
//                                    logger.error(e.getMessage(), e);
//                                }
//                            }
//                        } catch (BaseException e) {
//                            //  An exception happened while trying to process an already pending issuance token request.
//                            //  So ignore the failed request and go to the next one by increasing the start index to
//                            //  load
//                            personTokenIndex++;
//                            logger.error(e.getExceptionCode(), e.getMessage(), e);
//                        }
//                    } else {
//                        break;
//                    }
//                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            
            try {
                // PersonRenewalToken
                List<Long> personTokenIds = tokenManagementDelegator.getIdsByState(null, TokenState.PENDING_TO_RENEWAL_ISSUE);
                if (personTokenIds != null){
                	for (Long ptid : personTokenIds) {
                		if (!isJobInterrupted) {
                            try {
                                Long tokenId = tokenManagementDelegator.processPersonTokenResponse(ptid, certificateTO);
//                                if (tokenId != null) {
//                                    try {
//                                        tokenManagementDelegator.createBusinessLog(BusinessLogAction.PERSON_TOKEN_RESPONSE, BusinessLogEntity.PKI, "System", "The Token has been created successfully. PersonTokenID : '" + tokenId + "'", true);
//                                    } catch (Exception e) {
//                                        logger.error(e.getMessage(), e);
//                                    }
//                                }
                            } catch (BaseException e) {
                                logger.error(e.getExceptionCode(), e.getMessage(), e);
                            }
                        } else {
                            break;
                        }
					}
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            try {
                // NetworkToken
//Commented By Adldoost            	
//            	List<Long> networkTokenIds = tokenManagementDelegator.getIdsByState(TokenType.NETWORK, TokenState.PENDING_TO_ISSUE);
//            	if (networkTokenIds != null){
//            		for (Long ntid : networkTokenIds) {
//            			  if (!isJobInterrupted) {
//                              try {
//                                  Long tokenId = tokenManagementDelegator.processNetworkTokenResponse(ntid, certificateTO);
//                                  if (tokenId != null) {
//                                      try {
//                                          tokenManagementDelegator.createBusinessLog(BusinessLogAction.NETWORK_TOKEN_RESPONSE, BusinessLogEntity.PKI, "System", "The Token has been updated successfully. NetworkTokenID : '" + tokenId + "'", true);
//                                      } catch (Exception e) {
//                                          logger.error(e.getMessage(), e);
//                                      }
//                                  }
//                              } catch (BaseException e) {
//                                  logger.error(e.getExceptionCode(), e.getMessage(), e);
//                              }
//                          } else {
//                              break;
//                          }
//					}
//            	}
            	
            	
//                Long totalNetworkTokenCount = 0L;
//                if (!isJobInterrupted)
//                    totalNetworkTokenCount = tokenManagementDelegator.getCountByState(TokenType.NETWORK, TokenState.PENDING_TO_ISSUE);
//
//                Integer networkTokenIndex = 0;
//                for (int i = 0; i < totalNetworkTokenCount; i++) {
//                    if (!isJobInterrupted) {
//                        try {
//                            Long tokenId = tokenManagementDelegator.processNetworkTokenResponse(networkTokenIndex, certificateTO);
//                            if (tokenId != null) {
//                                try {
//                                    tokenManagementDelegator.createBusinessLog(BusinessLogAction.NETWORK_TOKEN_RESPONSE, BusinessLogEntity.PKI, "System", "The Token has been updated successfully. NetworkTokenID : '" + tokenId + "'", true);
//                                } catch (Exception e) {
//                                    logger.error(e.getMessage(), e);
//                                }
//                            }
//                        } catch (BaseException e) {
//                            //  An exception happened while trying to process an already pending issuance token request.
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
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        }
        logger.info("VerifyPendingToIssueTokensJob execute method is finished.");
        logger.info("=========================================================");
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.err.println("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
