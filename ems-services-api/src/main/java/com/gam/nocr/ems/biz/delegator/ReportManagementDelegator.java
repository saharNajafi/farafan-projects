package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.ReportManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportVTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportManagementDelegator implements Delegator {

    private ReportManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        ReportManagementService reportManagementService;
        try {
            reportManagementService = (ReportManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_REPORT_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RMD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_REPORT_MANAGEMENT.split(","));
        }
        reportManagementService.setUserProfileTO(userProfileTO);
        return reportManagementService;
    }

    public Long save(UserProfileTO userProfileTO,
                     ReportVTO reportVTO) throws BaseException {
        return getService(userProfileTO).save(reportVTO);
    }

    public void update(UserProfileTO userProfileTO,
                       ReportVTO reportVTO) throws BaseException {
        getService(userProfileTO).update(reportVTO);
    }

    public ReportVTO load(UserProfileTO userProfileTO,
                          Long reportId) throws BaseException {
        return getService(userProfileTO).load(reportId);
    }

    public boolean remove(UserProfileTO userProfileTO,
                          String reportIds) throws BaseException {
        return getService(userProfileTO).remove(reportIds);
    }

    /**
     * The method getMetaData is used to prepare the parameters, which belong to an appropriate report
     *
     * @param userProfileTO is an instance of type {@link UserProfileTO}
     * @param reportId      is an instance of type {@link Long}, which represents the id of a specified instance of type {@link ReportTO}
     * @return an instance of type {@link String}, which represents the sequence of parameters in a JSON String format
     * @throws BaseException
     */
    public String getMetaData(UserProfileTO userProfileTO,
                              Long reportId,
                              SystemId systemId) throws BaseException {
        return getService(userProfileTO).getMetaData(reportId, userProfileTO.getUserName(), systemId);
    }

    /**
     * Changes the state of given report. It would be used to enable/disable a report
     *
     * @param userProfileTO  User profile to be used for chacking its permission
     * @param reportId       Identifier of the record to change its state
     * @param newReportState New state of the report
     */
    public void changeReportState(UserProfileTO userProfileTO, Long reportId, BooleanType newReportState) throws BaseException {
        getService(userProfileTO).changeReportState(reportId, newReportState);
    }
}
