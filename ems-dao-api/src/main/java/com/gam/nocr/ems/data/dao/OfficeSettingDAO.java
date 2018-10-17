package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;

public interface OfficeSettingDAO extends EmsBaseDAO<OfficeSettingTO>{

	/**
	 * @author asus-pc: Madanipour
	 * @param depID
	 * @param uploadPhoto
	 * @throws DAOException 
	 * @throws BaseException 
	 */
	OfficeSettingTO findByOfficeId(long depID) throws  BaseException;

	OfficeSettingTO findById(Long id) throws BaseException;
}
