package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.OfficeSettingDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/14/18.
 */
public class OfficeSettingAction extends ListControllerImpl<FeatureExtractIdsVTO> {

    private String enrollmentId;

    @Override
    public void setRecords(List<FeatureExtractIdsVTO> records) {
        this.records = records;
    }

    public String save() throws BaseException {
        try {
            OfficeSettingDelegator officeSettingDelegator = new OfficeSettingDelegator();
            for (FeatureExtractIdsVTO to : records) {
                if (to.getId() == null)
                    officeSettingDelegator.save(getUserProfile(), to);
                else
                    officeSettingDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.OSA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

}
