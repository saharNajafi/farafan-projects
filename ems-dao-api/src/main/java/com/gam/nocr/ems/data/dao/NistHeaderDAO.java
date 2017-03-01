package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.NistHeaderTO;

public interface NistHeaderDAO extends EmsBaseDAO<NistHeaderTO> {

	NistHeaderTO findByRequestId(Long requestId) throws BaseException;
	
	
}
