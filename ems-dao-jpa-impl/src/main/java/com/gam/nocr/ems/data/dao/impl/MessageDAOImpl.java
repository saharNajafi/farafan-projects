package com.gam.nocr.ems.data.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.MessageTO;
import com.gam.nocr.ems.data.enums.DestMessageType;
import com.gam.nocr.ems.data.enums.PreparedMessageState;
import com.gam.nocr.ems.data.enums.ReadUnreadType;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "MessageDAO")
@Local(MessageDAOLocal.class)
@Remote(MessageDAORemote.class)
public class MessageDAOImpl extends EmsBaseDAOImpl<MessageTO> implements
		MessageDAOLocal, MessageDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public MessageTO create(MessageTO message) throws BaseException {
		try {
			MessageTO to = super.create(message);
			em.flush();
			return to;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.MDI_001,
					DataExceptionCode.MDI_001_MSG, e);
		}
	}

	@Override
	public Long getCountNewMessages(long personID) throws BaseException {

		try {
			String query = "SELECT count(*) "
					+ "FROM MessagePersonTO mp where mp.personId =:personId AND mp.readUnreadType =:readUnreadType";
			Number count = (Number) em.createQuery(query)
					.setParameter("personId", personID)
					.setParameter("readUnreadType", ReadUnreadType.U)
					.getSingleResult();
			return count == null ? 0L : count.longValue();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.MDI_002,
					DataExceptionCode.MDI_002_MSG, e);
		}

	}

	@Override
	public List<Long> fetchReadyToProcessMessage() throws BaseException {
		try {
			List<Long> idList = em
					.createQuery(
							"SELECT PMG.id FROM PreparedMessageTO PMG "
									+ "WHERE "
									+ "PMG.preparedState =:PREPARED_STATE ORDER BY  "
									+ "PMG.id ASC", Long.class)
					.setParameter("PREPARED_STATE", PreparedMessageState.NEW)
					.getResultList();
			if (EmsUtil.checkListSize(idList)) {
				return idList;
			}
			return null;
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.MDI_001,
					DataExceptionCode.GLB_005_MSG, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageDestinationTO> getMessageForAll(Long personId,
			Timestamp msgDate, int pageSize, int pageNo) {

		StringBuilder builder = new StringBuilder();

		builder.append("select m from MessageDestinationTO m join m.message mess where m.destinationType = :type and m.deleteList not like :personId");

		if (msgDate != null) {
			builder.append(" and mess.createDate = :createDate");
		}

		builder.append(" order by mess.createDate desc");

		Query query = em.createQuery(builder.toString());
		query.setParameter("type", DestMessageType.ALL);
		query.setParameter("personId", "%@" + personId + "@%");

		query.setFirstResult(pageNo * pageSize);
		query.setMaxResults(pageSize);

		if (msgDate != null) {
			query.setParameter("createDate", msgDate);
		}

		List<MessageDestinationTO> resultList = query.getResultList();

		return resultList == null ? new ArrayList<MessageDestinationTO>()
				: resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageDestinationTO> getMessageByProvinceId(Long personId,
			Long provinceId, Timestamp msgDate, int pageSize, int pageNo) {

		StringBuilder builder = new StringBuilder();

		builder.append("select m from MessageDestinationTO m join m.message mess where m.destinationType = :type and m.destinationId = :destinationId and m.deleteList not like :personId");

		if (msgDate != null) {
			builder.append(" and mess.createDate = :createDate");
		}

		builder.append(" order by mess.createDate desc");

		Query query = em.createQuery(builder.toString());

		query.setParameter("type", DestMessageType.PROVINCE);
		query.setParameter("personId", "%@" + personId + "@%");
		query.setParameter("destinationId", provinceId);

		query.setFirstResult(pageNo * pageSize);
		query.setMaxResults(pageSize);

		if (msgDate != null) {
			query.setParameter("createDate", msgDate);
		}

		List<MessageDestinationTO> resultList = query.getResultList();

		return resultList == null ? new ArrayList<MessageDestinationTO>()
				: resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageDestinationTO> getMessageByOfficeId(Long personId,
			Long officeId, Timestamp msgDate, int pageSize, int pageNo) {
		StringBuilder builder = new StringBuilder();

		builder.append("select m from MessageDestinationTO m join m.message mess where m.destinationType = :type and m.destinationId = :destinationId and m.deleteList not like :personId");

		if (msgDate != null) {
			builder.append(" and mess.createDate = :createDate");
		}

		builder.append(" order by mess.createDate desc");

		Query query = em.createQuery(builder.toString());

		query.setParameter("type", DestMessageType.OFFICE);
		query.setParameter("personId", "%@" + personId + "@%");
		query.setParameter("destinationId", officeId);

		query.setFirstResult(pageNo * pageSize);
		query.setMaxResults(pageSize);

		if (msgDate != null) {
			query.setParameter("createDate", msgDate);
		}

		List<MessageDestinationTO> resultList = query.getResultList();

		return resultList == null ? new ArrayList<MessageDestinationTO>()
				: resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageDestinationTO> getMessageByPersonId(Long personId,
			Timestamp msgDate, int pageSize, int pageNo) {
		StringBuilder builder = new StringBuilder();

		builder.append("select m from MessageDestinationTO m join m.message mess where m.destinationType = :type and m.destinationId = :destinationId and m.deleteList not like :personId");

		if (msgDate != null) {
			builder.append(" and mess.createDate = :createDate");
		}

		builder.append(" order by mess.createDate desc");

		Query query = em.createQuery(builder.toString());

		query.setParameter("type", DestMessageType.PRIVATE);
		query.setParameter("personId", "%@" + personId + "@%");
		query.setParameter("destinationId", personId);

		query.setFirstResult(pageNo * pageSize);
		query.setMaxResults(pageSize);

		if (msgDate != null) {
			query.setParameter("createDate", msgDate);
		}

		List<MessageDestinationTO> resultList = query.getResultList();

		return resultList == null ? new ArrayList<MessageDestinationTO>()
				: resultList;
	}
}
