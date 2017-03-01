package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.BusinessLogTO;

/**
 * @author Sina Golesorkhi
 */
public interface BusinessLogDAO extends EmsBaseDAO<BusinessLogTO> {

    public void insertLog(BusinessLogTO to) throws BaseException;

    public boolean verify(String id) throws BaseException;
}
