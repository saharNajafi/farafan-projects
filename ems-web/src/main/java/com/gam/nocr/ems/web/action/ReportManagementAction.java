package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.ReportManagementDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.ReportVTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Managing report definition is implemented in this class
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportManagementAction extends ListControllerImpl<ReportVTO> {

    /**
     * Metadata of the report that would be used as the result of fetchReportMetaData method
     */
    private String metaData;

    /**
     * Identifier of the report that would be used by different operations
     */
    private String reportId;

    /**
     * The new state of the report would be sent using this property in order to enable/disable a report
     */
    private String newReportState;

    /**
     * Must be implemented in order to set records by json populator
     *
     * @param records
     */
    @Override
    public void setRecords(List<ReportVTO> records) {
        this.records = records;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getMetaData() {
        return metaData;
    }

    public String getNewReportState() {
        return newReportState;
    }

    public void setNewReportState(String newReportState) {
        this.newReportState = newReportState;
    }

    /**
     * Fetches the metadata of a report specified by 'ids' property
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String fetchReportMetaData() throws BaseException {
        ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();

        if (!EmsUtil.checkString(reportId)) {
            throw new ActionException(WebExceptionCode.RMA_015, WebExceptionCode.RMA_015_MSG);
        }

        try {
            metaData = reportManagementDelegator.getMetaData(getUserProfile(), Long.parseLong(reportId), null);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_010, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Saves a report
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();
        try {
            for (ReportVTO reportVTO : records) {
                reportManagementDelegator.save(getUserProfile(), reportVTO);
            }
            return SUCCESS_RESULT;
        } catch (BaseException e) {
            throw e;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_001, WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.RMA_002, WebExceptionCode.GLB_003_MSG, e);
        }
    }

    /**
     * Updates given report
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String update() throws BaseException {
        ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();
        try {
            for (ReportVTO record : records) {
                reportManagementDelegator.update(getUserProfile(), record);
            }
            return SUCCESS_RESULT;
        } catch (BaseException e) {
            throw e;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_008, WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.RMA_009, WebExceptionCode.GLB_003_MSG, e);
        }
    }

    /**
     * Changes the state of given report. It would be used to enable/disable a report
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String changeReportState() throws BaseException {
        ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();

        BooleanType newState = null;
        try {
            newState = BooleanType.valueOf(getNewReportState());
        } catch (IllegalArgumentException e) {
            throw new ActionException(WebExceptionCode.RMA_011, WebExceptionCode.RMA_011_MSG, e);
        }

        Long reportID = null;
        try {
            reportID = Long.valueOf(getReportId());
        } catch (IllegalArgumentException e) {
            throw new ActionException(WebExceptionCode.RMA_014, WebExceptionCode.RMA_014_MSG, e);
        }

        try {
            reportManagementDelegator.changeReportState(getUserProfile(), reportID, newState);
            return SUCCESS_RESULT;
        } catch (BaseException e) {
            throw e;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_012, WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.RMA_013, WebExceptionCode.GLB_003_MSG, e);
        }
    }

    /**
     *  Deletes a report information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String remove() throws BaseException {
        try {
            ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();

            reportManagementDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BaseException e) {
            throw e;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_003, WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.RMA_004, WebExceptionCode.GLB_003_MSG, e);
        }
    }

    /**
     *  Loads a report information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            ReportManagementDelegator reportManagementDelegator = new ReportManagementDelegator();
            ReportVTO reportVTO;
            if (ids != null) {
                reportVTO = reportManagementDelegator.load(getUserProfile(), Long.parseLong(ids));
            } else {
                throw new ActionException(WebExceptionCode.RMA_005, WebExceptionCode.RMA_005_MSG);
            }
            List<ReportVTO> reportVTOs = new ArrayList<ReportVTO>();
            reportVTOs.add(reportVTO);
            setRecords(reportVTOs);
            return SUCCESS_RESULT;
        } catch (BaseException e) {
            throw e;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RMA_006, WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.RMA_007, WebExceptionCode.GLB_003_MSG, e);
        }
    }
}
