package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.OfficeCapacityDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/28/18.
 */
public class OfficeCapacityAction extends ListControllerImpl<OfficeCapacityTO> {

    @Override
    public void setRecords(List<OfficeCapacityTO> records) {
        this.records = records;
    }

    public String save() throws BaseException {
        try {
            OfficeCapacityDelegator officeCapacityDelegator = new OfficeCapacityDelegator();

                for (OfficeCapacityTO to : records) {
                    if (to.getId() == null)
                        officeCapacityDelegator.save(getUserProfile(), to);
                    else
                        officeCapacityDelegator.update(getUserProfile(), to);
                }
                return SUCCESS_RESULT;
            } catch (BusinessSecurityException e) {
                throw new ActionException(WebExceptionCode.RIA_001, WebExceptionCode.GLB_001_MSG, e);
            }
    }

    public String load() throws BaseException {
        OfficeCapacityDelegator officeCapacityDelegator = new OfficeCapacityDelegator();
        OfficeCapacityTO officeCapacityTO = null;
        try {
            if (ids != null)
                officeCapacityTO = officeCapacityDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.OFC_001, WebExceptionCode.OFC_001_MSG);

            List<OfficeCapacityTO> officeCapacityTOs = new ArrayList<OfficeCapacityTO>();
            officeCapacityTOs.add(officeCapacityTO);
            setRecords(officeCapacityTOs);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RIA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public String fetch() throws BaseException {
        OfficeCapacityDelegator officeCapacityDelegator = new OfficeCapacityDelegator();
        List<OfficeCapacityTO> officeCapacityList = null;
        try {
            if (ids != null)
                officeCapacityList = officeCapacityDelegator.fetchOfficeCapacityList(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.OFC_002, WebExceptionCode.OFC_002_MSG);

            setRecords(officeCapacityList);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.RIA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
