package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.biz.service.ServiceException;

public interface PurgeService extends Service {

	void doPurgeNextCycle() throws Exception;
}
