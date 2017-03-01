package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenType;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface TokenService extends Service {

    Long issueToken(Long entityId, TokenType type) throws BaseException;

    Long reissueToken(Long enrollmentOfficeId) throws BaseException;
    
    Long renewalToken(Long enrollmentOfficeId) throws BaseException;

    Long replicateToken(Long entityId, TokenType tokenType, ReplicaReason reason) throws BaseException;

    void deliverToken(Long enrollmentOfficeId) throws BaseException;

    void revokeToken(Long enrollmentOfficeId, ReplicaReason reason, String comment) throws BaseException;

    void deleteToken(Long tokenId) throws BaseException;

    void deleteRenewalTokenRequest(Long tokenId) throws BaseException;
    
}
