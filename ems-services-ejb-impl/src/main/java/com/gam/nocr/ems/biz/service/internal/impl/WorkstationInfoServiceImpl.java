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
import com.gam.nocr.ems.data.dao.WorkstationDAO;
import com.gam.nocr.ems.data.dao.WorkstationInfoDAO;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.ws.ClientHardWareSpecWTO;
import com.gam.nocr.ems.data.domain.ws.ClientNetworkConfigsWTO;
import com.gam.nocr.ems.data.domain.ws.ClientSoftWareSpecWTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;
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
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_WORKSTATIONINFO));
        } catch (DAOFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WSI_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_WORKSTATIONINFO});
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
    public Boolean isReliableVerInquiryRequired(String workstationCode) throws BaseException {
        WorkstationInfoTO workstationInfoTO = null;
        Boolean result = false;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            WorkstationTO workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            workstationInfoTO = getWorkstationInfoDAO().isReliableVerInquiryRequired(workstation.getId());
                if(workstationInfoTO != null)
                    result = Boolean.valueOf(String.valueOf(workstationInfoTO.getGatherSate()));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getReliableVerByPlatform(String workstationCode, ClientHardWareSpecWTO clientHardWareSpec,
                                           ClientNetworkConfigsWTO clientNetworkConfig,
                                           ClientSoftWareSpecWTO clientSoftWareSpec) throws BaseException {
        String ccosExactVersion = null;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
            WorkstationTO workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            WorkstationInfoTO workstationInfo =
                    getWorkstationInfoDAO().isReliableVerInquiryRequired(workstation.getId());
            if (workstationInfo != null) {
                updateWorkstationInfo(clientHardWareSpec,
                        clientNetworkConfig, clientSoftWareSpec, workstationInfo);
            } else
                saveWorkstationInfo(workstationCode, clientHardWareSpec, clientNetworkConfig, clientSoftWareSpec);
            ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }

    private void saveWorkstationInfo(String workstationCode, ClientHardWareSpecWTO clientHardWareSpec,
                                     ClientNetworkConfigsWTO clientNetworkConfig,
                                     ClientSoftWareSpecWTO clientSoftWareSpec) throws BaseException {
        try {
            WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();
            WorkstationTO workstationTO = getWorkstationDAO().findByActivationCode(workstationCode);
//            todo
//            if(workstationTO == null)
//                throw new Exception();
            if (workstationTO != null) {
                workstationInfoTO.setWorkstation(workstationTO);
                workstationInfoTO.setMacAddressList(String.valueOf(clientHardWareSpec.getMacAddressList()));
                workstationInfoTO.setCpuType(clientHardWareSpec.getCpuType());
                workstationInfoTO.setRamCapacity(clientHardWareSpec.getRamCapacity());
                workstationInfoTO.setOsVersion(clientSoftWareSpec.getOsVersion());
                if(clientSoftWareSpec.getDotNetwork45Installed() != null)
                    workstationInfoTO.setHasDotnetFramwork45(Short.parseShort(
                            String.valueOf(((clientSoftWareSpec.getDotNetwork45Installed()) ? 1 : 0))));
                if(clientSoftWareSpec.getIs64BitOs()!=null)
                workstationInfoTO.setIs64bitOs(Short.parseShort(
                        String.valueOf(((clientSoftWareSpec.getIs64BitOs())? 1 : 0))));
                workstationInfoTO.setIpAddressList(String.valueOf(clientNetworkConfig.getIpAddressList()));
                workstationInfoTO.setComputerName(clientNetworkConfig.getComputerName());
                workstationInfoTO.setUsername(clientNetworkConfig.getUserName());
                workstationInfoTO.setGateway(clientNetworkConfig.getGateway());
                workstationInfoTO.setGatherSate((short) 0);
                getWorkstationInfoDAO().create(workstationInfoTO);
            }
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    private void updateWorkstationInfo(
            ClientHardWareSpecWTO clientHardWareSpec,
            ClientNetworkConfigsWTO clientNetworkConfig,
            ClientSoftWareSpecWTO clientSoftWareSpec, WorkstationInfoTO workstationInfo) throws BaseException {
        try {
            workstationInfo.setMacAddressList(String.valueOf(clientHardWareSpec.getMacAddressList()));
            workstationInfo.setCpuType(clientHardWareSpec.getCpuType());
            workstationInfo.setRamCapacity(clientHardWareSpec.getRamCapacity());
            workstationInfo.setOsVersion(clientSoftWareSpec.getOsVersion());
            if(clientSoftWareSpec.getDotNetwork45Installed() != null)
            workstationInfo.setHasDotnetFramwork45(Short.parseShort(
                    String.valueOf(((clientSoftWareSpec.getDotNetwork45Installed()) ? 1 : 0))));
            if(clientSoftWareSpec.getIs64BitOs()!=null)
            workstationInfo.setIs64bitOs(Short.parseShort(
                    String.valueOf(((clientSoftWareSpec.getIs64BitOs()) ? 1 : 0))));
            workstationInfo.setIpAddressList(String.valueOf(clientNetworkConfig.getIpAddressList()));
            workstationInfo.setComputerName(clientNetworkConfig.getComputerName());
            workstationInfo.setUsername(clientNetworkConfig.getUserName());
            workstationInfo.setGateway(clientNetworkConfig.getGateway());
            getWorkstationInfoDAO().update(workstationInfo);
        } catch (BaseException e) {
            e.printStackTrace();
        }
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
