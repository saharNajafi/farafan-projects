package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.CompleteRegistrationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.ArrayList;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class CompleteRegistrationDelegator implements Delegator {

    private CompleteRegistrationService getService(UserProfileTO userProfileTO) throws BaseException {
        CompleteRegistrationService completeRegistrationService;
        try {
            completeRegistrationService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_COMPLETE_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.MDL_001, BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_COMPLETE_REGISTRATION.split(","));
        }
        completeRegistrationService.setUserProfileTO(userProfileTO);
        return completeRegistrationService;
    }

    public void register(UserProfileTO userProfileTO,
                         CardRequestTO requestTO,
                         ArrayList<BiometricTO> fingers,
                         ArrayList<BiometricTO> faces,
                         ArrayList<DocumentTO> documents,
                         byte[] signature, String featureExtractorID) throws BaseException {
//        getService(userProfileTO).register(requestTO, fingers, faces, documents, signature);
        getService(userProfileTO).register(requestTO, fingers, faces, documents, signature, featureExtractorID);
    }

    public String requestArchiveId(UserProfileTO userProfileTO, Long cardRequestId, Long enrollmentOfficeId) throws BaseException {
        return getService(userProfileTO).requestArchiveId(cardRequestId, enrollmentOfficeId);
    }
}
