package com.gam.nocr.ems.web.action;


import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.nocr.ems.biz.delegator.StateProviderDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.List;

/**
 * The state provider service that would be used by ExtJS StateProvider custom implementation. The custom implementation
 * tries to save and load state of user interface component from this service (instead of default implementation which
 * uses cookies). For more information, take a look at ExtJS documentation about StateProvider
 *
 * @author Sayyed Maysam Tayyeb
 *         Date: 2/21/12
 *         Time: 3:52 PM
 */
public class StateProviderController extends com.gam.commons.stateprovider.StateProviderController {

    private static final Logger logger = BaseLog.getLogger(StateProviderController.class);

    /**
     * Returns the value corresponding to a stateId specified as 'filter' parameter
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String get() throws BaseException {
        if (this.filter == null) {
            return SUCCESS_RESULT;
        }

        String moduleName = (String) this.filter.get("moduleName");
        if (moduleName == null || moduleName.trim().length() == 0) {
            return FAILURE_RESULT;
        }

        List<String> stateIds = (List<String>) this.filter.get("stateIds");
        StateProviderDelegator dlg = new StateProviderDelegator();
        try {
            this.records = dlg.getState(getUserProfile(), moduleName, stateIds);
        } catch (BusinessSecurityException ex) {
            throw new ActionException(WebExceptionCode.SPA_001, WebExceptionCode.GLB_001_MSG, ex);
        }

        return SUCCESS_RESULT;
    }

    /**
     * Persists the given value in database to be used in next calls on 'get' method of this action. As an example, when
     * a user changes the column order in card request grid, an automatic call by grid component would be made to this
     * method in order to save the ordering of columns in current user's profile in order to display the same grid in
     * same state in next visits
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            if (this.records == null) {
                return SUCCESS_RESULT;
            }
            String moduleName = "general";
            StateProviderDelegator dlg = new StateProviderDelegator();
            try {
                dlg.setState(getUserProfile(), moduleName, this.records);
            } catch (BusinessSecurityException bex) {
                throw new ActionException(WebExceptionCode.SPA_002, WebExceptionCode.GLB_001_MSG, bex);
            }

            return SUCCESS_RESULT;
        } catch (Throwable t) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, t);
            return FAILURE_RESULT;
        }
    }
}
