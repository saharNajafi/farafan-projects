package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.ws.MessageWTO;

public interface MessageService extends Service {

	List<MessageDestinationTO> getMessageByCriteriagetMessageByCriteria(UserProfileTO userProfileTO, MessageWTO wto) throws BaseException;

	List<Long> fetchReadyToProcessMessage() throws BaseException;

	void processMessage(Long id) throws BaseException;

	MessageDestinationTO getMessageDestinationById(Long msgId, Long personId) throws BaseException;

	void deleteMessageDestinationByPesonId(Long msgId, Long personId) throws BaseException;



}