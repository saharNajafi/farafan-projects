package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public interface WorkstationInfoService extends Service {

    boolean workstationInfoRequired(String workStationId) throws BaseException;

    void registerWorkstationInfo(String workStationCode, WorkstationInfoTO workstationInfoTO) throws BaseException;

    List<String> getCompatibleClientVerList() throws BaseException;
}
