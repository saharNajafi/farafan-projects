package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;

public interface PreparedMessageDAO extends EmsBaseDAO<PreparedMessageTO> {
	
	PreparedMessageTO loadById(Long id) throws BaseException;

}
