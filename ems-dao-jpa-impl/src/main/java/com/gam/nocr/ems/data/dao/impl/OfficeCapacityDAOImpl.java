package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;

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
    public List<OfficeCapacityTO> findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException {
        List<OfficeCapacityTO> officeCapacityList;
        try {
            officeCapacityList = em.createNamedQuery("EnrollmentOfficeTO.findEnrollmentOfficeById")
                    .setParameter("eofId", enrollmentOfficeId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OCD_001,
                    DataExceptionCode.OCD_001_MSG, e);
        }
        return officeCapacityList != null ? officeCapacityList : null;
    }


}
