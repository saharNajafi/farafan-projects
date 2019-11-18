package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
public interface WorkstationPluginsService extends Service {

    void registerWorkstationPlugins(
            String workStationCode, List<WorkstationPluginsTO> workstationPluginsList) throws BaseException;

}
