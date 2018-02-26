package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.WorkstationDAO;
import com.gam.nocr.ems.data.dao.WorkstationPluginsDAO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
@Stateless(name = "WorkstationPluginsService")
@Local(WorkstationPluginsServiceLocal.class)
@Remote(WorkstationPluginsServiceRemote.class)
public class WorkstationPluginsServiceImpl extends EMSAbstractService
        implements WorkstationPluginsServiceLocal, WorkstationPluginsServiceRemote {

    public WorkstationPluginsDAO getWorkstationPluginsDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_WORKSTATIONPlugins));
        } catch (DAOFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WSI_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_WORKSTATIONPlugins});
        }
    }

    public WorkstationDAO getWorkstationDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_WORKSTATION));
        } catch (DAOFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WSI_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_WORKSTATION});
        }
    }

    @Override
    public String getReliableVerByPlugin(
            String workStationCode, List<WorkstationPluginsTO> workstationPluginsList) throws BaseException {
        String ccosExactVersion = null;
        WorkstationTO workstationTO = null;
        try {
            workstationTO =
                    getWorkstationDAO().findByActivationCode(workStationCode);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
        }
        try {
            if (workstationTO != null) {
                if (workstationTO != null) {
                    for(WorkstationPluginsTO workstationPluginsTO : workstationPluginsList){
                        workstationPluginsTO.setWorkstationTO(workstationTO);
                    }
                    for(Iterator<WorkstationPluginsTO> workstationPluginsTOIterator = workstationTO.getWorkstationPluginsTOList().iterator();
                        workstationPluginsTOIterator.hasNext(); ) {
                        WorkstationPluginsTO workstationPluginsTO = workstationPluginsTOIterator.next();
                        workstationPluginsTO.setWorkstationTO(null);
                        workstationPluginsTOIterator.remove();
                    }
                    workstationTO.getWorkstationPluginsTOList().addAll(workstationPluginsList);
                    getWorkstationDAO().update(workstationTO);
                    ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
                }
                ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_004_MSG);
        }

        return ccosExactVersion;
    }
}
