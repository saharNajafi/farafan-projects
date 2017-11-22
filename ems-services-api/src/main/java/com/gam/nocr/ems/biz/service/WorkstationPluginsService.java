package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
public interface WorkstationPluginsService extends Service {

    String getReliableVerByPlugin(String workStationId, List<PluginInfoVTO> pluginInfoList) throws BaseException;

}
