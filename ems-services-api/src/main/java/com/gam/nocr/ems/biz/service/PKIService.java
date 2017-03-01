package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CertificateTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.TokenTO;
import com.gam.nocr.ems.data.enums.TokenType;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PKIService extends Service {

    /**
     * The method issuePersonTokenRequest is used to send a request to the sub system 'PKI' for issuing a token for
     * person.
     *
     * @param personTokenTO is an object of type {@link PersonTokenTO}, which carries the required field for
     *                      issuePersonTokenRequest
     * @param certificateTO is an object of type {@link CertificateTO}, which carries the required field for
     *                      signing request and verifying the response
     * @return an object of type {@link PersonTokenTO}
     */
    public PersonTokenTO issuePersonTokenRequest(PersonTokenTO personTokenTO,
                                                 CertificateTO certificateTO) throws BaseException;
    
    public PersonTokenTO activatePersonToken(PersonTokenTO personTokenTO,
            CertificateTO certificateTO) throws BaseException;

    /**
     * The method issueNetworkTokenRequest is used to send a request to the sub system 'PKI' for issuing a token of type
     * network.
     *
     * @param networkTokenTO is an object of type {@link NetworkTokenTO}, which carries the required field for
     *                       issueNetworkTokenRequest
     * @param certificateTO  is an object of type {@link CertificateTO}, which carries the required field for
     *                       signing request and verifying the response
     * @return an object of type {@link NetworkTokenTO}
     */
//    public NetworkTokenTO issueNetworkTokenRequest(NetworkTokenTO networkTokenTO,
//                                                   CertificateTO certificateTO) throws BaseException;
//

    /**
     * The method revokeTokenRequest is used to send a request to the sub system 'PKI' for revoking any type of token.
     *
     * @param tokenTO       is an object of type {@link TokenTO}, which carries the required fields for revocation request
     * @param tokenType     represents the type of token in spite of the enumeration {@link TokenType}
     * @param certificateTO is an object of type {@link CertificateTO}, which carries the required field for
     *                      signing request and verifying the response
     * @param comment
     */
    public void revokeTokenRequest(TokenTO tokenTO,
                                   TokenType tokenType,
                                   CertificateTO certificateTO,
                                   String comment) throws BaseException;

    /**
     * The method searchTokenRequest is used to send a request to the sub system 'PKI' for searching any type of token.
     *
     * @param tokenTO       is an object of type {@link com.gam.nocr.ems.data.domain.TokenTO}, which carries the required
     *                      fields for revocation request
     * @param certificateTO is an object of type {@link CertificateTO}, which carries the required field for
     *                      signing request and verifying the response
     * @param tokenType     represents the type of token in spite of the enumeration {@link TokenType}
     * @return an object of type {@link TokenTO}
     */
    public TokenTO searchTokenRequest(TokenTO tokenTO,
                                      TokenType tokenType,
                                      CertificateTO certificateTO) throws BaseException;
    
    
    public PersonTokenTO renewalPersonTokenRequest(PersonTokenTO personTokenTO,
            CertificateTO certificateTO) throws BaseException;


}
