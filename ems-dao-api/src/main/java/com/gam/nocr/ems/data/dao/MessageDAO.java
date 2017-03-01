package com.gam.nocr.ems.data.dao;

import java.sql.Timestamp;
import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.MessageTO;

public interface MessageDAO extends EmsBaseDAO<MessageTO> {

	Long getCountNewMessages(long personID) throws BaseException;

	List<Long> fetchReadyToProcessMessage() throws BaseException;

	List<MessageDestinationTO> getMessageForAll(Long personId , Timestamp msgDate , int pageSize , int pageNo);

	List<MessageDestinationTO> getMessageByProvinceId(Long personId, Long provinceId, Timestamp msgDate, int pageSize , int pageNo);

	List<MessageDestinationTO> getMessageByOfficeId(Long personId, Long officeId, Timestamp msgDate, int pageSize , int pageNo);

	List<MessageDestinationTO> getMessageByPersonId(Long personId, Timestamp msgDate, int pageSize , int pageNo);

}
