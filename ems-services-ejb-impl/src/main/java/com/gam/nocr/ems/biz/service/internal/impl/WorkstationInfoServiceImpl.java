package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ConfigurationFileHandler;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.WorkstationInfoDAO;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
@Stateless(name = "WorkstationInfoService")
@Local(WorkstationInfoServiceLocal.class)
@Remote(WorkstationInfoServiceRemote.class)
public class WorkstationInfoServiceImpl extends EMSAbstractService
        implements WorkstationInfoServiceLocal, WorkstationInfoServiceRemote {
    public WorkstationInfoDAO getWorkstationInfoDAO() throws BaseException {
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
    public WorkstationInfoTO isReliableVerInquiryRequired(String workStationId) throws BaseException {
        WorkstationInfoTO workstationInfoTO = null;
        try {
            if (workStationId == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            workstationInfoTO = getWorkstationInfoDAO().isReliableVerInquiryRequired(workStationId);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return workstationInfoTO;
    }

    @Override
    public String getReliableVerByPlatform(String workStationId, ClientHardWareSpecVTO clientHardWareSpec,
                                           ClientNetworkConfigsVTO clientNetworkConfig,
                                           ClientSoftWareSpecVTO clientSoftWareSpec) throws BaseException {
        String ccosExactVersion = null;
        try {
            if (workStationId == null)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
            WorkstationInfoTO workstationInfo = getWorkstationInfoDAO().isReliableVerInquiryRequired(workStationId);
//            update workstationInfo
            if (workstationInfo != null) {
                workstationInfo.setMacAddressList(String.valueOf(clientHardWareSpec.getMacAddressList()));
                workstationInfo.setCpuType(clientHardWareSpec.getCpuType());
                workstationInfo.setRamCapacity(clientHardWareSpec.getRamCapacity());
                workstationInfo.setOsVersion(clientSoftWareSpec.getOsVersion());
                workstationInfo.setHasDotnetFramwork45(Short.parseShort(
                        (clientSoftWareSpec.getDotNetwork45Installed()).toString()));
                workstationInfo.setIpAddressList(String.valueOf(clientNetworkConfig.getIpAddressList()));
                workstationInfo.setComputerName(clientNetworkConfig.getComputerName());
                workstationInfo.setUsername(clientNetworkConfig.getUserName());
                workstationInfo.setGateway(clientNetworkConfig.getGateway());
                getWorkstationInfoDAO().update(workstationInfo);
            }
//            save workstationInfo
            WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();
            workstationInfoTO.setMacAddressList(String.valueOf(clientHardWareSpec.getMacAddressList()));
            workstationInfoTO.setCpuType(clientHardWareSpec.getCpuType());
            workstationInfoTO.setRamCapacity(clientHardWareSpec.getRamCapacity());
            workstationInfoTO.setOsVersion(clientSoftWareSpec.getOsVersion());
            workstationInfoTO.setHasDotnetFramwork45(Short.parseShort(
                    (clientSoftWareSpec.getDotNetwork45Installed()).toString()));
            workstationInfoTO.setIpAddressList(String.valueOf(clientNetworkConfig.getIpAddressList()));
            workstationInfoTO.setComputerName(clientNetworkConfig.getComputerName());
            workstationInfoTO.setUsername(clientNetworkConfig.getUserName());
            workstationInfoTO.setGateway(clientNetworkConfig.getGateway());
            getWorkstationInfoDAO().create(workstationInfoTO);
            ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }

    @Override
    public String getReliableVerByPlugin(
            String workStationId, List<PluginInfoVTO> pluginInfoList) throws BaseException {
        String ccosExactVersion = null;
        try {
            if (workStationId == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }

    @Override
    public List<String> getCompatibleClientVerList() {
        List<String> verCodeList = new ArrayList<String>();
        String[] verCode =
                ConfigurationFileHandler.getInstance().getProperty("ccos-version").toString().split(",");
        for (String verCodeS : verCode) {
            verCodeList.add(verCodeS);
        }
        return verCodeList;
    }
}
