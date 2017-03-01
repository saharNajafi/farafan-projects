package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.dao.EmsBaseDAO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.enums.OfficeSettingType;

public interface OfficeSettingDAO extends EmsBaseDAO<OfficeSettingTO>{

	/**
	 * @author asus-pc: Madanipour
	 * @param depID
	 * @param uploadPhoto
	 * @throws DAOException 
	 * @throws BaseException 
	 */
	OfficeSettingTO findByOfficeId(long depID) throws  BaseException;

}
