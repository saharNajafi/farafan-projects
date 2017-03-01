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

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "ChildDAO")
@Local(ChildDAOLocal.class)
@Remote(ChildDAORemote.class)
public class ChildDAOImpl extends EmsBaseDAOImpl<ChildTO> implements
		ChildDAOLocal, ChildDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public void delete(ChildTO childTO) throws BaseException {
		try {
			em.remove(childTO);
			em.flush();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.RTI_006,
					DataExceptionCode.RTI_006_MSG, e);
		}
	}

}
