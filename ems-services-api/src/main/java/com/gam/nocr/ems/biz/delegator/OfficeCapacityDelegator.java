package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.OfficeCapacityService;
import com.gam.nocr.ems.biz.service.RatingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/28/18.
 */
public class OfficeCapacityDelegator implements Delegator {

    private OfficeCapacityService getService(UserProfileTO userProfileTO) throws BaseException {
        OfficeCapacityService officeCapacityService = null;
        try {
            officeCapacityService = (OfficeCapacityService) ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_OFFICE_CAPACITY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RTL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_OFFICE_CAPACITY.split(","));
        }
        officeCapacityService.setUserProfileTO(userProfileTO);
        return officeCapacityService;
    }

    public List<OfficeCapacityTO> fetchOfficeCapacityList(UserProfileTO userProfileTO, long officeId) throws BaseException {
        return getService(userProfileTO).fetchOfficeCapacityList(officeId);
    }

    public long save(UserProfileTO userProfileTO, OfficeCapacityTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, OfficeCapacityTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public OfficeCapacityTO load(UserProfileTO userProfileTO, Long officeCapacityId) throws BaseException{
        return getService(userProfileTO).load(officeCapacityId);
    }

}
