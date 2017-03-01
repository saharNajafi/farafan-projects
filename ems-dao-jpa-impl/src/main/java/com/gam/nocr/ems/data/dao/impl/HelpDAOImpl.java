package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.HelpTO;

@Stateless(name = "HelpDAO")
@Local(HelpDAOLocal.class)
@Remote(HelpDAORemote.class)
public class HelpDAOImpl extends EmsBaseDAOImpl<HelpTO> implements
		HelpDAOLocal, HelpDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	

	@Override
	public HelpTO create(HelpTO helpTO) throws BaseException {
		try {
			HelpTO about = super.create(helpTO);
			em.flush();
			return about;
		} catch (Exception e) {
			String err = e.getMessage();
			if (e.getCause() != null) {
				if (e.getCause().getCause() != null)
					err = e.getCause().getCause().getMessage();
				else
					err = e.getCause().getMessage();
			}
			if (err.contains("AK_RATING_CLASS"))
				throw new DAOException(DataExceptionCode.RTI_009,
						DataExceptionCode.RTI_009_MSG, e);
			if (err.contains("AK_RATING_SIZE"))
				throw new DAOException(DataExceptionCode.RTI_010,
						DataExceptionCode.RTI_010_MSG, e);
			throw new DAOException(DataExceptionCode.RTI_008,
					DataExceptionCode.GLB_004_MSG, e);
		}
	}

	
	@Override
	public boolean deleteHelps(List<Long> helpIds) throws BaseException {

	

        try {
            Query q = null;
            if (helpIds == null) {
                q = em.createQuery("delete from HelpTO hlp");
            } else {
                q = em.createQuery("delete from HelpTO hlp " +
                        "where hlp.id in (:ids)")
                        .setParameter("ids", helpIds);
            }
            int count = q.executeUpdate();
            em.flush();
            if (helpIds == null)
                return true;
            return count == helpIds.size();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_ENROLL_OFC_RAT_ID"))
                throw new DAOException(DataExceptionCode.RTI_005, DataExceptionCode.RTI_005_MSG, e);
            throw new DAOException(DataExceptionCode.RTI_002, DataExceptionCode.RTI_002_MSG, e);
        }
    
	
	}

	
}
