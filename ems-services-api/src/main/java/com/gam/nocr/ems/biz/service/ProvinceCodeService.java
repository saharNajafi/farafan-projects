package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.ProvinceCodeTO;

/**
 * @author Mazaher Namjoofar
 */

public interface ProvinceCodeService extends Service {

    ProvinceCodeTO findByLocationId(Object locationId) throws BaseException;

    ProvinceCodeTO findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;

}
