package com.gam.nocr.ems.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ExceptionCodeTO;

@Stateless(name = "ExceptionCodeDAO")
@Local(ExceptionCodeDAOLocal.class)
@Remote(ExceptionCodeDAORemote.class)
public class ExceptionCodeDAOImpl extends EmsBaseDAOImpl<ExceptionCodeTO>
		implements ExceptionCodeDAOLocal, ExceptionCodeDAORemote {

	
	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<String> getAll() throws BaseException {

		try {
			List<String> resultList = em.createQuery(
					"select to.code from ExceptionCodeTO to ",String.class).getResultList();
			return resultList == null ? new ArrayList<String>()
					: resultList;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.IDI_002,
					DataExceptionCode.IDI_002_MSG, e);
		}

	}

}
