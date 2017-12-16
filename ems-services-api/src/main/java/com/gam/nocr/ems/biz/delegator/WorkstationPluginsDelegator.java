package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.WorkstationPluginsService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ws.PluginInfoWTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
public class WorkstationPluginsDelegator implements Delegator {

    private WorkstationPluginsService getService(UserProfileTO userProfileTO) throws BaseException {
        WorkstationPluginsService workstationPluginsService = null;
        try {
            workstationPluginsService =
                    (WorkstationPluginsService) ServiceFactoryProvider.getServiceFactory().getService(
                            EMSLogicalNames.getServiceJNDIName(
                                    EMSLogicalNames.SRV_WORKSTATIONPLUGINS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WDL_001, BizExceptionCode.GLB_002_MSG,
                    e, EMSLogicalNames.SRV_WORKSTATIONPLUGINS.split(","));
        }
        workstationPluginsService.setUserProfileTO(userProfileTO);
        return workstationPluginsService;
    }

    public String getReliableVerByPlugin(
            UserProfileTO userProfileTO,String workStationCode, List<PluginInfoWTO> pluginInfoList)
            throws BaseException {
        String verCode = null;
        try {
            verCode = getService(userProfileTO).getReliableVerByPlugin(workStationCode, pluginInfoList);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }
}
