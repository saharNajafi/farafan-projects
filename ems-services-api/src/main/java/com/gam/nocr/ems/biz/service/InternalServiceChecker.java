package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface InternalServiceChecker extends Service {

    CardRequestTO inquiryHasCardRequest(String nationalId) throws BaseException;

    public boolean checkEnrollmentInProgress(CardRequestTO cardRequest) throws BaseException;

    void checkEditPersonalInformationEnabled(CardRequestTO cardRequestTO) throws BaseException;
}
