package com.gam.nocr.ems.biz.service.internal.impl;

/**
 * hossein
 */

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.PreparedMessageDAO;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;

@Stateless(name = "PreparedMessageService")
@Local(PreparedMessageServiceLocal.class)
@Remote(PreparedMessageServiceRemote.class)
public class PreparedMessageServiceImpl extends EMSAbstractService implements
		PreparedMessageServiceLocal, PreparedMessageServiceRemote {

	private PreparedMessageDAO getPreparedMessageDAO() throws BaseException {
		try {
			return DAOFactoryProvider
					.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_PREPARED_MESSAGE));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.RMS_006,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_PREPARED_MESSAGE.split(","));
		}
	}

	@Override
	public void save(PreparedMessageTO to) throws BaseException {
		if (to.getId() == null)
			getPreparedMessageDAO().create(to);
		else
			getPreparedMessageDAO().update(to);
	}

	@Override
	public void delete(String id) throws BaseException {
		Long toId = Long.valueOf(id);
		PreparedMessageTO messageTO = getPreparedMessageDAO().find(
				PreparedMessageTO.class, toId);
		if (messageTO != null)
			getPreparedMessageDAO().delete(messageTO);
	}

	@Override
	public PreparedMessageTO loadById(String id) throws BaseException {
		Long toId = Long.valueOf(id);
		return getPreparedMessageDAO().loadById(toId);
	}

	@Override
	public PreparedMessageTO downloadById(String id) throws BaseException {

		Long toId = Long.valueOf(id);
		PreparedMessageTO messageTO = getPreparedMessageDAO().find(
				PreparedMessageTO.class, toId);

		// load lazy value
		if (messageTO != null) {
			messageTO.getAttachFile();
		}

		return messageTO;

	}
}
