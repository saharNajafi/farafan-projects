package com.gam.nocr.ems.data.dao.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ChildTO;
import com.gam.nocr.ems.data.domain.SpouseTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "SpouseDAO")
@Local(SpouseDAOLocal.class)
@Remote(SpouseDAORemote.class)
public class SpouseDAOImpl extends EmsBaseDAOImpl<SpouseTO> implements
		SpouseDAOLocal, SpouseDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public void delete(SpouseTO spouseTO) throws BaseException {
		try {
			em.remove(spouseTO);
			em.flush();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.RTI_006,
					DataExceptionCode.RTI_006_MSG, e);
		}
	}

}
