package com.gam.nocr.ems.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ImsEstelamImageTO;
import com.gam.nocr.ems.data.enums.ImsEstelamImageType;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "ImsEstelamImageDAO")
@Local(ImsEstelamImageDAOLocal.class)
@Remote(ImsEstelamImageDAORemote.class)
public class ImsEstelamImageDAOImpl extends EmsBaseDAOImpl<ImsEstelamImageTO>
		implements ImsEstelamImageDAOLocal, ImsEstelamImageDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<ImsEstelamImageTO> findImsImageByNationalIdAndType(String nationalId,
			ImsEstelamImageType imsImageType) throws BaseException {
		
		List<ImsEstelamImageType> estelamImageTypes = new ArrayList<ImsEstelamImageType>();
		if(imsImageType.equals(ImsEstelamImageType.IMS_BOTH_IMAGE)){
			estelamImageTypes.add(ImsEstelamImageType.IMS_CERT_IMAGE);
			estelamImageTypes.add(ImsEstelamImageType.IMS_NID_IMAGE);
		}else
			estelamImageTypes.add(imsImageType);
		
        try {
             List<ImsEstelamImageTO> resultList = em.createQuery("select iei " +
                    "from ImsEstelamImageTO iei " +
                    "where iei.type in (:imsImageType) " +                    
                    "and iei.nationalID = :nationalId)", ImsEstelamImageTO.class)
                    .setParameter("nationalId", nationalId)
                    .setParameter("imsImageType", estelamImageTypes)
                    .getResultList();
            return resultList;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.IDI_002, DataExceptionCode.IDI_002_MSG, e);
        }
    
	}
	
	
	@Override
	public ImsEstelamImageTO findImsImageByNationalID(String nationalId) throws BaseException {		
		
        try {
             List<ImsEstelamImageTO> resultList = em.createQuery("select iei " +
                    "from ImsEstelamImageTO iei " +
                    "where iei.nationalID = :nationalId", ImsEstelamImageTO.class)
                    .setParameter("nationalId", nationalId)
                    .getResultList();
            
             if (EmsUtil.checkListSize(resultList))
     			return resultList.get(0);
     		else
     			return null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.IDI_003, DataExceptionCode.IDI_002_MSG, e);
        }
    
	}
	
	
	@Override
    public ImsEstelamImageTO create(ImsEstelamImageTO imsEstelamImageTO) throws BaseException {
        try {
        	ImsEstelamImageTO to = super.create(imsEstelamImageTO);
            em.flush();
            return to;
        } catch (Exception e) {            
            throw new DAOException(DataExceptionCode.IDI_001, DataExceptionCode.IDI_001_MSG, e);
        }
    }

   

}
