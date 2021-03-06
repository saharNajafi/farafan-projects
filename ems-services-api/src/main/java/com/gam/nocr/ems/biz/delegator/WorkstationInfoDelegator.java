package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.WorkstationInfoService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public class WorkstationInfoDelegator implements Delegator {
    private WorkstationInfoService getService(UserProfileTO userProfileTO) throws BaseException {
        WorkstationInfoService workstationInfoService = null;
        try {
            workstationInfoService =
                    (WorkstationInfoService) ServiceFactoryProvider.getServiceFactory().getService(
                            EMSLogicalNames.getServiceJNDIName(
                                    EMSLogicalNames.SRV_WORKSTATIONINFO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.WDL_001, BizExceptionCode.GLB_002_MSG,
                    e, EMSLogicalNames.SRV_WORKSTATIONINFO.split(","));
        }
        workstationInfoService.setUserProfileTO(userProfileTO);
        return workstationInfoService;
    }

    public Boolean workstationInfoRequired(
            UserProfileTO userProfileTO, String workstationCode) throws BaseException{
           return getService(userProfileTO).workstationInfoRequired(workstationCode);
    }

    public void registerWorkstationInfo(
            UserProfileTO userProfileTO, String workstationCode, WorkstationInfoTO workstationInfoTO)
            throws BaseException {
        try {
            getService(userProfileTO).registerWorkstationInfo(
                    workstationCode, workstationInfoTO);
        } catch (BaseException e) {
            throw e;
        }
        return;
    }

//    public List<String> getCompatibleClientVerList(UserProfileTO userProfileTO) throws BaseException {
//        List<String> verCode = null;
//        try {
//            verCode = getService(userProfileTO).getCompatibleClientVerList();
//        } catch (BaseException e) {
//            e.printStackTrace();
//        }
//        return verCode;
//    }
}
