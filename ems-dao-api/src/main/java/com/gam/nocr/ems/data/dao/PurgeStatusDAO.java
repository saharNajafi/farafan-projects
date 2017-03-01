package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.domain.PurgeStatusTO;

public interface PurgeStatusDAO extends EmsBaseDAO<PurgeStatusTO>{

	
	PurgeStatusTO findLastPurgeStatus() throws DAOException;

	
}
