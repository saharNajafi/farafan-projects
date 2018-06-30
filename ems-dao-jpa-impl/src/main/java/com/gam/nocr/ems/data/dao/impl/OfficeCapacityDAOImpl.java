package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeCapacityDAO")
@Local(OfficeCapacityDAOLocal.class)
@Remote(OfficeCapacityRemote.class)
public class OfficeCapacityDAOImpl extends EmsBaseDAOImpl<OfficeCapacityTO> implements OfficeCapacityDAOLocal, OfficeCapacityRemote{

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<OfficeCapacityTO> findByEnrollmentOfficeIdAndShiftNo(Long enrollmentOfficeId, ShiftEnum shiftNo) throws BaseException {
        List<OfficeCapacityTO> officeCapacityList;
        try {
            officeCapacityList = em.createNamedQuery("officeCapacityTO.findByEnrollmentOfficeIdAndShiftNo")
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("shiftNo", shiftNo)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OCD_002, DataExceptionCode.OCD_002_MSG, e);
        }
        return officeCapacityList.size() != 0 ? officeCapacityList : null;
    }

    @Override
    public OfficeCapacityTO create(OfficeCapacityTO officeCapacityTO) throws BaseException {
        try {
            OfficeCapacityTO officeCapacity = super.create(officeCapacityTO);
            em.flush();
            return officeCapacity;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_OFFICE_CAPACITY_CLASS"))
                throw new DAOException(DataExceptionCode.OCD_001, DataExceptionCode.OCD_001_MSG, e);
            if (err.contains("AK_OFFICE_CAPACITY_SIZE"))
                throw new DAOException(DataExceptionCode.OCD_003, DataExceptionCode.OCD_003_MSG, e);
            throw new DAOException(DataExceptionCode.OCD_004, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public OfficeCapacityTO update(OfficeCapacityTO officeCapacityTO) throws BaseException {
        try {
            OfficeCapacityTO officeCapacity = super.update(officeCapacityTO);
            em.flush();
            return officeCapacity;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_OFFICE_CAPACITY_CLASS"))
                throw new DAOException(DataExceptionCode.OCD_006, DataExceptionCode.OCD_001_MSG, e);
            if (err.contains("AK_OFFICE_CAPACITY_SIZE"))
                throw new DAOException(DataExceptionCode.OCD_007, DataExceptionCode.OCD_003_MSG, e);
            throw new DAOException(DataExceptionCode.OCD_005, DataExceptionCode.GLB_004_MSG, e);
        }
    }

}
