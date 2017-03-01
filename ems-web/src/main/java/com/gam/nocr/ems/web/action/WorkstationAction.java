package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.WorkstationDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Manipulating workstation information through 3S UI is backed by methods of this class
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WorkstationAction extends ListControllerImpl<WorkstationTO> {

    @Override
    public void setRecords(List<WorkstationTO> records) {
        this.records = records;
    }

    /**
     * Saves a workstation information
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        WorkstationDelegator workstationDelegator = new WorkstationDelegator();

        try {
            for (WorkstationTO to : records) {
                workstationDelegator.save(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.WSA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Loads a workstation information base on its identifier specified as 'ids'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            WorkstationDelegator workstationDelegator = new WorkstationDelegator();
            WorkstationTO workstation = null;

            if (ids != null && ids.trim().length() != 0)
                workstation = workstationDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.WSA_004, WebExceptionCode.WSA_004_MSG);

            List<WorkstationTO> workstationRecords = new ArrayList<WorkstationTO>();
            workstationRecords.add(workstation);
            setRecords(workstationRecords);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.WSA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes a workstation information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            WorkstationDelegator workstationDelegator = new WorkstationDelegator();

            workstationDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.WSA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * If a workstation information is registered by CCOS (so the record is in 'New' state), and a 3S administrator
     * accepts it in UI, a call to this method will change its state to 'APPROVED'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String approveWorkstation() throws BaseException {
        try {
            WorkstationDelegator workstationDelegator = new WorkstationDelegator();

            if (ids != null && ids.trim().length() != 0)
                workstationDelegator.approveWorkstation(getUserProfile(), ids);
            else
                throw new ActionException(WebExceptionCode.WSA_007, WebExceptionCode.WSA_004_MSG);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.WSA_005, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * If a workstation information is registered by CCOS (so the record is in 'New' state), and a 3S administrator
     * rejects it in UI, a call to this method will change its state to 'REJECTED'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String rejectWorkstation() throws BaseException {
        try {
            WorkstationDelegator workstationDelegator = new WorkstationDelegator();

            if (ids != null && ids.trim().length() != 0)
                workstationDelegator.rejectWorkstation(getUserProfile(), ids);
            else
                throw new ActionException(WebExceptionCode.WSA_008, WebExceptionCode.WSA_004_MSG);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.WSA_006, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}