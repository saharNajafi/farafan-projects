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
import com.gam.nocr.ems.data.domain.NistHeaderTO;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "NistHeaderDAO")
@Local(NistHeaderDAOLocal.class)
@Remote(NistHeaderDAORemote.class)
public class NistHeaderDAOImpl extends EmsBaseDAOImpl<NistHeaderTO> implements
		NistHeaderDAOLocal, NistHeaderDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public NistHeaderTO create(NistHeaderTO nistHeader) throws BaseException {
		try {
			NistHeaderTO nist = super.create(nistHeader);
			em.flush();
			return nist;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.NHD_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	@Override
	public NistHeaderTO findByRequestId(Long requestId) throws BaseException {
		try {
			List<NistHeaderTO> resultList = em
					.createQuery(
							"select nh from NistHeaderTO nh "
									+ "where nh.cardRequest.id = :requestId order by nh.createDate desc",
							NistHeaderTO.class)
					.setParameter("requestId", requestId).getResultList();

			if (EmsUtil.checkListSize(resultList)) {
				return resultList.get(0);

			} else
				return null;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.NHD_001,
					DataExceptionCode.NHD_001_MSG);
		}

	}
}
