package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.MessageTO;

@Stateless(name = "MessageDestinationDAO")
public class MessageDestinationDAOImpl extends
		EmsBaseDAOImpl<MessageDestinationTO> implements
		MessageDestinationDAOLocal, MessageDestinationDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public MessageDestinationTO create(MessageTO message,
			MessageDestinationTO mesdes) throws BaseException {
		try {
			mesdes.setMessage(message);
			MessageDestinationTO to = super.create(mesdes);
			em.flush();
			return to;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.MDD_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	@Override
	public List<MessageDestinationTO> findByMessageId(Long messageId)
			throws BaseException {

		try {

			List<MessageDestinationTO> messagesDestinations = em
					.createQuery(
							"FROM MessageDestinationTO md"
									+ " where md.message.id =:messageId ",
							MessageDestinationTO.class)
					.setParameter("messageId", messageId).getResultList();
			return messagesDestinations;
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.MDD_002,
					DataExceptionCode.MDD_002_MSG, e, new Long[] { messageId });
		}
	}

}
