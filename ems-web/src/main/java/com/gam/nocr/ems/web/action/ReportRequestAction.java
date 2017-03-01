package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.ReportRequestDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.security.BusinessSecurityException;

import java.io.*;
import java.util.List;

/**
 * Report request interactions are handled by this class
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportRequestAction extends ListControllerImpl<ReportRequestVTO> {

    /**
     * Identifier of the report that would be used by different operations
     */
    private String reportId;

    /**
     * Identifier of the report request used by different operations
     */
    private String reportRequestId;

    /**
     * Report output would be serialized to the client using this property (its that manner in which struts 2 sends
     * files)
     */
    private InputStream inputStream;

    /**
     * Content type of report result which would be used by struts stream result as a content type of http response
     */
    private String contentType;

    /**
     * This would be used in content disposition http header to specify a name for report result file while downloading
     */
    private String resultFileName;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResultFileName() {
        return resultFileName;
    }

    public void setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportRequestId() {
        return reportRequestId;
    }

    public void setReportRequestId(String reportRequestId) {
        this.reportRequestId = reportRequestId;
    }

    @Override
    public void setRecords(List<ReportRequestVTO> records) {
        this.records = records;
    }

    /**
     * Saves a new report request
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            ReportRequestVTO reportRequestVTO = records.get(0);
            ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();
            reportRequestDelegator.save(getUserProfile(), reportRequestVTO);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RRA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes a report request information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String remove() throws BaseException {
        try {

            if (!EmsUtil.checkString(ids)) {
                throw new ServiceException(BizExceptionCode.RRS_018, BizExceptionCode.RRS_018_MSG);
            }

            String[] idList = ids.split(",");
            Long[] idListInLong = new Long[idList.length];
            for (int i = 0; i < idList.length; i++) {
                idListInLong[i] = Long.parseLong(idList[i]);
            }

            ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();
            reportRequestDelegator.remove(getUserProfile(), idListInLong);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RRA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * When a 3S user tries to download the result file of a report request, a call to this method will be made by
     * passing the identifier of report request as 'reportRequestId'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String loadGeneratedReport() throws BaseException {
        ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();
        try {
            ReportRequestTO reportRequestTO = reportRequestDelegator.loadGeneratedReport(getUserProfile(), Long.parseLong(reportRequestId));

            setContentType(ReportOutputType.getContentType(reportRequestTO.getType()));

            setResultFileName("Report" + "-" + reportRequestTO.getId() + "." + reportRequestTO.getType().toString().toLowerCase());

            ByteArrayInputStream bis = new ByteArrayInputStream(reportRequestTO.getResult());
            setInputStream(bis);

            return "success";
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RRA_005, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
