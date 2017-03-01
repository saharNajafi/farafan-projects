package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.SystemProfileDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.SystemProfileVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Managing system configuration list is handled by methods implemented in this class
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class SystemProfileAction extends ListControllerImpl<SystemProfileVTO> {

    @Override
    public void setRecords(List<SystemProfileVTO> records) {
        this.records = records;
    }

    /**
     * Called by client to update the value of a config item
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            SystemProfileDelegator systemProfileDelegator = new SystemProfileDelegator();
            for (SystemProfileVTO to : records) {
                systemProfileDelegator.save(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BaseException bex) {
            throw bex;
        } catch (BusinessSecurityException zex) {
            throw new ActionException(WebExceptionCode.SYA_001, WebExceptionCode.GLB_001_MSG, zex);
        } catch (Exception ex) {
            throw new ActionException(WebExceptionCode.SYA_002, WebExceptionCode.GLB_003_MSG, ex);
        }
    }

    /**
     * Removes a configuration from system
     *
     * @deprecated
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            SystemProfileDelegator systemProfileDelegator = new SystemProfileDelegator();

            systemProfileDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BaseException bex) {
            throw bex;
        } catch (BusinessSecurityException zex) {
            throw new ActionException(WebExceptionCode.SYA_003, WebExceptionCode.GLB_001_MSG, zex);
        } catch (Exception ex) {
            throw new ActionException(WebExceptionCode.SYA_004, WebExceptionCode.GLB_003_MSG, ex);
        }
    }

    /**
     * As configuration values are held in cache, in order to make sure change in a configuration value is applied,
     * a call to this method (through a dedicated action in configuration grid toolbar) causes a reload on
     * configuration values from database
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String reload() throws BaseException {
        try {
            SystemProfileDelegator systemProfileDelegator = new SystemProfileDelegator();

            systemProfileDelegator.reload(getUserProfile());

            return SUCCESS_RESULT;
        } catch (BaseException bex) {
            throw bex;
        } catch (BusinessSecurityException zex) {
            throw new ActionException(WebExceptionCode.SYA_005, WebExceptionCode.GLB_001_MSG, zex);
        } catch (Exception ex) {
            throw new ActionException(WebExceptionCode.SYA_006, WebExceptionCode.GLB_003_MSG, ex);
        }
    }
}
