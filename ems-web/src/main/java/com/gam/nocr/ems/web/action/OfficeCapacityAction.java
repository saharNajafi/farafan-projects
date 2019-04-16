package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.OfficeCapacityDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.OfficeCapacityVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/28/18.
 */
public class OfficeCapacityAction extends ListControllerImpl<OfficeCapacityVTO> {

    @Override
    public void setRecords(List<OfficeCapacityVTO> records) {
        this.records = records;
    }

    public String save() throws BaseException {
        try {
            OfficeCapacityDelegator officeCapacityDelegator = new OfficeCapacityDelegator();
            for (OfficeCapacityVTO to : records) {
                    officeCapacityDelegator.saveOrUpdate(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.OFC_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public String load() throws BaseException {
        OfficeCapacityDelegator officeCapacityDelegator = new OfficeCapacityDelegator();
        OfficeCapacityVTO officeCapacityVTO = null;
        try {
            if (ids != null)
                officeCapacityVTO = officeCapacityDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.OFC_001, WebExceptionCode.OFC_001_MSG);
            List<OfficeCapacityVTO> officeCapacityTOs = new ArrayList<OfficeCapacityVTO>();
            officeCapacityTOs.add(officeCapacityVTO);
            setRecords(officeCapacityTOs);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.OFC_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public String delete() throws BaseException {
        try {
            OfficeCapacityDelegator OfficeCapacityDelegator = new OfficeCapacityDelegator();

            OfficeCapacityDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DEA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
