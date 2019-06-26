package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.FeatureExtractIdsDAO;
import com.gam.nocr.ems.data.dao.OfficeSettingDAO;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.domain.vol.OfficeSettingVTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
@Stateless(name = "OfficeSettingService")
@Local(OfficeSettingServiceLocal.class)
@Remote(OfficeSettingServiceRemote.class)
public class OfficeSettingServiceImpl extends EMSAbstractService
        implements OfficeSettingServiceLocal, OfficeSettingServiceRemote {

    @Override
    public Long update(OfficeSettingVTO officeSettingVTO) throws BaseException {
        OfficeSettingTO officeSettingTO;
        Set<FeatureExtractIdsTO> featureExtractIdsTOList;
        try {
            officeSettingTO = getOfficeSettingDAO().findById(officeSettingVTO.getId());
            if (officeSettingTO == null)
                throw new ServiceException(BizExceptionCode.OSTS_001,
                        BizExceptionCode.OSTS_001_MSG, new Long[]{officeSettingVTO.getId()});
            featureExtractIdsTOList =
                    getFeatureExtractIdsList(officeSettingVTO.getFeiN(), officeSettingVTO.getFeiCC(), officeSettingVTO.getFeiISOCC());
            officeSettingTO.setFeatureExtractIdsTO(featureExtractIdsTOList);
            getOfficeSettingDAO().update(officeSettingTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OSTS_002, BizExceptionCode.OSTS_002_MSG, e);
        }
        return officeSettingTO != null ? officeSettingTO.getId() : null;
    }

    private Set<FeatureExtractIdsTO> getFeatureExtractIdsList(Long fenId, Long fecId, Long feiISOCC) throws BaseException{
        FeatureExtractIdsTO featureExtractNormal;
        FeatureExtractIdsTO featureExtractCC;
        FeatureExtractIdsTO MOCEngineEnhancement;
        Set<FeatureExtractIdsTO> featureExtractIdsTOList = new HashSet<FeatureExtractIdsTO>();
        try {
            featureExtractNormal = getFeatureExtractIdsDAO().findById(fenId);
            featureExtractCC = getFeatureExtractIdsDAO().findById(fecId);
            MOCEngineEnhancement = getFeatureExtractIdsDAO().findById(feiISOCC);
            if (featureExtractNormal == null)
                throw new ServiceException(BizExceptionCode.OSTS_003
                        , BizExceptionCode.OSTS_003_MSG, new Long[]{fenId});
            if (featureExtractCC == null)
                throw new ServiceException(BizExceptionCode.OSTS_004
                         , BizExceptionCode.OSTS_004_MSG, new Long[]{fecId});
            if (MOCEngineEnhancement == null)
                throw new ServiceException(BizExceptionCode.OSTS_008
                         , BizExceptionCode.OSTS_008_MSG, new Long[]{fecId});
            featureExtractIdsTOList.add(featureExtractNormal);
            featureExtractIdsTOList.add(featureExtractCC);
            featureExtractIdsTOList.add(MOCEngineEnhancement);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OSTS_005, BizExceptionCode.OSTS_005_MSG, e);
        }
        return featureExtractIdsTOList;
    }

    private FeatureExtractIdsDAO getFeatureExtractIdsDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_FEATURE_EXTRACT_IDS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OSTS_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_FEATURE_EXTRACT_IDS.split(","));
        }
    }

    private OfficeSettingDAO getOfficeSettingDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_SETTING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OSTS_007,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_SETTING.split(","));
        }
    }
}
