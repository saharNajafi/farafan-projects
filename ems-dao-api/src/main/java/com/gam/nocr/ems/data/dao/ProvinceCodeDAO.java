package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ProvinceCodeTO;

/**
 * @author Mazaher Namjoofar
 */
public interface ProvinceCodeDAO extends EmsBaseDAO<ProvinceCodeTO> {

    ProvinceCodeTO findByLocationId(Class clazz, Object locationId) throws BaseException;
}
