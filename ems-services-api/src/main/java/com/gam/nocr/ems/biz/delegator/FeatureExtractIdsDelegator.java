package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.FeatureExtractIdsService;
import com.gam.nocr.ems.biz.service.OfficeSettingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public class FeatureExtractIdsDelegator implements Delegator {

    private FeatureExtractIdsService getService(UserProfileTO userProfileTO) throws BaseException {
        FeatureExtractIdsService featureExtractIdsService;
        try {
            featureExtractIdsService = ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_FEATURE_EXTRACT_IDS)
                    , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.FEID_001,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_FEATURE_EXTRACT_IDS.split(","));
        }
        featureExtractIdsService.setUserProfileTO(userProfileTO);
        return featureExtractIdsService;
    }


    public SearchResult fetchFeatureExtractIdsNormalList(UserProfileTO userProfile, String searchString
            , int from, int to, String orderBy) throws BaseException {
        return getService(userProfile).fetchFeatureExtractIdsNormalList(searchString, from, to, orderBy);
    }

  public SearchResult fetchFeatureExtractIdsCCList(UserProfileTO userProfile, String searchString
            , int from, int to, String orderBy) throws BaseException {
        return getService(userProfile).fetchFeatureExtractIdsCCList(searchString, from, to, orderBy);
    }

    public SearchResult fetchFeatureExtractorIdList(UserProfileTO userProfile, String searchString
            , int from, int to, String orderBy) throws BaseException {
        return getService(userProfile).fetchFeatureExtractorIdList(searchString, from, to, orderBy);
    }

    public List<FeatureExtractIdsVTO> load(UserProfileTO userProfile, Long officeId) throws BaseException {
        return getService(userProfile).load(officeId);
    }


}
