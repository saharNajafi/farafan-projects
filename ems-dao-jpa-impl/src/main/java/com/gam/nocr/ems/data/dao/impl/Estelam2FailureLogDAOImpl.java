package com.gam.nocr.ems.data.dao.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.Estelam2FailureLogTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "Estelam2FailureLogDAO")
@Local(Estelam2FailureLogDAOLocal.class)
@Remote(Estelam2FailureLogDAORemote.class)
public class Estelam2FailureLogDAOImpl extends EmsBaseDAOImpl<Estelam2FailureLogTO> implements
		Estelam2FailureLogDAOLocal, Estelam2FailureLogDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Estelam2FailureLogTO create(Estelam2FailureLogTO estelamLogTO)
			throws BaseException {
		try {
			Estelam2FailureLogTO estelamLog = super.create(estelamLogTO);
			em.flush();
			return estelamLog;
		} catch (Exception e) {
			String err = e.getMessage();
			if (e.getCause() != null) {
				if (e.getCause().getCause() != null)
					err = e.getCause().getCause().getMessage();
				else
					err = e.getCause().getMessage();
			}
			
			throw new DAOException(DataExceptionCode.EFI_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}
	//
	// @Override
	// public RatingInfoTO update(RatingInfoTO ratingInfoTO) throws
	// BaseException {
	// try {
	// RatingInfoTO ratingInfo = super.update(ratingInfoTO);
	// em.flush();
	// return ratingInfo;
	// } catch (Exception e) {
	// String err = e.getMessage();
	// if (e.getCause() != null) {
	// if (e.getCause().getCause() != null)
	// err = e.getCause().getCause().getMessage();
	// else
	// err = e.getCause().getMessage();
	// }
	// if (err.contains("AK_RATING_CLASS"))
	// throw new DAOException(DataExceptionCode.RTI_012,
	// DataExceptionCode.RTI_012_MSG, e);
	// if (err.contains("AK_RATING_SIZE"))
	// throw new DAOException(DataExceptionCode.RTI_013,
	// DataExceptionCode.RTI_013_MSG, e);
	// throw new DAOException(DataExceptionCode.RTI_011,
	// DataExceptionCode.GLB_004_MSG, e);
	// }
	// }

	// @Override
	// public void delete(RatingInfoTO ratingInfoTO) throws BaseException {
	// try {
	// em.remove(ratingInfoTO);
	// em.flush();
	// } catch (Exception e) {
	// String err = e.getMessage();
	// if (e.getCause() != null) {
	// if (e.getCause().getCause() != null)
	// err = e.getCause().getCause().getMessage();
	// else
	// err = e.getCause().getMessage();
	// }
	// if (err.contains("FK_ENROLL_OFC_RAT_ID"))
	// throw new DAOException(DataExceptionCode.RTI_004,
	// DataExceptionCode.RTI_004_MSG, e,
	// new Long[] { ratingInfoTO.getId() });
	// throw new DAOException(DataExceptionCode.RTI_006,
	// DataExceptionCode.RTI_006_MSG, e);
	// }
	// }

}
