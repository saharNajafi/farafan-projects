package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/11/18.
 */
public interface RegistrationPaymentDAO extends EmsBaseDAO<RegistrationPaymentTO> {

    RegistrationPaymentTO findByCitizenId(Long id) throws BaseException;

    RegistrationPaymentTO findLastCardRequestPaymentByNationalId(String nationalId) throws DataException;

    String nextValueOfRegistrationPaymentCode() throws BaseException;
}
