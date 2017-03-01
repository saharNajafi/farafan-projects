package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.BusinessLogDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Manages AJAX requests for business log grid
 *
 * @author Sina Golesorkhi
 */
public class BusinessLogAction extends ListControllerImpl<BusinessLogTO> {

    /**
     * The result of verifying a business log signature will be stored in this property to be serialized to the caller
     * in response
     */
    private boolean verified;

    @Override
    public void setRecords(List<BusinessLogTO> records) {
        this.records = records;
    }

    /**
     * Verifies the signature of requested business log record (specified as 'ids' property) and stores the result in
     * verified property to be serialized to the caller in response
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String checkBusinessLogStatus() throws BaseException {
        BusinessLogDelegator businessLogDelegator = new BusinessLogDelegator();
        try {
            if (businessLogDelegator.verify(getUserProfile(), ids)) {
                setVerified(true);
            } else {
                setVerified(false);
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.BGA_001, WebExceptionCode.GLB_001_MSG, e);
        }
        return SUCCESS_RESULT;
    }

    /**
     * @return the verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
