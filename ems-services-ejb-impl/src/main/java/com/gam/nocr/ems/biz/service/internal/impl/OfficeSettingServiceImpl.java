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
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    public Long save(FeatureExtractIdsVTO officeSettingVTO) throws BaseException {
//        OfficeSettingTO officeSettingTO = new OfficeSettingTO();
//        Set<FeatureExtractIdsTO> featureExtractIdsTOList;
//        try {
//            featureExtractIdsTOList =
//                    getFeatureExtractIdsList(officeSettingVTO.getFeiN(), officeSettingVTO.getFeiCC());
//            officeSettingTO.setFeatureExtractIdsTO(featureExtractIdsTOList);
//            officeSettingTO = getOfficeSettingDAO().create(officeSettingTO);
//        } catch (BaseException e) {
//            throw new ServiceException(BizExceptionCode.OST_013, BizExceptionCode.OST_013_MSG, e);
//        }
//        return officeSettingTO.getId();
        return null;
    }

    @Override
    public Long update(FeatureExtractIdsVTO officeSettingVTO) throws BaseException {
        OfficeSettingTO officeSettingTO;
        Set<FeatureExtractIdsTO> featureExtractIdsTOList;
//        try {
//            officeSettingTO = getOfficeSettingDAO().findById(officeSettingVTO.getId());
//            if (officeSettingTO == null)
//                throw new ServiceException(BizExceptionCode.OST_003,
//                        BizExceptionCode.OST_003_MSG, new Long[]{officeSettingVTO.getId()});
//            featureExtractIdsTOList =
//                    getFeatureExtractIdsList(officeSettingVTO.getFeiN(), officeSettingVTO.getFeiCC());
//            officeSettingTO.setFeatureExtractIdsTO(featureExtractIdsTOList);
//            getOfficeSettingDAO().update(officeSettingTO);
//        } catch (BaseException e) {
//            throw new ServiceException(BizExceptionCode.OST_011, BizExceptionCode.OST_011_MSG, e);
//        }
//        return officeSettingTO != null ? officeSettingTO.getId() : null;
        return null;
    }

    private Set<FeatureExtractIdsTO> getFeatureExtractIdsList(Long fenId, Long fecId) {
        FeatureExtractIdsTO featureExtractNormal;
        FeatureExtractIdsTO featureExtractCC;
        Set<FeatureExtractIdsTO> featureExtractIdsTOList = new HashSet<FeatureExtractIdsTO>();
        try {
            featureExtractNormal = getFeatureExtractIdsDAO().findById(fenId);
            featureExtractCC = getFeatureExtractIdsDAO().findById(fecId);
            if (featureExtractNormal == null)
                throw new ServiceException(BizExceptionCode.OST_007
                        , BizExceptionCode.OST_007_MSG, new Long[]{fenId});
            if (featureExtractCC == null)
                throw new ServiceException(BizExceptionCode.OST_008
                        , BizExceptionCode.OST_008_MSG, new Long[]{fecId});
            featureExtractIdsTOList.add(featureExtractNormal);
            featureExtractIdsTOList.add(featureExtractCC);
        } catch (BaseException e) {
            e.printStackTrace();
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
            throw new ServiceException(BizExceptionCode.OST_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_FEATURE_EXTRACT_IDS.split(","));
        }
    }
}
