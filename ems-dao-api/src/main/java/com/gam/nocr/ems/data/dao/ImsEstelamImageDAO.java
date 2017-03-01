package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ImsEstelamImageTO;
import com.gam.nocr.ems.data.enums.ImsEstelamImageType;

public interface ImsEstelamImageDAO extends EmsBaseDAO<ImsEstelamImageTO> {

	List<ImsEstelamImageTO> findImsImageByNationalIdAndType(String nationalId,
			ImsEstelamImageType imsImageType) throws BaseException;
	
	ImsEstelamImageTO findImsImageByNationalID(String nationalId) throws BaseException;

}
