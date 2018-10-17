package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.FeatureExtractIdsDelegator;
import com.gam.nocr.ems.biz.delegator.OfficeSettingDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.vol.OfficeSettingVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/14/18.
 */
public class OfficeSettingAction extends ListControllerImpl<OfficeSettingVTO> {

    @Override
    public void setRecords(List<OfficeSettingVTO> records) {
        this.records = records;
    }

    public String update() throws BaseException {
        try {
            OfficeSettingDelegator officeSettingDelegator = new OfficeSettingDelegator();
            for (OfficeSettingVTO to : records) {
                if (to.getId() == null)
                    officeSettingDelegator.save(getUserProfile(), to);
                else
                    officeSettingDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.OST_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public String load() throws BaseException {
        OfficeSettingDelegator officeSettingDelegator = new OfficeSettingDelegator();
        OfficeSettingVTO officeSettingVTO = null;
        try {
            if (ids != null)
                officeSettingVTO = officeSettingDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.OST_002, WebExceptionCode.OST_002_MSG);
            List<OfficeSettingVTO> officeSettingVTOs = new ArrayList<OfficeSettingVTO>();
            officeSettingVTOs.add(officeSettingVTO);
            setRecords(officeSettingVTOs);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.OST_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
