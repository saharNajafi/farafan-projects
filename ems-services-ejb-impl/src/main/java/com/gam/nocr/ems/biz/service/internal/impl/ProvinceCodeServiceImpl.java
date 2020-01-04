package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.EnrollmentOfficeService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.LocationDAO;
import com.gam.nocr.ems.data.dao.ProvinceCodeDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.ProvinceCodeTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PROVINCE_CODE;

/**
 * @author Mazaher Namjoofar
 */

@Stateless(name = "ProvinceCodeService")
@Local(ProvinceCodeServiceLocal.class)
@Remote(ProvinceCodeServiceRemote.class)
public class ProvinceCodeServiceImpl extends EMSAbstractService implements ProvinceCodeServiceLocal, ProvinceCodeServiceRemote {

    private ProvinceCodeDAO getProvinceCodeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(DAO_PROVINCE_CODE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.PCS_001,
                    BizExceptionCode.GLB_001_MSG, e, DAO_PROVINCE_CODE.split(","));
        }
    }


    private EnrollmentOfficeService getEnrollmentOfficeService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PCS_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_ENROLLMENT_OFFICE.split(","));
        }
        enrollmentOfficeService.setUserProfileTO(getUserProfileTO());
        return enrollmentOfficeService;
    }


    @Override
    public ProvinceCodeTO findByLocationId(Object locationId) throws BaseException {
        try {
            return getProvinceCodeDAO().findByLocationId(ProvinceCodeTO.class, locationId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PCS_002, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public ProvinceCodeTO findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException {
        try {
            EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeService().findEnrollmentOfficeById(enrollmentOfficeId);
            if (enrollmentOfficeTO == null) {
                Object[] args = {enrollmentOfficeId};
                throw new ServiceException(BizExceptionCode.PCS_004, BizExceptionCode.PCS_004_MSG, args);
            }
            return findByLocationId(enrollmentOfficeTO.getLocation().getProvince().getId());
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PCS_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }
}
