package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.IMSCitizenInfoTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSCitizenInfoDAO extends EmsBaseDAO<IMSCitizenInfoTO> {

	/**
	 * The method findByNationalId is used to find an instance of type {@link IMSCitizenInfoTO} with a specified nationalId
	 * @param nationalId is an instance of type {@link Long}, which represents a specified nationalId
	 * @return an instance of type {@link IMSCitizenInfoTO}
	 * @throws BaseException
	 */
	IMSCitizenInfoTO findByNationalId(Long nationalId) throws BaseException;

}
