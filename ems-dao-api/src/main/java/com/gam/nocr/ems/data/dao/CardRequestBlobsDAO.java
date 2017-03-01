package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestBlobsTO;

public interface CardRequestBlobsDAO extends EmsBaseDAO<CardRequestBlobsTO> {
	
	CardRequestBlobsTO findByCardRequestId(Long cardRequestId)throws BaseException;
}
