package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.DocTypeDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.DocTypeVTO;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all AJAX requests of document type management
 *
 * @author Haeri (haeri@gamelectronics.com)
 */
public class DocTypeAction extends ListControllerImpl<DocTypeVTO> {

    static final Logger logger = BaseLog.getLogger(DocTypeAction.class);

    @Override
    public void setRecords(List<DocTypeVTO> records) {
        this.records = records;
    }

    /**
     * Saves a document type information
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            DocTypeDelegator docTypeDelegator = new DocTypeDelegator();
            for (DocTypeVTO to : records) {
                if (to.getId() == null)
                    docTypeDelegator.save(getUserProfile(), to);
                else
                    docTypeDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DTA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Loads a document type information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            DocTypeDelegator docTypeDelegator = new DocTypeDelegator();
            DocTypeVTO docTypeVTO = null;

            if (ids != null)
                docTypeVTO = docTypeDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.DTA_002, WebExceptionCode.DTA_002_MSG);

            List<DocTypeVTO> docTypeVTOs = new ArrayList<DocTypeVTO>();
            docTypeVTOs.add(docTypeVTO);
            setRecords(docTypeVTOs);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DTA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes a document type information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            DocTypeDelegator docTypeDelegator = new DocTypeDelegator();

            docTypeDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DTA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
