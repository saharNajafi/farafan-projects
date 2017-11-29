package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface WorkstationDAO extends EmsBaseDAO<WorkstationTO> {

    public boolean removeWorkstations(String workstationIds) throws BaseException;

    public WorkstationTO findByActivationCode(String workstationActivationCode) throws BaseException;

    public void approveWorkstation(String workstationIds) throws BaseException;

    public void rejectWorkstation(String workstationIds) throws BaseException;

    public WorkstationTO findByDepartmentIdAndActivationCode(Long departmentId, String workstationActivationCode) throws BaseException;

}
