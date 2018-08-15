package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.InternalServiceChecker;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public class InternalServiceCheckerDelegator implements Delegator {

    private InternalServiceChecker getService(UserProfileTO userProfileTO) throws BaseException {
        InternalServiceChecker internalServiceChecker;
        try {
            internalServiceChecker = ServiceFactoryProvider.getServiceFactory().getService(
                    EMSLogicalNames.getServiceJNDIName(
                            EMSLogicalNames.SRV_INTERNAL_SERVICE_CHEKER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.ODL_001, BizExceptionCode.GLB_002_MSG,
                    e, EMSLogicalNames.SRV_INTERNAL_SERVICE_CHEKER.split(","));
        }
        internalServiceChecker.setUserProfileTO(userProfileTO);
        return internalServiceChecker;
    }

    public CardRequestTO inquiryHasCardRequest(UserProfileTO userProfileTO, String nationalId) throws BaseException {
        return getService(userProfileTO).inquiryHasCardRequest(nationalId);
    }
}
