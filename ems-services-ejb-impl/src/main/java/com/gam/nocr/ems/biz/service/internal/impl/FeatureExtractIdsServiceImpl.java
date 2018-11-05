package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.FeatureExtractIdsDAO;
import com.gam.nocr.ems.data.dao.OfficeSettingDAO;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */

@Stateless(name = "FeatureExtractIdSService")
@Local(FeatureExtractIdsServiceLocal.class)
@Remote(FeatureExtractIdsServiceRemote.class)
public class FeatureExtractIdsServiceImpl extends EMSAbstractService
        implements FeatureExtractIdsServiceLocal, FeatureExtractIdsServiceRemote {

    @Override
    public SearchResult fetchFeatureExtractIdsNormalList(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
            HashMap param = new HashMap();
            StringBuilder parts = new StringBuilder();

            if (searchString.indexOf(" (") > 0) {
                param.put("name", "%" + searchString.substring(0, searchString.indexOf(" (")) + "%");
            } else {
                param.put("name", "%" + searchString + "%");
            }
            try {
                ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                        "featureExtractIdsAC", ("main" + parts).split(","), ("count" + parts).split(","), param, orderBy, null);
                List list = vlh.getList(from, to, true);
                return new SearchResult(vlh.size(), list);
            } catch (ListException e) {
                throw new ServiceException(BizExceptionCode.FEI_001, BizExceptionCode.GLB_006_MSG, e);
            } catch (ListHandlerException e) {
                throw new ServiceException(BizExceptionCode.FEI_002, BizExceptionCode.GLB_007_MSG, e);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.FEI_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }


    @Override
    public SearchResult fetchFeatureExtractIdsCCList(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
            HashMap param = new HashMap();
            StringBuilder parts = new StringBuilder();

            if (searchString.indexOf(" (") > 0) {
                param.put("name", "%" + searchString.substring(0, searchString.indexOf(" (")) + "%");
            } else {
                param.put("name", "%" + searchString + "%");
            }
            try {
                ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                        "featureExtractIdsCCAC", ("main" + parts).split(","), ("count" + parts).split(","), param, orderBy, null);
                List list = vlh.getList(from, to, true);
                return new SearchResult(vlh.size(), list);
            } catch (ListException e) {
                throw new ServiceException(BizExceptionCode.FEI_001, BizExceptionCode.GLB_006_MSG, e);
            } catch (ListHandlerException e) {
                throw new ServiceException(BizExceptionCode.FEI_002, BizExceptionCode.GLB_007_MSG, e);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.FEI_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public List<FeatureExtractIdsVTO> load(Long enrollmentOfficeId) throws BaseException {
        OfficeSettingTO officeSettingTO;
        FeatureExtractIdsVTO officeSettingVTO = new FeatureExtractIdsVTO();
        List<FeatureExtractIdsVTO> featureExtractIdsVTOs = new ArrayList<FeatureExtractIdsVTO>();

        try {
            if (enrollmentOfficeId == null)
                throw new ServiceException(BizExceptionCode.OST_001, BizExceptionCode.OST_001_MSG);
            officeSettingTO = getOfficeSettingDAO().findByOfficeId(enrollmentOfficeId);
            if (officeSettingTO == null)
                throw new ServiceException(BizExceptionCode.OST_002,
                        BizExceptionCode.OST_002_MSG, new Long[]{enrollmentOfficeId});
            officeSettingVTO.setOsdId(officeSettingTO.getId());
            if (officeSettingTO.getFeatureExtractIdsTO().size() > 0) {
                for(FeatureExtractIdsTO featureExtractIdsTO : officeSettingTO.getFeatureExtractIdsTO()){
                    officeSettingVTO.setFeiId(featureExtractIdsTO.getId());
                    officeSettingVTO.setFeatureExtractName(featureExtractIdsTO.getFeatureExtractName());
                    officeSettingVTO.setFeatureExtractType(String.valueOf(featureExtractIdsTO.getFeatureExtractType()));
                    featureExtractIdsVTOs.add(officeSettingVTO);
                }
            }

        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.OST_012, BizExceptionCode.OST_012_MSG, e);
        }
        return featureExtractIdsVTOs;
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

}
