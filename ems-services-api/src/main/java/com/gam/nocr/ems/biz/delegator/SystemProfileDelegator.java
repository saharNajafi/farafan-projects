package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.SystemProfileService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.SystemProfileVTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class SystemProfileDelegator implements Delegator {

    private SystemProfileService getService(UserProfileTO userProfileTO) throws BaseException {
        SystemProfileService systemProfileService = null;
        try {
            systemProfileService = (SystemProfileService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_SYSTEM_PROFILE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.SPL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_SYSTEM_PROFILE.split(","));
        }
        systemProfileService.setUserProfileTO(userProfileTO);
        return systemProfileService;
    }

    public void save(UserProfileTO userProfileTO, SystemProfileVTO systemProfileVTO) throws BaseException {
        getService(userProfileTO).save(systemProfileVTO);
    }

    public void remove(UserProfileTO userProfileTO, String keyName) throws BaseException {
        getService(userProfileTO).remove(keyName);
    }

    public void reload(UserProfileTO userProfileTO) throws BaseException {
        getService(userProfileTO).reload();
    }

}
