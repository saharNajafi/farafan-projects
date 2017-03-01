package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.core.web.struts2.extJsController.BaseController;
import org.slf4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Manages the logout operation called by GAAS logout UI
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class LogoutAction extends BaseController {
    private static final Logger logger = BaseLog.getLogger(LogoutAction.class);

    /**
     * Invalidates the user session to force him/her log out of the system
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String logout() throws BaseException {
        logger.info("Logout method invoked...");
        try {
            UserProfileTO userProfileTO = getUserProfile();
            if (userProfileTO != null)
                logger.info("Going to logout user:" + userProfileTO.getUserName());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                Enumeration<String> attributes = session.getAttributeNames();
                if (attributes != null)
                    while (attributes.hasMoreElements()) {
                        String attribute = attributes.nextElement();
                        if (attribute != null && attribute.trim().length() != 0)
                            session.removeAttribute(attribute);
                    }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            session.invalidate();
        }
        logger.info("Logout method executed successfully and session invalidated.");
        return "success";
    }
}
