package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public interface WorkstationInfoDAO extends EmsBaseDAO<WorkstationInfoTO>{
    public WorkstationInfoTO isReliableVerInquiryRequired(String workStationId) throws BaseException;

    public String getReliableVerByPlatform(
            String workStationId, ClientHardWareSpecVTO clientHardWareSpec,
            ClientNetworkConfigsVTO clientNetworkConfig, ClientSoftWareSpecVTO clientSoftWareSpec) throws BaseException;


}
