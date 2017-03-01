//package com.gam.nocr.ems.data.dao.impl;
//
//import java.util.List;
//
//import javax.ejb.Local;
//import javax.ejb.Remote;
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import com.gam.commons.core.BaseException;
//import com.gam.commons.core.data.DataException;
//import com.gam.commons.core.data.dao.DAOException;
//import com.gam.nocr.ems.config.DataExceptionCode;
//import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
//import com.gam.nocr.ems.data.enums.TokenState;
//import com.gam.nocr.ems.data.enums.TokenType;
//import com.gam.nocr.ems.util.EmsUtil;
//
///**
// * @author Saeed Jalilian (jalilian@gamelectronics.com)
// */
//
//@Stateless(name = "NetworkTokenDAO")
//@Local(NetworkTokenDAOLocal.class)
//@Remote(NetworkTokenDAORemote.class)
//public class NetworkTokenDAOImpl extends EmsBaseDAOImpl<NetworkTokenTO> implements NetworkTokenDAOLocal, NetworkTokenDAORemote {
//
//    private static final String UNIQUE_KEY_NET_TKN_EOF_STAT = "AK_NET_TKN_EOF_STAT";
//
//    @Override
//    @PersistenceContext(unitName = "EmsOraclePU")
//    public void setEm(EntityManager em) {
//        this.em = em;
//    }
//
//    @Override
//    public NetworkTokenTO create(NetworkTokenTO networkTokenTO) throws BaseException {
//        try {
//            NetworkTokenTO nto = super.create(networkTokenTO);
//            em.flush();
//            return nto;
//        } catch (Exception e) {
//            String err = e.getMessage();
//            if (e.getCause() != null) {
//                if (e.getCause().getCause() != null)
//                    err = e.getCause().getCause().getMessage();
//                else
//                    err = e.getCause().getMessage();
//            }
//            if (err.contains(UNIQUE_KEY_NET_TKN_EOF_STAT))
//                throw new DAOException(DataExceptionCode.NTI_001, DataExceptionCode.NTI_001_MSG, e);
//            else
//                throw new DAOException(DataExceptionCode.NTI_012, DataExceptionCode.GLB_004_MSG, e);
//        }
//    }
//
//    @Override
//    public NetworkTokenTO find(Class type, Object id) throws BaseException {
//        try {
//            return super.find(type, id);
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_002, DataExceptionCode.GLB_002_MSG, e);
//        }
//    }
//
//    @Override
//    public NetworkTokenTO update(NetworkTokenTO networkTokenTO) throws BaseException {
//        try {
//            NetworkTokenTO nto = super.update(networkTokenTO);
//            em.flush();
//            return nto;
//        } catch (Exception e) {
//            String err = e.getMessage();
//            if (e.getCause() != null) {
//                if (e.getCause().getCause() != null)
//                    err = e.getCause().getCause().getMessage();
//                else
//                    err = e.getCause().getMessage();
//            }
//            if (err.contains(UNIQUE_KEY_NET_TKN_EOF_STAT))
//                throw new DAOException(DataExceptionCode.NTI_003, DataExceptionCode.NTI_001_MSG, e);
//            else
//                throw new DAOException(DataExceptionCode.NTI_013, DataExceptionCode.GLB_004_MSG, e);
//        }
//    }
//
//    @Override
//    public void delete(NetworkTokenTO networkTokenTO) throws BaseException {
//        try {
//            super.delete(networkTokenTO);
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_004, DataExceptionCode.GLB_004_MSG, e);
//        }
//    }
//
//    /**
//     * The method findByState is used to find a list of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} with a
//     * specified tokenState.
//     *
//     * @param tokenState is an enumeration of type {@link com.gam.nocr.ems.data.enums.TokenState}
//     * @return a list of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} or null if there is nothing to be
//     * found
//     */
//    @Override
//    public List<NetworkTokenTO> findByState(TokenState tokenState) throws BaseException {
//        List<NetworkTokenTO> networkTokenTOList;
//        try {
//            networkTokenTOList = em.createQuery("SELECT NTT FROM NetworkTokenTO NTT " +
//                    "WHERE NTT.state = :token_state", NetworkTokenTO.class)
//                    .setParameter("token_state", tokenState)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_008, DataExceptionCode.GLB_005_MSG, e);
//        }
//        return networkTokenTOList;
//    }
//
//    @Override
//    public List<NetworkTokenTO> findByState(TokenState tokenState, Integer from, Integer to) throws BaseException {
//        List<NetworkTokenTO> networkTokenTOList;
//        try {
//            networkTokenTOList = em.createQuery("SELECT NTT FROM NetworkTokenTO NTT " +
//                    "WHERE NTT.state = :token_state", NetworkTokenTO.class)
//                    .setParameter("token_state", tokenState)
//                    .setFirstResult(from)
//                    .setMaxResults(to)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_016, DataExceptionCode.GLB_005_MSG, e);
//        }
//        return networkTokenTOList;
//    }
//
//    @Override
//    public Long getCountByState(TokenState tokenState) throws BaseException {
//        List<Long> networkTokenCountList;
//        try {
//            networkTokenCountList = em.createQuery("SELECT COUNT(NTT.id) FROM NetworkTokenTO NTT " +
//                    "WHERE NTT.state = :token_state", Long.class)
//                    .setParameter("token_state", tokenState)
//                    .getResultList();
//            if (EmsUtil.checkListSize(networkTokenCountList)) {
//                return networkTokenCountList.get(0);
//            }
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_015, DataExceptionCode.GLB_005_MSG, e);
//        }
//        return null;
//    }
//    
//    @Override
//    public List<Long> getIdsByState(TokenState tokenState) throws BaseException {
//        List<Long> networkTokenIdsList;
//        try {
//        	networkTokenIdsList = em.createQuery("SELECT NTT.id FROM NetworkTokenTO NTT " +
//                    "WHERE NTT.state = :token_state", Long.class)
//                    .setParameter("token_state", tokenState)
//                    .getResultList();
//            return networkTokenIdsList;
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_015, DataExceptionCode.GLB_005_MSG, e);
//        }
//    }
//
//    /**
//     * The method findDeliveredByEnrollmentOfficeIdAndType is used to find an object of type {@link
//     * com.gam.nocr.ems.data.domain.NetworkTokenTO} in
//     * spite of enrollmentOfficeTO id and tokenType.
//     *
//     * @param enrollmentOfficeTO an object of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
//     * @return an instance of type ${@link com.gam.nocr.ems.data.domain.NetworkTokenTO} or null if there is no ${@link
//     * com.gam.nocr.ems.data.domain.NetworkTokenTO} which matches to
//     * input parameter
//     */
//    @Override
//    public NetworkTokenTO findDeliveredByEnrollmentOfficeIdAndType(EnrollmentOfficeTO enrollmentOfficeTO) throws BaseException {
//        List<NetworkTokenTO> networkTokenTOList;
//        try {
//            networkTokenTOList = em.createQuery("SELECT NTT FROM NetworkTokenTO NTT WHERE " +
//                    "NTT.enrollmentOffice = :enrollment_Office AND " +
//                    "NTT.state = :token_state", NetworkTokenTO.class)
//                    .setParameter("enrollment_Office", enrollmentOfficeTO)
//                    .setParameter("token_state", TokenState.DELIVERED).getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_009, DataExceptionCode.GLB_005_MSG, e);
//        }
//        if (networkTokenTOList == null || networkTokenTOList.size() == 0) {
//            return null;
//        } else {
//            return networkTokenTOList.get(0);
//        }
//    }
//
//    /**
//     * The method findByEnrollmentIdAndTypeWhereNotRevoked is used to find an object of type {@link
//     * com.gam.nocr.ems.data.domain.NetworkTokenTO}
//     * which are not revoked , in spite of enrollmentOfficeTO id and tokenType.
//     *
//     * @param enrollmentOfficeId a long value which represents the enrollment id
//     * @param tokenType          an enum type of {@link com.gam.nocr.ems.data.enums.TokenType}
//     * @return an instance of type ${@link com.gam.nocr.ems.data.domain.NetworkTokenTO} or null if there is no ${@link
//     * com.gam.nocr.ems.data.domain.NetworkTokenTO} which matches to
//     * input parameter
//     */
//    @Override
//    public NetworkTokenTO findByEnrollmentIdAndTypeWhereNotRevoked(Long enrollmentOfficeId,
//                                                                   TokenType tokenType) throws BaseException {
//        List<NetworkTokenTO> networkTokenTOList;
//        try {
//            networkTokenTOList = em.createQuery("SELECT NTT FROM NetworkTokenTO NTT" +
//                    " where NTT.enrollmentOffice.id = :ENROLLMENT_OFFICE_ID " +
//                    " and NTT.state <> '" + TokenState.REVOKED + "'", NetworkTokenTO.class)
//                    .setParameter("ENROLLMENT_OFFICE_ID", enrollmentOfficeId)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_010, DataExceptionCode.GLB_005_MSG, e);
//        }
//
//        if (EmsUtil.checkListSize(networkTokenTOList))
//            return networkTokenTOList.get(0);
//        else
//            return null;
//    }
//
//    @Override
//    public Long findNotRevokedTokenIdByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException {
//        List<Long> tokenIdList;
//        try {
//            tokenIdList = em.createQuery(" select NTT.id from NetworkTokenTO NTT " +
//                    " where NTT.enrollmentOffice.id = :enrollmentOfficeId " +
//                    " and NTT.state <> :state ", Long.class)
//                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
//                    .setParameter("state", TokenState.REVOKED)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_011, DataExceptionCode.GLB_005_MSG, e);
//        }
//
//        if (EmsUtil.checkListSize(tokenIdList))
//            return tokenIdList.get(0);
//        else
//            return null;
//    }
//
//    /**
//     * The method findByEnrollmentOfficeIdAndState is used to find a specified token regards to its attributes of Enrollment and state
//     *
//     * @param enrollmentOfficeId is an instance of type {@link Long}, which represents a specified instance of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
//     * @param tokenState         is an instance of type {@link com.gam.nocr.ems.data.enums.TokenState}
//     * @return a list of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} or null
//     * @throws com.gam.commons.core.BaseException
//     */
//    @Override
//    public List<NetworkTokenTO> findByEnrollmentOfficeIdAndState(Long enrollmentOfficeId,
//                                                                 TokenState tokenState) throws BaseException {
//        List<NetworkTokenTO> networkTokenTOList;
//        try {
//            networkTokenTOList = em.createQuery("" +
//                    "SELECT NTT FROM NetworkTokenTO NTT" +
//                    " where NTT.enrollmentOffice.id = :ENROLLMENT_OFFICE_ID " +
//                    " and NTT.state = :TOKEN_STATE ", NetworkTokenTO.class)
//                    .setParameter("ENROLLMENT_OFFICE_ID", enrollmentOfficeId)
//                    .setParameter("TOKEN_STATE", tokenState)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DataException(DataExceptionCode.NTI_014, DataExceptionCode.GLB_005_MSG, e);
//        }
//        return networkTokenTOList;
//    }
//    
//    
//    @Override
//    public NetworkTokenTO findById(Long networkTokenId) throws BaseException {
//    	 try {
//             return em.createQuery(" select nt from NetworkTokenTO nt " +
//                     " where nt.id = :networkTokenId", NetworkTokenTO.class)
//                     .setParameter("networkTokenId", networkTokenId)
//                     .getSingleResult();
//         } catch (Exception e) {
//        	 throw new DataException(DataExceptionCode.NTI_008, DataExceptionCode.GLB_005_MSG, e);
//         }
//    }
//}
