package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.enums.BiometricType;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "BiometricDAO")
@Local(BiometricDAOLocal.class)
public class BiometricDAOImpl extends EmsBaseDAOImpl<BiometricTO> implements BiometricDAOLocal, BiometricDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public BiometricTO create(BiometricTO biometricTO) throws BaseException {
        try {
            BiometricTO to = super.create(biometricTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_BIM_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.BDI_002, DataExceptionCode.BDI_002_MSG, e, new Long[]{biometricTO.getCitizenInfo().getId()});
            }
            if (err.contains("AK_BIM_TYPE_CTZ_INFO_ID")) {
                throw new DAOException(DataExceptionCode.BDI_006, DataExceptionCode.BDI_006_MSG + biometricTO.getCitizenInfo().getId(), e);
            }
            throw new DAOException(DataExceptionCode.BDI_001, DataExceptionCode.BDI_001_MSG, e);
        }
    }

    @Override
    public List<BiometricTO> findByRequestIdAndType(long requestId, BiometricType bioType) throws BaseException {
        try {
            return em.createQuery("select bio " +
                    "from BiometricTO bio " +
                    "where bio.type = :bioType " +
                    "and bio.citizenInfo.id in (" +
                    "select ctz.id " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id)", BiometricTO.class)
                    .setParameter("requestId", requestId)
                    .setParameter("bioType", bioType)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BDI_004, DataExceptionCode.BDI_004_MSG, e);
        }
    }

    @Override
    public int removeByRequestIdAndType(long requestId, BiometricType biometricType) throws BaseException {
        try {
            int result = em.createQuery("delete from BiometricTO bio " +
                    "where bio.type = :bioType " +
                    "and bio.citizenInfo.id in (" +
                    "select ctz.id " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id)")
                    .setParameter("requestId", requestId)
                    .setParameter("bioType", biometricType)
                    .executeUpdate();
            em.flush();
            return result;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BDI_003, DataExceptionCode.BDI_003_MSG, e, new Object[]{requestId, biometricType.name()});
        }
    }
    
    //Adldoost
    @Override
    public int removeFaceInfoByRequestId(long requestId) throws BaseException {
        try {
        	
        	List<BiometricType> types = new ArrayList<BiometricType>();
            types.add(BiometricType.FACE_CHIP);
            types.add(BiometricType.FACE_IMS);
            types.add(BiometricType.FACE_LASER);
            types.add(BiometricType.FACE_MLI);

            try {
                int result = em.createQuery("delete from BiometricTO bio " +
                    "where bio.type in (:bioType) " +
                    "and bio.citizenInfo.id in (" +
                    "select ctz.id " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id)")
                        .setParameter("requestId", requestId)
                        .setParameter("bioType", types)
                        .executeUpdate();
                em.flush();
                return result;
            } catch (Exception e) {
                throw new DAOException(DataExceptionCode.BDI_007, DataExceptionCode.BDI_007_MSG, e, new Object[]{types, requestId});
            }
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BDI_003, DataExceptionCode.BDI_003_MSG, e, new Object[]{requestId});
        }
    }

    /**
     * The method removeFingersInfoByCitizenId is used to remove all the biometric data of type finger which belongs to a
     * specified citizen
     *
     * @param citizenId an instance of type {@link Long} which represents the id of a specified citizen
     * @return 1 if success, otherwise 0
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public int removeFingersInfoByCitizenId(Long citizenId) throws BaseException {
//		TODO : Optimize this query on future
        List<BiometricType> fingerTypes = new ArrayList<BiometricType>();
        fingerTypes.add(BiometricType.FING_CANDIDATE);
        fingerTypes.add(BiometricType.FING_MIN_1);
        fingerTypes.add(BiometricType.FING_MIN_2);
        fingerTypes.add(BiometricType.FING_ALL);

        try {
            int result = em.createQuery("DELETE FROM BiometricTO bio " +
                    "WHERE bio.citizenInfo.id = :CITIZEN_ID " +
                    "AND bio.type IN (:FINGER_TYPES)")
                    .setParameter("CITIZEN_ID", citizenId)
                    .setParameter("FINGER_TYPES", fingerTypes)
                    .executeUpdate();
            em.flush();
            return result;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BDI_005, DataExceptionCode.BDI_005_MSG, e, new Object[]{citizenId});
        }
    }

    @Override
    public int removeFaceInfoByCitizenId(Long citizenId) throws BaseException {
        List<BiometricType> fingerTypes = new ArrayList<BiometricType>();
        fingerTypes.add(BiometricType.FACE_CHIP);
        fingerTypes.add(BiometricType.FACE_IMS);
        fingerTypes.add(BiometricType.FACE_LASER);
        fingerTypes.add(BiometricType.FACE_MLI);

        try {
            int result = em.createQuery("DELETE FROM BiometricTO bio " +
                    "WHERE bio.citizenInfo.id = :CITIZEN_ID " +
                    "AND bio.type IN (:FINGER_TYPES)")
                    .setParameter("CITIZEN_ID", citizenId)
                    .setParameter("FINGER_TYPES", fingerTypes)
                    .executeUpdate();
            em.flush();
            return result;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BDI_007, DataExceptionCode.BDI_007_MSG, e, new Object[]{citizenId});
        }
    }
    

	@Override
	public void emptyBiometricData(Long citizenId) throws BaseException {
		try {
			em.createNativeQuery(
					"update emst_biometric bio set bio.bim_data = empty_blob() where bio.bim_citizen_info_id = :citizenId")
					.setParameter("citizenId", citizenId).executeUpdate();
			em.flush();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.BDI_008,
					DataExceptionCode.GLB_008_MSG, e);
		}
	}

	//Madanipour
	@Override
	public int removeAllBioDataByRequestID(Long id) throws BaseException {
		
		try {
			int result = em
					.createQuery(
							"delete from BiometricTO bio "
									+ "where bio.citizenInfo.id in ("
									+ "select ctz.id "
									+ "from CitizenTO ctz, CardRequestTO crq "
									+ "where crq.id = :requestId "
									+ "and crq.citizen.id = ctz.id)")
					.setParameter("requestId", id).executeUpdate();
			em.flush();
			return result;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.BDI_009,
					DataExceptionCode.GLB_008_MSG, e);
		}
		
		
		
	}
}
