package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.UserDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.List;

/**
 * Manages the user related operations (like changePassword, fetch user details, etc.). These methods would be called
 * mainly by the 3S main menu
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class UserAction extends ListControllerImpl<UserVTO> {

    private static final Logger logger = BaseLog.getLogger(UserAction.class);

    /**
     * User's firsName + sureName will be stored in this property to be returned to the client and be displayed on UI
     */
    private String userInfo;

    private UserDelegator userDelegator;

    public UserAction() {
        userDelegator = new UserDelegator();
    }

    @Override
    public void setRecords(List<UserVTO> records) {
        this.records = records;
    }

    /**
     * Changes the user password by calling a service on GAAS
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String changePassword() throws BaseException {
        try {
            for (UserVTO to : records) {
                userDelegator.changePassword(getUserProfile(), to);
            }

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.USA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Fetches current user's firstName and sureName in order to be displayed on 3S main menu
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String fetchUserInfo() throws BaseException {
        try {
            setUserInfo(userDelegator.fetchUserInfo(getUserProfile()));
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.USA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Called by 3S UI in order to invalidate current user's session and logs him/her out of system
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String logout() throws BaseException {
        try {
            request.getSession().invalidate();
            return SUCCESS_RESULT;
        } catch (Exception e) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
            return FAILURE_RESULT;
        }
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
