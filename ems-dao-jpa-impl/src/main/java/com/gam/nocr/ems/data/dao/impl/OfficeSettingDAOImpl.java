package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "OfficeSettingDAO")
@Local(OfficeSettingDAOLocal.class)
@Remote(OfficeSettingDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OfficeSettingDAOImpl extends EmsBaseDAOImpl<OfficeSettingTO> implements OfficeSettingDAORemote,OfficeSettingDAOLocal{
	
	
	@Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

	/**
	 * @author asus-pc :Madanipour
	 * find if a setting set for an office or not
	 * 
	 *
	 **/
	@Override
	public OfficeSettingTO findByOfficeId(long depID) throws BaseException {

		try {
            List<OfficeSettingTO> officeSettingTOs = em.createQuery("SELECT OST FROM OfficeSettingTO OST " +
                    "WHERE OST.enrollmentOffice.id = :enrollmentOfficeId " 
            		
//                    +"AND OST.officeSettingType = :officeSettingType "
                    , OfficeSettingTO.class)
                    .setParameter("enrollmentOfficeId",depID )
//                    .setParameter("officeSettingType", officeSettingType)
                    .getResultList();
            em.flush();
            if(officeSettingTOs!= null && officeSettingTOs.size() != 0)
            	return officeSettingTOs.get(0);
            return null;
        } catch (Exception e) {
        	
        	//should change error code
            throw new DAOException(DataExceptionCode.ENI_014, DataExceptionCode.GLB_005_MSG, e);
        }

	}

	@Override
	public OfficeSettingTO findById(Long id) throws BaseException{
		try {
			return (OfficeSettingTO) em.createNamedQuery("OfficeSettingTO.findById")
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.OST_001, DataExceptionCode.OST_001_MSG, e);
		}

	}

}
