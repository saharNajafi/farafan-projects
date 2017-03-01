package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.CardRequestBlobsTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "CardRequestBlobsDAO")
@Local(CardRequestBlobsDAOLocal.class)
@Remote(CardRequestBlobsDAORemote.class)
public class CardRequestBlobsDAOImpl extends EmsBaseDAOImpl<CardRequestBlobsTO>
		implements CardRequestBlobsDAOLocal, CardRequestBlobsDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public CardRequestBlobsTO findByCardRequestId(Long cardRequestId)
			throws BaseException {

		try {
			List<CardRequestBlobsTO> cardRequestBlobList = em
					.createQuery(
							"select crqb from CardRequestBlobsTO crqb "
									+ "where crqb.cardRequest.id = :cardRequestId ",
							CardRequestBlobsTO.class)
					.setParameter("cardRequestId", cardRequestId)
					.getResultList();
			
			if (EmsUtil.checkListSize(cardRequestBlobList))
				return cardRequestBlobList.get(0);
			else
				return null;
			
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CRB_001,
					DataExceptionCode.GLB_005_MSG, e);
		}
	}

}
