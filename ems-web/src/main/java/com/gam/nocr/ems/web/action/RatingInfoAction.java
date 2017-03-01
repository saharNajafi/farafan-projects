package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.RatingDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Managing AJAX requests for manipulating rating information is implemented in this class
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class RatingInfoAction extends ListControllerImpl<RatingInfoTO> {
    @Override
    public void setRecords(List<RatingInfoTO> records) {
        this.records = records;
    }

    /**
     * Saves a rating category
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            RatingDelegator ratingDelegator = new RatingDelegator();
            for (RatingInfoTO to : records) {
                if (to.getId() == null)
                    ratingDelegator.save(getUserProfile(), to);
                else
                    ratingDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RIA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Loads a rating information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            RatingDelegator ratingDelegator = new RatingDelegator();
            RatingInfoTO ratingInfoTO = null;

            if (ids != null)
                ratingInfoTO = ratingDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.RIA_004, WebExceptionCode.RIA_004_MSG);

            List<RatingInfoTO> ratingInfoTOs = new ArrayList<RatingInfoTO>();
            ratingInfoTOs.add(ratingInfoTO);
            setRecords(ratingInfoTOs);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RIA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes a rating information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            RatingDelegator ratingDelegator = new RatingDelegator();

            ratingDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RIA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
