package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.FeatureExtractIdsDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/5/18.
 */
public class FeatureExtractIdsAction extends ListControllerImpl<FeatureExtractIdsVTO> {

    private Long enrollmentOfficeId;

    @Override
    public void setRecords(List<FeatureExtractIdsVTO> records) {
        this.records = records;
    }

    public String load() throws BaseException {

        FeatureExtractIdsDelegator featureExtractIdsDelegator = new FeatureExtractIdsDelegator();

        List<FeatureExtractIdsVTO> featureExtractIdsVTOs  = null;
        try {
            if (enrollmentOfficeId != null)
                featureExtractIdsVTOs = featureExtractIdsDelegator.load(getUserProfile(), enrollmentOfficeId);
            else
                throw new ActionException(WebExceptionCode.FEI_001, WebExceptionCode.FEI_001_MSG);
            setRecords(featureExtractIdsVTOs);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.FEI_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }
}

