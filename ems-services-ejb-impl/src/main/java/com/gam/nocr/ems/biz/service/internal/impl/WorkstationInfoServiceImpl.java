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
    public String getReliableVerByPlatform(
            String workstationCode, WorkstationInfoTO workstationInfoTO) throws BaseException {
        String ccosExactVersion = null;
        WorkstationTO workstation;
        WorkstationInfoTO workstationInfo = null;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
             workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            if(workstation != null)
             workstationInfo =
                    getWorkstationInfoDAO().isReliableVerInquiryRequired(workstation.getId());
            if (workstationInfo != null) {
                updateWorkstationInfo(workstationInfoTO, workstationInfo);
            } else if (workstation != null) {
                workstationInfoTO.setWorkstation(workstation);
                getWorkstationInfoDAO().create(workstationInfoTO);
            }
            ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }

    private void updateWorkstationInfo(WorkstationInfoTO workstationInfoTo
            , WorkstationInfoTO workstationInfo) throws BaseException {
        try {
            workstationInfo.setMacAddressList(String.valueOf(workstationInfoTo.getMacAddressList()));
            workstationInfo.setCpuType(workstationInfoTo.getCpuType());
            workstationInfo.setRamCapacity(workstationInfoTo.getRamCapacity());
            workstationInfo.setOsVersion(workstationInfoTo.getOsVersion());
            if(workstationInfoTo.getHasDotnetFramwork45() != null)
            workstationInfo.setHasDotnetFramwork45(workstationInfoTo.getHasDotnetFramwork45());
            if(workstationInfoTo.getIs64bitOs()!=null)
            workstationInfo.setIs64bitOs(workstationInfoTo.getIs64bitOs());
            workstationInfo.setIpAddressList(String.valueOf(workstationInfoTo.getIpAddressList()));
            workstationInfo.setComputerName(workstationInfoTo.getComputerName());
            workstationInfo.setUsername(workstationInfoTo.getUsername());
            workstationInfo.setGateway(workstationInfoTo.getGateway());
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
        if(verCode.length > 0) {
            for (String verCodeS : verCode) {
                verCodeList.add(verCodeS);
            }
        }
        return verCodeList;
    }
}
