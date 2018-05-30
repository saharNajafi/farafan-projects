package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.OfficeCapacityDAO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;

import javax.ejb.*;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeCapacityService")
@Local(OfficeCapacityServiceLocal.class)
@Remote(OfficeCapacityServiceRemote.class)
public class OfficeCapacityServiceImpl extends EMSAbstractService implements
        OfficeCapacityServiceLocal, OfficeCapacityServiceRemote {


    @Override
    @Permissions(value = "ems_editOfficeCapacity || ems_addOfficeCapacity")
    @BizLoggable(logAction = "INSERT", logEntityName = "EMST_OFFICE_CAPACITY")
    public Long save(OfficeCapacityTO officeCapacity) throws BaseException {
        checkOfficeCapacity(officeCapacity);
      OfficeCapacityTO officeCapacityTO =  getOfficeCapacityDAO().create(officeCapacity);
        return officeCapacityTO.getId();
    }


    @Override
    @Permissions(value = "ems_editOfficeCapacity")
    @BizLoggable(logAction = "UPDATE", logEntityName = "EMST_OFFICE_CAPACITY")
    public Long update(OfficeCapacityTO officeCapacity) throws BaseException {
        checkOfficeCapacity(officeCapacity);
        OfficeCapacityTO officeCapacityTO = getOfficeCapacityDAO().find(OfficeCapacityTO.class, officeCapacity.getId());
        if(officeCapacityTO == null)
            throw new ServiceException(BizExceptionCode.OC_002, BizExceptionCode.OC_002_MSG, new Long[]{officeCapacity.getId()});
        officeCapacityTO.setStartDate(officeCapacity.getStartDate());
        officeCapacityTO.setEndDate(officeCapacity.getEndDate());
        officeCapacityTO.setShiftNo(officeCapacity.getShiftNo());
        officeCapacityTO.setCapacity(officeCapacity.getCapacity());
        officeCapacityTO.setIsActive(officeCapacity.getIsActive());
        getOfficeCapacityDAO().update(officeCapacityTO);
        return officeCapacityTO.getId();
    }
    @Override
    @Permissions(value = "ems_viewOfficeCapacityList")
    public List<OfficeCapacityTO> fetchOfficeCapacityList(Long enrollmentOfficeId) throws BaseException {
        if(enrollmentOfficeId == null)
            throw new ServiceException(BizExceptionCode.OC_003, BizExceptionCode.OC_003_MSG);

        List<OfficeCapacityTO> officeCapacityList = getOfficeCapacityDAO().findByEnrollmentOfficeId(enrollmentOfficeId);
        if(officeCapacityList == null)
            throw new ServiceException(BizExceptionCode.OC_004,
                    BizExceptionCode.OC_004_MSG, new Long[] { enrollmentOfficeId });
        return officeCapacityList;
    }

    @Override
    @Permissions(value = "ems_viewOfficeCapacity")
    @BizLoggable(logAction = "LOAD", logEntityName = "EMST_OFFICE_CAPACITY")
    public OfficeCapacityTO load(Long officeCapacityId) throws BaseException{
        OfficeCapacityTO officeCapacityTO ;
        if(officeCapacityId == null)
            throw new ServiceException(BizExceptionCode.OC_001, BizExceptionCode.OC_001_MSG);
        officeCapacityTO = getOfficeCapacityDAO().find(OfficeCapacityTO.class, officeCapacityId);
        if(officeCapacityTO == null)
            throw new ServiceException(BizExceptionCode.OC_005,
                    BizExceptionCode.OC_005_MSG , new Long[] {officeCapacityId});
        return officeCapacityTO;
    }

    private void checkOfficeCapacity(OfficeCapacityTO officeCapacity) throws BaseException {

    }

    private OfficeCapacityDAO getOfficeCapacityDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_CAPACITY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_086,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_CAPACITY.split(","));
        }
    }


}
