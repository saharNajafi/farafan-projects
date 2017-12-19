package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.ws.ClientHardWareSpecWTO;
import com.gam.nocr.ems.data.domain.ws.ClientNetworkConfigsWTO;
import com.gam.nocr.ems.data.domain.ws.ClientSoftWareSpecWTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public interface WorkstationInfoService extends Service {

    boolean isReliableVerInquiryRequired(String workStationId) throws BaseException;

    String getReliableVerByPlatform(String workStationCode, WorkstationInfoTO workstationInfoTO) throws BaseException;

    List<String> getCompatibleClientVerList() throws BaseException;
}
