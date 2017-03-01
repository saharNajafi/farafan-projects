package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.RatingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class RatingDelegator {

    private RatingService getService(UserProfileTO userProfileTO) throws BaseException {
        RatingService ratingService = null;
        try {
            ratingService = (RatingService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_RATING), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RTL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_RATING.split(","));
        }
        ratingService.setUserProfileTO(userProfileTO);
        return ratingService;
    }

    public RatingInfoTO load(UserProfileTO userProfileTO, long officeId) throws BaseException {
        return getService(userProfileTO).load(officeId);
    }

    public long save(UserProfileTO userProfileTO, RatingInfoTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, RatingInfoTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public boolean remove(UserProfileTO userProfileTO, String ratingInfoIds) throws BaseException {
        return getService(userProfileTO).remove(ratingInfoIds);
    }

    public SearchResult fetchRatingList(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchRatingList(searchString, from, to, orderBy);
    }

    /**
     * The method notifyPortalAboutRatingInfo is used to notify the sub system 'Portal' about the new or the modified
     * instances of type {@link RatingInfoTO}. This method is triggered vai a specified job.
     *
     * @throws BaseException
     */
    public void notifyPortalAboutRatingInfo() throws BaseException {
        getService(null).notifyPortalAboutModifiedRatingInfo();
    }
}
