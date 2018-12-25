package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.DocumentTO;

import java.util.ArrayList;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public interface CompleteRegistrationService extends Service {

    public void register(CardRequestTO requestTO,
                         ArrayList<BiometricTO> fingers,
                         ArrayList<BiometricTO> faces,
                         ArrayList<DocumentTO> documents,
                         byte[] signature, String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException;

    public String requestArchiveId(Long cardRequestId, Long enrollmentOfficeId) throws BaseException;
}
