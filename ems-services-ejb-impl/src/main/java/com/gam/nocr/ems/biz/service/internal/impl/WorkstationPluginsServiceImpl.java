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
import com.gam.nocr.ems.data.dao.WorkstationPluginsDAO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;
import com.gam.nocr.ems.util.EmsUtil;

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

    @Override
    public String getReliableVerByPlugin (
            String workStationId, List<PluginInfoVTO> pluginInfoList) throws BaseException {
        String ccosExactVersion = null;
        try {
            if (workStationId == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            WorkstationPluginsTO workstationPlugins =
                    getWorkstationPluginsDAO().findByWorkstationId(workStationId);
            if (workstationPlugins != null) {
                for (PluginInfoVTO pluginInfo : pluginInfoList) {
                    workstationPlugins.setPluginName(pluginInfo.getPluginName());
                    workstationPlugins.setState(Short.valueOf(pluginInfo.getState()));
                    getWorkstationPluginsDAO().update(workstationPlugins);
                }
            } else {
                WorkstationPluginsTO workstationPluginsTO = new WorkstationPluginsTO();
                for (PluginInfoVTO pluginInfo : pluginInfoList) {
                    workstationPluginsTO.setPluginName(pluginInfo.getPluginName());
                    workstationPluginsTO.setState(Short.valueOf(pluginInfo.getState()));
                    getWorkstationPluginsDAO().create(workstationPluginsTO);
                }
            }
            ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }
}
