package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ExceptionCodeTO;

public interface ExceptionCodeDAO extends EmsBaseDAO<ExceptionCodeTO> {
	
	
	List<String> getAll() throws BaseException ;
	

}
