package com.gam.nocr.ems.biz.service.external.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiInquiryWTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;

public interface BpiInquiryService extends Service {

    BpiInquiryWTO bpiInquiry(RegistrationPaymentTO registrationPaymentTO) throws BaseException;
}
