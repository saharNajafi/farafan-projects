package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.MessageTO;

public interface MessageDestinationDAO extends EmsBaseDAO<MessageDestinationTO> {

	MessageDestinationTO create(MessageTO message,
			MessageDestinationTO messagesDestinationsTO) throws BaseException;

	List<MessageDestinationTO> findByMessageId(Long parseLong)
			throws BaseException;

}
