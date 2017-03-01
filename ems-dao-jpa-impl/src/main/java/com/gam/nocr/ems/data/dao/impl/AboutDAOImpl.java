package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.AboutTO;

@Stateless(name = "AboutDAO")
@Local(AboutDAOLocal.class)
@Remote(AboutDAORemote.class)
public class AboutDAOImpl extends EmsBaseDAOImpl<AboutTO> implements
		AboutDAOLocal, AboutDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public AboutTO create(AboutTO aboutTO) throws BaseException {
		try {
			AboutTO about = super.create(aboutTO);
			em.flush();
			return about;
		} catch (Exception e) {
		
			throw new DAOException(DataExceptionCode.ADI_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	@Override
	public AboutTO update(AboutTO aboutTO) throws BaseException {
		try {
			AboutTO about = super.update(aboutTO);
			em.flush();
			return about;
		} catch (Exception e) {

			throw new DAOException(DataExceptionCode.ADI_002,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	@Override
	public AboutTO getAbout() throws BaseException {
		try {
			List<AboutTO> resultList = em.createQuery(
					"select abt " + "from AboutTO abt ", AboutTO.class)
					.getResultList();

			if (!resultList.isEmpty())
				return resultList.get(0);
			return null;

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.ADI_002,
					DataExceptionCode.ADI_002_MSG, e);
		}
	}

}
