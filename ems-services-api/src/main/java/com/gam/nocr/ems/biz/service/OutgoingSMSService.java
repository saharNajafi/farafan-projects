package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;


public interface OutgoingSMSService extends Service {

	List<Long> fetchMessagesId(Integer intValue, Integer fetchLimit) throws BaseException;

	void processSmsToSend(Long id) throws BaseException;
}
