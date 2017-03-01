package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.domain.OutgoingSMSTO;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface SmsDAO extends EmsBaseDAO<OutgoingSMSTO> {

	void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType) throws DAOException;
	
	List<Long> fetchMessagesId(Integer intValue, Integer fetchLimit) throws DAOException;

}
