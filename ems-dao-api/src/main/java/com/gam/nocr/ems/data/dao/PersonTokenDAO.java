package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PersonTokenDAO extends EmsBaseDAO<PersonTokenTO> {
    /**
     * The method findByState is used to find a list of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO} with a
     * specified tokenState.
     *
     * @param tokenState is an enumeration of type {@link TokenState}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO} or null if there is nothing to be found
     */
    List<PersonTokenTO> findByState(TokenState tokenState) throws BaseException;
    
    //Adldoost
    List<PersonTokenTO> findByState(List<TokenState> tokenStateList) throws BaseException;
    
    //Adldoost
    void approveRewnewalRequest(String ids) throws BaseException;
    
    //Adldoost
    void rejectRewnewalRequest(String ids) throws BaseException;
    
    //Adldoost
    void deliverRewnewalRequest(String ids) throws BaseException;
    
    //Adldoost
    void activateRenewalRequest(String ids) throws BaseException;

    List<PersonTokenTO> findByState(TokenState tokenState, Integer from, Integer to) throws BaseException;

    Long findCountByState(TokenState tokenState, Integer from, Integer to) throws BaseException;

    Long getCountByState(TokenState tokenState) throws BaseException;
    List<Long> getIdsByState(TokenState tokenState) throws BaseException;

    /**
     * The method findDeliveredByPersonIdAndType is used to find an object of type {@link PersonTokenTO} in spite of
     * personId and tokenType.
     *
     * @param personId  an object of type {@link PersonTO}
     * @param tokenType an enum type of {@link TokenType}
     * @return an instance of type ${@link PersonTokenTO} or null if there is no ${@link PersonTokenTO} which matches to
     *         input parameter
     */
    PersonTokenTO findDeliveredByPersonIdAndType(Long personId,
                                                 TokenType tokenType) throws BaseException;

    PersonTokenTO findByPersonIdAndType(PersonTO personTO,
                                        TokenType tokenType) throws BaseException;

    PersonTokenTO findByPersonIdAndTypeWhereNotRevoked(Long personId, TokenType tokenType) throws BaseException;

    List<PersonTokenTO> findNotRevokedByPersonId(Long PersonId) throws BaseException;

    List<PersonTokenTO> findNotRevokedOrDeliveredByPersonIdAndType(Long PersonId, TokenType tokenType) throws BaseException;

    List<PersonTokenTO> findByPersonId(Long personId) throws BaseException;

	PersonTokenTO findById(Long personTokenId) throws BaseException;
	
	PersonTokenTO findByPersonIdAndRequestId(Long personId, String requestId) throws BaseException;
	
	List<PersonTokenTO> findByPersonIdAndTypeAndState(Long personId, TokenType type, List<TokenState> state) throws BaseException;

}
