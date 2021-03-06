package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/21/17.
 */
public interface WorkstationPluginsDAO extends EmsBaseDAO<WorkstationPluginsTO>{

    public WorkstationPluginsTO findByWorkstationById(Long workStationID) throws BaseException;

    public WorkstationPluginsTO findByWorkstationIdAndName(Long workStationID,String pluginName) throws BaseException;
}
