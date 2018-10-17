package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.FeatureExtractIdsDAO;
import com.gam.nocr.ems.data.dao.FeatureExtractVersionsDAO;
import com.gam.nocr.ems.data.dao.OfficeSettingDAO;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.FeatureExtractVersionsTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.domain.vol.OfficeSettingVTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
@Stateless(name = "OfficeSettingService")
@Local(OfficeSettingServiceLocal.class)
@Remote(OfficeSettingServiceRemote.class)
public class OfficeSettingServiceImpl extends EMSAbstractService
        implements OfficeSettingServiceLocal, OfficeSettingServiceRemote {


    @Override
    public Long save(OfficeSettingVTO officeSettingVTO) throws BaseException {
        OfficeSettingTO officeSettingTO = new OfficeSettingTO();
        FeatureExtractIdsTO featureExtractIdsTO;
        FeatureExtractVersionsTO featureExtractVersionsTO;
        try {
            featureExtractIdsTO = getFeatureExtractIdsDAO().findById(officeSettingVTO.getFeiId());
            if(featureExtractIdsTO == null)
                throw new ServiceException(BizExceptionCode.OST_007, BizExceptionCode.OST_007_MSG, new Long[]{officeSettingVTO.getFeiId()});
            featureExtractVersionsTO = getFeatureExtractVersionsDAO().findById(officeSettingVTO.getFevId());
            if(featureExtractVersionsTO == null)
                throw new ServiceException(BizExceptionCode.OST_008, BizExceptionCode.OST_008_MSG, new Long[]{officeSettingVTO.getFevId()});
            officeSettingTO.setFeatureExtractIdsTO(featureExtractIdsTO);
            officeSettingTO.setFeatureExtractVersionsTO(featureExtractVersionsTO);
            officeSettingTO = getOfficeSettingDAO().create(officeSettingTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OST_013, BizExceptionCode.OST_013_MSG, e);
        }
        return officeSettingTO.getId();
    }

    @Override
    public Long update(OfficeSettingVTO officeSettingVTO) throws BaseException {
        OfficeSettingTO officeSettingTO;
        FeatureExtractIdsTO featureExtractIdsTO;
        FeatureExtractVersionsTO featureExtractVersionsTO;
        try {
            officeSettingTO = getOfficeSettingDAO().findById(officeSettingVTO.getId());
            if (officeSettingTO == null)
                throw new ServiceException(BizExceptionCode.OST_003, BizExceptionCode.OST_003_MSG, new Long[]{officeSettingVTO.getId()});
            featureExtractIdsTO = getFeatureExtractIdsDAO().findById(officeSettingVTO.getFeiId());
            if(featureExtractIdsTO == null)
                throw new ServiceException(BizExceptionCode.OST_009, BizExceptionCode.OST_007_MSG, new Long[]{officeSettingVTO.getFeiId()});
            featureExtractVersionsTO = getFeatureExtractVersionsDAO().findById(officeSettingVTO.getFevId());
            if(featureExtractVersionsTO == null)
                throw new ServiceException(BizExceptionCode.OST_010, BizExceptionCode.OST_008_MSG, new Long[]{officeSettingVTO.getFevId()});
            officeSettingTO.setFeatureExtractIdsTO(featureExtractIdsTO);
            officeSettingTO.setFeatureExtractVersionsTO(featureExtractVersionsTO);
            getOfficeSettingDAO().update(officeSettingTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OST_011, BizExceptionCode.OST_011_MSG, e);
        }
        return officeSettingTO != null ? officeSettingTO.getId() : null;
    }

    @Override
    public OfficeSettingVTO load(Long enrollmentOfficeId) throws BaseException {
        OfficeSettingTO officeSettingTO;
        OfficeSettingVTO officeSettingVTO = new OfficeSettingVTO();
        try {
            if (enrollmentOfficeId == null)
                throw new ServiceException(BizExceptionCode.OST_001, BizExceptionCode.OST_001_MSG);
            officeSettingTO = getOfficeSettingDAO().findByOfficeId(enrollmentOfficeId);
            if (officeSettingTO == null)
                throw new ServiceException(BizExceptionCode.OST_002,
                        BizExceptionCode.OST_002_MSG, new Long[]{enrollmentOfficeId});
            officeSettingVTO.setId(officeSettingTO.getId());
            if(officeSettingTO.getFeatureExtractIdsTO()!= null) {
                officeSettingVTO.setFeatureExtractorID(officeSettingTO.getFeatureExtractIdsTO().getFeatureExtractId());
                officeSettingVTO.setFeiId(officeSettingTO.getFeatureExtractIdsTO().getId());
            }
            if(officeSettingTO.getFeatureExtractVersionsTO()!= null) {
                officeSettingVTO.setFevId(officeSettingTO.getFeatureExtractVersionsTO().getId());
                officeSettingVTO.setFeatureExtractorVersion(
                        officeSettingTO.getFeatureExtractVersionsTO().getFeatureExtractVersion());
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OST_012, BizExceptionCode.OST_012_MSG, e);
        }
        return officeSettingVTO;
    }

    private OfficeSettingDAO getOfficeSettingDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_SETTING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OST_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_SETTING.split(","));
        }
    }

    private FeatureExtractVersionsDAO getFeatureExtractVersionsDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_FEATURE_EXTRACT_VERSIONS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OST_005,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_FEATURE_EXTRACT_VERSIONS.split(","));
        }
    }

    private FeatureExtractIdsDAO getFeatureExtractIdsDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_FEATURE_EXTRACT_IDS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OST_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_FEATURE_EXTRACT_IDS.split(","));
        }
    }
}
