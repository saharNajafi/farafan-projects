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
import com.gam.nocr.ems.data.domain.ws.PluginInfoWTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/22/17.
 */
@Stateless(name = "WorkstationPluginsService")
@Local(WorkstationPluginsServiceLocal.class)
@Remote(WorkstationPluginsServiceRemote.class)
public class WorkstationPluginsServiceImpl extends EMSAbstractService
        implements WorkstationPluginsServiceLocal, WorkstationPluginsServiceRemote{

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
    public String getReliableVerByPlugin (
            String workstationCode, List<WorkstationPluginsTO> workstationPluginsList) throws BaseException {
        String ccosExactVersion = null;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_002, BizExceptionCode.EMSWorkstationPMService0002);
            if(workstationCode.length() < 48)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.EMSWorkstationPMService0003);
            if(workstationCode.length() > 48)
                throw new ServiceException(BizExceptionCode.WST_004, BizExceptionCode.EMSWorkstationPMService0004);

            WorkstationTO workstationTO = getWorkstationDAO().findByActivationCode(workstationCode);
            if (workstationTO == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.EMSWorkstationPMService0001);
            for (WorkstationPluginsTO workstationPlugin : workstationPluginsList) {
                WorkstationPluginsTO workstationPluginsTO = new WorkstationPluginsTO();
                workstationPluginsTO.setWorkstationTO(workstationTO);
                workstationPluginsTO.setPluginName(workstationPlugin.getPluginName());
//                    TODO
                workstationPluginsTO.setState(workstationPlugin.getState());
                getWorkstationPluginsDAO().create(workstationPluginsTO);
            }
                ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_004_MSG);
        }

        return ccosExactVersion;
    }
}
