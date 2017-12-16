package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.ws.PluginInfoWTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
public interface WorkstationPluginsService extends Service {

    String getReliableVerByPlugin(String workStationCode, List<PluginInfoWTO> pluginInfoList) throws BaseException;

}
