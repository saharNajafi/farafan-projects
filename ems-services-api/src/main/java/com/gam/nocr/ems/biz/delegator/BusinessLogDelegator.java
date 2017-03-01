package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Sina Golesorkhi
 */
public class BusinessLogDelegator {

    private BusinessLogService getService(UserProfileTO userProfileTO) throws BaseException {
        BusinessLogService businessLogService;
        try {
            businessLogService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.BGD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        businessLogService.setUserProfileTO(userProfileTO);
        return businessLogService;
    }

    public void createBusinessLog(UserProfileTO userProfileTO,
                                  BusinessLogAction logAction,
                                  BusinessLogEntity logEntityName,
                                  String logActor,
                                  String additionalData,
                                  Boolean exceptionFlag) throws BaseException {
        getService(userProfileTO).createBusinessLog(logAction, logEntityName, logActor, additionalData, exceptionFlag);
    }

    public boolean verify(UserProfileTO userProfileTO, String id) throws BaseException {
        return getService(userProfileTO).verify(id);
    }
}
