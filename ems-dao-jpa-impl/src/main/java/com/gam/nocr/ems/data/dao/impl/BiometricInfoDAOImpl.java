package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.BiometricInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haghshenas
 */
@Stateless(name = "BiometricInfoDAO")
@Local(BiometricInfoDAOLocal.class)
public class BiometricInfoDAOImpl extends EmsBaseDAOImpl<BiometricInfoTO>
		implements BiometricInfoDAOLocal, BiometricInfoDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Long checkBiometricInfo(Long requestId) throws BaseException {

		try {
			List<Long> count = em
					.createQuery(
							"select count(bim.citizenInfo.id) from BiometricTO bim, CardRequestTO crq "
									+ "where bim.citizenInfo.id = crq.citizen.id "
									+ "and crq.id = :requestId "
									+ "and (bim.type = 'FING_ALL' or bim.type = 'FING_MIN_1' or bim.type = 'FING_MIN_2')",
							Long.class).setParameter("requestId", requestId)
					.getResultList();

			if (EmsUtil.checkListSize(count)) {
				return count.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.BII_002, e);
		}
	}

	@Override
	public int removeBiometricInfo(Long citizenID) throws BaseException {
		try {
			int result = em
					.createQuery(
							"delete from BiometricInfoTO bii "
									+ "where bii.citizen.id = :citizenId ")
					.setParameter("citizenId", citizenID).executeUpdate();
			em.flush();
			return result;

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.BII_001,
					DataExceptionCode.BII_001_MSG, e);
		}
	}

//	@Override
//	public int removeBiometricInfoByCitizenId(String nid)
//			throws BaseException {
//		
//		try {
//			int result = em
//					.createQuery(
//							"delete from BiometricInfoTO bii "
//									+ "where bii.cardRequest.id = "
//									+"(select crt.id from CardRequestTO crt "
//									+"where crt.citizen.id = :citizenId)")
//					.setParameter("citizenId", citizenId).executeUpdate();
//			em.flush();
//			return result;
//
//		} catch (Exception e) {
//			throw new DAOException(DataExceptionCode.BII_002,
//					DataExceptionCode.BII_002_MSG, e,
//					new Object[] { citizenId });
//		}
//	}

	@Override
	public BiometricInfoTO findByNid(String nid) throws BaseException {
		try {
			List<BiometricInfoTO> result = em
					.createQuery(
							"select bii from BiometricInfoTO bii "
									+ "where bii.nationalID = :nid",BiometricInfoTO.class)
					.setParameter("nid", nid).getResultList();

			if(EmsUtil.checkListSize(result))
			{
				return result.get(0);
				
			}
			return null;
			

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.BII_003,
					DataExceptionCode.BII_003_MSG, e);
		}
	}

}
