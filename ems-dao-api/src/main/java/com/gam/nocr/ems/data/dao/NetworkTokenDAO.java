//package com.gam.nocr.ems.data.dao;
//
//import com.gam.commons.core.BaseException;
//import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
//import com.gam.nocr.ems.data.enums.TokenState;
//import com.gam.nocr.ems.data.enums.TokenType;
//
//import java.util.List;
//
///**
// * @author Saeed Jalilian (jalilian@gamelectronics.com)
// */
//public interface NetworkTokenDAO extends EmsBaseDAO<NetworkTokenTO> {
//
//    /**
//     * The method findByState is used to find a list of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} with a
//     * specified tokenState.
//     *
//     * @param tokenState is an enumeration of type {@link com.gam.nocr.ems.data.enums.TokenState}
//     * @return a list of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} or null if there is nothing to be
//     *         found
//     */
//    public List<NetworkTokenTO> findByState(TokenState tokenState) throws BaseException;
//
//    public List<NetworkTokenTO> findByState(TokenState tokenState, Integer from, Integer to) throws BaseException;
//
//    public Long getCountByState(TokenState tokenState) throws BaseException;
//    public List<Long> getIdsByState(TokenState tokenState) throws BaseException;
//
//    /**
//     * The method findDeliveredByEnrollmentOfficeIdAndType is used to find an object of type {@link NetworkTokenTO} in
//     * spite of enrollmentOfficeTO id and tokenType.
//     *
//     * @param enrollmentOfficeTO an object of type {@link EnrollmentOfficeTO}
//     * @return an instance of type ${@link NetworkTokenTO} or null if there is no ${@link NetworkTokenTO} which matches to
//     *         input parameter
//     */
//    public NetworkTokenTO findDeliveredByEnrollmentOfficeIdAndType(EnrollmentOfficeTO enrollmentOfficeTO) throws BaseException;
//
//    /**
//     * The method findByEnrollmentIdAndTypeWhereNotRevoked is used to find an object of type {@link NetworkTokenTO}
//     * which are not revoked , in spite of enrollmentOfficeTO id and tokenType.
//     *
//     * @param enrollmentOfficeId a long value which represents the enrollment id
//     * @param tokenType          an enum type of {@link TokenType}
//     * @return an instance of type ${@link NetworkTokenTO} or null if there is no ${@link NetworkTokenTO} which matches to
//     *         input parameter
//     */
//    public NetworkTokenTO findByEnrollmentIdAndTypeWhereNotRevoked(Long enrollmentOfficeId,
//                                                                   TokenType tokenType) throws BaseException;
//
//    public Long findNotRevokedTokenIdByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;
//
//    /**
//     * The method findByEnrollmentOfficeIdAndState is used to find a specified token regards to its attributes of Enrollment and state
//     *
//     * @param EnrollmentOfficeId is an instance of type {@link Long}, which represents a specified instance of type {@link EnrollmentOfficeTO}
//     * @param tokenState         is an instance of type {@link TokenState}
//     * @return a list of type {@link NetworkTokenTO} or null
//     * @throws BaseException
//     */
//    List<NetworkTokenTO> findByEnrollmentOfficeIdAndState(Long EnrollmentOfficeId,
//                                                          TokenState tokenState) throws BaseException;
//    
//    public NetworkTokenTO findById(Long networkTokenId) throws BaseException;
//}
