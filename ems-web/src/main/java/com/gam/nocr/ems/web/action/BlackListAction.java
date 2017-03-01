package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.BlackListDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.BlackListTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages AJAX requests for black list grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class BlackListAction extends ListControllerImpl<BlackListTO> {

    @Override
    public void setRecords(List<BlackListTO> records) {
        this.records = records;
    }

    /**
     * Saves a black listed person
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            BlackListDelegator blackListDelegator = new BlackListDelegator();
            for (BlackListTO to : records) {
                if (to.getId() == null)
                    blackListDelegator.save(getUserProfile(), to);
                else
                    blackListDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.BLA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Loads a black listed person information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            BlackListDelegator blackListDelegator = new BlackListDelegator();
            BlackListTO blackListTO = null;

            if (ids != null)
                blackListTO = blackListDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.BLA_002, WebExceptionCode.BLA_002_MSG);

            List<BlackListTO> blackListTOs = new ArrayList<BlackListTO>();
            blackListTOs.add(blackListTO);
            setRecords(blackListTOs);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.BLA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes a black listed person information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            BlackListDelegator blackListDelegator = new BlackListDelegator();

            blackListDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.BLA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
