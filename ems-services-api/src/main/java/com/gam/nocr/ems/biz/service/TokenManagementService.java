package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.CertificateTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.vol.PersonTokenVTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenOrigin;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface TokenManagementService extends Service {

    /**
     * The method issueFirstPersonToken is used to save an object of type ${link PersonTokenTO} as the first token
     *
     * @param entityId  is the id of an instance of type {@link PersonTO}
     * @param tokenType ia an enumeration of type {@link TokenType}, which demonstrates the
     *                  type of Token
     * @return A number of type long which represents the identification of the object of type {@link
     * PersonTokenTO} which was recorded on database
     */
    long issueFirstPersonToken(long entityId,
                               TokenType tokenType) throws BaseException;


    /**
     * The method issueFirstNetworkToken is used to save an object of type ${link NetworkTokenTO} as the first token
     *
     * @param entityId  is the id of an instance of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @param tokenType ia an enumeration of type {@link TokenType}, which demonstrates the
     *                  type of Token
     * @return A number of type long which represents the identification of the object of type {@link
     * NetworkTokenTO} which was recorded on database
     */
//    long issueFirstNetworkToken(long entityId,
//                                TokenType tokenType) throws BaseException;

    /**
     * The method reIssuePersonTokenRequest is used to handle the request of reissuing token for a specified person
     *
     * @param tokenId represents the id of previous token
     * @return A number of type long which represents the identification of the object of type {@link PersonTokenTO}
     * which was recorded on database
     */
    Long reIssuePersonTokenRequest(Long tokenId) throws BaseException;
    
    public Long renewalPersonTokenRequest(Long tokenId) throws BaseException;

    /**
     * The method reIssueNetworkTokenRequest is used to handle the request of reissuing token of type NETWORK
     *
     * @param tokenId represents the id of previous token
     * @return A number of type long which represents the identification of the object of type {@link NetworkTokenTO}
     * which was recorded on database
     */
//    Long reIssueNetworkTokenRequest(Long tokenId) throws BaseException;

    /**
     * The method replicatePersonToken is used to handle the request of replicating for a token for a specified person.
     *
     * @param entityId  is the id of an instance of type {@link com.gam.nocr.ems.data.domain.PersonTO}
     * @param tokenType represents the type of previous token
     * @param reason    represents the reason of replication which is an enum type of {@link ReplicaReason}
     * @return A number of type long which represents the identification of the object of type {@link PersonTokenTO}
     * which was recorded on database
     */
    Long replicatePersonToken(Long entityId,
                              TokenType tokenType,
                              ReplicaReason reason) throws BaseException;

    /**
     * The method replicateNetworkToken is used to handle the request of replicating for a token of type NETWORK.
     *
     * @param entityId  is the id of an instance of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @param tokenType represents the type of previous token
     * @param reason    represents the reason of replication which is an enum type of {@link ReplicaReason}
     * @return A number of type long which represents the identification of the object of type {@link NetworkTokenTO}
     * which was recorded on database
     */
//    Long replicateNetworkToken(Long entityId,
//                               TokenType tokenType,
//                               ReplicaReason reason) throws BaseException;

    /**
     * The method revokePersonToken is used to handle the request of revocation for any types of person token.
     *
     * @param tokenId represents the id of the token which should be revoked
     * @param reason  represents the reason of revocation which is an instance of type {@link String}
     * @param comment
     */
    void revokePersonToken(long tokenId,
                           ReplicaReason reason,
                           String comment) throws BaseException;

    /**
     * The method revokePersonToken is used to handle the request of revocation for any types of Person token.
     *
     * @param tokenId represents the id of the token which should be revoked
     * @param reason  represents the reason of revocation which is an instance of type {@link ReplicaReason}
     * @param comment
     */
//    void revokeNetworkToken(long tokenId,
//                            ReplicaReason reason,
//                            String comment) throws BaseException;

    /**
     * The method deliverPersonToken is used to handle the request of delivering for changing the state of the person
     * token.
     *
     * @param tokenId represents the id of previous token
     */
    void deliverPersonToken(Long tokenId) throws BaseException;

    /**
     * The method deliverNetworkToken is used to handle the request of delivering for changing the state of the token of
     * type NETWORK.
     *
     * @param tokenId represents the id of the specified token
     */
//    void deliverNetworkToken(Long tokenId) throws BaseException;

    /**
     * <br>The method verifyReadyToIssueTokens do these work :</br>
     * <br>1. Find the tokens with the state of 'READY_TO_ISSUE'</br>
     * <br>2. send token issuance request to the sub system 'PKI'</br>
     * <br>3. Change the state of tokens to 'PENDING_TO_ISSUE'</br>
     */
//    void verifyReadyToIssueTokens() throws BaseException;

    /**
     * <br>The method verifyPendingToIssueTokens do these work :</br>
     * <br>1. calling the search method of the sub system 'PKI'. If the specified token is created, then</br>
     * <br>2. Change the state of tokens from 'PENDING_TO_ISSUE' to the state 'READY_TO_DELIVER'</br>
     */
//    void verifyPendingToIssueTokens() throws BaseException;

    /**
     * The method deleteToken is used to delete an instance of type {@link com.gam.nocr.ems.data.domain.TokenTO}
     *
     * @param tokenId     is an instance of type {@link Long}, which represents the id of a specified token
     * @param tokenOrigin is an enumeration of type {@link TokenOrigin}
     * @throws BaseException
     */
    void deleteToken(Long tokenId,
                     TokenOrigin tokenOrigin) throws BaseException;

    Long getCountByState(TokenType tokenType, TokenState tokenState) throws BaseException;
    List<Long> getIdsByState(TokenType tokenType, TokenState tokenState) throws BaseException;

    CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException;

    void createBusinessLog(BusinessLogAction logAction, BusinessLogEntity logEntityName, String logActor, String additionalData, Boolean exceptionFlag) throws BaseException;

    Long ProcessPersonTokenRequest(Integer from, CertificateTO certificateTO) throws BaseException;
    
    Long ProcessPersonTokenRenewalRequest(Integer from, CertificateTO certificateTO) throws BaseException;

//    Long ProcessNetworkTokenRequest(Integer from, CertificateTO certificateTO) throws BaseException;

//    Long processPersonTokenResponse(Integer from, CertificateTO certificateTO) throws BaseException;
    Long processPersonTokenResponse(Long personTokenId, CertificateTO certificateTO) throws BaseException;
    
//    Long processNetworkTokenResponse(Integer from, CertificateTO certificateTO) throws BaseException;
//    Long processNetworkTokenResponse(Long networkTokenId, CertificateTO certificateTO) throws BaseException;
    
     //Adldoost
     PersonTokenTO findDeliveredPersonTokenByTypeAndPerson(UserProfileTO userProfileTO, TokenType tokenType) throws BaseException;
     
     //Adldoost
     PersonTokenTO findPersonTokenById(Long id) throws BaseException;
     
     //Adldoost
     List<PersonTokenTO> findPersonTokenRenewalRequest(UserProfileTO userProfileTO) throws BaseException;
     
     //Adldoost
     List<PersonTokenVTO> findPendingToEMSAndRejectByEMS() throws BaseException;
     
     //Adldoost
     void approveRenewalRequest(String ids) throws BaseException;
     
     //Adldoost
     void rejectRenewalRequest(String ids) throws BaseException;
     
     //Adldoost
     //edited Madanipour
     void deliverRenewalRequest(String ids) throws BaseException;
     
     //Adldoost
     void activateRenewalRequest(String ids) throws BaseException;
}
