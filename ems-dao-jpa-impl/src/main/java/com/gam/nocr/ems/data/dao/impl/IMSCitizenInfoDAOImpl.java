package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.IMSCitizenInfoTO;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "IMSCitizenInfoDAO")
@Local(IMSCitizenInfoDAOLocal.class)
@Remote(IMSCitizenInfoDAORemote.class)
public class IMSCitizenInfoDAOImpl extends EmsBaseDAOImpl<IMSCitizenInfoTO> implements IMSCitizenInfoDAOLocal, IMSCitizenInfoDAORemote {

    private static final Logger logger = BaseLog.getLogger(IMSCitizenInfoDAOImpl.class);

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * The method findByNationalId is used to find an instance of type {@link com.gam.nocr.ems.data.domain.IMSCitizenInfoTO}
	 * with a specified nationalId
	 *
	 * @param nationalId is an instance of type {@link Long}, which represents a specified nationalId
	 * @return an instance of type {@link com.gam.nocr.ems.data.domain.IMSCitizenInfoTO}
	 * @throws com.gam.commons.core.BaseException
	 *
	 */
	@Override
	public IMSCitizenInfoTO findByNationalId(Long nationalId) throws BaseException {
		List<IMSCitizenInfoTO> imsCitizenInfoTOList = null;
		try {
			imsCitizenInfoTOList = em.createQuery("" +
					"SELECT ICI FROM IMSCitizenInfoTO ICI " +
					"WHERE ICI.nationalId = :NATIONAL_ID", IMSCitizenInfoTO.class)
					.setParameter("NATIONAL_ID", nationalId)
					.getResultList();
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		if (EmsUtil.checkListSize(imsCitizenInfoTOList)) {
			return imsCitizenInfoTOList.get(0);
		} else {
			return null;
		}
	}
}
