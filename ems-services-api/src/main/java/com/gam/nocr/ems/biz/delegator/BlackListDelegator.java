package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.BlackListService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.BlackListTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class BlackListDelegator implements Delegator {

    private BlackListService getService(UserProfileTO userProfileTO) throws BaseException {
        BlackListService enrollmentOfficeService = null;
        try {
            enrollmentOfficeService = (BlackListService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BLACK_LIST), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.BLD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_BLACK_LIST.split(","));
        }
        enrollmentOfficeService.setUserProfileTO(userProfileTO);
        return enrollmentOfficeService;
    }

    public BlackListTO load(UserProfileTO userProfileTO, long blackListId) throws BaseException {
        return getService(userProfileTO).load(blackListId);
    }

    public long save(UserProfileTO userProfileTO, BlackListTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, BlackListTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public boolean remove(UserProfileTO userProfileTO, String blackListIds) throws BaseException {
        return getService(userProfileTO).remove(blackListIds);
    }
}
