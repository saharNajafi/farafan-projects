package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.WorkstationDAO;
import com.gam.nocr.ems.data.dao.WorkstationPluginsDAO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
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
    public void registerWorkstationPlugins (
            String workstationCode, List<WorkstationPluginsTO> workstationPluginsList) throws BaseException {

        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_002, BizExceptionCode.WST_002_MSG);
            if(workstationCode.length() < 40)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
            if(workstationCode.length() > 40)
                throw new ServiceException(BizExceptionCode.WST_004, BizExceptionCode.WST_004_MSG);
            WorkstationTO workstationTO =
                    getWorkstationDAO().findByActivationCode(workstationCode);
            if (workstationTO == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            for (WorkstationPluginsTO workstationPlugin : workstationPluginsList) {
                WorkstationPluginsTO workstationPluginsTO = new WorkstationPluginsTO();
                workstationPluginsTO.setWorkstationTO(workstationTO);
                workstationPluginsTO.setPluginName(workstationPlugin.getPluginName());
//                    TODO
                workstationPluginsTO.setState(workstationPlugin.getState());
                getWorkstationPluginsDAO().create(workstationPluginsTO);
            }

        } catch (BaseException e) {
            e.printStackTrace();
        }
    }
}