package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.ReportRequestService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;

import org.json.JSONObject;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportRequestDelegator implements Delegator {

    private ReportRequestService getService(UserProfileTO userProfileTO) throws BaseException {
        ReportRequestService reportRequestService;
        try {
            reportRequestService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_REPORT_REQUEST), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RRD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_REPORT_REQUEST.split(","));
        }
        reportRequestService.setUserProfileTO(userProfileTO);
        return reportRequestService;
    }

    public Long save(UserProfileTO userProfileTO, ReportRequestVTO reportRequestVTO) throws BaseException {
        return getService(userProfileTO).save(reportRequestVTO);
    }

    public void remove(UserProfileTO userProfileTO, Long[] reportRequestIds) throws BaseException {
        getService(userProfileTO).remove(reportRequestIds);
    }


    public ReportRequestTO loadGeneratedReport(UserProfileTO userProfileTO, Long reportRequestId) throws BaseException {
        return getService(userProfileTO).loadGeneratedReport(reportRequestId);
    }

    /**
     * This method is used for teting the process of generating reports
     *
     * @param reportRequestTO
     * @throws BaseException
     */
    public void generateRequestedReportTest(ReportRequestTO reportRequestTO) throws BaseException {
        getService(null).generateRequestedReportTest(reportRequestTO);
    }

    public Long getRequestedReportByJobCount() throws BaseException {
        return getService(null).getRequestedReportByJobCount();
    }

    public void generateRequestedReportByJob(Integer from, Integer to) throws BaseException {
        getService(null).generateRequestedReportsByJob(from, to);
    }

    public void validateReport(UserProfileTO userProfileTO,
                               Long reportId,
                               SystemId systemId,
                               JSONObject receivedParam) throws BaseException {
        getService(userProfileTO).validateReport(reportId, userProfileTO.getUserName(), systemId, receivedParam);
    }
}
