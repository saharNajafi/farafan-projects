package com.gam.nocr.ems.data.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.PurgeStatusTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.data.enums.SMSTypeState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

@Stateless(name = "PurgeStatusDAO")
@Local(PurgeStatusDAOLocal.class)
@Remote(PurgeStatusDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PurgeStatusDAOImpl extends EmsBaseDAOImpl<PurgeStatusTO> implements PurgeStatusDAOLocal, PurgeStatusDAORemote {
	
	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public PurgeStatusTO findLastPurgeStatus() throws DAOException {
		
		List<PurgeStatusTO> pssList = em.createQuery("SELECT PSS FROM PurgeStatusTO PSS ORDER BY PSS.id DESC", PurgeStatusTO.class).setMaxResults(1).getResultList();		
		if(pssList != null && !pssList.isEmpty())
		{
			return pssList.get(0);
		}
		return null;
	}

}
