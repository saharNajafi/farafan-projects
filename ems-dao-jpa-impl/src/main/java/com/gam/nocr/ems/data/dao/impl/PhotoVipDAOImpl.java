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
import com.gam.nocr.ems.data.domain.PhotoVipTO;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "PhotoVipDAO")
@Local(PhotoVipDAOLocal.class)
@Remote(PhotoVipDAORemote.class)
public class PhotoVipDAOImpl extends EmsBaseDAOImpl<PhotoVipTO> implements
		PhotoVipDAOLocal, PhotoVipDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public PhotoVipTO create(PhotoVipTO ratingInfoTO) throws BaseException {
		try {
			PhotoVipTO photoVip = super.create(ratingInfoTO);
			em.flush();
			return photoVip;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PHI_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	@Override
	public PhotoVipTO getPhotoVip(Long cardRquestId) throws BaseException {

		try {
			List<PhotoVipTO> resultList = em
					.createQuery(
							"SELECT pv " + "FROM PhotoVipTO pv "
									+ "WHERE pv.cardRequest.id = :cardRquestId",
							PhotoVipTO.class)
					.setParameter("cardRquestId", cardRquestId).getResultList();
			if (EmsUtil.checkListSize(resultList))
				return resultList.get(0);
			else
				return null;

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PHI_001,
					DataExceptionCode.GLB_004_MSG, e);
		}

	}

}
