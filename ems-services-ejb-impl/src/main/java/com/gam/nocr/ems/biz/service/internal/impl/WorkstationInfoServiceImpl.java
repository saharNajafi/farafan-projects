package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ConfigurationFileHandler;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.WorkstationDAO;
import com.gam.nocr.ems.data.dao.WorkstationInfoDAO;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.util.EmsUtil;
import weblogic.jndi.internal.NameAlreadyUnboundException;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Date;
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
    public boolean isReliableVerInquiryRequired(String workstationCode) throws BaseException {
        WorkstationInfoTO workstationInfoTO = null;
        boolean result = false;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_002, BizExceptionCode.WST_002_MSG);
            if (workstationCode.length() < 40)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
            if (workstationCode.length() > 40)
                throw new ServiceException(BizExceptionCode.WST_004, BizExceptionCode.WST_004_MSG);
            WorkstationTO workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            if (workstation == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            workstationInfoTO = getWorkstationInfoDAO().isReliableVerInquiryRequired(workstation.getId());

            //1-
            if (workstationInfoTO == null)
                return true;
            //2-
            if (workstationInfoTO != null && workstationInfoTO.getGatherState() == 1)
                return true;
            //3-
            try {
                ProfileManager pm = ProfileHelper.getProfileManager();
                String checkPeriod = (String) pm.getProfile(ProfileKeyName.KEY_WORKSTATION_INFO_CHECK_PERIOD, true, null, null);
                if (workstationInfoTO.getLastModifiedDate() == null)
                    return true;
                Date lastModifiedDatePlusCheckPeriod = EmsUtil.getDateAtMidnight(EmsUtil.differDay(workstationInfoTO.getLastModifiedDate(), Integer.valueOf(checkPeriod)));
                if (new Date().compareTo(lastModifiedDatePlusCheckPeriod) > 0)
                    return true;
            } catch (ProfileException e) {
                e.printStackTrace();
            }

        } catch (BaseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getReliableVerByPlatform(
            String workstationCode, WorkstationInfoTO newWorkstationInfoTO) throws BaseException {
        String ccosExactVersion = null;
        WorkstationTO workstation;
        WorkstationInfoTO oldWorkstationInfo = null;
        try {
            if (workstationCode == null)
                throw new ServiceException(BizExceptionCode.WST_002, BizExceptionCode.WST_002_MSG);
            if (workstationCode.length() < 40)
                throw new ServiceException(BizExceptionCode.WST_003, BizExceptionCode.WST_003_MSG);
            if (workstationCode.length() > 40)
                throw new ServiceException(BizExceptionCode.WST_004, BizExceptionCode.WST_004_MSG);
            workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            if (workstation == null)
                throw new ServiceException(BizExceptionCode.WST_001, BizExceptionCode.WST_001_MSG);
            oldWorkstationInfo =
                    getWorkstationInfoDAO().isReliableVerInquiryRequired(workstation.getId());
            if (oldWorkstationInfo != null) {
                updateWorkstationInfo(newWorkstationInfoTO, oldWorkstationInfo);
                ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
            } else if (workstation != null) {
                newWorkstationInfoTO.setWorkstation(workstation);
                getWorkstationInfoDAO().create(newWorkstationInfoTO);
                ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, null));
            }
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return ccosExactVersion;
    }

    private void updateWorkstationInfo(WorkstationInfoTO newWorkstationInfoTo, WorkstationInfoTO oldWorkstationInfo) throws BaseException {
        try {
            //1-
            if (String.valueOf(newWorkstationInfoTo.getIpAddressList()) != null)
                oldWorkstationInfo.setIpAddressList(String.valueOf(newWorkstationInfoTo.getIpAddressList()));
            //2-
            if (newWorkstationInfoTo.getCpuType() != null)
                oldWorkstationInfo.setCpuType(newWorkstationInfoTo.getCpuType());
            //3-
            if (newWorkstationInfoTo.getOsVersion() != null)
                oldWorkstationInfo.setOsVersion(newWorkstationInfoTo.getOsVersion());
            //4-
            if (newWorkstationInfoTo.getUsername() != null)
                oldWorkstationInfo.setUsername(newWorkstationInfoTo.getUsername());
            //5-
            if (newWorkstationInfoTo.getHasDotnetFramwork45() != null)
                oldWorkstationInfo.setHasDotnetFramwork45(newWorkstationInfoTo.getHasDotnetFramwork45());
            //6-
            if (newWorkstationInfoTo.getIs64bitOs() != null)
                oldWorkstationInfo.setIs64bitOs(newWorkstationInfoTo.getIs64bitOs());
            //7-
            oldWorkstationInfo.setGatherState(newWorkstationInfoTo.getGatherState());//Always this field be reset!
            //8-
            oldWorkstationInfo.setLastModifiedDate(newWorkstationInfoTo.getLastModifiedDate());//Always this field be reset!
            //9-
            if (newWorkstationInfoTo.getDataAsJson() != null)
                oldWorkstationInfo.setDataAsJson(newWorkstationInfoTo.getDataAsJson());

            getWorkstationInfoDAO().update(oldWorkstationInfo);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCompatibleClientVerList() {
        List<String> verCodeList = new ArrayList<String>();
        try {
            String[] verCode =
                    ConfigurationFileHandler.getInstance().getProperty("ccos-version").toString().split(",");
            if (verCode.length > 0) {
                for (String verCodeS : verCode) {
                    verCodeList.add(verCodeS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCodeList;
    }
}