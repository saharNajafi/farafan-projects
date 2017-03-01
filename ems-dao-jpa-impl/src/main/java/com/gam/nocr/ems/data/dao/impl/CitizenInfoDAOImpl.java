package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "CitizenInfoDAO")
@Local(CitizenInfoDAOLocal.class)
public class CitizenInfoDAOImpl extends EmsBaseDAOImpl<CitizenInfoTO> implements CitizenInfoDAOLocal, CitizenInfoDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The Delete method, handles the delete operation for all the classes which are extended from EntityTO.
     *
     * @param citizenInfoTO - the object of type EntityTO to create
     */
    @Override
    public void delete(CitizenInfoTO citizenInfoTO) throws BaseException {
        try {
            super.delete(citizenInfoTO);
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CZI_014, DataExceptionCode.GLB_007_MSG, e, new Long[]{citizenInfoTO.getCitizen().getId()});
        }
    }

    @Override
    public CitizenInfoTO create(CitizenInfoTO citizenInfoTO) throws BaseException {
        try {
            CitizenInfoTO to = super.create(citizenInfoTO);
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
            if (err.contains("FK_CZN_CITIZEN_ID")) {
                throw new DAOException(DataExceptionCode.CZI_002, DataExceptionCode.CZI_002_MSG, e, new Long[]{citizenInfoTO.getCitizen().getId()});
            }
            if (err.contains("FK_CZN_INF_BIRTH_PRV_ID")) {
                throw new DAOException(DataExceptionCode.CZI_003, DataExceptionCode.CZI_003_MSG, e, new Long[]{citizenInfoTO.getBirthCertificateIssuancePlaceProvince().getId()});
            }
            if (err.contains("FK_CZN_INF_LVN_CITY_ID")) {
                throw new DAOException(DataExceptionCode.CZI_004, DataExceptionCode.CZI_004_MSG, e, new Long[]{citizenInfoTO.getLivingCity().getId()});
            }
            if (err.contains("FK_CZN_INF_LVN_PRV_ID")) {
                throw new DAOException(DataExceptionCode.CZI_005, DataExceptionCode.CZI_005_MSG, e, new Long[]{citizenInfoTO.getLiving().getId()});
            }
            if (err.contains("FK_CZN_INF_RELGION_ID")) {
                throw new DAOException(DataExceptionCode.CZI_006, DataExceptionCode.CZI_006_MSG, e, new Long[]{citizenInfoTO.getReligion().getId()});
            }
            if (err.contains("FK_SPS_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CZI_008, DataExceptionCode.CZI_008_MSG, e);
            }
            if (err.contains("FK_SPS_MARITAL_STAT_ID")) {
                throw new DAOException(DataExceptionCode.CZI_009, DataExceptionCode.CZI_009_MSG, e);
            }
            if (err.contains("FK_CHILD_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CZI_010, DataExceptionCode.CZI_010_MSG, e);
            }
            if (err.contains("FK_BIM_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CZI_011, DataExceptionCode.CZI_011_MSG, e);
            }
            if (err.contains("FK_DOC_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CZI_012, DataExceptionCode.CZI_012_MSG, e);
            }
            if (err.contains("FK_DOC_DOC_TYPE_ID")) {
                throw new DAOException(DataExceptionCode.CZI_013, DataExceptionCode.CZI_013_MSG, e);
            }
            if (err.contains("AK_SPS_NATIONAL_ID")) {
                throw new DAOException(DataExceptionCode.CZI_015, DataExceptionCode.CZI_015_MSG, e);
            }
            if (err.contains("AK_CHILD_NATIONAL_ID")) {
                throw new DAOException(DataExceptionCode.CZI_016, DataExceptionCode.CZI_016_MSG, e);
            }
            throw new DAOException(DataExceptionCode.CZI_001, DataExceptionCode.CZI_001_MSG, e);
        }
    }

    /**
     * The method updateCitizenInfoByRequestIdOfHistory is used to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CitizenInfoTO} in spite of History requestId list
     *
     * @param citizenInfoHashMap is a {@link HashMap} of{@link HashMap<CitizenInfoTO, String>} which carries the necessary
     *                           attributes which are required for update
     */
    @Override
    public void updateCitizenInfoByRequestIdOfHistory(HashMap<CitizenInfoTO, String> citizenInfoHashMap) throws BaseException {
        for (Map.Entry<CitizenInfoTO, String> citizenInfoTOStringEntry : citizenInfoHashMap.entrySet()) {
            Map.Entry pairs = (Map.Entry) citizenInfoTOStringEntry;
            CitizenInfoTO citizenInfoTO = (CitizenInfoTO) pairs.getKey();
            String requestId = (String) pairs.getValue();
            try {
                em.createQuery("UPDATE CitizenInfoTO czi " +
                        "SET czi.afisState = :AFIS_STATE ," +
                        "czi.identityChanged = :IDENTITY_CHANGED " +
                        "WHERE czi.id IN (SELECT crh.cardRequest.citizen.id FROM CardRequestHistoryTO crh WHERE crh.requestID = :REQUEST_ID )")
                        .setParameter("AFIS_STATE", citizenInfoTO.getAfisState())
                        .setParameter("IDENTITY_CHANGED", citizenInfoTO.getIdentityChanged())
                        .setParameter("REQUEST_ID", requestId)
                        .executeUpdate();
                em.flush();
            } catch (Exception e) {
                throw new DataException(DataExceptionCode.CZI_007, DataExceptionCode.GLB_006_MSG, e);
            }

        }
    }

    /**
     * The method updateCitizenInfoByRequestHistory is used to update a list of type {@link CitizenInfoTO} in spite of
     * History requestId list
     *
     * @param citizenInfoHashMap is a {@link HashMap} of{@link HashMap<CitizenInfoTO, Long>} which carries the
     *                           necessary attributes which are required for update
     */
    @Override
    public void updateCitizenInfoByRequestHistory(HashMap<CitizenInfoTO, Long> citizenInfoHashMap) throws BaseException {
        for (Map.Entry<CitizenInfoTO, Long> citizenInfoTOStringEntry : citizenInfoHashMap.entrySet()) {
            Map.Entry pairs = (Map.Entry) citizenInfoTOStringEntry;
            CitizenInfoTO citizenInfoTO = (CitizenInfoTO) pairs.getKey();
            Long cardRequestHistoryId = (Long) pairs.getValue();
            try {
                em.createQuery("UPDATE CitizenInfoTO czi " +
                        "SET czi.afisState = :AFIS_STATE ," +
                        "czi.identityChanged = :IDENTITY_CHANGED " +
                        "WHERE czi.id IN (SELECT crh.cardRequest.citizen.id FROM CardRequestHistoryTO crh WHERE crh.id = :CARD_REQUEST_HISTORY_ID )")
                        .setParameter("AFIS_STATE", citizenInfoTO.getAfisState())
                        .setParameter("IDENTITY_CHANGED", citizenInfoTO.getIdentityChanged())
                        .setParameter("CARD_REQUEST_HISTORY_ID", cardRequestHistoryId)
                        .executeUpdate();
                em.flush();
            } catch (Exception e) {
                throw new DataException(DataExceptionCode.CZI_017, DataExceptionCode.GLB_006_MSG, e);
            }

        }
    }

	@Override
	public CitizenInfoTO getCitizenInfoById(Long id) throws BaseException{
		
		return em.createQuery("SELECT czi from CitizenInfoTO czi " +
                "WHERE czi.id = :CZI_ID " , CitizenInfoTO.class)
                .setParameter("CZI_ID", id).getSingleResult();
        

		
	}
	
	
	@Override
	public String getBirthCertIssuePlaceByNid(String nid)
			throws BaseException {

		try {
			List resultList = em
					.createNativeQuery(
							"select b.bcip_dep_name from emst_birth_cert_iss_place b "
									+ "where to_number("+nid.substring(0, 3)+") between to_number(b.bcip_code_from) and to_number(b.bcip_code_to)").getResultList();
			if (EmsUtil.checkListSize(resultList)) {

				Object record = resultList.get(0);
				return (String) record;

			} else
				return null;

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.CZI_018,
					DataExceptionCode.GLB_006_MSG, e);
		}
	}
}
