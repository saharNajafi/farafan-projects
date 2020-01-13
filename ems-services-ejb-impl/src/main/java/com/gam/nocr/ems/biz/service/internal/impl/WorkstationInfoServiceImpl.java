package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
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
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
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

    public final static String WORKSTATION_INFO_PERIOD_DEFAULT_VALUE = "40";//AS DAY
    private static final Logger workstationInfoLogger = BaseLog.getLogger("WorkstationInfoLogger");

    public WorkstationInfoDAO getWorkstationInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_WORKSTATIONINFO));
        } catch (DAOFactoryException e) {
            workstationInfoLogger.error(BizExceptionCode.WSTI_006 + " : " + BizExceptionCode.GLB_001_MSG, e);
            throw new DelegatorException(
                    BizExceptionCode.WSTI_006,
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
                    BizExceptionCode.WSTI_007,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_WORKSTATION});
        }
    }

    @Override
    public boolean workstationInfoRequired(String workstationCode) throws BaseException {
        WorkstationInfoTO workstationInfoTO = null;
        boolean result = false;
        try {
            if (workstationCode == null) {
                throw new ServiceException(BizExceptionCode.WSTI_008, BizExceptionCode.WSTI_008_MSG);
            }
            if (workstationCode.length() < 40) {
                throw new ServiceException(BizExceptionCode.WSTI_009, BizExceptionCode.WSTI_009_MSG);
            }
            if (workstationCode.length() > 40) {
                throw new ServiceException(BizExceptionCode.WSTI_010, BizExceptionCode.WSTI_010_MSG);
            }
            WorkstationTO workstation = getWorkstationDAO().findByActivationCode(workstationCode);
            if (workstation == null) {
                throw new ServiceException(BizExceptionCode.WSTI_011, BizExceptionCode.WSTI_011_MSG);
            }
            workstationInfoTO = getWorkstationInfoDAO().findByWorkstationId(workstation.getId());

            //1-
            if (workstationInfoTO == null) {
                return true;
            }
            //2-
            if (workstationInfoTO != null && workstationInfoTO.getGatherState()) {
                return true;
            }
            //3-
            if (workstationInfoTO.getLastModifiedDate() == null) {
                return true;
            }
            Integer checkPeriod = Integer.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_WORKSTATION_INFO_CHECK_PERIOD,
                    WORKSTATION_INFO_PERIOD_DEFAULT_VALUE));
            Date lastModifiedDatePlusCheckPeriod = EmsUtil.getDateAtMidnight(EmsUtil.differDay(workstationInfoTO.getLastModifiedDate(), checkPeriod));
            if (new Date().compareTo(lastModifiedDatePlusCheckPeriod) > 0)
                return true;

        } catch (BaseException e) {
            throw new ServiceException(
                    BizExceptionCode.WSTI_005,
                    BizExceptionCode.WSTI_005_MSG,
                    e);
        }
        return false;
    }

    @Override
    public void registerWorkstationInfo(
            String workstationCode, WorkstationInfoTO newWorkstationInfoTO) throws BaseException {
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
                    getWorkstationInfoDAO().findByWorkstationId(workstation.getId());
            if (oldWorkstationInfo != null) {
                updateWorkstationInfo(newWorkstationInfoTO, oldWorkstationInfo);
            } else if (workstation != null) {
                newWorkstationInfoTO.setWorkstation(workstation);
                getWorkstationInfoDAO().create(newWorkstationInfoTO);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(
                    BizExceptionCode.WSTI_003,
                    BizExceptionCode.WSTI_003_MSG,
                    e);
        }
    }

    private void updateWorkstationInfo(WorkstationInfoTO newWorkstationInfoTo, WorkstationInfoTO oldWorkstationInfo) throws BaseException {
        try {

            oldWorkstationInfo.setCpuInfo(newWorkstationInfoTo.getCpuInfo());
            oldWorkstationInfo.setOsVersion(newWorkstationInfoTo.getOsVersion());
            oldWorkstationInfo.setHasDotnetFramwork45(newWorkstationInfoTo.getHasDotnetFramwork45());
            oldWorkstationInfo.setMemoryCapacity(newWorkstationInfoTo.getMemoryCapacity());
            oldWorkstationInfo.setCcosVersion(newWorkstationInfoTo.getCcosVersion());
            oldWorkstationInfo.setIpAddress(String.valueOf(newWorkstationInfoTo.getIpAddress()));
            oldWorkstationInfo.setUsername(newWorkstationInfoTo.getUsername());
            oldWorkstationInfo.setAdditionalInfoAsJson(newWorkstationInfoTo.getAdditionalInfoAsJson());
            oldWorkstationInfo.setGatherState(newWorkstationInfoTo.getGatherState());//Always this field be reset!
            oldWorkstationInfo.setLastModifiedDate(newWorkstationInfoTo.getLastModifiedDate());//Always this field be reset!

            getWorkstationInfoDAO().update(oldWorkstationInfo);
        } catch (BaseException e) {
            throw new ServiceException(
                    BizExceptionCode.WSTI_002,
                    BizExceptionCode.WSTI_002_MSG,
                    e);
        }
    }

    @Override
    public List<String> getCompatibleClientVerList() throws BaseException {
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
            throw new ServiceException(
                    BizExceptionCode.WSTI_001,
                    BizExceptionCode.WSTI_001_MSG,
                    e);
        }
        return verCodeList;
    }
}