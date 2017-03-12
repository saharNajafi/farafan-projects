package com.gam.nocr.ems.data.dao.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.PurgeHistoryTO;

@Stateless(name = "PurgeHistoryDAO")
@Local(PurgeHistoryDAOLocal.class)
@Remote(PurgeHistoryDAORemote.class)
public class PurgeHistoryDAOImpl extends EmsBaseDAOImpl<PurgeHistoryTO>
		implements PurgeHistoryDAOLocal, PurgeHistoryDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public PurgeHistoryTO create(PurgeHistoryTO purgeHistoryTO)
			throws BaseException {
		try {
			PurgeHistoryTO purgeHistory = super.create(purgeHistoryTO);
			em.flush();
			return purgeHistory;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.RTI_008,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}
}
