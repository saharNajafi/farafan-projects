package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.stateprovider.StateProviderTO;
import com.gam.nocr.ems.biz.service.StateProviderService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class StateProviderDelegator implements Delegator {

    private StateProviderService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        StateProviderService stateProviderService = null;
        try {
            stateProviderService = (StateProviderService) factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_STATE_PROVIDER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.SDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_STATE_PROVIDER.split(","));
        }
        stateProviderService.setUserProfileTO(userProfileTO);
        return stateProviderService;
    }

    public List<StateProviderTO> getState(UserProfileTO userProfileTO, String moduleName, List<String> stateIds) throws BaseException {
        return getService(userProfileTO).getState(moduleName, stateIds);
    }

    public void setState(UserProfileTO userProfileTO, String moduleName, List<StateProviderTO> records) throws BaseException {
        getService(userProfileTO).setState(moduleName, records);
    }
}
