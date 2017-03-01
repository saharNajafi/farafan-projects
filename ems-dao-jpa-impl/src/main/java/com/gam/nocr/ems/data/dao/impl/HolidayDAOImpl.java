package com.gam.nocr.ems.data.dao.impl;

import java.util.ArrayList;
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
import com.gam.nocr.ems.data.domain.HolidayTO;

/**
 * Created by IntelliJ IDEA. User: jalilian Date: 8/21/13 Time: 10:12 AM
 */
@Stateless(name = "HolidayDAO")
@Local(HolidayDAOLocal.class)
@Remote(HolidayDAORemote.class)
public class HolidayDAOImpl extends EmsBaseDAOImpl<HolidayTO> implements
		HolidayDAOLocal, HolidayDAORemote {
	
	private static final String UNIQUE_KEY_HOLIDAY_DATE = "PK_HOL_HOLIDAY_DATE";

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HolidayTO> getAllChangedHoliday() {

		Query createQuery = em
				.createQuery("select to from HolidayTO to where to.flag != :flag");

		createQuery.setParameter("flag", 0);

		List resultList = createQuery.getResultList();

		return resultList == null ? new ArrayList<HolidayTO>() : resultList;
	}

	@Override
	public void notifyUpdateHolidays(List<HolidayTO> tos) {
		
		List<Long> ids = new ArrayList<Long>();
		
		for (HolidayTO holidayTO : tos) {
			ids.add(holidayTO.getId());
		}
		
		Query createQuery = em.createQuery("update HolidayTO to set to.flag = :flag where to.id in(:ids)");
		
		createQuery.setParameter("flag", 0);
		
		createQuery.setParameter("ids", ids);
		
		createQuery.executeUpdate();
		
	}

	@Override
	public void notifyRemoveHolidays(List<HolidayTO> tos) {
		List<Long> ids = new ArrayList<Long>();
		
		for (HolidayTO holidayTO : tos) {
			ids.add(holidayTO.getId());
		}
		
		Query createQuery = em.createQuery("delete HolidayTO to where to.id in(:ids)");
		
		createQuery.setParameter("ids", ids);
		
		createQuery.executeUpdate();

	}

	
	@Override
	public HolidayTO create(HolidayTO t) throws BaseException {
		try {
			return super.create(t);
		} catch (Exception e) {
			String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_HOLIDAY_DATE))
                throw new DAOException(DataExceptionCode.HDI_001, DataExceptionCode.HDI_001_MSG, e);
            e.printStackTrace();
		}
		return null;
	}
}
