package com.gam.nocr.ems.data.dao.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.NistHeaderTO;
import com.gam.nocr.ems.data.domain.XmlAfisTO;

@Stateless(name = "XmlAfisDAO")
@Local(XmlAfisDAOLocal.class)
@Remote(XmlAfisDAORemote.class)
public class XmlAfisDAOImpl extends EmsBaseDAOImpl<XmlAfisTO> implements
		XmlAfisDAOLocal, XmlAfisDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public XmlAfisTO create(XmlAfisTO to) throws BaseException {
		try {
			XmlAfisTO result = super.create(to);
			em.flush();
			return result;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.NHD_001,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}
}
