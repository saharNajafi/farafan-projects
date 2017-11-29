package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import java.util.List;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface WorkstationService extends Service {

    public long save(WorkstationTO to) throws BaseException;

    public WorkstationTO load(Long workstationId) throws BaseException;

    public boolean remove(String workstationIds) throws BaseException;

    public WorkstationTO find(String workstationActivationCode) throws BaseException;

    public void approveWorkstation(String workstationIds) throws BaseException;

    public void rejectWorkstation(String workstationIds) throws BaseException;

    public WorkstationTO findByDepartmentIdAndActivationCode(Long departmentId, String workstationActivationCode) throws BaseException;

}