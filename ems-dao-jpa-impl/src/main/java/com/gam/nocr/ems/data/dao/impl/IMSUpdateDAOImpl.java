package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.IMSUpdateTO;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * The interface IMSUpdateDAOImpl is a temporary class which is used for testing the IMS stub and will be omitted
 * on future
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "IMSUpdateDAO")
@Local(IMSUpdateDAOLocal.class)
@Remote(IMSUpdateDAORemote.class)
public class IMSUpdateDAOImpl extends EmsBaseDAOImpl<IMSUpdateTO> implements IMSUpdateDAOLocal, IMSUpdateDAORemote {

    private static final Logger logger = BaseLog.getLogger(IMSUpdateDAOImpl.class);

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}


	@Override
	public List<IMSUpdateTO> findAll() throws BaseException {
		List<IMSUpdateTO> imsUpdateTOList = null;
		try {
			imsUpdateTOList = em.createQuery("SELECT iut FROM IMSUpdateTO iut", IMSUpdateTO.class).getResultList();
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		return imsUpdateTOList;
	}

	@Override
	public List<IMSUpdateTO> findAllByState(IMSUpdateTO.State state) throws BaseException {
		List<IMSUpdateTO> imsUpdateTOList = null;
		try {
			imsUpdateTOList = em.createQuery("SELECT iut FROM IMSUpdateTO iut " +
					"WHERE iut.state = :STATE", IMSUpdateTO.class)
					.setParameter("STATE", state)
					.getResultList();
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		return imsUpdateTOList;
	}

	@Override
	public IMSUpdateTO findByNationalId(String nationalId) {
		List<IMSUpdateTO> imsUpdateTOList = null;
		try {
			imsUpdateTOList = em.createQuery("SELECT iut FROM IMSUpdateTO iut " +
					"WHERE iut.nationalId = :NATIONAL_ID", IMSUpdateTO.class)
					.setParameter("NATIONAL_ID", nationalId)
					.getResultList();
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		if (imsUpdateTOList != null && imsUpdateTOList.size() > 0) {
			return imsUpdateTOList.get(0);
		} else {
			return null;
		}

	}

	@Override
	public IMSUpdateTO findByRequestId(String requestId) throws BaseException {
		List<IMSUpdateTO> imsUpdateTOList = null;
		try {
			imsUpdateTOList = em.createQuery("" +
					"SELECT iut FROM IMSUpdateTO iut " +
					"WHERE iut.requestId = :REQUEST_ID", IMSUpdateTO.class)
					.setParameter("REQUEST_ID", requestId)
					.getResultList();
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		if (imsUpdateTOList != null && imsUpdateTOList.size() > 0) {
			return imsUpdateTOList.get(0);
		} else {
			return null;
		}
	}
}
