package com.gam.nocr.ems.biz.delegator;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.TokenManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.vol.PersonTokenVTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.EmsUtil;


/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class TokenManagementDelegator implements Delegator {

    private TokenManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        TokenManagementService tokenManagementService = null;
        try {
            tokenManagementService = (TokenManagementService) factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_TOKEN_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.TMD_001,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_TOKEN_MANAGEMENT.split(","));
        }
        tokenManagementService.setUserProfileTO(userProfileTO);
        return tokenManagementService;
    }

    public Long getCountByState(TokenType tokenType, TokenState tokenState) throws BaseException {
        return getService(null).getCountByState(tokenType, tokenState);
    }
    
    public List<Long> getIdsByState(TokenType tokenType, TokenState tokenState) throws BaseException {
        return getService(null).getIdsByState(tokenType, tokenState);
    }

    public Long processPersonTokenRequest(Integer from, CertificateTO certificateTO) throws BaseException {
        return getService(null).ProcessPersonTokenRequest(from, certificateTO);
    }

    public Long processPersonTokenRenewalRequest(Integer from, CertificateTO certificateTO) throws BaseException {
        return getService(null).ProcessPersonTokenRenewalRequest(from, certificateTO);
    }
//Commented By Adldoost
//    public Long processNetworkTokenRequest(Integer from, CertificateTO certificateTO) throws BaseException {
//        return getService(null).ProcessNetworkTokenRequest(from, certificateTO);
//    }

    public CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException {
        return getService(null).findCertificateByUsage(certificateUsage);
    }

    public void createBusinessLog(BusinessLogAction logAction,
                                  BusinessLogEntity logEntityName,
                                  String logActor,
                                  String additionalData,
                                  Boolean exceptionFlag) throws BaseException {
        getService(null).createBusinessLog(logAction, logEntityName, logActor, additionalData, exceptionFlag);
    }

//    public Long processPersonTokenResponse(Integer from, CertificateTO certificateTO) throws BaseException{
//        return getService(null).processPersonTokenResponse(from, certificateTO);
//    }
    
    public Long processPersonTokenResponse(Long personTokenId, CertificateTO certificateTO) throws BaseException{
        return getService(null).processPersonTokenResponse(personTokenId, certificateTO);
    }

//    public Long processNetworkTokenResponse(Integer from, CertificateTO certificateTO) throws BaseException{
//        return getService(null).processNetworkTokenResponse(from, certificateTO);
//    }

//By Adldoost
//    public Long processNetworkTokenResponse(Long networkTokenId, CertificateTO certificateTO) throws BaseException{
//        return getService(null).processNetworkTokenResponse(networkTokenId, certificateTO);
//    }
    
    //Adldoost
    public PersonTokenTO findPersonTokenByPersonAndTokenType(UserProfileTO userProfileTO, TokenType tokenType) throws BaseException{
    	return getService(userProfileTO).findDeliveredPersonTokenByTypeAndPerson(userProfileTO,tokenType);
    }
    
    //Adldoost
    public List<PersonTokenVTO> findPendingToEMSAndRejectByEMS(UserProfileTO userProfileTO)throws BaseException{
    	return getService(userProfileTO).findPendingToEMSAndRejectByEMS();
    }
    
    //Adldoost
    public void approveRewnewalRequest(UserProfileTO userProfileTO, String ids)throws BaseException
    {
    	getService(userProfileTO).approveRenewalRequest(ids);
    }
    
    //Adldoost
    public void rejectRewnewalRequest(UserProfileTO userProfileTO, String ids)throws BaseException
    {
    	getService(userProfileTO).rejectRenewalRequest(ids);
    }
    
    //Adldoost
    public void deliverRewnewalRequest(UserProfileTO userProfileTO, String ids)throws BaseException
    {
    	getService(userProfileTO).deliverRenewalRequest(ids);
    }
    
    //Adldoost
    public void activateRewnewalRequest(UserProfileTO userProfileTO, String ids)throws BaseException
    {
    	getService(userProfileTO).activateRenewalRequest(ids);
    }
    
  //Adldoost
    public void revokeRewnewalRequest(UserProfileTO userProfileTO, long id)throws BaseException
    {
    	getService(userProfileTO).revokePersonToken(id, ReplicaReason.RENEW, null);
    }
    
    //Adldoost
    public PersonTokenTO findPersonTokenById(UserProfileTO userProfileTO, Long id) throws BaseException{
    	return getService(userProfileTO).findPersonTokenById(id);
    }
    
    //Adldoost
    public List<PersonTokenTO> findPersonTokenRenewalRequest(UserProfileTO userProfileTO) throws BaseException{
    	return getService(userProfileTO).findPersonTokenRenewalRequest(userProfileTO);
    }
}
