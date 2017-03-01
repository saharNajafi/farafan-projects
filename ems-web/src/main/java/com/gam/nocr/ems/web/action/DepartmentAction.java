package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.DepartmentDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.DepartmentVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Handles AJAX requests about departments
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DepartmentAction extends ListControllerImpl<DepartmentVTO> {

    @Override
    public void setRecords(List<DepartmentVTO> records) {
        this.records = records;
    }

    /**
     * Saves a department in database
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            DepartmentDelegator departmentDelegator = new DepartmentDelegator();
            for (DepartmentVTO vto : records) {
                if (vto.getId() == null)
                    departmentDelegator.save(getUserProfile(), vto);
                else
                    departmentDelegator.update(getUserProfile(), vto);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DEA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Deletes a department information base on its identifier specified as 'ids'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            DepartmentDelegator departmentDelegator = new DepartmentDelegator();

            departmentDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DEA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
