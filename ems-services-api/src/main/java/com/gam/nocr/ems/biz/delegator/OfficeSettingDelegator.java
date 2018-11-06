package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.OfficeSettingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.OfficeSettingVTO;
import com.gam.nocr.ems.util.EmsUtil;


/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
public class OfficeSettingDelegator implements Delegator{

    private OfficeSettingService getService(UserProfileTO userProfileTO) throws BaseException {

        OfficeSettingService officeSettingService = null;
        try {
            officeSettingService = (OfficeSettingService) ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_OFFICE_SETTING)
                    , EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.OSTD_001
                    , BizExceptionCode.GLB_002_MSG, e
                    , EMSLogicalNames.SRV_OFFICE_SETTING.split(","));
        }
        officeSettingService.setUserProfileTO(userProfileTO);
        return officeSettingService;
    }

public Long update(UserProfileTO userProfile, OfficeSettingVTO to) throws BaseException {
        return getService(userProfile).update(to);
    }

}
