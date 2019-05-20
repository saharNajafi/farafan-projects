package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.OfficeCapacityService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.OfficeCapacityVTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/28/18.
 */
public class OfficeCapacityDelegator implements Delegator {

    private OfficeCapacityService getService(UserProfileTO userProfileTO) throws BaseException {
        OfficeCapacityService officeCapacityService;
        try {
            officeCapacityService = ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(
                            EMSLogicalNames.SRV_OFFICE_CAPACITY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.ODL_001, BizExceptionCode.GLB_002_MSG, e
                    , EMSLogicalNames.SRV_OFFICE_CAPACITY.split(","));
        }
        officeCapacityService.setUserProfileTO(userProfileTO);
        return officeCapacityService;
    }
    public long save(UserProfileTO userProfileTO, OfficeCapacityVTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, OfficeCapacityVTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public boolean remove(UserProfileTO userProfileTO, String officeCapacityIds) throws BaseException {
        return getService(userProfileTO).remove(officeCapacityIds);
    }

    public OfficeCapacityVTO load(UserProfileTO userProfileTO, Long officeCapacityId) throws BaseException{
        return getService(userProfileTO).load(officeCapacityId);
    }

}
