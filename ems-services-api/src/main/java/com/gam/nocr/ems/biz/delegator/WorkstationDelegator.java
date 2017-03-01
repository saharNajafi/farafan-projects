package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.WorkstationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WorkstationDelegator implements Delegator {

    private WorkstationService getService(UserProfileTO userProfileTO) throws BaseException {
        WorkstationService workstationService = null;
        try {
            workstationService = (WorkstationService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_WORKSTATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.WDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_WORKSTATION.split(","));
        }
        workstationService.setUserProfileTO(userProfileTO);
        return workstationService;
    }

    public long save(UserProfileTO userProfileTO, WorkstationTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public WorkstationTO load(UserProfileTO userProfileTO, long workstationId) throws BaseException {
        return getService(userProfileTO).load(workstationId);
    }

    public boolean remove(UserProfileTO userProfileTO, String workstationIds) throws BaseException {
        return getService(userProfileTO).remove(workstationIds);
    }

    public WorkstationTO find(UserProfileTO userProfileTO, String workstationActivationCode) throws BaseException {
        return getService(userProfileTO).find(workstationActivationCode);
    }

    public void approveWorkstation(UserProfileTO userProfileTO, String workstationIds) throws BaseException {
        getService(userProfileTO).approveWorkstation(workstationIds);
    }

    public void rejectWorkstation(UserProfileTO userProfileTO, String workstationIds) throws BaseException {
        getService(userProfileTO).rejectWorkstation(workstationIds);
    }

    public WorkstationTO findByDepartmentIdAndActivationCode(UserProfileTO userProfileTO,
                                                             Long departmentId,
                                                             String workstationActivationCode) throws BaseException {
        return getService(userProfileTO).findByDepartmentIdAndActivationCode(departmentId, workstationActivationCode);
    }


}