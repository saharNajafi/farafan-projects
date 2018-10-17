package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.FeatureExtractVersionsService;
import com.gam.nocr.ems.biz.service.RatingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.FeatureExtractVersionsTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public class FeatureExtractVersionsDelegator {

    private FeatureExtractVersionsService getService(UserProfileTO userProfileTO) throws BaseException {
        FeatureExtractVersionsService featureExtractVersionsService = null;
        try {
            featureExtractVersionsService =
                    (FeatureExtractVersionsService) ServiceFactoryProvider.getServiceFactory().getService(
                            EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_FEATURE_EXTRACT_VERSIONS)
                            , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.FEV_001
                    , BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_FEATURE_EXTRACT_VERSIONS.split(","));
        }
        featureExtractVersionsService.setUserProfileTO(userProfileTO);
        return featureExtractVersionsService;
    }

    public SearchResult FeatureExtractVersionList(
            UserProfileTO userProfile, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfile).FeatureExtractVersionList(searchString, from, to, orderBy);
    }

}
