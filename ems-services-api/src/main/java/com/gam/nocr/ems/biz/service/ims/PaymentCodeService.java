package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;

public interface PaymentCodeService extends Service {

    String fetchPaymentCode(Long officeId, String serviceCode ,String paymentAmount) throws BaseException;
}
